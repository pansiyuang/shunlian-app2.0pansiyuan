package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ICustomerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/19.
 */

public class CustomerPresenter extends BasePresenter<ICustomerView> {

    public CustomerPresenter(Context context, ICustomerView iView) {
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

    public void getUserList(boolean isLoad, String kid) {
        Map<String, String> map = new HashMap<>();
        map.put("kid", kid);
        sortAndMD5(map);

        Call<BaseEntity<ChatMemberEntity>> baseEntityCall = getAddCookieApiService().getUserList(map);
        getNetData(isLoad, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ChatMemberEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ChatMemberEntity> entity) {
                super.onSuccess(entity);
                ChatMemberEntity chatMemberEntity = entity.data;
                List<ChatMemberEntity.ChatMember> members = chatMemberEntity.list;
                iView.getUserList(members);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void getReception(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().getReception(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.getReception(entity.data.reception);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void setReception(String userId, int reception) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("reception", String.valueOf(reception));
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().setReception(getRequestBody(map));
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.setReception();
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

}
