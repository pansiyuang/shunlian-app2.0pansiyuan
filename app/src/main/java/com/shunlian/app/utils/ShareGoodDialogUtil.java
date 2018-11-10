package com.shunlian.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.dialog.CommonDialog;
import com.shunlian.app.wxapi.WXEntryActivity;

public class ShareGoodDialogUtil {
    private ShareInfoParam mShareInfoParam;
    public Context context;

    public CommonDialog nomalBuildl;
    public CommonDialog showGoodBuild;
    public ShareGoodDialogUtil(Context context){
        this.context = context;
    }

    //分享商品的diolog
    public void shareGoodDialog(ShareInfoParam shareInfoParam,boolean isGood) {
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
                    nomalBuildl.dismiss();
                }
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_weixinpenyou, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGood) {
                    WXEntryActivity.startAct(context, "shareCircle", shareInfoParam);
                    Constant.SHARE_TYPE = "goods";
                    Constant.SHARE_ID = mShareInfoParam.goods_id;
                    nomalBuildl.dismiss();
                }
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_tuwenerweima, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGood) {
                    createCode();
                }
//                nomalBuildl.dismiss();
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


    public void createCode() {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(context, "login");
        } else {
            final View inflate = LayoutInflater.from(context)
                    .inflate(R.layout.share_goods_new, null, false);
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd)
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
                    showGoodBuild.dismiss();
                    goodsPic(inflate,false);
                }
            });
            mllayout_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showGoodBuild.getView(R.id.line_share_line).setVisibility(View.GONE);
                    showGoodBuild.getView(R.id.line_share_boottom).setVisibility(View.GONE);
                    showGoodBuild.dismiss();
                    goodsPic(inflate,true);
                }
            });
        }
    }

    private void goodsPic(View inflate,boolean isShow) {
        GlideUtils.getInstance().loadBitmapSync(context, mShareInfoParam.img,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                            Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                        if (isShow) {
                            boolean isSuccess = BitmapUtil.saveImageToAlbumn(context, bitmapByView,false,false);
                            if (isSuccess) {
//                                SaveAlbumDialog dialog = new SaveAlbumDialog((Activity) mContext, shareType, shareId);
//                                dialog.show();
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
                            Common.staticToast(context.getString(R.string.operate_tupianbaocunshibai));
                        } else {
                            Common.staticToast("分享失败");
                        }
                    }
                });
    }


    public interface OnShareBlogCallBack {
        void shareSuccess(String blogId,String goodsId);
    }
}
