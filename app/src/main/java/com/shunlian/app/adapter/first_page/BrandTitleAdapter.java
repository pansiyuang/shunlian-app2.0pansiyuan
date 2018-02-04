package com.shunlian.app.adapter.first_page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.shunlian.app.R;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/1.
 */

public class BrandTitleAdapter extends DelegateAdapter.Adapter  {

    private Context mContext;
    private LayoutHelper mHelper;
    private OnAnotherBatchListener mBatchListener;

    public BrandTitleAdapter(Context context, LayoutHelper helper){
        mContext = context;
        mHelper = helper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.first_page_brands, parent, false);
        return new BrandTitleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class BrandTitleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_batch)
        MyTextView mtv_batch;

        public BrandTitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            mtv_batch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mBatchListener != null){
                mBatchListener.onBatch();
            }
        }
    }

    public void setOnAnotherBatchListener(OnAnotherBatchListener batchListener){

        mBatchListener = batchListener;
    }

    public interface OnAnotherBatchListener{
        void onBatch();
    }
}
