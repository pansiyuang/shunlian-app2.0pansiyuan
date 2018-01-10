package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by Administrator on 2017/12/2.
 */

public class HttpDialog extends Dialog {
    private static Context mContext;
    private ProgressView mProgressBar;
    private long startTime;
    private static final long min_show_tile = 600;//最少显示时间(毫秒)
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismiss();
        }
    };

    public HttpDialog(Context context) {
        this(context, R.style.Mydialog);
        View view = View.inflate(context, R.layout.dialog_http, null);
        setCanceledOnTouchOutside(false);
        setContentView(view);
        mProgressBar = (ProgressView) view.findViewById(R.id.loading_progress);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = TransformUtil.dip2px(context, 77.5f); // 高度设置为屏幕的0.6
        p.width = TransformUtil.dip2px(context, 77.5f);// 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
    }

    public HttpDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void dismiss() {
        long dt = System.currentTimeMillis() - startTime;
        if (dt >= min_show_tile) {

            try {
                super.dismiss();
                if (mProgressBar != null && mProgressBar.isRunning()) {
                    mProgressBar.stopAnimation();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            mHandler.sendEmptyMessageDelayed(0, min_show_tile - dt);
        }
    }

    @Override
    public void show() {
        startTime = System.currentTimeMillis();
        try {
            super.show();
            if (mProgressBar != null && !mProgressBar.isRunning()) {
                mProgressBar.startAnimation();
            }
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
