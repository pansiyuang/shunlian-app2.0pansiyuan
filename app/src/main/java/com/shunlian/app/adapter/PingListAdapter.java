package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/1.
 */

public class PingListAdapter extends BaseRecyclerAdapter<CorePingEntity.MData> {

    private LayoutInflater mInflater;

    public PingListAdapter(Context context, List<CorePingEntity.MData> lists) {
        super(context, true, lists);
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 设置baseFooterHolder  layoutparams
     *
     * @param baseFooterHolder
     */
    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
//        自定义分页尾布局
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
        return new SingleViewHolder(mInflater.inflate(R.layout.item_ping_list, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SingleViewHolder) {
            SingleViewHolder viewHolder = (SingleViewHolder) holder;
            CorePingEntity.MData goods=lists.get(position);
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_photo, goods.thumb);
            viewHolder.mtv_title.setText(goods.title);
            SpannableStringBuilder spannableStringBuilder=Common.changeTextSize(getString(R.string.common_yuan)+goods.price,getString(R.string.common_yuan),12);
            viewHolder.mtv_price.setText(spannableStringBuilder);
            viewHolder.mtv_marketPrice.setText(goods.market_price);
            viewHolder.mtv_marketPrice.setStrikethrough();
            viewHolder.mtv_gou.setText(String.format(getString(R.string.first_yigou),goods.sales));
            if (!isEmpty(goods.stock)&&Float.parseFloat(goods.stock)==0) {
                viewHolder.miv_seller_out.setVisibility(View.VISIBLE);
                viewHolder.mtv_price.setTextColor(getColor(R.color.value_A0A0A0));
            } else {
                viewHolder.miv_seller_out.setVisibility(View.GONE);
                viewHolder.mtv_price.setTextColor(getColor(R.color.pink_color));
            }
        }
    }



    public class SingleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.miv_seller_out)
        MyImageView miv_seller_out;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_gou)
        MyTextView mtv_gou;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_marketPrice)
        MyTextView mtv_marketPrice;

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
