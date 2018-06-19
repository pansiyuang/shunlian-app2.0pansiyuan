package com.shunlian.app.ui.discover;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DiscoverSucaikuAdapter;
import com.shunlian.app.bean.DiscoveryMaterialEntity;
import com.shunlian.app.presenter.PDiscoverSucaiku;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IDiscoverSucaiku;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.app.widget.nestedrefresh.interf.onRefreshListener;

import java.util.List;

import butterknife.BindView;


public class DiscoverSucaikuFrag extends DiscoversFrag implements IDiscoverSucaiku{

    @BindView(R.id.rv_sucaiku)
    RecyclerView rv_sucaiku;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    private PDiscoverSucaiku pDiscoverSucaiku;
    private LinearLayoutManager linearLayoutManager;
    private DiscoverSucaikuAdapter discoverSucaikuAdapter;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_sucaiku,null,false);
    }

    @Override
    protected void initData() {
        pDiscoverSucaiku=new PDiscoverSucaiku(getContext(),this);
        NestedSlHeader header = new NestedSlHeader(getContext());
        lay_refresh.setRefreshHeaderView(header);
        nei_empty.setImageResource(R.mipmap.img_empty_faxian).setText(getString(R.string.discover_weifaxianxin));
        nei_empty.setButtonText(null);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rv_sucaiku.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (pDiscoverSucaiku != null) {
                            pDiscoverSucaiku.refreshBaby();
                        }
                    }
                }
            }
        });
        lay_refresh.setOnRefreshListener(new onRefreshListener() {
            @Override
            public void onRefresh() {
                pDiscoverSucaiku.resetBaby();
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
    public void setApiData(DiscoveryMaterialEntity data, List<DiscoveryMaterialEntity.Content> datas) {
        lay_refresh.setRefreshing(false);
        if (isEmpty(datas)){
            visible(nestedScrollView);
            gone(rv_sucaiku);
        }else {
            visible(rv_sucaiku);
            gone(nestedScrollView);
            if (discoverSucaikuAdapter == null) {
                discoverSucaikuAdapter = new DiscoverSucaikuAdapter(getContext(), true, datas);
                linearLayoutManager = new LinearLayoutManager(getContext());
                rv_sucaiku.setLayoutManager(linearLayoutManager);
                rv_sucaiku.setAdapter(discoverSucaikuAdapter);
            } else {
                discoverSucaikuAdapter.notifyDataSetChanged();
            }
            discoverSucaikuAdapter.setPageLoading(Integer.parseInt(data.page),Integer.parseInt( data.total_page));
        }
    }

    @Override
    public void showFailureView(int request_code) {
        lay_refresh.setRefreshing(false);
    }

    @Override
    public void showDataEmptyView(int request_code) {
        visible(nestedScrollView);
        gone(rv_sucaiku);
    }
}
