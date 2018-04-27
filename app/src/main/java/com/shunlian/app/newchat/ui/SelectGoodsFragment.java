package com.shunlian.app.newchat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.newchat.adapter.ChatGoodsAdapter;
import com.shunlian.app.newchat.entity.ChatGoodsEntity;
import com.shunlian.app.presenter.ChatGoodsPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.myself_store.AddStoreGoodsAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IChatGoodsView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import static com.shunlian.app.newchat.ui.SelectGoodsActivity.mSelectGoods;

/**
 * Created by Administrator on 2018/4/12.
 */

public class SelectGoodsFragment extends BaseLazyFragment implements IChatGoodsView, BaseRecyclerAdapter.OnItemClickListener {

    private RecyclerView recy_view;
    private NetAndEmptyInterface emptyInterface;
    private LinearLayoutManager manager;
    private ChatGoodsPresenter mPresenter;
    private String mFrom;
    private String mStoreId;
    private List<ChatGoodsEntity.Goods> currentGoods;
    private ChatGoodsAdapter mAdapter;
    private SelectGoodsActivity mActivity;

    public static SelectGoodsFragment getInstance(String from, String storeId) {
        SelectGoodsFragment selectGoodsFragmet = new SelectGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putString("storeId", storeId);
        selectGoodsFragmet.setArguments(bundle);
        return selectGoodsFragmet;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_all_goods, container, false);
        recy_view = (RecyclerView) view.findViewById(R.id.recy_view);
        emptyInterface = (NetAndEmptyInterface) view.findViewById(R.id.nei_empty);
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
        return view;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onFragmentFirstVisible() {
        manager = new GridLayoutManager(baseActivity, 2);
        recy_view.setLayoutManager(manager);
        recy_view.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(baseContext, 12), true));

        currentGoods = new ArrayList<>();
        mFrom = getArguments().getString("from");
        mStoreId = getArguments().getString("storeId");

        mAdapter = new ChatGoodsAdapter(baseActivity, currentGoods);
        recy_view.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mPresenter = new ChatGoodsPresenter(baseActivity, this);
        mPresenter.getChatGoodsList(mFrom, mStoreId, true);
        super.onFragmentFirstVisible();
    }

    @Override
    public void getChatGoodsList(List<ChatGoodsEntity.Goods> goodsList, int currentPage, int total) {
        if (currentPage == 1) {
            currentGoods.clear();
        }
        if (!isEmpty(goodsList)) {
            currentGoods.addAll(goodsList);
        }
        if (isEmpty(goodsList)) {
            showEmptyView(true);
        } else {
            showEmptyView(false);
        }

        mAdapter.notifyItemChanged(0, currentGoods.size());
    }

    @Override
    public void refreshFinish() {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onItemClick(View view, int position) {
        ChatGoodsEntity.Goods goods = currentGoods.get(position);
        if (goods.isSelect) {
            goods.isSelect = false;
            if (isContains(goods.goods_id)) {
                mSelectGoods.remove(goods);
            }
        } else {
            if (isContains(goods.goods_id)) {
                Common.staticToast("不能重复添加");
            } else {
                if (mSelectGoods.size() >= 4) {
                    Common.staticToast("最多选择4个商品");
                    return;
                }
                goods.isSelect = true;
                mSelectGoods.add(goods);
            }
        }
        mActivity.updateSelectCount();
        mAdapter.notifyItemChanged(position);
    }

    public boolean isContains(String id) {
        for (ChatGoodsEntity.Goods goods : mSelectGoods) {
            if (id.equals(goods.goods_id)) {
                return true;
            }
        }
        return false;
    }


    public void showEmptyView(boolean isShowEmpty) {
        if (isShowEmpty) {
            emptyInterface.setImageResource(R.mipmap.img_xiaodian_kongyemian).setText("您还没有相关订单商品，赶紧去下单吧。");
            emptyInterface.setButtonText(getStringResouce(R.string.to_mainpage));
            emptyInterface.setVisibility(View.VISIBLE);
            emptyInterface.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.startAct(baseContext, "mainPage");
                }
            });
            recy_view.setVisibility(View.GONE);
        } else {
            emptyInterface.setVisibility(View.GONE);
            recy_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof SelectGoodsActivity) {
            mActivity = (SelectGoodsActivity) activity;
        }
        super.onAttach(activity);
    }
}
