package com.shunlian.app.ui.login;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.presenter.SendSmsPresenter;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.ISendSmsView;
import com.shunlian.app.widget.ClearableEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.PhoneTextWatcher;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/17.
 */

public class LoginVerfiFrag extends BaseFragment implements PhoneTextWatcher.OnInputCompleteListener, View.OnClickListener, ISendSmsView {
    private View rootView;
    private PhoneTextWatcher phoneTextWatcher;
    private SendSmsPresenter sendSmsPresenter;

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
    public void onResume() {
        GlideUtils.getInstance().downPicture(getActivity(), InterentTools.HTTPADDR + "member/Common/vcode", iv_verifi);
        super.onResume();
    }

    @Override
    protected void initData() {
        sendSmsPresenter = new SendSmsPresenter(getActivity(), LoginVerfiFrag.this);
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
        checkData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_verifi:
                GlideUtils.getInstance().downPicture(getActivity(), InterentTools.HTTPADDR + "member/Common/vcode", iv_verifi);
                break;
        }
    }

    @Override
    public void sendSms(String smsCode) {

    }

    @Override
    public void showFailureView() {

    }

    @Override
    public void showDataEmptyView() {

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

            sendSmsPresenter.checkCode(verifiCode);
//            sendSmsPresenter.sendSms(phoneNum.replaceAll(" ", ""), verifiCode);
//                    InputVerfiCodeAct.startAct(getActivity(), phoneNum);
        }
    }
}
