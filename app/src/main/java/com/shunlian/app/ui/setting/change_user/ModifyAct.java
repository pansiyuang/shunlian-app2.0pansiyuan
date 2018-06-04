package com.shunlian.app.ui.setting.change_user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/6/1.
 */

public class ModifyAct extends BaseActivity {


    private boolean isSetPwd;
    private String mKey;
    private boolean isBind;
    private FragmentManager mFragmentManager;
    private static Map<String, BaseFragment> fragmentMap = new HashMap<>();
    private static final String[] flags = {"modify1", "modify2"};
    private boolean isCanBack = false;//是否可以返回 默认不可以
    private BaseFragment mModifyFrag1;//修改第一个界面
    private BaseFragment mModifyFrag2;//修改第二个界面
    private boolean mIsBinding = false;//当前状态是否绑定手机

    public static void startAct(Context context, boolean isBind, boolean isSetPwd, String key){
        Intent intent = new Intent(context,ModifyAct.class);
        intent.putExtra("isBind",isBind);
        intent.putExtra("isSetPwd",isSetPwd);
        intent.putExtra("key",key);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_modify;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        closeSideslip();

        isBind = getIntent().getBooleanExtra("isBind", false);
        isSetPwd = getIntent().getBooleanExtra("isSetPwd", false);
        mKey = getIntent().getStringExtra("key");

        mFragmentManager = getSupportFragmentManager();

        modifyOnePage(isBind,isSetPwd,mKey);
    }


    public void modifyOnePage(boolean isBind, boolean isSetPwd, String key){
        isCanBack = false;
        mIsBinding = isBind;
        if (mModifyFrag1 == null) {
            mModifyFrag1 = fragmentMap.get(flags[0]);
            if (mModifyFrag1 == null) {
                mModifyFrag1 = new ChangeUserFrag();
                Bundle bundle = new Bundle();
                bundle.putString("key",mKey);
                bundle.putBoolean("isBind",isBind);
                bundle.putBoolean("isSetPwd",isSetPwd);
                mModifyFrag1.setArguments(bundle);
                fragmentMap.put(flags[0], mModifyFrag1);
            }else {
                if (mModifyFrag1 instanceof ChangeUserFrag){
                    ((ChangeUserFrag)mModifyFrag1).setArgument(isBind,isSetPwd,key);
                }
            }
        }else {
            if (mModifyFrag1 instanceof ChangeUserFrag){
                ((ChangeUserFrag)mModifyFrag1).setArgument(isBind,isSetPwd,key);
            }
        }
        switchContent(mModifyFrag1);
    }

    public void modifyTwoPage(int state,String mobile,String key){
        isCanBack = true;
        if (mModifyFrag2 == null) {
            mModifyFrag2 = fragmentMap.get(flags[1]);
            if (mModifyFrag2 == null) {
                mModifyFrag2 = new SettingPwdFrag();
                Bundle bundle = new Bundle();
                bundle.putInt("state", state);
                bundle.putString("mobile", mobile);
                bundle.putString("key", key);
                mModifyFrag2.setArguments(bundle);
                fragmentMap.put(flags[1], mModifyFrag2);
            }else {
                if (mModifyFrag2 instanceof SettingPwdFrag){
                    ((SettingPwdFrag)mModifyFrag2).setArgument(state,mobile,key);
                }
            }
        }else {
            if (mModifyFrag2 instanceof SettingPwdFrag){
                ((SettingPwdFrag)mModifyFrag2).setArgument(state,mobile,key);
            }
        }
        switchContent(mModifyFrag2);
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
            if (isCanBack || mIsBinding) {
                modifyOnePage(isBind,isSetPwd,mKey);
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
