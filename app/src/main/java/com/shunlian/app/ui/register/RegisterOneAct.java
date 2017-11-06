package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.RegisterOnePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.view.IRegisterOneView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.PhoneTextWatcher;
import com.shunlian.mylibrary.KeyboardPatch;

import butterknife.BindView;


public class RegisterOneAct extends BaseActivity implements View.OnClickListener, IRegisterOneView {

    @BindView(R.id.et_id)
    EditText et_id;

    @BindView(R.id.tv_select)
    TextView tv_select;

    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.et_code)
    EditText et_code;

    @BindView(R.id.miv_code)
    MyImageView miv_code;

    @BindView(R.id.miv_logo)
    MyImageView miv_logo;

    @BindView(R.id.view_title)
    View view_title;

    @BindView(R.id.sv_content)
    ScrollView sv_content;

    private String id;//推荐人id
    private RegisterOnePresenter onePresenter;
    private boolean isCheckCode;
    private String unique_sign;
    private boolean isCheckMobile;

    public static void stratAct(Context context) {
        Intent intent = new Intent(context, RegisterOneAct.class);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_one;
    }

    @Override
    protected void initListener() {
        super.initListener();
        et_phone.addTextChangedListener(new PhoneTextWatcher(et_phone));
        tv_select.setOnClickListener(this);
        miv_code.setOnClickListener(this);
        miv_logo.setOnClickListener(this);
        sv_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        et_phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isEtIdEmpty()) {
                    return false;
                } else {
                    if (TextUtils.isEmpty(id)) {
                        id = et_id.getText().toString();
                    }
                    onePresenter.checkCode(id);
                }
                return false;
            }
        });

        et_phone.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (!TextUtils.isEmpty(s) && !s.toString().startsWith("1")){
                    Common.staticToast(getString(R.string.RegisterOneAct_sjhbzq));
                    return;
                }
                if (!TextUtils.isEmpty(s) && s.length() >= 13) {
                    onePresenter.checkPhone(et_phone.getText().toString().replaceAll(" ",""));
                    setEdittextFocusable(true, et_code);
                }
            }
        });

        et_code.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isEtIdEmpty())
                    return false;
                if (isEtPhoneEmpty())
                    return false;
                return false;
            }
        });
        et_code.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String phone = et_phone.getText().toString().replaceAll(" ", "");
                if (!TextUtils.isEmpty(s) && s.length() >= 4) {
                    et_code.setSelection(s.length());
                    String code = et_code.getText().toString();
                    onePresenter.sendSmsCode(phone, code);
                }
            }
        });
    }

    private boolean isEtPhoneEmpty() {
        String phone = et_phone.getText().toString().replaceAll(" ", "");
        if (TextUtils.isEmpty(phone)) {
            Common.staticToast(getString(R.string.RegisterOneAct_sjhbnwk));
            setEdittextFocusable(true, et_phone);
            setEdittextFocusable(false, et_code);
            return true;
        }else if (!TextUtils.isEmpty(phone) && !phone.startsWith("1") && phone.length() < 11){
            Common.staticToast(getString(R.string.RegisterOneAct_sjhbzq));
            setEdittextFocusable(true, et_phone);
            setEdittextFocusable(false, et_code);
            return true;
        }
        return false;
    }

    private boolean isEtIdEmpty() {
        Editable text = et_id.getText();
        if (TextUtils.isEmpty(text)) {
            Common.staticToast(getString(R.string.RegisterOneAct_tjridbnwk));
            setEdittextFocusable(true, et_id);
            setEdittextFocusable(false, et_phone, et_code);
            return true;
        }
        return false;
    }

    @Override
    protected void initData() {
        setEdittextFocusable(true,et_id);
        miv_logo.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            view_title.setVisibility(View.VISIBLE);
            immersionBar.statusBarColor(R.color.white).keyboardEnable(true).statusBarDarkFont(true, 0.2f).init();
            KeyboardPatch.patch(this).enable();
        }else {
            view_title.setVisibility(View.GONE);
        }

        unique_sign = getIntent().getStringExtra("unique_sign");
        onePresenter = new RegisterOnePresenter(RegisterOneAct.this, this);
    }

    @Override
    public void onClick(View v) {
        if (FastClickListener.isFastClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.tv_select:
                SelectRecommendAct.startAct(this);
                break;
            case R.id.miv_code:
                onePresenter.getCode();
                break;
            case R.id.miv_logo:
                TestAct.startAct(this);
                break;
        }
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
        } else {
            Common.staticToast(getString(R.string.RegisterOneAct_hqyzmsb));
        }
    }

    @Override
    public void smsCode(String smsCode) {
        if (!TextUtils.isEmpty(smsCode) && isCheckCode) {
            if (TextUtils.isEmpty(id)) {
                id = et_id.getText().toString();
            }
            RegisterTwoAct.startAct(this, smsCode, et_phone.getText().toString(), id, unique_sign, RegisterTwoAct.TYPE_REGIST,et_code.getText().toString());
            finish();
        } else {
            onePresenter.getCode();
            et_code.setText("");
        }
    }

    @Override
    public void checkCode(boolean isSuccess) {
        isCheckCode = isSuccess;
        if (!isSuccess) {
            setEdittextFocusable(true, et_id);
        }
    }

    @Override
    public void checkMobile(boolean isSuccess) {
        isCheckMobile = isSuccess;
    }

    @Override
    public void showFailureView() {

    }

    @Override
    public void showDataEmptyView() {

    }

    @Override
    protected void onDestroy() {
        Common.hideKeyboard(et_phone);
        super.onDestroy();
    }
}
