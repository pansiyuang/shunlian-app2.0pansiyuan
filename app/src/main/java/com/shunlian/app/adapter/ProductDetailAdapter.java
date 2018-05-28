package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ProductDetailEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.banner.Kanner;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/25.
 */

public class ProductDetailAdapter extends BaseRecyclerAdapter<String> {

    private final int BANNER = 100001;
    private final int ATTRIBUTE = 100002;
    private final int IMAGE = 100003;

    private ProductDetailEntity productDetailEntity;
    private String re = "(w=|h=)(\\d+)";
    private Pattern p = Pattern.compile(re);
    private int mDeviceWidth;

    public ProductDetailAdapter(Context context, ProductDetailEntity entity, List<String> lists) {
        super(context, false, lists);
        this.productDetailEntity = entity;
        mDeviceWidth = DeviceInfoUtil.getDeviceWidth(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER:
                return new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.banner, parent, false));
            case ATTRIBUTE:
                return new AttributeViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_plus_attribute, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ImgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_img, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return BANNER;
        }
        if (position == 1) {
            return ATTRIBUTE;
        }
        return IMAGE;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 2;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BANNER:
                handleBanner(holder);
                break;
            case ATTRIBUTE:
                handleAttribute(holder);
                break;
            case IMAGE:
                handleImage(holder, position);
                break;
        }
    }

    public void handleBanner(RecyclerView.ViewHolder holder) {
        BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
        if (!isEmpty(productDetailEntity.pics)) {
            bannerViewHolder.kanner.setBanner(productDetailEntity.pics);
        }
    }

    public void handleAttribute(RecyclerView.ViewHolder holder) {
        AttributeViewHolder attributeViewHolder = (AttributeViewHolder) holder;
        attributeViewHolder.tv_title.setText(productDetailEntity.title);
        String price = getString(R.string.common_yuan) + productDetailEntity.price;
        attributeViewHolder.tv_price.setText(Common.changeTextSize(price, getString(R.string.common_yuan), 12));
        attributeViewHolder.tv_market_price.setText(productDetailEntity.market_price);
        attributeViewHolder.tv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        attributeViewHolder.tv_title_image.setText(productDetailEntity.detail.text);
    }

    public void handleImage(RecyclerView.ViewHolder holder, int position) {
        ImgViewHolder imgViewHolder = (ImgViewHolder) holder;
        String s = lists.get(position - 2);
        if (Pattern.matches(".*(w=\\d+&h=\\d+).*", s)) {
            Matcher m = p.matcher(s);
            int w = 0;
            int h = 0;
            if (m.find()) {
                w = Integer.parseInt(m.group(2));
            }
            if (m.find()) {
                h = Integer.parseInt(m.group(2));
            }
            int i = mDeviceWidth * h / w;
            GlideUtils.getInstance().loadOverrideImage(context, imgViewHolder.miv_img, s, mDeviceWidth, i);
        } else {
            int i = mDeviceWidth * 330 / 720;
            GlideUtils.getInstance().loadOverrideImage(context, imgViewHolder.miv_img, s, mDeviceWidth, i);
        }
    }

    public class BannerViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.kanner)
        Kanner kanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class AttributeViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_market_price)
        TextView tv_market_price;

        @BindView(R.id.tv_title_image)
        TextView tv_title_image;

        public AttributeViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ImgViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        public ImgViewHolder(View itemView) {
            super(itemView);
        }
    }
}
