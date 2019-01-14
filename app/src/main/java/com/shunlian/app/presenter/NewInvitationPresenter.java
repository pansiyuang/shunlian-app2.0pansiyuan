package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.InviteLogUserEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.InvitationView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class NewInvitationPresenter  extends BasePresenter<InvitationView> {

    private int currentPage = 1;
    protected boolean isLoading = false;//是否正在加载
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

    public void getUserList(boolean isLoad) {
        Map<String, String> map = new HashMap<>();
        map.put("page",currentPage+"");
        map.put("pageSize",5+"");
        sortAndMD5(map);
        Call<BaseEntity<InviteLogUserEntity>> setinfo = getAddCookieApiService().inviteLog(map);
        getNetData(true, setinfo, new SimpleNetDataCallback<BaseEntity<InviteLogUserEntity>>() {
            @Override
            public void onSuccess(BaseEntity<InviteLogUserEntity> entity) {
                super.onSuccess(entity);
                if(entity.data==null){
                    return;
                }
                iView.refreshFinish(entity.data.list, entity.data, currentPage);
//                if(entity.data.list==null){
//                    allPage = currentPage;
//                    return;
//                }
//                if(entity.data.list.size()>=0&&entity.data.list.size()<5) {
//                      allPage = currentPage;
//                 }else{
//                      currentPage++;
//                }
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getUserList(false);
            }
        }
    }
}
