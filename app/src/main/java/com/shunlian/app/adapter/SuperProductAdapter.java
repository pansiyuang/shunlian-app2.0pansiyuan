package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.SuperProductEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.banner.MyKanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/24.
 */

public class SuperProductAdapter extends BaseRecyclerAdapter<SuperProductEntity.SuperProduct> {
    private static final int TYPE_BANNER = 10001;
    private static final int TYPE_GOODS = 10002;
    private ShareInfoParam mShareInfoParam = new ShareInfoParam();
    private OnShareClickListener mShareLinstener;

    public SuperProductAdapter(Context context, List<SuperProductEntity.SuperProduct> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BANNER:
                return new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.block_first_page_banner, parent, false));
            case TYPE_GOODS:
                return new GoodsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_superior_products, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        SuperProductEntity.SuperProduct superProduct = lists.get(position);
        switch (superProduct.type) {
            case "banner":
                return TYPE_BANNER;
            case "goods":
                return TYPE_GOODS;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_BANNER:
                handleBanner(holder, position);
                break;
            case TYPE_GOODS:
                handleGoods(holder, position);
                break;
        }
    }

    public void handleBanner(RecyclerView.ViewHolder holder, int position) {
        BannerViewHolder viewHolder = (BannerViewHolder) holder;
        SuperProductEntity.SuperProduct superProduct = lists.get(position);

        if (!isEmpty(superProduct.data)) {
            List<SuperProductEntity.BannerEntity> bannerEntityList = superProduct.data;
            List<String> banners = new ArrayList<>();
            for (SuperProductEntity.BannerEntity bannerEntity : bannerEntityList) {
                banners.add(bannerEntity.thumb);
            }
            viewHolder.kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
            viewHolder.kanner.setBanner(banners);

            ViewGroup.LayoutParams params = viewHolder.kanner.getLayoutParams();

            ViewGroup.MarginLayoutParams vmlp = (ViewGroup.MarginLayoutParams) viewHolder.kanner.getLayoutParams();
            if (position != 0) {
                vmlp.setMargins(0, TransformUtil.dip2px(context, 10), 0, 0);
            }
            params.height = TransformUtil.dip2px(context, 192);
            viewHolder.kanner.setLayoutParams(params);

            viewHolder.kanner.setOnItemClickL(pos -> {
                SuperProductEntity.BannerEntity bannerEntity = bannerEntityList.get(pos);
                SuperProductEntity.Url url = bannerEntity.url;
                switch (url.type) {
                    case "shop":
                        StoreAct.startAct(context, url.item_id);
                        break;
                    case "goods":
                        GoodsDetailAct.startAct(context, url.item_id);
                        break;
                }
            });
        }
    }

    public void handleGoods(RecyclerView.ViewHolder holder, int position) {
        try {
            GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
            SuperProductEntity.SuperProduct superProduct = lists.get(position);
            GlideUtils.getInstance().loadImage(context, goodsViewHolder.miv_icon, superProduct.thumb);
            goodsViewHolder.tv_products_title.setText(superProduct.title);
            goodsViewHolder.tv_products_isNew.setText(superProduct.desc);
            goodsViewHolder.tv_products_price.setText(getString(R.string.common_yuan) + superProduct.price);

            if (isEmpty(superProduct.earned)) {
                goodsViewHolder.ll_earn.setVisibility(View.GONE);
            } else {
                goodsViewHolder.ll_earn.setVisibility(View.VISIBLE);
                goodsViewHolder.tv_earn_money.setText(getString(R.string.common_yuan) + superProduct.earned);
            }

            if (!isEmpty(superProduct.stock)) {
                int stock = Integer.valueOf(superProduct.stock);
                if (stock <= 0) {
                    goodsViewHolder.miv_sale_out.setVisibility(View.VISIBLE);
                    goodsViewHolder.tv_products_price.setTextColor(getColor(R.color.value_A0A0A0));
                } else {
                    goodsViewHolder.miv_sale_out.setVisibility(View.GONE);
                    goodsViewHolder.tv_products_price.setTextColor(getColor(R.color.new_text));
                }
            }

            goodsViewHolder.ll_tags.removeAllViews();
            if (1 == superProduct.is_new) {
                goodsViewHolder.ll_tags.addView(creatTextTag("新品", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fbd500_2px), goodsViewHolder));
            }

            if (1 == superProduct.is_hot) {
                goodsViewHolder.ll_tags.addView(creatTextTag("热卖", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fb9f00_2px), goodsViewHolder));
            }

            if (1 == superProduct.is_explosion) {
                goodsViewHolder.ll_tags.addView(creatTextTag("爆款", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fb6400_2px), goodsViewHolder));
            }

            if (1 == superProduct.is_recommend) {
                goodsViewHolder.ll_tags.addView(creatTextTag("推荐", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_7898da_2px), goodsViewHolder));
            }

            //返回键扩大点击范围
            int i = TransformUtil.dip2px(context, 10);
            TransformUtil.expandViewTouchDelegate(goodsViewHolder.miv_share, i, i, i, i);

            goodsViewHolder.miv_share.setOnClickListener(v -> {
                //分享
                if (mShareLinstener != null) {
                    SuperProductEntity.Share share = superProduct.share;
                    mShareInfoParam.title = share.title;
                    mShareInfoParam.desc = share.content;
                    mShareInfoParam.img = share.pic;
                    mShareInfoParam.userName = share.nick_name;
                    mShareInfoParam.userAvatar = share.portrait;
                    mShareInfoParam.shareLink = share.share_url;
                    mShareInfoParam.goodsPrice = superProduct.price;
                    mShareLinstener.onShare(mShareInfoParam, superProduct.url.item_id);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class BannerViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.kanner)
        MyKanner kanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class GoodsViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_sale_out)
        MyImageView miv_sale_out;

        @BindView(R.id.tv_products_title)
        TextView tv_products_title;

        @BindView(R.id.tv_products_isNew)
        TextView tv_products_isNew;

        @BindView(R.id.ll_tags)
        LinearLayout ll_tags;

        @BindView(R.id.tv_products_price)
        TextView tv_products_price;

        @BindView(R.id.ll_earn)
        LinearLayout ll_earn;

        @BindView(R.id.tv_earn_money)
        TextView tv_earn_money;

        @BindView(R.id.miv_share)
        MyImageView miv_share;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            int picWidth = Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, 20);
            int picHeight = picWidth * 158 / 341;
            miv_icon.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, picHeight));
            miv_icon.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    public TextView creatTextTag(String content, int colorRes, Drawable drawable, GoodsViewHolder viewHolder) {
        TextView textView = new TextView(context);
        textView.setText(content);
        textView.setTextSize(9);
        textView.setBackgroundDrawable(drawable);
        textView.setTextColor(colorRes);
        int padding = TransformUtil.dip2px(context, 3f);
        textView.setPadding(padding, 0, padding, 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (viewHolder.ll_tags.getChildCount() == 0) {
            params.setMargins(0, 0, 0, 0);
        } else {
            params.setMargins(TransformUtil.dip2px(context, 5.5f), 0, 0, 0);
        }
        textView.setLayoutParams(params);
        return textView;
    }

    public void setOnShareClickListener(OnShareClickListener listener) {
        this.mShareLinstener = listener;
    }

    public interface OnShareClickListener {
        void onShare(ShareInfoParam infoParam, String id);
    }
}
