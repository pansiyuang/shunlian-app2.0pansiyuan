package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.newchat.adapter.StoreMsgAdapter;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.presenter.VipMsgPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IVipMsgView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/8.
 */

public class StoreMsgActivity extends BaseActivity implements IVipMsgView, StoreMsgAdapter.OnDelMessageListener {

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.rl_title_more)
    RelativeLayout rl_title_more;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.tv_vip_list)
    TextView tv_vip_list;

    private VipMsgPresenter vipMsgPresenter;
    private StoreMsgAdapter msgAdapter;
    private List<StoreMsgEntity.StoreMsg> storeMsgList;
    private boolean isGetVip;
    private int currentDelType;
    private String h5Url;

    @Override
    protected int getLayoutId() {
        return R.layout.act_store_msg;
    }

    public static void startAct(Context context, String h5Url, boolean isVip) {
        Intent intent = new Intent(context, StoreMsgActivity.class);
        intent.putExtra("isVip", isVip);
        intent.putExtra("url", h5Url);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        isGetVip = getIntent().getBooleanExtra("isVip", false);
        h5Url = getIntent().getStringExtra("url");

        vipMsgPresenter = new VipMsgPresenter(this, this);
        if (isGetVip) {
            tv_title.setText(getStringResouce(R.string.vip_msg));
            vipMsgPresenter.getVipMessageList(true);
            tv_vip_list.setVisibility(View.VISIBLE);
        } else {
            tv_title.setText(getStringResouce(R.string.order_msg));
            vipMsgPresenter.getOrderMessageList(true);
            tv_vip_list.setVisibility(View.VISIBLE);
        }
        storeMsgList = new ArrayList<>();

        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);

        recycler_list.setLayoutManager(new LinearLayoutManager(this));

        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText("暂无数据")
                .setButtonText(null);
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

        rl_title_more.setOnClickListener(v -> {
            quick_actions.setVisibility(View.VISIBLE);
            quick_actions.message();
        });

        tv_vip_list.setOnClickListener(v -> {
            if (!isEmpty(h5Url)) {
                H5Act.startAct(this, h5Url, H5Act.MODE_SONIC);
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
    public void getStoreMsgs(List<StoreMsgEntity.StoreMsg> msgList, int page, int total, int delType) {
        refreshview.stopRefresh(true);
        currentDelType = delType;
        if (page == 1) {
            storeMsgList.clear();
        }

        if (!isEmpty(msgList)) {
            storeMsgList.addAll(msgList);
        }
        if (msgAdapter == null) {
            msgAdapter = new StoreMsgAdapter(this, storeMsgList, isGetVip);
            msgAdapter.setOnDelMessageListener(this);
            recycler_list.setAdapter(msgAdapter);
        }
        msgAdapter.notifyDataSetChanged();

        if (page == 1 && isEmpty(storeMsgList)) {
            nei_empty.setVisibility(View.VISIBLE);
            recycler_list.setVisibility(View.GONE);
        } else {
            nei_empty.setVisibility(View.GONE);
            recycler_list.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getOrderMsgs(List<StoreMsgEntity.StoreMsg> msgList, int page, int total, int delType) {
        refreshview.stopRefresh(true);
        currentDelType = delType;
        if (page == 1) {
            storeMsgList.clear();
        }

        if (!isEmpty(msgList)) {
            storeMsgList.addAll(msgList);
        }

        if (msgAdapter == null) {
            msgAdapter = new StoreMsgAdapter(this, storeMsgList, isGetVip);
            msgAdapter.setOnDelMessageListener(this);
            recycler_list.setAdapter(msgAdapter);
        }
        msgAdapter.notifyDataSetChanged();

        if (page == 1 && isEmpty(storeMsgList)) {
            nei_empty.setVisibility(View.VISIBLE);
            recycler_list.setVisibility(View.GONE);
        } else {
            nei_empty.setVisibility(View.GONE);
            recycler_list.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void delSuccess(String msgId) {
        for (int i = 0; i < storeMsgList.size(); i++) {
            if (msgId.equals(storeMsgList.get(i).id)) {
                storeMsgList.remove(i);
                break;
            }
        }
        msgAdapter.notifyDataSetChanged();
        if (isEmpty(storeMsgList)) {
            nei_empty.setVisibility(View.VISIBLE);
            recycler_list.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDel(int position) {
        StoreMsgEntity.StoreMsg storeMsg = storeMsgList.get(position);
        vipMsgPresenter.deleteMessage(String.valueOf(currentDelType), storeMsg.id);
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
    }
}
