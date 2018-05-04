package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/1.
 */

public class FirstMoreAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {

    private LayoutInflater mInflater;

    public FirstMoreAdapter(Context context, List<GoodsDeatilEntity.Goods> lists) {
        super(context, true, lists);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isBottom(position) ? manager.getSpanCount() : 1;
                }
            });
        }
    }

    private boolean isBottom(int position) {
        if (position + 1 == getItemCount()){
            return true;
        }
        return false;
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
        return new SingleViewHolder(mInflater.inflate(R.layout.item_first_more, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SingleViewHolder) {
            SingleViewHolder viewHolder = (SingleViewHolder) holder;
            GoodsDeatilEntity.Goods goods=lists.get(position);
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_photo, goods.thumb);
            viewHolder.mtv_title.setText(goods.title);
            SpannableStringBuilder spannableStringBuilder=Common.changeTextSize(getString(R.string.common_yuan)+goods.price,getString(R.string.common_yuan),12);
            viewHolder.mtv_price.setText(spannableStringBuilder);

            viewHolder.mllayout_tag.removeAllViews();

            if ("1".equals(goods.is_new)) {
                viewHolder.mllayout_tag.addView(creatTextTag("新品", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fbd500_2px), viewHolder));
            }

            if ("1".equals(goods.is_hot)) {
                viewHolder.mllayout_tag.addView(creatTextTag("热卖", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fb9f00_2px), viewHolder));
            }

            if ("1".equals(goods.is_explosion)) {
                viewHolder.mllayout_tag.addView(creatTextTag("爆款", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fb6400_2px), viewHolder));
            }

            if ("1".equals(goods.is_recommend)) {
                viewHolder.mllayout_tag.addView(creatTextTag("推荐", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_7898da_2px), viewHolder));
            }

            if ("1".equals(goods.has_coupon)) {
                viewHolder.mllayout_tag.addView(creatTextTag("劵", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
            }

            if ("1".equals(goods.has_discount)) {
                viewHolder.mllayout_tag.addView(creatTextTag("折", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
            }

            if ("1".equals(goods.has_gift)) {
                viewHolder.mllayout_tag.addView(creatTextTag("赠", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
            }

        }
    }




    public TextView creatTextTag(String content, int colorRes, Drawable drawable, SingleViewHolder viewHolder) {
        TextView textView = new TextView(context);
        textView.setText(content);
        textView.setTextSize(9);
        textView.setBackgroundDrawable(drawable);
        textView.setTextColor(colorRes);
        int padding = TransformUtil.dip2px(context, 3f);
        textView.setPadding(padding, 0, padding, 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (viewHolder.mllayout_tag.getChildCount() == 0) {
            params.setMargins(0, 0, 0, 0);
        } else {
            params.setMargins(TransformUtil.dip2px(context, 5.5f), 0, 0, 0);
        }
        textView.setLayoutParams(params);
        return textView;
    }



    public class SingleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mllayout_tag)
        MyLinearLayout mllayout_tag;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        public SingleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
