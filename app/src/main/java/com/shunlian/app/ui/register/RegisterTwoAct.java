package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

import butterknife.BindView;

public class RegisterTwoAct extends BaseActivity {

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    public static void startAct(Context context, String phone) {
        Intent intent = new Intent(context, RegisterTwoAct.class);
        intent.putExtra("phone", phone);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_two;
    }


    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        String phone = getIntent().getStringExtra("phone");
        tv_phone.setText(phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_hidden_psw:
                if (isHiddenPwd){//隐藏
                    isHiddenPwd = false;
                    isShowPwd(et_pwd,false);
                    iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_h);
                }else {
                    isHiddenPwd = true;
                    isShowPwd(et_pwd,true);
                    iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_n);
                }
                break;
            case R.id.iv_hidden_rpsw:
                if (isHiddenRPwd){
                    isHiddenRPwd = false;
                    isShowPwd(et_rpwd,false);
                    iv_hidden_rpsw.setImageResource(R.mipmap.icon_login_eyes_h);
                }else {
                    isHiddenRPwd = true;
                    isShowPwd(et_rpwd,true);
                    iv_hidden_rpsw.setImageResource(R.mipmap.icon_login_eyes_n);
                }
                break;
        }
    }

    private void isShowPwd(EditText editText,boolean isShow) {
        if (isShow){//显示
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().length());
    }
}
