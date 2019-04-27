package com.shunlian.app.yjfk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/4/19.
 */

public class ComplaninRecordActivityAdapter extends BaseRecyclerAdapter<ComplanintListEntity.Lists> {


    public ComplaninRecordActivityAdapter(Context context, boolean isShowFooter, List lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View inflate = mInflater.inflate(R.layout.item_complanin, parent,false);

        return new ComplaninHolder(inflate);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ComplaninHolder holder1 = (ComplaninHolder) holder;
        holder1.mtv1.setText(lists.get(position).cate_name);
        holder1.mtv3.setText(lists.get(position).create_time);
        holder1.mtv2.setText(lists.get(position).is_success==true?"投诉成功":"投诉失败");
    }

    public class ComplaninHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.mtv1)
        TextView mtv1;
        @BindView(R.id.mtv2)
        TextView mtv2;
        @BindView(R.id.mtv3)
        TextView mtv3;

        public ComplaninHolder(View itemView) {
            super(itemView);
        }
    }
}
