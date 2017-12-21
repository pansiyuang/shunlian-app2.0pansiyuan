package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.PayListEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

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
        mHolder.mtv_pay_name.setText(payTypes.name);
        GlideUtils.getInstance().loadImage(context,mHolder.miv_pay_pic,payTypes.pic);
    }

    public class PayListHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mtv_pay_name)
        MyTextView mtv_pay_name;

        @BindView(R.id.miv_pay_pic)
        MyImageView miv_pay_pic;
        public PayListHolder(View itemView) {
            super(itemView);
        }
    }
}
