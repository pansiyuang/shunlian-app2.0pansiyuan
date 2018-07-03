package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.SaleDataEntity;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/3.
 */

public class MemberAdapter extends BaseRecyclerAdapter<SaleDataEntity.NewMemberInfo> {

    public MemberAdapter(Context context, List<SaleDataEntity.NewMemberInfo> lists) {
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
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_member, parent, false);
        return new MemberHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MemberHolder){
            MemberHolder mHolder = (MemberHolder) holder;
            SaleDataEntity.NewMemberInfo newMemberInfo = lists.get(position);
            mHolder.mtv_title.setText(newMemberInfo.label);
            mHolder.mtv_data.setText(newMemberInfo.value);
        }
    }

    public class MemberHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_data)
        MyTextView mtv_data;
        public MemberHolder(View itemView) {
            super(itemView);
        }
    }
}
