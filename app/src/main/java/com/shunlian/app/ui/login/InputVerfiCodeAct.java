package com.shunlian.app.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.VerificationCodeInput;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/18.
 */

public class InputVerfiCodeAct extends BaseActivity implements View.OnClickListener, VerificationCodeInput.Listener {
    private CountDownTimer countDownTimer;
    private String currentPhone;

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.input_code)
    VerificationCodeInput input_code;

    @BindView(R.id.et_pwd)
    EditText et_pwd;

    @BindView(R.id.et_rpwd)
    EditText et_rpwd;

    @BindView(R.id.et_nickname)
    EditText et_nickname;

    @BindView(R.id.tv_complete)
    TextView tv_complete;


    public static void startAct(Context context, String phoneNum) {
        Intent intent = new Intent(context, InputVerfiCodeAct.class);
        intent.putExtra("phoneNum", phoneNum);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_two;
    }


    private void initViews() {

        et_pwd.setVisibility(View.GONE);
        et_rpwd.setVisibility(View.GONE);
        et_nickname.setVisibility(View.GONE);
        tv_complete.setText(getString(R.string.LoginPswFrg_dl));

        tv_phone.setText(currentPhone);

        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tv_time.setText((int) Math.floor(l / 1000) + "s");
                tv_time.setEnabled(false);
            }

            @Override
            public void onFinish() {
                tv_time.setText("重新获取");
                tv_time.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        currentPhone = getIntent().getStringExtra("phoneNum");
        initViews();
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_time.setOnClickListener(this);
        input_code.setOnCompleteListener(this);
    }

    @Override
    public void onClick(View view) {
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        countDownTimer = null;
    }

    @Override
    public void onComplete(String content) {

    }
}
