package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class FirstHorizonAdapter extends BaseRecyclerAdapter<GetDataEntity.MData.MMData> {
    private boolean isNew = false;

    public FirstHorizonAdapter(Context context, boolean isShowFooter,
                               List<GetDataEntity.MData.MMData> lists, boolean isNew) {
        super(context, isShowFooter, lists);
        this.isNew = isNew;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_first_goods, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        try {
            GetDataEntity.MData.MMData data = lists.get(position);
            SpannableStringBuilder priceBuilder = Common.changeTextSize(getString(R.string.common_yuan) + data.price,
                    getString(R.string.common_yuan), 11);
            mHolder.mtv_price.setText(priceBuilder);
            if (isNew) {
                mHolder.mtv_title.setText(data.title);
                mHolder.mtv_title.setVisibility(View.VISIBLE);
                mHolder.mtv_earn_price.setVisibility(View.VISIBLE);
                mHolder.mtv_earn_price.setEarnMoney(data.self_buy_earn,11);
            } else {
                mHolder.mtv_title.setVisibility(View.GONE);
                mHolder.mtv_earn_price.setEarnMoney(data.self_buy_earn,11);
//                if (!TextUtils.isEmpty(data.self_buy_earn)) {
//                    mHolder.mtv_earn_price.setText(data.self_buy_earn);
//                    mHolder.mtv_earn_price.setVisibility(View.VISIBLE);
//                } else {
//                    mHolder.mtv_earn_price.setVisibility(View.GONE);
//                }
            }
            GlideUtils.getInstance().loadImageZheng(context, mHolder.miv_photo, data.thumb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolders implements View.OnClickListener {
        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_earn_price)
        MyTextView mtv_earn_price;

        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

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
