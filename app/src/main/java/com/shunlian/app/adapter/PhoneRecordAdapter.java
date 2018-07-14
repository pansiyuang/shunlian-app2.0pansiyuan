package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.PhoneRecordEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/1.
 */

public class PhoneRecordAdapter extends BaseRecyclerAdapter<PhoneRecordEntity.MData> {


    public PhoneRecordAdapter(Context context, List<PhoneRecordEntity.MData> lists) {
        super(context, true, lists);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phone_record, parent, false);
        return  new SingleViewHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        SingleViewHolder viewHolder = (SingleViewHolder) holder;
        PhoneRecordEntity.MData goods = lists.get(position);
        GlideUtils.getInstance().loadImageZheng(context,viewHolder.miv_goods_pic,goods.image);
        viewHolder.mtv_storeName.setText(goods.finish_time);
        viewHolder.mtv_title.setText(goods.card_addr);
        viewHolder.mtv_attribute.setText(goods.card_number);
        viewHolder.mtv_attributes.setText(goods.face_price);
        viewHolder.mtv_price.setText(goods.payment_money);
        viewHolder.mtv_status.setText(goods.status_name);
    }


    public class SingleViewHolder extends BaseRecyclerViewHolders implements View.OnClickListener {
        @BindView(R.id.miv_goods_pic)
        MyImageView miv_goods_pic;

        @BindView(R.id.mtv_storeName)
        MyTextView mtv_storeName;

        @BindView(R.id.mtv_status)
        MyTextView mtv_status;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_attribute)
        MyTextView mtv_attribute;

        @BindView(R.id.mtv_attributes)
        MyTextView mtv_attributes;

        public SingleViewHolder(View itemView) {
            super(itemView);
        }

    }
}
