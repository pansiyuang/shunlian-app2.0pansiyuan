package com.shunlian.app.ui.plus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
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
    public String currentUrl;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_invitations, container, false);
        mWebView = (WebView) view.findViewById(R.id.mWebView);
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
    }
}
