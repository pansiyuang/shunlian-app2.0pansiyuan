package com.shunlian.app.shunlianyoupin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;

import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2019/4/4.
 */

public class ShunlianyoupinMystoreFrag extends MyX5Frag {
//    @BindView(R.id.webview)
//    WebView webView;
//    @BindView(R.id.progressbar)
//    ProgressBar progressbar;
//    private String url;
    public static BaseFragment getInstance(String h5Url, int mode, String loginVerion) {
        ShunlianyoupinMystoreFrag fragment = new ShunlianyoupinMystoreFrag();
        Bundle args = new Bundle();
        try {
            if (!TextUtils.isEmpty(h5Url))
                h5Url = java.net.URLDecoder.decode(h5Url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        args.putSerializable("h5Url", h5Url);
        args.putSerializable("mode", mode);
        args.putSerializable("loginVerion", loginVerion);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initData() {
        super.initData();

//        url = "http://mt-front.v2.shunliandongli.com/app/personal";
//        addCookie(url);
//        WebSettings webSetting = webView.getSettings();
//        webView.getSettings().setUserAgentString(webSetting.getUserAgentString()
//                + " MengTian Android " + Build.VERSION.RELEASE + "/" + SharedPrefUtil.getCacheSharedPrf("localVersion", "1.0.0"));
//
////        SharedPreferences spf = getActivity().getSharedPreferences("h5token", Context.MODE_PRIVATE);
////                String cookieString = spf.getString("tk", "");
////
////        CookieSyncManager.createInstance(getActivity());
////        CookieManager cookieManager = CookieManager.getInstance();
////        cookieManager.setCookie(url, cookieString);
////        CookieSyncManager.getInstance().sync();
//
//        webView.loadUrl(url);

//        webView.loadUrl("http://mt-front.v2.shunliandongli.com/app/personal");
//        webView.setWebViewClient(new MyWebViewClient());
//        webView.setWebChromeClient(new MyWebChromeClient());
//        webView.setWebChromeClient(new MyWebChromeClient());
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);//允许使用js



    }
    public void onBack() {
        mwv_h5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mwv_h5.canGoBack()) {
                        mwv_h5.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

//    public class MyWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
//            webview.loadUrl(url);
//            return true;
//        }
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//
//        }
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            CookieSyncManager.createInstance(getContext());
//            CookieManager cookieManager = CookieManager.getInstance();
//            String cookie = cookieManager.getCookie(url);
//            LogUtil.testLogW("Cookies", "Cookies = " + cookie);
//            CookieSyncManager.getInstance().sync();
//            //将cookie存到数据库
//            if (!TextUtils.isEmpty(cookie)) {
//                String[] cookies = cookie.split(";");
//                for(int i =0;i<cookies.length;i++){
//                    String item = cookies[i];
//                    if(item.contains("token=")){
//                        int indexOf = item.indexOf("token=");
//                        LogUtil.augusLogW("333token-------"+item.substring(indexOf+6));
////                        LogUtil.testLogW("token-------",item.substring(indexOf+6));
//                        SharedPrefUtil.saveSharedUserString("token",item.substring(indexOf+6));
//                    }
//                }
//            }
//        }
//
//
//    }
////    public class MyWebChromeClient extends WebChromeClient{
//        public void onProgressChanged(WebView view, int newProgress) {
//            if (newProgress == 100) {
//                progressbar.setVisibility(GONE);
//            } else {
//                if (progressbar.getVisibility() == GONE)
//                    progressbar.setVisibility(VISIBLE);
//                progressbar.setProgress(newProgress);
//            }
//            super.onProgressChanged(view, newProgress);
//        }
//
//    }
//    public void addCookie(String url) {
//        String domain = Common.getDomain(url);
//        if (isEmpty(domain))
//            return;
//        String token = SharedPrefUtil.getSharedUserString("token", "");
//
//        String ua = "MengTian Android " + Build.VERSION.RELEASE + "/" + SharedPrefUtil.getCacheSharedPrf("localVersion", "1.0.0");
//        CookieSyncManager.createInstance(getActivity());
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        if (!isEmpty(token)){
//            cookieManager.removeAllCookie();
//            LogUtil.augusLogW("2222token-------"+token);
//            cookieManager.setCookie(domain, "token=" + token);
//        }
//        cookieManager.setCookie(domain, "Client-Type=Android");
//        cookieManager.setCookie(domain, "User-Agent=" + ua);
//        if (Build.VERSION.SDK_INT < 21) {
//            CookieSyncManager.getInstance().sync();
//        } else {
//            CookieManager.getInstance().flush();
//        }
//    }

}

