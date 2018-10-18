package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.NearAddrEntity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/10/18.
 */

public class NearAddrAdapter extends BaseRecyclerAdapter<NearAddrEntity.NearAddr> {

    public NearAddrAdapter(Context context,List<NearAddrEntity.NearAddr> lists) {
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
        View view = mInflater.inflate(R.layout.item_near_addr, parent, false);
        return new NearAddrHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        NearAddrHolder mHolder = (NearAddrHolder) holder;
        NearAddrEntity.NearAddr nearAddr = lists.get(position);
        mHolder.mtv_name.setText(nearAddr.name);
        mHolder.mtv_addr.setText(nearAddr.addr);
    }

    public class NearAddrHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.mtv_addr)
        MyTextView mtv_addr;

        @BindView(R.id.miv_dot)
        MyImageView miv_dot;

        public NearAddrHolder(View itemView) {
            super(itemView);
        }
    }
}
