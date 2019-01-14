package com.shunlian.app.ui.new3_login;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyTextView;

/**
 * Created by MBENBEN on 2016/8/13 10 : 46.
 * 提示框
 */
public class EditInviteCodeDialog {

    private  Dialog logoutDialog;
    private final Activity ctx;
    private MyTextView mtv_sure;
    private MyTextView mtv_cancle;
    private InviteCodeWidget invite_code;


    public EditInviteCodeDialog(Activity ctx) {
        this.ctx = ctx;
        logoutDialog = new Dialog(ctx, R.style.Mydialog);
        logoutDialog.setContentView(R.layout.dialog_edit_invite_code);
        initView(logoutDialog);
        Window window = logoutDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (DeviceInfoUtil.getDeviceWidth(ctx)*0.84f);
        logoutDialog.getWindow().setAttributes(params);
        setCancelable(false);
    }

    private void initView(Dialog logoutDialog) {
        LinearLayout ll_root = logoutDialog.findViewById(R.id.ll_root);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(TransformUtil.dip2px(ctx,11));
        ll_root.setBackgroundDrawable(gd);

        invite_code = logoutDialog.findViewById(R.id.invite_code);

        mtv_sure = logoutDialog.findViewById(R.id.mtv_sure);
        mtv_cancle = logoutDialog.findViewById(R.id.mtv_cancle);
    }



    public void setOnClickListener(View.OnClickListener cancleListener
            ,View.OnClickListener sureListener){
        if (mtv_cancle != null){
            mtv_cancle.setOnClickListener(cancleListener);
        }

        if (mtv_sure != null){
            mtv_sure.setOnClickListener(sureListener);
        }
    }

    /**
     * 获取邀请码
     * @return
     */
    public String getInviteCode(){
        if (invite_code != null){
            return invite_code.getText().toString();
        }
        return "";
    }

    public void setStrategyUrl(String url){
        if (invite_code != null){
            invite_code.setStrategyUrl(url);
        }
    }



    /**
     * 显示view
     *
     * @param views
     */
    protected void visible(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    /**
     * 隐藏view
     *
     * @param views
     */
    protected void gone(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
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
