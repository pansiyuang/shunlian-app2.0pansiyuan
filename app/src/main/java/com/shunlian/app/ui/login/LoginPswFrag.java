package com.shunlian.app.ui.login;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.presenter.LoginPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.register.RegisterOneAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.ILoginView;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.wxapi.WXEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;

import butterknife.BindView;

import static com.shunlian.app.presenter.LoginPresenter.TYPE_USER;

/**
 * Created by Administrator on 2017/10/17.
 */

public class LoginPswFrag extends BaseFragment implements View.OnClickListener, ILoginView {
    @BindView(R.id.iv_hidden_psw)
    MyImageView iv_hidden_psw;
    @BindView(R.id.edt_account)
    EditText edt_account;
    @BindView(R.id.edt_psw)
    MyEditText edt_psw;
    @BindView(R.id.tv_new_regist)
    TextView tv_new_regist;
    @BindView(R.id.btn_login)
    MyButton btn_login;
    @BindView(R.id.tv_find_psw)
    TextView tv_find_psw;
    @BindView(R.id.tv_wx_login)
    TextView tv_wx_login;
    private View rootView;
    private boolean isHidden = true;
    private LoginPresenter loginPresenter;
    private String jumpType;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_login_psw, container, false);
        return rootView;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        btn_login.setWHProportion(650, 90);
        setEdittextFocusable(true, edt_psw, edt_account);
        loginPresenter = new LoginPresenter(getActivity(), this, TYPE_USER);
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_new_regist.setOnClickListener(this);
        iv_hidden_psw.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_wx_login.setOnClickListener(this);
        tv_find_psw.setOnClickListener(this);
        edt_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_psw.getText().toString().isEmpty()) {
                    iv_hidden_psw.setVisibility(View.INVISIBLE);
                    hiddenPsw(false);
                } else {
                    iv_hidden_psw.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (MyOnClickListener.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_hidden_psw:
                hiddenPsw(isHidden);
                int index = edt_psw.getText().toString().length();
                if (index > 0) {
                    edt_psw.setSelection(index);
                }
                break;
            case R.id.tv_new_regist:
                RegisterOneAct.stratAct(baseContext);
                break;

            case R.id.tv_find_psw:
                FindPswAct.startAct(getActivity());
                break;
            case R.id.btn_login:
                String currentAccount = edt_account.getText().toString();
                String currentPsw = edt_psw.getText().toString();
                if (TextUtils.isEmpty(currentAccount)) {
                    Common.staticToast("账号不能为空喔~");
                    return;
                }

                if (TextUtils.isEmpty(currentPsw)) {
                    Common.staticToast("密码不能为空喔~");
                    return;
                }
                if (edt_psw.getText().length() < 8) {
                    Common.staticToast("密码输入太短~");
                    return;
                }
                loginPresenter.LoginUserName(currentAccount, currentPsw);
                break;
            case R.id.tv_wx_login:
                WXEntryActivity.startAct(baseActivity, "login", null);
                break;
        }
    }

    public void hiddenPsw(boolean b) {
        if (b) {
            edt_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_n);
            isHidden = false;
        } else {
            edt_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_h);
            isHidden = true;
        }
    }

    @Subscribe(sticky = true)
    public void eventBus(DispachJump jump){
        jumpType = jump.jumpType;
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().postSticky(jump);
    }

    @Override
    public void login(LoginFinishEntity content) {
        //登陆成功啦
        SharedPrefUtil.saveSharedPrfString("token", content.token);
        SharedPrefUtil.saveSharedPrfString("refresh_token", content.refresh_token);
        SharedPrefUtil.saveSharedPrfString("member_id", content.member_id);
        if (content.tag != null)
            SharedPrefUtil.saveSharedPrfStringss("tags", new HashSet<>(content.tag));
        DefMessageEvent event = new DefMessageEvent();
        event.loginSuccess = true;
        EventBus.getDefault().post(event);
        EasyWebsocketClient.initWebsocketClient(getActivity()); //初始化聊天
        if (Constant.JPUSH != null && !"login".equals(Constant.JPUSH.get(0))) {
            Common.goGoGo(baseActivity, Constant.JPUSH.get(0), Constant.JPUSH.get(1), Constant.JPUSH.get(2));
        }
        JpushUtil.setJPushAlias();
        if (!isEmpty(jumpType)){
            Common.goGoGo(baseActivity,jumpType);
        }
        baseActivity.finish();
    }

    @Override
    public void getSmsCode(String code) {

    }

    @Override
    public void loginFail(String erroMsg) {
        Common.staticToast(erroMsg);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
