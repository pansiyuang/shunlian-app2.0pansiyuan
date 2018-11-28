package com.shunlian.app.ui.new_login_register;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.shunlian.app.R;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.ui.new3_login.INew3LoginView;
import com.shunlian.app.ui.new3_login.New3LoginAct;
import com.shunlian.app.ui.new3_login.New3LoginInfoTipEntity;
import com.shunlian.app.ui.new3_login.New3LoginPresenter;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.CustomVideoPlayer;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.SmallVideoPlayer;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/7/20.
 */

public class LoginEntryAct extends BaseActivity implements INew3LoginView{

    /***************登录条款************/
    public static final String TERMS_OF_SERVICE = "agreement/1";
    //private TestWXLoginPresenter presenterTest;

    @BindView(R.id.mtv_pwd_login)
    MyTextView mtv_pwd_login;

    @BindView(R.id.customVideoPlayer)
    SmallVideoPlayer customVideoPlayer;
    //是否要释放
    private boolean isRelease;

    public static void startAct(Context context){
        Intent intent = new Intent(context, LoginEntryAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_loginentry;
    }

    @Override
    protected void initListener() {
        super.initListener();
        if (customVideoPlayer != null){
            customVideoPlayer.setOnClickListener(v -> {
                customVideoPlayer.startVideo();
                isRelease = true;
            });
        }
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        EventBus.getDefault().register(this);
        New3LoginPresenter presenter = new New3LoginPresenter(this,this);
        presenter.loginInfoTip();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SmallVideoPlayer.goOnlyPause();
    }

    @OnClick(R.id.llayout_wechat_login)
    public void wechatLogin(){
        WXLogin();
        /*if (presenterTest == null) {
            presenterTest = new TestWXLoginPresenter(this, this);
        }else {
            presenterTest.initApi();
        }*/
    }

    @OnClick({R.id.mbtn_login,R.id.mtv_register})
    public void mobileLogin(){
        New3LoginAct.LoginConfig config = new New3LoginAct.LoginConfig();
        config.login_mode = New3LoginAct.LoginConfig.LOGIN_MODE.SMS_TO_LOGIN;
        New3LoginAct.startAct(this,config);
        /*RegisterAndBindingAct.startAct(this,
                RegisterAndBindingAct.FLAG_LOGIN,null,null,null);*/
        finish();
    }

    @OnClick(R.id.mtv_pwd_login)
    public void pwdLoginEntry(){
        New3LoginAct.LoginConfig config = new New3LoginAct.LoginConfig();
        config.login_mode = New3LoginAct.LoginConfig.LOGIN_MODE.PASSWORD_TO_LOGIN;
        New3LoginAct.startAct(this,config);
        /*RegisterAndBindingAct.startAct(this,
                RegisterAndBindingAct.FLAG_PWD_LOGIN,null,null,null);*/
        finish();
    }

    @OnClick(R.id.llayout_login_agreement)
    public void loginAgreement(){
        H5X5Act.startAct(this, InterentTools.H5_HOST
                + TERMS_OF_SERVICE,H5X5Act.MODE_SONIC);
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void setLoginInfoTip(New3LoginInfoTipEntity data) {
        if (data != null){
            New3LoginInfoTipEntity.AdBean ad = data.ad;
            if (ad != null){
                int rw = DeviceInfoUtil.getDeviceWidth(this) - TransformUtil.dip2px(this, 40);
                int rh = TransformUtil.dip2px(this, 160);
                /*int[] ints = TransformUtil.countRealWH(this, rw, rh);
                ViewGroup.LayoutParams layoutParams = customVideoPlayer.getLayoutParams();
                layoutParams.width = ints[0];
                layoutParams.height = ints[1];
                customVideoPlayer.setLayoutParams(layoutParams);*/
                String image = ad.image;
                New3LoginInfoTipEntity.LinkBean link = ad.link;
                if (!isEmpty(image)) {
                    customVideoPlayer.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    GlideUtils.getInstance().loadOverrideImage(this,
                            R.mipmap.img_default_find_articlelist2,
                            customVideoPlayer.thumbImageView,image,rw,rh);
                }
                if (link != null && !isEmpty(link.item_id)) {
                    customVideoPlayer.setUp(link.item_id, CustomVideoPlayer.SCREEN_WINDOW_NORMAL, "");
                    visible(customVideoPlayer);
                    customVideoPlayer.onlyLook();
                }
            }
        }
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
    protected void onDestroy() {
        if (customVideoPlayer != null && isRelease){
            customVideoPlayer.onAutoCompletion();
            customVideoPlayer.release();
            customVideoPlayer = null;
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 微信登录
     */
    public void WXLogin(){
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        api.registerApp(Constant.WX_APP_ID);

        int wxSdkVersion = api.getWXAppSupportAPI();
        if (wxSdkVersion >= Constant.TIMELINE_SUPPORTED_VERSION){
            try {
                // send oauth request
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = SharedPrefUtil.getCacheSharedPrf("X-Device-ID",
                        "744D9FC3-5DBD-3EDD-A589-56D77BDB0E5D");
                api.sendReq(req);
            }catch (Exception e){

            }
            //finish();//在此finish会导致某些页面的请求隐私接口时频繁打开登录入口，导致微信登录调不起来
        }else if (wxSdkVersion == 0) {
            Common.staticToast("请先安装微信");
            finish();
        } else {
            Common.staticToast("当前微信版本过低，请更新后再试");
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notifClose(DefMessageEvent n){
        if (n != null && n.loginSuccess){
            finish();
        }
    }
}
