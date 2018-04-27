package com.shunlian.app.newchat.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.newchat.entity.ChatGoodsEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ChatGoodsAdapter extends BaseRecyclerAdapter<ChatGoodsEntity.Goods> {

    public ChatGoodsAdapter(Context context, List<ChatGoodsEntity.Goods> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new GoodsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_goods_from, parent, false));
    }

    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.value_302E56));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.value_302E56));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.value_302E56));
        baseFooterHolder.layout_no_more.setText(getString(R.string.no_more_goods));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
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
                    return isBottom(position) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    private boolean isBottom(int position) {
        if (position + 1 == getItemCount()) {
            return true;
        }
        return false;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsViewHolder) {
            GoodsViewHolder goodsHolderView = (GoodsViewHolder) holder;
            ChatGoodsEntity.Goods goods = lists.get(position);

            GlideUtils.getInstance().loadImage(context, goodsHolderView.miv_icon, goods.thumb);
            goodsHolderView.tv_goods_title.setText(goods.title);
            if (!isEmpty(goods.price)) {
                goodsHolderView.tv_price.setText(getString(R.string.common_yuan) + " " + goods.price);
            }
            goodsHolderView.tv_from.setText(goods.from);

            if (goods.isSelect) {
                goodsHolderView.tv_select.setBackgroundDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_h));
            } else {
                goodsHolderView.tv_select.setBackgroundDrawable(getDrawable(R.drawable.oval_stroke_pink));
            }
        }
    }

    public class GoodsViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_goods_title)
        TextView tv_goods_title;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_from)
        TextView tv_from;

        @BindView(R.id.tv_select)
        TextView tv_select;

        @BindView(R.id.rl_rootView)
        RelativeLayout rl_rootView;

        public GoodsViewHolder(View itemView) {
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
