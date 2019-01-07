package com.shunlian.app.ui.new_user;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.CollectionGoodsAdapter;
import com.shunlian.app.adapter.HotBlogAdapter;
import com.shunlian.app.adapter.NewUserGoodsAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.CollectionStoresEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.RefreshBlogEvent;
import com.shunlian.app.eventbus_bean.UserPaySuccessEvent;
import com.shunlian.app.presenter.CollectionGoodsPresenter;
import com.shunlian.app.presenter.CommonBlogPresenter;
import com.shunlian.app.presenter.NewUserGoodsPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.order.MyOrderAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.view.ICollectionGoodsView;
import com.shunlian.app.view.ICollectionStoresView;
import com.shunlian.app.view.ICommonBlogView;
import com.shunlian.app.view.INewUserGoodsView;
import com.shunlian.app.widget.ParamDialog;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/22.
 */

public class NewUserGoodsFrag extends BaseLazyFragment implements INewUserGoodsView,NewUserGoodsAdapter.IAddShoppingCarListener {
    private ShareInfoParam shareInfoParam;
    private ShareGoodDialogUtil shareGoodDialogUtil;
    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    private NewUserGoodsPresenter mPresenter;
    private String currentFrom;
    private NewUserGoodsAdapter hotBlogAdapter;
    private List<NewUserGoodsEntity.Goods> goodList;
    private LinearLayoutManager manager;

    private String type;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_new_user, null, false);

        return view;
    }

    public static NewUserGoodsFrag getInstance(String comFrom,String type,boolean isNew) {
        NewUserGoodsFrag commonBlogFrag = new NewUserGoodsFrag();
        Bundle bundle = new Bundle();
        bundle.putString("from", comFrom);
        bundle.putString("type", type);
        bundle.putBoolean("isNew", isNew);
        commonBlogFrag.setArguments(bundle);
        return commonBlogFrag;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        EventBus.getDefault().register(this);
        NestedSlHeader header = new NestedSlHeader(getActivity());
        lay_refresh.setRefreshHeaderView(header);
        shareInfoParam = new ShareInfoParam();
        shareGoodDialogUtil = new ShareGoodDialogUtil(baseActivity);
        currentFrom = getArguments().getString("from");
        type = getArguments().getString("type");
        goodList = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
        mPresenter = new NewUserGoodsPresenter(baseActivity,  this,type);

        if (hotBlogAdapter == null) {
            hotBlogAdapter = new NewUserGoodsAdapter(baseActivity, goodList,type,NewUserPageActivity.isNew);
            hotBlogAdapter.setAddShoppingCarListener(this);
            hotBlogAdapter.setOnItemClickListener((view, position) -> {
                if(goodList.get(position).status.equals("0")){
                    Common.staticToast("不好意思啦！亲！该商品已失效了！");
                }else{
                    GoodsDetailAct.startAct(baseActivity, goodList.get(position).id);
                }
            });
            recycler_list.setAdapter(hotBlogAdapter);
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPayData(UserPaySuccessEvent event) {
        if (event!=null && event.isSuccess&&event.isFragmet) {
            if (mPresenter != null) {
                if (mPresenter != null) {
                    mPresenter.refreshData();
                }
            }
        }
    }

    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            if (mPresenter != null) {
                mPresenter.refreshData();
            }
        });

        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
        super.initListener();
    }


    @Override
    public void showFailureView(int request_code) {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }

    @Override
    public void showDataEmptyView(int request_code) {
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(RefreshBlogEvent event) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void userGoodsList(int currentPage, int totalPage, List<NewUserGoodsEntity.Goods> collectionGoodsLists) {
        if (currentPage == 1) {
            goodList.clear();
            if (isEmpty(collectionGoodsLists)) {
                recycler_list.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.VISIBLE);
            } else {
                recycler_list.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
            }
        }
        if (!isEmpty(collectionGoodsLists)) {
            goodList.addAll(collectionGoodsLists);
        }

        hotBlogAdapter.setPageLoading(currentPage, totalPage);
        hotBlogAdapter.notifyDataSetChanged();
    }

    /**
     * 显示商品属性
     *
     * @param goods
     */
    @Override
    public void showGoodsSku(GoodsDeatilEntity.Goods goods,int postion) {
        ParamDialog paramDialog = new ParamDialog(baseActivity,goods);
        paramDialog.show();
        paramDialog.setSelectCount(false);
        paramDialog.setOnSelectCallBack((sku, count) -> {
            if (mPresenter != null&&type.equals("1")){
                mPresenter.addCart(goods.goods_id,sku==null?"":sku.id,String.valueOf(count),postion);
            }
        });
    }

    @Override
    public void onGoodsId(View view, int position) {
        if(goodList.get(position).status.equals("0")){
            Common.staticToast("不好意思啦！亲！该商品已失效了！");
            return;
        }
        if(type.equals("1")){
            if(NewUserPageActivity.isNew) {
                NewUserGoodsEntity.Goods goods = goodList.get(position);
                mPresenter.getGoodsSku(goods.id,position);
            }else{
                updateShareParm(position);
                mPresenter.getShareInfo(mPresenter.new_exclusive,goodList.get(position).id);
            }
            return;
        }
        GoodsDetailAct.startAct(baseActivity, goodList.get(position).id);
    }

    /**
     * 更新分享的参数
     * @param postion
     */
    private void updateShareParm(int postion){
        shareInfoParam.title = goodList.get(postion).title;
        shareInfoParam.price = goodList.get(postion).price;
        shareInfoParam.market_price = goodList.get(postion).market_price;
        shareInfoParam.shareLink = goodList.get(postion).thumb;
        shareInfoParam.img = goodList.get(postion).thumb;
        shareInfoParam.goods_id = goodList.get(postion).id;
        shareInfoParam.start_time = "0元3件";
        shareInfoParam.act_label = "新人专享";
    }

    @Override
    public void addCartSuccess(int cartNum,int postion) {
        if(goodList!=null&&goodList.size()>postion){
            goodList.get(postion).is_add_cart = 1;
            ((NewUserPageActivity)baseActivity).addCartOne();
            if(NewUserPageActivity.CURRENT_NUM==NewUserPageActivity.MAX_COUNT) {
                hotBlogAdapter.notifyDataSetChanged();
            }else{
                hotBlogAdapter.notifyItemChanged(postion);
            }

        }
    }

    @Override
    public void showNoPayDialog(String msg) {
        CommonDialogUtil promptDialog = new CommonDialogUtil(baseActivity);
        promptDialog.defaultCommonDialog(msg, "去支付", view -> {
            promptDialog.dismiss();
            MyOrderAct.startAct(baseContext, 2);
        }, "再逛逛", view -> promptDialog.dismiss());
    }

    public void updateCartGoods(NewUserGoodsEntity.Goods goodsList) {
           if(hotBlogAdapter!=null){
               for (NewUserGoodsEntity.Goods good:this.goodList){
                   if(goodsList.goods_id.equals(good.id)){
                       good.is_add_cart = 0;
                       hotBlogAdapter.notifyDataSetChanged();
                   }
               }
           }
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        shareInfoParam.title = baseEntity.data.title;
        shareInfoParam.price = baseEntity.data.price;
        shareInfoParam.market_price = baseEntity.data.market_price;
        shareInfoParam.shareLink = baseEntity.data.shareLink;
        shareInfoParam.img = baseEntity.data.img;
        shareInfoParam.desc = baseEntity.data.desc;
        shareInfoParam.start_time = "0元3件";
        shareInfoParam.act_label = "新人专享";
        shareGoodDialogUtil.shareGoodDialog(shareInfoParam,true,false);
    }
}
