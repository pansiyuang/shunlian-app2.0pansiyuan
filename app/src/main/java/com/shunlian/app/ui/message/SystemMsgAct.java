package com.shunlian.app.ui.message;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.SystemMsgPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.ISystemMsgView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/8.
 */

public class SystemMsgAct extends BaseActivity implements ISystemMsgView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private LinearLayoutManager manager;
    private SystemMsgPresenter presenter;

    public static void startAct(Context context){
        context.startActivity(new Intent(context,SystemMsgAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_system_msg;
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
                        if (presenter != null){
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
        mtv_toolbar_title.setText("系统通知");

        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        presenter = new SystemMsgPresenter(this,this);
    }

    @OnClick(R.id.mrlayout_toolbar_more)
    public void more(){
        visible(quick_actions);
        quick_actions.message();
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {
        if (request_code == 0){
            visible(nei_empty);
            gone(recy_view);
            nei_empty.setNetExecption().setOnClickListener(v -> {
                if (presenter != null)presenter.initApi();
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
        if (request_code == 0){
            visible(nei_empty);
            gone(recy_view);
            nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText("暂无消息")
                    .setButtonText(null);
        }else {
            gone(nei_empty);
            visible(recy_view);
        }
    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        gone(nei_empty);
        visible(recy_view);
        recy_view.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null){
            quick_actions.destoryQuickActions();
        }
        super.onDestroy();
        if (presenter != null){
            presenter.detachView();
            presenter = null;
        }
    }
}
