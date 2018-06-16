package com.shunlian.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by Administrator on 2018/6/15.
 */

public class TipDialog{

    private Dialog logoutDialog;
    private final Activity ctx;
    private MyButton mbtn_sure;
    private TextView tvMessage;
    private MyTextView mtv_describe;

    public TipDialog(Activity ctx) {
        this.ctx = ctx;
        logoutDialog = new Dialog(ctx, R.style.Mydialog);
        logoutDialog.setContentView(R.layout.dialog_tip);
        initView(logoutDialog);
        setCancelable(false);
    }

    private void initView(Dialog logoutDialog) {
        Window window = logoutDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = TransformUtil.countRealWidth(ctx, 600);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        mbtn_sure = (MyButton) logoutDialog.findViewById(R.id.mbtn_sure);
        tvMessage = (TextView) logoutDialog.findViewById(R.id.tv_message);
        mtv_describe = (MyTextView) logoutDialog.findViewById(R.id.mtv_describe);
    }

    public void setTvSureText(CharSequence text) {
        if (mbtn_sure != null) {
            mbtn_sure.setText(text);
        }
    }

    /**
     * 设置确定按钮是否加粗
     * @param isBold true 加粗
     * @return
     */
    public TipDialog setTvSureIsBold(boolean isBold){
        if (mbtn_sure != null) {
            if (isBold){
                mbtn_sure.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            }else {
                mbtn_sure.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            }
        }
        return this;
    }

    public void setTvSureColor(int color) {
        if (mbtn_sure != null) {
            mbtn_sure.setTextColor(ctx.getResources().getColor(color));
        }
    }
    public void setTvSureBg(int drawable) {
        if (mbtn_sure != null) {
            mbtn_sure.setBackgroundDrawable(ctx.getResources().getDrawable((drawable)));
        }
    }

    public void setTvMessage(CharSequence text) {
        if (tvMessage != null) {
            tvMessage.setText(text);
        }
    }

    public void setTvMessages(CharSequence text,CharSequence texts) {
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

    public TipDialog setSureListener(CharSequence titel, CharSequence tvSureText,
                                              View.OnClickListener sureListener) {
        setTvMessage(titel);
        setTvSureText(tvSureText);
        if (mbtn_sure != null) {
            mbtn_sure.setOnClickListener(sureListener);
        }
        return this;
    }

    public TipDialog setSureListener(CharSequence titel,CharSequence describe,
                                              CharSequence tvSureText,
                                              View.OnClickListener sureListener) {
        setTvMessage(titel);
        setTvSureText(tvSureText);
        setTvDescribe(describe);
        if (mbtn_sure != null) {
            mbtn_sure.setOnClickListener(sureListener);
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

    DialogInterface.OnKeyListener keylistener = (dialog, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        } else {
            return false;
        }
    };
}
