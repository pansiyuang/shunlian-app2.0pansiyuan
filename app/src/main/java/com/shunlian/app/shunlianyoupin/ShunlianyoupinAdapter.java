package com.shunlian.app.shunlianyoupin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/4/4.
 */

public class ShunlianyoupinAdapter extends BaseRecyclerAdapter<YouPingListEntity.lists> {
    public ShunlianyoupinAdapter(Context context, boolean isShowFooter, List<YouPingListEntity.lists> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_you_pin, parent, false);
        YouPinlistHolder youPinglistHolder = new YouPinlistHolder(inflate);
        return youPinglistHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof YouPinlistHolder){
            YouPinlistHolder youpinholder = (YouPinlistHolder) holder;
            YouPingListEntity.lists data = lists.get(position);
            GlideUtils.getInstance().loadImage(context,youpinholder.miv_list,data.thumb);
            youpinholder.mtv_list1.setText(data.title);
            youpinholder.mtv_list2.setText(data.characteristic);
            youpinholder.mtv_list3.setText(data.price);
        }

    }
   class YouPinlistHolder extends BaseRecyclerViewHolder implements View.OnClickListener{
        @BindView(R.id.mll_root2)
        LinearLayout mll_root2;

        @BindView(R.id.miv_list)
        ImageView miv_list;

        @BindView(R.id.mtv_list1)
        TextView mtv_list1;

        @BindView(R.id.mtv_list2)
        TextView mtv_list2;

        @BindView(R.id.mtv_list3)
        TextView mtv_list3;

        public YouPinlistHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
