package com.shunlian.app.wxapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.ShareInfoEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.my_profit.SexSelectAct;
import com.shunlian.app.ui.new3_login.New3LoginAct;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.mylibrary.OSUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler, WXEntryView {

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
        wxEntryPresenter = new WXEntryPresenter(this, this);
        //初始注册方法必须有，即使就算第二次回调启动的时候先调用onResp，也必须有注册方法，否则会出错
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);

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
            mYFinish();
        } else {
            Common.staticToast("当前微信版本过低，请更新后再试");
            mYFinish();
        }
    }

    public void initGet() {
        mHandler = new MyHandler();
        //必须用getExtras
        Intent intent = getIntent();
        if (intent.getExtras() != null)
            flag = intent.getExtras().getString("flag");
        ShareInfoParam shareInfoParam = (ShareInfoParam) intent.getSerializableExtra("shareInfoParam");
        if (!isEmpty(flag)) SharedPrefUtil.saveCacheSharedPrf("wx_flag", flag);
        if (!isEmpty(flag) && shareInfoParam != null) {
            if (!isEmpty(shareInfoParam.photo)) {
                downloadPic(shareInfoParam);
            } else {
                defShare(shareInfoParam);
            }
        }
    }

    //分享添加默认数据
    private void defShare(ShareInfoParam shareInfoParam) {
        if (!isEmpty(shareInfoParam.shop_name)) {
            currTitle = shareInfoParam.shop_name;
            shareInfoParam.img = shareInfoParam.shop_logo;
            currentDesc = "想要生活有乐趣！打开顺联动力购物去～";
        } else {
            if (!isEmpty(shareInfoParam.title)) {
                currTitle = shareInfoParam.title;
            } else {
                currTitle = "顺联动力";
            }
            if (!isEmpty(shareInfoParam.desc)) {
                currentDesc = shareInfoParam.desc;
            } else {
                currentDesc = "要想生活有乐趣！打开顺联动力购物去～";
            }
        }
        if (!isEmpty(shareInfoParam.shareLink)) {
            shareLink = shareInfoParam.shareLink;
        } else {
            shareLink = "www.shunliandongli.com";
        }
        if (!isEmpty(shareInfoParam.img)) {
            Glide.with(this).load(shareInfoParam.img).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (flag.equals("shareFriend")) {
                        shareUrl2Circle(shareLink, SendMessageToWX.Req.WXSceneSession,
                                currTitle, currentDesc, resource, "friend");
                    } else if (flag.equals("shareCircle")) {
                        shareUrl2Circle(shareLink, SendMessageToWX.Req.WXSceneTimeline,
                                currTitle, currentDesc, resource, "circle");
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

    //下载图片成功后去分享
    private void downloadPic(ShareInfoParam shareInfoParam) {
        try {
            if (shareInfoParam.photo.startsWith("http")) {
                Glide.with(this).load(shareInfoParam.photo)
                        .asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        if (flag.equals("shareFriend")) {
                            sharePicture(SendMessageToWX.Req.WXSceneSession, resource);
                        } else if (flag.equals("shareCircle")) {
                            sharePicture(SendMessageToWX.Req.WXSceneTimeline, resource);
                        }

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        Common.staticToasts(baseAct,
                                "分享失败", R.mipmap.icon_common_tanhao);
                        mYFinish();
                    }
                });
            } else {
                Glide.with(this).load(Base64.decode(shareInfoParam.photo, Base64.DEFAULT))
                        .asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        if (flag.equals("shareFriend")) {
                            sharePicture(SendMessageToWX.Req.WXSceneSession, resource);
                        } else if (flag.equals("shareCircle")) {
                            sharePicture(SendMessageToWX.Req.WXSceneTimeline, resource);
                        }

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        Common.staticToasts(baseAct,
                                "分享失败", R.mipmap.icon_common_tanhao);
                        mYFinish();
                    }
                });
            }
        }catch (Exception e){
            Common.staticToasts(baseAct,
                    "分享失败", R.mipmap.icon_common_tanhao);
            mYFinish();
        }
    }

    private void defShare() {
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (flag.equals("shareFriend")) {
            shareUrl2Circle(shareLink, SendMessageToWX.Req.WXSceneSession,
                    currTitle, currentDesc, img, "friend");
        } else if (flag.equals("shareCircle")) {
            shareUrl2Circle(shareLink, SendMessageToWX.Req.WXSceneTimeline,
                    currTitle, currentDesc, img, "circle");
        }
    }

    /**
     * 构造一个用于请求的唯一标识
     *
     * @param type 分享的内容类型
     * @return
     */
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
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
        mYFinish();
    }

    /**
     * @param url 要分享的链接
     */
    private void shareUrl2Circle(final String url, int type, String title,
                                 String desc, Bitmap img, String flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        if ("circle".equals(flag)) {
//            msg.title = desc;
            msg.title = title;
            msg.description = desc;
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
        mYFinish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
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
                //区分登录还是分享
                String wx_flag = SharedPrefUtil.getCacheSharedPrf("wx_flag", "");
                if (isEmpty(wx_flag)) {
                    SendAuth.Resp sendAuthResp = (SendAuth.Resp) baseResp;// 用于分享时不要有这个，不能强转
                    String code = sendAuthResp.code;
                    wxEntryPresenter.newWXLogin(code);
                } else {
                    if (!isEmpty(Constant.SHARE_TYPE)) {
                        if ("goods".equals(Constant.SHARE_TYPE)
                                || "income".equals(Constant.SHARE_TYPE) || "store".equals(Constant.SHARE_TYPE)) {
                            //用于分享领金蛋
                            wxEntryPresenter.goodsShare(Constant.SHARE_TYPE, Constant.SHARE_ID);
                        } else if ("article".equals(Constant.SHARE_TYPE)) {
                            //用于分享领金蛋
                            wxEntryPresenter.goodsShare(Constant.SHARE_TYPE, Constant.SHARE_ID);
                            //用于分享统计
                            wxEntryPresenter.notifyShare(Constant.SHARE_TYPE, Constant.SHARE_ID);
                        } else {
                            //用于分享统计
                            wxEntryPresenter.notifyShare(Constant.SHARE_TYPE, Constant.SHARE_ID);
                        }
                    } else {
                        mYFinish();
                    }
                    Common.staticToast("分享成功");
                }
                SharedPrefUtil.saveCacheSharedPrf("wx_flag", "");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                Constant.SHARE_TYPE = "";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                Constant.SHARE_TYPE = "";
                break;
            default:
                result = R.string.errcode_unknown;
                Constant.SHARE_TYPE = "";
                break;
        }

        if (baseResp.errCode != BaseResp.ErrCode.ERR_OK) {
            Common.staticToast(getString(result));
            mYFinish();
        }
    }

    @Override
    public void onWXCallback(BaseEntity<LoginFinishEntity> entity) {
        if (entity != null && entity.data != null) {
            LoginFinishEntity wxLoginEntity = entity.data;
            String unique_sign = wxLoginEntity.unique_sign;
            //String mobile = wxLoginEntity.mobile;
            String member_id = wxLoginEntity.member_id;
            String status = wxLoginEntity.status;

            if ("1".equals(status)) {//登录成功
                loginSuccess(entity, wxLoginEntity);
            } else {
                New3LoginAct.LoginConfig config = new New3LoginAct.LoginConfig();
                config.login_mode = New3LoginAct.LoginConfig.LOGIN_MODE.SMS_TO_LOGIN;
                config.unique_sign = unique_sign;
                config.member_id = member_id;
                config.status = status;
                New3LoginAct.startAct(this, config);
                mYFinish();
            }

        } else {
            mYFinish();
        }
    }

    private void loginSuccess(BaseEntity<LoginFinishEntity> entity, LoginFinishEntity wxLoginEntity) {
        //Common.staticToast(entity.message);
        SharedPrefUtil.saveSharedUserString("token", wxLoginEntity.token);
        SharedPrefUtil.saveSharedUserString("refresh_token", wxLoginEntity.refresh_token);
        SharedPrefUtil.saveSharedUserString("member_id", wxLoginEntity.member_id);
        SharedPrefUtil.saveSharedUserString("avatar", wxLoginEntity.avatar);
        SharedPrefUtil.saveSharedUserString("nickname", wxLoginEntity.nickname);
        SharedPrefUtil.saveSharedUserString("plus_role", wxLoginEntity.plus_role);
        SensorsDataAPI.sharedInstance().login(SharedPrefUtil.getSharedUserString("member_id", ""));
        CrashReport.setUserId(wxLoginEntity.member_id);
        if (wxLoginEntity.tag != null)
            SharedPrefUtil.saveSharedUserStringss("tags", new HashSet<>(wxLoginEntity.tag));
        JpushUtil.setJPushAlias();
        //通知登录成功
        DefMessageEvent event = new DefMessageEvent();
        event.loginSuccess = true;
        EventBus.getDefault().post(event);

        EasyWebsocketClient.getInstance(this).initChat(); //初始化聊天
        MessageCountManager.getInstance(this).initData();

        if (!"1".equals(wxLoginEntity.is_tag)) {
            SexSelectAct.startAct(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && OSUtils.isMIUI()) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    @Override
    public void notifyCallback(CommonEntity commonEntity) {
        mYFinish();
    }

    @Override
    public void golde_eggs(String eggs) {
        if (!isEmpty(eggs) && !"0".equals(eggs)) {
            String tip = "恭喜获得%s金蛋";
            ShareInfoEvent event = new ShareInfoEvent();
            event.isShareSuccess = true;
            event.type = Constant.SHARE_TYPE;
            event.eggs_count = String.format(tip, eggs);
            EventBus.getDefault().post(event);
        }
        Constant.SHARE_TYPE = "";
        cloasePage();
    }

    @Override
    public void cloasePage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && OSUtils.isMIUI()) {
            finishAndRemoveTask();
        } else {
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
    protected void onDestroy() {
        super.onDestroy();
    }

    private void mYFinish() {
        finish();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //showSaveSuccessDialog();
                    break;
                case 2:
                    Common.staticToast("保存图片失败,请重试");
                    mYFinish();
                    break;
                case 3:
                    Common.staticToast("分享失败,请重试");
                    mYFinish();
                    break;
                case 4:
                    Common.staticToast("手机内存不足");
                    mYFinish();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
