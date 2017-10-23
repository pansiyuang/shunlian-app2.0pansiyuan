package com.shunlian.app.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.RegisterOnePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.IRegisterOneView;
import com.shunlian.app.widget.ClearableEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.PhoneTextWatcher;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/23.
 */

public class FindPswAct extends BaseActivity implements View.OnClickListener, IRegisterOneView, PhoneTextWatcher.OnInputCompleteListener {
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

    public static void startAct(Context context) {
        Intent intent = new Intent(context, FindPswAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.binding_phone;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        onePresenter = new RegisterOnePresenter(this, this);
        initViews();
    }

    public void initViews() {
        rl_id.setVisibility(View.GONE);
        tv_title.setText(getResources().getString(R.string.FindPsw_zhmm));
    }

    @Override
    protected void initListener() {
        miv_code.setOnClickListener(this);
        phoneTextWatcher = new PhoneTextWatcher(et_phone);
        et_phone.addTextChangedListener(phoneTextWatcher);
        phoneTextWatcher.setOnInputListener(this);

        et_code.addTextChangedListener(new MyTextWatch());
        super.initListener();
    }

    @Override
    public void setCode(byte[] bytes) {
        if (bytes != null) {
            miv_code.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
    }

    @Override
    public void smsCode(String smsCode) {

    }

    @Override
    public void showFailureView() {

    }

    @Override
    public void showDataEmptyView() {

    }

    @Override
    public void onClick(View view) {
        onePresenter.getCode();
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
            //输入完验证码跳转逻辑
            onePresenter.sendSmsCode(phoneNum.replaceAll(" ", ""), verifiCode);
        }
    }
}
