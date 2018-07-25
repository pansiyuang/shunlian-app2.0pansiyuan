package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.VoucherEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class CouponGoodsAdapter extends BaseRecyclerAdapter<VoucherEntity.Goods> {
    private RecyclerView.LayoutParams params;

    public CouponGoodsAdapter(Context context, boolean isShowFooter,
                              List<VoucherEntity.Goods> lists) {
        super(context, isShowFooter, lists);
        params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_double, parent, false);
        return new DoubleViewHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof DoubleViewHolder) {
                VoucherEntity.Goods goods;
                int margin = TransformUtil.dip2px(context, 5f);
                goods = lists.get(position);
                if (position % 2 == 0) {
                    params.setMargins(0, 0, margin, margin);
                    holder.itemView.setLayoutParams(params);
                } else {
                    params.setMargins(0, 0, 0, margin);
                    holder.itemView.setLayoutParams(params);
                }
                DoubleViewHolder viewHolder = (DoubleViewHolder) holder;
                GlideUtils.getInstance().loadImage(context, viewHolder.miv_icon, goods.thumb);
                viewHolder.tv_title.setText(goods.title);
                viewHolder.tv_price.setText(goods.price);
                String price = getString(R.string.common_yuan) + goods.price;
                viewHolder.tv_price.setText(Common.changeTextSize(price, getString(R.string.common_yuan), 11));

                if ("1".equals(goods.free_shpping)) {
                    viewHolder.tv_free.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tv_free.setVisibility(View.GONE);
                }

                viewHolder.ll_tag.removeAllViews();

                if ("1".equals(goods.is_new)) {
                    viewHolder.ll_tag.addView(creatTextTag("新品", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fbd500_2px), viewHolder));
                }

                if ("1".equals(goods.is_hot)) {
                    viewHolder.ll_tag.addView(creatTextTag("热卖", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fb9f00_2px), viewHolder));
                }

//                if ("1".equals(goods.is_explosion)) {
//                    viewHolder.ll_tag.addView(creatTextTag("爆款", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fb6400_2px), viewHolder));
//                }

                if ("1".equals(goods.is_pop)) {
                    viewHolder.ll_tag.addView(creatTextTag("推荐", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_7898da_2px), viewHolder));
                }

                if ("1".equals(goods.voucher)) {
                    viewHolder.ll_tag.addView(creatTextTag("劵", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
                }

                if ("1".equals(goods.discount)) {
                    viewHolder.ll_tag.addView(creatTextTag("折", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
                }

                if ("1".equals(goods.gift)) {
                    viewHolder.ll_tag.addView(creatTextTag("赠", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
                }

//                if (goods.type == 1) { //是优品
//                    viewHolder.miv_product.setVisibility(View.VISIBLE);
//                    if (!isEmpty(goods.self_buy_earn)) { //有字段才显示布局
//                        viewHolder.ll_earn.setVisibility(View.VISIBLE);
//                        viewHolder.tv_earn_money.setText(getString(R.string.common_yuan) + goods.self_buy_earn);
//                    } else {
//                        viewHolder.ll_earn.setVisibility(View.GONE);
//                    }
//                } else {
                    viewHolder.ll_earn.setVisibility(View.GONE);
                    viewHolder.miv_product.setVisibility(View.GONE);
//                }

                if ("0".equals(goods.comments_num)) {
                    viewHolder.tv_comment.setText("暂无评论");
                } else {
                    if ("0".equals(goods.good_comments_num)) {
                        viewHolder.tv_comment.setText(goods.comments_num + "条评论");
                    } else {
                        viewHolder.tv_comment.setText(goods.comments_num + "条评论  " + goods.good_comments_num + "%好评");
                    }
                }
                viewHolder.tv_address.setText(goods.location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextView creatTextTag(String content, int colorRes, Drawable drawable, DoubleViewHolder viewHolder) {

        TextView textView = new TextView(context);
        textView.setText(content);
        textView.setTextSize(9);
        textView.setBackgroundDrawable(drawable);
        textView.setTextColor(colorRes);
        int padding = TransformUtil.dip2px(context, 3f);
        textView.setPadding(padding, 0, padding, 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (viewHolder.ll_tag.getChildCount() == 0) {
            params.setMargins(0, 0, 0, 0);
        } else {
            params.setMargins(TransformUtil.dip2px(context, 3f), 0, 0, 0);
        }
        textView.setLayoutParams(params);
        return textView;
    }

    public class DoubleViewHolder extends BaseRecyclerViewHolders implements View.OnClickListener {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_product)
        MyImageView miv_product;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.ll_tag)
        LinearLayout ll_tag;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_free)
        TextView tv_free;

        @BindView(R.id.tv_comment)
        TextView tv_comment;

        @BindView(R.id.tv_address)
        TextView tv_address;

        @BindView(R.id.ll_earn)
        LinearLayout ll_earn;

        @BindView(R.id.tv_earn_money)
        TextView tv_earn_money;


        public DoubleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

    }
}
