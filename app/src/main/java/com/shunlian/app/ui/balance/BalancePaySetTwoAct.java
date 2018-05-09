package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.presenter.PBalancePaySetTwo;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IBalancePaySetTwo;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.gridpasswordview.GridPasswordView;

import butterknife.BindView;

public class BalancePaySetTwoAct extends BaseActivity implements IBalancePaySetTwo {

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_desc)
    MyTextView mtv_desc;

    @BindView(R.id.mtv_hint)
    MyTextView mtv_hint;

    @BindView(R.id.gpv_customUi)
    GridPasswordView gpv_customUi;

    private PBalancePaySetTwo pBalancePaySetTwo;
    private String mpsw, before, tag, key;


    public static void startAct(Context context, String before, String tag, String key,boolean isPaySet,boolean isAli) {
        Intent intent = new Intent(context, BalancePaySetTwoAct.class);
        intent.putExtra("before", before);
        intent.putExtra("tag", tag);
        intent.putExtra("key", key);
        intent.putExtra("isPaySet", isPaySet);
        intent.putExtra("isAli", isAli);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        gpv_customUi.clearPassword();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_balance_pay_set_two;
    }


    @Override
    protected void initListener() {
        super.initListener();
        gpv_customUi.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
            }

            @Override
            public void onInputFinish(String psw) {
                mpsw = psw;
                if ("set".equals(tag) || "sets".equals(tag)) {
                    if (TextUtils.isEmpty(before)) {
                        pBalancePaySetTwo.checkRule(psw);
                    } else if (psw.equals(before)) {
                        if ("set".equals(tag)) {
                            pBalancePaySetTwo.setPayPassword(psw, key);
                        } else {
                            pBalancePaySetTwo.changePayPassword(psw, key);
                        }
                    } else {
                        Common.staticToast(getStringResouce(R.string.RegisterTwoAct_mmbyz));
                        finish();
                    }
                } else if ("unbind".equals(tag)) {
                    pBalancePaySetTwo.unbindAliPay(psw);
                } else {
                    pBalancePaySetTwo.checkPayPassword(psw);
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        before = getIntent().getStringExtra("before");
        tag = getIntent().getStringExtra("tag");
        key = getIntent().getStringExtra("key");
        if ("set".equals(tag)) {
            mtv_title.setText(getStringResouce(R.string.balance_shezhimima));
            if (TextUtils.isEmpty(before)) {
                mtv_desc.setText(getStringResouce(R.string.balance_qingshezhimima));
            } else {
                mtv_desc.setText(getStringResouce(R.string.balance_qingzaicishuru));
                mtv_hint.setVisibility(View.GONE);
            }
        } else if ("sets".equals(tag)) {
            mtv_title.setText(getStringResouce(R.string.balance_shezhixinmima));
            if (TextUtils.isEmpty(before)) {
                mtv_desc.setText(getStringResouce(R.string.balance_qingshexinzhimima));
            } else {
                mtv_desc.setText(getStringResouce(R.string.balance_qingzaicishuru));
                mtv_hint.setVisibility(View.GONE);
            }
        } else {
            mtv_desc.setText(getStringResouce(R.string.balance_qingshuruzhifu));
            mtv_title.setText(getStringResouce(R.string.balance_shenfengyanzheng));
            mtv_hint.setVisibility(View.GONE);
        }
        pBalancePaySetTwo = new PBalancePaySetTwo(this, this);
    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void setPasswordCall() {
        Common.staticToasts(this, getStringResouce(R.string.balance_shezhichenggong), R.mipmap.icon_common_duihao);
        if (getIntent().getBooleanExtra("isPaySet",false)){
            BalancePaySetAct.startAct(this, true, true);
        }else{
            AlipayAddAct.startAct(this,getIntent().getBooleanExtra("isAli",false));
        }
    }

    @Override
    public void changePasswordCall() {
        Common.staticToasts(this, getStringResouce(R.string.balance_xiugaichenggong), R.mipmap.icon_common_duihao);
        BalancePaySetAct.startAct(this, true, true);
    }

    @Override
    public void checkPasswordCall(boolean isRight, String key) {
        if (isRight) {
            if ("modify".equals(tag)) {
                BalancePaySetTwoAct.startAct(this, "", "sets", key,false,false);
            } else {
                AlipayAddAct.startAct(this,getIntent().getBooleanExtra("isAli",false));
            }
        } else {
            gpv_customUi.clearPassword();
        }
    }

    @Override
    public void unbindAliPayCall(boolean isOk) {
        if (isOk) {
            AlipayMyAct.startAct(this, false,true, true, "");
        } else {
            gpv_customUi.clearPassword();
        }
    }

    @Override
    public void checkRuleValidCall(boolean isOk) {
        if (isOk) {
            BalancePaySetTwoAct.startAct(this, mpsw, tag, key,false,false);
        } else {
            gpv_customUi.clearPassword();
        }
    }
}
