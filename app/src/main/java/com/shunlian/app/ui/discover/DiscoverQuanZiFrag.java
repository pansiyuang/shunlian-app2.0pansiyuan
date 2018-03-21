package com.shunlian.app.ui.discover;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.DiscoverNewAdapter;
import com.shunlian.app.bean.DiscoveryCircleEntity;
import com.shunlian.app.presenter.PDiscoverQuanzi;
import com.shunlian.app.ui.discover.quanzi.DiscoverTieziAct;
import com.shunlian.app.view.IDiscoverQuanzi;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.Kanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class DiscoverQuanZiFrag extends DiscoversFrag implements IDiscoverQuanzi {
    @BindView(R.id.rv_new)
    RecyclerView rv_new;
    @BindView(R.id.kanner)
    Kanner kanner;
    private PDiscoverQuanzi pDiscoverQuanzi;
    private LinearLayoutManager linearLayoutManager;
    private DiscoverNewAdapter newAdapter;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_quanzi, null, false);
    }

    @Override
    protected void initData() {
        pDiscoverQuanzi = new PDiscoverQuanzi(getContext(), this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rv_new.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (pDiscoverQuanzi != null) {
                            pDiscoverQuanzi.refreshBaby();
                        }
                    }
                }
            }
        });
    }

    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        return false;
    }

    @Override
    public void setApiData(final DiscoveryCircleEntity.Mdata data, final List<DiscoveryCircleEntity.Mdata.Content> mdatas) {
        if (newAdapter == null) {
            newAdapter = new DiscoverNewAdapter(getContext(), true, mdatas);
            linearLayoutManager = new LinearLayoutManager(getContext());
            rv_new.setLayoutManager(linearLayoutManager);
            rv_new.setNestedScrollingEnabled(false);
            rv_new.setAdapter(newAdapter);
            newAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    DiscoverTieziAct.startAct(getContext(),mdatas.get(position).id);
                }
            });
            if (data.banner != null) {
                List<String> list=new ArrayList<>();
                for (int i=0;i<data.banner.size();i++)
                {
                    list.add(data.banner.get(i).img);
                    if (i>=data.banner.size()-1){
                        kanner.setBanner(list);
                        kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });
                    }
                }
            }
        } else {
            newAdapter.notifyDataSetChanged();
        }
        newAdapter.setPageLoading(Integer.parseInt(data.page), Integer.parseInt(data.total_page));
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
