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
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

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


    @OnClick(R.id.mtv_register)
    public void goRegister(){
        RegisterAct.startAct(this,RegisterAct.NEW_USER,null);
    }

    @OnClick(R.id.miv_close)
    public void close(){
        if (isCanBack) {
            backPage();
        }else {
            Common.goGoGo(this,"mainPage");
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
                Common.goGoGo(this,"mainPage");
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
}
