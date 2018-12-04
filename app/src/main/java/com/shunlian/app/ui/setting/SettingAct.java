package com.shunlian.app.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.BuildConfig;
import com.shunlian.app.R;
import com.shunlian.app.presenter.SettingPresenter;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.ui.my_comment.MyCommentAct;
import com.shunlian.app.ui.receive_adress.AddressManageActivity;
import com.shunlian.app.ui.setting.feed_back.BeforeFeedBackAct;
import com.shunlian.app.utils.ClearCacheUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.SwitchHostUtil;
import com.shunlian.app.view.ISettingView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.io.File;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018/4/23.
 */

public class SettingAct extends BaseActivity implements ISettingView {

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

    @BindView(R.id.tv_transfer)
    TextView tv_transfer;

    @BindView(R.id.miv_message)
    MyImageView miv_message;

    private boolean isPushOpen = true;

    private SettingPresenter presenter;
    private String mAboutUrl;
    private String mAppQRCode;
    private String mIsSetPwd;


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
        gone(mrlayout_toolbar_more, mtv_app_score);

        presenter = new SettingPresenter(this, this);

        String localVersion = SharedPrefUtil.getCacheSharedPrf("localVersion", "");
        mtv_app_version.setText("V" + localVersion);

        //计算缓存大小
        CacheSize cacheSize = new CacheSize();
        cacheSize.execute();
        if (BuildConfig.DEBUG) {
            tv_transfer.setVisibility(View.VISIBLE);
            tv_transfer.setOnClickListener(this);
            if (InterentTools.HTTPADDR.contains("v20-front-api")) {
                tv_transfer.setText("当前测试环境");
            } else if (InterentTools.HTTPADDR.contains("api-front.v2")) {
                tv_transfer.setText("当前预发布环境");
            } else {
                tv_transfer.setText("当前正式环境");
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_transfer:
                SwitchHostUtil.switchMethod(this);
                if (!isEmpty(mtv_count.getText())) {
                    ClearCacheUtil cacheUtil = new ClearCacheUtil();
                    cacheUtil.execute();
                    mtv_count.setText("");
                }
                break;
            case R.id.mtv_personal_data:
                PersonalDataAct.startAct(this);
                break;
            case R.id.mtv_address:
                AddressManageActivity.startAct(this);
                break;
            case R.id.mtv_comment_manage:
                MyCommentAct.startAct(this);
                break;
            case R.id.mtv_user_security:
                UserSecurityAct.startAct(this,mIsSetPwd);
                break;
            case R.id.llayout_clear:
                if (!isEmpty(mtv_count.getText())) {
                    ClearCacheUtil cacheUtil = new ClearCacheUtil();
                    cacheUtil.execute();
                    mtv_count.setText("");
                }
                break;
            case R.id.llayout_message:
                if (isPushOpen) {//停止推送服务
                    miv_message.setImageResource(R.mipmap.icon_chat_userlist_n);
                    JPushInterface.stopPush(Common.getApplicationContext());
                    if (presenter != null) {
                        presenter.updatePushSetting("0");
                    }
                } else {//开启推送服务
                    miv_message.setImageResource(R.mipmap.icon_chat_userlist_h);
                    JPushInterface.resumePush(Common.getApplicationContext());
                    if (presenter != null) {
                        presenter.updatePushSetting("1");
                    }
                }
                isPushOpen = !isPushOpen;
                break;
            case R.id.mtv_feedback:
                BeforeFeedBackAct.startAct(this, null);
                break;
            case R.id.mtv_app_score:
                break;
            case R.id.mtv_app_recommend:
                BusinessCardAct.startAct(this, mAppQRCode);
                break;
            case R.id.llayout_about:
                H5X5Act.startAct(this, mAboutUrl, H5X5Act.MODE_SONIC);
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

    /**
     * 推送开关
     *
     * @param push_on
     */
    @Override
    public void pushSwitch(String push_on) {
        if ("0".equals(push_on)) {//接收推送，1是，0否
            isPushOpen = false;
            miv_message.setImageResource(R.mipmap.icon_chat_userlist_n);
        } else {
            isPushOpen = true;
            miv_message.setImageResource(R.mipmap.icon_chat_userlist_h);
        }
    }

    /**
     * 关于app的url
     *
     * @param about_url
     */
    @Override
    public void aboutUrl(String about_url) {
        mAboutUrl = about_url + "?versioncode=" + mtv_app_version.getText();
    }

    /**
     * 是否设置密码
     *
     * @param if_pwd_set 0未设置 1已设置
     */
    @Override
    public void isSetPwd(String if_pwd_set) {
        mIsSetPwd = if_pwd_set;
    }

    /**
     * 下载app二维码路径
     *
     * @param appQRCode
     */
    @Override
    public void downAppQRCode(String appQRCode) {
        mAppQRCode = appQRCode;
    }

    class CacheSize extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            File file = new File(Constant.CACHE_PATH_EXTERNAL);
            if (file.exists()) {
                long t = getTotalSizeOfFilesInDir(file) - (80*1000*1000);
                if (t > 20 * 1000 * 1000) {//大于20兆可清理缓存
                    String s = Formatter.formatFileSize(SettingAct.this, t);
                    return s;
                }
                return "";
            } else {
                file.mkdirs();
            }
            return "";
        }

        // 递归方式 计算文件的大小
        private long getTotalSizeOfFilesInDir(final File file) {
            if (file.isFile())
                return file.length();
            final File[] children = file.listFiles();
            long total = 0;
            if (children != null)
                for (final File child : children)
                    total += getTotalSizeOfFilesInDir(child);
            return total;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mtv_count != null) mtv_count.setText(s==null?"":s);
        }
    }
}
