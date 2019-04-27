package com.shunlian.app.shunlianyoupin;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shunlian.app.utils.LogUtil;

/**
 * Created by Administrator on 2019/4/9.
 */

public class MyWebView extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            webview.loadUrl(url);
            return true;
        }
        public void onPageFinished(WebView view, String url) {
            CookieManager cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie(url);
            LogUtil.testLogW("Cookies", "Cookies = " + cookie);
            CookieSyncManager.getInstance().sync();
            //将cookie存到数据库
            if (!TextUtils.isEmpty(cookie)) {
                int token = cookie.indexOf("token");
                cookie.substring(token, cookie.length());
                int indexOf = cookie.indexOf(";");
                cookie.substring(0,indexOf);
            }
            super.onPageFinished(view, url);
        }

}
