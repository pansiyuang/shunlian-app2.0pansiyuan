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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.adapter.StoreShareBabyAdapter;
import com.shunlian.app.bean.ShareInfoParam;
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

    public CommonDialogUtil(Context context){
        this.context = context;
    }

    //分享商品的diolog
    public void defaultCommonDialog(String message,String tv_sure, View.OnClickListener sureListener,
                                String tv_cancle, View.OnClickListener cancleListener) {
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


    public void dismiss() {
        if(nomalBuildl!=null&&nomalBuildl.isShowing()){
            nomalBuildl.dismiss();
        }
    }
}
