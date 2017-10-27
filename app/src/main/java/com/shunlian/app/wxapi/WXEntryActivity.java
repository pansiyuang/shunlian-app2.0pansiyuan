package com.shunlian.app.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.bean.WXLoginEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.register.BindingPhoneAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.SharedPrefUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler,WXEntryView {


    private String deviceId;
    private IWXAPI api;
    private String flag;
    private MyHandler mHandler;
    private String currentDesc;
    private String currTitle;
    private String currentType;
    private String currentGoodsId;
    private WXEntryPresenter wxEntryPresenter;

    public static void startAct(Context context,String flag){
        Intent intent = new Intent(context,WXEntryActivity.class);
        intent.putExtra("flag",flag);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wxentry;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        deviceId = SharedPrefUtil.getSharedPrfString("X-Device-ID", "744D9FC3-5DBD-3EDD-A589-56D77BDB0E5D");
        //初始注册方法必须有，即使就算第二次回调启动的时候先调用onResp，也必须有注册方法，否则会出错
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        api.registerApp(Constant.WX_APP_ID);// 注册到微信列表，没什么用，笔者不知道干嘛用的，有知道的请告诉我，该文件顶部有我博客链接。或加Q1692475028,谢谢！
        try {
            api.handleIntent(getIntent(), this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        wxEntryPresenter = new WXEntryPresenter(this,this);
        int wxSdkVersion = api.getWXAppSupportAPI();
        if (wxSdkVersion >= Constant.TIMELINE_SUPPORTED_VERSION) {
            initGet();
        } else if (wxSdkVersion == 0) {
            Toast.makeText(this, "请先安装微信", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "当前微信版本过低，请更新后再试", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void initGet() {
        mHandler = new MyHandler();


        if (getIntent().getStringExtra("flag") != null) {
            //必须用getExtras
            flag = getIntent().getExtras().getString("flag");
            //tv_nick.setText("");
            SharedPrefUtil.saveSharedPrfString("flag", flag);
            final String shareLink, desc;
            if (getIntent().getStringExtra("shareLink") != null) {
                shareLink = getIntent().getExtras().getString("shareLink");
            } else {
                shareLink = "www.shunliandongli.com";
            }
            if (getIntent().getStringExtra("desc") != null) {
                desc = getIntent().getExtras().getString("desc");
            } else {
                desc = "顺联动力商城";
            }
            currentDesc = desc;
            if (getIntent().getStringExtra("title") != null) {
                currTitle = getIntent().getExtras().getString("title");
            } else {
                currTitle = "顺联动力";
            }

            currentType = getIntent().getStringExtra("type");
            currentGoodsId = getIntent().getStringExtra("goodsId");

            if (flag.equals("login") || flag.equals("bind")) {
                login();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    public void login() {
        // send oauth request
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = deviceId;
        api.sendReq(req);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int result = 0;
        if (wxEntryPresenter == null) {
            wxEntryPresenter = new WXEntryPresenter(this, this);
        }
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                flag = SharedPrefUtil.getSharedPrfString("flag", "");
                if ("login".equals(flag)) {
                    SendAuth.Resp sendAuthResp = (SendAuth.Resp) baseResp;// 用于分享时不要有这个，不能强转
                    String code = sendAuthResp.code;
                    wxEntryPresenter.wxLogin(code);
                } else if ("bind".equals(flag)) {
                    SendAuth.Resp sendAuthResp = (SendAuth.Resp) baseResp;// 用于分享时不要有这个，不能强转
                    String code = sendAuthResp.code;
//                    initApi();
//                    MyHttpUtil.settingBindWeixin(this, settingBindWeixinCallBack, code, deviceId);
                } else {
                    Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }

        if (baseResp.errCode != BaseResp.ErrCode.ERR_OK) {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void showFailureView() {

    }

    @Override
    public void showDataEmptyView() {

    }

    @Override
    public void onWXCallback(WXLoginEntity wxLoginEntity) {
        if (wxLoginEntity != null){
            String unique_sign = wxLoginEntity.unique_sign;
            int member_id = wxLoginEntity.member_id;
            int status = wxLoginEntity.status;
             if (status == 2){
                 BindingPhoneAct.startAct(this,0,unique_sign);
            }else if (status == 1){
                SharedPrefUtil.saveSharedPrfString("token","");
            }else {
                 BindingPhoneAct.startAct(this,1,unique_sign);
            }
            finish();
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
//            if (!isFinishing() && mDialog != null && mDialog.isShowing() && mDialog != null) {
//                mDialog.dismiss();
//            }
//            if (!isFinishing() && shareDialog != null && shareDialog.isShowing()) {
//                shareDialog.dismiss();
//            }
            switch (msg.what) {
                case 1:
//                    showSaveSuccessDialog();
                    break;
                case 2:
                    Common.staticToast("保存图片失败,请重试");
                    finish();
                    break;
                case 3:
                    Common.staticToast("分享失败,请重试");
                    finish();
                    break;
                case 4:
                    Common.staticToast("手机内存不足");
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
