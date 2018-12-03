package com.shunlian.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.adapter.StoreShareBabyAdapter;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.dialog.CommonDialog;

import java.util.List;

public class StoreViewUtil {
    private Context context;
    private List<StoreGoodsListEntity.MData> datas;

    private int successCount = 0;
    private boolean isCircle = false;
    private StoreShareBabyAdapter.LoadImageCount cloadImageCount;
    public   CommonDialog showShopBuild;

    private MyTextView mtv_descl,mtv_pricel,mtv_pricer;
    private MyImageView miv_onel;

    private MyTextView mtv_descl2,mtv_pricel2,mtv_pricer2;
    private MyImageView miv_onel2;

    public StoreViewUtil(CommonDialog showShopBuild ,Context context, List<StoreGoodsListEntity.MData> datas, boolean isCircle, StoreShareBabyAdapter.LoadImageCount loadImageCount){
        this.showShopBuild = showShopBuild;
        this.context = context;
        this.datas = datas;
        this.isCircle = isCircle;
        successCount = 0;
        this.cloadImageCount = loadImageCount;
        initView();
    }

    private void initView() {
        miv_onel = showShopBuild.findViewById(R.id.miv_onel);
        mtv_descl = showShopBuild.findViewById(R.id.mtv_descl);
        mtv_pricel =  showShopBuild.findViewById(R.id.mtv_pricel);
        mtv_pricer = showShopBuild.findViewById(R.id.mtv_pricer);

        miv_onel2 =  showShopBuild.findViewById(R.id.miv_onel2);
        mtv_descl2 =  showShopBuild.findViewById(R.id.mtv_descl2);
        mtv_pricel2 =  showShopBuild.findViewById(R.id.mtv_pricel2);
        mtv_pricer2 =  showShopBuild.findViewById(R.id.mtv_pricer2);

        int width = Common.getScreenWidth((Activity) context)/2 - TransformUtil.dip2px(context, 70);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) miv_onel.getLayoutParams();
        layoutParams.width = width+TransformUtil.dip2px(context, 25);
        layoutParams.height = width;

        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) miv_onel2.getLayoutParams();
        layoutParams2.width = width+TransformUtil.dip2px(context, 25);
        layoutParams2.height = width;
    }

    public void showStoreGoodView() {
        if (datas.size() >= 1) {
            StoreGoodsListEntity.MData data = datas.get(0);
            mtv_descl.setText(data.title);
            mtv_pricel.setText(context.getResources().getString(R.string.common_yuan) +data.price);
            mtv_pricer.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
            miv_onel.setScaleType(ImageView.ScaleType.FIT_START);
            mtv_pricer.setText(context.getResources().getString(R.string.common_yuan) + data.market_price);
            if (!isCircle) {
                GlideUtils.getInstance().loadImage(context, miv_onel, data.thumb);
            } else {
                GlideUtils.getInstance().loadBitmapSync(context, data.thumb,
                        new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource,
                                                        GlideAnimation<? super Bitmap> glideAnimation) {
                                miv_onel.setImageBitmap(resource);
                                successCount++;
                                if (cloadImageCount != null) {
                                    cloadImageCount.imageSuccessCount(successCount);
                                }
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                Common.staticToast("图片没有加载成功不能分享朋友圈");
                            }
                        });
            }
        }
        if (datas.size() >= 2) {
            StoreGoodsListEntity.MData data2 = datas.get(1);
            mtv_descl2.setText(data2.title);
            mtv_pricel2.setText(context.getResources().getString(R.string.common_yuan) +data2.price);
            mtv_pricer2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
            miv_onel2.setScaleType(ImageView.ScaleType.FIT_START);
            mtv_pricer2.setText(context.getResources().getString(R.string.common_yuan) + data2.market_price);
            if (!isCircle) {
                GlideUtils.getInstance().loadImage(context, miv_onel2, data2.thumb);
            } else {
                GlideUtils.getInstance().loadBitmapSync(context, data2.thumb,
                        new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource,
                                                        GlideAnimation<? super Bitmap> glideAnimation) {
                                miv_onel2.setImageBitmap(resource);
                                successCount++;
                                if (cloadImageCount != null) {
                                    cloadImageCount.imageSuccessCount(successCount);
                                }
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                Common.staticToast("图片没有加载成功不能分享朋友圈");
                            }
                        });
            }

        }
    }
}
