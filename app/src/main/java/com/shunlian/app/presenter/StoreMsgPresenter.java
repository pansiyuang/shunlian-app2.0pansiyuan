package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.MessageListEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IStoreMsgView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/8.
 */

public class StoreMsgPresenter extends BasePresenter<IStoreMsgView> {

    public StoreMsgPresenter(Context context, IStoreMsgView iView) {
        super(context, iView);
    }

    public void getStoreMessage() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<MessageListEntity>> baseEntityCall = getAddCookieApiService().getStoremMessage(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<MessageListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<MessageListEntity> entity) {
                super.onSuccess(entity);
                MessageListEntity messageListEntity = entity.data;
                List<MessageListEntity.Msg> msgList = messageListEntity.list;
                iView.getStoreMsgList(msgList);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    protected void initApi() {

    }
}
