package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.AdUserEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.InviteLogUserEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.INewUserPageView;
import com.shunlian.app.view.InvitationView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class NewInvitationPresenter  extends BasePresenter<InvitationView> {

    public NewInvitationPresenter(Context context, InvitationView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    public void getUserList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<InviteLogUserEntity>> setinfo = getAddCookieApiService().inviteLog(map);
        getNetData(true, setinfo, new SimpleNetDataCallback<BaseEntity<InviteLogUserEntity>>() {
            @Override
            public void onSuccess(BaseEntity<InviteLogUserEntity> entity) {
                super.onSuccess(entity);
                iView.refreshFinish(entity.data.list,entity.data);
            }
        });
    }

}
