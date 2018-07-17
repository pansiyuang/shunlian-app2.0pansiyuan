package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.PhoneRecordEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/1.
 */

public class PhoneDetailAdapter extends BaseRecyclerAdapter<String> {


    public PhoneDetailAdapter(Context context, List<String> lists) {
        super(context, false, lists);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_common_tv, parent, false);
        return  new SingleViewHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        SingleViewHolder viewHolder = (SingleViewHolder) holder;
        viewHolder.mtv_name.setText(lists.get(position));
    }


    public class SingleViewHolder extends BaseRecyclerViewHolders implements View.OnClickListener {
        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        public SingleViewHolder(View itemView) {
            super(itemView);
        }

    }
}
