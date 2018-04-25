package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.VouchercenterplEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class CouponAdapter extends BaseRecyclerAdapter<VouchercenterplEntity.MData> {

    public CouponAdapter(Context context, boolean isShowFooter,
                         List<VouchercenterplEntity.MData> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_get_coupon, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        VouchercenterplEntity.MData data = lists.get(position);
        GlideUtils.getInstance().loadImage(context, mHolder.miv_photo, data.thumb);
        mHolder.mtv_title.setText(data.title);
        mHolder.mtv_desc.setText(data.valid_date);
        SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.common_yuan)+data.denomination+data.use_condition
                , data.denomination, 21);
        mHolder.mtv_price.setText(spannableStringBuilder);
        mHolder.mtv_yiqiang.setText(data.already_get);
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_yiqiang)
        MyTextView mtv_yiqiang;

        @BindView(R.id.mtv_get)
        MyTextView mtv_get;

        public ActivityMoreHolder(View itemView) {
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
