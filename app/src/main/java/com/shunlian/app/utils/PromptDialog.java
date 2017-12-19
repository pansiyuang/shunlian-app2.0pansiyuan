package com.shunlian.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyTextView;

/**
 * Created by MBENBEN on 2016/8/13 10 : 46.
 * 提示框
 */
public class PromptDialog {

    private  Dialog logoutDialog;
    private final Activity ctx;
    private TextView tvSure;
    private TextView tvCancle;
    private TextView tvMessage;
    private MyTextView mtv_describe;

    public PromptDialog(Activity ctx) {
        this.ctx = ctx;
        logoutDialog = new Dialog(ctx, R.style.Mydialog);
        logoutDialog.setContentView(R.layout.dialog_common);
        initView(logoutDialog);
        setCancelable(false);
    }

    private void initView(Dialog logoutDialog) {
        tvSure = (TextView) logoutDialog.findViewById(R.id.tv_sure);
        tvCancle = (TextView) logoutDialog.findViewById(R.id.tv_cancel);
        tvMessage = (TextView) logoutDialog.findViewById(R.id.tv_message);
        mtv_describe = (MyTextView) logoutDialog.findViewById(R.id.mtv_describe);
    }

    public void setTvSureText(CharSequence text) {
        if (tvSure != null) {
            tvSure.setText(text);
        }
    }

    public void setTvSureColor(int color) {
        if (tvSure != null) {
            tvSure.setTextColor(ctx.getResources().getColor(color));
        }
    }
    public void setTvSureBg(int drawable) {
        if (tvSure != null) {
            tvSure.setBackgroundDrawable(ctx.getResources().getDrawable((drawable)));
        }
    }
    public void setTvCancleText(CharSequence text) {
        if (tvCancle != null) {
            tvCancle.setText(text);
        }
    }

    public void setTvMessage(CharSequence text) {
        if (tvMessage != null) {
            tvMessage.setText(text);
        }
    }

    public void setTvDescribe(CharSequence text){
        if (mtv_describe != null){
            if (TextUtils.isEmpty(text)){
                mtv_describe.setVisibility(View.GONE);
                return;
            }
            mtv_describe.setVisibility(View.VISIBLE);
            mtv_describe.setText(text);
        }
    }

    /**
     * 是否显示确定按钮 isGone 为true隐藏否则显示
     *
     * @param isGone
     */
    public void tvSureVisibility(boolean isGone) {
        if (isGone) {
            tvSure.setVisibility(View.GONE);
        } else {
            tvSure.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 是否显示取消按钮 isGone 为true隐藏否则显示
     *
     * @param isGone
     */
    public void tvCancleVisibility(boolean isGone) {
        if (isGone) {
            tvCancle.setVisibility(View.GONE);
        } else {
            tvCancle.setVisibility(View.VISIBLE);
        }
    }

    public void setCancelable(boolean flag) {
        if (logoutDialog != null)
            logoutDialog.setCancelable(flag);
    }

    /**
     * 是否设置键监听 flag为true 返回键不能取消dialog
     *
     * @param flag
     */
    public void isOnKeyListener(boolean flag) {
        if (logoutDialog != null) {
            if (flag) {
                logoutDialog.setOnKeyListener(keylistener);
            } else {
                logoutDialog.setOnKeyListener(null);
            }
        }
    }

    public PromptDialog setSureAndCancleListener(CharSequence titel, CharSequence tvSureText, View.OnClickListener sureListener,
                                                 CharSequence tvCancleText, View.OnClickListener cancleListener) {
        setTvMessage(titel);
        setTvSureText(tvSureText);
        if (tvSure != null) {
            tvSure.setOnClickListener(sureListener);
        }
        setTvCancleText(tvCancleText);
        if (tvCancle != null) {
            tvCancle.setOnClickListener(cancleListener);
        }
        return this;
    }

    public PromptDialog setSureAndCancleListener(CharSequence titel,CharSequence describe, CharSequence tvSureText,
                                                 View.OnClickListener sureListener,
                                                 CharSequence tvCancleText, View.OnClickListener cancleListener) {
        setTvMessage(titel);
        setTvSureText(tvSureText);
        setTvDescribe(describe);
        if (tvSure != null) {
            tvSure.setOnClickListener(sureListener);
        }
        setTvCancleText(tvCancleText);
        if (tvCancle != null) {
            tvCancle.setOnClickListener(cancleListener);
        }
        return this;
    }


    public void show() {
        if (!ctx.isFinishing()&&logoutDialog!=null&&!logoutDialog.isShowing()) {
            logoutDialog.show();
        }
    }

    public void dismiss() {
        if (logoutDialog != null && logoutDialog.isShowing()) {
            logoutDialog.dismiss();
        }
    }

    /**
     * 释放dialog 防止内存泄漏
     */
    public void release(){
        dismiss();
        logoutDialog=null;
    }

    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };
}
