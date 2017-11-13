package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/10.
 */

public class GoodsDetailShopAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.StoreInfo.Item> {


    public GoodsDetailShopAdapter(Context context, boolean isShowFooter,
                                  List<GoodsDeatilEntity.StoreInfo.Item> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_goods_detail, parent, false);
        return new GoodsDetailShopHolder(inflate);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        GoodsDetailShopHolder mHoler = (GoodsDetailShopHolder) holder;
        GoodsDeatilEntity.StoreInfo.Item item = lists.get(position);
        GlideUtils.getInstance().loadImage(context,mHoler.miv_shop_head,item.thumb);
        mHoler.mtv_title.setText(item.title);
        mHoler.mtv_price.setText("￥"+item.price);
    }

    public class GoodsDetailShopHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.miv_shop_head)
        MyImageView miv_shop_head;

        public GoodsDetailShopHolder(View itemView) {
            super(itemView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mtv_title.getLayoutParams();
            int marginsw = TransformUtil.countRealWidth(context, 10);
            int width = TransformUtil.countRealWidth(context, 198);
            int marginsh = TransformUtil.countRealHeight(context, 15);
            layoutParams.setMargins(marginsw,marginsh,marginsw,0);
            layoutParams.width = width;
            mtv_title.setLayoutParams(layoutParams);
        }
    }
}

