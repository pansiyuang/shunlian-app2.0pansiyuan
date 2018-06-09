package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.newchat.entity.ChatGoodsEntity;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/2.
 */

public class LittleStoreAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {
    private boolean isEditMode;

    public LittleStoreAdapter(Context context, List<GoodsDeatilEntity.Goods> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new LittleStoreHolderView(LayoutInflater.from(context).inflate(R.layout.item_goods_from, parent, false));
    }

    public void setEditMode(boolean isEdit) {
        isEditMode = isEdit;
        notifyDataSetChanged();
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LittleStoreHolderView) {
            LittleStoreHolderView littleHolderView = (LittleStoreHolderView) holder;
            GoodsDeatilEntity.Goods goods = lists.get(position);

            GlideUtils.getInstance().loadImage(context, littleHolderView.miv_icon, goods.thumb, false);

            littleHolderView.tv_goods_title.setText(goods.title);
            if (!isEmpty(goods.price)) {
                littleHolderView.tv_price.setText(getString(R.string.common_yuan) + " " + goods.price);
            }
            littleHolderView.tv_from.setText(goods.from);

            if (isEditMode) {
                littleHolderView.tv_select.setVisibility(View.VISIBLE);
            } else {
                littleHolderView.tv_select.setVisibility(View.GONE);
            }

            if (goods.isSelect) {
                littleHolderView.tv_select.setBackgroundDrawable(getDrawable(R.drawable.oval_soild_pink));
            } else {
                littleHolderView.tv_select.setBackgroundDrawable(getDrawable(R.drawable.oval_stroke_pink));
            }


            if (goods.index > 0) {
                littleHolderView.tv_select.setText(String.valueOf(goods.index));
            } else {
                littleHolderView.tv_select.setText("");
            }
        }
    }

    public class LittleStoreHolderView extends BaseRecyclerViewHolder implements View.OnClickListener {
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

        public LittleStoreHolderView(View itemView) {
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