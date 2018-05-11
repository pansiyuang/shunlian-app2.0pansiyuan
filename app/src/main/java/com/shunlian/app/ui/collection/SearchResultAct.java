package com.shunlian.app.ui.collection;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.CollectionGoodsAdapter;
import com.shunlian.app.adapter.CollectionStoresAdapter;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.CollectionStoresEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.SearchResultPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.ICollectionSearchResultView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ParamDialog;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/25.
 * 收藏搜索结果
 */

public class SearchResultAct extends BaseActivity implements ICollectionSearchResultView{

    @BindView(R.id.mtv_goods_search)
    MyTextView mtv_goods_search;

    @BindView(R.id.recycler_search)
    RecyclerView recycler_search;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private String keyword = "衣服";
    private String type = "goods";
    private SearchResultPresenter mPresenter;
    private List<CollectionGoodsEntity.Goods> goodsLists = new ArrayList<>();
    private List<CollectionStoresEntity.Store> storeLists = new ArrayList<>();
    private List<CollectionGoodsEntity.Goods> delGoodsLists;
    private List<CollectionStoresEntity.Store> delStoreLists;
    private CollectionGoodsAdapter goodsAdapter;
    private CollectionStoresAdapter storeAdapter;
    private LinearLayoutManager manager;

    public static void startAct(Context context, String keyword, String type){
        Intent intent = new Intent(context,SearchResultAct.class);
        intent.putExtra("keyword",keyword);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    /**
     * 布局id
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_search_result;
    }

    @Override
    protected void initListener() {
        super.initListener();
        recycler_search.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()){
                        if (mPresenter != null){
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        type = intent.getStringExtra("type");

        mtv_goods_search.setText(keyword);

        mPresenter = new SearchResultPresenter(this,this,keyword,type);

        manager = new LinearLayoutManager(this);
        recycler_search.setLayoutManager(manager);
        int space = TransformUtil.dip2px(this, 0.5f);
        recycler_search.addItemDecoration(new VerticalItemDecoration(space,
                0,0,getColorResouce(R.color.light_gray_three)));
    }

    @OnClick(R.id.tv_search_cancel)
    public void cancel(){
        finish();
    }

    @OnClick(R.id.mtv_goods_search)
    public void search(){
        SearchGoodsActivity.startActivityForResult(this,false,type);
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {
        if (request_code == SearchResultPresenter.FIRST_NET_FAIL){
            visible(nei_empty);
            gone(recycler_search);
            nei_empty.setNetExecption().setOnClickListener(v -> {
                if (mPresenter != null){
                    mPresenter.againRequest();
                }
            });
        }else {
            if (goodsAdapter != null){
                goodsAdapter.loadFailure();
            }
        }
    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    /**
     * 收藏商品列表
     *
     * @param page
     * @param allPage
     * @param collectionGoodsLists
     */
    @Override
    public void collectionGoodsList(int page, int allPage, List<CollectionGoodsEntity.Goods> collectionGoodsLists) {
        if (page == 1){
            recycler_search.scrollToPosition(0);
            goodsLists.clear();
        }
        if (!isEmpty(collectionGoodsLists)){
            goodsLists.addAll(collectionGoodsLists);
        }
        if (goodsAdapter == null) {
            goodsAdapter = new CollectionGoodsAdapter(this, goodsLists);
            recycler_search.setAdapter(goodsAdapter);
            goodsAdapter.setPageLoading(page,allPage);
            //添加购物车
            goodsAdapter.setAddShoppingCarListener(new CollectionGoodsAdapter.IAddShoppingCarListener() {
                @Override
                public void onGoodsId(View view, int position) {
                    CollectionGoodsEntity.Goods goods = goodsLists.get(position);
                    mPresenter.getGoodsSku(goods.goods_id);
                }
            });
            //点击条目
            goodsAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    CollectionGoodsEntity.Goods goods = goodsLists.get(position);
                    GoodsDetailAct.startAct(SearchResultAct.this, goods.goods_id);
                }
            });
            //重新获取数据
            goodsAdapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
                @Override
                public void onReload() {
                    if (mPresenter != null){
                        mPresenter.onRefresh();
                    }
                }
            });
            //侧滑删除
            goodsAdapter.setDelCollectionGoodsListener(new CollectionGoodsAdapter.IDelCollectionGoodsListener() {
                @Override
                public void onDelGoods(CollectionGoodsEntity.Goods goods) {
                    mPresenter.goodsFavRemove(goods.favo_id);
                    delGoodsLists = new ArrayList<>();
                    delGoodsLists.add(goods);
                }
            });
        }else {
            goodsAdapter.setPageLoading(page,allPage);
            goodsAdapter.notifyDataSetChanged();
        }
        showEmptyPage(isEmpty(goodsLists));
    }

    /**
     * 收藏店铺列表
     *
     * @param page
     * @param allPage
     * @param stores
     */
    @Override
    public void collectionStoresList(int page, int allPage, List<CollectionStoresEntity.Store> stores) {
        if (page == 1){
            recycler_search.scrollToPosition(0);
            storeLists.clear();
        }
        if (!isEmpty(stores)){
            storeLists.addAll(stores);
        }
        if (storeAdapter == null) {
            storeAdapter = new CollectionStoresAdapter(this, stores);
            recycler_search.setAdapter(storeAdapter);
            storeAdapter.setPageLoading(page,allPage);
            storeAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    CollectionStoresEntity.Store store = storeLists.get(position);
                    StoreAct.startAct(SearchResultAct.this, store.store_id);
                }
            });

            //侧滑删除
            storeAdapter.setDelCollectionStoresListener(new CollectionStoresAdapter.IDelCollectionStoresListener() {
                @Override
                public void onDelStores(CollectionStoresEntity.Store store) {
                    mPresenter.storesFavRemove(store.id);
                    delStoreLists = new ArrayList<>();
                    delStoreLists.add(store);
                }
            });

            storeAdapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
                @Override
                public void onReload() {
                    if (mPresenter != null){
                        mPresenter.onRefresh();
                    }
                }
            });
        }else {
            storeAdapter.setPageLoading(page,allPage);
            storeAdapter.notifyDataSetChanged();
        }

        showEmptyPage(isEmpty(storeLists));
    }

    /**
     * 显示商品属性
     *
     * @param goods
     */
    @Override
    public void showGoodsSku(final GoodsDeatilEntity.Goods goods) {
        ParamDialog paramDialog = new ParamDialog(this,goods);
        paramDialog.show();
        paramDialog.setOnSelectCallBack(new ParamDialog.OnSelectCallBack() {
            @Override
            public void onSelectComplete(GoodsDeatilEntity.Sku sku, int count) {
                if (mPresenter != null){
                    mPresenter.addCart(goods.goods_id,sku.id,String.valueOf(count));
                }
            }
        });
    }

    /**
     * 删除成功
     */
    @Override
    public void delSuccess() {
        if ("goods".equals(type)){
            if (!isEmpty(delGoodsLists)){
                for (CollectionGoodsEntity.Goods goods : delGoodsLists){
                    goodsLists.remove(goods);
                }
                goodsAdapter.notifyDataSetChanged();
            }
            delGoodsLists = null;
            showEmptyPage(isEmpty(goodsLists));
        }else {
            if (!isEmpty(delStoreLists)){
                for (CollectionStoresEntity.Store store : delStoreLists){
                    storeLists.remove(store);
                }
                storeAdapter.notifyDataSetChanged();
            }
            delStoreLists = null;
            showEmptyPage(isEmpty(storeLists));
        }

    }

    private void showEmptyPage(boolean isShow){
        if (isShow) {
            visible(nei_empty);
            gone(recycler_search);
            nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText("未发现收藏的商品")
                    .setButtonText("去逛逛")
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Common.staticToast("去首页");
                        }
                    });
        }else {
            gone(nei_empty);
            visible(recycler_search);
        }
    }
}
