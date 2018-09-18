package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.PayListEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/7.
 */

public class PayListAdapter extends BaseRecyclerAdapter<PayListEntity.PayTypes> {

    public PayListAdapter(Context context, boolean isShowFooter, List<PayListEntity.PayTypes> lists) {
        super(context, isShowFooter, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pay, parent, false);
        return new PayListHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        PayListHolder mHolder = (PayListHolder) holder;
        PayListEntity.PayTypes payTypes = lists.get(position);
        if (!isEmpty(payTypes.desc)){
            mHolder.ntv_desc.setText(payTypes.desc);
            mHolder.ntv_desc.setVisibility(View.VISIBLE);
        }else {
            mHolder.ntv_desc.setVisibility(View.GONE);
        }
        if ("1".equals(payTypes.style)&&!isEmpty(payTypes.name)){
            mHolder.mtv_pay_name.setText(payTypes.name);
            mHolder.mtv_pay_name.setVisibility(View.VISIBLE);
        }else {
            mHolder.mtv_pay_name.setVisibility(View.GONE);
        }

        if (!isEmpty(payTypes.pic)&&!isEmpty(Common.getURLParameterValue(payTypes.pic, "w"))&&!isEmpty(Common.getURLParameterValue(payTypes.pic, "h"))){
            int picHeight=TransformUtil.dip2px(context,20);
            int picWidth= Integer.valueOf(Common.getURLParameterValue(payTypes.pic, "w"))*picHeight/Integer.valueOf(Common.getURLParameterValue(payTypes.pic, "h"));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(picWidth,picHeight);
            params.setMargins(TransformUtil.dip2px(context,10),0,TransformUtil.dip2px(context,26),0);
            mHolder.miv_pay_pic.setLayoutParams(params);
            mHolder.miv_pay_pic.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        GlideUtils.getInstance().loadImage(context,mHolder.miv_pay_pic,payTypes.pic);
    }

    public class PayListHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.ntv_desc)
        NewTextView ntv_desc;

        @BindView(R.id.mtv_pay_name)
        MyTextView mtv_pay_name;

        @BindView(R.id.miv_pay_pic)
        MyImageView miv_pay_pic;
        public PayListHolder(View itemView) {
            super(itemView);
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
