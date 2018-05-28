package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.SuperProductEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.banner.Kanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/24.
 */

public class SuperProductAdapter extends BaseRecyclerAdapter<SuperProductEntity.SuperProduct> {
    private static final int TYPE_BANNER = 10001;
    private static final int TYPE_GOODS = 10002;

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
                return new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.banner, parent, false));
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
            viewHolder.kanner.setBanner(banners);

            ViewGroup.LayoutParams frameLayout = viewHolder.kanner.getLayoutParams();
            frameLayout.height = TransformUtil.dip2px(context, 192);
            viewHolder.kanner.setLayoutParams(frameLayout);

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
        GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
        SuperProductEntity.SuperProduct superProduct = lists.get(position);
        GlideUtils.getInstance().loadImage(context, goodsViewHolder.miv_icon, superProduct.thumb);
        goodsViewHolder.tv_products_title.setText(superProduct.title);
        if (superProduct.is_new == 1) {
            goodsViewHolder.tv_products_isNew.setVisibility(View.VISIBLE);
        }
        goodsViewHolder.tv_products_price.setText(getString(R.string.common_yuan) + superProduct.price);

        if (isEmpty(superProduct.earned)) {
            goodsViewHolder.ll_earn.setVisibility(View.GONE);
        } else {
            goodsViewHolder.ll_earn.setVisibility(View.VISIBLE);
            goodsViewHolder.tv_earn_money.setText(getString(R.string.common_yuan) + superProduct.earned);
        }

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
    }


    public class BannerViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.kanner)
        Kanner kanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class GoodsViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

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

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (viewHolder.ll_tags.getChildCount() == 0) {
            params.setMargins(0, 0, 0, 0);
        } else {
            params.setMargins(TransformUtil.dip2px(context, 5.5f), 0, 0, 0);
        }
        textView.setLayoutParams(params);
        return textView;
    }
}
