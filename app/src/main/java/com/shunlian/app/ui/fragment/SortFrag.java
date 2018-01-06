package com.shunlian.app.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.SortCategoryAdapter;
import com.shunlian.app.adapter.SortFragAdapter;
import com.shunlian.app.bean.SortFragEntity;
import com.shunlian.app.presenter.SortFragPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ISortFragView;

import java.util.List;

import butterknife.BindView;

public class SortFrag extends BaseFragment implements ISortFragView{

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.recycler_sort)
    RecyclerView recycler_sort;
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
     * 左侧大分类
     *
     * @param toplists
     */
    @Override
    public void toplist(final List<SortFragEntity.Toplist> toplists) {
        final SortFragAdapter adapter = new SortFragAdapter(baseActivity, toplists);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SortFragEntity.Toplist toplist = toplists.get(position);
                adapter.currentPosition = position;
                adapter.notifyDataSetChanged();
                presenter.categorySubList(toplist.id);
            }
        });
    }

    /**
     * 右侧子分类
     * @param subLists
     */
    @Override
    public void subRightList(List<SortFragEntity.SubList> subLists, final List<SortFragEntity.ItemList> subAllItemLists) {

        final SortCategoryAdapter adapter = new SortCategoryAdapter(baseActivity,subAllItemLists,subLists);

        recycler_sort.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (adapter.counts.contains(position)){
                    // TODO: 2018/1/6 跳转排行榜
                    Common.staticToast(adapter.titleData.get(position).name);
                }else {
                    int i = adapter.computeCount(position);
                    SortFragEntity.ItemList itemList = subAllItemLists.get(position - i);
                    // TODO: 2018/1/6 跳转分类列表
                    Common.staticToast(itemList.name);
                }
            }
        });

    }


}
