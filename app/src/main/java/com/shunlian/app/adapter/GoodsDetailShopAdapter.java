package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.Common;
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
        if (holder instanceof GoodsDetailShopHolder) {
            GoodsDetailShopHolder mHoler = (GoodsDetailShopHolder) holder;
            GoodsDeatilEntity.StoreInfo.Item item = lists.get(position);
            GlideUtils.getInstance().loadOverrideImage(context,
                    mHoler.miv_shop_head, item.thumb, 198, 198);
            mHoler.mtv_title.setText(item.title);
            SpannableStringBuilder spannableStringBuilder = Common.
                    changeTextSize(getString(R.string.rmb).concat(item.price), getString(R.string.rmb), 12);
            mHoler.mtv_price.setText(spannableStringBuilder);

            if (isEmpty(item.tag)){
                gone(mHoler.miv_tag);
            }else {
                visible(mHoler.miv_tag);
                GlideUtils.getInstance().loadOverrideImage(context,mHoler.miv_tag,item.tag,
                        56,56);
            }

            if (isEmpty(item.self_buy_earn)){
                gone(mHoler.mtv_earn_much);
            }else {
                visible(mHoler.mtv_earn_much);
                mHoler.mtv_earn_much.setEarnMoney(item.self_buy_earn,12);
            }
        }
    }

    public class GoodsDetailShopHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.miv_shop_head)
        MyImageView miv_shop_head;

        @BindView(R.id.miv_tag)
        MyImageView miv_tag;

        @BindView(R.id.mtv_earn_much)
        MyTextView mtv_earn_much;

        public GoodsDetailShopHolder(View itemView) {
            super(itemView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mtv_title.getLayoutParams();
            int marginsw = TransformUtil.countRealWidth(context, 10);
            int width = TransformUtil.countRealWidth(context, 198);
            int marginsh = TransformUtil.countRealHeight(context, 15);
            layoutParams.setMargins(marginsw,marginsh,marginsw,0);
            layoutParams.width = width;
            mtv_title.setLayoutParams(layoutParams);
            miv_shop_head.setWHProportion(198,198);
            miv_shop_head.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}

