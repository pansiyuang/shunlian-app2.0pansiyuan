package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.shunlian.app.R;
import com.shunlian.app.bean.VouchercenterplEntity;
import com.shunlian.app.presenter.PGetCoupon;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class NewCouponsAdapter extends BaseRecyclerAdapter<VouchercenterplEntity.MData> {
    private PGetCoupon pGetCoupon;
    private static final int TYPE3 = 3;//单个
    private static final int TYPE2 = 2;//多个

    public NewCouponsAdapter(Context context, boolean isShowFooter,
                             List<VouchercenterplEntity.MData> lists, PGetCoupon pGetCoupon) {
        super(context, isShowFooter, lists);
        this.pGetCoupon=pGetCoupon;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (2==type){
                        return manager.getSpanCount();
                    }else {
                        return isBottoms(position) ? manager.getSpanCount() : 1;
                    }
                }
            });
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE2:
                return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon_one, parent, false));
            case TYPE3:
                return new ThreeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon_two, parent, false));
           default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position > lists.size() - 1)
            return super.getItemViewType(position);
        switch (lists.get(position).is_more) {
            case "1":
                return TYPE2;
            case "0":
                return TYPE3;
            default:
                return super.getItemViewType(position);
        }
    }
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_first_page_nav, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE2:
                if (holder instanceof TwoHolder) {
                    TwoHolder mHolder = (TwoHolder) holder;
                    VouchercenterplEntity.MData data = lists.get(position);
                    mHolder.ntv_title.setText(data.title);
                    GlideUtils.getInstance().loadImageZheng(context,mHolder.miv_logo,data.store_label);
                    if (isEmpty(data.tag)){
                        mHolder.miv_tag.setVisibility(View.GONE);
                    }else {
                        mHolder.miv_tag.setVisibility(View.VISIBLE);
                        GlideUtils.getInstance().loadImageZheng(context,mHolder.miv_tag,data.tag);
                    }
                    mHolder.ntv_name.setText(data.store_name);
                    if ("0".equals(data.if_get)) {
                        mHolder.mtv_use_coupon.setText(getString(R.string.chat_lijilingqu));
                        mHolder.mtv_use_coupon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pGetCoupon.getVoucher(data.id,false,position);
                            }
                        });
                    } else {
                        mHolder.mtv_use_coupon.setText(getString(R.string.coupon_lijishiyong));
                        mHolder.mtv_use_coupon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                    StoreAct.startAct(context,data.store_id);
                                Common.goGoGo(context,data.jump_type,data.lazy_id);
                            }
                        });
                    }
                    SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.common_yuan) + data.denomination
                            , data.denomination, 14);
                    mHolder.ntv_price.setText(spannableStringBuilder);
                    mHolder.ntv_condition.setText(data.use_condition);
                    if (!isEmpty(data.goods_data)){
                        mHolder.mllayout_goods.setVisibility(View.VISIBLE);
                        int picWidth=Common.getScreenWidth((Activity) context)- TransformUtil.dip2px(context,134);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth / 3, picWidth / 3);
                        mHolder.miv_img.setLayoutParams(params);
                        mHolder.ntv_desc.setText(data.goods_data.get(0).goods_title);
                        mHolder.ntv_price0.setText(getString(R.string.common_yuan)+data.goods_data.get(0).goods_price);
                        GlideUtils.getInstance().loadImageZheng(context,mHolder.miv_img,data.goods_data.get(0).goods_thumb);
                        switch (data.goods_data.size()){
                            case 1:
                                mHolder.mllayout_two.setVisibility(View.INVISIBLE);
                                mHolder.mllayout_one.setVisibility(View.INVISIBLE);
                                break;
                            case 2:
                                mHolder.mllayout_two.setVisibility(View.INVISIBLE);
                                mHolder.mllayout_one.setVisibility(View.VISIBLE);
                                mHolder.miv_img1.setLayoutParams(params);
                                mHolder.ntv_desc1.setText(data.goods_data.get(1).goods_title);
                                mHolder.ntv_price1.setText(getString(R.string.common_yuan)+data.goods_data.get(1).goods_price);
                                GlideUtils.getInstance().loadImageZheng(context,mHolder.miv_img1,data.goods_data.get(1).goods_thumb);
                               break;
                            case 3:
                                mHolder.mllayout_two.setVisibility(View.VISIBLE);
                                mHolder.mllayout_one.setVisibility(View.VISIBLE);
                                mHolder.miv_img1.setLayoutParams(params);
                                mHolder.ntv_desc1.setText(data.goods_data.get(1).goods_title);
                                mHolder.ntv_price1.setText(getString(R.string.common_yuan)+data.goods_data.get(1).goods_price);
                                GlideUtils.getInstance().loadImageZheng(context,mHolder.miv_img1,data.goods_data.get(1).goods_thumb);
                                mHolder.miv_img2.setLayoutParams(params);
                                mHolder.ntv_desc2.setText(data.goods_data.get(2).goods_title);
                                mHolder.ntv_price2.setText(getString(R.string.common_yuan)+data.goods_data.get(2).goods_price);
                                GlideUtils.getInstance().loadImageZheng(context,mHolder.miv_img2,data.goods_data.get(2).goods_thumb);
                                break;
                        }
                    }else {
                        mHolder.mllayout_goods.setVisibility(View.GONE);
                    }
                }

            case TYPE3:
                if (holder instanceof ThreeHolder) {
                    ThreeHolder mHolder = (ThreeHolder) holder;
                    VouchercenterplEntity.MData data = lists.get(position);
                    if (!isEmpty(data.goods_data)){
                        mHolder.mllayout_root.setVisibility(View.VISIBLE);
                        int picWidth=Common.getScreenWidth((Activity) context)- TransformUtil.dip2px(context,70);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth / 2, picWidth / 2);
                        mHolder.miv_img.setLayoutParams(params);
                        mHolder.ntv_desc.setText(data.goods_data.get(0).goods_title);
                        mHolder.ntv_title.setText(data.goods_data.get(0).title);
                        SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.common_yuan) + data.goods_data.get(0).denomination
                                , data.goods_data.get(0).denomination, 14);
                        mHolder.ntv_price.setText(spannableStringBuilder);
                        mHolder.ntv_condition.setText(data.use_condition);
                        mHolder.ntv_price0.setText(getString(R.string.common_yuan)+data.goods_data.get(0).goods_price);
                        mHolder.ntv_pricem.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
                        mHolder.ntv_pricem.setText(getString(R.string.common_yuan)+data.goods_data.get(0).goods_market_price);
                        GlideUtils.getInstance().loadImageZheng(context,mHolder.miv_img,data.goods_data.get(0).goods_thumb);
                        if ("0".equals(data.if_get)) {
                            mHolder.mtv_use_coupon.setText(getString(R.string.chat_lijilingqu));
                            mHolder.mtv_use_coupon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    pGetCoupon.getVoucher(data.goods_data.get(0).id,false,position);
                                }
                            });
                        } else {
                            mHolder.mtv_use_coupon.setText(getString(R.string.coupon_lijishiyong));
                            mHolder.mtv_use_coupon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                    StoreAct.startAct(context,data.store_id);
                                    Common.goGoGo(context,data.goods_data.get(0).jump_type,data.goods_data.get(0).lazy_id);
                                }
                            });
                        }
                        if (isEmpty(data.goods_data.get(0).tag)){
                            mHolder.miv_tag.setVisibility(View.GONE);
                        }else {
                            mHolder.miv_tag.setVisibility(View.VISIBLE);
                            GlideUtils.getInstance().loadImageZheng(context,mHolder.miv_tag,data.goods_data.get(0).tag);
                        }
                    }else {
                        mHolder.mllayout_root.setVisibility(View.GONE);
                    }
                }
        }
    }

    public class TwoHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_logo)
        MyImageView miv_logo;

        @BindView(R.id.ntv_name)
        NewTextView ntv_name;

        @BindView(R.id.ntv_price)
        NewTextView ntv_price;

        @BindView(R.id.ntv_condition)
        NewTextView ntv_condition;

        @BindView(R.id.ntv_title)
        NewTextView ntv_title;

        @BindView(R.id.mtv_use_coupon)
        MyTextView mtv_use_coupon;

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.mllayout_goods)
        MyLinearLayout mllayout_goods;

        @BindView(R.id.mllayout_one)
        MyLinearLayout mllayout_one;

        @BindView(R.id.mllayout_two)
        MyLinearLayout mllayout_two;

        @BindView(R.id.miv_tag)
        MyImageView miv_tag;

        @BindView(R.id.ntv_desc)
        NewTextView ntv_desc;

        @BindView(R.id.ntv_price0)
        NewTextView ntv_price0;

        @BindView(R.id.miv_img1)
        MyImageView miv_img1;

        @BindView(R.id.ntv_desc1)
        NewTextView ntv_desc1;

        @BindView(R.id.ntv_price1)
        NewTextView ntv_price1;

        @BindView(R.id.miv_img2)
        MyImageView miv_img2;

        @BindView(R.id.ntv_desc2)
        NewTextView ntv_desc2;

        @BindView(R.id.ntv_price2)
        NewTextView ntv_price2;

        public TwoHolder(View itemView) {
            super(itemView);
        }

    }

    public class ThreeHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.ntv_price)
        NewTextView ntv_price;

        @BindView(R.id.ntv_condition)
        NewTextView ntv_condition;

        @BindView(R.id.ntv_title)
        NewTextView ntv_title;

        @BindView(R.id.mtv_use_coupon)
        MyTextView mtv_use_coupon;

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.miv_tag)
        MyImageView miv_tag;

        @BindView(R.id.ntv_desc)
        NewTextView ntv_desc;

        @BindView(R.id.ntv_price0)
        NewTextView ntv_price0;

        @BindView(R.id.ntv_pricem)
        NewTextView ntv_pricem;

        @BindView(R.id.mllayout_root)
        MyLinearLayout mllayout_root;

        public ThreeHolder(View itemView) {
            super(itemView);
        }

    }
}
