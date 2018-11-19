package com.shunlian.app.ui.new3_login;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.VerificationCodeInput;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/11/17.
 * 验证手机号
 */

public class VerifyMobileFrag extends BaseFragment {

    @BindView(R.id.mtv_mobile)
    MyTextView mtv_mobile;

    @BindView(R.id.input_code)
    VerificationCodeInput input_code;

    @BindView(R.id.mtv_reset)
    MyTextView mtv_reset;

    private VerifyPicDialog mVerifyPicDialog;
    private CountDownTimer countDownTimer;
    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_verify_mobile, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        input_code.setOnCompleteListener(content -> {
            if ("8888".equals(content)){
                ((New3LoginAct)baseActivity).loginInviteCode();
            }
        });

        mtv_reset.setOnClickListener(v -> {
            countDown();
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        String mobile = arguments.getString("mobile");
        mtv_mobile.setText(mobile);
        countDown();
    }

    private void countDown() {
        String format = "(%ds)";
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    mtv_reset.setText(String.format(format,(int) Math.floor(l / 1000)));
                    mtv_reset.setEnabled(false);
                    mtv_reset.setTextColor(getColorResouce(R.color.value_484848));
                }

                @Override
                public void onFinish() {
                    mtv_reset.setTextColor(getColorResouce(R.color.pink_color));
                    mtv_reset.setText(getString(R.string.LoginPswFrg_cxhq));
                    mtv_reset.setEnabled(true);
                }
            };
        }
        countDownTimer.cancel();
        countDownTimer.start();
    }

    private void showCode() {
        mVerifyPicDialog = new VerifyPicDialog(baseActivity);
        mVerifyPicDialog.setTvSureColor(R.color.value_007AFF);
        mVerifyPicDialog.setTvSureBgColor(Color.WHITE);
        mVerifyPicDialog.setSureAndCancleListener("确认", v -> {
            String verifyText = mVerifyPicDialog.getVerifyText();
            System.out.println("====verifyText========="+verifyText);
            mVerifyPicDialog.dismiss();
        }, "取消", v -> mVerifyPicDialog.dismiss()).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
