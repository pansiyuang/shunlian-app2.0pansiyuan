package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.RegisterOnePresenter;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.PhoneTextWatcher;

import butterknife.BindView;

public class RegisterOneAct extends BaseActivity implements View.OnClickListener {

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
    private String id;//推荐人id
    private RegisterOnePresenter onePresenter;

    public static void stratAct(Context context){
        context.startActivity(new Intent(context,RegisterOneAct.class));
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
        et_code.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String phone = et_phone.getText().toString().replaceAll(" ","");
                if (TextUtils.isEmpty(phone)){
                    return;
                }
                if (!TextUtils.isEmpty(s) && s.length() >= 4){
                    et_code.setSelection(s.length());
                    String code = et_code.getText().toString();
                    onePresenter.sendSmsCode(phone,code);
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        GlideUtils.getInstance().downPicture(this, InterentTools.HTTPADDR + "member/Common/vcode", miv_code);
        onePresenter = new RegisterOnePresenter(RegisterOneAct.this, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_select:
                SelectRecommendAct.startAct(this);
                break;
            case R.id.miv_code:
                GlideUtils.getInstance().downPicture(this, InterentTools.HTTPADDR + "member/Common/vcode", miv_code);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 201 && resultCode == 200){
            id = data.getStringExtra("id");
            String nickname = data.getStringExtra("nickname");
            et_id.setText(nickname);
            setEdittextFocusable(true,et_phone);
        }
    }
}
