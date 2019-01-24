package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.bean.WXLoginEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.ui.my_profit.SexSelectAct;
import com.shunlian.app.ui.new3_login.New3LoginAct;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/7/27.
 */

public class TestWXLoginPresenter extends BasePresenter {

    public TestWXLoginPresenter(Context context, IView iView) {
        super(context, iView);
        //initApi();
        wxLoginV2();
    }

    public void wxLoginV2(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<LoginFinishEntity>> baseEntityCall = getApiService().checkTestV2(map);

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<LoginFinishEntity>>(){
            @Override
            public void onSuccess(BaseEntity<LoginFinishEntity> entity) {
                super.onSuccess(entity);

                LoginFinishEntity wxLoginEntity = entity.data;
                String unique_sign = wxLoginEntity.unique_sign;
                //String mobile = wxLoginEntity.mobile;
                String member_id = wxLoginEntity.member_id;
                String status = wxLoginEntity.status;

                if ("1".equals(status)){//登录成功
                    loginSuccess(entity.data);
                }else{
                    New3LoginAct.LoginConfig config = new New3LoginAct.LoginConfig();
                    config.login_mode = New3LoginAct.LoginConfig.LOGIN_MODE.SMS_TO_LOGIN;
                    config.unique_sign = unique_sign;
                    config.member_id = member_id;
                    config.status = status;
                    New3LoginAct.startAct(context,config);
                    ((Activity)context).finish();
                }
            }
        });
    }


    private void loginSuccess(LoginFinishEntity wxLoginEntity) {
        SharedPrefUtil.saveSharedUserString("token", wxLoginEntity.token);
        SharedPrefUtil.saveSharedUserString("refresh_token", wxLoginEntity.refresh_token);
        SharedPrefUtil.saveSharedUserString("member_id", wxLoginEntity.member_id);
        SharedPrefUtil.saveSharedUserString("avatar", wxLoginEntity.avatar);
        SharedPrefUtil.saveSharedUserString("nickname", wxLoginEntity.nickname);
        SharedPrefUtil.saveSharedUserString("plus_role", wxLoginEntity.plus_role);
        if (!isEmpty(wxLoginEntity.code))
            SharedPrefUtil.saveSharedUserString("invite_code", wxLoginEntity.code);
        SensorsDataAPI.sharedInstance().login(SharedPrefUtil.getSharedUserString("member_id", ""));
        CrashReport.setUserId(wxLoginEntity.member_id);
        if (wxLoginEntity.tag != null)
            SharedPrefUtil.saveSharedUserStringss("tags", new HashSet<>(wxLoginEntity.tag));
        JpushUtil.setJPushAlias();
        //通知登录成功
        DefMessageEvent event = new DefMessageEvent();
        event.loginSuccess = true;
        EventBus.getDefault().post(event);

        EasyWebsocketClient.getInstance(context).initChat(); //初始化聊天
        MessageCountManager.getInstance(context).initData();

        if (!"1".equals(wxLoginEntity.is_tag)){
            SexSelectAct.startAct(context);
        }
        ((Activity)context).finish();
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
                //status = "3";
                if ("2".equals(status)) {//绑定手机号不需要推荐人
                    /*RegisterAndBindingAct.startAct(context,
                            RegisterAndBindingAct.FLAG_BIND_MOBILE, null,unique_sign,member_id);*/


                    New3LoginAct.LoginConfig config = new New3LoginAct.LoginConfig();
                    config.login_mode = New3LoginAct.LoginConfig.LOGIN_MODE.SMS_TO_LOGIN;
                    config.status = status;
                    config.unique_sign = unique_sign;
                    config.member_id = member_id;
                    New3LoginAct.startAct(context,config);

                } else if ("1".equals(status)) {

                    loginSuccess(entity, wxLoginEntity);

                } else if ("0".equals(status) || "3".equals(status)){//绑定手机号 需要推荐人

                    /*RegisterAndBindingAct.startAct(context,
                            RegisterAndBindingAct.FLAG_BIND_MOBILE_ID,null,unique_sign,member_id);*/


                    New3LoginAct.LoginConfig config = new New3LoginAct.LoginConfig();
                    config.login_mode = New3LoginAct.LoginConfig.LOGIN_MODE.SMS_TO_LOGIN;
                    config.status = status;
                    config.unique_sign = unique_sign;
                    config.member_id = member_id;
                    New3LoginAct.startAct(context,config);

                }else if ("4".equals(status)){//绑定推荐人
                    /*RegisterAndBindingAct.startAct(context,
                            RegisterAndBindingAct.FLAG_BIND_ID,mobile,unique_sign,member_id);*/


                    New3LoginAct.LoginConfig config = new New3LoginAct.LoginConfig();
                    config.login_mode = New3LoginAct.LoginConfig.LOGIN_MODE.BIND_INVITE_CODE;
                    config.unique_sign = unique_sign;
                    config.member_id = member_id;
                    config.mobile = mobile;
                    config.isMobileRegister = true;
                    New3LoginAct.startAct(context,config);

                }

                ((Activity)context).finish();
            }
        });
    }

    private void loginSuccess(BaseEntity<WXLoginEntity> entity, WXLoginEntity wxLoginEntity) {
        //Common.staticToast(entity.message);

        //登陆成功啦
        SharedPrefUtil.saveSharedUserString("token", wxLoginEntity.token);
        SharedPrefUtil.saveSharedUserString("avatar", wxLoginEntity.avatar);
        SharedPrefUtil.saveSharedUserString("nickname", wxLoginEntity.nickname);
        SharedPrefUtil.saveSharedUserString("plus_role", wxLoginEntity.plus_role);
        SharedPrefUtil.saveSharedUserString("refresh_token", wxLoginEntity.refresh_token);
        SharedPrefUtil.saveSharedUserString("member_id", wxLoginEntity.member_id);
        if (!isEmpty(wxLoginEntity.code))
            SharedPrefUtil.saveSharedUserString("invite_code", wxLoginEntity.code);
        SensorsDataAPI.sharedInstance().login(SharedPrefUtil.getSharedUserString("member_id", ""));
        CrashReport.setUserId(wxLoginEntity.member_id);
        if (wxLoginEntity.tag != null)
            SharedPrefUtil.saveSharedUserStringss("tags", new HashSet<>(wxLoginEntity.tag));
        JpushUtil.setJPushAlias();
        //通知登录成功
        DefMessageEvent event = new DefMessageEvent();
        event.loginSuccess = true;
        EventBus.getDefault().post(event);

        EasyWebsocketClient.getInstance(context).initChat(); //初始化聊天
        MessageCountManager.getInstance(context).initData();

        if (!"1".equals(wxLoginEntity.is_tag)){
            SexSelectAct.startAct(context);
        }
        ((Activity)context).finish();
    }
}
