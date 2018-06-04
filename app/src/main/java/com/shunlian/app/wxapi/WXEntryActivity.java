package com.shunlian.app.wxapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.WXLoginEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.register.RegisterAct;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler, WXEntryView {
    private String deviceId;
    private IWXAPI api;
    private String flag;
    private MyHandler mHandler;
    private String currentDesc;
    private String currTitle;
    private String shareLink;
    private WXEntryPresenter wxEntryPresenter;


    public static void startAct(Context context, String flag, ShareInfoParam shareInfoParam) {
        Intent intent = new Intent(context, WXEntryActivity.class);
        intent.putExtra("flag", flag);
        intent.putExtra("shareInfoParam", shareInfoParam);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wxentry;
    }

    @Override
    protected void initData() {
        setHideStatusAndNavigation();
        deviceId = SharedPrefUtil.getSharedPrfString("X-Device-ID", "744D9FC3-5DBD-3EDD-A589-56D77BDB0E5D");
        //初始注册方法必须有，即使就算第二次回调启动的时候先调用onResp，也必须有注册方法，否则会出错
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        //api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.WX_APP_ID);// 注册到微信列表，没什么用，笔者不知道干嘛用的，有知道的请告诉我，该文件顶部有我博客链接。或加Q1692475028,谢谢！
        try {
            api.handleIntent(getIntent(), this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        int wxSdkVersion = api.getWXAppSupportAPI();
        if (wxSdkVersion >= Constant.TIMELINE_SUPPORTED_VERSION) {
            initGet();
        } else if (wxSdkVersion == 0) {
            Common.staticToast("请先安装微信");
            finish();
        } else {
            Common.staticToast("当前微信版本过低，请更新后再试");
            finish();
        }
    }

    public void initGet() {
        mHandler = new MyHandler();
        if (getIntent().getStringExtra("flag") != null) {
            //必须用getExtras
            flag = getIntent().getExtras().getString("flag");
            if (getIntent().getSerializableExtra("shareInfoParam") != null) {
                ShareInfoParam shareInfoParam = (ShareInfoParam) getIntent()
                        .getSerializableExtra("shareInfoParam");
                if (!TextUtils.isEmpty(shareInfoParam.photo)) {
                    Glide.with(this).load(shareInfoParam.photo)
                            .asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource,GlideAnimation<? super Bitmap> glideAnimation) {
                            sharePicture(SendMessageToWX.Req.WXSceneSession, resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            Common.staticToasts(getBaseContext(), "分享失败", R.mipmap.icon_common_tanhao);
                        }
                    });
                } else {
                    if (!TextUtils.isEmpty(shareInfoParam.title)) {
                        currTitle = shareInfoParam.title;
                    } else {
                        currTitle = "顺联动力";
                    }
                    if (!TextUtils.isEmpty(shareInfoParam.desc)) {
                        currentDesc = shareInfoParam.desc;
                    } else {
                        currentDesc = "顺联动力商城";
                    }
                    if (!TextUtils.isEmpty(shareInfoParam.shareLink)) {
                        shareLink = shareInfoParam.shareLink;
                    } else {
                        shareLink = "www.shunliandongli.com";
                    }
                    if (!TextUtils.isEmpty(shareInfoParam.img)) {
                        String mImg = shareInfoParam.img;
                        Glide.with(this).load(mImg).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                if (flag.equals("shareFriend")) {
                                    shareUrl2Circle(shareLink, SendMessageToWX.Req.WXSceneSession,
                                            currTitle, currentDesc, resource, "friend");
                                }
                            }
                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                defShare();
                            }
                        });
                    } else {
                        defShare();
                    }
                }
            }
            if (flag.equals("login") || flag.equals("bind")) {
                login();
            }
        }
    }

    private void defShare() {
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (flag.equals("shareFriend")) {
            shareUrl2Circle(shareLink, SendMessageToWX.Req.WXSceneSession,
                    currTitle, currentDesc, img, "friend");
        }
    }

    /**
     * 构造一个用于请求的唯一标识
     *
     * @param type 分享的内容类型
     * @return
     */
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 分享图片
     */
    private void sharePicture(int shareType, Bitmap bitmap) {

        WXImageObject imgObj = new WXImageObject(bitmap);
//        imgObj.setImagePath(SAVE_PIC_PATH);
//        imgObj.setImagePath(photoUrl);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
//        Bitmap bmp = BitmapFactory.decodeFile(SAVE_PIC_PATH);
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
//        bitmap.recycle();

        //设置缩略图
        Bitmap mBp = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
//        bitmap.recycle();//不能及时回收，后面可能还有保存图片操作
        msg.thumbData = TransformUtil.bmpToByteArray(mBp, true);// 设置缩略图
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = shareType;
        api.sendReq(req);
        finish();
    }

    /**
     * @param url 要分享的链接
     */
    private void shareUrl2Circle(final String url, int type, String title, String desc, Bitmap img, String flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        if ("circle".equals(flag)) {
//            msg.title = desc;
            msg.title = title;
        } else if ("friend".equals(flag)) {
            msg.title = title;
            msg.description = desc;
        }
        if (img != null) {
            img = BitmapUtil.createBitmapThumbnail(img);
            msg.setThumbImage(img);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        api.sendReq(req);
        finish();
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
        LogUtil.zhLogW("=onResp============"+baseResp.errCode);
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
                } else {
                    if (!isEmpty(Constant.SHARE_TYPE)){
                        wxEntryPresenter.notifyShare(Constant.SHARE_TYPE,"");
                    }else {
                        finish();
                    }
                    Common.staticToast("分享成功");
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
            Common.staticToast(getString(result));
            finish();
        }
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void onWXCallback(WXLoginEntity wxLoginEntity) {
        if (wxLoginEntity != null) {
            String unique_sign = wxLoginEntity.unique_sign;
            String status = wxLoginEntity.status;
            if ("2".equals(status)) {
                //BindingPhoneAct.startAct(this, 0, unique_sign);
                RegisterAct.startAct(this,RegisterAct.UNBIND_SUPERIOR_USER,unique_sign);
            } else if ("1".equals(status)) {
                Common.staticToast("登录成功");
                SharedPrefUtil.saveSharedPrfString("token", wxLoginEntity.token);
                SharedPrefUtil.saveSharedPrfString("refresh_token", wxLoginEntity.refresh_token);
                SharedPrefUtil.saveSharedPrfString("member_id", wxLoginEntity.member_id);
            } else {
                //BindingPhoneAct.startAct(this, 1, unique_sign);
                RegisterAct.startAct(this,RegisterAct.UNBIND_NEW_USER,unique_sign);
            }
            finish();
        } else {
            finish();
        }
        finish();
    }

    @Override
    public void notifyCallback(CommonEntity commonEntity) {
        finish();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
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
