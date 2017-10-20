package com.shunlian.app.ui.login;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.ClearableEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.PhoneTextWatcher;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/17.
 */

public class LoginVerfiFrag extends BaseFragment implements PhoneTextWatcher.OnInputCompleteListener, View.OnClickListener {
    private View rootView;
    private PhoneTextWatcher phoneTextWatcher;

    @BindView(R.id.edt_verifi)
    EditText edt_verifi;

    @BindView(R.id.edt_account)
    ClearableEditText edt_account;

    @BindView(R.id.iv_verifi)
    MyImageView iv_verifi;


    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_login_verification, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        edt_verifi.setEnabled(false);
        super.initViews();
    }

    @Override
    protected void initData() {
        GlideUtils.getInstance().downPicture(getActivity(), InterentTools.HTTPADDR + "member/Common/vcode", iv_verifi);
    }

    @Override
    protected void initListener() {
        super.initListener();
        phoneTextWatcher = new PhoneTextWatcher(edt_account);
        edt_account.addTextChangedListener(phoneTextWatcher);
        phoneTextWatcher.setOnInputListener(this);

        edt_verifi.addTextChangedListener(new MyTextWatch());
        iv_verifi.setOnClickListener(this);

    }

    @Override
    public void onInput() {//输入手机号码完成
        edt_verifi.setEnabled(true);
        edt_verifi.requestFocus();
        edt_verifi.setSelection(edt_verifi.getText().length());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_verifi:
                GlideUtils.getInstance().downPicture(getActivity(), InterentTools.HTTPADDR + "member/Common/vcode", iv_verifi);
                break;
        }
    }


    private class MyTextWatch implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String verifiCode = edt_verifi.getText().toString();
            if (verifiCode.length() == 4) {
                //输入完验证码跳转逻辑
                String phoneNum = edt_account.getText().toString();
                InputVerfiCodeAct.startAct(getActivity(), phoneNum);
            }
        }
    }
}
