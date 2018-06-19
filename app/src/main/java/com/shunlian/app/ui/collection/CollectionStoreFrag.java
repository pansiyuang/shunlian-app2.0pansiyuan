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
import com.shunlian.app.adapter.CollectionStoresAdapter;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.CollectionStoresEntity;
import com.shunlian.app.presenter.CollectionGoodsPresenter;
import com.shunlian.app.presenter.CollectionStoresPresenter;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.ICollectionStoresView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/22.
 * 收藏店铺
 */

public class CollectionStoreFrag extends CollectionFrag implements ICollectionStoresView {
    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.recycle_category)
    RecyclerView recycle_category;

    @BindView(R.id.mtv_in_cate)
    MyTextView mtv_in_cate;

    @BindView(R.id.mtv_cate)
    MyTextView mtv_cate;

    @BindView(R.id.flayout_category)
    FrameLayout flayout_category;

    @BindView(R.id.mtv_out_cate)
    MyTextView mtv_out_cate;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;
    private SimpleRecyclerAdapter cateAdapter;
    private String selectId = "0";
    private CollectionStoresPresenter mPresenter;
    private List<CollectionStoresEntity.Store> stores = new ArrayList<>();
    private CollectionStoresAdapter adapter;
    private boolean isAllSelect;//是否全选
    private List<CollectionStoresEntity.Store> delLists;
    private LinearLayoutManager manager;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_collection_store, null, false);
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
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter = new CollectionStoresPresenter(baseActivity, this);

        //列表
        manager = new LinearLayoutManager(baseActivity);
        recy_view.setLayoutManager(manager);

        int space = TransformUtil.dip2px(baseActivity, 0.5f);
        recy_view.addItemDecoration(new VerticalItemDecoration(space,
                0, 0, getColorResouce(R.color.light_gray_three)));


        //分类
        GridLayoutManager gridLayoutManager = new GridLayoutManager(baseActivity, 3);
        recycle_category.setLayoutManager(gridLayoutManager);

    }

    /**
     * 删除
     */
    @Override
    public void delete() {
        delLists = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stores.size(); i++) {
            CollectionStoresEntity.Store store = stores.get(i);
            if (store.isSelect) {
                sb.append(store.id);
                sb.append(",");
                delLists.add(store);
            }
            if (i>=stores.size()-1){
                sb.replace(sb.length()-1,sb.length(),"");
                mPresenter.storesFavRemove(sb.toString());
            }
        }

    }

    /**
     * 选择所有
     */
    @Override
    public void selectAll() {
        if (!isEmpty(stores)) {
            if (!isAllSelect) {
                for (CollectionStoresEntity.Store store : stores) {
                    store.isSelect = true;
                }
            } else {
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
        for (CollectionStoresEntity.Store goods : stores) {
            goods.isSelect = false;
        }
        adapter.notifyDataSetChanged();
        isAllSelect = false;
        gone(flayout_category);
    }

    /**
     * 完成管理
     */
    @Override
    public void finishManage() {
        if (adapter != null) {
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
        if (!isEmpty(stores)) {
            if (adapter != null) {
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
        if (request_code == CollectionGoodsPresenter.DISPLAY_NET_FAIL) {
            visible(nei_empty);
            nei_empty.setNetExecption().setOnClickListener(v -> {
                if (mPresenter != null) {
                    mPresenter.sort();
                }
            });
        } else {
            if (adapter != null) {
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

    @OnClick({R.id.mrlayout_in_category, R.id.mrlayout_out_category, R.id.flayout_category})
    public void clickCategory() {
        if (flayout_category.getVisibility() == View.GONE) {
            visible(flayout_category);
        } else {
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
        if (page == 1) {
            recy_view.scrollToPosition(0);
            stores.clear();
        }
        if (!isEmpty(mstores)) {
            stores.addAll(mstores);
        }
        if (adapter == null) {
            adapter = new CollectionStoresAdapter(baseActivity, stores);
            recy_view.setAdapter(adapter);
            adapter.setOnItemClickListener((view, position) -> {
                CollectionStoresEntity.Store store = stores.get(position);
                if (adapter.isShowSelect) {
                    store.isSelect = !store.isSelect;
                    adapter.notifyItemChanged(position);
                    ((MyCollectionAct) baseActivity).setManageState(selectState());
                } else if ("1".equals(store.status)){
                    StoreAct.startAct(baseActivity, store.store_id);
                }
            });

            //侧滑删除
            adapter.setDelCollectionStoresListener(store -> {
                mPresenter.storesFavRemove(store.id);
                delLists = new ArrayList<>();
                delLists.add(store);
            });
            adapter.setOnReloadListener(() -> {
                if (mPresenter != null) {
                    mPresenter.onRefresh();
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(page, allPage);
        showEmptyPage(isEmpty(stores));
    }

    /*
   是否全选  0 全选  1 部分选择 2 全不选
    */
    private int selectState() {
        int hasSlect = 0;//有选择
        int notSlect = 0;//没有选择
        if (!isEmpty(stores)) {
            for (CollectionStoresEntity.Store store : stores) {
                if (!store.isSelect) {
                    notSlect = 1;
                } else {
                    hasSlect = 1;
                }
                if (hasSlect == 1 && notSlect == 1) {
                    isAllSelect = false;
                    return 1;//部分选择
                }
            }
            if (hasSlect == 0 && notSlect == 1) {
                isAllSelect = false;
                return 2;//全不选
            }
            isAllSelect = true;
            return 0;//全选
        }
        return -1;
    }

    /**
     * 分类
     *
     * @param cates
     */
    @Override
    public void collectionStoresCategoryList( List<CollectionStoresEntity.Cates> cates) {
        final List<CollectionStoresEntity.Cates> mcates=cates;
        CollectionStoresEntity.Cates cates1=new CollectionStoresEntity.Cates();
        cates1.id="0";
        cates1.name="全部";
        mcates.add(0,cates1);
        if (cateAdapter == null) {
            cateAdapter = new SimpleRecyclerAdapter<CollectionStoresEntity.Cates>
                    (baseActivity, R.layout.textview_layout, mcates) {

                @Override
                public void convert(SimpleViewHolder holder, CollectionStoresEntity.Cates cates, int position) {
                    MyTextView textView = holder.getView(R.id.mtv_text);
                    holder.addOnClickListener(R.id.mtv_text);
                    textView.setWHProportion(210, 70);
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) textView.getLayoutParams();
                    layoutParams.topMargin = TransformUtil.dip2px(baseContext, 15);
                    layoutParams.leftMargin = TransformUtil.dip2px(baseContext, 10);
                    textView.setLayoutParams(layoutParams);
                    Drawable drawable = baseActivity.getResources().getDrawable(R.drawable.rounded_rectangle_35px);
                    textView.setBackgroundDrawable(drawable);
                    textView.setText(cates.name);
                    GradientDrawable background = (GradientDrawable) textView.getBackground();
                    if (selectId.equals(cates.id)) {
                        background.setColor(getColorResouce(R.color.category_reset));
                        textView.setTextColor(getColorResouce(R.color.pink_color));
                    } else {
                        background.setColor(getColorResouce(R.color.white));
                        textView.setTextColor(getColorResouce(R.color.new_text));
                    }
                }
            };
            recycle_category.setAdapter(cateAdapter);
            cateAdapter.setOnItemClickListener((view, position) -> {
                CollectionStoresEntity.Cates cate = mcates.get(position);
                if (cate.id != null) {
                    selectId = cate.id;
                }
                mtv_cate.setText(cate.name);
                mPresenter.setCate(cate.id);
                cateAdapter.notifyDataSetChanged();
                gone(flayout_category);
                mPresenter.sort();
            });
        }
    }

    private void showEmptyPage(boolean isShow) {
        if (isShow) {
            visible(nei_empty);
            nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText(getStringResouce(R.string.collection_empty))
                    .setButtonText(getStringResouce(R.string.common_go_shop))
                    .setOnClickListener(v -> Common.goGoGo(baseActivity,"mainPage"));
        } else {
            gone(nei_empty);
        }
    }

    /**
     * 删除成功
     */
    @Override
    public void delSuccess() {
        if (!isEmpty(delLists)) {
            for (CollectionStoresEntity.Store store : delLists) {
                stores.remove(store);
            }
            adapter.notifyDataSetChanged();
        }
        boolean empty = isEmpty(stores);
        showEmptyPage(empty);
        if (!empty) {
            ((MyCollectionAct) baseActivity).setManageState(selectState());
        }else {
            ((MyCollectionAct) baseActivity).recoveryManage(this);
        }
    }
}
