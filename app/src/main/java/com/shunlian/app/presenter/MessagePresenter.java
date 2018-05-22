package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.entity.MessageListEntity;
import com.shunlian.app.newchat.entity.SystemMessageEntity;
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

        Call<BaseEntity<SystemMessageEntity>> baseEntityCall = getAddCookieApiService().getSystemMessage(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<SystemMessageEntity>>() {
            @Override
            public void onSuccess(BaseEntity<SystemMessageEntity> entity) {
                super.onSuccess(entity);
                SystemMessageEntity systemMessageEntity = entity.data;
                iView.getSysMessage(systemMessageEntity);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void getMessageList(boolean isLoad, String keyWord, String type) {
        Map<String, String> map = new HashMap<>();
        if (!isEmpty(keyWord)) {
            map.put("keyword", String.valueOf(keyWord));
        }
        if (!isEmpty(type)) {
            map.put("type", String.valueOf(type));
        }
        sortAndMD5(map);

        Call<BaseEntity<ChatMemberEntity>> baseEntityCall = getAddCookieApiService().getMessageList(map);
        getNetData(isLoad, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ChatMemberEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ChatMemberEntity> entity) {
                super.onSuccess(entity);
                ChatMemberEntity chatMemberEntity = entity.data;
                List<ChatMemberEntity.ChatMember> members = chatMemberEntity.list;
                iView.getMessageList(members);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void deleteMessage(String chatUserId) {
        Map<String, String> map = new HashMap<>();
        map.put("m_user_id", chatUserId);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().deleteMessage(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.delSuccess(entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }
}
