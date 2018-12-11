package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.WXLoginEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.ui.my_profit.SexSelectAct;
import com.shunlian.app.ui.new3_login.New3LoginAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        EventBus.getDefault().register(this);
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
        EventBus.getDefault().unregister(this);
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

    private DispachJump mJump;

    @Subscribe(sticky = true)
    public void eventBus(DispachJump jump) {
        mJump = jump;
        /*ObjectMapper om = new ObjectMapper();
        try {
            String s = om.writeValueAsString(jump);
            LogUtil.zhLogW("==eventBus======"+s);
            SharedPrefUtil.saveCacheSharedPrf("wx_jump", s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
    }

    /*private void handleJump() {
        String jumpType = SharedPrefUtil.getCacheSharedPrf("wx_jump", "");
        LogUtil.zhLogW("==handleJump======"+jumpType);
        ObjectMapper om = new ObjectMapper();
        try {
            DispachJump dispachJump = om.readValue(jumpType, DispachJump.class);
            if (dispachJump != null) {
                Common.goGoGo(context, dispachJump.jumpType,dispachJump.items);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void loginSuccess(BaseEntity<WXLoginEntity> entity, WXLoginEntity wxLoginEntity) {
        //Common.staticToast(entity.message);

        //登陆成功啦
        SharedPrefUtil.saveSharedUserString("token", wxLoginEntity.token);
        SharedPrefUtil.saveSharedUserString("avatar", wxLoginEntity.avatar);
        SharedPrefUtil.saveSharedUserString("nickname", wxLoginEntity.nickname);
        SharedPrefUtil.saveSharedUserString("plus_role", wxLoginEntity.plus_role);
        SharedPrefUtil.saveSharedUserString("refresh_token", wxLoginEntity.refresh_token);
        SharedPrefUtil.saveSharedUserString("member_id", wxLoginEntity.member_id);
        SensorsDataAPI.sharedInstance().login(SharedPrefUtil.getSharedUserString("member_id", ""));
        CrashReport.setUserId(wxLoginEntity.member_id);
        if (wxLoginEntity.tag != null)
            SharedPrefUtil.saveSharedUserStringss("tags", new HashSet<>(wxLoginEntity.tag));
        JpushUtil.setJPushAlias();
        //通知登录成功
        DefMessageEvent event = new DefMessageEvent();
        event.loginSuccess = true;
        EventBus.getDefault().post(event);

//        if (Constant.JPUSH != null && !"login".equals(Constant.JPUSH.get(0))) {
//            Common.goGoGo(context, Constant.JPUSH.get(0), Constant.JPUSH.get(1), Constant.JPUSH.get(2)
//                    ,Constant.JPUSH.get(3),Constant.JPUSH.get(4),Constant.JPUSH.get(5),Constant.JPUSH.get(6),Constant.JPUSH.get(7)
//                    ,Constant.JPUSH.get(8),Constant.JPUSH.get(9),Constant.JPUSH.get(10),Constant.JPUSH.get(11),Constant.JPUSH.get(12));
//        }
        EasyWebsocketClient.getInstance(context).initChat(); //初始化聊天
        MessageCountManager.getInstance(context).initData();

        if (mJump != null){
            Common.goGoGo(context,mJump.jumpType,mJump.items);
        }
        //handleJump();

        if (!"1".equals(wxLoginEntity.is_tag)){
            SexSelectAct.startAct(context);
        }
        ((Activity)context).finish();
    }
}
