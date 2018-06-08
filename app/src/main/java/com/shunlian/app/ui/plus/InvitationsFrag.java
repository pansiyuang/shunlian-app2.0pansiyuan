package com.shunlian.app.ui.plus;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.widget.WebViewProgressBar;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/28.
 */

public class InvitationsFrag extends BaseFragment {

    public static InvitationsFrag getInstance(String url) {
        InvitationsFrag invitationsFrag = new InvitationsFrag();
        Bundle args = new Bundle();
        args.putString("url", url);
        invitationsFrag.setArguments(args);
        return invitationsFrag;
    }

    private WebView mWebView;
    public WebViewProgressBar mProgressbar;
    public NetAndEmptyInterface nei_empty;
    public String currentUrl;
    private boolean isContinue = false;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_invitations, container, false);
        mWebView = (WebView) view.findViewById(R.id.mWebView);
        mProgressbar = (WebViewProgressBar) view.findViewById(R.id.mProgressbar);
        nei_empty = (NetAndEmptyInterface) view.findViewById(R.id.nei_empty);
        return view;
    }

    @Override
    protected void initData() {
        currentUrl = getArguments().getString("url");

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        if (!isEmpty(currentUrl)) {
            mWebView.loadUrl(currentUrl);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //如果没有网络直接跳出方法
                if (!NetworkUtils.isNetworkAvailable(getActivity())) {
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
            mWebView.setVisibility(flag ? View.VISIBLE : View.GONE);
            //点击重新连接网络
            nei_empty.setNetExecption().setOnClickListener(v -> {
                mWebView.setVisibility(View.VISIBLE);
                nei_empty.setVisibility(View.GONE);
                mWebView.reload();
            });
            hideProgressWithAnim();
        }
    }

    /**
     * 隐藏加载对话框
     */
    private void hideProgressWithAnim() {
        AnimationSet animation = getDismissAnim(getActivity());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mProgressbar.setVisibility(View.GONE);
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
}
