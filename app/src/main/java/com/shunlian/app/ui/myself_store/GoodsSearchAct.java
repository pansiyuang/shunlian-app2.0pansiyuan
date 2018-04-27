package com.shunlian.app.ui.myself_store;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.GoodsSearchAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.presenter.CategoryPresenter;
import com.shunlian.app.presenter.GoodsSearchPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IGoodsSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/1.
 */

public class GoodsSearchAct extends BaseActivity implements IGoodsSearchView {


    @BindView(R.id.recycle_category)
    RecyclerView recycle_category;

    @BindView(R.id.tv_keyword)
    TextView tv_keyword;

    @BindView(R.id.tv_store_add)
    TextView tv_store_add;

    @BindView(R.id.tv_add_goods)
    TextView tv_add_goods;


    private GoodsSearchPresenter presenter;
    private GoodsSearchAdapter singleAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GoodsSearchParam searchParam;
    private List<GoodsDeatilEntity.Goods> mGoods;
    private SearchGoodsEntity.RefStore mRefStore;
    private int totalPage;
    private int currentPage;
    public List<String> goods_list = new ArrayList<>();
    private int count;


    public static void startAct(Context context, GoodsSearchParam param) {
        Intent intent = new Intent(context, GoodsSearchAct.class);
        intent.putExtra("param", param);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_goodssearch;
    }

    @Override
    protected void initListener() {
        super.initListener();
        recycle_category.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (presenter != null) {
                            if (currentPage > totalPage) {
                                presenter.resetCurrentPage();
                            }
                            presenter.onRefresh();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        searchParam = (GoodsSearchParam) getIntent().getSerializableExtra("param");
        if (searchParam == null) {
            searchParam = new GoodsSearchParam();
        }
        tv_keyword.setText(searchParam.keyword);
        presenter = new GoodsSearchPresenter(this, this);
        presenter.getSearchGoods(searchParam, true);
        presenter.getFairishNums();
        mGoods = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        recycle_category.setLayoutManager(linearLayoutManager);

        singleAdapter = new GoodsSearchAdapter(this, mGoods, mRefStore,goods_list);

        int space = TransformUtil.dip2px(this, 0.5f);
        recycle_category.addItemDecoration(new VerticalItemDecoration(space,
                0,0,getColorResouce(R.color.value_ECECEC)));
        recycle_category.setAdapter(singleAdapter);

        singleAdapter.setOnItemClickListener((view, position) -> {
            GoodsDeatilEntity.Goods goods = mGoods.get(position);
            if (goods_list.contains(goods.id)){
                goods_list.remove(goods.id);
            }else {
                goods_list.add(goods.id);
            }
            singleAdapter.notifyDataSetChanged();
            tv_add_goods.setText(String.format(getStringResouce(R.string.add_some_goods)
                    ,String.valueOf(count-goods_list.size())));
        });
    }

    @Override
    public void getSearchGoods(SearchGoodsEntity goodsEntity, int page, int allPage) {
        currentPage = page;
        totalPage = allPage;
        if (currentPage == 1) {
            mGoods.clear();
            mRefStore = goodsEntity.ref_store;
            singleAdapter.setStoreData(mRefStore);
        }
        if (!isEmpty(goodsEntity.goods_list)) {
            mGoods.addAll(goodsEntity.goods_list);
        }

        singleAdapter.setStoreData(mRefStore);
        if (goodsEntity.goods_list.size() <= CategoryPresenter.PAGE_SIZE) {
            singleAdapter.notifyDataSetChanged();
        } else {
            singleAdapter.notifyItemInserted(CategoryPresenter.PAGE_SIZE);
        }
        singleAdapter.setPageLoading(page, allPage);

    }

    //全选
   /* @OnClick(R.id.tv_selectAll)
    public void allSelect(){
        if (!isEmpty(mGoods) && mGoods.size() <= count){
            for (int i = 0; i < mGoods.size(); i++) {
                GoodsDeatilEntity.Goods goods = mGoods.get(i);
                if (!goods_list.contains(goods.goods_id)){
                    goods_list.add(goods.goods_id);
                }
            }
            singleAdapter.notifyDataSetChanged();
        }
    }*/

   @OnClick(R.id.tv_store_add)
   public void addStore(){
       StringBuilder sb = new StringBuilder();
       for (int i = 0; i < goods_list.size(); i++) {
           String s = goods_list.get(i);
           sb.append(s);
           if (i + 1 != goods_list.size()){
               sb.append(",");
           }
       }
       presenter.addStoreGoods(sb.toString());
   }

   @OnClick(R.id.mtv_cancel)
   public void cancle(){
       finish();
   }

   @OnClick(R.id.ll_search)
   public void search(){
       SearchGoodsActivity.startActivityForResult(this);
   }

    @Override
    public void getFairishNums(String count) {
        this.count = Integer.parseInt(count);
        tv_add_goods.setText(String.format(getStringResouce(R.string.add_some_goods), count));
    }

    @Override
    public void addStoreFinish(String count) {
        finish();
        MyLittleStoreActivity.startAct(this);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SearchGoodsActivity.SEARCH_REQUEST_CODE && resultCode == RESULT_OK){
            String keyword = data.getStringExtra("keyword");
            if (searchParam == null) {
                searchParam = new GoodsSearchParam();
            }
            tv_keyword.setText(keyword);
            searchParam.keyword = keyword;
            presenter.getSearchGoods(searchParam,true);
        }
    }
}
