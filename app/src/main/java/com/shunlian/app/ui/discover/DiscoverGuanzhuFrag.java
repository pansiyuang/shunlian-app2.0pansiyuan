package com.shunlian.app.ui.discover;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.presenter.GuanzhuPresenter;
import com.shunlian.app.ui.discover.guanzhu.FindSelectShopAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IGuanzhuView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;


public class DiscoverGuanzhuFrag extends DiscoversFrag implements IGuanzhuView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    QuickActions quick_actions;

    private LinearLayoutManager manager;
    private GuanzhuPresenter presenter;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_guanzhu, null, false);
    }

    @Override
    protected void initData() {

        NestedSlHeader header = new NestedSlHeader(baseContext);
        lay_refresh.setRefreshHeaderView(header);

        presenter = new GuanzhuPresenter(baseActivity, this);
        manager = new LinearLayoutManager(baseActivity);
        recy_view.setLayoutManager(manager);
        int space = TransformUtil.dip2px(baseActivity, 10);
        recy_view.addItemDecoration(new VerticalItemDecoration(space,
                0, 0, getColorResouce(R.color.white_ash)));
        EventBus.getDefault().register(this);//注册
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(baseActivity,"login");
        }

        //分享
        quick_actions = new QuickActions(baseActivity);
        ViewGroup decorView = (ViewGroup) getActivity().getWindow().getDecorView();
        decorView.addView(quick_actions);
        quick_actions.setVisibility(View.INVISIBLE);
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
                        if (presenter != null) {
                            presenter.onRefresh();
                        }
                    }
                }
            }
        });

        lay_refresh.setOnRefreshListener(()-> {
            if (presenter != null) {
                presenter.initApi();
            }
        });

    }

    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        return false;
    }

    @Override
    public void showFailureView(int request_code) {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
        if (request_code == 0) {//无网络页面
            visible(nestedScrollView);
            gone(recy_view);
            nei_empty.setNetExecption().setOnClickListener(v -> {
                if (presenter != null) {
                    presenter.initApi();
                }
            });
        }
    }

    @Override
    public void showDataEmptyView(int request_code) {
        if (request_code == 0) {//显示空页面
            visible(nestedScrollView);
            gone(recy_view);
            nei_empty.setImageResource(R.mipmap.img_empty_faxian)
                    .setText(getStringResouce(R.string.discover_notfollow))
                    .setButtonText(getStringResouce(R.string.discover_gofollow))
                    .setOnClickListener((view) -> {
                        if (!Common.isAlreadyLogin()) {
                            Common.goGoGo(baseActivity,"login");
                            return;
                        }
                        FindSelectShopAct.startAct(baseActivity);
                    });
        } else {
            gone(nestedScrollView);
            visible(recy_view);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(DefMessageEvent event) {
        if (event.isRefGuanzhu || event.loginSuccess) {
            if (presenter != null) {
                presenter.initApi();
            }
        }
    }


    /**
     * 设置adapter
     *
     * @param adapter
     */
    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        gone(nestedScrollView);
        visible(recy_view);
        recy_view.setAdapter(adapter);
    }

    /**
     * 刷新完成
     */
    @Override
    public void refreshFinish() {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }

    /**
     * 分享
     *
     * @param shareInfoParam
     */
    @Override
    public void share(ShareInfoParam shareInfoParam) {
        if (quick_actions != null){
            visible(quick_actions);
            quick_actions.shareInfo(shareInfoParam);
            quick_actions.shareStyle2Dialog(true,2);
        }
    }

    @Override
    public void onDestroyView() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
    }
}
