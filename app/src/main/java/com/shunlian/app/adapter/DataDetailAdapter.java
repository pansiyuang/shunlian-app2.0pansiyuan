package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.SaleDetailEntity;
import com.shunlian.app.ui.sale_data.SaleDetailAct;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/14.
 */

public class DataDetailAdapter extends BaseRecyclerAdapter<SaleDetailEntity.Item> {


    private int mType;

    public DataDetailAdapter(Context context, List<SaleDetailEntity.Item> lists, int type) {
        super(context, false, lists);
        mType = type;
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_data_detail, parent, false);
        return new DataDetailHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        DataDetailHolder mHolder = (DataDetailHolder) holder;
        SaleDetailEntity.Item item = lists.get(position);
        if (mType == SaleDetailAct.SALE_DETAIL) {
            mHolder.mtv_data1.setText(item.sale_money);
            mHolder.mtv_data2.setText(item.sale_type_desc);
            mHolder.mtv_data3.setText(item.sale_time);
            mHolder.mtv_data4.setText(item.estimate_profit);
        }else {
            mHolder.mtv_data1.setText(item.type_desc);
            mHolder.mtv_data2.setText(item.grant_time);
            mHolder.mtv_data3.setText(item.money);
        }
    }


    public class DataDetailHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mtv_data1)
        MyTextView mtv_data1;

        @BindView(R.id.mtv_data2)
        MyTextView mtv_data2;

        @BindView(R.id.mtv_data3)
        MyTextView mtv_data3;

        @BindView(R.id.mtv_data4)
        MyTextView mtv_data4;

        @BindView(R.id.view_line)
        View view_line;

        public DataDetailHolder(View itemView) {
            super(itemView);
            if (mType == SaleDetailAct.REWARD_DETAIL){
                gone(view_line,mtv_data4);
            }
        }
    }
}
