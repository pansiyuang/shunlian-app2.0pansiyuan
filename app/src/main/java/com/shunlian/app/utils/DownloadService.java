package com.shunlian.app.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.shunlian.app.App;
import com.shunlian.app.R;
import com.shunlian.app.service.ApiService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class DownloadService extends Service {
    public static final String TAG = "download";
    public static final String fileName = "shunlian.apk";//文件名
    private static final int LOACL_INSTALL = 4;//本地安装
    private static final int MSG_INIT = 0;
    private static final int URL_ERROR = 1;
    private static final int NET_ERROR = 2;
    private static final int DOWNLOAD_SUCCESS = 3;
    private static final int UPDATE_PROGRESS = 5;
    private static final int NET_UNSTEADILY = 6;//网络不稳定
    private String url;//下载链接
    private Notification notification;
    private RemoteViews contentView;
    private NotificationManager notificationManager;
    /**
     * 是否正在下载
     */
    private boolean isDownlaoding = false;

    private Handler mHandler = new Handler() {


        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    int length;//文件长度
                    length = (int) msg.obj;
                    if (!isDownlaoding) {
                        if (Common.hasSD()){
                           if ( Common.getSDFreeSize()>length){
                               new DownloadThread(url, length).start();
                               if (listener != null) {
                                   listener.onStart();
                               }
                               createNotification();
                           }else {
                               Toast.makeText(DownloadService.this,"SD卡空间不足",Toast.LENGTH_SHORT).show();
                           }
                        }else{
                            if ( Common.getMemoryFreeSize(DownloadService.this)>length){
                                new DownloadThread(url, length).start();
                                if (listener != null) {
                                    listener.onStart();
                                }
                                createNotification();
                            }else {
                                Toast.makeText(DownloadService.this,"系统空间不足",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                case LOACL_INSTALL:
                    String localAPKPath = (String) msg.obj;
                    installApk(DownloadService.this, new File(localAPKPath));
                    break;

                case UPDATE_PROGRESS:
                    long progress = (long) msg.obj;
                    break;
                case DOWNLOAD_SUCCESS:
                    //下载完成
                    if (listener != null) {
                        listener.downloadSuccess();
                    }
                    notifyNotification(100, 100);
                    isDownlaoding = false;
                    //下载完成后，取消下载进度通知
                    notificationManager.cancel(R.layout.notification_item);
                    installApk(DownloadService.this, new File(App.DOWNLOAD_PATH, fileName));
//                    Toast.makeText(DownloadService.this, "下载完成", Toast.LENGTH_SHORT).show();
                    break;
                case URL_ERROR:
                    isDownlaoding = false;
//                    Toast.makeText(DownloadService.this, "下载地址错误", Toast.LENGTH_LONG).show();
                    showToast("下载地址错误");
                    if (listener != null) {
                        listener.onError();
                    }
                    break;
                case NET_UNSTEADILY:
                    isDownlaoding = false;
//                    Toast.makeText(DownloadService.this, "当前网络不稳定，请稍后再试！", Toast.LENGTH_LONG).show();
                    showToast("网络状况差，请重试！");
                    if (listener != null) {
                        listener.onError();
                    }
                    break;
                case NET_ERROR:
                    isDownlaoding = false;
//                    Toast.makeText(DownloadService.this, "连接失败，请检查网络设置", Toast.LENGTH_LONG).show();
                    showToast("连接失败，请检查网络设置");
                    if (listener != null) {
                        listener.onError();
                    }
                    break;
            }
        }
    };

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件
     */
    public static void installApk(Context context, File file) {
        Constant.IS_DOWNLOAD = false;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }

        if (listener != null){
            listener.installAPK();
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            url = intent.getStringExtra("url");
            String fileMd5 = intent.getStringExtra("fileMd5");
            if (url != null && !TextUtils.isEmpty(url)) {
                /**
                 * 判断本地是否有下载好的apk
                 */
                String localAPKPath = UpdateUtil.getLocalAPKPath();
                if (TextUtils.isEmpty(localAPKPath)) {
                    //本地没有下载好的apk
                    //需要联网从服务器下载
//                    System.out.println("本地没有下载好的apk");
                    new InitThread(url).start();
                    if (listener != null) {
                        listener.onStart();
                    }
                } else {
                    //存在apk,隐藏下载进度条
                    //获取该apk的MD5值，判断该apk是否和服务器的一样
                    try {
                        String fileMD5 = UpdateUtil.getFileMD5(localAPKPath);
//                        System.out.println("fileMD5="+fileMD5+" ;Md5="+fileMd5);
                        /**
                         * 判断该apk是否和服务器的一样
                         */
                        if (fileMd5.equals(fileMD5)) {
                            //直接安装
                            if (listener != null) {
                                listener.isLoaclAPK(true);
                            }
//                          installApk(DownloadService.this, new File(localAPKPath));
                            Message msg = Message.obtain();
                            msg.obj = localAPKPath;
                            msg.what = LOACL_INSTALL;
                            mHandler.sendMessageDelayed(msg,100);
                        } else {
                            //不是最新apk，删除该文件，重新下载
                            File file = new File(localAPKPath);
                            file.delete();
                            new InitThread(url).start();
                            if (listener != null) {
                                listener.onStart();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("更新出现异常:"+e.getMessage());
                    }
                }
            } else {
                mHandler.sendEmptyMessage(URL_ERROR);
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressWarnings("deprecation")
    public void createNotification() {
        notification = new Notification(
                R.mipmap.ic_launcher,//应用的图标
                "安装包正在下载...",
                System.currentTimeMillis());
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        //notification.flags = Notification.FLAG_AUTO_CANCEL;

        /*** 自定义  Notification 的显示****/
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setProgressBar(R.id.progress, 100, 0, false);
        contentView.setTextViewText(R.id.tv_progress, "0%");
        
        notification.contentView = contentView;

        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //设置notification的PendingIntent
        notificationManager.notify(R.layout.notification_item, notification);

    }

    private void notifyNotification(long percent, long length) {
        contentView.setTextViewText(R.id.tv_progress, (percent * 100 / length) + "%");
        contentView.setProgressBar(R.id.progress, (int) length, (int) percent, false);
        notification.contentView = contentView;
        notificationManager.notify(R.layout.notification_item, notification);
    }


    /**
     * 初始化子线程
     *
     * @author dong
     */
    class InitThread extends Thread {
        String url = "";

        public InitThread(String url) {
            this.url = url;
        }

        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            try {
                //连接网络文件
                URL url = new URL(this.url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(30*1000);//设置连接超时时间为30秒
                conn.setReadTimeout(30*1000);
                conn.setRequestMethod("GET");
                int length = -1;
                if (conn.getResponseCode() == ApiService.SC_OK) {
                    //获得文件长度
                    length = conn.getContentLength();
                }
                if (length <= 0) {
                    return;
                }
                File dir = new File(App.DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                if (listener != null) {
                    listener.maxProgress(length);
                }
                File file = new File(dir, fileName);
                raf = new RandomAccessFile(file, "rwd");
                //设置文件长度
                raf.setLength(length);
                mHandler.obtainMessage(MSG_INIT, length).sendToTarget();
            } catch (Exception e) {
                mHandler.sendEmptyMessage(NET_UNSTEADILY);
                e.printStackTrace();
            } finally {
                try {
                    conn.disconnect();
                    raf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载线程
     *
     * @author dong
     */
    class DownloadThread extends Thread {
        String url;
        int length;

        public DownloadThread(String url, int length) {
            this.url = url;
            this.length = length;
            isDownlaoding = true;
//            Toast.makeText(DownloadService.this, "下载中...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream input = null;
            try {
                URL url = new URL(this.url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(30*1000);
                conn.setReadTimeout(30*1000);
                conn.setRequestMethod("GET");
                //设置下载位置
                int start = 0;
                conn.setRequestProperty("Range", "bytes=" + 0 + "-" + length);
                //设置文件写入位置
                File file = new File(App.DOWNLOAD_PATH,fileName);
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                long mFinished = 0;
                //开始下载
                if (conn.getResponseCode() == ApiService.SC_PARTIAL_CONTENT || conn.getResponseCode() == ApiService.SC_OK) {
                    //LogUtil.i("下载开始了。。。");
                    //读取数据
                    input = conn.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len;
                    long speed = 0;
                    long time = System.currentTimeMillis();
                    while ((len = input.read(buffer)) != -1) {
                        //写入文件
                        raf.write(buffer, 0, len);
                        //把下载进度发送广播给Activity
                        mFinished += len;
                        speed += len;
                        if (System.currentTimeMillis() - time > 200) {
                            time = System.currentTimeMillis();
                            if (listener != null) {
                                listener.progress(mFinished);
                                listener.maxProgress(length);
                            }
                            notifyNotification(mFinished, length);
                            Message msg = Message.obtain();
                            msg.what = UPDATE_PROGRESS;
                            msg.obj = mFinished;
                            mHandler.sendMessage(msg);
                            Log.i(TAG, "mFinished==" + mFinished);
                            Log.i(TAG, "length==" + length);
                            Log.i(TAG, "speed==" + speed);
                            speed = 0;

                        }
                    }
                    mHandler.sendEmptyMessageDelayed(DOWNLOAD_SUCCESS, 1000);
                    Log.i(TAG, "下载完成了。。。");
                } else {
                    Log.i(TAG, "下载出错了。。。");
                    mHandler.sendEmptyMessage(NET_ERROR);
                }

            }catch(ProtocolException e){
                e.printStackTrace();//http已经建立连接
                mHandler.sendEmptyMessage(NET_UNSTEADILY);
            }catch(FileNotFoundException e){
                e.printStackTrace();//文件没有找到异常
                mHandler.sendEmptyMessage(NET_UNSTEADILY);
            } catch (IOException e) {
                e.printStackTrace();//IO异常
                mHandler.sendEmptyMessage(NET_UNSTEADILY);
            } finally {
                try {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (raf != null) {
                        raf.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(NET_UNSTEADILY);
                }

            }
        }
    }

    private static OnProgressChangeListener listener;

    public static void setOnProgressChangeListener(OnProgressChangeListener l) {
        listener = l;
    }


    public interface OnProgressChangeListener {
        /**
         * 进度最大值
         *
         * @param max
         */
        void maxProgress(float max);

        /**
         * 当前进度
         *
         * @param progress
         */
        void progress(float progress);

        /**
         * 下载成功
         */
        void downloadSuccess();

        /**
         * 是否是本地apk
         * @param is
         */
        void isLoaclAPK(boolean is);

        /**
         * 安装apk
         */
        void installAPK();

        void onError();
        void onStart();
    }


    public void showToast(String msg){
        Toast mToast = Toast.makeText(DownloadService.this, msg, Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }

}
