package com.shunlian.app.adapter.first_page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.shunlian.app.R;
import com.shunlian.app.bean.MainPageEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/1.
 */

public class FirstPageGoodsAdapter extends DelegateAdapter.Adapter {

    private List<MainPageEntity.Data> mDataLists;
    private Context mContext;
    private LayoutHelper mLayoutHelper;
    private OnItemClickListener mListener;

    public FirstPageGoodsAdapter(List<MainPageEntity.Data> dataLists, Context context, LayoutHelper layoutHelper){
        mDataLists = dataLists;
        mContext = context;
        mLayoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods, parent, false);
        return new FirstPageGoodsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FirstPageGoodsHolder mHolder = (FirstPageGoodsHolder) holder;
        MainPageEntity.Data data = mDataLists.get(position);
        GlideUtils.getInstance().loadImage(mContext,mHolder.miv_pic,data.pic);
        mHolder.mtv_title.setText(data.title);
        String rmb = mContext.getResources().getString(R.string.rmb);
        mHolder.mtv_price.setText(rmb.concat(data.price));
        mHolder.mtv_market_price.setStrikethrough().setText(rmb.concat(data.market_price));
    }

    @Override
    public int getItemCount() {
        return mDataLists.size();
    }

    public class FirstPageGoodsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_market_price)
        MyTextView mtv_market_price;

        public FirstPageGoodsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                mListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        mListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v,int position);
    }
}
