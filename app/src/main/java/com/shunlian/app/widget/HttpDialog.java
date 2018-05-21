package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
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
    private static final long min_show_time = 500;//最少显示时间(毫秒)
    private static final long quest_time = 500;//请求网络时间少于quest_time(毫秒)不显示动画
    private boolean isRealShow = false;//真正显示动画
    private boolean isRunDismiss = false;//是否执行dismiss
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    dismiss();
                    break;
                case 100:
                    if (!isRunDismiss){
                        isRealShow = true;
                        show();
                    }else {
                        isRunDismiss = false;
                    }
                    break;
            }
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
        setCancelable(false);
    }

    public HttpDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void dismiss() {
        /***********少于quest_time毫秒 不执行动画*****************/
        isRunDismiss = true;
        try {
            if (isShowing())
                super.dismiss();
            if (mProgressBar != null && mProgressBar.isRunning()) {
                mProgressBar.stopAnimation();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        /***********至少执行min_show_time毫秒*****************/
        /*long dt = System.currentTimeMillis() - startTime;
        if (dt >= min_show_time) {
            try {
                if (isShowing())
                    super.dismiss();
                if (mProgressBar != null && mProgressBar.isRunning()) {
                    mProgressBar.stopAnimation();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            mHandler.sendEmptyMessageDelayed(0, min_show_time - dt);
        }*/
    }

    @Override
    public void show() {
        /***********少于quest_time毫秒 不执行动画*****************/
        if (isRealShow) {
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
            isRealShow = false;
        }else {
            mHandler.sendEmptyMessageDelayed(100, quest_time);
        }

        /***********至少执行min_show_time毫秒*****************/
        /*startTime = System.currentTimeMillis();
        try {
            super.show();
            if (mProgressBar != null && !mProgressBar.isRunning()) {
                mProgressBar.startAnimation();
            }
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }*/
    }
}
