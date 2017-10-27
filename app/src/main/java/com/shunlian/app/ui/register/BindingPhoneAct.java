package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.RegisterOnePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.view.IRegisterOneView;
import com.shunlian.app.widget.ClearableEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.PhoneTextWatcher;

import butterknife.BindView;

public class BindingPhoneAct extends BaseActivity implements IRegisterOneView, View.OnClickListener {
    /**
     * 用户状态
     * 0 没有绑定手机号有上级
     * 1 没有绑定手机号无上级 新用户
     * 2 没有绑定手机号无上级 老直属
     */
    public final int USER_STATES = 0;
    public final int USER_STATES_NEW = 1;
    public final int USER_STATES_OLD = 2;
    private RegisterOnePresenter onePresenter;
    private int state;
    private String id;
    private String unique_sign;

    public static void startAct(Context context, int state,String unique_sign) {
        Intent intent = new Intent(context, BindingPhoneAct.class);
        intent.putExtra("state", state);
        intent.putExtra("unique_sign", unique_sign);
        context.startActivity(intent);
    }


    @BindView(R.id.rl_id)
    RelativeLayout rl_id;

    @BindView(R.id.et_phone)
    ClearableEditText et_phone;

    @BindView(R.id.et_code)
    EditText et_code;

    @BindView(R.id.et_id)
    EditText et_id;

    @BindView(R.id.miv_code)
    MyImageView miv_code;

    @BindView(R.id.tv_select)
    TextView tv_select;

    @Override
    protected int getLayoutId() {
        return R.layout.binding_phone;
    }

    @Override
    protected void initListener() {
        super.initListener();
        et_phone.addTextChangedListener(new PhoneTextWatcher(et_phone));
        miv_code.setOnClickListener(this);
        tv_select.setOnClickListener(this);

        et_code.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                Editable text = et_id.getText();
                if (state == USER_STATES_NEW && TextUtils.isEmpty(text)) {
                    Common.staticToast(getString(R.string.RegisterOneAct_tjridbnwk));
                    return;
                }
                String phone = et_phone.getText().toString().replaceAll(" ", "");
                if (TextUtils.isEmpty(phone)) {
                    Common.staticToast(getString(R.string.RegisterOneAct_sjhbnwk));
                    return;
                }
                if (!TextUtils.isEmpty(s) && s.length() >= 4) {
                    et_code.setSelection(s.length());
                    String code = et_code.getText().toString();
                    onePresenter.sendSmsCode(phone, code);
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        state = getIntent().getIntExtra("state", -1);
        unique_sign = getIntent().getStringExtra("unique_sign");
        if (state == USER_STATES || state == USER_STATES_OLD) {
            rl_id.setVisibility(View.GONE);
        }

        onePresenter = new RegisterOnePresenter(this, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 201 && resultCode == 200) {
            id = data.getStringExtra("id");
            String nickname = data.getStringExtra("nickname");
            et_id.setText(nickname);
            setEdittextFocusable(true, et_phone);
        }
    }

    @Override
    public void setCode(byte[] bytes) {
        if (bytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            miv_code.setImageBitmap(bitmap);
        }
    }

    @Override
    public void smsCode(String smsCode) {
        if (!TextUtils.isEmpty(smsCode)) {
            if (state == USER_STATES_NEW && TextUtils.isEmpty(id)) {
                id = et_id.getText().toString();
            }
            RegisterTwoAct.startAct(this, smsCode, et_phone.getText().toString(), id, unique_sign, null,et_code.getText().toString());
            finish();
        } else {
            Common.staticToast(getString(R.string.RegisterOneAct_sjyzmfssb));
        }
    }

    @Override
    public void checkCode(boolean isSuccess) {

    }

    @Override
    public void showFailureView() {

    }

    @Override
    public void showDataEmptyView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.miv_code:
                onePresenter.getCode();
                break;
            case R.id.tv_select:
                SelectRecommendAct.startAct(this);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean focusable1 = et_id.isFocusable();
        boolean focusable = et_phone.isFocusable();
        boolean focusable2 = et_code.isFocusable();
        if (focusable) {
            Common.hideKeyboard(et_phone);
        } else if (focusable1) {
            Common.hideKeyboard(et_id);
        } else if (focusable2) {
            Common.hideKeyboard(et_code);
        }
    }
}
