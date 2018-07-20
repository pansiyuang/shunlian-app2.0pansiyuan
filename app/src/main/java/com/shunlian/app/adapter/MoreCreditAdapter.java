package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.MoreCreditEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/7/13.
 */

public class MoreCreditAdapter extends BaseRecyclerAdapter<MoreCreditEntity.ListBean> {

    public int currentPos = -1;//当前选中位置

    public MoreCreditAdapter(Context context, List<MoreCreditEntity.ListBean> lists) {
        super(context, false, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_more_credit, parent, false);
        return new MoreCreditHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MoreCreditHolder){
            MoreCreditHolder mHolder = (MoreCreditHolder) holder;
            MoreCreditEntity.ListBean listBean = lists.get(position);

            mHolder.mtv_price.setText(Common.changeTextSize(getString(R.string.rmb)
                    +listBean.face_price,getString(R.string.rmb),15));

            if (isEmpty(listBean.sale_price)){
                gone(mHolder.mtv_rel_price);
            }else {
                visible(mHolder.mtv_rel_price);
                String format = "售价%s元";
                mHolder.mtv_rel_price.setText(String.format(format,listBean.sale_price));
            }

            GradientDrawable rootGB = (GradientDrawable) mHolder.rlayout_root.getBackground();
            if ("1".equals(listBean.isBuy)) {
                gone(mHolder.mtv_mask);
                mHolder.rlayout_root.setEnabled(true);
                if (position == currentPos) {
                    rootGB.setColor(getColor(R.color.pink_color));
                    mHolder.mtv_price.setTextColor(Color.WHITE);
                    mHolder.mtv_rel_price.setTextColor(Color.WHITE);
                } else {
                    rootGB.setColor(Color.WHITE);
                    mHolder.mtv_price.setTextColor(getColor(R.color.value_484848));
                    mHolder.mtv_rel_price.setTextColor(getColor(R.color.pink_color));
                }
            }else {
                visible(mHolder.mtv_mask);
                rootGB.setColor(Color.WHITE);
                mHolder.mtv_price.setTextColor(getColor(R.color.value_484848));
                mHolder.mtv_rel_price.setTextColor(getColor(R.color.pink_color));
                mHolder.rlayout_root.setEnabled(false);
            }

        }
    }

    public class MoreCreditHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.rlayout_root)
        RelativeLayout rlayout_root;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_rel_price)
        MyTextView mtv_rel_price;

        @BindView(R.id.mtv_mask)
        MyTextView mtv_mask;

        public MoreCreditHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(v,getAdapterPosition());
            });
        }
    }
}
