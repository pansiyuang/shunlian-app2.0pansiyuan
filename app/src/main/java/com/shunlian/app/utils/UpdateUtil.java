package com.shunlian.app.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;

import com.shunlian.app.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by MBENBEN on 2016/7/9 09 : 30.
 * 更新App工具类
 */
public class UpdateUtil {


    /**
     * 下载路径
     */
   /* public static final String DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/sl_download/";*/
//    public static final String DOWNLOAD_PATH = DownloadService.DOWNLOAD_PATH;


    /**
     * 获取本地是否有apk存在
     * 如果存在则返回apk路径，如果不存在返回null
     * @return
     */
    public static String getLocalAPKPath() {

        if (isMountedSD()) {

            File file = new File(App.DOWNLOAD_PATH+ DownloadService.fileName);
            if (!file.exists()){
                //file.mkdirs();
                return null;
            }else{
                return file.getAbsolutePath();
            }


          /*  File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile()){
                    if (f.getName().endsWith(".apk")){
                        return f.getAbsolutePath();
                    }
                }
            }*/
        }
        return null;
    }



    /**
     * 获取文件的md5值
     * @param path 文件路径
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getFileMD5(String path) throws NoSuchAlgorithmException, IOException {

        File file = new File(path);

        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        digest = MessageDigest.getInstance("MD5");
        in = new FileInputStream(file);
        while ((len = in.read(buffer, 0, 1024)) != -1) {
            digest.update(buffer, 0, len);
        }
        in.close();
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }


    /**
     * 判断是否挂载SD卡
     *
     * @return true 挂载
     */
    public static boolean isMountedSD() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 是否开启服务
     */
    public static boolean isStartService(Context context,Class<? extends Service> clazz) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo info : infos){
            ComponentName service = info.service;
            if(service.getClassName().equals(clazz.getName())){
                return true;
            }
        }
        return false;
    }
}
