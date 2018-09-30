package com.shunlian.app.wxapi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.shunlian.app.utils.Constant;
import com.shunlian.mylibrary.OSUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhanghe on 2018/9/27.
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler{

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
//        LogUtil.zhLogW(baseResp.getType()+"onPayFinish,errCode="+baseResp.errCode);
        if(baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            EventBus.getDefault().post(baseResp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && OSUtils.isMIUI()){
                finishAndRemoveTask();
            }else {
                finish();
            }
        }
    }
}
