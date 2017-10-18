package com.shunlian.app.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


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

/**
 * Created by zhang on 2017/4/15 16 : 41.
 */

public class HttpRequestHeader implements Interceptor {

    private final boolean isRemoveContentType;

    public HttpRequestHeader(boolean isRemoveContentType) {
        this.isRemoveContentType = isRemoveContentType;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder()
                .addHeader("User-Agent", SharedPrefUtil.getSharedPrfString("User-Agent", "Shunlian Android 5.1.1/1.0.0"))
                .addHeader("X-Device-ID", SharedPrefUtil.getSharedPrfString("X-Device-ID", "744D9FC3-5DBD-3EDD-A589-56D77BDB0E5D"))
                .addHeader("resolution", SharedPrefUtil.getSharedPrfString("resolution", "720x1184"))
                .addHeader("X-Ip", SharedPrefUtil.getSharedPrfString("DeviceIp", "192.168.1.1"))
                .addHeader("Content-Type", "application/json");
        if (isRemoveContentType) {
            builder.removeHeader("Content-Type");
        }
        return chain.proceed(builder.build());
    }
}