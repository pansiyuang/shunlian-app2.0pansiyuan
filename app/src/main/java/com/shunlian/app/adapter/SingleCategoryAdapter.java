package com.shunlian.app.adapter;

import android.app.Activity;
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
import com.shunlian.app.ui.category.CategoryAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.flowlayout.FlowLayout;
import com.shunlian.app.widget.flowlayout.TagAdapter;
import com.shunlian.app.widget.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;


/**
 * Created by Administrator on 2018/1/5.
 */

public class SingleCategoryAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {
    private static final int TITLE_LAYOUT = 10;
    public static final int KEYWORD_LAYOUT = 12;

    private LayoutInflater mInflater;
    private SearchGoodsEntity.RefStore mStore;
    private List<GoodsDeatilEntity.Goods> mGoods;
    private List<String> mKeywords;
    private StringBuffer mSb;

    public SingleCategoryAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Goods> lists, SearchGoodsEntity.RefStore store, List<String> keywordList) {
        super(context, isShowFooter, lists);
        mInflater = LayoutInflater.from(context);
        this.mGoods = lists;
        this.mStore = store;
        this.mKeywords = keywordList;
        mSb = new StringBuffer();
    }

    public SingleCategoryAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Goods> lists) {
        super(context, isShowFooter, lists);
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<GoodsDeatilEntity.Goods> lists) {
        this.mGoods = lists;
    }

    public void setStoreData(SearchGoodsEntity.RefStore store) {
        this.mStore = store;
    }

    public void setKeywordData(List<String> keywordList) {
        this.mKeywords = keywordList;
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
        return new SingleViewHolder(mInflater.inflate(R.layout.item_category_single, parent, false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE_LAYOUT:
                TitleViewHolder viewHolder = new TitleViewHolder(mInflater.inflate(R.layout.layout_sort_title, parent, false));
                return viewHolder;
            case KEYWORD_LAYOUT:
                KeywordViewHolder keywordViewHolder = new KeywordViewHolder(mInflater.inflate(R.layout.layout_search_keyword, parent, false));
                return keywordViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mStore != null) {
            return TITLE_LAYOUT;
        }
        if (position == 0 && !isEmpty(mKeywords)) {
            return KEYWORD_LAYOUT;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mStore != null) {
            return super.getItemCount() + 1;
        }
        if (!isEmpty(mKeywords)) {
            return super.getItemCount() + 1;
        }
        return super.getItemCount();
    }

    public void handTitle(RecyclerView.ViewHolder holder) {
        TitleViewHolder viewHolder = (TitleViewHolder) holder;

        GlideUtils.getInstance().loadImage(context, viewHolder.miv_icon, mStore.store_logo);
        viewHolder.tv_name.setText(mStore.store_name);
        viewHolder.tv_comment.setText(mStore.praise_rate + "%好评");
        if (!isEmpty(mStore.head_banner)) {
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_bananer, mStore.head_banner);
            viewHolder.miv_bananer.setVisibility(View.VISIBLE);
            viewHolder.ll_score.setVisibility(View.GONE);

            int screenWidth = DeviceInfoUtil.getDeviceWidth(context);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.miv_bananer.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            double height = screenWidth / 750d * 260;
            layoutParams.height = (int) Math.round(height);
            viewHolder.miv_bananer.setLayoutParams(layoutParams);
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
        try {
            if (holder instanceof SingleViewHolder) {
                GoodsDeatilEntity.Goods goods;
                if (mStore != null || !isEmpty(mKeywords)) {
                    goods = lists.get(position - 1);
                } else {
                    goods = lists.get(position);
                }
                if (goods == null) {
                    return;
                }
                SingleViewHolder viewHolder = (SingleViewHolder) holder;
                GlideUtils.getInstance().loadImage(context, viewHolder.miv_icon, goods.thumb);
                if (viewHolder.tv_title != null)
                    viewHolder.tv_title.setText(goods.title);
                String price = getString(R.string.common_yuan) + goods.price;
                viewHolder.tv_price.setText(price);

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

                mSb.setLength(0);
                if (!isEmpty(goods.sales_desc)) {
                    mSb.append(goods.sales_desc);
                }
                if (!isEmpty(goods.comment_rate)) {
                    int rate = Integer.valueOf(goods.comment_rate);
                    if (rate <= 0) {
                        viewHolder.tv_comment.setText(mSb.toString());
                    } else {
                        if (isEmpty(goods.sales_desc)) {
                            mSb.append(goods.comment_rate + "%好评");
                        } else {
                            mSb.append("  " + goods.comment_rate + "%好评");
                        }
                        viewHolder.tv_comment.setText(mSb.toString());
                    }
                }

                if (isEmpty(goods.tag_pic)) {
                    viewHolder.miv_act.setVisibility(View.GONE);
                } else {
                    viewHolder.miv_act.setVisibility(View.VISIBLE);
                    GlideUtils.getInstance().loadImageZheng(context, viewHolder.miv_act, goods.tag_pic);
                }
                if (1 == goods.is_sell_out) {
                    viewHolder.miv_seller_out.setVisibility(View.VISIBLE);
                    viewHolder.tv_price.setTextColor(getColor(R.color.value_A0A0A0));
                } else {
                    viewHolder.miv_seller_out.setVisibility(View.GONE);
                    viewHolder.tv_price.setTextColor(getColor(R.color.value_484848));
                }

                viewHolder.tv_address.setText(goods.send_area);
                if (goods.type == 1) { //是优品
                    viewHolder.miv_product.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.miv_product.setVisibility(View.GONE);
                }
                if (!isEmpty(goods.self_buy_earn)) { //有字段才显示布局
                    viewHolder.tv_earn_money.setVisibility(View.VISIBLE);
                    viewHolder.tv_earn_money.setText(goods.self_buy_earn);
                } else {
                    viewHolder.tv_earn_money.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handlKeyword(RecyclerView.ViewHolder holder) {
        if (holder instanceof KeywordViewHolder) {
            KeywordViewHolder keywordViewHolder = (KeywordViewHolder) holder;
            TagAdapter historyAdapter = new TagAdapter<String>(mKeywords) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    View view = LayoutInflater.from(context).inflate(R.layout.item_goods_tag_layout, keywordViewHolder.taglayout_keyword, false);
                    TextView tv = view.findViewById(R.id.tv_history_tag);
                    tv.setText(s);
                    return view;
                }
            };
            keywordViewHolder.taglayout_keyword.setAdapter(historyAdapter);
            if (!isEmpty(mKeywords.get(0))) {
                keywordViewHolder.tv_keyword.setText("我们为您推荐了“" + mKeywords.get(0) + "”");
            }
            keywordViewHolder.taglayout_keyword.setOnTagClickListener((view, position, parent) -> {
                String s = mKeywords.get(position);
                if (context instanceof CategoryAct) {
                    ((CategoryAct) context).requestData(s);
                }
                return false;
            });
        }
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TITLE_LAYOUT:
                handTitle(holder);
                break;
            case KEYWORD_LAYOUT:
                handlKeyword(holder);
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
            itemView.setOnClickListener(v -> {
                if (mStore != null) {
                    StoreAct.startAct(context, mStore.store_id);
                }
            });
        }
    }

    public class KeywordViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.taglayout_keyword)
        TagFlowLayout taglayout_keyword;

        @BindView(R.id.tv_keyword)
        TextView tv_keyword;

        public KeywordViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class SingleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_product)
        MyImageView miv_product;

        @BindView(R.id.miv_seller_out)
        MyImageView miv_seller_out;

        @BindView(R.id.miv_act)
        MyImageView miv_act;

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

        @BindView(R.id.tv_earn_money)
        TextView tv_earn_money;

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
