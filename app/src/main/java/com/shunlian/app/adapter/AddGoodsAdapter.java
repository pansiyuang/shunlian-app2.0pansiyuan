package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.discover.other.ExperiencePublishActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/27.
 */

public class AddGoodsAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {
    private boolean isEditMode;
    private String currentFrom;

    public AddGoodsAdapter(Context context, boolean isShowFooter, boolean isEdit, List<GoodsDeatilEntity.Goods> lists, String from) {
        super(context, isShowFooter, lists);
        this.isEditMode = isEdit;
        this.currentFrom = from;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new GoodsHolderView(LayoutInflater.from(context).inflate(R.layout.item_goods_from, parent, false));
    }

    public void setEditMode(boolean isEdit) {
        isEditMode = isEdit;
        notifyDataSetChanged();
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
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsHolderView) {
            GoodsHolderView goodsHolderView = (GoodsHolderView) holder;
            GoodsDeatilEntity.Goods goods = lists.get(position);

            GlideUtils.getInstance().loadImage(context, goodsHolderView.miv_icon, goods.thumb,false);
            goodsHolderView.tv_goods_title.setText(goods.title);
            if (!isEmpty(goods.price)) {
                goodsHolderView.tv_price.setText(getString(R.string.common_yuan) + " " + goods.price);
            }
            goodsHolderView.tv_from.setText(goods.from);

            if (isEditMode) {
                goodsHolderView.tv_select.setVisibility(View.VISIBLE);
            } else {
                goodsHolderView.tv_select.setVisibility(View.GONE);
            }

            if (goods.isSelect) {
                if (ExperiencePublishActivity.FROM_EXPERIENCE_PUBLISH.equals(currentFrom)) {
                    goodsHolderView.tv_select.setBackgroundDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_h));
                    goodsHolderView.tv_select.setText("");
                } else {
                    goodsHolderView.tv_select.setBackgroundDrawable(getDrawable(R.drawable.oval_soild_pink));
                }
            } else {
                goodsHolderView.tv_select.setBackgroundDrawable(getDrawable(R.drawable.oval_stroke_pink));
            }

            if (goods.index > 0) {
                goodsHolderView.tv_select.setText(String.valueOf(goods.index));
            } else {
                goodsHolderView.tv_select.setText("");
            }
        }
    }

    public class GoodsHolderView extends BaseRecyclerViewHolder implements View.OnClickListener {
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

        public GoodsHolderView(View itemView) {
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
