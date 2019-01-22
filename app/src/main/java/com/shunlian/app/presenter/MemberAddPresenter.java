package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.bean.MemberTeacherEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IMemberCodePageView;
import com.shunlian.app.view.IMemberCodePageView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/11/5.
 */

public class MemberAddPresenter extends BasePresenter<IMemberCodePageView> {
    public MemberAddPresenter(Context context, IMemberCodePageView iView) {
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

    /**
     * 我的导师
     */
    public void codeTeacherDetail(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<MemberTeacherEntity>>
                baseEntityCall = getApiService().codeTeacherInfo(map);

        getNetData(true,baseEntityCall,new SimpleNetDataCallback
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
     * 邀请码详情
     * @param id
     */
    public void codeDetail(boolean intoPage,String id){
        Map<String,String> map = new HashMap<>();
        map.put("code",id);
        sortAndMD5(map);

        Call<BaseEntity<MemberCodeListEntity>>
                baseEntityCall = getApiService().codeInfo(map);

        getNetData(true,baseEntityCall,new SimpleNetDataCallback
                <BaseEntity<MemberCodeListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<MemberCodeListEntity> entity) {
                super.onSuccess(entity);
                iView.codeInfo(entity.data,null,intoPage);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.codeInfo(null, message,intoPage);
            }
        });
    }

    /**
     * 确认绑定
     * @param code
     */
    public void bindShareidAfter(String code){
        Map<String,String> map = new HashMap<>();
        map.put("code",code);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>>
                baseEntityCall = getApiService().bindShareidAfter(requestBody);

        getNetData(true,baseEntityCall,new SimpleNetDataCallback
                <BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.bindSuccess(code);
                Common.staticToasts(context, "绑定成功", R.mipmap.icon_common_duihao);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
            }
        });
    }

    public void setInfo(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> setinfo = getAddCookieApiService().setinfo(getRequestBody(map));
        getNetData(true, setinfo, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                if ("weixin".equals(key)){
                    iView.setWeixin(value);
                }
                Common.staticToast(entity.message);
            }
        });

    }
}
