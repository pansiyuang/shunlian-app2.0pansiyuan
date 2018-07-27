package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.WXLoginEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.new_login_register.RegisterAndBindingAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/7/27.
 */

public class TestWXLoginPresenter extends BasePresenter {

    public TestWXLoginPresenter(Context context, IView iView) {
        super(context, iView);
        initApi();
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<WXLoginEntity>> baseEntityCall = getApiService().checkTest(map);

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<WXLoginEntity>>(){
            @Override
            public void onSuccess(BaseEntity<WXLoginEntity> entity) {
                super.onSuccess(entity);
                WXLoginEntity wxLoginEntity = entity.data;
                String unique_sign = wxLoginEntity.unique_sign;
                String mobile = wxLoginEntity.mobile;
                String member_id = wxLoginEntity.member_id;
                String status = wxLoginEntity.status;
                if ("2".equals(status)) {//绑定手机号不需要推荐人

                    RegisterAndBindingAct.startAct(context,
                            RegisterAndBindingAct.FLAG_BIND_MOBILE, null,unique_sign,member_id);

                } else if ("1".equals(status)) {

                    Common.staticToast(entity.message);

                } else if ("0".equals(status) || "3".equals(status)){//绑定手机号 需要推荐人

                    RegisterAndBindingAct.startAct(context,
                            RegisterAndBindingAct.FLAG_BIND_MOBILE_ID,null,unique_sign,member_id);

                }else if ("4".equals(status)){//绑定推荐人

                    RegisterAndBindingAct.startAct(context,
                            RegisterAndBindingAct.FLAG_BIND_ID,mobile,unique_sign,member_id);
                }
            }
        });
    }
}
