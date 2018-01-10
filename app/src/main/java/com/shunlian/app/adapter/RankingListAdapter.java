package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.RankingListEntity;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9.
 */

public class RankingListAdapter extends BaseRecyclerAdapter<RankingListEntity.Category> {

    public int currentPosition;

    public RankingListAdapter(Context context,List<RankingListEntity.Category> lists) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.layout_ranking_title, parent, false);
        return new RankingListHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RankingListHolder){
            RankingListHolder mHolder = (RankingListHolder) holder;
            RankingListEntity.Category category = lists.get(position);
            mHolder.textView.setText(category.name);
            if (position == currentPosition){
                mHolder.textView.setTextColor(getColor(R.color.pink_color));
            }else {
                mHolder.textView.setTextColor(getColor(R.color.new_text));
            }
        }
    }

    public class RankingListHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        private final MyTextView textView;

        public RankingListHolder(View itemView) {
            super(itemView);
            textView = (MyTextView) itemView;
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }


}
