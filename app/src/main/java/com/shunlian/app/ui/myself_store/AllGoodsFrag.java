package com.shunlian.app.ui.myself_store;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AddGoodsAdapter;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.GoodsListPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IGoodsListView;

import java.util.ArrayList;
import java.util.List;

import static com.shunlian.app.ui.myself_store.AddStoreGoodsAct.currentGoodsList;


/**
 * Created by Administrator on 2018/2/27.
 */

public class AllGoodsFrag extends BaseLazyFragment implements IGoodsListView, BaseRecyclerAdapter.OnItemClickListener {

    RecyclerView recy_view;

    private GridLayoutManager manager;
    private String currentFrom;
    private GoodsListPresenter mPresenter;
    private AddGoodsAdapter mAdapter;
    private List<GoodsDeatilEntity.Goods> goodsList;
    private View rootView;

    public static AllGoodsFrag getInstance(String from) {
        AllGoodsFrag allFrag = new AllGoodsFrag();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        allFrag.setArguments(bundle);
        return allFrag;
    }


    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_all_goods, container, false);
        recy_view = (RecyclerView) rootView.findViewById(R.id.recy_view);
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
        ((DefaultItemAnimator) recy_view.getItemAnimator()).setSupportsChangeAnimations(false);
        return rootView;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    protected void onFragmentFirstVisible() {
        manager = new GridLayoutManager(baseActivity, 2);
        recy_view.setLayoutManager(manager);
        recy_view.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(baseContext, 12), true));

        goodsList = new ArrayList<>();
        mAdapter = new AddGoodsAdapter(baseActivity, true, true, goodsList);
        recy_view.setAdapter(mAdapter);
        currentFrom = getArguments().getString("from");
        mPresenter = new GoodsListPresenter(baseActivity, this);
        mPresenter.getValidGoods(currentFrom, true);

        mAdapter.setOnItemClickListener(this);
        super.onFragmentFirstVisible();
    }

    @Override
    public void getValidGoods(List<GoodsDeatilEntity.Goods> goods, int currentPage, int totalPage) {
        if (currentPage == 1) {
            goodsList.clear();
        }
        goodsList.addAll(goods);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        int currentIndex = 0;
        String currentGoodsId;
        GoodsDeatilEntity.Goods goods = goodsList.get(position);
        currentGoodsId = goods.goods_id;
        if (goods.isSelect) {
            for (int i = 0; i < currentGoodsList.size(); i++) {
                if (currentGoodsId.equals(currentGoodsList.get(i).goods_id)) {
                    currentIndex = goods.index;
                    currentGoodsList.remove(i);
                    break;
                }
            }
            for (int i = 0; i < goodsList.size(); i++) {
                if (goodsList.get(i).index > currentIndex) {
                    goodsList.get(i).index = goodsList.get(i).index - 1;
                    mAdapter.notifyItemChanged(i);
                }
            }
            goods.isSelect = false;
            goods.index = -1;
            mAdapter.notifyItemChanged(position);
        } else {
            if (!isContain(currentGoodsId)) {
                goods.isSelect = true;
                goods.index = currentGoodsList.size() + 1;
                currentGoodsList.add(goods);
                mAdapter.notifyItemChanged(position);
            }
        }
    }

    public boolean isContain(String id) {
        for (GoodsDeatilEntity.Goods goods : currentGoodsList) {
            if (goods.goods_id.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
