package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.login.FindPswFrag;
import com.shunlian.mylibrary.KeyboardPatch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/31.
 */

public class RegisterAct extends BaseActivity {
    /*****新用户注册******/
    public static final int NEW_USER = 0;
    /**未绑定老用户*/
    public static final int UNBIND_OLD_USER = 1;
    /**未绑定新用户*/
    public static final int UNBIND_NEW_USER = 2;
    /**未绑定有上级用户*/
    public static final int UNBIND_SUPERIOR_USER = 3;
    /******找回密码**********/
    public static final int FIND_PWD = 4;

    private int mState;
    private FragmentManager mFragmentManager;
    private BaseFragment mRegisterFrag1;//注册第一个界面
    private BaseFragment mRegisterFrag2;//注册第二个界面
    private boolean isCanBack = false;//是否可以返回 默认不可以
    private static Map<String, BaseFragment> fragmentMap = new HashMap<>();
    private static final String[] flags = {"register1", "register2"};
    private String mUniqueSign;

    public static void startAct(Context context, int state,String unique_sign){
        Intent intent = new Intent(context,RegisterAct.class);
        intent.putExtra("state",state);
        intent.putExtra("unique_sign",unique_sign);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_register;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        immersionBar.statusBarColor(R.color.white)
                .keyboardEnable(true)
                .statusBarDarkFont(true, 0.2f)
                .init();
        KeyboardPatch.patch(this).enable();
        closeSideslip();

        mState = getIntent().getIntExtra("state",NEW_USER);
        mUniqueSign = getIntent().getStringExtra("unique_sign");
        mFragmentManager = getSupportFragmentManager();

        dispachRegister();
    }

    /**
     * 根据state 分发注册状态
     */
    private void dispachRegister(){
        switch (mState){
            case UNBIND_OLD_USER:
            case UNBIND_NEW_USER:
            case UNBIND_SUPERIOR_USER:
                if (mRegisterFrag1 == null) {
                    mRegisterFrag1 = fragmentMap.get(flags[0]);
                    if (mRegisterFrag1 == null) {
                        mRegisterFrag1 = new BindingPhoneFrag();
                        Bundle bundle = new Bundle();
                        bundle.putInt("state",mState);
                        bundle.putString("unique_sign",mUniqueSign);
                        mRegisterFrag1.setArguments(bundle);
                        fragmentMap.put(flags[0], mRegisterFrag1);
                    }
                }
                break;
            case FIND_PWD:
                if (mRegisterFrag1 == null) {
                    mRegisterFrag1 = fragmentMap.get(flags[0]);
                    if (mRegisterFrag1 == null) {
                        mRegisterFrag1 = new FindPswFrag();
                        fragmentMap.put(flags[0], mRegisterFrag1);
                    }
                }
                break;
            case NEW_USER:
            default:
                if (mRegisterFrag1 == null) {
                    mRegisterFrag1 = fragmentMap.get(flags[0]);
                    if (mRegisterFrag1 == null) {
                        mRegisterFrag1 = new RegisterOneFrag();
                        fragmentMap.put(flags[0], mRegisterFrag1);
                    }
                }
                break;

        }
        addRegisterOne();
    }

    public void addRegisterOne(){
        isCanBack = false;
        switchContent(mRegisterFrag1);
    }


    public void addRegisterTwo(String phone,String smsCode,String codeId,
                               String pictureCode,String unique_sign,String currentType){
        isCanBack = true;
        if (mRegisterFrag2 == null) {
            mRegisterFrag2 = fragmentMap.get(flags[1]);
            if (mRegisterFrag2 == null) {
                mRegisterFrag2 = new RegisterTwoFrag();
                Bundle bundle = new Bundle();
                bundle.putString("phone",phone);
                bundle.putString("smsCode",smsCode);
                bundle.putString("codeId",codeId);
                bundle.putString("unique_sign",unique_sign);
                bundle.putString("pictureCode",pictureCode);
                bundle.putString("type",currentType);
                mRegisterFrag2.setArguments(bundle);
                fragmentMap.put(flags[1], mRegisterFrag2);
            }else {
                if (mRegisterFrag2 instanceof RegisterTwoFrag){
                    ((RegisterTwoFrag)mRegisterFrag2).setArgument(phone,smsCode,
                            codeId,pictureCode,unique_sign,currentType);
                }
            }
        }else {
            if (mRegisterFrag2 instanceof RegisterTwoFrag){
                ((RegisterTwoFrag)mRegisterFrag2).setArgument(phone,smsCode,
                        codeId,pictureCode,unique_sign,currentType);
            }
        }
        switchContent(mRegisterFrag2);
    }

    public void switchContent(Fragment show) {
        if (show != null) {
            if (!show.isAdded()) {
                mFragmentManager.beginTransaction().remove(show)
                        .commitAllowingStateLoss();
                mFragmentManager.beginTransaction().add(R.id.frame_content, show)
                        .commitAllowingStateLoss();
            } else {
                mFragmentManager.beginTransaction().show(show).commitAllowingStateLoss();
            }
            if (fragmentMap != null && fragmentMap.size() > 0) {
                Set<String> keySet = fragmentMap.keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    BaseFragment baseFragment = fragmentMap.get(key);
                    if (show != baseFragment) {
                        if (baseFragment != null && baseFragment.isVisible()) {
                            mFragmentManager.beginTransaction().hide(baseFragment)
                                    .commitAllowingStateLoss();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (isCanBack) {
                addRegisterOne();
                return true;
            }else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        fragmentMap.clear();
        super.onDestroy();
    }
}
