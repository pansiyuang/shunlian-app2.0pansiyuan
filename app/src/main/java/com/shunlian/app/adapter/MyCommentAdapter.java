package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyCommentAdapter extends BaseRecyclerAdapter<String> {

    public MyCommentAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_comment, parent, false);
        return new MyCommentHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {

    }

    public class MyCommentHolder extends BaseRecyclerViewHolder{

        public MyCommentHolder(View itemView) {
            super(itemView);
        }
    }
}
