package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.newchat.adapter.StoreMsgAdapter;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.presenter.VipMsgPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.IVipMsgView;
import com.shunlian.app.widget.nestedrefresh.interf.onRefreshListener;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/8.
 */

public class StoreMsgActivity extends BaseActivity implements IVipMsgView {

    @BindView(R.id.tv_vip_list)
    TextView tv_vip_list;

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private VipMsgPresenter vipMsgPresenter;
    private StoreMsgAdapter msgAdapter;
    private List<StoreMsgEntity.StoreMsg> storeMsgList;
    private boolean isGetVip;

    @Override
    protected int getLayoutId() {
        return R.layout.act_store_msg;
    }

    public static void startAct(Context context, boolean isVip) {
        Intent intent = new Intent(context, StoreMsgActivity.class);
        intent.putExtra("isVip", isVip);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        isGetVip = getIntent().getBooleanExtra("isVip", false);

        vipMsgPresenter = new VipMsgPresenter(this, this);
        if (isGetVip) {
            tv_title.setText(getStringResouce(R.string.vip_msg));
            vipMsgPresenter.getVipMessageList(true);
        } else {
            tv_title.setText(getStringResouce(R.string.order_msg));
            vipMsgPresenter.getOrderMessageList(true);
        }
        storeMsgList = new ArrayList<>();

        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(true);
    }

    @Override
    protected void initListener() {
        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (vipMsgPresenter != null) {
                    vipMsgPresenter.initPage();
                    if (isGetVip) {
                        vipMsgPresenter.getVipMessageList(true);
                    } else {
                        vipMsgPresenter.getOrderMessageList(true);
                    }
                }
            }

            @Override
            public void onLoadMore() {

            }
        });
        super.initListener();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getStoreMsgs(List<StoreMsgEntity.StoreMsg> msgList, int page, int total) {
        refreshview.stopRefresh(true);
        if (page == 1) {
            storeMsgList.clear();
        }

        if (!isEmpty(msgList)) {
            storeMsgList.addAll(msgList);
        }

        if (msgAdapter == null) {
            msgAdapter = new StoreMsgAdapter(this, storeMsgList);
            recycler_list.setAdapter(msgAdapter);
        }
        msgAdapter.notifyDataSetChanged();
    }

    @Override
    public void getOrderMsgs(List<StoreMsgEntity.StoreMsg> msgList, int page, int total) {
        refreshview.stopRefresh(true);

        if (page == 1) {
            storeMsgList.clear();
        }

        if (!isEmpty(msgList)) {
            storeMsgList.addAll(msgList);
        }

        if (msgAdapter == null) {
            msgAdapter = new StoreMsgAdapter(this, storeMsgList);
            recycler_list.setAdapter(msgAdapter);
        }
        msgAdapter.notifyDataSetChanged();
    }
}
