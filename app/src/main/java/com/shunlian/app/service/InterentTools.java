package com.shunlian.app.service;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

import android.os.Environment;

import com.shunlian.app.utils.AddCookiesInterceptor;
import com.shunlian.app.utils.HttpRequestHeader;
import com.shunlian.app.utils.ReceivedCookiesInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by zhang on 2017/4/13 16 : 15.
 */

public final class InterentTools {
    public static final String CACHE_PATH_EXTERNAL = Environment.getExternalStorageDirectory().getAbsolutePath() +
            "/Android/data/com.shunlian.app/ShunLian/Cache/";
//    public final static String HTTPADDR = "https://api.shunliandongli.com/v1/";
//        public final static String HTTPADDR = "https://api-test.shunliandongli.com/v1/";//测试接口
        public final static String HTTPADDR = "http://v20-front-api.shunliandongli.com/";//测试接口
    public final static String DOMAIN = "v20-wx.shunliandongli.com";//webView的cookie中的domain，即h5的域名

    /**********h5域名******************/
    public final static String H5_HOST = "http://v20-wx.shunliandongli.com/";

    private static OkHttpClient.Builder okHttpBuilder;
    private static Retrofit retrofit;

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private InterentTools(){
    }

    public static final class Builder {

        private final HttpLoggingInterceptor loggingInterceptor;

        public Builder() {
            loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            okHttpBuilder = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true);//失败重发
        }

        /**
         * 是否开启okhttp日志
         *
         * @param flag
         * @return
         */
        public Builder isOpenLogging(boolean flag) {
            if (flag) {
                okHttpBuilder.addInterceptor(loggingInterceptor);
            }
            return this;
        }

        /**
         * 设置超时时间30秒
         *
         * @return
         */
        public Builder connectTimeout() {
            okHttpBuilder.connectTimeout(30, TimeUnit.SECONDS);
            return this;
        }

        /**
         * 设置保存cookie
         *
         * @return
         */
        public Builder addReceivedCookiesInterceptor() {
            okHttpBuilder.addInterceptor(new ReceivedCookiesInterceptor());
            return this;
        }

        /**
         * 设置添加cookie
         *
         * @return
         */
        public Builder addCookiesInterceptor() {
            okHttpBuilder.addInterceptor(new AddCookiesInterceptor());
            return this;
        }

        public Builder addHeaderInterceptor(){
            okHttpBuilder.addNetworkInterceptor(new HttpRequestHeader(false));
            return this;
        }

        public Builder removeHeaderContentType(){
            okHttpBuilder.addNetworkInterceptor(new HttpRequestHeader(true));
            return this;
        }

        /**
         * 手动设置超时秒数
         *
         * @param timeout
         * @return
         */
        public Builder connectTimeout(long timeout) {
            okHttpBuilder.connectTimeout(timeout, TimeUnit.SECONDS);
            return this;
        }

        /**
         * 手动设置超时时间
         *
         * @param timeout
         * @param utils
         * @return
         */
        public Builder connectTimeout(long timeout, TimeUnit utils) {
            okHttpBuilder.connectTimeout(timeout, utils);
            return this;
        }


        public InterentTools build() {
            retrofit = new Retrofit.Builder()
                    .client(okHttpBuilder.build())
                    .baseUrl(HTTPADDR)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
            return new InterentTools();
        }
    }
}
