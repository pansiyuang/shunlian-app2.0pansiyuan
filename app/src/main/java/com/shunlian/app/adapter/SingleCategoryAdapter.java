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
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;


/**
 * Created by Administrator on 2018/1/5.
 */

public class SingleCategoryAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {
    private static final int TITLE_LAYOUT = 10;

    private LayoutInflater mInflater;
    private SearchGoodsEntity.RefStore mStore;

    public SingleCategoryAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Goods> lists, SearchGoodsEntity.RefStore store) {
        super(context, isShowFooter, lists);
        mInflater = LayoutInflater.from(context);
        this.mStore = store;
    }

    public SingleCategoryAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Goods> lists) {
        super(context, isShowFooter, lists);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new SingleViewHolder(mInflater.inflate(R.layout.item_category_single, parent, false));
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE_LAYOUT:
                TitleViewHolder viewHolder = new TitleViewHolder(mInflater.inflate(R.layout.layout_sort_title, parent, false));
                return viewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mStore != null) {
            return TITLE_LAYOUT;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mStore != null) {
            return super.getItemCount() + 1;
        }
        return super.getItemCount();
    }

    public void handTitle(RecyclerView.ViewHolder holder) {
        TitleViewHolder viewHolder = (TitleViewHolder) holder;

        GlideUtils.getInstance().loadImage(context, viewHolder.miv_icon, mStore.store_logo);
        viewHolder.tv_name.setText(mStore.store_name);
        viewHolder.tv_comment.setText(mStore.praise_rate);
        if (!isEmpty(mStore.head_banner)) {
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_bananer, mStore.head_banner);
            viewHolder.miv_bananer.setVisibility(View.VISIBLE);
            viewHolder.ll_score.setVisibility(View.GONE);
        } else {
            viewHolder.miv_bananer.setVisibility(View.GONE);
            viewHolder.ll_score.setVisibility(View.VISIBLE);

            if (mStore.pj != null && mStore.pj.size() != 0) {
                List<SearchGoodsEntity.Comment> comments = mStore.pj;
                if (comments.get(0) != null) {
                    viewHolder.tv_describe.setText(comments.get(0).name + " " + comments.get(0).score);
                }
                if (comments.get(1) != null) {
                    viewHolder.tv_service.setText(comments.get(1).name + " " + comments.get(1).score);
                }
                if (comments.get(2) != null) {
                    viewHolder.tv_service.setText(comments.get(2).name + " " + comments.get(2).score);
                }
            }
        }
    }

    public void handleItem(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SingleViewHolder) {
            GoodsDeatilEntity.Goods goods;
            if (mStore != null) {
                goods = lists.get(position - 1);
            } else {
                goods = lists.get(position);
            }
            SingleViewHolder viewHolder = (SingleViewHolder) holder;
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_icon, goods.thumb);
            viewHolder.tv_title.setText(goods.title);
            viewHolder.tv_price.setText(goods.price);

            if ("1".equals(goods.free_ship)) {
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

            if ("1".equals(goods.is_explosion)) {
                viewHolder.ll_tag.addView(creatTextTag("爆款", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fb6400_2px), viewHolder));
            }

            if ("1".equals(goods.is_recommend)) {
                viewHolder.ll_tag.addView(creatTextTag("推荐", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_7898da_2px), viewHolder));
            }

            if ("1".equals(goods.has_coupon)) {
                viewHolder.ll_tag.addView(creatTextTag("劵", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
            }

            if ("1".equals(goods.has_discount)) {
                viewHolder.ll_tag.addView(creatTextTag("折", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
            }

            if ("1".equals(goods.has_gift)) {
                viewHolder.ll_tag.addView(creatTextTag("赠", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), viewHolder));
            }

            viewHolder.tv_comment.setText(String.format(getString(R.string.sort_comment), goods.comment_num, goods.comment_rate));
            viewHolder.tv_address.setText(goods.send_area);
        }
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TITLE_LAYOUT:
                handTitle(holder);
                break;
            default:
                handleItem(holder, position);
                break;
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

        if (viewHolder.ll_tag.getChildCount() == 0) {
            params.setMargins(0, 0, 0, 0);
        } else {
            params.setMargins(TransformUtil.dip2px(context, 5.5f), 0, 0, 0);
        }
        textView.setLayoutParams(params);
        return textView;
    }

    public class TitleViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_bananer)
        MyImageView miv_bananer;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_comment)
        TextView tv_comment;

        @BindView(R.id.ll_score)
        LinearLayout ll_score;

        @BindView(R.id.tv_describe)
        TextView tv_describe;

        @BindView(R.id.tv_service)
        TextView tv_service;

        @BindView(R.id.tv_logistics)
        TextView tv_logistics;

        public TitleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class SingleViewHolder extends BaseRecyclerViewHolder {
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

        public SingleViewHolder(View itemView) {
            super(itemView);
        }
    }
}