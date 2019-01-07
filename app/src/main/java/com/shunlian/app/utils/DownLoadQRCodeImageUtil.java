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
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

public class DownLoadQRCodeImageUtil {
    private Context mContext;
    private MyCallBack myCallBack;

    public DownLoadQRCodeImageUtil(Context context) {
        mContext = context;
    }

    public void saveGoodsQRImage(List<GoodsDeatilEntity.Goods> goodsList) {
        try {
            for (int i = 0; i < goodsList.size(); i++) {
                createGoodCode(goodsList.get(i));
            }
            if (myCallBack != null) {
                myCallBack.successBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (myCallBack != null) {
                myCallBack.errorBack();
            }
        }
    }

    public void setMyCallBack(MyCallBack callBack) {
        this.myCallBack = callBack;
    }

    public void createGoodCode(GoodsDeatilEntity.Goods goods) {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(mContext, "login");
        } else {
            final View inflate = LayoutInflater.from(mContext).inflate(R.layout.share_goods_new, null, false);
            MyImageView miv_close = inflate.findViewById(R.id.miv_close);
            MyImageView miv_user_head = inflate.findViewById(R.id.miv_user_head);
            MyTextView mtv_nickname = inflate.findViewById(R.id.mtv_nickname);
            TextView mtv_market_price = inflate.findViewById(R.id.mtv_market_price);
            mtv_nickname.setText("来自" + SharedPrefUtil.getSharedUserString("nickname", "") + "的分享");
            GlideUtils.getInstance().loadCircleAvar(mContext, miv_user_head, SharedPrefUtil.getSharedUserString("avatar", ""));
            MyImageView miv_code = inflate.findViewById(R.id.miv_code);
            int i = TransformUtil.dip2px(mContext, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(goods.share_url, null, i);
            miv_code.setImageBitmap(qrImage);
            MyTextView mtv_title = inflate.findViewById(R.id.mtv_title);
            mtv_title.setText(goods.title);
            if (!TextUtils.isEmpty(goods.market_price)) {
                mtv_market_price.setVisibility(View.VISIBLE);
                mtv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mtv_market_price.setText("￥" + goods.market_price);
            } else {
                mtv_market_price.setVisibility(View.VISIBLE);
            }
            MyTextView mtv_desc = inflate.findViewById(R.id.mtv_desc);
            if (!TextUtils.isEmpty(goods.desc)) {
                mtv_desc.setVisibility(View.GONE);
                mtv_desc.setText(goods.desc);
            } else {
                mtv_desc.setVisibility(View.GONE);
            }
            MyTextView mtv_price = inflate.findViewById(R.id.mtv_price);
            mtv_price.setText("￥" + goods.price);
            MyTextView mtv_goodsID = inflate.findViewById(R.id.mtv_goodsID);
            mtv_goodsID.setText("商品编号:" + goods.goods_id + "(搜索可直达)");

            //显示优品图标
            MyTextView mtv_SuperiorProduct = inflate.findViewById(R.id.mtv_SuperiorProduct);
            if (goods.isSuperiorProduct == 1) {
                mtv_SuperiorProduct.setVisibility(View.VISIBLE);
            } else {
                mtv_SuperiorProduct.setVisibility(View.GONE);
            }

//            LinearLayout llayout_day = inflate.findViewById(R.id.llayout_day);
//            MyTextView mtv_time = inflate.findViewById(R.id.mtv_time);
//            MyTextView mtv_act_label = inflate.findViewById(R.id.mtv_act_label);
//            if (TextUtils.isEmpty(goods.start_time)) {
//                llayout_day.setVisibility(View.GONE);
//            } else {
//                llayout_day.setVisibility(View.VISIBLE);
//                mtv_time.setText(mShareInfoParam.start_time);
//                mtv_act_label.setText(mShareInfoParam.act_label);
//            }
            MyImageView miv_goods_pic = inflate.findViewById(R.id.miv_goods_pic);
            int width = Common.getScreenWidth((Activity) mContext) - TransformUtil.dip2px(mContext, 80);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) miv_goods_pic.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = width;

            miv_close.setVisibility(View.GONE);
            inflate.findViewById(R.id.line_share_line).setVisibility(View.GONE);
            inflate.findViewById(R.id.line_share_boottom).setVisibility(View.GONE);
            GlideUtils.getInstance().loadBitmapSync(mContext, goods.thumb,
                    new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource,
                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                            int width = Common.getScreenWidth((Activity) mContext) - TransformUtil.dip2px(mContext, 10);
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) miv_goods_pic.getLayoutParams();
                            layoutParams.width = width;
                            layoutParams.height = width;

                            miv_goods_pic.setImageBitmap(resource);
                            Bitmap bitmapByView = BitmapUtil.getBitmapByView(inflate);
                            if (bitmapByView == null) {
                                return;
                            }
                            BitmapUtil.saveImageToAlbumn(mContext, bitmapByView, false, false);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });
        }
    }

    public interface MyCallBack {
        void successBack();

        void errorBack();
    }
}
