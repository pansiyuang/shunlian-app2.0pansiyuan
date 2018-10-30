package com.shunlian.app.ui.new_login_register;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IView;
import com.shunlian.app.widget.MyTextView;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/7/20.
 */

public class LoginEntryAct extends BaseActivity implements IView{

    /***************登录条款************/
    public static final String TERMS_OF_SERVICE = "agreement/1";
    //private TestWXLoginPresenter presenter;

    @BindView(R.id.mtv_pwd_login)
    MyTextView mtv_pwd_login;

    public static void startAct(Context context){
        Intent intent = new Intent(context, LoginEntryAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
        WXLogin();
        /*if (presenter == null) {
            presenter = new TestWXLoginPresenter(this, this);
        }else {
            presenter.initApi();
        }*/
    }

    @OnClick(R.id.mbtn_login)
    public void mobileLogin(){
        RegisterAndBindingAct.startAct(this,
                RegisterAndBindingAct.FLAG_LOGIN,null,null,null);
        finish();
    }

    @OnClick(R.id.mtv_pwd_login)
    public void pwdLoginEntry(){
        RegisterAndBindingAct.startAct(this,
                RegisterAndBindingAct.FLAG_PWD_LOGIN,null,null,null);
        finish();
    }

    @OnClick(R.id.llayout_login_agreement)
    public void loginAgreement(){
        H5X5Act.startAct(this, InterentTools.H5_HOST
                + TERMS_OF_SERVICE,H5X5Act.MODE_SONIC);
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    /**
     * 微信登录
     */
    public void WXLogin(){
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        api.registerApp(Constant.WX_APP_ID);

        int wxSdkVersion = api.getWXAppSupportAPI();
        if (wxSdkVersion >= Constant.TIMELINE_SUPPORTED_VERSION){
            try {
                // send oauth request
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = SharedPrefUtil.getCacheSharedPrf("X-Device-ID",
                        "744D9FC3-5DBD-3EDD-A589-56D77BDB0E5D");
                api.sendReq(req);
            }catch (Exception e){

            }
            finish();
        }else if (wxSdkVersion == 0) {
            Common.staticToast("请先安装微信");
            finish();
        } else {
            Common.staticToast("当前微信版本过低，请更新后再试");
            finish();
        }
    }

}
