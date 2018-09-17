package com.shunlian.app.ui.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.StoreSortAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.StoreCategoriesEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.StoreSortPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.StoreSortView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StoreSortAct extends BaseActivity implements View.OnClickListener, StoreSortView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.rv_sort)
    RecyclerView rv_sort;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.mrlayout_all)
    MyRelativeLayout mrlayout_all;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    private MessageCountManager messageCountManager;
    private String storeId;
    private StoreSortPresenter storeSortPresenter;
    private StoreSortAdapter storeSortAdapter;

    public static void startAct(Context context, String storeId, boolean isStore) {
        Intent intent = new Intent(context, StoreSortAct.class);
        intent.putExtra("storeId", storeId);//店铺id
        intent.putExtra("isStore", isStore);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActForResult(Activity activity, String storeId, int requestCode) {
        Intent intent = new Intent(activity, StoreSortAct.class);
        intent.putExtra("storeId", storeId);//店铺id
        activity.startActivityForResult(intent, requestCode);
    }

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.Store();
    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(baseAct);
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

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_sort;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mrlayout_all:
                StoreSearchAct.startAct(this, storeId,"","",getStringResouce(R.string.personal_quanbu));
                finish();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mrlayout_all.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        storeId = getIntent().getStringExtra("storeId");
        storeSortPresenter = new StoreSortPresenter(this, this, storeId);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void introduceInfo(StoreCategoriesEntity storeCategoriesEntity) {
        LinearLayoutManager firstManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_sort.setLayoutManager(firstManager);
        storeSortAdapter = new StoreSortAdapter(this, false, storeCategoriesEntity.data,storeId);
        rv_sort.setAdapter(storeSortAdapter);
        rv_sort.setNestedScrollingEnabled(false);
        storeSortAdapter.setOnItemClickListener((view, parentPosition, childPosition) -> {
            List<StoreCategoriesEntity.MData> mDataList = storeCategoriesEntity.data;
            String parentId = null, childId = null, keyword = null;
            if (mDataList.get(parentPosition).children != null) {
                List<StoreCategoriesEntity.MData.Children> childrenList = mDataList.get(parentPosition).children;
                StoreCategoriesEntity.MData.Children children = childrenList.get(childPosition);
                parentId = children.parent_id;
                childId = children.id;
                keyword = children.name;
            }
            if (getIntent().getBooleanExtra("isStore", false)) {
                StoreSearchAct.startAct(baseAct, storeId,"",childId,keyword);
            } else {
                Intent intent = new Intent();
                intent.putExtra("parentSrc", parentId);
                intent.putExtra("childSrc", childId);
                intent.putExtra("keyword", keyword);
                setResult(RESULT_OK, intent);
            }
            finish();
        });
    }
}
