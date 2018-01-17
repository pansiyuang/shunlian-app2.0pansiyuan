package com.shunlian.app.ui.category;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.RankingListAdapter;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.adapter.SingleCategoryAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.RankingListEntity;
import com.shunlian.app.listener.OnItemClickListener;
import com.shunlian.app.presenter.RankingListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.HorItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IRankingListView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/9.
 */

public class RankingListAct extends BaseActivity implements IRankingListView{


    private String id;
    private String firstName;
    private String secondName;
    private LinearLayoutManager horManager;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.recy_view_list)
    RecyclerView recy_view_list;

    @BindView(R.id.mtv_name)
    MyTextView mtv_name;

    @BindView(R.id.lLayout_category)
    LinearLayout lLayout_category;

    @BindView(R.id.recy_view_text)
    RecyclerView recy_view_text;

    @BindView(R.id.miv_pic)
    MyImageView miv_pic;

    private LinearLayoutManager verManager;
    private boolean isShow;//默认不显示
    private GridLayoutManager gridManager;
    private RankingListPresenter presenter;
    private SingleCategoryAdapter adapter;
    private List<GoodsDeatilEntity.Goods> goodsList = new ArrayList<>();
    private String g_cid;
    private RankingListAdapter listAdapter;
    private SimpleRecyclerAdapter simpleRecyclerAdapter;

    public static void startAct(Context context, String id, String firstName, String secondName){

        Intent intent = new Intent(context,RankingListAct.class);
        intent.putExtra("id",id);
        intent.putExtra("firstName",firstName);
        intent.putExtra("secondName",secondName);
        context.startActivity(intent);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_ranking_list;
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view_list.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (verManager != null){
                    int lastPosition = verManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == verManager.getItemCount()){
                        if ( presenter  != null){
                            presenter.onRefresh();
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
        id = intent.getStringExtra("id");
        firstName = intent.getStringExtra("firstName");
        secondName = intent.getStringExtra("secondName");

        mtv_name.setText(firstName.concat("-").concat(secondName));

        horManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recy_view.setLayoutManager(horManager);
        int space = TransformUtil.dip2px(this, 25);
        int left = TransformUtil.dip2px(this,10);
        recy_view.addItemDecoration(new HorItemDecoration(space,left,left));

        verManager = new LinearLayoutManager(this);
        recy_view_list.setLayoutManager(verManager);


        gridManager = new GridLayoutManager(this,3);
        recy_view_text.setLayoutManager(gridManager);

        presenter = new RankingListPresenter(this,this,id);

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

    /**
     * 排行分类列表
     *
     * @param categoryList
     */
    @Override
    public void rankingCategoryList(final List<RankingListEntity.Category> categoryList) {
        if (listAdapter == null) {
            listAdapter = new RankingListAdapter(this, categoryList);
            recy_view.setAdapter(listAdapter);
        }
        if (categoryList != null && categoryList.size() > 0){
            g_cid = categoryList.get(0).g_cid;
            presenter.cid = g_cid;
        }

        listAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                listAdapter.currentPosition = position;
                listAdapter.notifyDataSetChanged();
                RankingListEntity.Category category = categoryList.get(position);
                presenter.getNewRankingList(category.g_cid);
            }
        });

        if (simpleRecyclerAdapter == null) {
            simpleRecyclerAdapter = new SimpleRecyclerAdapter<RankingListEntity.Category>(this, R.layout.layout_ranking_title, categoryList) {

                @Override
                public void convert(SimpleViewHolder holder, RankingListEntity.Category category, int position) {
                    MyTextView mtv_text = holder.getView(R.id.mtv_text);
                    holder.addOnClickListener(R.id.mtv_text);
                    mtv_text.setWHProportion(240, 50);
                    mtv_text.setText(category.name);
                }
            };

            recy_view_text.setAdapter(simpleRecyclerAdapter);
        }
        simpleRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RankingListEntity.Category category = categoryList.get(position);
                presenter.getNewRankingList(category.g_cid);
                showCategory();
                listAdapter.currentPosition = position;
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.view_btn)
    public void showCategoryClick(){
        showCategory();
    }

    private void showCategory() {
        if (!isShow){//显示
            recy_view.setVisibility(View.GONE);
            mtv_name.setVisibility(View.VISIBLE);
            lLayout_category.setVisibility(View.VISIBLE);
            miv_pic.setImageResource(R.mipmap.icon_sjiantou);
        }else {//隐藏
            recy_view.setVisibility(View.VISIBLE);
            mtv_name.setVisibility(View.GONE);
            lLayout_category.setVisibility(View.GONE);
            miv_pic.setImageResource(R.mipmap.icon_xjiantou);
        }
        isShow = !isShow;
    }

    /**
     * 排行商品列表
     *
     * @param goods
     */
    @Override
    public void rankingGoodsList(RankingListEntity.Goods goods,int page, int allpage) {

        if (page == 1){
            goodsList.clear();
        }

        if (!isEmpty(goods.goods_list)){
            goodsList.addAll(goods.goods_list);
        }
        if (adapter == null) {
            adapter = new SingleCategoryAdapter(this, true, goodsList);
            recy_view_list.setAdapter(adapter);
            adapter.setPageLoading(page,allpage);
            adapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
                @Override
                public void onReload() {
                    if (presenter != null){
                        presenter.onRefresh();
                    }
                }
            });
        }else {
            adapter.setPageLoading(page,allpage);
            if (goodsList.size() <= presenter.page_size)
                adapter.notifyDataSetChanged();
            else
                adapter.notifyItemInserted(presenter.page_size);
        }
    }
}
