package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/10/15.
 */

public class AttentionAdapter extends BaseRecyclerAdapter {

    public AttentionAdapter(Context context, List lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {

    }
}
