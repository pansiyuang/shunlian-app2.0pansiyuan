package com.shunlian.app.ui.h5;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.webkit.JavascriptInterface;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.bean.H5CallEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.download.DownLoadDialogProgress;
import com.shunlian.app.utils.download.DownloadUtils;
import com.shunlian.app.utils.download.JsDownloadListener;
import com.shunlian.app.widget.MarqueeTextView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ObtainGoldenEggsTip;
import com.shunlian.app.widget.WebViewProgressBar;
import com.shunlian.app.widget.X5WebView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.wxapi.WXEntryActivity;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
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

//import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

/**
 * Created by Administrator on 2017/12/26.
 */

public class H5X5Act extends BaseActivity implements X5WebView.ScrollListener {
    public static final int MODE_DEFAULT = 0;//默认模式，没有缓存
    public static final int MODE_SONIC = 1;//有缓存
    public static final int MODE_SONIC_WITH_OFFLINE_CACHE = 2;//清除缓存
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    @BindView(R.id.mtv_close)
    public MyTextView mtv_close;
    @BindView(R.id.mar_title)
    public MarqueeTextView mar_title;
    @BindView(R.id.mtv_title)
    public MyTextView mtv_title;
    @BindView(R.id.mwv_h5)
    public X5WebView mwv_h5;
    @BindView(R.id.rl_title_more)
    public RelativeLayout rl_title_more;
    @BindView(R.id.rl_title)
    public RelativeLayout rl_title;
    @BindView(R.id.tv_msg_count)
    public TextView tv_msg_count;
    @BindView(R.id.quick_actions)
    public QuickActions quick_actions;
    @BindView(R.id.miv_favorite)
    public MyImageView miv_favorite;
    @BindView(R.id.miv_close)
    public MyImageView miv_close;
    @BindView(R.id.mProgressbar)
    public WebViewProgressBar mProgressbar;
    @BindView(R.id.miv_title_more)
    public MyImageView miv_title_more;
    @BindView(R.id.oget)
    protected ObtainGoldenEggsTip oget;

    protected String h5Url = "", beforeUrl = "", member_id = "";
    protected String title, flag;
    protected int mode;
    protected SonicSession sonicSession;
    //    protected HttpDialog httpDialog;
    protected ValueCallback<Uri> uploadMessage;
    protected ValueCallback<Uri[]> uploadMessageAboveL;
    protected Intent mIntent;
    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    SonicSessionClientImplX sonicSessionClient = null;
    private boolean isContinue = false;
    private H5CallEntity h5CallEntity;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            dealJs();
            super.handleMessage(msg);
        }
    };

    public static void startAct(Context context, String url, int mode) {
        Intent intentH5 = new Intent(context, H5X5Act.class);
        try {
            if (!TextUtils.isEmpty(url))
                url = java.net.URLDecoder.decode(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intentH5.putExtra("url", url);
        intentH5.putExtra("mode", mode);
        intentH5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentH5);
    }

    public static void startAct(Context context, String url, int mode, String flag) {
        Intent intentH5 = new Intent(context, H5X5Act.class);
        try {
            if (!TextUtils.isEmpty(url))
                url = java.net.URLDecoder.decode(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intentH5.putExtra("flag", flag);
        intentH5.putExtra("url", url);
        intentH5.putExtra("mode", mode);
        intentH5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentH5);
    }

    public void dealJs() {
        LogUtil.augusLogW("yxftest--" + h5CallEntity.toString());
        ShareInfoParam shareInfoParam = new ShareInfoParam();
        switch (h5CallEntity.origin) {
            case "shareImage":
                shareInfoParam.photo = h5CallEntity.contentUrl;
                break;
            case "shareWebpage":
                shareInfoParam.shareLink = h5CallEntity.contentUrl;
                shareInfoParam.title = h5CallEntity.title;
                shareInfoParam.desc = h5CallEntity.description;
                shareInfoParam.img = h5CallEntity.thumb;
                break;
            case "saveVideo":
                if(!TextUtils.isEmpty(h5CallEntity.contentUrl)) {
                    downFileStart(h5CallEntity.contentUrl);
                 }
                return;
            case "saveImage":
                try{
                if (!h5CallEntity.contentUrl.contains(";base64,")) {
                    GlideUtils.getInstance().savePicture(baseAct, h5CallEntity.contentUrl);
                } else {
                    byte[] b = Base64.decode(h5CallEntity.contentUrl.split(",")[1], Base64.DEFAULT);
                    if (b != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                        BitmapUtil.saveImageToAlbumn(baseAct, bitmap, false, false);
                        Common.staticToasts(baseAct, "已保存到手机相册", R.mipmap.icon_common_duihao);
                    } else {
                        Common.staticToasts(baseAct, "保存失败", R.mipmap.icon_common_tanhao);
                    }
                }
                }catch (Exception e){
                    Common.staticToasts(baseAct, "保存失败", R.mipmap.icon_common_tanhao);
                }
                return;
        }
        share(shareInfoParam, h5CallEntity.scene);
    }

    private DownLoadDialogProgress downLoadDialogProgress;
    private DownloadUtils downloadUtils;
    private void downFileStart(String currentUrl){
        downloadUtils = new DownloadUtils(new JsDownloadListener() {
            @Override
            public void onStartDownload() {
            }
            @Override
            public void onProgress(int progress) {
                downLoadDialogProgress.showProgress(progress);
            }
            @Override
            public void onFinishDownload(String filePath,boolean isCancel) {
                if(!isCancel)
                    downLoadDialogProgress.downLoadSuccess();
            }
            @Override
            public void onFail(String errorInfo) {
                downLoadDialogProgress.dissMissDialog();
            }
            @Override
            public void onFinishEnd() {
            }
        });
        boolean checkState = downloadUtils.checkDownLoadFileExists(currentUrl);
        if (checkState) {
            Common.staticToast("已下载过该视频,请勿重复下载!");
            return;
        }
        downLoadDialogProgress.showDownLoadDialogProgress(this, new DownLoadDialogProgress.downStateListen() {
            @Override
            public void cancelDownLoad() {
                downloadUtils.setCancel(true);
            }

            @Override
            public void fileDownLoad() {
                downloadUtils.download(currentUrl,downloadUtils.fileName);
            }
        }, !NetworkUtils.isWifiConnected(this));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mwv_h5.canGoBack()) {
            mwv_h5.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void share(ShareInfoParam shareInfoParam, String type) {
        if ("session".equals(type)) {
            WXEntryActivity.startAct(this, "shareFriend", shareInfoParam);
        } else {
            WXEntryActivity.startAct(this, "shareCircle", shareInfoParam);
        }
    }

    /**
     * 布局id
     *
     * @return
     */
    protected void jsCallback(H5CallEntity h5CallEntity) {
        if (h5CallEntity != null && !isEmpty(h5CallEntity.origin)) {
            this.h5CallEntity = h5CallEntity;
            handler.sendMessage(new Message());
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_close.setOnClickListener(this);
        miv_close.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.mtv_close:
                finish();
                break;
            case R.id.miv_close:
                if (mwv_h5.canGoBack()) {
                    mwv_h5.goBack();// 返回前一个页面
                } else {
                    finish();
                }
                break;
//            case R.id.layout_backtToUp:
//                h5_mwb.scrollTo(0, 0);
//                break;
        }
    }

    private void setTitle() {
        if (!TextUtils.isEmpty(title) && title.length() > 8) {
            visible(mar_title);
            gone(mtv_title);
            if (mar_title != null)
            mar_title.setText(title);
        } else {
            gone(mar_title);
            visible(mtv_title);
            if (mtv_title != null)
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
        return R.layout.act_h5x;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        member_id = SharedPrefUtil.getSharedUserString("member_id", "");
        mIntent = getIntent();
        flag = mIntent.getStringExtra("flag");
        downLoadDialogProgress = new DownLoadDialogProgress();
        if (!isEmpty(flag) && "noTitle".equals(flag)) {
            rl_title.setVisibility(View.GONE);
        }
        immersionBar.statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f).statusBarView(R.id.view_status_bar)
                .keyboardEnable(true)
                .init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


//        httpDialog = new HttpDialog(this);
        mode = mIntent.getIntExtra("mode", 0);
        h5Url = mIntent.getStringExtra("url");
        if (!isEmpty(h5Url))
            beforeUrl = h5Url;
        if (!isEmpty(h5Url)) {
            initSonic();
        }
        initWebView();
        loadUrl();
        addJs("JSWXShare");
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
                        return new OfflinePkgSessionConnection(H5X5Act.this, session, intent);
                    }
                });
            }

            // create sonic session and run sonic flow
            sonicSession = SonicEngine.getInstance().createSession(h5Url, sessionConfigBuilder.build());
            if (null != sonicSession) {
                sonicSession.bindClient(sonicSessionClient = new SonicSessionClientImplX());
            } else {
                // this only happen when a same sonic session is already running,
                // u can comment following codes to feedback as a default mode.
                // throw new UnknownError("create session fail!");
                LogUtil.augusLogW("create sonic session fail!--" + h5Url);
            }
        }
    }

    protected void loadUrl() {
        LogUtil.zhLogW("h5Url=====" + h5Url);
        // webview is ready now, just tell session client to bind
        if (!isEmpty(h5Url)) {
            if (sonicSessionClient != null) {
                sonicSessionClient.bindWebView(mwv_h5);
                sonicSessionClient.clientReady();
            } else { // default mode
                mwv_h5.loadUrl(h5Url, setWebviewHeader());
            }
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
//        webSetting.setAppCacheMaxSize(5 * 1024 * 1024);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(Constant.CACHE_PATH_EXTERNAL);
        webSetting.setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
//        h5_mwb.removeJavascriptInterface("searchBoxJavaBridge_");
//        h5_mwb.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, getIntent()), "sonic");
        webSetting.setAppCacheEnabled(true);
        webSetting.setAllowFileAccess(true);
        //开启DOM缓存，关闭的话H5自身的一些操作是无效的
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowContentAccess(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setSavePassword(false);
        webSetting.setSaveFormData(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
//        SensorsDataAPI.sharedInstance().showUpX5WebView(mwv_h5,true);
        //x5新增
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setGeolocationEnabled(true);
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        //x5新增

        mwv_h5.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
                super.onPageStarted(webView, url, bitmap);
                LogUtil.augusLogW("=onPageStarted=======" + url);
//                if (!isFinishing() && httpDialog != null) {
//                    httpDialog.show();
//                }
                if (mwv_h5 != null) {
                    mwv_h5.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                addCookie(url);
                LogUtil.augusLogW("=onPageFinished=======" + url);
                if (!isFinishing()) {
                    if (!isEmpty(view.getTitle())) {
                        title = view.getTitle();
                        setTitle();
                    }
//                if (!isFinishing() && httpDialog != null && httpDialog.isShowing()) {
//                    httpDialog.dismiss();
//                }
                    if (sonicSession != null) {
                        sonicSession.getSessionClient().pageFinish(url);
                    }
                }
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(webView, handler, error);
                LogUtil.augusLogW("=error=======" + error.getPrimaryError());
                handler.proceed();//接受证书
//                if (!isFinishing() && httpDialog != null && httpDialog.isShowing()) {
//                    httpDialog.dismiss();
//                }
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (mwv_h5 != null) {
                    mwv_h5.setVisibility(View.GONE);
                    Common.staticToast("网络连接错误，请稍后再试");
                    errorOperation();
                }
//                if (!isFinishing() && httpDialog != null && httpDialog.isShowing()) {
//                    httpDialog.dismiss();
//                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.augusLogW("========h5Url==========" + url);
                if (url.startsWith("alipay")) {
//                    Log.i("shouldOverrideUrlLoading", "处理自定义scheme");
                    try {
                        // 以下固定写法
                        final Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    } catch (Exception e) {
                        // 防止没有安装的情况
                        Common.staticToast(getStringResouce(R.string.common_zhifubaohint));
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                    return true;
                } else if (url.startsWith("weixin://")) {
                    try {
                        // 以下固定写法
                        final Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    } catch (Exception e) {
                        // 防止没有安装的情况
                        Common.staticToast(getStringResouce(R.string.common_weixinhint));
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                    return true;
                } else if (url.contains("slmall://")) {
                    analysisUrl(url);
                    return true;
//                }else  if(Build.VERSION.SDK_INT<26) {
//                    view.loadUrl(url);
//                    return true;
//                }
//                return false;
                } else {
//                    if (!Common.getDomain(h5Url).equals(Common.getDomain(url)))
//                        addCookie(url);
                    return super.shouldOverrideUrlLoading(view, url);
//                    return false;
                }
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (sonicSession != null) {
                    try {
                        return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
                    } catch (Exception e) {
                        return super.shouldInterceptRequest(view, url);
                    }
                }
                return super.shouldInterceptRequest(view, url);
            }


        });

        mwv_h5.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //如果没有网络直接跳出方法
                if (!NetworkUtils.isNetworkAvailable(H5X5Act.this)) {
                    return;
                }
                //如果进度条隐藏则让它显示
                if (mProgressbar != null && View.VISIBLE == mProgressbar.getVisibility() && isContinue == false) {
                    mProgressbar.setVisibility(View.VISIBLE);
                    //大于80的进度的时候,放慢速度加载,否则交给自己加载
                    if (newProgress >= 80) {
                        //拦截webView自己的处理方式
                        if (isContinue) {
                            return;
                        }
                        mProgressbar.setCurProgress(100, 3000, () -> {
                            finishOperation(true);
                        });
                        isContinue = true;
                    } else {
                        mProgressbar.setNormalProgress(newProgress);
                    }
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String titles) {
                super.onReceivedTitle(view, titles);
                if (!isFinishing()) {
                    if (!isEmpty(titles)) {
                        title = titles;
                        setTitle();
                    }
                }
            }

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
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;

            }

        });
        addCookie(h5Url);
        mwv_h5.getSettings().setUserAgentString(webSetting.getUserAgentString() + " " + SharedPrefUtil
//        mwv_h5.getSettings().setUserAgentString(SharedPrefUtil
                .getCacheSharedPrf("User-Agent", "ShunLian Android 1.1.1/0.0.0"));
    }

    @Override
    protected void onRestart() {
        reFresh();
        super.onRestart();
    }


    public void reFresh() {
        if (!beforeUrl.equals(h5Url) && !isEmpty(h5Url)) {
            initSonic();
            initWebView();
//            loadUrl();
            mwv_h5.loadUrl(h5Url, setWebviewHeader());
            beforeUrl = h5Url;
        } else {
            addCookie(h5Url);
            if (!member_id.equals(SharedPrefUtil.getSharedUserString("member_id", "")))
                mwv_h5.reload();
        }
        member_id = SharedPrefUtil.getSharedUserString("member_id", "");
    }

    public void addCookie(String url) {
        String domain = Common.getDomain(url);
        if (isEmpty(domain))
            return;
        //add
        String token = SharedPrefUtil.getSharedUserString("token", "");
        String ua = SharedPrefUtil.getCacheSharedPrf("User-Agent", "ShunLian Android 4.0.0/1.0.0");
        String member_id = SharedPrefUtil.getSharedUserString("member_id", "");
        String code = SharedPrefUtil.getSharedUserString("invite_code", "");

        CookieSyncManager.createInstance(this);
//        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();

        cookieManager.setCookie(domain, "Client-Type=Android");
        cookieManager.setCookie(domain, "token=" + token);
        cookieManager.setCookie(domain, "User-Agent=" + ua);
        cookieManager.setCookie(domain, "member_id=" + member_id);
        cookieManager.setCookie(domain, "code=" + code);
//        cookieSyncManager.sync();

        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
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

//        if (mwv_h5 != null) { //webView在xml中使用会出现内存泄漏
//            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
//            // destory()
//            ViewParent parent = mwv_h5.getParent();
//            if (parent != null) {
//                ((ViewGroup) parent).removeView(mwv_h5);
//            }
//
//            mwv_h5.stopLoading();
//            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
//            mwv_h5.getSettings().setJavaScriptEnabled(false);
//            mwv_h5.clearHistory();
//            mwv_h5.clearView();
//            mwv_h5.removeAllViews();
//            mwv_h5.destroy();
//        }

        try {
            ViewGroup view = (ViewGroup) getWindow().getDecorView();
            view.removeAllViews();
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    private void analysisUrl(String url) {
        if (url.equals(h5Url)) {
            LogUtil.httpLogW("=====analysisUrl=====h5Url==========" + h5Url);
            return;
        }
        LogUtil.httpLogW("链接:" + url);
        Common.urlToPage(H5X5Act.this, url);
//        if (url.startsWith("slmall://")) {
//            String type = interceptBody(url);
//            if (!TextUtils.isEmpty(type)) {
//                String id = "";
//                String id1 = "";
//
//                if (!TextUtils.isEmpty(Common.getURLParameterValue(url, "id")))
//                    id = interceptId(url);
//                if (!TextUtils.isEmpty(Common.getURLParameterValue(url, "id1")))
//                    id1 = interceptId(url);
//                Common.goGoGo(H5X5Act.this, type, id, id1);
//            }
//        }
    }

    /**
     * 完整url slmall://goods/item.json?goodsId=138471
     * 截取之后goods/item.json
     *
     * @param url
     * @return
     */
//    private String interceptBody(String url) {
//        String[] split = url.split("\\?");
//        String s = split[0];
//        if (!TextUtils.isEmpty(s)) {
//            String[] split1 = s.split("//");
//            if (!TextUtils.isEmpty(split1[1])) {
//                return split1[1];
//            }
//        }
//        return null;
//    }

    /**
     * 截取商品id
     *
     * @param
     * @return
     */
//    private String interceptId(String url) {
//        String[] split = url.split("\\?");
//        String s = split[1];
//        String[] split1 = s.split("=");
//        String s1 = split1[1];
//        /*if (s1.matches("[0-9]+")){
//            return s1;
//        }*/
//        return s1;
//    }
    @Override
    public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
//        if (y > 2000) {
//            layout_backtToUp.setVisibility(View.VISIBLE);
//        } else {
//            layout_backtToUp.setVisibility(View.GONE);
//        }
    }

    /**
     * 错误的时候进行的操作
     */
    private void errorOperation() {
        //隐藏webview
        mwv_h5.setVisibility(View.GONE);
        if (View.INVISIBLE == mProgressbar.getVisibility()) {
            mProgressbar.setVisibility(View.VISIBLE);
        }
        //3.5s 加载 0->80 进度的加载 为了实现,特意调节长了事件
        mProgressbar.setCurProgress(80, 3500, () -> {
            //3.5s 加载 80->100 进度的加载
            if (mProgressbar != null)
                mProgressbar.setCurProgress(100, 3500, () -> finishOperation(false));
        });
    }

    /**
     * 结束进行的操作
     */
    private void finishOperation(boolean flag) {
        if (mProgressbar != null) {
            //最后加载设置100进度
            mProgressbar.setNormalProgress(100);
            //显示网络异常布局
            nei_empty.setVisibility(flag ? View.GONE : View.VISIBLE);
            mwv_h5.setVisibility(flag ? View.VISIBLE : View.GONE);
            //点击重新连接网络
            nei_empty.setNetExecption().setOnClickListener(v -> {
                mwv_h5.setVisibility(View.VISIBLE);
                nei_empty.setVisibility(View.GONE);
                mwv_h5.reload();
            });
            hideProgressWithAnim();
        }
    }

    /**
     * 隐藏加载对话框
     */
    private void hideProgressWithAnim() {
        AnimationSet animation = getDismissAnim(H5X5Act.this);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mProgressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mProgressbar.startAnimation(animation);
    }

    /**
     * 获取消失的动画
     *
     * @param context
     * @return
     */
    private AnimationSet getDismissAnim(Context context) {
        AnimationSet dismiss = new AnimationSet(context, null);
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(1000);
        dismiss.addAnimation(alpha);
        return dismiss;
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
