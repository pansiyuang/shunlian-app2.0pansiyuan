package com.shunlian.app.ui.login;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.RegisterOnePresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.register.RegisterAct;
import com.shunlian.app.ui.register.RegisterTwoFrag;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IRegisterOneView;
import com.shunlian.app.widget.ClearableEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.PhoneTextWatcher;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/23.
 */

public class FindPswFrag extends BaseFragment implements View.OnClickListener, IRegisterOneView,
        PhoneTextWatcher.OnInputCompleteListener {

    private RegisterOnePresenter onePresenter;

    private PhoneTextWatcher phoneTextWatcher;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rl_id)
    RelativeLayout rl_id;

    @BindView(R.id.et_phone)
    ClearableEditText et_phone;

    @BindView(R.id.et_code)
    EditText et_code;

    @BindView(R.id.miv_code)
    MyImageView miv_code;

    @BindView(R.id.sv_content)
    ScrollView sv_content;

    @BindView(R.id.view_title)
    View view_title;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.binding_phone, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(this);
        miv_code.setOnClickListener(this);
        phoneTextWatcher = new PhoneTextWatcher(et_phone);
        et_phone.addTextChangedListener(phoneTextWatcher);
        phoneTextWatcher.setOnInputListener(this);
        et_code.addTextChangedListener(new MyTextWatch());
        sv_content.setOnTouchListener((v, event) -> true);
    }

    @Override
    protected void initData() {
        gone(rl_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            visible(view_title);
        } else {
            gone(view_title);
        }

        //返回键扩大点击范围
        int i = TransformUtil.dip2px(baseActivity, 20);
        TransformUtil.expandViewTouchDelegate(miv_close,i,i,i,i);

        tv_title.setText(getStringResouce(R.string.FindPsw_zhmm));
        setEdittextFocusable(true, et_code, et_phone);
        onePresenter = new RegisterOnePresenter(baseActivity, this);
    }

    @Override
    public void setCode(byte[] bytes) {
        if (bytes != null) {
            miv_code.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
    }

    @Override
    public void smsCode(String smsCode) {
        String phoneNum = et_phone.getText().toString();
        Common.staticToast(smsCode);
        String code = et_code.getText().toString();
        ((RegisterAct) baseActivity).addRegisterTwo(phoneNum, smsCode, "", code,
                null, RegisterTwoFrag.TYPE_FIND_PSW);
    }

    @Override
    public void checkCode(boolean isSuccess) {

    }

    @Override
    public void checkMobile(boolean isSuccess) {

    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.miv_close:
                baseActivity.finish();
                break;
            case R.id.miv_code:
                onePresenter.getCode();
                break;
        }
    }

    @Override
    public void onInput() {
        et_code.setEnabled(true);
        et_code.requestFocus();
        et_code.setSelection(et_code.getText().length());
        checkData();
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
        String verifiCode = et_code.getText().toString();
        String phoneNum = et_phone.getText().toString();
        if (verifiCode.length() == 4 && phoneNum.length() == 13) {
            onePresenter.sendSmsCode(phoneNum.replaceAll(" ", ""), verifiCode);
        }
    }
}
