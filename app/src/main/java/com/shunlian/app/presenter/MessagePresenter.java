package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.MessageListEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IMessageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/8.
 */

public class MessagePresenter extends BasePresenter<IMessageView> {

    public MessagePresenter(Context context, IMessageView iView) {
        super(context, iView);
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

    public void getSystemMessage() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<MessageListEntity>> baseEntityCall = getAddCookieApiService().getSystemMessage(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<MessageListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<MessageListEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
                MessageListEntity messageListEntity = entity.data;
                List<MessageListEntity.Msg> msgList = messageListEntity.list;
                iView.getSysMessageList(msgList);
            }
        });
    }

}
