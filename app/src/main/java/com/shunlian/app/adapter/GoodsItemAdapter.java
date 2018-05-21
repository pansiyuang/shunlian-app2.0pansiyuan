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
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/20.
 */

public class GoodsItemAdapter extends BaseRecyclerAdapter<StoreGoodsListEntity.MData> {

    public GoodsItemAdapter(Context context, List<StoreGoodsListEntity.MData> lists) {
        super(context, true, lists);
    }

    /**
     * 设置baseFooterHolder  layoutparams
     *
     * @param baseFooterHolder
     */
    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setText(getString(R.string.no_more_goods));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new GoodsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_goods_search, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsViewHolder) {
            StoreGoodsListEntity.MData mData = lists.get(position);
            GoodsViewHolder viewHolder = (GoodsViewHolder) holder;
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_icon, mData.whole_thumb);
            viewHolder.tv_title.setText(mData.title);
            viewHolder.tv_price.setText(mData.price);

//            if ("1".equals(mData.is_new)) {
//                viewHolder.ll_tag.addView(creatTextTag("新品", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fbd500_2px), viewHolder));
//            }
//
//            if ("1".equals(mData.is_hot)) {
//                viewHolder.ll_tag.addView(creatTextTag("热卖", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fb9f00_2px), viewHolder));
//            }
//
//            if ("1".equals(mData.is_explosion)) {
//                viewHolder.ll_tag.addView(creatTextTag("爆款", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fb6400_2px), viewHolder));
//            }
//
//            if ("1".equals(mData.is_recommend)) {
//                viewHolder.ll_tag.addView(creatTextTag("推荐", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_7898da_2px), viewHolder));
//            }
//
//            if ("1".equals(mData.has_coupon)) {
//                viewHolder.ll_tag.addView(creatTextTag("劵", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
//            }
//
//            if ("1".equals(mData.has_discount)) {
//                viewHolder.ll_tag.addView(creatTextTag("折", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
//            }
//
//            if ("1".equals(mData.has_gift)) {
//                viewHolder.ll_tag.addView(creatTextTag("赠", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
//            }
//
//            viewHolder.tv_comment.setText(String.format(getString(R.string.sort_comment), goods.comment_num, goods.comment_rate));
//            viewHolder.tv_address.setText(goods.send_area);
            if (mData.isSelect) {
                viewHolder.mtv_corner.setBackgroundDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_h));
            } else {
                viewHolder.mtv_corner.setBackgroundDrawable(getDrawable(R.drawable.oval_stroke_pink));
            }
        }
    }

    public TextView creatTextTag(String content, int colorRes, Drawable drawable, GoodsSearchAdapter.SingleViewHolder viewHolder) {
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
            params.setMargins(TransformUtil.dip2px(context, 5.5f), 0, 0, 0);
        }
        textView.setLayoutParams(params);
        return textView;
    }


    public class GoodsViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

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

        @BindView(R.id.mtv_corner)
        MyTextView mtv_corner;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemView != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
