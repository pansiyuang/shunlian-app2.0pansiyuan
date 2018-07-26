package com.shunlian.app.ui.new_login_register;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.wxapi.WXEntryActivity;

import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/7/20.
 */

public class LoginEntryAct extends BaseActivity {

    /***************登录条款************/
    public static final String TERMS_OF_SERVICE = "agreement/1";

    public static void startAct(Context context){
        context.startActivity(new Intent(context,LoginEntryAct.class));
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_loginentry;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
    }

    @OnClick(R.id.llayout_wechat_login)
    public void wechatLogin(){
        WXEntryActivity.startAct(this,"login",null);
        finish();
    }

    @OnClick(R.id.mbtn_login)
    public void mobileLogin(){
        RegisterAndBindingAct.startAct(this,RegisterAndBindingAct.FLAG_LOGIN,null,null);
        finish();
    }

    @OnClick(R.id.llayout_login_agreement)
    public void loginAgreement(){
        H5Act.startAct(this, InterentTools.H5_HOST
                + TERMS_OF_SERVICE,H5Act.MODE_SONIC);
    }
}