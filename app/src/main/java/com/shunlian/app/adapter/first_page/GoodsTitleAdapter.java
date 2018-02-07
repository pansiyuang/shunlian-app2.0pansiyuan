package com.shunlian.app.adapter.first_page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.shunlian.app.R;

/**
 * Created by Administrator on 2018/2/1.
 */

public class GoodsTitleAdapter extends DelegateAdapter.Adapter  {

    private Context mContext;
    private LayoutHelper mHelper;

    public GoodsTitleAdapter(Context context, LayoutHelper helper){
        mContext = context;
        mHelper = helper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.choie_goods_layout, parent, false);
        return new BrandTitleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class BrandTitleHolder extends RecyclerView.ViewHolder{

        public BrandTitleHolder(View itemView) {
            super(itemView);
        }
    }
}
