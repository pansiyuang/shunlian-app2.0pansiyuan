package com.shunlian.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SortCategoryAdapter;
import com.shunlian.app.adapter.SortFragAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.SortFragEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.SortFragPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.category.CategoryAct;
import com.shunlian.app.ui.category.RankingListAct;
import com.shunlian.app.ui.fragment.first_page.FirstPageFrag;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.ISortFragView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
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

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.lLayout_sort)
    LinearLayout lLayout_sort;

    private SortFragPresenter presenter;
    private MessageCountManager messageCountManager;
    private List<SortFragEntity.ItemList> itemLists = new ArrayList<>();
    private SortCategoryAdapter adapter;
    private GoodsSearchParam mParam;
    private GridLayoutManager manager;
    private int lvTotalHeight;//listview总高度
    private GetDataEntity.KeyWord keyworld;

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
        visible(miv_close);
        presenter = new SortFragPresenter(this, this);

        manager = new GridLayoutManager(this, 3);
        recycler_sort.setLayoutManager(manager);

        listView.post(() -> lvTotalHeight = listView.getMeasuredHeight());
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
        visible(quick_actions);
        quick_actions.category();
    }

    @OnClick(R.id.mtv_search)
    public void onClickSearch() {
        CharSequence text = mtv_search.getText();
        if (keyworld!=null&&!isEmpty(keyworld.type)){
            SearchGoodsActivity.startAct(baseAct, text.toString(), "sortFrag",keyworld.type,keyworld.item_id);
        }else {
            SearchGoodsActivity.startAct(baseAct, text.toString(), "sortFrag");
        }
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {
        if (nei_empty != null){
            visible(nei_empty);
            gone(lLayout_sort);
            nei_empty.setNetExecption().setOnClickListener(v -> {
                if (presenter != null){
                    presenter.attachView();
                }
            });
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
     * 右侧子分类
     *
     * @param toplist
     */
    public void subRightList(final SortFragEntity.Toplist toplist) {
        if (itemLists == null)
            itemLists = new ArrayList<>();
        itemLists.clear();
        List<SortFragEntity.SubList> children = toplist.children;
        if (!isEmpty(children)) {
            for (int i = 0; i < children.size(); i++) {//遍历二级分类
                List<SortFragEntity.ItemList> children1 = children.get(i).children;
                if (!isEmpty(children1)) {
                    itemLists.addAll(children1);//对应一级分类下的所有三级分类列表
                }
            }
        }
        manager.scrollToPosition(0);
        if (adapter == null) {
            adapter = new SortCategoryAdapter(this, itemLists, toplist);
            recycler_sort.setAdapter(adapter);

            adapter.setOnItemClickListener((view, position) -> {
                if (adapter.counts.contains(position)) {
                    SortFragEntity.SubList subList = adapter.titleData.get(position);
                    if ("1".equalsIgnoreCase(subList.on_ranking)) {
                        RankingListAct.startAct(this, subList.id, toplist.name, subList.name);
                    }
                } else {
                    int i = adapter.computeCount(position);
                    SortFragEntity.ItemList itemList = itemLists.get(position - i);
                    if (mParam == null)
                        mParam = new GoodsSearchParam();
                    mParam.cid = itemList.g_cid;
                    mParam.attr_data = itemList.attrs;
                    mParam.keyword = itemList.keyword;
                    mParam.name = itemList.name;
                    CategoryAct.startAct(this, mParam);
                }
            });
        }else {
            adapter.filterData(toplist, true);
        }
    }


    /**
     * 分类所有类目
     *
     * @param categoryList
     */
    @Override
    public void categoryAll(final List<SortFragEntity.Toplist> categoryList) {
        gone(nei_empty);
        visible(lLayout_sort);
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
            listView.smoothScrollToPositionFromTop(position, lvTotalHeight / 2, 300);//把当前点击position的view移到顶部
        });
    }

    @Override
    public void setKeyworld(GetDataEntity.KeyWord keyworld) {
        if (keyworld != null ) {
            this.keyworld= keyworld;
            if (!isEmpty(keyworld.keyword))
                mtv_search.setText(keyworld.keyword);
        }
    }


    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (presenter != null)presenter.detachView();
        if (itemLists != null){
            itemLists.clear();
            itemLists = null;
        }
        if (adapter != null){
            adapter.detachView();
            adapter = null;
        }
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
