package com.shunlian.app.newchat.util;

import android.content.Context;

import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.presenter.AllMessageCountPresenter;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IMessageCountView;

import java.util.Observable;

/**
 * Created by Administrator on 2018/4/9.
 */

public class MessageCountManager extends Observable implements IMessageCountView {
    private static MessageCountManager manager;
    private AllMessageCountPresenter mPresenter;
    private static Context mContext;
    private OnGetMessageListener mListener;
    private boolean isLoad;
    private AllMessageCountEntity allMessageCountEntity;

    public static MessageCountManager getInstance(Context context) {
        mContext = context;
        if (manager == null) {
            synchronized (MessageCountManager.class) {
                if (manager == null) {
                    manager = new MessageCountManager();
                }
            }
        }
        return manager;
    }

    public MessageCountManager() {
        mPresenter = new AllMessageCountPresenter(mContext, this);
    }

    public void initData() {
        mPresenter.messageAllCount();
    }

    public void upDateMessageCount() {
        mPresenter.messageAllCount();
    }

    @Override
    public void getMessageCount(AllMessageCountEntity messageCountEntity) {
        isLoad = true;

        allMessageCountEntity = messageCountEntity;

        if (mListener == null) {
            mListener.OnLoadSuccess(messageCountEntity);
        }
    }

    @Override
    public void getMessageCountFail() {
        if (mListener == null) {
            mListener.OnLoadFail();
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public void setOnGetMessageListener(OnGetMessageListener messageListener) {
        mListener = messageListener;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setAllMessageCountEntity(AllMessageCountEntity messageCountEntity) {
        this.allMessageCountEntity = messageCountEntity;
    }

    public AllMessageCountEntity getAllMessageCountEntity() {
        return allMessageCountEntity;
    }

    public int getAll_msg() {
        return allMessageCountEntity.all_msg;
    }

    public void setAll_msg(int all_msg) {
        this.allMessageCountEntity.all_msg = all_msg;
    }

    public int getSys_msg() {
        return allMessageCountEntity.sys_msg;
    }

    public void setSys_msg(int sys_msg) {
        this.allMessageCountEntity.sys_msg = sys_msg;
    }

    public int getStore_msg() {
        return allMessageCountEntity.store_msg;
    }

    public void setStore_msg(int store_msg) {
        this.allMessageCountEntity.store_msg = store_msg;
    }

    public int getSys_notice_msg() {
        return allMessageCountEntity.sys_notice_msg;
    }

    public void setSys_notice_msg(int sys_notice_msg) {
        this.allMessageCountEntity.sys_notice_msg = sys_notice_msg;
    }

    public int getDiscover_msg() {
        return allMessageCountEntity.discover_msg;
    }

    public void setDiscover_msg(int discover_msg) {
        this.allMessageCountEntity.discover_msg = discover_msg;
    }

    public int getOrder_notice_msg() {
        return allMessageCountEntity.order_notice_msg;
    }

    public void setOrder_notice_msg(int order_notice_msg) {
        this.allMessageCountEntity.order_notice_msg = order_notice_msg;
    }

    public int getVip_notice_msg() {
        return allMessageCountEntity.vip_notice_msg;
    }

    public void setVip_notice_msg(int vip_notice_msg) {
        this.allMessageCountEntity.vip_notice_msg = vip_notice_msg;
    }

    public int getDiscover_topic_msg() {
        return allMessageCountEntity.discover_topic_msg;
    }

    public void setDiscover_topic_msg(int discover_topic_msg) {
        this.allMessageCountEntity.discover_topic_msg = discover_topic_msg;
    }

    public int getDiscover_comment_msg() {
        return allMessageCountEntity.discover_comment_msg;
    }

    public void setDiscover_comment_msg(int discover_comment_msg) {
        this.allMessageCountEntity.discover_comment_msg = discover_comment_msg;
    }

    public int getCustom_msg() {
        return allMessageCountEntity.custom_msg;
    }

    public void setCustom_msg(int custom_msg) {
        this.allMessageCountEntity.custom_msg = custom_msg;
    }

    public interface OnGetMessageListener {
        void OnLoadSuccess(AllMessageCountEntity messageCountEntity);

        void OnLoadFail();
    }
}