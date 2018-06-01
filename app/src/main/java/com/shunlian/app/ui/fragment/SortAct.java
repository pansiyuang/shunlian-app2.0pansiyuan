package com.shunlian.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SortCategoryAdapter;
import com.shunlian.app.adapter.SortFragAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.SortFragEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.SortFragPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.category.CategoryAct;
import com.shunlian.app.ui.category.RankingListAct;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.ISortFragView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SortAct extends BaseActivity implements ISortFragView, MessageCountManager.OnGetMessageListener {

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

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    private SortFragPresenter presenter;
    private MessageCountManager messageCountManager;

    public static void startAct(Context context){
        context.startActivity(new Intent(context,SortAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.frag_sort;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

        EventBus.getDefault().register(this);

        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        presenter = new SortFragPresenter(this, this);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recycler_sort.setLayoutManager(manager);
    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(this);
            if (messageCountManager.isLoad()) {
                String s = messageCountManager.setTextCount(tv_msg_count);
                if (quick_actions != null)
                    quick_actions.setMessageCount(s);
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
        super.onResume();
    }



    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.category();
    }

    @OnClick(R.id.mtv_search)
    public void onClickSearch() {
        CharSequence text = mtv_search.getText();
        SearchGoodsActivity.startAct(this, text.toString(), "sortFrag");
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
     *
     * @param toplist
     */
    public void subRightList(final SortFragEntity.Toplist toplist) {

        final List<SortFragEntity.ItemList> itemLists = new ArrayList<>();
        List<SortFragEntity.SubList> children = toplist.children;
        if (!isEmpty(children)) {
            for (int i = 0; i < children.size(); i++) {//遍历二级分类
                List<SortFragEntity.ItemList> children1 = children.get(i).children;
                if (!isEmpty(children1)) {
                    itemLists.addAll(children1);//对应一级分类下的所有三级分类列表
                }
            }
        }


        final SortCategoryAdapter adapter = new SortCategoryAdapter(this, itemLists, toplist);

        recycler_sort.setAdapter(adapter);

        adapter.setOnItemClickListener((view, position) -> {
            if (adapter.counts.contains(position)) {
                SortFragEntity.SubList subList = adapter.titleData.get(position);
                RankingListAct.startAct(this, subList.id, toplist.name, subList.name);
            } else {
                int i = adapter.computeCount(position);
                SortFragEntity.ItemList itemList = itemLists.get(position - i);
                GoodsSearchParam param = new GoodsSearchParam();
                param.cid = itemList.g_cid;
                param.attr_data = itemList.attrs;
                param.keyword = itemList.name;
                CategoryAct.startAct(this, param);
            }
        });
    }


    /**
     * 分类所有类目
     *
     * @param categoryList
     */
    @Override
    public void categoryAll(final List<SortFragEntity.Toplist> categoryList) {
        if (isEmpty(categoryList)) {
            return;
        }
        SortFragEntity.Toplist toplist = categoryList.get(0);
        subRightList(toplist);

        final SortFragAdapter adapter = new SortFragAdapter(this, categoryList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            SortFragEntity.Toplist toplist1 = categoryList.get(position);
            adapter.currentPosition = position;
            adapter.notifyDataSetChanged();
            subRightList(toplist1);
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
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }
}