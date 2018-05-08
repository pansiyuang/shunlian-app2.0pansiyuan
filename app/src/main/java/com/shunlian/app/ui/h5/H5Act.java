package com.shunlian.app.ui.h5;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.bean.H5CallEntity;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MarqueeTextView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.MyWebView;
import com.tencent.sonic.sdk.SonicCacheInterceptor;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;
import com.tencent.sonic.sdk.SonicSessionConnectionInterceptor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.shunlian.app.service.InterentTools.DOMAIN;

/**
 * Created by Administrator on 2017/12/26.
 */

public class H5Act extends BaseActivity implements MyWebView.ScrollListener {
    public static final int MODE_DEFAULT = 0;//默认模式，没有缓存
    public static final int MODE_SONIC = 1;//有缓存
    public static final int MODE_SONIC_WITH_OFFLINE_CACHE = 2;//清除缓存
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    protected String h5Url = "";
    protected String title;
    protected int mode;
    protected SonicSession sonicSession;
    protected HttpDialog httpDialog;
    protected ValueCallback<Uri> uploadMessage;
    protected ValueCallback<Uri[]> uploadMessageAboveL;
    protected Intent mIntent;

    @BindView(R.id.mtv_close)
    public MyTextView mtv_close;

    @BindView(R.id.mar_title)
    public MarqueeTextView mar_title;

    @BindView(R.id.mtv_title)
    public MyTextView mtv_title;

    @BindView(R.id.mwv_h5)
    public MyWebView mwv_h5;

    @BindView(R.id.rl_title_more)
    public RelativeLayout rl_title_more;

    @BindView(R.id.tv_msg_count)
    public TextView tv_msg_count;

    @BindView(R.id.quick_actions)
    public QuickActions quick_actions;

    @BindView(R.id.miv_favorite)
    public MyImageView miv_favorite;

    SonicSessionClientImpl sonicSessionClient = null;
    private boolean isLogin = false;

    public static void startAct(Context context, String url, int mode) {
        Intent intentH5 = new Intent(context, H5Act.class);
        intentH5.putExtra("url", url);
        intentH5.putExtra("mode", mode);
        intentH5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentH5);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mwv_h5.canGoBack()) {
            mwv_h5.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 布局id
     *
     * @return
     */
    protected void jsCallback(H5CallEntity h5CallEntity) {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.mtv_close:
//                if (h5_mwb.canGoBack()) {
//                    h5_mwb.goBack();// 返回前一个页面
//                } else {
//                  finish();
//                }
                finish();
                break;
//            case R.id.layout_backtToUp:
//                h5_mwb.scrollTo(0, 0);
//                break;
        }
    }

    private void setTitle() {
        if (!TextUtils.isEmpty(title) && title.length() > 8) {
            mar_title.setVisibility(View.VISIBLE);
            mtv_title.setVisibility(View.GONE);
            mar_title.setText(title);
        } else {
            mar_title.setVisibility(View.GONE);
            mtv_title.setVisibility(View.VISIBLE);
            mtv_title.setText(title);
        }
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mIntent = getIntent();
        mode = mIntent.getIntExtra("mode", 0);
        h5Url = mIntent.getStringExtra("url");
        if (!isEmpty(h5Url)) {
            initSonic();
        }
        return R.layout.act_h5;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        immersionBar.statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .keyboardEnable(true)
                .init();
        httpDialog = new HttpDialog(this);
        initWebView();
        if (!isEmpty(h5Url)) {
            loadUrl();
        }
    }

    public void initSonic() {
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }

        // if it's sonic mode , startup sonic session at first time
        if (MODE_DEFAULT != mode) { // sonic mode
            SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
            sessionConfigBuilder.setSupportLocalServer(true);

            // if it's offline pkg mode, we need to intercept the session connection
            if (MODE_SONIC_WITH_OFFLINE_CACHE == mode) {
                sessionConfigBuilder.setCacheInterceptor(new SonicCacheInterceptor(null) {
                    @Override
                    public String getCacheData(SonicSession session) {
                        return null; // offline pkg does not need cache
                    }
                });

                sessionConfigBuilder.setConnectionInterceptor(new SonicSessionConnectionInterceptor() {
                    @Override
                    public SonicSessionConnection getConnection(SonicSession session, Intent intent) {
                        return new OfflinePkgSessionConnection(H5Act.this, session, intent);
                    }
                });
            }

            // create sonic session and run sonic flow
            sonicSession = SonicEngine.getInstance().createSession(h5Url, sessionConfigBuilder.build());
            if (null != sonicSession) {
                sonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
            } else {
                // this only happen when a same sonic session is already running,
                // u can comment following codes to feedback as a default mode.
                // throw new UnknownError("create session fail!");
                LogUtil.augusLogW("create sonic session fail!--" + h5Url);
            }
        }
    }

    protected void loadUrl() {
        LogUtil.zhLogW("h5Url====="+h5Url);
        if (!isEmpty(h5Url)) {
            if (h5Url.startsWith(InterentTools.H5_HOST + "special")) {
                visible(rl_title_more);
                rl_title_more.setOnClickListener(v -> {
                    quick_actions.setVisibility(View.VISIBLE);
                    quick_actions.special();
                });
            } else if (h5Url.startsWith(InterentTools.H5_HOST + "activity")) {
                visible(rl_title_more);
                rl_title_more.setOnClickListener(v -> {
                    quick_actions.setVisibility(View.VISIBLE);
                    quick_actions.activity();
                });
            }
        }

        // webview is ready now, just tell session client to bind
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(mwv_h5);
            sonicSessionClient.clientReady();
        } else { // default mode
            if (!isEmpty(h5Url))
                mwv_h5.loadUrl(h5Url, setWebviewHeader());
        }
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    protected void addJs(final String methodName) {
        //如果有多个交互方法，stringName必须取名不一样，否则后写的覆盖前面的
        mwv_h5.addJavascriptInterface(new Object() {
            @JavascriptInterface//Android4.4后每个js交互方法必须要有注解
            public void androidCallback(String param) {
                try {
                    H5CallEntity h5CallEntity = new ObjectMapper().readValue(param, H5CallEntity.class);
                    h5CallEntity.type = methodName;
                    jsCallback(h5CallEntity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, methodName);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    protected void initWebView() {
        WebSettings webSetting = mwv_h5.getSettings();
        webSetting.setAppCachePath(Constant.CACHE_PATH_EXTERNAL);
        webSetting.setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
//        h5_mwb.removeJavascriptInterface("searchBoxJavaBridge_");
//        h5_mwb.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, getIntent()), "sonic");
        webSetting.setAppCacheEnabled(true);
        //开启DOM缓存，关闭的话H5自身的一些操作是无效的
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowContentAccess(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setSavePassword(false);
        webSetting.setSaveFormData(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);

        mwv_h5.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.zhLogW("=onPageStarted======="+url);
                if (!isFinishing() && httpDialog != null) {
                    httpDialog.show();
                }
                if (mwv_h5 != null) {
                    mwv_h5.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.zhLogW("=onPageFinished======="+url);
                title = view.getTitle();
                setTitle();
                if (!isFinishing() && httpDialog != null && httpDialog.isShowing()) {
                    httpDialog.dismiss();
                }
                if (sonicSession != null) {
                    sonicSession.getSessionClient().pageFinish(url);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                LogUtil.zhLogW("=error======="+error.getPrimaryError());
                handler.proceed();//接受证书
                if (!isFinishing() && httpDialog != null && httpDialog.isShowing()) {
                    httpDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (mwv_h5 != null) {
                    mwv_h5.setVisibility(View.INVISIBLE);
                    Common.staticToast("网络连接错误，请稍后再试");
                }
                if (!isFinishing() && httpDialog != null && httpDialog.isShowing()) {
                    httpDialog.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.httpLogW("========h5Url==========" + h5Url);
                if (url.contains("slmall://")) {
                    analysisUrl(url);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (sonicSession != null) {
                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
                }
                return super.shouldInterceptRequest(view, url);
            }


        });
        mwv_h5.setWebChromeClient(new WebChromeClient() {

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }
        });
        addCookie();
        mwv_h5.getSettings().setUserAgentString(SharedPrefUtil
                .getSharedPrfString("User-Agent", "Shunlian Android 1.1.1/0.0.0"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isLogin) {
            addCookie();
            mwv_h5.reload();
        }
    }


    public void addCookie() {
        //add
        String token = SharedPrefUtil.getSharedPrfString("token", "");
        String ua = SharedPrefUtil.getSharedPrfString("User-Agent", "Shunlian Android 4.0.0/1.0.0");

        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();

        cookieManager.setCookie(DOMAIN, "Client-Type=Android");
        cookieManager.setCookie(DOMAIN, "token=" + token);
        cookieManager.setCookie(DOMAIN, "User-Agent=ShunLian" + ua);
        cookieSyncManager.sync();
        //end
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSession = null;
        }
        if (null != sonicSessionClient) {
            sonicSessionClient.destroy();
            sonicSessionClient = null;
        }
        super.onDestroy();
    }

    private void analysisUrl(String url) {
        if (url.equals(h5Url)) {
            LogUtil.httpLogW("=====analysisUrl=====h5Url==========" + h5Url);
            return;
        }
        LogUtil.httpLogW("链接:" + url);
        if (url.startsWith("slmall://")) {
            String type = interceptBody(url);
            if (!TextUtils.isEmpty(type)) {
                if ("login".equals(type))
                    isLogin = true;
                String id = "";
                String id1 = "";
                if (!TextUtils.isEmpty(Common.getURLParameterValue(url, "id")))
                    id = interceptId(url);
                if (!TextUtils.isEmpty(Common.getURLParameterValue(url, "id1")))
                    id1 = interceptId(url);
                Common.goGoGo(H5Act.this, type, id, id1);
            }
        }
    }

    /**
     * 完整url slmall://goods/item.json?goodsId=138471
     * 截取之后goods/item.json
     *
     * @param url
     * @return
     */
    private String interceptBody(String url) {
        String[] split = url.split("\\?");
        String s = split[0];
        if (!TextUtils.isEmpty(s)) {
            String[] split1 = s.split("//");
            if (!TextUtils.isEmpty(split1[1])) {
                return split1[1];
            }
        }
        return null;
    }

    /**
     * 截取商品id
     *
     * @param url
     * @return
     */
    private String interceptId(String url) {
        String[] split = url.split("\\?");
        String s = split[1];
        String[] split1 = s.split("=");
        String s1 = split1[1];
        /*if (s1.matches("[0-9]+")){
            return s1;
        }*/
        return s1;
    }

    @Override
    public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
//        if (y > 2000) {
//            layout_backtToUp.setVisibility(View.VISIBLE);
//        } else {
//            layout_backtToUp.setVisibility(View.GONE);
//        }
    }

    private static class OfflinePkgSessionConnection extends SonicSessionConnection {

        private final WeakReference<Context> context;

        public OfflinePkgSessionConnection(Context context, SonicSession session, Intent intent) {
            super(session, intent);
            this.context = new WeakReference<Context>(context);
        }

        @Override
        protected int internalConnect() {
            Context ctx = context.get();
            if (null != ctx) {
                try {
                    InputStream offlineHtmlInputStream = ctx.getAssets().open("sonic-demo-index.html");
                    responseStream = new BufferedInputStream(offlineHtmlInputStream);
                    return SonicConstants.ERROR_CODE_SUCCESS;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return SonicConstants.ERROR_CODE_UNKNOWN;
        }

        @Override
        protected BufferedInputStream internalGetResponseStream() {
            return responseStream;
        }

        @Override
        public void disconnect() {
            if (null != responseStream) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getResponseCode() {
            return 200;
        }

        @Override
        public Map<String, List<String>> getResponseHeaderFields() {
            return new HashMap<>(0);
        }

        @Override
        public String getResponseHeaderField(String key) {
            return "";
        }
    }
}
