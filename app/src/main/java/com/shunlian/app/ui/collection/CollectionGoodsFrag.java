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
import com.shunlian.app.adapter.CollectionGoodsAdapter;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.CollectionGoodsPresenter;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.ICollectionGoodsView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ParamDialog;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/22.
 * 收藏商品
 */

public class CollectionGoodsFrag extends CollectionFrag implements ICollectionGoodsView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.recycle_category)
    RecyclerView recycle_category;

    @BindView(R.id.flayout_category)
    FrameLayout flayout_category;

    @BindView(R.id.mtv_cate_name)
    MyTextView mtv_cate_name;

    @BindView(R.id.mtv_cut_price)
    MyTextView mtv_cut_price;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;
    private SimpleRecyclerAdapter cateAdapter;
    private String selectId = "0";
    private CollectionGoodsPresenter mPresenter;
    private List<CollectionGoodsEntity.Goods> goodsLists = new ArrayList<>();
    private CollectionGoodsAdapter adapter;
    private boolean isAllSelect;//是否全选
    private List<CollectionGoodsEntity.Goods> delLists;
    private LinearLayoutManager manager;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_collection_goods,null,false);
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
        mPresenter = new CollectionGoodsPresenter(baseActivity,this);

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
        for (int i = 0; i < goodsLists.size(); i++){
            CollectionGoodsEntity.Goods goods = goodsLists.get(i);
            if (goods.isSelect){
                sb.append(goods.favo_id);
                sb.append(",");
                delLists.add(goods);
            }
        }
        mPresenter.goodsFavRemove(sb.toString());
    }

    /**
     * 选择所有
     */
    @Override
    public void selectAll() {
        if (!isEmpty(goodsLists)){
            if (!isAllSelect) {
                for (CollectionGoodsEntity.Goods goods : goodsLists) {
                    goods.isSelect = true;
                }
            }else {
                for (CollectionGoodsEntity.Goods goods : goodsLists) {
                    goods.isSelect = false;
                }
            }
            adapter.notifyItemRangeChanged(0,goodsLists.size(),goodsLists);
            isAllSelect = !isAllSelect;
        }
    }

    /**
     * 操作管理
     */
    @Override
    public void operationMange() {
        if (adapter==null)
            return;
        for (CollectionGoodsEntity.Goods goods : goodsLists) {
            goods.isSelect = false;
        }
        adapter.notifyItemRangeChanged(0,goodsLists.size(),goodsLists);
        isAllSelect = false;
        gone(flayout_category);
    }

    /**
     * 完成管理
     */
    @Override
    public void finishManage() {
        if (adapter != null){
            adapter.isShowSelect = false;
            adapter.notifyItemRangeChanged(0,goodsLists.size(),goodsLists);
            isAllSelect = false;
        }
    }

    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        if (!isEmpty(goodsLists)){
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
        if (request_code == CollectionGoodsPresenter.DISPLAY_NET_FAIL){
            visible(nei_empty);
            nei_empty.setNetExecption().setOnClickListener(v -> {
                if (mPresenter != null){
                    mPresenter.sort();
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

    @OnClick({R.id.mrlayout_in_category,R.id.flayout_category})
    public void clickCategory(){
        if (flayout_category.getVisibility() == View.GONE) {
            visible(flayout_category);
        }else {
            gone(flayout_category);
        }
    }

    @OnClick(R.id.mtv_cut_price)
    public void  reducePrice(){
        GradientDrawable background = (GradientDrawable) mtv_cut_price.getBackground();
        background.setColor(getColorResouce(R.color.category_reset));
        mtv_cut_price.setTextColor(getColorResouce(R.color.pink_color));
        gone(flayout_category);
        mPresenter.setCateOrIscut(null,"1");
        mPresenter.sort();
        isAllSelect = false;
        ((MyCollectionAct) baseActivity).recoveryManage(this);
    }

    /**
     * 收藏商品列表
     *
     * @param collectionGoodsLists
     */
    @Override
    public void collectionGoodsList(int page,int allPage,
                                    List<CollectionGoodsEntity.Goods> collectionGoodsLists) {
        if (page == 1){
            recy_view.scrollToPosition(0);
            goodsLists.clear();
        }
        if (!isEmpty(collectionGoodsLists)){
            goodsLists.addAll(collectionGoodsLists);
        }
        if (adapter == null) {
            adapter = new CollectionGoodsAdapter(baseActivity, goodsLists);
            recy_view.setAdapter(adapter);
            adapter.setPageLoading(page,allPage);
            //添加购物车
            adapter.setAddShoppingCarListener((view, position) -> {
                CollectionGoodsEntity.Goods goods = goodsLists.get(position);
                mPresenter.getGoodsSku(goods.goods_id);
            });
            //点击条目
            adapter.setOnItemClickListener((view, position) -> {
                CollectionGoodsEntity.Goods goods = goodsLists.get(position);
                if (adapter.isShowSelect) {
                    goods.isSelect = !goods.isSelect;
                    adapter.notifyItemChanged(position,goods);
                    ((MyCollectionAct) baseActivity).setManageState(selectState());
                }else {
                    GoodsDetailAct.startAct(baseActivity,goods.goods_id);
                }
            });
            //重新获取数据
            adapter.setOnReloadListener(() -> {
                if (mPresenter != null){
                    mPresenter.onRefresh();
                }
            });
            //侧滑删除
            adapter.setDelCollectionGoodsListener(goods -> {
                mPresenter.goodsFavRemove(goods.favo_id);
                delLists = new ArrayList<>();
                delLists.add(goods);
            });
        }else {
            adapter.setPageLoading(page,allPage);
            adapter.notifyDataSetChanged();
        }

        showEmptyPage(isEmpty(goodsLists));
    }

    /**
     * 分类
     * @param cates
     */
    @Override
    public void collectionGoodsCategoryList(final List<CollectionGoodsEntity.Cates> cates) {
        if (cateAdapter == null) {
            CollectionGoodsEntity.Cates cate = new CollectionGoodsEntity.Cates();
            cate.id = "0";
            cate.name = getStringResouce(R.string.all);
            cates.add(0,cate);
            cateAdapter = new SimpleRecyclerAdapter<CollectionGoodsEntity.Cates>
                    (baseActivity, R.layout.textview_layout, cates) {

                @Override
                public void convert(SimpleViewHolder holder, CollectionGoodsEntity.Cates cates, int position) {
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
            cateAdapter.setOnItemClickListener((view, position) -> {
                CollectionGoodsEntity.Cates cate1 = cates.get(position);
                selectId = cate1.id;
                mPresenter.setCateOrIscut(cate1.id,"0");
                cateAdapter.notifyDataSetChanged();
                mtv_cate_name.setText(cate1.name);
                gone(flayout_category);
                mPresenter.sort();
                GradientDrawable background = (GradientDrawable) mtv_cut_price.getBackground();
                background.setColor(getColorResouce(R.color.white));
                mtv_cut_price.setTextColor(getColorResouce(R.color.new_text));
                isAllSelect = false;
                ((MyCollectionAct) baseActivity).recoveryManage(this);
            });
        }
    }

    private void showEmptyPage(boolean isShow){
        if (isShow) {
            visible(nei_empty);
            nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText("未发现收藏的商品")
                    .setButtonText("去逛逛")
                    .setOnClickListener(v ->Common.goGoGo(baseActivity,"mainPage"));
        }else {
            gone(nei_empty);
        }
    }

    /**
     * 显示商品属性
     *
     * @param goods
     */
    @Override
    public void showGoodsSku(final GoodsDeatilEntity.Goods goods) {
        ParamDialog paramDialog = new ParamDialog(baseActivity,goods);
        paramDialog.show();
        paramDialog.setOnSelectCallBack((sku, count) -> {
            if (mPresenter != null){
                mPresenter.addCart(goods.goods_id,sku==null?"":sku.id,String.valueOf(count));
            }
        });
    }

    /**
     * 删除成功
     */
    @Override
    public void delSuccess() {
        if (!isEmpty(delLists)){
            for (CollectionGoodsEntity.Goods goods : delLists){
                goodsLists.remove(goods);
            }
            adapter.notifyDataSetChanged();
        }
        boolean empty = isEmpty(goodsLists);
        showEmptyPage(empty);
        if (!empty) {
            ((MyCollectionAct) baseActivity).setManageState(selectState());
        }else {
            ((MyCollectionAct) baseActivity).recoveryManage(this);
        }
    }
    /*
    是否选择条目
     */
//    private boolean isSelectItem(){
//        if (!isEmpty(goodsLists)){
//            for (CollectionGoodsEntity.Goods goods: goodsLists){
//                if (goods.isSelect){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
    /*
    是否全选  0 全选  1 部分选择 2 全不选
     */
    private int selectState(){
        int hasSlect = 0;//有选择
        int notSlect = 0;//没有选择
        if (!isEmpty(goodsLists)){
            for (CollectionGoodsEntity.Goods goods: goodsLists){
                if (!goods.isSelect){
                    notSlect = 1;
                }else {
                    hasSlect = 1;
                }
                if (hasSlect == 1 && notSlect == 1){
                    isAllSelect = false;
                    return 1;//部分选择
                }
            }
            if (hasSlect == 0 && notSlect == 1){
                isAllSelect = false;
                return 2;//全不选
            }
            isAllSelect = true;
            return 0;//全选
        }
        return -1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null)
            mPresenter.detachView();
        if (goodsLists != null){
            goodsLists.clear();
            goodsLists = null;
        }
        if (delLists != null){
            delLists.clear();
            delLists = null;
        }
        if (cateAdapter != null){
            cateAdapter = null;
        }
        if (adapter != null){
            adapter.unbind();
            adapter = null;
        }
    }
}
