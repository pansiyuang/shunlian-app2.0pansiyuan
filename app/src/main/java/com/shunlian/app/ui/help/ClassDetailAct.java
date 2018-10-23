package com.shunlian.app.ui.help;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.ClassDetailPresenter;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IClassDetailView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2018/5/4.
 */

public class ClassDetailAct extends H5X5Act implements IClassDetailView, MessageCountManager.OnGetMessageListener {


    private ClassDetailPresenter mPresenter;
    private MessageCountManager messageCountManager;

    public static void startAct(Context context, String url,String id, int mode) {
        Intent intentH5 = new Intent(context, ClassDetailAct.class);
        intentH5.putExtra("url", url);
        intentH5.putExtra("id", id);//课堂id
        intentH5.putExtra("mode", mode);
        intentH5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intentH5);
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

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        String id = mIntent.getStringExtra("id");
        mPresenter = new ClassDetailPresenter(this,this,id);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rl_title_more.setVisibility(View.VISIBLE);
        rl_title_more.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.rl_title_more:
                quick_actions.setVisibility(View.VISIBLE);
                if (mPresenter != null&&mPresenter.shareInfoParam!=null&&!isEmpty(mPresenter.shareInfoParam.shareLink)){
                    quick_actions.classDetailShare();
                    quick_actions.shareInfo(mPresenter.shareInfoParam);
                }else {
                    quick_actions.order();
                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
