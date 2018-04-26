package com.shunlian.app.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.presenter.SettingPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.receive_adress.AddressManageActivity;
import com.shunlian.app.ui.setting.feed_back.BeforeFeedBackAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.ISettingView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/23.
 */

public class SettingAct extends BaseActivity implements ISettingView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.mtv_personal_data)
    MyTextView mtv_personal_data;

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;

    @BindView(R.id.mtv_comment_manage)
    MyTextView mtv_comment_manage;

    @BindView(R.id.mtv_user_security)
    MyTextView mtv_user_security;

    @BindView(R.id.llayout_clear)
    LinearLayout llayout_clear;

    @BindView(R.id.llayout_message)
    LinearLayout llayout_message;

    @BindView(R.id.mtv_feedback)
    MyTextView mtv_feedback;

    @BindView(R.id.mtv_app_score)
    MyTextView mtv_app_score;

    @BindView(R.id.mtv_app_recommend)
    MyTextView mtv_app_recommend;

    @BindView(R.id.llayout_about)
    LinearLayout llayout_about;

    @BindView(R.id.mtv_app_version)
    MyTextView mtv_app_version;

    @BindView(R.id.mtv_count)
    MyTextView mtv_count;

    @BindView(R.id.miv_message)
    MyImageView miv_message;

    private SettingPresenter presenter;


    public static void startAct(Context context) {
        context.startActivity(new Intent(context, SettingAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_setting;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_personal_data.setOnClickListener(this);
        mtv_address.setOnClickListener(this);
        mtv_comment_manage.setOnClickListener(this);
        mtv_user_security.setOnClickListener(this);
        llayout_clear.setOnClickListener(this);
        llayout_message.setOnClickListener(this);
        mtv_feedback.setOnClickListener(this);
        mtv_app_score.setOnClickListener(this);
        mtv_app_recommend.setOnClickListener(this);
        llayout_about.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText("设置");
        gone(mrlayout_toolbar_more);

        presenter = new SettingPresenter(this,this);

        String localVersion = SharedPrefUtil.getSharedPrfString("localVersion", "");
        mtv_app_version.setText("V"+localVersion);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.mtv_personal_data:
                PersonalDataAct.startAct(this);
                break;
            case R.id.mtv_address:
                AddressManageActivity.startAct(this);
                break;
            case R.id.mtv_comment_manage:
                break;
            case R.id.mtv_user_security:
                UserSecurityAct.startAct(this);
                break;
            case R.id.llayout_clear:
                Common.staticToast("清除缓存成功");
                mtv_count.setText("已清理");
                break;
            case R.id.llayout_message:

                break;
            case R.id.mtv_feedback:
                BeforeFeedBackAct.startAct(this,null);
                break;
            case R.id.mtv_app_score:
                break;
            case R.id.mtv_app_recommend:
                break;
            case R.id.llayout_about:
                break;
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
}
