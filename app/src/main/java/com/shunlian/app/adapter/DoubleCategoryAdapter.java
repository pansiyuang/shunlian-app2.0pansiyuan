package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;


/**
 * Created by Administrator on 2018/1/5.
 */

public class DoubleCategoryAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {
    public static final int BANANER_LAYOUT = 11;

    private LayoutInflater mInflater;
    private SearchGoodsEntity.RefStore mStore;
    private RecyclerView.LayoutParams params;
    private List<GoodsDeatilEntity.Goods> mGoods;

    public DoubleCategoryAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Goods> lists, SearchGoodsEntity.RefStore store) {
        super(context, isShowFooter, lists);
        mInflater = LayoutInflater.from(context);
        params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mStore = store;
        this.mGoods = lists;
    }

    public void setData(List<GoodsDeatilEntity.Goods> lists) {
        this.mGoods = lists;
    }

    public void setStoreData(SearchGoodsEntity.RefStore store) {
        this.mStore = store;
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
        return new DoubleViewHolder(mInflater.inflate(R.layout.item_category_double, parent, false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANANER_LAYOUT:
                TitleViewHolder viewHolder = new TitleViewHolder(mInflater.inflate(R.layout.layout_sort_title, parent, false));
                return viewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mStore != null) {
            return BANANER_LAYOUT;
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
        if (holder instanceof TitleViewHolder) {
            TitleViewHolder viewHolder = (TitleViewHolder) holder;
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_icon, mStore.store_logo);
            viewHolder.tv_name.setText(mStore.store_name);
            viewHolder.tv_comment.setText(mStore.praise_rate + "好评");
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
    }


    public void handItem(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DoubleViewHolder) {
            GoodsDeatilEntity.Goods goods;
            int margin = TransformUtil.dip2px(context, 5f);
            if (mStore != null) {
                goods = mGoods.get(position - 1);
                if (position % 2 == 0) {
                    params.setMargins(0, 0, 0, margin);
                    holder.itemView.setLayoutParams(params);
                } else {
                    params.setMargins(0, 0, margin, margin);
                    holder.itemView.setLayoutParams(params);
                }
            } else {
                goods = mGoods.get(position);
                if (position % 2 == 0) {
                    params.setMargins(0, 0, margin, margin);
                    holder.itemView.setLayoutParams(params);
                } else {
                    params.setMargins(0, 0, 0, margin);
                    holder.itemView.setLayoutParams(params);
                }
            }
            DoubleViewHolder viewHolder = (DoubleViewHolder) holder;
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_icon, goods.thumb);
            viewHolder.tv_title.setText(goods.title);
            viewHolder.tv_price.setText(goods.price);
            String price = getString(R.string.common_yuan) + goods.price;
            Common.firstSmallText(viewHolder.tv_price, price, 12);

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

            if (goods.type == 1) { //是优品
                viewHolder.miv_product.setVisibility(View.VISIBLE);
                if (!isEmpty(goods.self_buy_earn)) { //有字段才显示布局
                    viewHolder.ll_earn.setVisibility(View.VISIBLE);
                    viewHolder.tv_earn_money.setText(getString(R.string.common_yuan) + goods.self_buy_earn);
                } else {
                    viewHolder.ll_earn.setVisibility(View.GONE);
                }
            } else {
                viewHolder.miv_product.setVisibility(View.GONE);
            }

            if ("0".equals(goods.comment_num)) {
                viewHolder.tv_comment.setText("暂无评论");
            } else {
                if ("0".equals(goods.comment_rate)) {
                    viewHolder.tv_comment.setText(goods.comment_num + "条评论");
                } else {
                    viewHolder.tv_comment.setText(goods.comment_num + "条评论  " + goods.comment_rate + "%好评");
                }
            }

            viewHolder.tv_address.setText(goods.send_area);
        }
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BANANER_LAYOUT:
                handTitle(holder);
                break;
            default:
                handItem(holder, position);
                break;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position + 1 == getItemCount()) {
                        return gridManager.getSpanCount();
                    }
                    int type = getItemViewType(position);
                    switch (type) {
                        case BANANER_LAYOUT:
                            return gridManager.getSpanCount();
                        default:
                            return 1;
                    }
                }
            });
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStore != null) {
                        StoreAct.startAct(context, mStore.store_id);
                    }
                }
            });
        }
    }

    public class DoubleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
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

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
