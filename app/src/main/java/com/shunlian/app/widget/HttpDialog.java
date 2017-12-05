package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.shunlian.app.R;

/**
 * Created by Administrator on 2017/12/2.
 */

public class HttpDialog extends Dialog {
    private static Context mContext;
    private ProgressView mProgressBar;
    private long startTime;
    private static final long min_show_tile = 1000;//最少显示时间(毫秒)
    public Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismiss();
        }
    };

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

    @Override
    public void dismiss() {
        long dt = System.currentTimeMillis() - startTime;
        if (dt >= min_show_tile){
            super.dismiss();

        }else {
            mHandler.sendEmptyMessageDelayed(0,min_show_tile - dt);
        }

    }

    @Override
    public void show() {
        startTime = System.currentTimeMillis();
        try {
            super.show();
        }catch (WindowManager.BadTokenException e){
            e.printStackTrace();
        }

    }
}
