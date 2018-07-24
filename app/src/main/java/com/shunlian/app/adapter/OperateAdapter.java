package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/1.
 */

public class OperateAdapter extends BaseRecyclerAdapter<String> {


    public OperateAdapter(Context context, List<String> lists) {
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
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TransformUtil.dip2px(context,56));
            mtv_name.setLayoutParams(params);
            mtv_name.setGravity(Gravity.CENTER_VERTICAL);
            mtv_name.setPadding(TransformUtil.dip2px(context,16),0,TransformUtil.dip2px(context,16),0);
            mtv_name.setTextColor(getColor(R.color.new_text));
            mtv_name.setTextSize(16);
        }

    }
}
