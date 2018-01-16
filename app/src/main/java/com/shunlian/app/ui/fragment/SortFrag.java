package com.shunlian.app.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.SortCategoryAdapter;
import com.shunlian.app.adapter.SortFragAdapter;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.SortFragEntity;
import com.shunlian.app.presenter.SortFragPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.category.CategoryAct;
import com.shunlian.app.ui.category.RankingListAct;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.ISortFragView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SortFrag extends BaseFragment implements ISortFragView{

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.recycler_sort)
    RecyclerView recycler_sort;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.mtv_search)
    MyTextView mtv_search;

    private SortFragPresenter presenter;

    /**
     * 设置布局id
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_sort, container, false);
        return view;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        presenter = new SortFragPresenter(baseActivity,this);

        GridLayoutManager manager = new GridLayoutManager(baseActivity,3);
        recycler_sort.setLayoutManager(manager);
    }

    @OnClick(R.id.rl_more)
    public void more(){
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.setShowItem(1,4,5);
    }

    @OnClick(R.id.mtv_search)
    public void onClickSearch(){
        CharSequence text = mtv_search.getText();
        if (!isEmpty(text)){
            GoodsSearchParam param = new GoodsSearchParam();
            param.keyword = text.toString();
            CategoryAct.startAct(baseActivity,param);
        }else {
            CategoryAct.startAct(baseActivity,null);
        }
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
     * 右侧子分类
     * @param toplist
     */
    public void subRightList(final SortFragEntity.Toplist toplist) {

        final List<SortFragEntity.ItemList> itemLists = new ArrayList<>();
        List<SortFragEntity.SubList> children = toplist.children;
        if (children != null && children.size() > 0){
            for (int i = 0; i < children.size(); i++) {
                List<SortFragEntity.ItemList> children1 = children.get(i).children;
                if (children1 != null && children1.size() > 0){
                    itemLists.addAll(children1);
                }
            }
        }


        final SortCategoryAdapter adapter = new SortCategoryAdapter(baseActivity,itemLists,toplist);

        recycler_sort.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (adapter.counts.contains(position)){
                    SortFragEntity.SubList subList = adapter.titleData.get(position);
                    RankingListAct.startAct(baseActivity, subList.id,toplist.name,subList.name);
                }else {
                    int i = adapter.computeCount(position);
                    SortFragEntity.ItemList itemList = itemLists.get(position - i);
                    GoodsSearchParam param = new GoodsSearchParam();
                    param.cid = itemList.g_cid;
                    param.attr_data = itemList.attrs;
                    CategoryAct.startAct(baseActivity, param);
                }
            }
        });
    }


    /**
     * 分类所有类目
     * @param categoryList
     */
    @Override
    public void categoryAll(final List<SortFragEntity.Toplist> categoryList) {
        SortFragEntity.Toplist toplist = categoryList.get(0);
        subRightList(toplist);

        final SortFragAdapter adapter = new SortFragAdapter(baseActivity, categoryList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SortFragEntity.Toplist toplist = categoryList.get(position);
                adapter.currentPosition = position;
                adapter.notifyDataSetChanged();
                subRightList(toplist);
            }
        });
    }

    /**
     * 设置搜索关键字
     *
     * @param keyworld
     */
    @Override
    public void setKeyworld(String keyworld) {
        mtv_search.setText(keyworld);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
    }
}
