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
import com.shunlian.app.widget.MyLinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/1.
 */

public class BrandAdapter extends DelegateAdapter.Adapter {

    private final LayoutHelper mHelper;
    private List<MainPageEntity.Data> mDatas;
    private final Context mContext;
    private OnItemClickListener mListener;

    public BrandAdapter(List<MainPageEntity.Data> datas,Context context, LayoutHelper helper){
        mDatas = datas;
        mContext = context;
        mHelper = helper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_brand, parent, false);
        return new BrandHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BrandHolder mHolder = (BrandHolder) holder;
        MainPageEntity.Data data = mDatas.get(position);
        GlideUtils.getInstance().loadImage(mContext,mHolder.imageView,data.pic);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class BrandHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        private final MyImageView imageView;

        @BindView(R.id.iv_brand)
        MyImageView imageView;

        @BindView(R.id.brand_item)
        MyLinearLayout brand_item;
        public BrandHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            brand_item.setWHProportion(180,180);
//            imageView = (MyImageView) itemView;
//            imageView.setWHProportion(180,180);
//            imageView.setScaleType(ImageView.ScaleType.CENTER);
//            imageView.setBackgroundColor(Color.WHITE);
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
