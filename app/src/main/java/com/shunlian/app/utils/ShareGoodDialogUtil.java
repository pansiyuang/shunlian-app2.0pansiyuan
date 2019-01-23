package com.shunlian.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.common.StringUtils;
import com.shunlian.app.App;
import com.shunlian.app.R;
import com.shunlian.app.adapter.StoreShareBabyAdapter;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.ui.myself_store.MyLittleStoreActivity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.dialog.CommonDialog;
import com.shunlian.app.wxapi.WXEntryActivity;
import com.zh.chartlibrary.common.DensityUtil;

public class ShareGoodDialogUtil {
    private ShareInfoParam mShareInfoParam;
    public Context context;
    private OnShareBlogCallBack mCallBack;
    public CommonDialog nomalBuildl;
    public CommonDialog showGoodBuild;
    public CommonDialog showShopBuild;
    public CommonDialog showSpecialBuild;
    public ShareGoodDialogUtil(Context context){
        this.context = context;
    }

    public void setShareInfoParam(ShareInfoParam shareInfoParam){
        this.mShareInfoParam = shareInfoParam;
    }

    //分享商品的diolog
    public void shareGoodDialog(ShareInfoParam shareInfoParam,boolean isGood,boolean isFound) {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(context, "login");
            return;
        }
        if(nomalBuildl!=null&&nomalBuildl.isShowing()){
            nomalBuildl.dismiss();
        }
        this.mShareInfoParam = shareInfoParam;
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context).fromBottom().fullWidth()
                .setView(R.layout.dialog_share);
        nomalBuildl = nomalBuild.create();
        nomalBuildl.setCancelable(true);
        nomalBuildl.setCanceledOnTouchOutside(true);
        nomalBuildl.show();
        nomalBuildl.getWindow().getDecorView().setOnTouchListener((v, event) -> {
            nomalBuildl.dismiss();
            return false;
        });
        nomalBuildl.setOnClickListener(R.id.ntv_cancel, v -> nomalBuildl.dismiss());
        if(shareInfoParam.isSpecial){
            nomalBuildl.getView(R.id.mllayout_weixinpenyou).setVisibility(View.GONE);
            nomalBuildl.getView(R.id.mllayout_tuwenerweima).setVisibility(View.GONE);
        }else{
            nomalBuildl.getView(R.id.mllayout_weixinpenyou).setVisibility(View.VISIBLE);
            nomalBuildl.getView(R.id.mllayout_tuwenerweima).setVisibility(View.VISIBLE);
        }
        TextView tv_price_state =  nomalBuildl.getView(R.id.tv_price_state);
        LinearLayout line_share_title =  nomalBuildl.getView(R.id.line_share_title);

        if (!TextUtils.isEmpty(mShareInfoParam.share_buy_earn)&&isGood){
            line_share_title.setVisibility(View.VISIBLE);
            tv_price_state.setText(mShareInfoParam.share_buy_earn);
        }else {
            line_share_title.setVisibility(View.GONE);
        }
        nomalBuildl.setOnClickListener(R.id.mllayout_weixinhaoyou, v -> {
            setEddType();
            Common.copyTextNoToast(context,mShareInfoParam.title);
            nomalBuildl.dismiss();
            if(isGood) {
                WXEntryActivity.startAct(context,
                        "shareFriend", mShareInfoParam);
            }else{
                WXEntryActivity.startAct(context,
                        "shareFriend", mShareInfoParam);
            }
            if(mShareInfoParam.cate1!=null&&mShareInfoParam.shop_id!=null){
                JosnSensorsDataAPI.shareGoodClick(mShareInfoParam.goods_id,mShareInfoParam.title,mShareInfoParam.cate1,mShareInfoParam.cate2,
                        mShareInfoParam.price,mShareInfoParam.shop_id,mShareInfoParam.shop_name,"微信好友");
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_weixinpenyou, v -> {
            setEddType();
            Common.copyTextNoToast(context,mShareInfoParam.title);
            nomalBuildl.dismiss();
            if(!TextUtils.isEmpty(mShareInfoParam.special_img_url)){
                createSpecialCode( false);
                return;
            }
            if(isGood) {
//                createGoodCode(isFound,true);
                createFullGoodCode(mShareInfoParam,true,false);
            }else{
                createShopCode(true);
            }
            if(mShareInfoParam.cate1!=null&&mShareInfoParam.shop_id!=null){
                JosnSensorsDataAPI.shareGoodClick(mShareInfoParam.goods_id,mShareInfoParam.title,mShareInfoParam.cate1,mShareInfoParam.cate2,
                        mShareInfoParam.price,mShareInfoParam.shop_id,mShareInfoParam.shop_name,"朋友圈");
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_tuwenerweima, v -> {
            setEddType();
            nomalBuildl.dismiss();
            Common.copyTextNoToast(context,mShareInfoParam.title);
            if(!TextUtils.isEmpty(mShareInfoParam.special_img_url)){
                createSpecialCode(true);
                return;
            }
            if(isGood) {
//                createGoodCode(isFound,false);
                createGoodCode();
            }else{
                createShopCode(false);
            }
            if(mShareInfoParam.cate1!=null&&mShareInfoParam.shop_id!=null){
                JosnSensorsDataAPI.shareGoodClick(mShareInfoParam.goods_id,mShareInfoParam.title,mShareInfoParam.cate1,mShareInfoParam.cate2,mShareInfoParam.price,
                        mShareInfoParam.shop_id,mShareInfoParam.shop_name,"图文");
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_shangping, v -> {
            Common.copyText(context, mShareInfoParam.shareLink, mShareInfoParam.isCopyTitle ? mShareInfoParam.title : mShareInfoParam.desc, true);
            nomalBuildl.dismiss();
            if(mShareInfoParam.cate1!=null&&mShareInfoParam.shop_id!=null){
                JosnSensorsDataAPI.shareGoodClick(mShareInfoParam.goods_id,mShareInfoParam.title,mShareInfoParam.cate1,mShareInfoParam.cate2,
                        mShareInfoParam.price,mShareInfoParam.shop_id,mShareInfoParam.shop_name,"复制链接");
            }
        });
    }

    public void setShareGoods() {
        if(Common.isAlreadyLogin()){
            if (mCallBack != null) {
                mCallBack.shareSuccess(mShareInfoParam.blogId, mShareInfoParam.goods_id);
            }
        }else {
            Common.goGoGo(context,"login");
        }
    }

    private void setEddType(){
        if(mShareInfoParam.egg_type==1){
            Constant.SHARE_TYPE = "goods";
            Constant.SHARE_ID = mShareInfoParam.goods_id;
        }else if(mShareInfoParam.egg_type==2){
            Constant.SHARE_TYPE = "store";
            Constant.SHARE_ID = mShareInfoParam.shop_id;
        }else{
            Constant.SHARE_TYPE = "";
        }
    }
    /**
     * isShow 是否可见 不可见直接分享朋友圈
     * 创建专题图文
     */
    private void createSpecialCode(boolean isShow) {
            final View inflate = LayoutInflater.from(context)
                    .inflate(R.layout.share_special_new, null, false);
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,250))
                    .setView(inflate);
            showSpecialBuild = nomalBuild.create();
            showSpecialBuild.setCancelable(false);
            if(isShow) {
                showSpecialBuild.show();
            }
            MyImageView miv_close = showSpecialBuild.findViewById(R.id.miv_close);
            MyLinearLayout mllayout_save = showSpecialBuild.findViewById(R.id.mllayout_save);
            MyLinearLayout  mllayout_wexin = showSpecialBuild.findViewById(R.id.mllayout_wexin);
           ImageView imv_special_pic =  showSpecialBuild.findViewById(R.id.imv_special_pic);
           RelativeLayout img_hight=  showSpecialBuild.findViewById(R.id.img_hight);
           LinearLayout.LayoutParams layoutParams =( (LinearLayout.LayoutParams)img_hight.getLayoutParams());
          layoutParams.weight = DensityUtil.dip2px(context,250);
          layoutParams.height =DensityUtil.dip2px(context,250)*16/9-DensityUtil.dip2px(context,20);
           if(isShow) {
               GlideUtils.getInstance().loadCornerImage(context, imv_special_pic, mShareInfoParam.img,5);
               showSpecialBuild.getView(R.id.line_share_line).setVisibility(View.GONE);
               showSpecialBuild.getView(R.id.line_share_boottom).setVisibility(View.VISIBLE);
           }else{
               showSpecialBuild.getView(R.id.line_share_line).setVisibility(View.GONE);
               showSpecialBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
               miv_close.setVisibility(View.GONE);
               GlideUtils.getInstance().loadBitmapSync(context, mShareInfoParam.img,
                       new SimpleTarget<Bitmap>() {
                           @Override
                           public void onResourceReady(Bitmap resource,
                                                       GlideAnimation<? super Bitmap> glideAnimation) {
                               imv_special_pic.setImageBitmap(resource);
                               BitmapUtil.saveImageToAlbumn(context, resource,true,false);
                           }
                           @Override
                           public void onLoadFailed(Exception e, Drawable errorDrawable) {
                               super.onLoadFailed(e, errorDrawable);
                           }
                       });
           }
            miv_close.setOnClickListener(view -> showSpecialBuild.dismiss());
            mllayout_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSpecialBuild.getView(R.id.line_share_line).setVisibility(View.GONE);
                    showSpecialBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
                    miv_close.setVisibility(View.GONE);
                    GlideUtils.getInstance().loadBitmapSync(context, mShareInfoParam.img,
                            new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource,
                                                            GlideAnimation<? super Bitmap> glideAnimation) {
                                    if(resource!=null) {
                                        boolean isSuccess = BitmapUtil.saveImageToAlbumn(context, resource, false, false);
                                        if (isSuccess) {
                                            Common.staticToast(context.getString(R.string.operate_tupianyibaocun));
                                        } else {
                                            Common.staticToast(context.getString(R.string.operate_tupianbaocunshibai));
                                        }
                                    }
                                }
                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    super.onLoadFailed(e, errorDrawable);
                                }
                            });
                    showSpecialBuild.dismiss();
                }
            });
         mllayout_wexin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpecialBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
                miv_close.setVisibility(View.GONE);
                GlideUtils.getInstance().loadBitmapSync(context, mShareInfoParam.img,
                        new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource,
                                                        GlideAnimation<? super Bitmap> glideAnimation) {
                                if(resource!=null) {
                                     BitmapUtil.saveImageToAlbumn(context, resource, true, true);
                                }
                            }
                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                            }
                        });
                showSpecialBuild.dismiss();
            }
        });
    }
    /**
     * isCircleShare 是否直接分享到朋友圈
     * 创建商品视图
     */
    public void createGoodCode() {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(context, "login");
        } else {
            final View inflate = LayoutInflater.from(context)
                    .inflate(R.layout.share_goods_new, null, false);
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).
                    setWidth(Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, 100))
                    .setView(inflate);
            showGoodBuild = nomalBuild.create();
            showGoodBuild.setCancelable(false);
//            if(!isCircleShare) {
            showGoodBuild.show();
//            }
            MyImageView miv_close = showGoodBuild.findViewById(R.id.miv_close);
            MyLinearLayout mllayout_wexin = showGoodBuild.findViewById(R.id.mllayout_wexin);
            MyLinearLayout mllayout_save = showGoodBuild.findViewById(R.id.mllayout_save);
            MyImageView miv_code =  showGoodBuild.findViewById(R.id.miv_code);
            int i = TransformUtil.dip2px(context, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
            miv_code.setImageBitmap(qrImage);
            MyTextView mtv_title =  showGoodBuild.findViewById(R.id.mtv_title);
            MyTextView  mtv_coupon_title =  showGoodBuild.findViewById(R.id.mtv_coupon_title);
            RelativeLayout relt_share_image = showGoodBuild.findViewById(R.id.relt_share_image);
            if(!TextUtils.isEmpty(mShareInfoParam.voucher)){
                mtv_coupon_title.setVisibility(View.VISIBLE);
                mtv_coupon_title.setText(mShareInfoParam.voucher);
                if(mShareInfoParam.voucher.length()>2) {
                    SpannableStringBuilder span = new SpannableStringBuilder((mShareInfoParam.voucher).substring(0, mShareInfoParam.voucher.length() - 1) + mShareInfoParam.title);
                    span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, (mShareInfoParam.voucher).length() - 1,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    mtv_title.setText(span);
                }else{
                    SpannableStringBuilder span = new SpannableStringBuilder(mShareInfoParam.voucher.substring(0, mShareInfoParam.voucher.length() - 1) + mShareInfoParam.title);
                    span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, mShareInfoParam.voucher.length() - 1,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    mtv_title.setText(span);
                }
            }else{
               mtv_title.setText(mShareInfoParam.title);
                mtv_coupon_title.setVisibility(View.GONE);
            }

            //不是新用户商品显示价格
            LinearLayout line_old_user= showGoodBuild.findViewById(R.id.line_old_user);
            MyTextView mtv_price =  showGoodBuild.findViewById(R.id.mtv_price);
            //新用户商品显示价格
            RelativeLayout re_newuser_layout = showGoodBuild.findViewById(R.id.re_newuser_layout);
            MyTextView mtv_newuser_price = showGoodBuild.findViewById(R.id.mtv_newuser_price);
            MyTextView mtv_newuser_mark_price = showGoodBuild.findViewById(R.id.mtv_newuser_mark_price);

            if(mShareInfoParam.isNewUserGood) {
                relt_share_image.setPadding(DensityUtil.dip2px(context,12),0,DensityUtil.dip2px(context,12),0);
                line_old_user.setVisibility(View.GONE);
                re_newuser_layout.setVisibility(View.VISIBLE);
                mtv_newuser_price.setText(context.getResources().getString(R.string.common_yuan)+mShareInfoParam.price);
                if (!TextUtils.isEmpty(mShareInfoParam.market_price)) {
                    mtv_newuser_mark_price.setVisibility(View.VISIBLE);
                    mtv_newuser_mark_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    mtv_newuser_mark_price.setText(context.getResources().getString(R.string.common_yuan)+ mShareInfoParam.market_price);
                } else {
                    mtv_newuser_mark_price.setVisibility(View.VISIBLE);
                }
            }else{
                re_newuser_layout.setVisibility(View.GONE);
                line_old_user.setVisibility(View.VISIBLE);
                mtv_price.setText(Common.dotPointAfterSmall(mShareInfoParam.price,11));
            }
            LinearLayout  llayout_day =showGoodBuild.findViewById(R.id.llayout_day);
            MyTextView mtv_time  = showGoodBuild.findViewById(R.id.mtv_time);
            MyTextView mtv_act_label  = showGoodBuild.findViewById(R.id.mtv_act_label);

            if (TextUtils.isEmpty(mShareInfoParam.time_text)||mShareInfoParam.isNewUserGood) {
                llayout_day.setVisibility(View.GONE);
            } else if(!TextUtils.isEmpty(mShareInfoParam.time_text)){
                llayout_day.setVisibility(View.VISIBLE);
                if(mShareInfoParam.is_start==0){
                    llayout_day.setBackgroundResource(R.drawable.edge_007aff_1px);
                    mtv_act_label.setTextColor(context.getResources().getColor(R.color.value_007AFF));
                    mtv_time.setTextColor(context.getResources().getColor(R.color.white));
                    mtv_time.setBackgroundResource(R.drawable.edge_007aff_right_1px);
                }else{
                    llayout_day.setBackgroundResource(R.drawable.edge_pink_1px);
                    mtv_act_label.setTextColor(context.getResources().getColor(R.color.pink_color));
                    mtv_time.setTextColor(context.getResources().getColor(R.color.white));
                    mtv_time.setBackgroundResource(R.drawable.edge_pink_right_1px);
                }
                mtv_time.setText(mShareInfoParam.time_text);
                mtv_act_label.setText(mShareInfoParam.little_word);
            }
            MyImageView miv_goods_pic =  showGoodBuild.findViewById(R.id.miv_goods_pic);
            int width = Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, mShareInfoParam.isNewUserGood?124:100);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) miv_goods_pic.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = width;

//            if(!isCircleShare) {
                GlideUtils.getInstance().loadImageZheng(context, miv_goods_pic, mShareInfoParam.img);
                showGoodBuild.getView(R.id.line_share_boottom).setVisibility(View.VISIBLE);
//            }else{
//                miv_close.setVisibility(View.GONE);
//                showGoodBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
//                GlideUtils.getInstance().loadBitmapSync(context, mShareInfoParam.img,
//                        new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource,
//                                                        GlideAnimation<? super Bitmap> glideAnimation) {
//                                int width = Common.getScreenWidth((Activity) context);
//                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) miv_goods_pic.getLayoutParams();
//                                layoutParams.width = width;
//                                layoutParams.height = width;
//
//                                miv_goods_pic.setImageBitmap(resource);
//                                Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
//                                if(bitmapByView==null){
//                                    return;
//                                }
//                                BitmapUtil.saveImageToAlbumn(context, bitmapByView,isCircleShare,false);
//                            }
//                            @Override
//                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                super.onLoadFailed(e, errorDrawable);
//                            }
//                        });
//            }
            miv_close.setOnClickListener(view -> showGoodBuild.dismiss());

            mllayout_wexin.setOnClickListener(view -> {
//                int width1 = Common.getScreenWidth((Activity) context);
//                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) miv_goods_pic.getLayoutParams();
//                layoutParams1.width = width1;
//                layoutParams1.height = width1;
//                showGoodBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
//                miv_close.setVisibility(View.GONE);
                showGoodBuild.dismiss();
//                goodsPic(inflate,mShareInfoParam.img,false,false);
                createFullGoodCode(mShareInfoParam,true,true);
                if(mCallBack!=null){
                    mCallBack.shareSuccess(mShareInfoParam.blogId,mShareInfoParam.goods_id);
                }
            });
            mllayout_save.setOnClickListener(view -> {
//                int width12 = Common.getScreenWidth((Activity) context);
//                RelativeLayout.LayoutParams layoutParams12 = (RelativeLayout.LayoutParams) miv_goods_pic.getLayoutParams();
//                layoutParams12.width = width12;
//                layoutParams12.height = width12;
//                showGoodBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
//                miv_close.setVisibility(View.GONE);
                showGoodBuild.dismiss();
//                goodsPic(inflate,mShareInfoParam.img,true,false);
                createFullGoodCode(mShareInfoParam,false,false);
            });
        }
    }

    /**
     * isCircleShare 是否直接分享到朋友圈
     * 创建店铺视图
     */
    public void createShopCode(boolean isCircleShare) {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(context, "login");
        } else {
            final View inflate = LayoutInflater.from(context)
                    .inflate(R.layout.share_shop_new, null, false);
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd)
                    .setView(inflate);
            showShopBuild = nomalBuild.create();
            showShopBuild.setCancelable(false);
            if(!isCircleShare) {
                showShopBuild.show();
            }
           View ll_root= showShopBuild.findViewById(R.id.ll_root);
            MyImageView miv_close = showShopBuild.findViewById(R.id.miv_close);
            MyLinearLayout mllayout_save = showShopBuild.findViewById(R.id.mllayout_save);

            MyImageView  miv_store = showShopBuild.findViewById(R.id.miv_store);

//            MyImageView miv_user_head = showShopBuild.findViewById(R.id.miv_user_head);
//            MyTextView mtv_nickname = showShopBuild.findViewById(R.id.mtv_nickname);
//            mtv_nickname.setText("来自" + SharedPrefUtil.getSharedUserString("nickname", "") + "的分享");
//            GlideUtils.getInstance().loadCircleAvar(context,miv_user_head,SharedPrefUtil.getSharedUserString("avatar", ""));
            StoreViewUtil storeBabyAdapter = null;
            GlideUtils.getInstance().loadCornerImage(context, miv_store, mShareInfoParam.shop_logo);
            MyImageView miv_code =  showShopBuild.findViewById(R.id.miv_code);
            int i = TransformUtil.dip2px(context, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
            miv_code.setImageBitmap(qrImage);
            MyTextView mtv_title =  showShopBuild.findViewById(R.id.mtv_title);
            mtv_title.setText(mShareInfoParam.title);
            if(mShareInfoParam.share_goods!=null&&mShareInfoParam.share_goods.size()>0) {
                 storeBabyAdapter = new StoreViewUtil(showShopBuild, context, mShareInfoParam.share_goods, isCircleShare, successCount -> {
                    if(successCount==mShareInfoParam.share_goods.size()){
                        Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                        if(bitmapByView==null){
                            return;
                        }
                        if(isCircleShare) {
                            BitmapUtil.saveImageToAlbumn(context, bitmapByView, isCircleShare, false);
                        }
                    }
                });
                storeBabyAdapter.showStoreGoodView();
            }
            showShopBuild.getView(R.id.line_share_line).setVisibility(View.GONE);
            if(isCircleShare) {
                miv_close.setVisibility(View.GONE);
                showShopBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
                if(storeBabyAdapter !=null) {
                    storeBabyAdapter.updateImageHight();
                }
            }else{
                showShopBuild.getView(R.id.line_share_boottom).setVisibility(View.VISIBLE);
            }
            miv_close.setOnClickListener(view -> showShopBuild.dismiss());
            StoreViewUtil finalStoreBabyAdapter = storeBabyAdapter;
            mllayout_save.setOnClickListener(view -> {
                if(finalStoreBabyAdapter !=null) {
                    finalStoreBabyAdapter.updateImageHight();
                }
                showShopBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
                showShopBuild.getView(R.id.line_share_boottom).setMinimumHeight(0);
                miv_close.setVisibility(View.GONE);
                showShopBuild.dismiss();
                goodsPic(inflate,mShareInfoParam.shop_logo,true,false);
            });
        }
    }

    private void goodsPic(View inflate,String img,boolean isShowSaveToast,boolean isCircleShare) {
        GlideUtils.getInstance().loadBitmapSync(context, img,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                            Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                            if(bitmapByView==null){
                                return;
                            }
                          if (isShowSaveToast) {
                            boolean isSuccess = BitmapUtil.saveImageToAlbumn(context, bitmapByView,false,!isCircleShare);
                            if (isSuccess) {
                                Common.staticToast(context.getString(R.string.operate_tupianyibaocun));
                            } else {
                                Common.staticToast(context.getString(R.string.operate_tupianbaocunshibai));
                            }
                        } else {
                            boolean isSuccess = BitmapUtil.saveImageToAlbumn(context, bitmapByView,true,!isCircleShare);
                            if (!isSuccess)
                                Common.staticToast("分享失败");
                        }
                    }
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        if (isShowSaveToast) {
                            Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                            if(bitmapByView==null){
                                return;
                            }
                            boolean isSuccess = BitmapUtil.saveImageToAlbumn(context, bitmapByView,false,!isCircleShare);
                            if (isSuccess) {
                                Common.staticToast(context.getString(R.string.operate_tupianyibaocun));
                            } else {
                                Common.staticToast(context.getString(R.string.operate_tupianbaocunshibai));
                            }
                        } else {
                            Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                            if(bitmapByView==null){
                                return;
                            }
                            boolean isSuccess = BitmapUtil.saveImageToAlbumn(context, bitmapByView,true,!isCircleShare);
                            if (!isSuccess)
                                Common.staticToast("分享失败");
                        }
                    }
                });
    }


    public void createFullGoodCode(ShareInfoParam mShareInfoParam,boolean isShare,boolean isFriend) {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(context, "login");
        } else {
            final View inflate = LayoutInflater.from(context).inflate(R.layout.share_goods_new_full, null, false);
            MyImageView miv_code = inflate.findViewById(R.id.miv_code);
            int i = TransformUtil.dip2px(context, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
            miv_code.setImageBitmap(qrImage);
            MyTextView mtv_title =  inflate.findViewById(R.id.mtv_title);
            MyTextView  mtv_coupon_title =  inflate.findViewById(R.id.mtv_coupon_title);
            RelativeLayout relt_share_image = inflate.findViewById(R.id.relt_share_image);
            if(!TextUtils.isEmpty(mShareInfoParam.voucher)){
                mtv_coupon_title.setVisibility(View.VISIBLE);
                mtv_coupon_title.setText(mShareInfoParam.voucher);
                SpannableStringBuilder span = new SpannableStringBuilder(mShareInfoParam.voucher+mShareInfoParam.title);
                span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, mShareInfoParam.voucher.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mtv_title.setText(span);
            }else{
                mtv_title.setText(mShareInfoParam.title);
                mtv_coupon_title.setVisibility(View.GONE);
            }
            //不是新用户商品显示价格
            LinearLayout line_old_user= inflate.findViewById(R.id.line_old_user);
            MyTextView mtv_price =  inflate.findViewById(R.id.mtv_price);
            //新用户商品显示价格
            RelativeLayout re_newuser_layout = inflate.findViewById(R.id.re_newuser_layout);
            MyTextView mtv_newuser_price = inflate.findViewById(R.id.mtv_newuser_price);
            MyTextView mtv_newuser_mark_price = inflate.findViewById(R.id.mtv_newuser_mark_price);

            if(mShareInfoParam.isNewUserGood) {
                relt_share_image.setPadding(DensityUtil.dip2px(context,15),0,DensityUtil.dip2px(context,15),0);
                line_old_user.setVisibility(View.GONE);
                re_newuser_layout.setVisibility(View.VISIBLE);
                mtv_newuser_price.setText(context.getResources().getString(R.string.common_yuan)+mShareInfoParam.price);
                if (!TextUtils.isEmpty(mShareInfoParam.market_price)) {
                    mtv_newuser_mark_price.setVisibility(View.VISIBLE);
                    mtv_newuser_mark_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    mtv_newuser_mark_price.setText(context.getResources().getString(R.string.common_yuan)+ mShareInfoParam.market_price);
                } else {
                    mtv_newuser_mark_price.setVisibility(View.VISIBLE);
                }
            }else{
                re_newuser_layout.setVisibility(View.GONE);
                line_old_user.setVisibility(View.VISIBLE);
                mtv_price.setText(Common.dotPointAfterSmall(mShareInfoParam.price,13));
            }
            LinearLayout  llayout_day =inflate.findViewById(R.id.llayout_day);
            MyTextView mtv_time  = inflate.findViewById(R.id.mtv_time);
            MyTextView mtv_act_label  = inflate.findViewById(R.id.mtv_act_label);

            if (TextUtils.isEmpty(mShareInfoParam.time_text)||mShareInfoParam.isNewUserGood) {
                llayout_day.setVisibility(View.GONE);
            } else if(!TextUtils.isEmpty(mShareInfoParam.time_text)){
                llayout_day.setVisibility(View.VISIBLE);
                if(mShareInfoParam.is_start==0){
                    llayout_day.setBackgroundResource(R.drawable.edge_007aff_1px);
                    mtv_act_label.setTextColor(context.getResources().getColor(R.color.value_007AFF));
                    mtv_time.setTextColor(context.getResources().getColor(R.color.white));
                    mtv_time.setBackgroundResource(R.drawable.edge_007aff_right_1px);
                }else{
                    llayout_day.setBackgroundResource(R.drawable.edge_pink_1px);
                    mtv_act_label.setTextColor(context.getResources().getColor(R.color.pink_color));
                    mtv_time.setTextColor(context.getResources().getColor(R.color.white));
                    mtv_time.setBackgroundResource(R.drawable.edge_pink_right_1px);
                }
                mtv_time.setText(mShareInfoParam.time_text);
                mtv_act_label.setText(mShareInfoParam.little_word);
            }

            MyImageView miv_goods_pic =  inflate.findViewById(R.id.miv_goods_pic);
            int width = Common.getScreenWidth((Activity) context);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) miv_goods_pic.getLayoutParams();
            if(mShareInfoParam.isNewUserGood) {
                layoutParams.width = width-DensityUtil.dip2px(context,30);
                layoutParams.height = width-DensityUtil.dip2px(context,30);
            }else{
                layoutParams.width = width;
                layoutParams.height = width;
            }
            GlideUtils.getInstance().loadBitmapSync(context, mShareInfoParam.img,
                    new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource,
                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                            miv_goods_pic.setImageBitmap(resource);
                            Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                            if (bitmapByView == null) {
                                return;
                            }
                            boolean isSuccess =   BitmapUtil.saveImageToAlbumn(context, bitmapByView, isShare, isFriend);
                            if (!isShare) {
                                if (isSuccess) {
                                    Common.staticToast(context.getString(R.string.operate_tupianyibaocun));
                                } else {
                                    Common.staticToast(context.getString(R.string.operate_tupianbaocunshibai));
                                }
                            } else {
                                if (!isSuccess)
                                    Common.staticToast("分享失败");
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                            if (bitmapByView == null) {
                                return;
                            }
                            boolean isSuccess =   BitmapUtil.saveImageToAlbumn(context, bitmapByView, isShare, isFriend);
                            if (!isShare) {
                                if (isSuccess) {
                                    Common.staticToast(context.getString(R.string.operate_tupianyibaocun));
                                } else {
                                    Common.staticToast(context.getString(R.string.operate_tupianbaocunshibai));
                                }
                            } else {
                                if (!isSuccess)
                                    Common.staticToast("分享失败");
                            }
                        }
                    });
        }
    }



    public void setOnShareBlogCallBack(OnShareBlogCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnShareBlogCallBack {
        void shareSuccess(String blogId,String goodsId);
    }
}
