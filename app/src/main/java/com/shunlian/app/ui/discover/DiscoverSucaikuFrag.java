package com.shunlian.app.ui.discover;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DiscoverSucaikuAdapter;
import com.shunlian.app.bean.DiscoveryMaterialEntity;
import com.shunlian.app.presenter.PDiscoverSucaiku;
import com.shunlian.app.view.IDiscoverSucaiku;

import butterknife.BindView;


public class DiscoverSucaikuFrag extends DiscoversFrag implements IDiscoverSucaiku{

    @BindView(R.id.rv_sucaiku)
    RecyclerView rv_sucaiku;

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
    public void setApiData(DiscoveryMaterialEntity data) {
        if (discoverSucaikuAdapter == null) {
            discoverSucaikuAdapter = new DiscoverSucaikuAdapter(getContext(), true, data.list);
            linearLayoutManager = new LinearLayoutManager(getContext());
            rv_sucaiku.setLayoutManager(linearLayoutManager);
            rv_sucaiku.setAdapter(discoverSucaikuAdapter);
        } else {
            discoverSucaikuAdapter.notifyDataSetChanged();
        }
        discoverSucaikuAdapter.setPageLoading(Integer.parseInt(data.page),Integer.parseInt( data.total_page));
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
