package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CreditPhoneListEntity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/7/17.
 */

public class TopUpHistoryAdapter extends BaseRecyclerAdapter<CreditPhoneListEntity.ListBean> {

    private IDelPhoneListener mDelPhoneListener;

    public TopUpHistoryAdapter(Context context, List<CreditPhoneListEntity.ListBean> lists) {
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
        View view = mInflater.inflate(R.layout.item_top_up, parent, false);
        return new TopUpHistoryHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopUpHistoryHolder){
            TopUpHistoryHolder mHolder = (TopUpHistoryHolder) holder;
            CreditPhoneListEntity.ListBean listBean = lists.get(position);
            mHolder.mtv_phone.setText(listBean.number);
        }
    }



    public class TopUpHistoryHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mtv_phone)
        MyTextView mtv_phone;

        @BindView(R.id.miv_clear)
        MyImageView miv_clear;

        public TopUpHistoryHolder(View itemView) {
            super(itemView);
            miv_clear.setOnClickListener(v -> {
                if (mDelPhoneListener != null){
                    CreditPhoneListEntity.ListBean listBean = lists.get(getAdapterPosition());
                    mDelPhoneListener.onDelPhone(listBean.id,getAdapterPosition());
                }
            });

            mtv_phone.setOnClickListener(v -> {
                if (listener != null){
                    listener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }

    public void setDelPhoneListener(IDelPhoneListener delPhoneListener){
        mDelPhoneListener = delPhoneListener;
    }

    public interface IDelPhoneListener{
        void onDelPhone(String id,int position);
    }
}
