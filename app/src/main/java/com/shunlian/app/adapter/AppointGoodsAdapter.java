package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/27.
 */

public class AppointGoodsAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {


    public AppointGoodsAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Goods> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View item_appoint_goods = LayoutInflater.from(context).inflate(R.layout.item_appoint_goods, parent, false);
        return new AppointGoodsHolder(item_appoint_goods);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        AppointGoodsHolder mHolder = (AppointGoodsHolder) holder;
        GoodsDeatilEntity.Goods goods = lists.get(position);
        GlideUtils.getInstance().loadImage(context,mHolder.miv_goods,goods.thumb);
        mHolder.mtv_count.setText("x"+goods.qty);
        mHolder.mtv_title.setText(goods.title);
        mHolder.mtv_price.setText(Common.dotAfterSmall(getString(R.string.rmb)+goods.price,11));
        mHolder.mtv_attribute.setText(goods.sku);
    }

    public class AppointGoodsHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.miv_goods)
        MyImageView miv_goods;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_attribute)
        MyTextView mtv_attribute;

        @BindView(R.id.mtv_count)
        MyTextView mtv_count;

        public AppointGoodsHolder(View itemView) {
            super(itemView);
        }
    }
}
