package com.shunlian.app.ui.new3_login;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shunlian.app.R;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/11/19.
 * 填写邀请码
 */

public class InviteCodeFrag extends BaseFragment implements INew3LoginView{

    @BindView(R.id.mbtn_login)
    MyButton mbtnLogin;

    @BindView(R.id.mtv_rule)
    MyTextView mtvRule;

    @BindView(R.id.invite_code)
    InviteCodeWidget invite_code;

    @BindView(R.id.mtv_InviteTip)
    MyTextView mtv_InviteTip;

    @BindView(R.id.miv_coupon)
    ImageView miv_coupon;

    @BindView(R.id.mtv_tip)
    MyTextView mtv_tip;

    private VerifyPicDialog mVerifyPicDialog;
    private New3LoginPresenter presenter;
    private New3LoginAct.LoginConfig mConfig;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_invite_code_new3, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        /*invite_code.setOnTextChangeListener(sequence -> {
            GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
            if (!isEmpty(sequence)){
                btnDrawable.setColor(getColorResouce(R.color.pink_color));
            }else {
                btnDrawable.setColor(Color.parseColor("#ECECEC"));
            }
            if (isEmpty(sequence)){
                mtv_tip.setVisibility(View.INVISIBLE);
            }
        });*/
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        //按钮状态
        //GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
        //btnDrawable.setColor(Color.parseColor("#ECECEC"));
        presenter = new New3LoginPresenter(baseActivity,this);
        presenter.loginInfoTip();
        //如果有推荐人，直接填写推荐人
        String inviteCode = SharedPrefUtil.getSharedUserString("share_code", "");
        if (!isEmpty(inviteCode)){
            invite_code.setInviteCodeText(inviteCode);
        }

        mConfig = getArguments().getParcelable("config");
    }

    public void initStatus(New3LoginAct.LoginConfig config) {
        mConfig = config;
        //如果有推荐人，直接填写推荐人
        String inviteCode = SharedPrefUtil.getSharedUserString("share_code", "");
        if (!isEmpty(inviteCode)){
            invite_code.setInviteCodeText(inviteCode);
        }else if (mConfig != null && !isEmpty(mConfig.invite_code)){

        }else {
            invite_code.setInviteCodeText("");
        }
    }


    @OnClick(R.id.mbtn_login)
    public void bindInviteCode(){
        if (invite_code == null || isEmpty(invite_code.getText())){
            SharedPrefUtil.saveCacheSharedPrf("wx_jump", "");
            Common.goGoGo(baseActivity,"home");
            ((New3LoginAct) baseActivity).finish();
            return;
        }

        String code = invite_code.getText().toString();
        if (presenter != null){
            presenter.codeDetail(code);
        }
    }

    /**
     * 邀请码详情
     *
     * @param bean
     */
    @Override
    public void codeInfo(MemberCodeListEntity bean,String error) {
        if (bean != null) {
            mVerifyPicDialog = new VerifyPicDialog(baseActivity);
            mVerifyPicDialog.setTvSureColor(R.color.value_007AFF);
            mVerifyPicDialog.setTvSureBgColor(Color.WHITE);
            mVerifyPicDialog.setMessage("请确认您的导购专员");
            mVerifyPicDialog.showState(2);
            mVerifyPicDialog.setMemberDetail(bean.info);
            mVerifyPicDialog.setSureAndCancleListener("确认绑定", v -> {
                String code = invite_code.getText().toString();
                if (presenter != null) {
                    presenter.bindShareid(code);
                }
                mVerifyPicDialog.dismiss();
            }, "取消", v -> mVerifyPicDialog.dismiss()).show();
        }else {
            mtv_tip.setText(error);
            mtv_tip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void bindShareID(String tip) {
        if (!isEmpty(tip)){
            mtv_tip.setText(tip);
            mtv_tip.setVisibility(View.VISIBLE);
        }else {
            Common.goGoGo(baseActivity,"home");
            ((New3LoginAct)baseActivity).finish();
        }
    }

    @Override
    public void setLoginInfoTip(New3LoginInfoTipEntity data) {
        if (data != null){
            New3LoginInfoTipEntity.V2 v2 = data.v2;
            if (v2 == null)return;
            if (mtv_InviteTip != null)mtv_InviteTip.setText(v2.incite_code_title);
            if (miv_coupon != null){
                String w = Common.getURLParameterValue(data.voucher, "w");
                String h = Common.getURLParameterValue(data.voucher, "h");
                int rh = TransformUtil.dip2px(baseActivity, 105);
                int ww = Integer.parseInt(w);
                int hh = Integer.parseInt(h);
                int rw = (int) (ww * rh * 1.0f / hh + 0.5f);
                GlideUtils.getInstance().loadOverrideImage(baseActivity,miv_coupon,v2.voucher,rw,rh);
            }
            if (mtvRule != null && !isEmpty(v2.incite_code_rule)){
                mtvRule.setText(v2.incite_code_rule);
            }
            if (invite_code != null){
                invite_code.setStrategyUrl(data.incite_code_url);
            }
        }
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
