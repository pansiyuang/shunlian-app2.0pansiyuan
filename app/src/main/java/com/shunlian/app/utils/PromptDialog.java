package com.shunlian.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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
    private TextView tv_messages;
    private MyTextView mtv_describe;
    private View view_line;

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
        tv_messages = (TextView) logoutDialog.findViewById(R.id.tv_messages);
        mtv_describe = (MyTextView) logoutDialog.findViewById(R.id.mtv_describe);
        view_line = logoutDialog.findViewById(R.id.view_line);

        GradientDrawable background = (GradientDrawable) tvSure.getBackground();
        int i = TransformUtil.dip2px(ctx, 5);
        float[] radii = {0,0,0,0,i,i,0,0};
        background.setCornerRadii(radii);
    }

    public void setTvSureText(CharSequence text) {
        if (tvSure != null) {
            tvSure.setText(text);
        }
    }

    /**
     * 设置确定按钮是否加粗
     * @param isBold true 加粗
     * @return
     */
    public PromptDialog setTvSureIsBold(boolean isBold){
        if (tvSure != null) {
            if (isBold){
                tvSure.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            }else {
                tvSure.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            }
        }
        return this;
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

    public void setTvSureBGColor(int color){
        if (tvSure != null) {
            GradientDrawable background = (GradientDrawable) tvSure.getBackground();
            background.setColor(color);
        }
    }

    public void setTvCancleText(CharSequence text) {
        if (tvCancle != null) {
            tvCancle.setText(text);
        }
    }

    /**
     * 取消按钮是否加粗
     * @param isBold true 加粗
     */
    public PromptDialog setTvCancleIsBold(boolean isBold){
        if (tvCancle != null) {
            if (isBold){
                tvCancle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            }else {
                tvCancle.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            }
        }
        return this;
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
        if (tv_messages != null) {
            tv_messages.setText(texts);
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
            view_line.setVisibility(View.GONE);
            tvSure.setVisibility(View.GONE);
        } else {
            view_line.setVisibility(View.VISIBLE);
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
            view_line.setVisibility(View.GONE);
            tvCancle.setVisibility(View.GONE);
            GradientDrawable background = (GradientDrawable) tvSure.getBackground();
            int i = TransformUtil.dip2px(ctx, 5);
            float[] radii = {0,0,0,0,i,i,i,i};
            background.setCornerRadii(radii);
        } else {
            view_line.setVisibility(View.VISIBLE);
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

    public PromptDialog setSureAndCancleListener(CharSequence titel,CharSequence tite2,CharSequence describe, CharSequence tvSureText,
                                                 View.OnClickListener sureListener,
                                                 CharSequence tvCancleText, View.OnClickListener cancleListener) {
        tv_messages.setVisibility(View.VISIBLE);
        setTvMessages(titel,tite2);
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
