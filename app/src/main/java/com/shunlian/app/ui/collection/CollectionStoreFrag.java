package com.shunlian.app.ui.collection;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.CollectionGoodsAdapter;
import com.shunlian.app.adapter.CollectionStoresAdapter;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.CollectionStoresEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.listener.OnItemClickListener;
import com.shunlian.app.presenter.CollectionGoodsPresenter;
import com.shunlian.app.presenter.CollectionStoresPresenter;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.ICollectionStoresView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ParamDialog;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.shunlian.app.R2.string.goods;

/**
 * Created by Administrator on 2018/1/22.
 * 收藏店铺
 */

public class CollectionStoreFrag extends CollectionFrag implements ICollectionStoresView{
    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.recycle_category)
    RecyclerView recycle_category;

    @BindView(R.id.mtv_in_cate)
    MyTextView mtv_in_cate;

    @BindView(R.id.flayout_category)
    FrameLayout flayout_category;

    @BindView(R.id.mtv_out_cate)
    MyTextView mtv_out_cate;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;
    private SimpleRecyclerAdapter cateAdapter;
    private String selectId = "null";
    private CollectionStoresPresenter mPresenter;
    private List<CollectionStoresEntity.Store> stores = new ArrayList<>();
    private CollectionStoresAdapter adapter;
    private boolean isAllSelect;//是否全选
    private List<CollectionStoresEntity.Store> delLists;
    private LinearLayoutManager manager;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_collection_store,null,false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

    @Override
    protected void initData() {
        mPresenter = new CollectionStoresPresenter(baseActivity,this);

        //列表
        manager = new LinearLayoutManager(baseActivity);
        recy_view.setLayoutManager(manager);

        int space = TransformUtil.dip2px(baseActivity, 0.5f);
        recy_view.addItemDecoration(new VerticalItemDecoration(space,
                0,0,getColorResouce(R.color.light_gray_three)));


        //分类
        GridLayoutManager gridLayoutManager = new GridLayoutManager(baseActivity,3);
        recycle_category.setLayoutManager(gridLayoutManager);

    }

    /**
     * 删除
     */
    @Override
    public void delete() {
        delLists = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stores.size(); i++){
            CollectionStoresEntity.Store store = stores.get(i);
            if (store.isSelect){
                sb.append(store.id);
                sb.append(",");
                delLists.add(store);
            }
        }
        mPresenter.storesFavRemove(sb.toString());
    }

    /**
     * 选择所有
     */
    @Override
    public void selectAll() {
        if (!isEmpty(stores)){
            if (!isAllSelect) {
                for (CollectionStoresEntity.Store store : stores) {
                    store.isSelect = true;
                }
            }else {
                for (CollectionStoresEntity.Store store : stores) {
                    store.isSelect = false;
                }
            }
            adapter.notifyDataSetChanged();
            isAllSelect = !isAllSelect;
        }
    }

    /**
     * 操作管理
     */
    @Override
    public void operationMange() {

    }

    /**
     * 完成管理
     */
    @Override
    public void finishManage() {
        if (adapter != null){
            adapter.isShowSelect = false;
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        if (!isEmpty(stores)){
            if (adapter != null){
                adapter.isShowSelect = true;
                adapter.notifyDataSetChanged();
            }
            return true;
        }
        return false;
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {
        LogUtil.zhLogW("showFailureView=========request_code="+request_code);
        if (request_code == CollectionGoodsPresenter.DISPLAY_NET_FAIL){
            visible(nei_empty);
            nei_empty.setNetExecption().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPresenter != null){
                        mPresenter.sort();
                    }
                }
            });
        }else {
            if (adapter != null){
                adapter.loadFailure();
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

    @OnClick({R.id.mrlayout_in_category,R.id.mrlayout_out_category,R.id.flayout_category})
    public void clickCategory(){
        if (flayout_category.getVisibility() == View.GONE) {
            visible(flayout_category);
        }else {
            gone(flayout_category);
        }
    }


    /**
     * 收藏商品列表
     *
     * @param mstores
     */
    @Override
    public void collectionStoresList(int page, int allPage, final List<CollectionStoresEntity.Store> mstores) {
        if (page == 1){
            recy_view.scrollToPosition(0);
            stores.clear();
        }
        if (!isEmpty(mstores)){
            stores.addAll(mstores);
        }
        if (adapter == null) {
            adapter = new CollectionStoresAdapter(baseActivity, stores);
            recy_view.setAdapter(adapter);
            adapter.setPageLoading(page,allPage);
            adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    CollectionStoresEntity.Store store = stores.get(position);
                    if (adapter.isShowSelect) {
                        store.isSelect = !store.isSelect;
                        adapter.notifyItemChanged(position);
                        ((MyCollectionAct) baseActivity).setDeleteBackgroundColor(isSelectItem());
                    }else if ("1".equals(store.status)){
                        StoreAct.startAct(baseActivity,store.store_id);
                    }
                }
            });

            //侧滑删除
            adapter.setDelCollectionStoresListener(new CollectionStoresAdapter.IDelCollectionStoresListener() {
                @Override
                public void onDelStores(CollectionStoresEntity.Store store) {
                    mPresenter.storesFavRemove(store.id);
                    delLists = new ArrayList<>();
                    delLists.add(store);
                }
            });
            adapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
                @Override
                public void onReload() {
                    if (mPresenter != null){
                        mPresenter.onRefresh();
                    }
                }
            });
        }else {
            adapter.setPageLoading(page,allPage);
            adapter.notifyDataSetChanged();
        }

        showEmptyPage(isEmpty(stores));
    }

    /**
     * 分类
     * @param cates
     */
    @Override
    public void collectionStoresCategoryList(final List<CollectionStoresEntity.Cates> cates) {
        if (cateAdapter == null) {
            cateAdapter = new SimpleRecyclerAdapter<CollectionStoresEntity.Cates>
                    (baseActivity, R.layout.textview_layout, cates) {

                @Override
                public void convert(SimpleViewHolder holder, CollectionStoresEntity.Cates cates, int position) {
                    MyTextView textView = holder.getView(R.id.mtv_text);
                    holder.addOnClickListener(R.id.mtv_text);
                    textView.setWHProportion(210, 70);
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) textView.getLayoutParams();
                    layoutParams.topMargin = TransformUtil.dip2px(baseContext,15);
                    layoutParams.leftMargin = TransformUtil.dip2px(baseContext,10);
                    textView.setLayoutParams(layoutParams);
                    Drawable drawable = baseActivity.getResources().getDrawable(R.drawable.rounded_rectangle_35px);
                    textView.setBackgroundDrawable(drawable);
                    textView.setText(cates.name);
                    GradientDrawable background = (GradientDrawable) textView.getBackground();
                    if (selectId.equals(cates.id)){
                        background.setColor(getColorResouce(R.color.category_reset));
                        textView.setTextColor(getColorResouce(R.color.pink_color));
                    }else {
                        background.setColor(getColorResouce(R.color.white));
                        textView.setTextColor(getColorResouce(R.color.new_text));
                    }
                }
            };
            recycle_category.setAdapter(cateAdapter);
            cateAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    CollectionStoresEntity.Cates cate = cates.get(position);
                    selectId = cate.id;
                    mPresenter.setCate(cate.id);
                    cateAdapter.notifyDataSetChanged();
                    gone(flayout_category);
                    mPresenter.sort();
                }
            });
        }
    }

    private void showEmptyPage(boolean isShow){
        if (isShow) {
            visible(nei_empty);
            nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText(getStringResouce(R.string.collection_empty))
                    .setButtonText(getStringResouce(R.string.common_go_shop))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Common.staticToast("去首页");
                        }
                    });
        }else {
            gone(nei_empty);
        }
    }

    /**
     * 删除成功
     */
    @Override
    public void delSuccess() {
        if (!isEmpty(delLists)){
            for (CollectionStoresEntity.Store store : delLists){
                stores.remove(store);
            }
            adapter.notifyDataSetChanged();
        }
        showEmptyPage(isEmpty(stores));
    }

    private boolean isSelectItem(){
        if (!isEmpty(stores)){
            for (CollectionStoresEntity.Store store : stores){
                if (store.isSelect){
                    return true;
                }
            }
        }
        return false;
    }
}
