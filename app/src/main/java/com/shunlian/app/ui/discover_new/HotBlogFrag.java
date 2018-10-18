package com.shunlian.app.ui.discover_new;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AddGoodsAdapter;
import com.shunlian.app.adapter.HotBlogAdapter;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.presenter.GoodsListPresenter;
import com.shunlian.app.presenter.HotBlogPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.ui.myself_store.AddStoreGoodsAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IHotBlogView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.MyKanner;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.app.widget.nestedrefresh.interf.onRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/15.
 */

public class HotBlogFrag extends BaseLazyFragment implements IHotBlogView {

    @BindView(R.id.myKanner)
    MyKanner myKanner;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private HotBlogPresenter hotBlogPresenter;
    private HotBlogAdapter hotBlogAdapter;
    private List<HotBlogsEntity.Blog> blogList;
    private List<String> banners;
    private LinearLayoutManager manager;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.hotblog_frag, null, false);
        return view;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        NestedSlHeader header = new NestedSlHeader(getContext());
        lay_refresh.setRefreshHeaderView(header);
        recycler_list.setNestedScrollingEnabled(false);

        blogList = new ArrayList<>();
        hotBlogPresenter = new HotBlogPresenter(getActivity(), this);
        hotBlogPresenter.getHotBlogList(true);

        banners = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);

        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText("暂时没有用户发布精选文章")
                .setButtonText(null);
    }

    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            hotBlogPresenter.initPage();
            hotBlogPresenter.getHotBlogList(true);
        });
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (hotBlogPresenter != null) {
                            hotBlogPresenter.onRefresh();
                        }
                    }
                }
            }
        });
        super.initListener();
    }

    public void initBanner(List<HotBlogsEntity.Ad> ad_list) {
        banners.clear();
        for (HotBlogsEntity.Ad ad : ad_list) {
            banners.add(ad.ad_img);
        }
        myKanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
        myKanner.setBanner(banners);
        myKanner.setOnItemClickL(position -> {
//                    Common.goGoGo(baseAct, coreHotEntity.banner_list.get(position).type, coreHotEntity.banner_list.get(position).item_id);
        });
    }

    @Override
    public void getBlogList(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage) {
        if (currentPage == 1) {
            blogList.clear();

            if (!isEmpty(hotBlogsEntity.ad_list)) {
                initBanner(hotBlogsEntity.ad_list);
            }

            if (isEmpty(hotBlogsEntity.list)) {
                nei_empty.setVisibility(View.VISIBLE);
                recycler_list.setVisibility(View.GONE);
            } else {
                nei_empty.setVisibility(View.GONE);
                recycler_list.setVisibility(View.VISIBLE);
            }
        }
        if (!isEmpty(hotBlogsEntity.list)) {
            blogList.addAll(hotBlogsEntity.list);
        }
        if (hotBlogAdapter == null) {
            hotBlogAdapter = new HotBlogAdapter(getActivity(), blogList, getActivity());
            recycler_list.setAdapter(hotBlogAdapter);
        }
        hotBlogAdapter.setPageLoading(currentPage, totalPage);
        hotBlogAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新完成
     */
    @Override
    public void refreshFinish() {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
