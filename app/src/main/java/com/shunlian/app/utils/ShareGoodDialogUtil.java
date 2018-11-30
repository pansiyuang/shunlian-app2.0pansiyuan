package com.shunlian.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).fromBottom()
                .setView(R.layout.dialog_share);
        nomalBuildl = nomalBuild.create();
        nomalBuildl.setCancelable(false);
        nomalBuildl.show();
        nomalBuildl.setOnClickListener(R.id.ntv_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomalBuildl.dismiss();
            }
        });
        if(!shareInfoParam.isShowTiltle){
            nomalBuildl.getView(R.id.tv_title1).setVisibility(View.GONE);
            nomalBuildl.getView(R.id.tv_title2).setVisibility(View.GONE);
        }else{
            nomalBuildl.getView(R.id.tv_title1).setVisibility(View.VISIBLE);
            nomalBuildl.getView(R.id.tv_title2).setVisibility(View.VISIBLE);
        }
        if(shareInfoParam.isSpecial){
            nomalBuildl.getView(R.id.mllayout_weixinpenyou).setVisibility(View.GONE);
            nomalBuildl.getView(R.id.mllayout_tuwenerweima).setVisibility(View.GONE);
        }else{
            nomalBuildl.getView(R.id.mllayout_weixinpenyou).setVisibility(View.VISIBLE);
            nomalBuildl.getView(R.id.mllayout_tuwenerweima).setVisibility(View.VISIBLE);
        }

        nomalBuildl.setOnClickListener(R.id.mllayout_weixinhaoyou, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEddType();
                nomalBuildl.dismiss();
                if(isGood) {
                    WXEntryActivity.startAct(context,
                            "shareFriend", mShareInfoParam);
                    if(isFound&&mCallBack!=null){
//                        mCallBack.shareSuccess(mShareInfoParam.blogId,mShareInfoParam.goods_id);
                    }
                }else{
                    WXEntryActivity.startAct(context,
                            "shareFriend", mShareInfoParam);
                }
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_weixinpenyou, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEddType();
                nomalBuildl.dismiss();
                if(!TextUtils.isEmpty(mShareInfoParam.special_img_url)){
                    createSpecialCode( false);
                    return;
                }
                if(isGood) {
                    createGoodCode(isFound,true);
                    if(isFound&&mCallBack!=null){
//                        mCallBack.shareSuccess(mShareInfoParam.blogId,mShareInfoParam.goods_id);
                    }
                }else{
                    createShopCode(true);
                }
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_tuwenerweima, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEddType();
                nomalBuildl.dismiss();
                if(!TextUtils.isEmpty(mShareInfoParam.special_img_url)){
                    createSpecialCode(true);
                    return;
                }
                if(isGood) {
                    createGoodCode(isFound,false);
                }else{
                    createShopCode(false);
                }
            }


        });
        nomalBuildl.setOnClickListener(R.id.mllayout_shangping, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.copyText(context, mShareInfoParam.shareLink, mShareInfoParam.isCopyTitle ? mShareInfoParam.title : mShareInfoParam.desc, true);
                nomalBuildl.dismiss();
            }
        });
    }

    public void setShareGoods() {
        if (mCallBack != null) {
            mCallBack.shareSuccess(mShareInfoParam.blogId, mShareInfoParam.goods_id);
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
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).fromBottomToMiddle()
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
           if(isShow) {
               GlideUtils.getInstance().loadImageZheng(context, imv_special_pic, mShareInfoParam.img);
               showSpecialBuild.getView(R.id.line_share_line).setVisibility(View.VISIBLE);
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
            miv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSpecialBuild.dismiss();
                }
            });
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
//                    goodsPic(inflate,mShareInfoParam.img,true,false);
                }
            });
         mllayout_wexin.setOnClickListener(new View.OnClickListener() {
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
                                    boolean isSuccess = BitmapUtil.saveImageToAlbumn(context, resource, true, true);
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
//                goodsPic(inflate,mShareInfoParam.shop_logo,false,false);
            }
        });
    }
    /**
     * isCircleShare 是否直接分享到朋友圈
     * 创建商品视图
     */
    public void createGoodCode(boolean isFound,boolean isCircleShare) {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(context, "login");
        } else {
            final View inflate = LayoutInflater.from(context)
                    .inflate(R.layout.share_goods_new, null, false);
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).fromBottomToMiddle()
                    .setView(inflate);
            showGoodBuild = nomalBuild.create();
            showGoodBuild.setCancelable(false);
            if(!isCircleShare) {
                showGoodBuild.show();
            }
            MyImageView miv_close = showGoodBuild.findViewById(R.id.miv_close);
            MyLinearLayout mllayout_wexin = showGoodBuild.findViewById(R.id.mllayout_wexin);
            MyLinearLayout mllayout_save = showGoodBuild.findViewById(R.id.mllayout_save);
            MyImageView miv_user_head = showGoodBuild.findViewById(R.id.miv_user_head);
            MyTextView mtv_nickname = showGoodBuild.findViewById(R.id.mtv_nickname);
           TextView mtv_market_price = showGoodBuild.findViewById(R.id.mtv_market_price);
            mtv_nickname.setText("来自" + SharedPrefUtil.getSharedUserString("nickname", "") + "的分享");
            GlideUtils.getInstance().loadCircleAvar(context,miv_user_head,SharedPrefUtil.getSharedUserString("avatar", ""));
            MyImageView miv_code =  showGoodBuild.findViewById(R.id.miv_code);
            int i = TransformUtil.dip2px(context, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
            miv_code.setImageBitmap(qrImage);
            MyTextView mtv_title =  showGoodBuild.findViewById(R.id.mtv_title);
            mtv_title.setText(mShareInfoParam.title);
            if (!TextUtils.isEmpty(mShareInfoParam.market_price)) {
                mtv_market_price.setVisibility(View.VISIBLE);
                mtv_market_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                mtv_market_price.setText("￥" + mShareInfoParam.market_price);
            }else{
                mtv_market_price.setVisibility(View.VISIBLE);
            }
            MyTextView mtv_desc =  showGoodBuild.findViewById(R.id.mtv_desc);
            if (!TextUtils.isEmpty(mShareInfoParam.desc)) {
                mtv_desc.setVisibility(View.GONE);
                mtv_desc.setText(mShareInfoParam.desc);
            } else {
                mtv_desc.setVisibility(View.GONE);
            }
            MyTextView mtv_price =  showGoodBuild.findViewById(R.id.mtv_price);
            mtv_price.setText("￥" + mShareInfoParam.price);
            MyTextView mtv_goodsID =  showGoodBuild.findViewById(R.id.mtv_goodsID);
            mtv_goodsID.setText("商品编号:" + mShareInfoParam.goods_id + "(搜索可直达)");


            //显示优品图标
            MyTextView mtv_SuperiorProduct = showGoodBuild.findViewById(R.id.mtv_SuperiorProduct);
            if (mShareInfoParam.isSuperiorProduct) {
                mtv_SuperiorProduct.setVisibility(View.VISIBLE);
            } else {
                mtv_SuperiorProduct.setVisibility(View.GONE);
            }

            LinearLayout  llayout_day =showGoodBuild.findViewById(R.id.llayout_day);
            MyTextView mtv_time  = showGoodBuild.findViewById(R.id.mtv_time);
            MyTextView mtv_act_label  = showGoodBuild.findViewById(R.id.mtv_act_label);
            if (TextUtils.isEmpty(mShareInfoParam.start_time)) {
                llayout_day.setVisibility(View.GONE);
            } else {
                llayout_day.setVisibility(View.VISIBLE);
                mtv_time.setText(mShareInfoParam.start_time);
                mtv_act_label.setText(mShareInfoParam.act_label);
            }
            MyImageView miv_goods_pic =  showGoodBuild.findViewById(R.id.miv_goods_pic);
            int width = Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, 80);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) miv_goods_pic.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = width;

            if(!isCircleShare) {
                GlideUtils.getInstance().loadImageZheng(context, miv_goods_pic, mShareInfoParam.img);
                showGoodBuild.getView(R.id.line_share_line).setVisibility(View.VISIBLE);
                showGoodBuild.getView(R.id.line_share_boottom).setVisibility(View.VISIBLE);
            }else{
                miv_close.setVisibility(View.GONE);
                showGoodBuild.getView(R.id.line_share_line).setVisibility(View.GONE);
                showGoodBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
                GlideUtils.getInstance().loadBitmapSync(context, mShareInfoParam.img,
                        new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource,
                                                        GlideAnimation<? super Bitmap> glideAnimation) {
                                miv_goods_pic.setImageBitmap(resource);
                                Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                                if(bitmapByView==null){
                                    return;
                                }
                                BitmapUtil.saveImageToAlbumn(context, bitmapByView,isCircleShare,false);
                            }
                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                            }
                        });
            }
            miv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showGoodBuild.dismiss();
                }
            });

            mllayout_wexin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showGoodBuild.getView(R.id.line_share_line).setVisibility(View.GONE);
                    showGoodBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
                    miv_close.setVisibility(View.GONE);
                    showGoodBuild.dismiss();
                    goodsPic(inflate,mShareInfoParam.img,false,false);
                    if(isFound&&mCallBack!=null){
                        mCallBack.shareSuccess(mShareInfoParam.blogId,mShareInfoParam.goods_id);
                    }
                }
            });
            mllayout_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showGoodBuild.getView(R.id.line_share_line).setVisibility(View.GONE);
                    showGoodBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
                    miv_close.setVisibility(View.GONE);
                    showGoodBuild.dismiss();
                    goodsPic(inflate,mShareInfoParam.img,true,false);
                }
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
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).fromBottomToMiddle()
                    .setView(inflate);
            showShopBuild = nomalBuild.create();
            showShopBuild.setCancelable(false);
            if(!isCircleShare) {
                showShopBuild.show();
            }
           View ll_root= showShopBuild.findViewById(R.id.ll_root);
            MyImageView miv_close = showShopBuild.findViewById(R.id.miv_close);
            MyLinearLayout mllayout_save = showShopBuild.findViewById(R.id.mllayout_save);
            MyImageView miv_user_head = showShopBuild.findViewById(R.id.miv_user_head);
            MyImageView  miv_store = showShopBuild.findViewById(R.id.miv_store);
            MyTextView mtv_nickname = showShopBuild.findViewById(R.id.mtv_nickname);

            mtv_nickname.setText("来自" + SharedPrefUtil.getSharedUserString("nickname", "") + "的分享");
            GlideUtils.getInstance().loadCircleAvar(context,miv_user_head,SharedPrefUtil.getSharedUserString("avatar", ""));
            GlideUtils.getInstance().loadImageZheng(context, miv_store, mShareInfoParam.shop_logo);
            MyImageView miv_code =  showShopBuild.findViewById(R.id.miv_code);
            int i = TransformUtil.dip2px(context, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
            miv_code.setImageBitmap(qrImage);
            MyTextView mtv_title =  showShopBuild.findViewById(R.id.mtv_title);
            mtv_title.setText(mShareInfoParam.title);
            if(mShareInfoParam.share_goods!=null&&mShareInfoParam.share_goods.size()>0) {
                StoreViewUtil storeBabyAdapter = new StoreViewUtil(showShopBuild, context, mShareInfoParam.share_goods, isCircleShare, new StoreShareBabyAdapter.LoadImageCount() {
                    @Override
                    public void imageSuccessCount(int successCount) {
                        if(successCount==mShareInfoParam.share_goods.size()){
                            Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                            if(bitmapByView==null){
                                return;
                            }
                            BitmapUtil.saveImageToAlbumn(context, bitmapByView,isCircleShare,false);
                        }
                    }
                });
                storeBabyAdapter.showStoreGoodView();
            }
            if(isCircleShare) {
                miv_close.setVisibility(View.GONE);
                showShopBuild.getView(R.id.line_share_line).setVisibility(View.GONE);
                showShopBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);

            }else{
                showShopBuild.getView(R.id.line_share_line).setVisibility(View.VISIBLE);
                showShopBuild.getView(R.id.line_share_boottom).setVisibility(View.VISIBLE);
            }
            miv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showShopBuild.dismiss();
                }
            });
            mllayout_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showShopBuild.getView(R.id.line_share_line).setVisibility(View.GONE);
                    showShopBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
                    showShopBuild.getView(R.id.line_share_boottom).setMinimumHeight(0);
                    miv_close.setVisibility(View.GONE);
                    showShopBuild.dismiss();
                    goodsPic(inflate,mShareInfoParam.shop_logo,true,false);
                }
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


    public void setOnShareBlogCallBack(OnShareBlogCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnShareBlogCallBack {
        void shareSuccess(String blogId,String goodsId);
    }
}
