package com.shunlian.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.adapter.StoreShareBabyAdapter;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.listener.ICallBackResult;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.dialog.CommonDialog;
import com.shunlian.app.wxapi.WXEntryActivity;
import com.zh.chartlibrary.common.DensityUtil;

public class CommonDialogUtil {
    public Context context;
    public CommonDialog nomalBuildl;
    private TextView tvSure;
    private TextView tvCancle;
    private TextView tvMessage;

    public CommonDialog guideInfo;
    public CommonDialog inputGuide;
    public CommonDialogUtil(Context context){
        this.context = context;
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
    public void guideInfoCommonDialog(ICallBackResult<String> callBackResult) {
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

        GlideUtils.getInstance().loadCircleAvar(context,img_member_head,"https://static.veer.com/veer/static/resources/FourPack/2018-12-03/d9738f6321324d51a78e567fdfeabc63.jpg");
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
                callBackResult.onTagClick("");
            }
        });
    }


    public void dismiss() {
        if(nomalBuildl!=null&&nomalBuildl.isShowing()){
            nomalBuildl.dismiss();
        }
    }
}
