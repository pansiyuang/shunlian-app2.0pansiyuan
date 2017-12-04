package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.shunlian.app.R;

/**
 * Created by Administrator on 2017/12/2.
 */

public class HttpDialog extends Dialog {
    private ProgressView mProgressBar;


    public HttpDialog(Context context) {
        this(context, R.style.Mydialog);
    }

    public HttpDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_http);
        setCanceledOnTouchOutside(false);
        mProgressBar = (ProgressView) findViewById(R.id.loading_progress);
        if (mProgressBar != null) {
            mProgressBar.startAnimation();
        }
    }
}
