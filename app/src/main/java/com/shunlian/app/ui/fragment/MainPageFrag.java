package com.shunlian.app.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.first_page.BannerAdapter;
import com.shunlian.app.adapter.first_page.BrandAdapter;
import com.shunlian.app.adapter.first_page.BrandTitleAdapter;
import com.shunlian.app.adapter.first_page.FirstPageGoodsAdapter;
import com.shunlian.app.adapter.first_page.GoodsTitleAdapter;
import com.shunlian.app.adapter.first_page.OnePlusTwoLayoutAdapter;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.MainPageEntity;
import com.shunlian.app.presenter.MainPagePresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.category.CategoryAct;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.ui.myself_store.AddStoreGoodsAct;
import com.shunlian.app.ui.myself_store.MyLittleStoreActivity;
import com.shunlian.app.ui.zxing_code.ZXingDemoAct;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IMainPageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 首页页面
 */

public class MainPageFrag extends BaseFragment implements IMainPageView, View.OnClickListener {
    @BindView(R.id.refreshview)
    SlRefreshView refreshview;
    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    @BindView(R.id.mll_message)
    MyLinearLayout mll_message;

    private List<DelegateAdapter.Adapter> adapterList;
    private DelegateAdapter adapter;
    private FirstPageGoodsAdapter firstPageGoodsAdapter;
    private List<MainPageEntity.Data> goodsLists = new ArrayList<>();
    private MainPagePresenter pagePresenter;
    private VirtualLayoutManager manager;
    private int itemCount;
    private boolean isEmptyBanner = true;
    private BrandAdapter brandAdapter;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.frag_main, container, false);
        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ImmersionBar.with(this).fitsSystemWindows(true)
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true, 0.2f)
                    .init();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        pagePresenter.onRefresh();
                    }
                }
            }
        });
        mll_message.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        //新增下拉刷新
        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);
        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                pagePresenter.refreshData();
            }

            @Override
            public void onLoadMore() {

            }
        });

        //end

        pagePresenter = new MainPagePresenter(baseActivity, this);

        manager = new VirtualLayoutManager(baseActivity);

        adapter = new DelegateAdapter(manager);

        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();

        recy_view.setRecycledViewPool(pool);

        pool.setMaxRecycledViews(0, 20);

        recy_view.setLayoutManager(manager);

        recy_view.setAdapter(adapter);

        adapterList = new LinkedList<>();


    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    @OnClick(R.id.mllayout_scan)
    public void scan() {
        ZXingDemoAct.startAct(baseActivity, false, 0);
    }

    @OnClick(R.id.mllayout_search)
    public void search() {
        SearchGoodsActivity.startAct(baseActivity, "", "sortFrag");
    }

    /**
     * 首页轮播
     *
     * @param banners
     */
    @Override
    public void banner(List<MainPageEntity.Banner> banners) {
        refreshview.stopRefresh(true);
        if (!isEmpty(banners)) {
            isEmptyBanner = false;
            SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
            adapterList.add(new BannerAdapter(banners, baseActivity, singleLayoutHelper));
        } else {
            isEmptyBanner = true;
        }
    }

    /**
     * 天天特惠和新品
     *
     * @param daySpecial
     * @param newGoods
     */
    @Override
    public void daySpecialAndNewGoods(MainPageEntity.DaySpecial daySpecial,
                                      MainPageEntity.NewGoods newGoods) {

        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();

        OnePlusTwoLayoutAdapter onePlusTwoLayoutAdapter = new OnePlusTwoLayoutAdapter
                (newGoods, daySpecial, baseActivity, singleLayoutHelper);
        adapterList.add(onePlusTwoLayoutAdapter);
    }

    /**
     * 更多品牌
     *
     * @param brands
     */
    @Override
    public void moreBrands(final MainPageEntity.RecommendBrands brands) {
        if (!isEmpty(brands.data)) {
            itemCount = brands.data.size();
            if (brandAdapter == null) {
                SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
                BrandTitleAdapter brandTitleAdapter = new BrandTitleAdapter(baseActivity, singleLayoutHelper);
                adapterList.add(brandTitleAdapter);
                brandTitleAdapter.setOnAnotherBatchListener(new BrandTitleAdapter.OnAnotherBatchListener() {
                    @Override
                    public void onBatch() {
                        if (pagePresenter != null) {
                            pagePresenter.onBatch();
                        }
                    }
                });

                GridLayoutHelper helper = new GridLayoutHelper(4);
                int i = TransformUtil.dip2px(baseActivity, 0.5f);
                helper.setBgColor(getColorResouce(R.color.light_gray_three));
                helper.setVGap(i);
                helper.setHGap(i);
                helper.setPaddingTop(i);
                helper.setPaddingBottom(i);
                brandAdapter = new BrandAdapter(brands.data, baseActivity, helper);
                adapterList.add(brandAdapter);

                brandAdapter.setOnItemClickListener(new BrandAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        int i = 0;
                        if (!isEmptyBanner) {
                            i = position - 3;
                        } else {
                            i = position - 2;
                        }
                        MainPageEntity.Data data = brands.data.get(i);
                        GoodsSearchParam param = new GoodsSearchParam();
                        param.brand_ids = data.item_id;
                        CategoryAct.startAct(baseActivity, param);
                    }
                });
            } else {
                brandAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 精选商品
     *
     * @param goods
     */
    @Override
    public void moreGoods(MainPageEntity.RecommendBrands goods) {
        if (!isEmpty(goods.data)) {
            goodsLists.addAll(goods.data);
        }
        if (firstPageGoodsAdapter == null && !isEmpty(goods.data)) {
            SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
            adapterList.add(new GoodsTitleAdapter(baseActivity, singleLayoutHelper));

            GridLayoutHelper helper = new GridLayoutHelper(2);
            int i = TransformUtil.dip2px(baseActivity, 5);
            helper.setHGap(i);
            helper.setVGap(i);
            helper.setBgColor(getColorResouce(R.color.white_ash));
            firstPageGoodsAdapter = new FirstPageGoodsAdapter(goodsLists, baseActivity, helper);
            adapterList.add(firstPageGoodsAdapter);

            adapter.setAdapters(adapterList);

            firstPageGoodsAdapter.setOnItemClickListener(new FirstPageGoodsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    int i = 0;
                    if (!isEmptyBanner) {
                        i = position - itemCount - 4;
                    } else {
                        i = position - itemCount - 3;
                    }
                    MainPageEntity.Data data = goodsLists.get(i);
                    GoodsDetailAct.startAct(baseContext, data.item_id);
                }
            });
        } else {
            firstPageGoodsAdapter.notifyItemInserted(10);
        }
    }

    @Override
    public void onClick(View v) {
        MyLittleStoreActivity.startAct(getActivity());
    }
}
