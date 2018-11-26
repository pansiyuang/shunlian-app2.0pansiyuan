package com.shunlian.app.ui.new3_login;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

/**
 * Created by MBENBEN on 2016/8/13 10 : 46.
 * 提示框
 */
public class VerifyPicDialog {

    private  Dialog logoutDialog;
    private final Activity ctx;
    private TextView tvSure;
    private TextView tvCancle;
    private View view_line;
    private EditText ed_verify;
    private MyImageView miv_pic_code;
    private LinearLayout ll_verify;
    private MyImageView miv_avatar;
    private MyTextView mtv_nickname;
    private MyTextView mtv_invite_code;
    private LinearLayout ll_invite_code;
    private LinearLayout ll_root;
    private TextView tv_message;
    private TextView mtv_tip;

    public VerifyPicDialog(Activity ctx) {
        this.ctx = ctx;
        logoutDialog = new Dialog(ctx, R.style.Mydialog);
        logoutDialog.setContentView(R.layout.dialog_verify_pic);
        initView(logoutDialog);
        Window window = logoutDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (DeviceInfoUtil.getDeviceWidth(ctx)*0.72f);
        logoutDialog.getWindow().setAttributes(params);
        setCancelable(false);
    }

    private void initView(Dialog logoutDialog) {
        tvSure = logoutDialog.findViewById(R.id.tv_sure);
        tvCancle = logoutDialog.findViewById(R.id.tv_cancel);
        view_line = logoutDialog.findViewById(R.id.view_line);
        ed_verify = logoutDialog.findViewById(R.id.ed_verify);
        miv_pic_code = logoutDialog.findViewById(R.id.miv_pic_code);
        ll_verify = logoutDialog.findViewById(R.id.ll_verify);
        miv_avatar = logoutDialog.findViewById(R.id.miv_avatar);
        mtv_nickname = logoutDialog.findViewById(R.id.mtv_nickname);
        mtv_invite_code = logoutDialog.findViewById(R.id.mtv_invite_code);
        ll_invite_code = logoutDialog.findViewById(R.id.ll_invite_code);
        ll_root = logoutDialog.findViewById(R.id.ll_root);
        tv_message = logoutDialog.findViewById(R.id.tv_message);
        mtv_tip = logoutDialog.findViewById(R.id.mtv_tip);

        GradientDrawable background = (GradientDrawable) tvSure.getBackground();
        int i = TransformUtil.dip2px(ctx, 5);
        float[] radii = {0,0,0,0,i,i,0,0};
        background.setCornerRadii(radii);

        ed_verify.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (s.length() > 0 && mtv_tip != null){
                    mtv_tip.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 1 输入图形验证码  2 邀请码
     * @param state
     */
    public void showState(int state){
        ViewGroup.LayoutParams layoutParams = ll_root.getLayoutParams();
        if (1 == state){
            layoutParams.height = TransformUtil.dip2px(ctx,144);
            visible(ll_verify);
            gone(miv_avatar,mtv_nickname,ll_invite_code);
        }else if (2 == state){
            layoutParams.height = TransformUtil.dip2px(ctx,265);
            gone(ll_verify);
            visible(miv_avatar,mtv_nickname,ll_invite_code);

            GradientDrawable gb = new GradientDrawable();
            gb.setColor(Color.parseColor("#ECECEC"));
            int radius = TransformUtil.dip2px(ctx, 27);
            int minWidth = TransformUtil.dip2px(ctx, 190);
            gb.setCornerRadius(radius);
            ll_invite_code.setMinimumHeight(radius*2);
            ll_invite_code.setMinimumWidth(minWidth);
            ll_invite_code.setBackgroundDrawable(gb);
        }
        ll_root.setLayoutParams(layoutParams);
    }


    public void setMessage(String msg){
        if (tv_message != null){
            tv_message.setText(msg);
        }
    }

    public void setPicTip(String tip){
        if (mtv_tip != null){
            mtv_tip.setVisibility(View.VISIBLE);
            mtv_tip.setText(tip);
        }
    }

    /**
     * 推荐人详情
     * @param bean
     */
    public void setMemberDetail(MemberCodeListEntity.ListBean bean){
        if (mtv_nickname != null){
            mtv_nickname.setText(bean.nickname);
        }
        if (mtv_invite_code != null){
            mtv_invite_code.setText(bean.code);
        }
        if (miv_avatar != null){
            GlideUtils.getInstance().loadCircleHeadImage(ctx,miv_avatar,bean.avatar);
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
    public VerifyPicDialog setTvSureIsBold(boolean isBold){
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

    public void setTvSureBgColor(int color) {
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
    public VerifyPicDialog setTvCancleIsBold(boolean isBold){
        if (tvCancle != null) {
            if (isBold){
                tvCancle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            }else {
                tvCancle.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            }
        }
        return this;
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

    public void setImagViewCode(Bitmap bitmap){
        if (miv_pic_code != null){
            miv_pic_code.setImageBitmap(bitmap);
            if (ed_verify != null){
                ed_verify.setText("");
            }
        }
    }

    public String getVerifyText(){
        if (ed_verify != null){
            return ed_verify.getText().toString();
        }
        return "";
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

    public VerifyPicDialog setSureAndCancleListener(CharSequence tvSureText, View.OnClickListener sureListener,
                                                    CharSequence tvCancleText, View.OnClickListener cancleListener) {
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
