package com.shunlian.app.ui.discover;

import android.support.v4.widget.NestedScrollView;
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
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IDiscoverQuanzi;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.MyKanner;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.app.widget.nestedrefresh.interf.onRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class DiscoverQuanZiFrag extends DiscoversFrag implements IDiscoverQuanzi {
    @BindView(R.id.rv_new)
    RecyclerView rv_new;
    @BindView(R.id.kanner)
    MyKanner kanner;

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_remen)
    MyTextView mtv_remen;

    @BindView(R.id.mrlayout_remen)
    MyRelativeLayout mrlayout_remen;

    @BindView(R.id.mtv_zuixin)
    MyTextView mtv_zuixin;


    @BindView(R.id.nsv_top)
    NestedScrollView nsv_top;

    @BindView(R.id.nsv_bootom)
    NestedScrollView nsv_bootom;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    private PDiscoverQuanzi pDiscoverQuanzi;
    private LinearLayoutManager linearLayoutManager;
    private DiscoverNewAdapter newAdapter;
    private boolean isRefresh = false;


    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_quanzi, null, false);
    }

    @Override
    protected void initData() {
        pDiscoverQuanzi = new PDiscoverQuanzi(baseContext, this);
        NestedSlHeader header = new NestedSlHeader(baseContext);
        lay_refresh.setRefreshHeaderView(header);

        nei_empty.setImageResource(R.mipmap.img_empty_faxian).setText(getString(R.string.discover_weifaxianxin));
        nei_empty.setButtonText(null);
    }

    @Override
    protected void initListener() {
        super.initListener();
        nsv_top.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            View view = nsv_top.getChildAt(nsv_top.getChildCount() - 1);
            int d = view.getBottom();
            d -= (nsv_top.getHeight() + nsv_top.getScrollY());
            if (d == 0 && pDiscoverQuanzi != null) {
                    pDiscoverQuanzi.refreshBaby();
            }
        });
//        rv_new.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (linearLayoutManager != null) {
//                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
//                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
//                        if (pDiscoverQuanzi != null) {
//                            pDiscoverQuanzi.refreshBaby();
//                        }
//                    }
//                }
//            }
//        });
        lay_refresh.setOnRefreshListener(new onRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh=false;
                pDiscoverQuanzi.resetBaby();
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
        lay_refresh.setRefreshing(false);
        if (!isRefresh) {
            if (isEmpty(data.banner) && isEmpty(mdatas)) {
                visible(nsv_bootom);
                gone(nsv_top);
            } else {
                visible(nsv_top);
                gone(nsv_bootom);
                if (!isEmpty(data.banner)) {
                    visible(mtv_remen, mrlayout_remen);
                    mtv_title.setText(data.banner.get(0).title);
                    List<String> strings = new ArrayList<>();
                    for (int i = 0; i < data.banner.size(); i++) {
                        strings.add(data.banner.get(i).img);
                        if (i >= data.banner.size() - 1) {
                            kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                            kanner.setBanner(strings);
                            kanner.onPageChangeCall(new BaseBanner.onPageChanged() {
                                @Override
                                public void onPageChange(int position) {
                                    mtv_title.setText(data.banner.get(position).title);
                                }
                            });
                            kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                                @Override
                                public void onItemClick(int position) {
                                    DiscoverTieziAct.startAct(baseContext, data.banner.get(position).id);
                                }
                            });
                        }
                    }
                } else {
                    gone(mtv_remen, mrlayout_remen);
                }
                if (isEmpty(mdatas)) {
                    gone(mtv_zuixin, rv_new);
                } else {
                    visible(mtv_zuixin, rv_new);
                    if (newAdapter == null) {
                        newAdapter = new DiscoverNewAdapter(baseContext, true, mdatas);
                        linearLayoutManager = new LinearLayoutManager(baseContext);
                        rv_new.setLayoutManager(linearLayoutManager);
                        rv_new.setNestedScrollingEnabled(false);
                        rv_new.setAdapter(newAdapter);
                        newAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                DiscoverTieziAct.startAct(baseContext, mdatas.get(position).id);
                            }
                        });
                    } else {
                        newAdapter.notifyDataSetChanged();
                    }
                    newAdapter.setPageLoading(Integer.parseInt(data.page), Integer.parseInt(data.total_page));
                }
            }
        }else {
            newAdapter.notifyDataSetChanged();
            newAdapter.setPageLoading(Integer.parseInt(data.page), Integer.parseInt(data.total_page));
        }
        isRefresh=true;
    }

    @Override
    public void showFailureView(int request_code) {
        visible(nsv_bootom);
        gone(nsv_top);
        lay_refresh.setRefreshing(false);
    }

    @Override
    public void showDataEmptyView(int request_code) {
        visible(nsv_bootom);
        gone(nsv_top);
    }
}
