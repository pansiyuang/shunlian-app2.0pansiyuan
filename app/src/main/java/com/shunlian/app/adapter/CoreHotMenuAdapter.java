package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class CoreHotMenuAdapter extends BaseRecyclerAdapter<CoreHotEntity.MData> {
    public int selectedPosition = 0;

    public CoreHotMenuAdapter(Context context, boolean isShowFooter,
                              List<CoreHotEntity.MData> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_first_category_menu, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        CoreHotEntity.MData data = lists.get(position);
        mHolder.mrlayout_category.setVisibility(View.VISIBLE);
        mHolder.mtv_category.setText(data.cate_name);
        if (position == selectedPosition) {
            mHolder.mtv_category.setTextColor(getColor(R.color.pink_color));
            mHolder.view_line.setVisibility(View.VISIBLE);
        } else {
            mHolder.view_line.setVisibility(View.INVISIBLE);
            mHolder.mtv_category.setTextColor(getColor(R.color.new_text));
        }
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_titles)
        MyTextView mtv_titles;

        @BindView(R.id.mrlayout_category)
        MyRelativeLayout mrlayout_category;

        @BindView(R.id.mtv_category)
        MyTextView mtv_category;

        @BindView(R.id.view_line)
        View view_line;


        public ActivityMoreHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
