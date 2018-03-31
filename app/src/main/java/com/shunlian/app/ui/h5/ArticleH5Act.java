package com.shunlian.app.ui.h5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.http.SslError;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.bean.ArticleDetailEntity;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.presenter.ArticleDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IArticleDetailView;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
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

/**
 * Created by Administrator on 2018/3/19.
 */

public class ArticleH5Act extends BaseActivity implements IArticleDetailView {
    @BindView(R.id.tv_storeName)
    MyTextView tv_storeName;

    @BindView(R.id.miv_favorite)
    MyImageView miv_favorite;

//    @BindView(R.id.rl_bottom)
//    RelativeLayout rl_bottom;

    @BindView(R.id.h5_mwb)
    WebView h5_mwb;
    private String articleId;

    public static final int MODE_DEFAULT = 0;//默认模式，没有缓存

    public static final int MODE_SONIC = 1;//有缓存

    public static final int MODE_SONIC_WITH_OFFLINE_CACHE = 2;//清除缓存
    private int mode;
    private SonicSession sonicSession;
    SonicSessionClientImpl sonicSessionClient = null;
    private HttpDialog httpDialog;
    private ArticleDetailPresenter mPresent;
    private int currentFavoriteStatus;

    public static void startAct(Context context, String articleId, int mode) {
        Intent intentH5 = new Intent(context, ArticleH5Act.class);
        intentH5.putExtra("articleId", articleId);
        intentH5.putExtra("mode", mode);
        context.startActivity(intentH5);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {

        Intent intent = getIntent();
        articleId = intent.getStringExtra("articleId");
        mode = intent.getIntExtra("mode", 0);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
                        return new ArticleH5Act.OfflinePkgSessionConnection(ArticleH5Act.this, session, intent);
                    }
                });
            }

            // create sonic session and run sonic flow
//            sonicSession = SonicEngine.getInstance().createSession(h5Url, sessionConfigBuilder.build());
//            if (null != sonicSession) {
//                sonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
//            } else {
//                // this only happen when a same sonic session is already running,
//                // u can comment following codes to feedback as a default mode.
//                // throw new UnknownError("create session fail!");
//                Toast.makeText(this, "create sonic session fail!", Toast.LENGTH_LONG).show();
//            }
        }

        return R.layout.act_h5;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        mPresent = new ArticleDetailPresenter(this, this);
        mPresent.getArticleDetail(articleId);
        httpDialog = new HttpDialog(this);
        initWebView();
//        setListenerToRootView();
    }

    @Override
    protected void initListener() {
        miv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFavoriteStatus == 1) {
                    mPresent.unFavoriteArticle(articleId);
                } else {
                    mPresent.favoriteArticle(articleId);
                }
            }
        });
        super.initListener();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        WebSettings webSetting = h5_mwb.getSettings();
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

        h5_mwb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!isFinishing() && httpDialog != null) {
                    httpDialog.show();
                }
                if (h5_mwb != null) {
                    h5_mwb.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!isFinishing() && httpDialog != null && httpDialog.isShowing()) {
                    httpDialog.dismiss();
                }
                if (sonicSession != null) {
                    sonicSession.getSessionClient().pageFinish(url);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();//接受证书
//                view.loadUrl(h5Url,setWebviewHeader());
                if (!isFinishing() && httpDialog != null && httpDialog.isShowing()) {
                    httpDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                view.loadUrl(h5Url,setWebviewHeader());
                if (h5_mwb != null) {
                    h5_mwb.setVisibility(View.INVISIBLE);
                    Common.staticToast("网络连接错误，请稍后再试");
                }
                if (!isFinishing() && httpDialog != null && httpDialog.isShowing()) {
                    httpDialog.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.httpLogW("==========url==========" + url);
//                if (url.contains("slmall://")) {
//                    analysisUrl(url);
//                    return true;
//                } else {
//                    return super.shouldOverrideUrlLoading(view, url);
//                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (sonicSession != null) {
                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
                }
                return super.shouldInterceptRequest(view, url);
            }


        });

//        addCookie();

        // webview is ready now, just tell session client to bind
    }


    private void setListenerToRootView() {
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean mKeyboardUp = isKeyboardShown(rootView);
                if (mKeyboardUp) {
//                    rl_bottom.setVisibility(View.VISIBLE);
                } else {
//                    rl_bottom.setVisibility(View.GONE);
                }
            }
        });
    }

    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    @Override
    protected void onDestroy() {
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

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getArticleDetail(ArticleDetailEntity detailEntity) {
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(h5_mwb);
            sonicSessionClient.clientReady();
        } else { // default mode
            if (!isEmpty(detailEntity.h5_detail_url))
                h5_mwb.loadUrl(detailEntity.h5_detail_url, setWebviewHeader());
        }
        if ("1".equals(detailEntity.had_favorites)) {
            miv_favorite.setImageResource(R.mipmap.icon_found_souchang_h);
        } else {
            miv_favorite.setImageResource(R.mipmap.icon_found_souchang_n);
        }
        currentFavoriteStatus = Integer.valueOf(detailEntity.had_favorites);
        miv_favorite.setVisibility(View.VISIBLE);
    }

    @Override
    public void favoriteSuccess() {
        currentFavoriteStatus = 1;
        miv_favorite.setImageResource(R.mipmap.icon_found_souchang_h);
    }

    @Override
    public void unFavoriteSuccess() {
        currentFavoriteStatus = 0;
        miv_favorite.setImageResource(R.mipmap.icon_found_souchang_n);
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
