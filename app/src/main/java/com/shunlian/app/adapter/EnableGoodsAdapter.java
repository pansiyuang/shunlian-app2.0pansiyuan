package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/20.
 */

public class EnableGoodsAdapter extends BaseRecyclerAdapter<ShoppingCarEntity.Enabled.Goods> {
    private List<ShoppingCarEntity.Enabled.Goods> mGoods;
    private Context mContext;

    public EnableGoodsAdapter(Context context, boolean isShowFooter, List<ShoppingCarEntity.Enabled.Goods> lists) {
        super(context, isShowFooter, lists);
        this.mGoods = lists;
        this.mContext = context;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        EnableViewHolder enableViewHolder = new EnableViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shoppingcar_goods, parent, false));
        return enableViewHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        int leftCount;
        EnableViewHolder enableViewHolder = (EnableViewHolder) holder;
        ShoppingCarEntity.Enabled.Goods goods = mGoods.get(position);

        GlideUtils.getInstance().loadImage(mContext, enableViewHolder.miv_goods, goods.thumb);
        enableViewHolder.tv_goods_title.setText(goods.title);
        enableViewHolder.tv_goods_param.setText(goods.sku);
        if (!TextUtils.isEmpty(goods.left)) {
            leftCount = Integer.valueOf(goods.left);
            if (leftCount > 0 && leftCount < 3) {
//                enableViewHolder.tv_goods_notice.setText(  String.format(mContext.getResources().getText(R.string.count_notice),3));
            }
        }
    }

    public class EnableViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.miv_goods)
        MyImageView miv_goods;
        @BindView(R.id.tv_goods_title)
        TextView tv_goods_title;
        @BindView(R.id.tv_goods_param)
        TextView tv_goods_param;
        @BindView(R.id.tv_goods_notice)
        TextView tv_goods_notice;
        @BindView(R.id.tv_goods_price)
        TextView tv_goods_price;
        @BindView(R.id.tv_goods_count)
        TextView tv_goods_count;

        public EnableViewHolder(View itemView) {
            super(itemView);
        }
    }
}
