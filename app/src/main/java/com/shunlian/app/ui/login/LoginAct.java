package com.shunlian.app.ui.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */

public class LoginAct extends BaseActivity {
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private LoginPswFrag pswFrag;
    private LoginVerfiFrag verfiFrag;
    private List<Fragment> fragmentList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        pswFrag = new LoginPswFrag();
        verfiFrag = new LoginVerfiFrag();
        fragmentList.add(pswFrag);
        fragmentList.add(verfiFrag);
        mAdapter = new ViewPagerAdapter(fragmentManager, fragmentList);

        mViewPager.setAdapter(mAdapter);
    }
}
