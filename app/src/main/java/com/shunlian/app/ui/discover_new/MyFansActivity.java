package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MyFansAdapter;
import com.shunlian.app.bean.FansEntity;
import com.shunlian.app.presenter.FansPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.IFansView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/23.
 */

public class MyFansActivity extends BaseActivity implements IFansView, MyFansAdapter.OnAdapterCallBack {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private FansPresenter mPresenter;
    private LinearLayoutManager manager;
    private List<FansEntity.Fans> fansList;
    private MyFansAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.act_my_fans;
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, MyFansActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();

        tv_title.setText("我的粉丝");

        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);
        recycler_list.setNestedScrollingEnabled(false);

        mPresenter = new FansPresenter(this, this);
        mPresenter.getFansList(true);

        manager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(manager);
        fansList = new ArrayList<>();

        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText("还没有关注你的粉丝哟")
                .setButtonText(null);
    }

    @Override
    protected void initListener() {
        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.initPage();
                    mPresenter.getFansList(true);
                }
            }

            @Override
            public void onLoadMore() {

            }
        });
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        super.initListener();
    }

    @Override
    public void getFansList(List<FansEntity.Fans> list, int page, int totalPage) {
        refreshview.stopRefresh(true);
        if (page == 1) {
            fansList.clear();
        }
        if (!isEmpty(list)) {
            fansList.addAll(list);
        }

        if (page == 1 && isEmpty(fansList)) {
            nei_empty.setVisibility(View.VISIBLE);
            recycler_list.setVisibility(View.GONE);
        } else {
            nei_empty.setVisibility(View.GONE);
            recycler_list.setVisibility(View.VISIBLE);
        }

        if (mAdapter == null) {
            mAdapter = new MyFansAdapter(this, fansList);
            recycler_list.setAdapter(mAdapter);
            mAdapter.setAdapterCallBack(this);
        }
        mAdapter.setPageLoading(page, totalPage);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void focusUser(int isFocus, String memberId) {
        for (FansEntity.Fans fans : fansList) {
            if (memberId.equals(fans.member_id)) {
                if (fans.focus_status == 0) {
                    fans.focus_status = 1;
                } else {
                    fans.focus_status = 0;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void toFocusUser(int isFocus, String memberId) {
        mPresenter.focusUser(isFocus, memberId);
    }
}
