package com.shunlian.app.ui.login;

import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.RegisterOnePresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.register.RegisterOneAct;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.view.IRegisterOneView;
import com.shunlian.app.widget.ClearableEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.PhoneTextWatcher;
import com.shunlian.app.wxapi.WXEntryActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/17.
 */

public class LoginVerfiFrag extends BaseFragment implements PhoneTextWatcher.OnInputCompleteListener, View.OnClickListener, IRegisterOneView {
    private View rootView;
    private PhoneTextWatcher phoneTextWatcher;
    private RegisterOnePresenter onePresenter;

    @BindView(R.id.edt_verifi)
    EditText edt_verifi;

    @BindView(R.id.edt_account)
    ClearableEditText edt_account;

    @BindView(R.id.iv_verifi)
    MyImageView iv_verifi;

    @BindView(R.id.tv_new_regist)
    TextView tv_new_regist;

    @BindView(R.id.tv_wx_login)
    TextView tv_wx_login;

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
        setEdittextFocusable(true,edt_verifi,edt_account);
        onePresenter = new RegisterOnePresenter(getActivity(), this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        phoneTextWatcher = new PhoneTextWatcher(edt_account);
        edt_account.addTextChangedListener(phoneTextWatcher);
        phoneTextWatcher.setOnInputListener(this);
        tv_new_regist.setOnClickListener(this);

        edt_verifi.addTextChangedListener(new MyTextWatch());
        iv_verifi.setOnClickListener(this);
        tv_wx_login.setOnClickListener(this);

    }

    @Override
    public void onInput() {//输入手机号码完成
        edt_verifi.setEnabled(true);
        edt_verifi.requestFocus();
        edt_verifi.setSelection(edt_verifi.getText().length());
        checkData();
    }

    @Override
    public void onClick(View view) {
        if (FastClickListener.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_verifi:
                onePresenter.getCode();
                break;
            case R.id.tv_new_regist:
                RegisterOneAct.stratAct(baseContext);
                break;
            case R.id.tv_wx_login:
                WXEntryActivity.startAct(baseActivity, "login");
                break;
        }
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void setCode(byte[] bytes) {
        if (bytes != null) {
            iv_verifi.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
    }

    @Override
    public void smsCode(String smsCode) {
        String currentPhoneNum = edt_account.getText().toString();
        String verifiCode = edt_verifi.getText().toString();
        InputVerfiCodeAct.startAct(getActivity(), currentPhoneNum, verifiCode);
    }

    @Override
    public void checkCode(boolean isSuccess) {

    }

    @Override
    public void checkMobile(boolean isSuccess) {

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
            checkData();
        }
    }

    private void checkData() {
        String verifiCode = edt_verifi.getText().toString();
        String phoneNum = edt_account.getText().toString();
        if (verifiCode.length() == 4 && phoneNum.length() == 13) {
            //输入完验证码跳转逻辑
            onePresenter.sendSmsCode(phoneNum.replaceAll(" ", ""), verifiCode);
        }
    }
}
