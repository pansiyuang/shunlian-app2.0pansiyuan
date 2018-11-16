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
import com.google.zxing.common.StringUtils;
import com.shunlian.app.R;
import com.shunlian.app.adapter.StoreShareBabyAdapter;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.dialog.CommonDialog;
import com.shunlian.app.wxapi.WXEntryActivity;

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
        this.mShareInfoParam = mShareInfoParam;
    }
    //分享商品的diolog
    public void shareGoodDialog(ShareInfoParam shareInfoParam,boolean isGood,boolean isFound) {
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
        nomalBuildl.setOnClickListener(R.id.mllayout_weixinhaoyou, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGood) {
                    WXEntryActivity.startAct(context,
                            "shareFriend", mShareInfoParam);
                    Constant.SHARE_TYPE = "goods";
                    Constant.SHARE_ID = mShareInfoParam.goods_id;
                    if(isFound&&mCallBack!=null){
                        mCallBack.shareSuccess(mShareInfoParam.blogId,mShareInfoParam.goods_id);
                    }
                    nomalBuildl.dismiss();
                }else{
                    Constant.SHARE_TYPE = "";
                    Constant.SHARE_ID = "";
                    WXEntryActivity.startAct(context,
                            "shareFriend", mShareInfoParam);
                }
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_weixinpenyou, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGood) {
                    Constant.SHARE_TYPE = "goods";
                    Constant.SHARE_ID = mShareInfoParam.goods_id;
                    WXEntryActivity.startAct(context, "shareCircle", shareInfoParam);
                    if(isFound&&mCallBack!=null){
                        mCallBack.shareSuccess(mShareInfoParam.blogId,mShareInfoParam.goods_id);
                    }
                    nomalBuildl.dismiss();
                }else{
                    Constant.SHARE_TYPE = "";
                    Constant.SHARE_ID = "";
                    WXEntryActivity.startAct(context,
                            "shareCircle", mShareInfoParam);
                }
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_tuwenerweima, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomalBuildl.dismiss();
                if(!TextUtils.isEmpty(mShareInfoParam.special_img_url)){
                    createSpecialCode();
                    return;
                }
                if(isGood) {
                    createGoodCode(isFound);
                }else{
                    createShopCode();
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

    /**
     * 创建专题图文
     */
    private void createSpecialCode() {
            final View inflate = LayoutInflater.from(context)
                    .inflate(R.layout.share_special_new, null, false);
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).fromBottomToMiddle()
                    .setView(inflate);
            showSpecialBuild = nomalBuild.create();
            showSpecialBuild.setCancelable(false);
            showSpecialBuild.show();
            MyImageView miv_close = showSpecialBuild.findViewById(R.id.miv_close);
            MyLinearLayout mllayout_save = showSpecialBuild.findViewById(R.id.mllayout_save);
            MyLinearLayout  mllayout_wexin = showSpecialBuild.findViewById(R.id.mllayout_wexin);
            MyTextView tv_title_name =  showSpecialBuild.findViewById(R.id.tv_title_name);
            MyTextView tv_title_desc =  showSpecialBuild.findViewById(R.id.tv_title_desc);
            MyImageView miv_code =  showSpecialBuild.findViewById(R.id.miv_code);
           ImageView imv_special_pic =  showSpecialBuild.findViewById(R.id.imv_special_pic);
            int i = TransformUtil.dip2px(context, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
            miv_code.setImageBitmap(qrImage);

            tv_title_name.setText(mShareInfoParam.title);
           GlideUtils.getInstance().loadImageZheng(context, imv_special_pic, mShareInfoParam.img);

           if(mShareInfoParam.desc!=null){
               tv_title_desc.setText(mShareInfoParam.desc);
           }
           showSpecialBuild.getView(R.id.line_share_line).setVisibility(View.VISIBLE);
           showSpecialBuild.getView(R.id.line_share_boottom).setVisibility(View.VISIBLE);
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
                    showSpecialBuild.dismiss();
                    goodsPic(inflate,mShareInfoParam.img,true);
                }
            });
         mllayout_wexin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpecialBuild.getView(R.id.line_share_line).setVisibility(View.GONE);
                showSpecialBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
                miv_close.setVisibility(View.GONE);
                showSpecialBuild.dismiss();
                goodsPic(inflate,mShareInfoParam.shop_logo,false);
            }
        });
    }
    /**
     * 创建商品视图
     */
    public void createGoodCode(boolean isFound) {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(context, "login");
        } else {
            final View inflate = LayoutInflater.from(context)
                    .inflate(R.layout.share_goods_new, null, false);
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).fromBottomToMiddle()
                    .setView(inflate);
            showGoodBuild = nomalBuild.create();
            showGoodBuild.setCancelable(false);
            showGoodBuild.show();
            MyImageView miv_close = showGoodBuild.findViewById(R.id.miv_close);
            MyLinearLayout mllayout_wexin = showGoodBuild.findViewById(R.id.mllayout_wexin);
            MyLinearLayout mllayout_save = showGoodBuild.findViewById(R.id.mllayout_save);
            MyImageView miv_user_head = showGoodBuild.findViewById(R.id.miv_user_head);
            MyTextView mtv_nickname = showGoodBuild.findViewById(R.id.mtv_nickname);
           TextView mtv_market_price = showGoodBuild.findViewById(R.id.mtv_market_price);
            mtv_nickname.setText("来自" + SharedPrefUtil.getSharedUserString("nickname", "") + "的分享");
            GlideUtils.getInstance().loadCircleAvar(context,miv_user_head,SharedPrefUtil.getSharedUserString("nickname", ""));
            MyImageView miv_code =  showGoodBuild.findViewById(R.id.miv_code);
            int i = TransformUtil.dip2px(context, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
            miv_code.setImageBitmap(qrImage);

            MyTextView mtv_title =  showGoodBuild.findViewById(R.id.mtv_title);
            mtv_title.setText(mShareInfoParam.title);
            if (!TextUtils.isEmpty(mShareInfoParam.market_price)) {
                mtv_market_price.setVisibility(View.VISIBLE);
                mtv_market_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                mtv_market_price.setText(mShareInfoParam.market_price);
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
            MyImageView miv_SuperiorProduct = showGoodBuild.findViewById(R.id.miv_SuperiorProduct);
            if (mShareInfoParam.isSuperiorProduct) {
                miv_SuperiorProduct.setVisibility(View.VISIBLE);
            } else {
                miv_SuperiorProduct.setVisibility(View.GONE);
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
            int width = Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, 120);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) miv_goods_pic.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = width;
            GlideUtils.getInstance().loadImageZheng(context, miv_goods_pic, mShareInfoParam.img);
            showGoodBuild.getView(R.id.line_share_line).setVisibility(View.VISIBLE);
            showGoodBuild.getView(R.id.line_share_boottom).setVisibility(View.VISIBLE);
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
                    goodsPic(inflate,mShareInfoParam.img,false);
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
                    goodsPic(inflate,mShareInfoParam.img,true);
                }
            });
        }
    }

    /**
     * 创建商品视图
     */
    public void createShopCode() {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(context, "login");
        } else {
            final View inflate = LayoutInflater.from(context)
                    .inflate(R.layout.share_shop_new, null, false);
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).fromBottomToMiddle()
                    .setView(inflate);
            showShopBuild = nomalBuild.create();
            showShopBuild.setCancelable(false);
            showShopBuild.show();
            MyImageView miv_close = showShopBuild.findViewById(R.id.miv_close);
            MyLinearLayout mllayout_save = showShopBuild.findViewById(R.id.mllayout_save);
            MyImageView miv_user_head = showShopBuild.findViewById(R.id.miv_user_head);
            MyImageView  miv_store = showShopBuild.findViewById(R.id.miv_store);
            MyTextView mtv_nickname = showShopBuild.findViewById(R.id.mtv_nickname);


            mtv_nickname.setText("来自" + SharedPrefUtil.getSharedUserString("nickname", "") + "的分享");
            GlideUtils.getInstance().loadCircleAvar(context,miv_user_head,SharedPrefUtil.getSharedUserString("nickname", ""));
            GlideUtils.getInstance().loadImageZheng(context, miv_store, mShareInfoParam.shop_logo);
            MyImageView miv_code =  showShopBuild.findViewById(R.id.miv_code);
            int i = TransformUtil.dip2px(context, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
            miv_code.setImageBitmap(qrImage);
            MyTextView mtv_title =  showShopBuild.findViewById(R.id.mtv_title);
            mtv_title.setText(mShareInfoParam.title);
            RecyclerView recycler_shop = showShopBuild.findViewById(R.id.recycler_shop);
            if(mShareInfoParam.share_goods!=null&&mShareInfoParam.share_goods.size()>0) {
                recycler_shop.setVisibility(View.VISIBLE);
                StoreShareBabyAdapter storeBabyAdapter = new StoreShareBabyAdapter(context, true, mShareInfoParam.share_goods);
                GridLayoutManager babyManager = new GridLayoutManager(context, 2);
                recycler_shop.setLayoutManager(babyManager);
                recycler_shop.addItemDecoration(new GrideItemDecoration(0, 0, TransformUtil.dip2px(context, 5), TransformUtil.dip2px(context, 5), true));
                recycler_shop.setAdapter(storeBabyAdapter);
            }else{
                recycler_shop.setVisibility(View.GONE);
            }

            showShopBuild.getView(R.id.line_share_line).setVisibility(View.VISIBLE);
            showShopBuild.getView(R.id.line_share_boottom).setVisibility(View.VISIBLE);
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
                    miv_close.setVisibility(View.GONE);
                    showShopBuild.dismiss();
                    goodsPic(inflate,mShareInfoParam.shop_logo,true);
                }
            });
        }
    }

    private void goodsPic(View inflate,String img,boolean isShow) {
        GlideUtils.getInstance().loadBitmapSync(context, img,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                            Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                        if (isShow) {
                            boolean isSuccess = BitmapUtil.saveImageToAlbumn(context, bitmapByView,false,false);
                            if (isSuccess) {
                                Common.staticToast(context.getString(R.string.operate_tupianyibaocun));
                            } else {
                                Common.staticToast(context.getString(R.string.operate_tupianbaocunshibai));
                            }
                        } else {
                            boolean isSuccess = BitmapUtil.saveImageToAlbumn(context, bitmapByView,true,true);
                            if (!isSuccess)
                                Common.staticToast("分享失败");
                        }
                    }
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        if (isShow) {
                            Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                            boolean isSuccess = BitmapUtil.saveImageToAlbumn(context, bitmapByView,false,false);
                            if (isSuccess) {
                                Common.staticToast(context.getString(R.string.operate_tupianyibaocun));
                            } else {
                                Common.staticToast(context.getString(R.string.operate_tupianbaocunshibai));
                            }
                        } else {
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
