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

import java.util.List;

/**
 * Created by Administrator on 2018/2/1.
 */

public class BannerAdapter extends DelegateAdapter.Adapter {

    private List<MainPageEntity.Banner> mBanners;
    private Context mContext;
    private LayoutHelper mHelper;

    public BannerAdapter(List<MainPageEntity.Banner> banners,Context context,LayoutHelper helper){
        mBanners = banners;
        mContext = context;
        mHelper = helper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.first_page_banner,parent,false);
        return new BannerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class BannerHolder extends RecyclerView.ViewHolder{

        private final FirstPageBanner banner;

        public BannerHolder(View itemView) {
            super(itemView);
            banner = (FirstPageBanner) itemView;
            banner.setBanner(mBanners);
        }
    }
}
