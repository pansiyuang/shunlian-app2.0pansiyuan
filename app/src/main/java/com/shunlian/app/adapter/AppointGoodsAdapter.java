package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class AppointGoodsAdapter extends BaseRecyclerAdapter<String> {

    public AppointGoodsAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View item_appoint_goods = LayoutInflater.from(context).inflate(R.layout.item_appoint_goods, parent, false);
        return new AppointGoodsHolder(item_appoint_goods);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {

    }

    public class AppointGoodsHolder extends BaseRecyclerViewHolder{

        public AppointGoodsHolder(View itemView) {
            super(itemView);
        }
    }
}
