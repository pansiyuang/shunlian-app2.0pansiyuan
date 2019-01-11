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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.presenter.EmptyPresenter;
import com.shunlian.app.presenter.GoodsDetailPresenter;
import com.shunlian.app.view.IView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

public class DownLoadQRCodeImageUtil implements IView {
    private Context mContext;
    private MyCallBack myCallBack;

    private EmptyPresenter emptyPresenter;
    public DownLoadQRCodeImageUtil(Context context) {
        mContext = context;
    }

    public void saveGoodsQRImage(List<GoodsDeatilEntity.Goods> goodsList) {
        emptyPresenter = new EmptyPresenter(mContext,this);
        try {
            for (int i = 0; i < goodsList.size(); i++) {
                emptyPresenter.getShareInfo(emptyPresenter.goods,goodsList.get(i).goods_id);
//                createGoodCode(goodsList.get(i));
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

    public void createGoodCode(ShareInfoParam mShareInfoParam) {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(mContext, "login");
        } else {
            final View inflate = LayoutInflater.from(mContext).inflate(R.layout.share_goods_new, null, false);
            MyImageView miv_close = inflate.findViewById(R.id.miv_close);
            MyImageView miv_code = inflate.findViewById(R.id.miv_code);
            int i = TransformUtil.dip2px(mContext, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
            miv_code.setImageBitmap(qrImage);

            MyTextView mtv_title =  inflate.findViewById(R.id.mtv_title);
            MyTextView  mtv_coupon_title =  inflate.findViewById(R.id.mtv_coupon_title);

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

            re_newuser_layout.setVisibility(View.GONE);
            line_old_user.setVisibility(View.VISIBLE);
             mtv_price.setText(Common.dotPointAfterSmall(mShareInfoParam.price,11));
            LinearLayout  llayout_day =inflate.findViewById(R.id.llayout_day);
            MyTextView mtv_time  = inflate.findViewById(R.id.mtv_time);
            MyTextView mtv_act_label  = inflate.findViewById(R.id.mtv_act_label);

         if(!TextUtils.isEmpty(mShareInfoParam.time_text)){
                llayout_day.setVisibility(View.VISIBLE);
                if(mShareInfoParam.is_start==0){
                    llayout_day.setBackgroundResource(R.drawable.edge_007aff_1px);
                    mtv_act_label.setTextColor(mContext.getResources().getColor(R.color.value_007AFF));
                    mtv_time.setTextColor(mContext.getResources().getColor(R.color.white));
                    mtv_time.setBackgroundColor(mContext.getResources().getColor(R.color.value_007AFF));
                }else{
                    llayout_day.setBackgroundResource(R.drawable.edge_pink_1px);
                    mtv_act_label.setTextColor(mContext.getResources().getColor(R.color.pink_color));
                    mtv_time.setTextColor(mContext.getResources().getColor(R.color.white));
                    mtv_time.setBackgroundColor(mContext.getResources().getColor(R.color.pink_color));
                }
                mtv_time.setText(mShareInfoParam.time_text);
                mtv_act_label.setText(mShareInfoParam.little_word);
            }
            MyImageView miv_goods_pic =  inflate.findViewById(R.id.miv_goods_pic);
            int width = Common.getScreenWidth((Activity) mContext) - TransformUtil.dip2px(mContext, 80);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) miv_goods_pic.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = width;

            inflate.findViewById(R.id.line_share_boottom).setVisibility(View.GONE);
            GlideUtils.getInstance().loadBitmapSync(mContext, mShareInfoParam.img,
                    new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource,
                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                            int width = Common.getScreenWidth((Activity) mContext) - TransformUtil.dip2px(mContext, 10);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) miv_goods_pic.getLayoutParams();
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

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        ShareInfoParam  mShareInfoParam = new ShareInfoParam();
        if (mShareInfoParam != null) {
            mShareInfoParam.isShowTiltle = false;
            mShareInfoParam.userName = baseEntity.data.userName;
            mShareInfoParam.userAvatar = baseEntity.data.userAvatar;
            mShareInfoParam.shareLink = baseEntity.data.shareLink;
            mShareInfoParam.desc = baseEntity.data.desc;
            mShareInfoParam.img = baseEntity.data.img;
            mShareInfoParam.title = baseEntity.data.title;
            mShareInfoParam.goods_id = baseEntity.data.goods_id;
            if(!TextUtils.isEmpty(baseEntity.data.share_buy_earn))
                mShareInfoParam.share_buy_earn = baseEntity.data.share_buy_earn;
            mShareInfoParam.price = baseEntity.data.price;
            mShareInfoParam.little_word = baseEntity.data.little_word;
            mShareInfoParam.time_text = baseEntity.data.time_text;
            mShareInfoParam.is_start = baseEntity.data.is_start;
            mShareInfoParam.market_price = baseEntity.data.market_price;
            mShareInfoParam.voucher = baseEntity.data.voucher;
            createGoodCode(mShareInfoParam);
        }
    }

    public interface MyCallBack {
        void successBack();

        void errorBack();
    }
}
