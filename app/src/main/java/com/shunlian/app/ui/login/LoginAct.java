package com.shunlian.app.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.register.RegisterAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/17.
 */

public class LoginAct extends BaseActivity {

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @BindView(R.id.frame_content)
    FrameLayout frame_content;

    @BindView(R.id.rlayout_title)
    RelativeLayout rlayout_title;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private ViewPagerAdapter mAdapter;
    //密码
    private LoginPswFrag pswFrag;
    //验证码
    private LoginVerfiFrag verfiFrag;

    private List<Fragment> fragmentList;
    private FragmentManager fragmentManager;
    private boolean isCanBack = false;//是否可以返回 默认不可以
    private InputVerfiCodeFrag inputVerfiCodeFrag;
    /***************登录条款************/
    public static final String TERMS_OF_SERVICE = "agreement/1";

    public static void startAct(Context context) {
        Intent intent = new Intent(context, LoginAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(v->close());
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        closeSideslip();
        initView();
        setHeader();
        //返回键扩大点击范围
        int i = TransformUtil.dip2px(this, 20);
        TransformUtil.expandViewTouchDelegate(miv_close,i,i,i,i);
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();

        fragmentList = new ArrayList<>();

        pswFrag = new LoginPswFrag();
        verfiFrag = new LoginVerfiFrag();

        fragmentList.add(verfiFrag);

        fragmentList.add(pswFrag);

        mAdapter = new ViewPagerAdapter(fragmentManager, fragmentList);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void onRestart() {
        if (verfiFrag != null) {
            verfiFrag.refreshCode();
        }
        super.onRestart();
    }

    @OnClick(R.id.mtv_register)
    public void goRegister(){
        RegisterAct.startAct(this,RegisterAct.NEW_USER,null);
    }


    public void close(){
        if (isCanBack) {
            backPage();
        }else {
            //Common.goGoGo(this,"mainPage");
            finish();
        }
    }
    /**
     * 验证码登录
     */
    public void verificationCodeLogin(){
        visible(mViewPager,rlayout_title);
        gone(frame_content);
        if (mViewPager != null)
            mViewPager.setCurrentItem(0);
    }

    /**
     * 密码登录
     */
    public void pwdLogin(){
        if (mViewPager != null)
            mViewPager.setCurrentItem(1);
    }

    /**
     * 短信验证码界面
     * @param phoneNum
     * @param vCode
     */
    public void inputSmsCodePage(String phoneNum, String vCode){
        isCanBack = true;
        gone(mViewPager,rlayout_title);
        visible(frame_content);
        inputVerfiCodeFrag = new InputVerfiCodeFrag();
        Bundle bundle = new Bundle();
        bundle.putString("phoneNum", phoneNum);
        bundle.putString("vCode", vCode);
        inputVerfiCodeFrag.setArguments(bundle);
        fragmentManager.beginTransaction().add(R.id.frame_content, inputVerfiCodeFrag).commit();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (isCanBack) {
                backPage();
                return true;
            }else {
                //Common.goGoGo(this,"mainPage");
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void backPage(){
        visible(mViewPager,rlayout_title);
        gone(frame_content);
        fragmentManager.beginTransaction().remove(inputVerfiCodeFrag).commit();
        isCanBack = false;
    }

    /**
     * 微信登录
     */
    public void WXLogin(){
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        api.registerApp(Constant.WX_APP_ID);

        int wxSdkVersion = api.getWXAppSupportAPI();
        if (wxSdkVersion >= Constant.TIMELINE_SUPPORTED_VERSION){
            // send oauth request
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = SharedPrefUtil.getCacheSharedPrf("X-Device-ID",
                    "744D9FC3-5DBD-3EDD-A589-56D77BDB0E5D");
            api.sendReq(req);
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
