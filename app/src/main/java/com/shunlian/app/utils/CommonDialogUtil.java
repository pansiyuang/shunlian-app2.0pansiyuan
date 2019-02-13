package com.shunlian.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.App;
import com.shunlian.app.R;
import com.shunlian.app.adapter.StoreShareBabyAdapter;
import com.shunlian.app.bean.DiscoverActivityEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.listener.ICallBackResult;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.dialog.CommonDialog;
import com.shunlian.app.wxapi.WXEntryActivity;
import com.zh.chartlibrary.common.DensityUtil;

import static android.os.Build.VERSION_CODES.M;

public class CommonDialogUtil {
    public Context context;
    public CommonDialog nomalBuildl;
    private TextView tvSure;
    private TextView tvCancle;
    private TextView tvMessage;

    public CommonDialog guideInfo;
    public CommonDialog inputGuide;
    public CommonDialog inputWeixin;
    public CommonDialog dialog_user_info;
    public CommonDialog dialog_user_old;
    public CommonDialog dialog_me_teach;
    public CommonDialogUtil(Context context){
        this.context = context;
    }

    //新人领取优惠劵
    public void userNewShowDialog(ICallBackResult<String> callBackResult,String defaultValue,String warn_txt) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAdLiuhai).fullWidth()
                .setView(R.layout.dialog_page_user_new);
        dialog_user_info = nomalBuild.create();
        dialog_user_info.getWindow().getAttributes().flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        dialog_user_info.getWindow().getAttributes().height = App.hightPixels;
        dialog_user_info.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog_user_info.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
                Window window = dialog_user_info.getWindow();
                int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                uiFlags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.WHITE);
                window.getDecorView().setSystemUiVisibility(uiFlags);
            } else {//4.4 全透明状态栏
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        dialog_user_info.show();
        MyImageView miv_close= dialog_user_info.findViewById(R.id.miv_close);
        TextView tv_new_submit = dialog_user_info.findViewById(R.id.tv_new_submit);
        TextView  ntv_user_page_price= dialog_user_info.findViewById(R.id.ntv_user_page_price);
        TextView tv_desc_text= dialog_user_info.findViewById(R.id.tv_desc_text);
        ntv_user_page_price.setText(Common.changeTextSize( context.getResources().getString(R.string.common_yuan)+"?", context.getResources().getString(R.string.common_yuan), 16));
        if(!TextUtils.isEmpty(defaultValue)){
            tv_new_submit.setText(defaultValue);
        }
        if(!TextUtils.isEmpty(warn_txt)){
            tv_desc_text.setText(warn_txt);
        }
        tv_new_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackResult.onTagClick(tv_new_submit.getText().toString());
            }
        });
        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((Activity)context).isFinishing()){
                    dialog_user_info.dismiss();
                    ((Activity)context).finish();
                    return;
                }
            }
        });
    }

    //老用户领取优惠劵
    public void userOldShowDialog(ICallBackResult<String> callBackResult,String content,int code) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,280))
                .setView(R.layout.dialog_page_user_old);
        dialog_user_old = nomalBuild.create();
        dialog_user_old.setCancelable(false);
        dialog_user_old.show();
        TextView tv_messages = dialog_user_old.findViewById(R.id.tv_messages);
        TextView  tv_sure= dialog_user_old.findViewById(R.id.tv_sure);
        TextView  tv_cancel= dialog_user_old.findViewById(R.id.tv_cancel);
       ImageView image_pic= dialog_user_old.findViewById(R.id.image_pic);
        TextView  tv_title= dialog_user_old.findViewById(R.id.tv_title);

        if(code==6201){
            tv_cancel.setText("去新人专享");
            image_pic.setVisibility(View.GONE);
            tv_title.setVisibility(View.INVISIBLE);
            tv_messages.setText("您已经领取过新人专享优惠券了哦，赶紧去使用吧");
        }else{
            tv_cancel.setText("确定");
            tv_title.setVisibility(View.VISIBLE);
            image_pic.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(content)){
                tv_messages.setText(content);
            }
        }
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_user_old.dismiss();
                callBackResult.onTagClick("home");
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_user_old.dismiss();

                callBackResult.onTagClick("page");
            }
        });
    }

    //添加微信
    public void defaultEditDialog(ICallBackResult<String> callBackResult,String defaultValue) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,250))
                .setView(R.layout.dialog_weixin_input);
        inputWeixin = nomalBuild.create();
        inputWeixin.setCancelable(false);
        inputWeixin.show();
        TextView tvSure = inputWeixin.findViewById(R.id.tv_sure);
        TextView  tvCancle = inputWeixin.findViewById(R.id.tv_cancel);
        EditText edit_input = inputWeixin.findViewById(R.id.edit_input);

        if(!TextUtils.isEmpty(defaultValue)){
            edit_input.setText(defaultValue);
            edit_input.setSelection(defaultValue.length());
        }
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(edit_input);
                inputWeixin.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edit_input.getText())){
                    Common.staticToast("请输入微信号");
                    return;
                }
                Common.hideKeyboard(edit_input);
                inputWeixin.dismiss();
                callBackResult.onTagClick(edit_input.getText().toString());
            }
        });
    }
    //默认的
    public void defaultCommonDialog(String message,String tv_sure, View.OnClickListener sureListener,
                                String tv_cancle, View.OnClickListener cancleListener) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,250))
                .setView(R.layout.dialog_common_cart);
        nomalBuildl = nomalBuild.create();
        nomalBuildl.setCancelable(false);
        nomalBuildl.show();
        tvSure = nomalBuildl.findViewById(R.id.tv_sure);
        tvCancle = nomalBuildl.findViewById(R.id.tv_cancel);
        tvMessage = nomalBuildl.findViewById(R.id.tv_message);

        tvCancle.setText(tv_cancle);
        tvSure.setText(tv_sure);
        tvMessage.setText(message);
        tvCancle.setOnClickListener(cancleListener);
        tvSure.setOnClickListener(sureListener);
    }

    //输入导购
    public void inputGuideCommonDialog(ICallBackResult<String> callBackResult) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,250))
                .setView(R.layout.dialog_member_input);
        inputGuide = nomalBuild.create();
        inputGuide.setCancelable(false);
        inputGuide.show();
        TextView tvSure = inputGuide.findViewById(R.id.tv_sure);
        TextView  tvCancle = inputGuide.findViewById(R.id.tv_cancel);
         EditText edit_input = inputGuide.findViewById(R.id.edit_input);

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(edit_input);
                inputGuide.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edit_input.getText())){
                    Common.staticToast("请输入邀请码");
                    return;
                }
                Common.hideKeyboard(edit_input);
                inputGuide.dismiss();
                callBackResult.onTagClick(edit_input.getText().toString());
            }
        });
    }


    //导购信息
    public void guideInfoCommonDialog(ICallBackResult<MemberCodeListEntity.ListBean> callBackResult, MemberCodeListEntity.ListBean listBean) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,250))
                .setView(R.layout.dialog_member_info);
        guideInfo = nomalBuild.create();
        guideInfo.setCancelable(false);
        guideInfo.show();
        TextView tvSure = guideInfo.findViewById(R.id.tv_sure);
        TextView  tvCancle = guideInfo.findViewById(R.id.tv_cancel);

        ImageView img_member_head = guideInfo.findViewById(R.id.img_member_head);
        TextView  tv_member_name = guideInfo.findViewById(R.id.tv_member_name);
        TextView  tv_member_number = guideInfo.findViewById(R.id.tv_member_number);
        if(listBean!=null){
            GlideUtils.getInstance().loadCircleAvar(context,img_member_head,listBean.avatar);
            tv_member_name.setText(listBean.nickname);
            tv_member_number.setText(listBean.code);
        }

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideInfo.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideInfo.dismiss();
                callBackResult.onTagClick(listBean);
            }
        });
    }


    public void dismiss() {
        if(nomalBuildl!=null&&nomalBuildl.isShowing()){
            nomalBuildl.dismiss();
        }
    }

    //我的导师提示
    public void meTeachCommonDialog(String weixinCode,boolean isTeach, View.OnClickListener sureListener,
                                     View.OnClickListener cancleListener) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd)
                .setView(R.layout.dialog_me_teach);
        dialog_me_teach = nomalBuild.create();
        dialog_me_teach.setCancelable(false);
        dialog_me_teach.show();
        TextView tv_teach_title = dialog_me_teach.findViewById(R.id.tv_teach_title);
        TextView tv_teach_desc = dialog_me_teach.findViewById(R.id.tv_teach_desc);
        TextView tv_teach_weixin = dialog_me_teach.findViewById(R.id.tv_teach_weixin);
        TextView tv_teach_weixin_code = dialog_me_teach.findViewById(R.id.tv_teach_weixin_code);
        TextView tv_teach_copy = dialog_me_teach.findViewById(R.id.tv_teach_copy);
        TextView tv_teach_remind = dialog_me_teach.findViewById(R.id.tv_teach_remind);
        MyImageView  miv_close= dialog_me_teach.findViewById(R.id.miv_close);
        if(isTeach) {
            tv_teach_title.setText("添加社群导师");
            tv_teach_desc.setText("获取导师一对一辅导帮助！");
            tv_teach_weixin.setText("社群导师微信");
        }else{
            tv_teach_title.setText("添加官方导师");
            tv_teach_desc.setText("获取官方一手重要资料及培训辅导!");
            tv_teach_weixin.setText("官方导师微信");
        }
        if(!TextUtils.isEmpty(weixinCode)){
            tv_teach_weixin_code.setText(weixinCode);
        }
        tv_teach_remind.setOnClickListener(cancleListener);
        tv_teach_copy.setOnClickListener(sureListener);
        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_me_teach.dismiss();
            }
        });
    }
}
