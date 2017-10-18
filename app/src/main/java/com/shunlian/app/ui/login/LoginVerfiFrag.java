package com.shunlian.app.ui.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.widget.ClearableEditText;
import com.shunlian.app.widget.PhoneTextWatcher;

/**
 * Created by Administrator on 2017/10/17.
 */

public class LoginVerfiFrag extends BaseFragment {
    private View rootView;
    private ClearableEditText edt_account;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_login_verification, container, false);
        initView();
        return rootView;
    }

    @Override
    protected void initData() {

    }

    private void initView() {
        edt_account = (ClearableEditText) rootView.findViewById(R.id.edt_account);
        edt_account.addTextChangedListener(new PhoneTextWatcher(edt_account));
    }
}
