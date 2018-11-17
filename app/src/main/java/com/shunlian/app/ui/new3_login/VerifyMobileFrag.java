package com.shunlian.app.ui.new3_login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;

/**
 * Created by zhanghe on 2018/11/17.
 * 验证手机号
 */

public class VerifyMobileFrag extends BaseFragment {

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_verify_mobile, null);
        return view;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

    }
}
