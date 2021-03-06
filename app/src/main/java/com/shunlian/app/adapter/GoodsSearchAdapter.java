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
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/1.
 */

public class GoodsSearchAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {

    private LayoutInflater mInflater;
    private SearchGoodsEntity.RefStore mStore;
    private List<String> mGoods_list;
    private List<GoodsDeatilEntity.Goods> mGoods;

    public GoodsSearchAdapter(Context context, List<GoodsDeatilEntity.Goods> lists,
                              SearchGoodsEntity.RefStore store,
                              List<String> goods_list) {
        super(context, true, lists);
        mInflater = LayoutInflater.from(context);
        this.mGoods = lists;
        this.mStore = store;
        mGoods_list = goods_list;
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
        return new SingleViewHolder(mInflater.inflate(R.layout.item_goods_search, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SingleViewHolder) {
            GoodsDeatilEntity.Goods goods;
            if (mStore != null) {
                goods = mGoods.get(position>0?position - 1:0);
            } else {
                goods = mGoods.get(position);
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

            if (mGoods_list.contains(goods.id)){
                viewHolder.mtv_corner.setBackgroundDrawable(getDrawable(R.drawable.oval_soild_pink));
                viewHolder.mtv_corner.setText(String.valueOf(mGoods_list.indexOf(goods.id)+1));
            }else {
                viewHolder.mtv_corner.setBackgroundDrawable(getDrawable(R.drawable.oval_stroke_pink));
                viewHolder.mtv_corner.setText("");
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

        if (viewHolder.ll_tag.getChildCount() == 0) {
            params.setMargins(0, 0, 0, 0);
        } else {
            params.setMargins(TransformUtil.dip2px(context, 5.5f), 0, 0, 0);
        }
        textView.setLayoutParams(params);
        return textView;
    }



    public class SingleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
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
