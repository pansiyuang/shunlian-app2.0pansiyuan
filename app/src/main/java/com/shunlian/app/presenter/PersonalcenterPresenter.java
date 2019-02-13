package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.MemberTeacherEntity;
import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IPersonalView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/5.
 */

public class PersonalcenterPresenter extends BasePresenter<IPersonalView> {

    public PersonalcenterPresenter(Context context, IPersonalView iView) {
        super(context, iView);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }


    /**
     *
     * @param
     */
    public void getApiData(){
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<PersonalcenterEntity>> baseEntityCall = getApiService().personalcenter(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<PersonalcenterEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PersonalcenterEntity> entity) {
                super.onSuccess(entity);
                PersonalcenterEntity personalcenterEntity=entity.data;
                iView.getApiData(personalcenterEntity);
            }

        });
    }

    public void getUserId() {
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", "-1");
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().getUserId(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity = entity.data;
                iView.getUserId(commonEntity.user_id);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    /**
     * 我的导师
     */
    public void codeTeacherDetail(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<MemberTeacherEntity>>
                baseEntityCall = getApiService().codeTeacherInfo(map);

        getNetData(false,baseEntityCall,new SimpleNetDataCallback
                <BaseEntity<MemberTeacherEntity>>(){
            @Override
            public void onSuccess(BaseEntity<MemberTeacherEntity> entity) {
                super.onSuccess(entity);
                iView.teacherCodeInfo(entity.data);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.teacherCodeInfo(null);
            }
        });
    }


    /**
     *点击提醒
     */
    public void neverPop(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>>
                baseEntityCall = getApiService().neverPop(map);

        getNetData(false,baseEntityCall,new SimpleNetDataCallback
                <BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
            }
        });
    }


    @Override
    protected void initApi() {

    }

}
