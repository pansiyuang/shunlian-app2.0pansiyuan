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
import com.shunlian.app.utils.TransformUtil;
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
            mHolder.mtv_price.setText(Common.changeTextSize(listBean.face_price+"元","元",15));
            if (isEmpty(listBean.sale_price)){
                mHolder.mtv_rel_price.setVisibility(View.INVISIBLE);
            }else {
                visible(mHolder.mtv_rel_price);
                mHolder.mtv_rel_price.setText("售价："+listBean.sale_price);
            }

            GradientDrawable rootGB = (GradientDrawable) mHolder.rlayout_root.getBackground();
            int w = TransformUtil.dip2px(context, 1);
            if ("1".equals(listBean.isBuy)) {
                mHolder.rlayout_root.setEnabled(true);
            }else {
                rootGB.setColor(Color.parseColor("#90f7f7f7"));
                mHolder.rlayout_root.setEnabled(false);
            }
            if (position == currentPos) {
                rootGB.setColor(getColor(R.color.pink_color));
                rootGB.setStroke(w, getColor(R.color.pink_color));
                mHolder.mtv_price.setTextColor(Color.WHITE);
                mHolder.mtv_rel_price.setTextColor(Color.WHITE);
            } else {
                rootGB.setColor(Color.WHITE);
                rootGB.setStroke(w, getColor(R.color.color_value_6c));
                mHolder.mtv_price.setTextColor(getColor(R.color.value_484848));
                mHolder.mtv_rel_price.setTextColor(Color.parseColor("#FF767676"));
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

        public MoreCreditHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(v,getAdapterPosition());
            });
        }
    }
}
