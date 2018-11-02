package com.shunlian.app.utils.download;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.shunlian.app.service.ApiService;
import com.shunlian.app.service.InterentTools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


/**
 1. Description: 下载工具类
 */
public class DownloadUtils {

    private static final String TAG = "DownloadUtils";

    private static final int DEFAULT_TIMEOUT = 15;

    private Retrofit retrofit;

    private JsDownloadListener listener;

    private String baseUrl;

    private String downloadUrl;

    private boolean isCancel = false;

    public DownloadUtils(JsDownloadListener listener) {
        this.baseUrl = baseUrl;
        this.listener = listener;

        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(listener);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(InterentTools.HTTPADDR)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public void setCancel(boolean isCancel){
        this.isCancel = isCancel;
    }
    /**
     * 开始下载
     *
     * @param url
     * @param filePath
     */
    public void download( String url,  String filePath) {
        listener.onStartDownload();
        Call<ResponseBody> service = retrofit.create(ApiService.class)
                .download(url);
        service.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                new Thread(() ->   writeFileFromIS(new File(filePath), response.body().byteStream(), response.body().contentLength(), listener)).start();
              ;
                Log.d("下载：","下载完成");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private Handler handerProgress = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case  1:
                    listener.onProgress(msg.arg1);
                    break;
                case  2:
                    listener.onFinishDownload((String) msg.obj,isCancel);
                    break;
                case  3:
                    listener.onFail("下载失败");
                    break;
                 default:
                        break;
            }
        }
    };
    private static int sBufferSize = 8192;

    //将输入流写入文件
    private  void writeFileFromIS(File file, InputStream is, long totalLength, JsDownloadListener downloadListener) {
        //开始下载
        downloadListener.onStartDownload();

        //创建文件
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                downloadListener.onFail("createNewFile IOException");
            }
        }
        OutputStream os = null;
        long currentLength = 0;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[sBufferSize];
            int len;
            while ((len = is.read(data, 0, sBufferSize)) != -1&&!isCancel) {
                os.write(data, 0, len);
                currentLength += len;
                //计算当前下载进度
                Message msg =handerProgress.obtainMessage();
                msg.what=1;
                msg.arg1 = (int) (100 * currentLength / totalLength);
                handerProgress.sendMessage(msg);
                downloadListener.onProgress((int) (100 * currentLength / totalLength));
            }
            //下载完成，并返回保存的文件路径
            Message msgfinfish = handerProgress.obtainMessage();
            msgfinfish.what=2;
            msgfinfish.obj = file.getAbsolutePath();
            handerProgress.sendMessage( msgfinfish);
        } catch (IOException e) {
            e.printStackTrace();
            Message msgfail = handerProgress.obtainMessage();
            msgfail.what=3;
            handerProgress.sendMessage( msgfail);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
