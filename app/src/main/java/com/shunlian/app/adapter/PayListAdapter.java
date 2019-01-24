package com.shunlian.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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

    public int mCurrentPosition = 0;
    private boolean isShowMore = true;

    public PayListAdapter(Context context, List<PayListEntity.PayTypes> lists) {
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
            visible(mHolder.ntv_desc);
        }else {
            gone(mHolder.ntv_desc);
        }
        if ("1".equals(payTypes.style)&&!isEmpty(payTypes.name)){
            mHolder.mtv_pay_name.setText(payTypes.name);
            visible(mHolder.mtv_pay_name);
            gone(mHolder.miv_pic_end);
        }else if ("3".equals(payTypes.style)&&!isEmpty(payTypes.name)){
            mHolder.mtv_pay_name.setText(payTypes.name);
            visible(mHolder.mtv_pay_name,mHolder.miv_pic_end);
            setWH(payTypes.end_pic,mHolder.miv_pic_end,165, payTypes.code);
            GlideUtils.getInstance().loadImage(context,mHolder.miv_pic_end,payTypes.end_pic);
        }else {
            gone(mHolder.miv_pic_end,mHolder.mtv_pay_name);
        }

        if (!isEmpty(payTypes.pic)&&!isEmpty(Common.getURLParameterValue(payTypes.pic, "w"))
                &&!isEmpty(Common.getURLParameterValue(payTypes.pic, "h"))){
            setWH(payTypes.pic,mHolder.miv_pay_pic,10,payTypes.code);
        }

        GlideUtils.getInstance().loadImage(context,mHolder.miv_pay_pic,payTypes.pic);



        if (position == 0){
            mHolder.mtv_pay_name.setTextSize(18);
        }else {
            mHolder.mtv_pay_name.setTextSize(14);
        }

        if (mCurrentPosition == position){
            mHolder.miv_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
        }else {
            mHolder.miv_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
        }

        if (isShowMore){
            if (position == 0){
                visible(mHolder.ll_more);
                mHolder.setVisibility(true);
            }else {
                mHolder.setVisibility(false);
            }
        }else {
            gone(mHolder.ll_more);
            mHolder.setVisibility(true);
        }
    }

    @NonNull
    private void setWH(String url, MyImageView imageView, int marginLeft, String code) {
        int picHeight = 0;
        if ("alipay".equals(code)){
            picHeight = TransformUtil.dip2px(context,38);
        }else {
            picHeight= TransformUtil.dip2px(context,20);
        }
        int picWidth= Integer.valueOf(Common.getURLParameterValue(url, "w"))
                *picHeight/Integer.valueOf(Common.getURLParameterValue(url, "h"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(picWidth,picHeight);
        params.setMargins(TransformUtil.dip2px(context,marginLeft),
                0,0,0);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    public class PayListHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.ntv_desc)
        NewTextView ntv_desc;

        @BindView(R.id.mtv_pay_name)
        MyTextView mtv_pay_name;

        @BindView(R.id.miv_pay_pic)
        MyImageView miv_pay_pic;

        @BindView(R.id.miv_pic_end)
        MyImageView miv_pic_end;

        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.ll_more)
        LinearLayout ll_more;

        public PayListHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ll_more.setOnClickListener(this);
        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.width = RecyclerView.LayoutParams.MATCH_PARENT;// 这里注意使用自己布局的根布局类型
                param.height = RecyclerView.LayoutParams.WRAP_CONTENT;// 这里注意使用自己布局的根布局类型
                visible(itemView);
            } else {
                gone(itemView);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ll_more){
                isShowMore = false;
                notifyDataSetChanged();
            }else {
                if (listener != null){
                    listener.onItemClick(v,getAdapterPosition());
                }
            }
        }
    }
}
