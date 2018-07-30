package com.shunlian.app.utils;
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

import android.text.TextUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by zhang on 2017/4/14 09 : 57.
 */
public class ReceivedCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的cookie
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            List<String> headers = originalResponse.headers("Set-Cookie");
            for (int i = 0; i < headers.size(); i++) {
                String s = headers.get(i);
                String[] split = s.split(";");
                if (!TextUtils.isEmpty(split[0])){
                    cookieBuffer.append(split[0]).append("; ");
                }
            }
            cookieBuffer.append("path=/;");
            cookieBuffer.append("domain=api.shunliandongli.com");
            LogUtil.augusLogW("yxf-pp-"+ cookieBuffer.toString());
            SharedPrefUtil.saveSharedUserString("cookie", cookieBuffer.toString());
        }
        return originalResponse;
    }

//    /*
//   解析cookie
//    */
//    protected static String[] analysisCookie(Header[] headers) {
//        String[] cookies = new String[6];
//        printHeaders(headers);
//        if (headers != null) {
//            // 遍历头信息
//            for (int m = 0; m < headers.length; m++) {
//                Header header = headers[m];
//                //对登录验证码的cookie字符串进行属性提取
//                if (header.getName().equals("Set-Cookie")) {
//                    String[] out = header.getValue().split(";");
//                    for (int n = 0; n < out.length; n++) {
//                        String[] in = out[n].split("=");
//                        for (int p = 0; p < in.length; p++) {
//                            if (in[p].equals("api_pin")) {
//                                cookies[0] = in[p + 1];
//                            } else if (in[p].equals("api_wskey")) {
//                                cookies[1] = in[p + 1];
//                            } else if (in[p].equals(" path")) {
//                                cookies[2] = in[p + 1];
//                            } else if (in[p].equals(" domain")) {
//                                cookies[3] = in[p + 1];
//                            } else if (in[p].equals("api_utj")) {
//                                cookies[4] = in[p + 1];
//                                BaseApplication myApp = (BaseApplication) BaseApplication.getContext();
//                                myApp.edUserInfo.putString("api_utj", cookies[4]).apply();
//                            } else if (in[p].equals("api_utjs")) {
//                                cookies[5] = in[p + 1];
//                                BaseApplication myApp = (BaseApplication) BaseApplication.getContext();
//                                myApp.edUserInfo.putString("api_utjs", cookies[5]).apply();
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return cookies;
//    }
}
