package com.shunlian.app.utils;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.App;
import com.shunlian.app.R;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.ui.CouponMsgAct;
import com.shunlian.app.newchat.ui.MessageActivity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.LuckWheelPanActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.activity.DayDayAct;
import com.shunlian.app.ui.collection.MyCollectionAct;
import com.shunlian.app.ui.confirm_order.OrderLogisticsActivity;
import com.shunlian.app.ui.core.AishangAct;
import com.shunlian.app.ui.core.GetCouponAct;
import com.shunlian.app.ui.core.HotRecommendAct;
import com.shunlian.app.ui.core.KouBeiAct;
import com.shunlian.app.ui.core.NewGetCouponAct;
import com.shunlian.app.ui.core.PingpaiAct;
import com.shunlian.app.ui.coupon.CouponGoodsAct;
import com.shunlian.app.ui.coupon.CouponListAct;
import com.shunlian.app.ui.coupon.UserCouponListAct;
import com.shunlian.app.ui.discover.jingxuan.ArticleH5Act;
import com.shunlian.app.ui.discover.other.CommentListAct;
import com.shunlian.app.ui.fragment.SortAct;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.ui.h5.H5SpecialAct;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.ui.help.HelpOneAct;
import com.shunlian.app.ui.more_credit.MoreCreditAct;
import com.shunlian.app.ui.my_profit.MyProfitAct;
import com.shunlian.app.ui.myself_store.QrcodeStoreAct;
import com.shunlian.app.ui.new_login_register.LoginEntryAct;
import com.shunlian.app.ui.new_user.NewUserPageActivity;
import com.shunlian.app.ui.order.OrderDetailAct;
import com.shunlian.app.ui.plus.GifBagListAct;
import com.shunlian.app.ui.plus.PlusGifDetailAct;
import com.shunlian.app.ui.plus.PlusOrderAct;
import com.shunlian.app.ui.plus.SuperProductsAct;
import com.shunlian.app.ui.qr_code.QrCodeAct;
import com.shunlian.app.ui.returns_order.SubmitLogisticsInfoAct;
import com.shunlian.app.ui.setting.feed_back.BeforeFeedBackAct;
import com.shunlian.app.ui.sign.SignInAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.ui.task.TaskCenterAct;
import com.shunlian.app.widget.BoldTextSpan;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.popmenu.PopMenu;
import com.shunlian.app.widget.popmenu.PopMenuItem;
import com.shunlian.app.widget.popmenu.PopMenuItemCallback;
import com.shunlian.app.wxapi.WXEntryActivity;
import com.shunlian.app.wxapi.WXEntryPresenter;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhang on 2017/4/14 11 : 42.
 */

public class Common {
    private static Toast toast, toasts;
    private static MyTextView mtv_toast, mtv_toasts, mtv_desc,ex_desc;
    private static SpannableStringBuilder ssb;
    private static MyImageView miv_logo;

    public static boolean isColor(String value) {
        if (TextUtils.isEmpty(value))
            return false;
        String type = "^#[0-9a-fA-F]{6}$";
        Pattern pattern = Pattern.compile(type);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     *
     *
     * @return true栈顶栈底都是当前act，false不唯一
     */

    public static boolean isUniqueAct(String className) {
        try {
            ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            String baseName = manager.getRunningTasks(1).get(0).baseActivity.getClassName();
            String topName = manager.getRunningTasks(1).get(0).topActivity.getClassName();
            String mBaseName = baseName.substring(baseName.lastIndexOf(".") + 1);
            String mTopName = topName.substring(topName.lastIndexOf(".") + 1);
            return (mTopName.equals(className)&&mBaseName.equals(className));
        } catch (Exception e) {
            return true;
        }
    }

    public static String transClassName(String toPage) {
        if (TextUtils.isEmpty(toPage))
            return "";
        switch (toPage) {
            case "goods":
                return "GoodsDetailAct";
            case "goToPayPlus":
                return "GifBagListAct";
            case "search":
                return "SearchGoodsActivity";
            case "checkin":
                return "SignInAct";
            case "myshop":
                return "QrcodeStoreAct";
            case "find":
            case "my":
            case "cart":
            case "home":
                return "MainActivity";
            case "myvoucherlist":
            case "voucherlist":
                return "CouponListAct";
            case "login":
                return "LoginEntryAct";
            case "article":
                return "ArticleH5Act";
            case "artdetails":
                return "CommentListAct";
            case "myorder":
                return "OrderDetailAct";
            case "hotpush":
                return "HotRecommendAct";
            case "special":
                return "H5SpecialAct";
            case "slyoupin":
                return "SuperProductsAct";
            case "slmall":
                return "TaskCenterAct";
            case "benefit":
                return "DayDayAct";
            case "sale":
                return "PingpaiAct";
            case "loveyoupin":
                return "AishangAct";
            case "praise":
                return "KouBeiAct";
            case "commission":
                return "MyProfitAct";
            case "coupon":
                return "GetCouponAct";
            case "order":
                return "OrderDetailAct";
            case "shipping":
                return "OrderLogisticsActivity";
            case "shop":
                return "StoreAct";
            case "plus":
                return "MainActivity";
            case "plusOrder":
                return "PlusOrderAct";
            case "plusdetail":
            case "pulsdetail":
                return "PlusGifDetailAct";
            case "chat":
                return "ChatActivity";
            case "virtual":
                return "MoreCreditAct";
            case "usecoupon":
                return "UserCouponListAct";
            case "discountgoods":
                return "CouponGoodsAct";
            case "invite":
                return "QrCodeAct";
            case "taskTurnTable":
                return "LuckWheelPanActivity";
            case "newuser":
            case "olduser":
                return "NewUserPageActivity";
            case "url":
            case "noTitleUrl":
                return "H5Act";
            case "HTMLShare":
                return "WXEntryActivity";
            case "attentionList":
                return "MainActivity";
            case "submitlogisticsinfo":
                return "SubmitLogisticsInfoAct";
            case "voucher":
                return "CouponMsgAct";
            case "taskSystems":
                return "TaskCenterAct";
            default:
                return "";
        }
    }


    public static void goGoGo(Context context, String type, String... params) {
        //params从第7个参数开始是聊天的参数
        String token = SharedPrefUtil.getSharedUserString("token", "");
        LogUtil.augusLogW("where---" + type);
        if (type == null) {
            return;
        }
        switch (type) {
            case "popLastView":
                ((Activity) context).finish();
                break;
            case "noTitleUrl":
                H5X5Act.startAct(context, params[0], H5X5Act.MODE_SONIC,"noTitle");
                break;
            case "taskSystems":
                TaskCenterAct.startAct(context);
                break;
            case "taskTurnTable":
                LuckWheelPanActivity.startAct(context);
                break;
            case "HTMLShare":
                if (!TextUtils.isEmpty(params[0])){
                    copyText(context,params[1],params[3],false);
                    ShareInfoParam shareInfoParam = new ShareInfoParam();
                    if ("1".equals(params[0])){
                        shareInfoParam.photo = params[2];
                        WXEntryActivity.startAct(context, "shareFriend", shareInfoParam);
                    }else if ("2".equals(params[0])){
//                        shareInfoParam.shareLink = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=95684227_hao_pg&wd=Rxjava%20%2Bre&oq=android%25E5%25AF%25B9url%25E8%25A7%25A3%25E7%25A0%2581&rsv_pq=dc1f4fe400029324&rsv_t=3a61SdbfGVa6%2BW9Dsq4tVpuDsLQVpB7q0%2BGONNSdL6LBQ35u0c1y7BXBoGsrkj2Hu1dcTf0U&rqlang=cn&rsv_enter=1&rsv_n=2&rsv_sug3=1&rsv_sug2=0&inputT=909&rsv_sug4=909";
                        shareInfoParam.shareLink = params[1];
                        shareInfoParam.title = params[3];
                        shareInfoParam.desc = params[4];
                        shareInfoParam.img = params[2];
                        WXEntryActivity.startAct(context, "shareFriend", shareInfoParam);
                    }
                }
                break;
            case "virtual":
                MoreCreditAct.startAct(context);
                break;
            case "invite":
                QrCodeAct.startAct(context,params[0]);
                break;
            case "goods":
                GoodsDetailAct.startAct(context, params[0]);
                break;
            case "plusOrder":
                PlusOrderAct.startAct(context);
                break;
            case "shop":
                StoreAct.startAct(context, params[0]);
                break;
            case "nojump":
                LogUtil.augusLogW("gogogo---nojump");
                break;
            case "shipping":
                OrderLogisticsActivity.startAct(context,params[0]);
                break;
            case "order":
                OrderDetailAct.startAct(context,params[0]);
                break;
            case "categories":
                SortAct.startAct(context);
                break;
            case "coupon":
                if (TextUtils.isEmpty(token)) {
                    Common.goGoGo(context,"login");
                    theRelayJump(type,params);
                } else {
//                    GetCouponAct.startAct(context);
                    NewGetCouponAct.startAct(context);
                }
                break;
            case "voucher":
                if (TextUtils.isEmpty(token)) {
                    Common.goGoGo(context,"login");
                    theRelayJump(type,params);
                } else {
                    CouponMsgAct.startAct(context,params[0]);
                }
//                Common.staticToast("优惠券");
                break;
//            case "gift":
//                //todo
//                Common.staticToast("买赠");
//                break;
//            case "discount":
//                //todo
//                Common.staticToast("满额");
//                break;
//            case "combo":
//                //todo
//                Common.staticToast("套餐");
//                break;
//            case "pingtuan":
//                //todo
//                Common.staticToast("优惠拼单");
//                break;
            case "praise":
                KouBeiAct.startAct(context);
                break;
            case "loveyoupin":
                AishangAct.startAct(context);
                break;
            case "sale":
                PingpaiAct.startAct(context);
                break;
            case "benefit":
                DayDayAct.startAct(context);
                break;
            case "myshop":
                if (!TextUtils.isEmpty(params[0]))
                    QrcodeStoreAct.startAct(context,params[0]);
                break;
            case "checkin":
                SignInAct.startAct(context);
                break;
            case "home":
                MainActivity.startAct(context, "");
                break;
//            case "fanslist":
//                //todo
//                Common.staticToast("粉丝列表");
//                break;
//            case "popshare":
//                //todo
//                Common.staticToast("弹出分享");
//                break;
            case "commission":
                if (TextUtils.isEmpty(token)) {
                    Common.goGoGo(context,"login");
                    theRelayJump(type,params);
                } else {
                    MyProfitAct.startAct(context, false);
                }
                break;
//            case "myvoucherlist":
//                //todo
//                Common.staticToast("我的优惠券");
//                break;
            case "myvoucherlist":
            case "voucherlist":
                if (TextUtils.isEmpty(token)) {
                    Common.goGoGo(context,"login");
                    theRelayJump(type,params);
                } else {
                    CouponListAct.startAct(context);
                }
                break;
            case "goToPayPlus":
                GifBagListAct.startAct(context);
                break;
            case "plusdetail":
            case "pulsdetail":
                PlusGifDetailAct.startAct(context, params[0]);
                break;
            case "login":
                theRelayJump(null,null);
                LoginEntryAct.startAct(context);
                break;
//            case "article":
//                ArticleH5Act.startAct(context, params[0], ArticleH5Act.MODE_SONIC);
//                break;
            case "artdetails":
                CommentListAct.startAct((Activity) context, params[0]);
                break;
            case "myorder"://我的订单
                if (TextUtils.isEmpty(token)) {
                    Common.goGoGo(context,"login");
                    theRelayJump(type,params);
                } else {
                    OrderDetailAct.startAct(context, params[0]);
                }
                break;
            case "hotpush":
                HotRecommendAct.startAct(context, params[0],params[1]);
                break;
            case "my":
            case "personCenter"://个人中心
                MainActivity.startAct(context, "personCenter");
                break;
            case "cart":
            case "shoppingcar"://购物车
                MainActivity.startAct(context, "shoppingcar");
                break;
            case "message"://消息
                MessageActivity.startAct(context);
                break;
            case "feedback"://反馈
                if (params != null && params.length > 0)
                    BeforeFeedBackAct.startAct(context, params[0]);
                else
                    BeforeFeedBackAct.startAct(context, null);
                break;
            case "help"://帮助
                HelpOneAct.startAct(context);
                break;
            case "search"://搜索
                SearchGoodsActivity.startAct((Activity) context, "", "sortFrag");
                break;
            case "collection"://收藏
                MyCollectionAct.startAct(context, null);
                break;
            case "find"://收藏
                MainActivity.startAct(context, "discover");
                break;
            case "footprint"://足迹
                MyCollectionAct.startAct(context, MyCollectionAct.FOOTPRINT_FLAG);
                break;
            case "special":
                String url = InterentTools.H5_HOST + "special/" + params[0];
//                String url = InterentTools.H5_HOST + "special/127";
                H5SpecialAct.startAct(context, url, H5X5Act.MODE_SONIC);
                break;
            case "url":
//                LogUtil.augusLogW("uiui-"+params[0]);
//                H5Act.startAct(context, params[0], H5Act.MODE_SONIC);
                H5X5Act.startAct(context, params[0], H5X5Act.MODE_SONIC);
                break;
            case "slyoupin"://顺联优品
                SuperProductsAct.startAct(context);
                break;
            case "slmall"://任务中心
                TaskCenterAct.startAct(context);
                break;
            case "plus":
                MainActivity.startAct(context, "myplus");
                break;
            case "usecoupon":
                UserCouponListAct.startAct(context,params[0]);
                break;
            case "discountgoods":
                if (params != null && params.length > 0){
                    String id = "";
                    if (params != null && params.length > 1){
                        id = params[1];
                    }
                    CouponGoodsAct.startAct(context,params[0],id);
                }
                break;
            case "chat"://聊天
//               Common.goGoGo(context, toPage, id, id1,id2, id3,id4, id5,id6,to_shop_id, from_shop_id, from_nickname, from_type, to_type, from_user_id, to_user_id);

                if (TextUtils.isEmpty(token)) {
                    Common.goGoGo(context,"login");
                    theRelayJump(type,params);
                } else {

                    ChatMemberEntity.ChatMember chatMember = new ChatMemberEntity.ChatMember();

                    if (!"0".equals(params[7])) {//赋值shopId
                        chatMember.shop_id = params[7];
                    } else if (!"0".equals(params[8])) {
                        chatMember.shop_id = params[8];
                    }
                    chatMember.nickname = params[9];
                    chatMember.type = params[10];
                    chatMember.m_user_id = params[12];

                    //0，普通用户，1平台客服管理员，2平台普通客服，3商家客服管理员，4商家普通客服
                    ChatManager.getInstance(context).init().switch2jumpChat(params[10], params[11], chatMember);
                }
                break;
            case "submitlogisticsinfo"://提交物流信息
                SubmitLogisticsInfoAct.startAct(context,params[0],SubmitLogisticsInfoAct.APPLY);
                break;
            case "newuser"://新人专享
            case "olduser":
                NewUserPageActivity.startAct(context);
                break;
            case "attentionList"://发现关注列表

                break;
            default://首页
                MainActivity.startAct(context, "");
                break;
        }
    }

    /**
     * 接力跳转
     * @param type
     * @param items
     */
    public static void theRelayJump(String type,String[] items){
        DispachJump dispachJump = new DispachJump();
        dispachJump.jumpType = type;
        dispachJump.items = items;
        EventBus.getDefault().postSticky(dispachJump);
    }

    public static String getURLParameterValue(String url, String parameter) {
        Matcher accessMatcher = Pattern.compile(parameter + "=(.+?)(?:&|$)").matcher(url);
        String parameterValue = null;
        if (accessMatcher.find()) {
            parameterValue = accessMatcher.group(1);
        }
        return parameterValue;
    }


    public static long getMemoryFreeSize(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        return info.availMem;
//        Log.i(tag,"系统剩余内存:"+(info.availMem /1024)+"k");
//        Log.i(tag,"系统是否处于低内存运行："+info.lowMemory);
//        Log.i(tag,"当系统剩余内存低于"+info.threshold+"时就看成低内存运行");
    }

    public static long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
//        return (freeBlocks * blockSize)/1024 /1024; //单位MB
    }

    //判断是否有sd卡
    public static boolean hasSD() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取屏幕宽
     *
     * @param ac
     * @return
     */
    public static int getScreenWidth(Activity ac) {
        if (ac!=null){
            return ac.getWindowManager().getDefaultDisplay().getWidth();
        }else {
           return 720;
        }
    }


    /**
     * 获取屏幕高
     *
     * @param ac
     * @return
     */
    public static int getScreenHeight(Activity ac) {
        return ac.getWindowManager().getDefaultDisplay().getHeight();
    }

    //获取经纬度
    public static Location getGPS(final Activity activity) {
        // 获取位置管理服务
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) activity.getSystemService(serviceName);

        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

//        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息

        Location location = null;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); // 通过GPS获取位置
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            if (location == null) {
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); // 通过NETWORK获取位置
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                }
            }
            if (location == null)
                locateDialog(activity);
            return location;
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); // 通过NETWORK获取位置
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            if (location == null)
                locateDialog(activity);
            return location;
        } else {
            locateDialog(activity);
            return null;
        }
    }

    public static void locateDialog(Activity activity) {
        PromptDialog promptDialog = new PromptDialog(activity);
        promptDialog.setTvSureColor(R.color.new_text);
        promptDialog.setTvSureBg(R.drawable.bg_dialog_bottomr);
        promptDialog.setSureAndCancleListener("无法获取您的位置信息，请在设置中打开定位功能.", "去开启", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri packageURI = Uri.parse("package:" + "com.shunlian.app");
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    activity.startActivity(intent);
                } catch (Exception e) {
                    activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
                promptDialog.dismiss();
            }
        }, "取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
            }
        }).show();
    }

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static Context getApplicationContext() {
        return App.mApp;
    }

    /**
     * 获取只有文件
     *
     * @return
     */
    public static Resources getResources() {
        return getApplicationContext().getResources();
    }


    public static boolean regularPwd(String pwd) {
//        String regular = "^[[^a-zA-Z0-9]+\\w]{8,16}$";
        String regular = "^(?!\\d+$)(?![a-zA-Z]+$)(?![^a-zA-Z0-9]+$)[[^a-zA-Z0-9]+\\w]{8,16}$";
        boolean matches = Pattern.matches(regular, pwd);
        return matches;
    }

    private static Toast exToast;
    private static LottieAnimationView ex_animation_view;

    private static Toast toastAnim;
    private static LottieAnimationView animation_view;

    public static void staticAnimToast(String content,String desc,String jsonAnim) {
        if (TextUtils.isEmpty(content))
            return;
        if (toastAnim == null) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast_anim_json, null);
            mtv_toast =  v.findViewById(R.id.mtv_toasts);
            mtv_desc=  v.findViewById(R.id.mtv_desc);
            animation_view=  v.findViewById(R.id.animation_view);
            mtv_toast.setText(content);
            mtv_desc.setText(desc);
            toastAnim = new Toast(getApplicationContext());
//            LinearLayout.LayoutParams vlp = new LinearLayout.LayoutParams(App.widthPixels,
//                    App.hightPixels);
//            vlp.setMargins(0, 0, 0, 0);
//            show_toast.setLayoutParams(vlp);
//            toast = Toast.makeText(getApplicationContext(), "ceshi", Toast.LENGTH_SHORT);
            toastAnim.setDuration(Toast.LENGTH_LONG);
            toastAnim.setView(v);
            toastAnim.setGravity(Gravity.FILL, 0, 0);
            showAnimJsonFile(jsonAnim,"images/img_3.png");
        } else {
            mtv_toast.setText(content);
            mtv_desc.setText(desc);
            showAnimJsonFile(jsonAnim,"images/img_3.png");
        }
        toastAnim.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toastAnim.show();
    }

    private static void showAnimJsonFile(String jsonAnim,String defaultImage){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                animation_view.setAnimation(jsonAnim);//在assets目录下的动画json文件名。
                animation_view.loop(false);//设置动画循环播放
                animation_view.setImageAssetsFolder("images/");//assets目录下的子目录，存放动画所需的图片
                animation_view.playAnimation();//播放动画
            } else {
                AssetManager assets = getApplicationContext().getAssets();
                animation_view.setImageBitmap(BitmapFactory.decodeStream(assets.open(defaultImage)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void staticAnimNewToast(String content,String desc,String jsonAnim)  {
        if (TextUtils.isEmpty(content))
            return;
        if (exToast == null) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast_anim_new_json, null);
            ex_desc=  v.findViewById(R.id.mtv_desc);
            ex_animation_view=  v.findViewById(R.id.animation_view);
            ex_desc.setText(content);
            exToast =  new Toast(getApplicationContext());
            exToast.setDuration(Toast.LENGTH_LONG);
            exToast.setView(v);
//            exToast.setAnimations(R.style.ClickToast);
            exToast.setGravity(Gravity.FILL, 0, 0);
        } else {
            ex_desc.setText(desc);
        }
        exToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ex_animation_view, "alpha", 0f, 1f);
//        objectAnimator.setDuration(300);
//        objectAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//
//            }});
         exToast.show();
        try {
            showAnimExJsonFile(jsonAnim,"images/img_11.png");
        }catch (Exception e){
            e.printStackTrace();
        }
//        objectAnimator.start();
    }

    private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private static void showAnimExJsonFile(String jsonAnim,String defaultImage){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ex_animation_view.setAnimation(jsonAnim);//在assets目录下的动画json文件名。
                ex_animation_view.loop(false);//设置动画循环播放
                ex_animation_view.setImageAssetsFolder("images/");//assets目录下的子目录，存放动画所需的图片
                Thread.sleep(200);
                ex_animation_view.playAnimation();//播放动画
            } else {
                AssetManager assets = getApplicationContext().getAssets();
                ex_animation_view.setImageBitmap(BitmapFactory.decodeStream(assets.open(defaultImage)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void staticToast(String content) {
        if (TextUtils.isEmpty(content))
            return;
        if (toast == null) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast, null);
            mtv_toast = (MyTextView) v.findViewById(R.id.mtv_toast);
            mtv_toast.setText(content);
            toast = new Toast(getApplicationContext());
//            toast = Toast.makeText(getApplicationContext(), "ceshi", Toast.LENGTH_SHORT);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(v);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mtv_toast.setText(content);
        }
        toast.show();
    }

    public static void staticToasts(Context context, String content, String desc, int imgSource) {
        initToast(context, content, desc, imgSource);
    }

    public static void staticToasts(Context context, String content, int imgSource) {
        initToast(context, content, "", imgSource);
    }

    //隐藏虚拟键盘
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    public static boolean HasAliPay(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    //显示虚拟键盘
    public static void showKeyboard(View v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 获取占位符 一个占位符代表一个3个字节
     *
     * @param num
     * @return
     */
    public static String getPlaceholder(int num) {
        String str = "\u3000";
        if (num == 1)str += " ";
        for (int i = 0; i < num - 1; i++) {
            str = str.concat("\u3000");
        }
        return str;
    }

    public static Boolean checkIsVisible(Context context, View view) {
        // 如果已经加载了，判断广告view是否显示出来，然后曝光
        int screenWidth = DeviceInfoUtil.getDeviceWidth(context);
        int screenHeight = DeviceInfoUtil.getDeviceHeight(context);
        Rect rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        if (view.getLocalVisibleRect(rect)) {
            return true;
        } else {
            //view已不在屏幕可见区域;
            return false;
        }
    }

    /**
     * 格式化float 四舍五入保留两位小数
     *
     * @param f
     * @return
     */
    public static String formatFloat(float f) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String format = decimalFormat.format(f);
        return format;
    }

    /**
     * 格式化float 四舍五入保留两位小数
     *
     * @param f
     * @return
     */
    public static String formatFloat(String f) {
        float v = 0;
        if (!TextUtils.isEmpty(f)) {
            v = Float.parseFloat(f);
        }
        return formatFloat(v);
    }

    /**
     * 格式化float 四舍五入保留两位小数
     *
     * @param
     * @return
     */
    public static String formatFloat(float f1, float f2) {
        return formatFloat(f1 - f2);
    }

    /**
     * 格式化float 四舍五入保留两位小数
     *
     * @param
     * @return
     */
    public static String formatFloat(String f1, String f2) {
        float v1 = 0;
        if (!TextUtils.isEmpty(f1)) {
            v1 = Float.parseFloat(f1);
        }
        float v2 = 0;
        if (!TextUtils.isEmpty(f2)) {
            v2 = Float.parseFloat(f2);
        }
        return formatFloat(v1 - v2);
    }

    /**
     * 将点后面的字变小
     *
     * @param source
     * @param textSize 需要变小的文字大小
     * @return
     */
    public static SpannableStringBuilder dotAfterSmall(String source, int textSize) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(textSize, true);
        if (ssb == null)
            ssb = new SpannableStringBuilder();
        ssb.clear();
        ssb.append(source);
        int start = source.indexOf(".") + 1;
        if (start > 0) {
            ssb.setSpan(sizeSpan, start, source.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

    /**
     * 更改源字符串的颜色
     *
     * @param source    源字符串
     * @param changeStr 需要改变颜色的字符串
     * @param color     变化的颜色
     * @return
     */
    public static SpannableStringBuilder changeColor(String source, String changeStr, @ColorInt int color) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        if (ssb == null)
            ssb = new SpannableStringBuilder();
        ssb.clear();
        ssb.append(source);
        int i=-1;
        if (!TextUtils.isEmpty(changeStr))
            i = source.indexOf(changeStr);
        if (i == -1) {
            return ssb;
        } else {
            ssb.setSpan(colorSpan, i, i + changeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

    /**
     * 更改源字符串的大小
     *
     * @param source    源字符串
     * @param changeStr 需要改变大小的字符串
     * @param textSize  变化的文字大小  单位dip
     * @return
     */
    public static SpannableStringBuilder changeTextSize(String source, String changeStr, int textSize) {
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(textSize, true);
        if (ssb == null)
            ssb = new SpannableStringBuilder();
        ssb.clear();
        ssb.append(source);
        if (TextUtils.isEmpty(changeStr))
            return ssb;
        int i=-1;
        if (!TextUtils.isEmpty(changeStr))
            i = source.indexOf(changeStr);
        if (i == -1) {
            return ssb;
        } else {
            ssb.setSpan(sizeSpan, i, i + changeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

    /**
     * 更改源字符串的大小和颜色
     *
     * @param source    源字符串
     * @param changeStr 需要改变大小的字符串
     * @param textSize  变化的文字大小  单位dip
     * @return
     */
    public static SpannableStringBuilder changeColorAndSize(String source, String changeStr, int textSize, @ColorInt int color) {

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);


        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(textSize, true);

        if (ssb == null)
            ssb = new SpannableStringBuilder();
        ssb.clear();
        ssb.append(source);
        int i=-1;
        if (!TextUtils.isEmpty(changeStr))
        i = source.indexOf(changeStr);
        if (i == -1) {
            return ssb;
        } else {
            ssb.setSpan(colorSpan, i, i + changeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(sizeSpan, i, i + changeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

    /**
     * 更改源字符串的大小和颜色
     *
     * @param source    源字符串
     * @param changeStr 需要改变大小的字符串
     * @return
     */
    public static SpannableStringBuilder changeColors(String source, @ColorInt int color, String... changeStr) {
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        if (ssb == null)
            ssb = new SpannableStringBuilder();
        ssb.clear();
        ssb.append(source);
        for (int i = 0; i < changeStr.length; i++) {
            int m = source.indexOf(changeStr[i]);
            if (m == -1) {
                return ssb;
            } else {
                //        如果要改变字符串中多处字体颜色，setSpan方法中第一个参数必须要每次new一个对象出来才能显示效果
                ssb.setSpan(new ForegroundColorSpan(color), m + 2, m + changeStr[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (i >= changeStr.length - 1) {
                return ssb;
            }
        }
        return ssb;
    }

    /**
     * 文字加粗
     *
     * @param source    源字符串
     * @param changeStr 需要加粗的字符串
     * @return
     */
    public static SpannableStringBuilder changetextbold(String source, String changeStr) {
        BoldTextSpan boldTextSpan = new BoldTextSpan();
        if (ssb == null)
            ssb = new SpannableStringBuilder();
        ssb.clear();
        ssb.append(source);
        int i=-1;
        if (!TextUtils.isEmpty(changeStr))
            i = source.indexOf(changeStr);
        if (i == -1) {
            return ssb;
        } else {
            ssb.setSpan(boldTextSpan, i, i + changeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

    /**
     * 判断是否已经登录，登录返回true 否则false
     *
     * @return
     */
    public static boolean isAlreadyLogin() {
        String token = SharedPrefUtil.getSharedUserString("token", "");
        String member_id = SharedPrefUtil.getSharedUserString("member_id", "");
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(member_id)) {
            return true;
        }
        return false;
    }

    /**
     * 获取url对应的域名
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        if (TextUtils.isEmpty(url)||!url.startsWith("http"))
            return "";
        try {
            String result = "";
            int j = 0, startIndex = 0, endIndex = url.length();
            for (int i = 0; i < url.length(); i++) {
                if (url.charAt(i) == '/') {
                    j++;
                    if (j == 2)
                        startIndex = i;
                    else if (j == 3)
                        endIndex = i;
                }

            }
            result = url.substring(startIndex + 1, endIndex);
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 如果没有登录提示《请先登录》 并返回true
     *
     * @return
     */
    public static boolean loginPrompt() {
        if (!isAlreadyLogin()) {
            Common.staticToast(getResources().getString(R.string.plase_login));
            return true;
        }
        return false;
    }

    /**
     * 清空登录信息
     */
    public static void clearLoginInfo() {
        SharedPrefUtil.clearSharedPreferences();
    }

    /**
     * 判断应用是否在运行
     *
     * @param context
     * @return
     */
    public static boolean isRun(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String MY_PKG_NAME = "com.ad";
        //100表示取的最大的任务数，info.topActivity表示当前正在运行的Activity，info.baseActivity表系统后台有此进程在运行
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
//                Log.i("ActivityService isRun()",info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="+info.baseActivity.getPackageName());
                break;
            }
        }

//        Log.i("ActivityService isRun()", "com.ad 程序   ...isAppRunning......"+isAppRunning);
        return isAppRunning;
    }

    public static String formatBadgeNumber(int value) {
        if (value <= 0) {
            return null;
        }

        if (value < 100) {
            return Integer.toString(value);
        }

        return "99+";
    }

    /**
     * @param plusRoleCode
     * @return
     */
    public static int plusRoleCode(String plusRoleCode) {
        int resid = 0;
        if (TextUtils.isEmpty(plusRoleCode)) {
            plusRoleCode = "0";
        }
        int code = Integer.parseInt(plusRoleCode);
        switch (code) {//1 普通 2黄金  3钻石 4皇冠;
            case 1:
                resid = R.mipmap.img_putong;
                break;
            case 2:
                resid = R.mipmap.img_jinpai;
                break;
            case 3:
                resid = R.mipmap.img_zuanshi;
                break;
            case 4:
                resid = R.mipmap.img_huangguan;
                break;
        }
        return resid;
    }

    public static void copyText(Context context, String content){
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        Common.staticToast("复制成功");
    }

    public static void copyText(Context context, String shareLink, String shareDesc,boolean isToast) {
        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
//        if (!TextUtils.isEmpty(shareTitle)) {
//            sb.append(shareTitle);
//            sb.append("\n");
//        }
        if (!TextUtils.isEmpty(shareDesc)) {
            sb.append(shareDesc);
            sb.append("\n");
        }
        if (!TextUtils.isEmpty(shareLink)) {
            sb.append(shareLink);
        }
        if (!TextUtils.isEmpty(shareLink)&&shareLink.contains("slAppWord")) {
            sb.append("\n");
            sb.append("复制这条信息，打开顺联APP~");
        }
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(sb.toString());
        if (isToast)
        staticToasts(context, "复制链接成功", R.mipmap.icon_common_duihao);
        Constant.SHARE_LINK = sb.toString();
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void openWeiXin(Context context, String type, String id) {
        if (isWeixinAvilible(context)) {
            try {
                Intent intent = new Intent();
                ComponentName cmp = new ComponentName("com.tencent.mm",
                        "com.tencent.mm.ui.LauncherUI");
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                context.startActivity(intent);
                if (!TextUtils.isEmpty(type)) {
                    WXEntryPresenter wxEntryPresenter = new WXEntryPresenter(context, null);
                    wxEntryPresenter.notifyShare(type, id);
                }
            } catch (Exception e) {
                staticToast("打开微信失败，请重试");
                e.printStackTrace();
            }
        } else {
            staticToast("请安装微信后重试!");
        }
    }

    public static void initToast(Context context, String content, String desc, int imgSource) {
        if (TextUtils.isEmpty(content)&&TextUtils.isEmpty(desc))
            return;
        if (toasts == null) {
            View v = LayoutInflater.from(context).inflate(R.layout.toasts, null);
            mtv_toasts =  v.findViewById(R.id.mtv_toasts);
            mtv_desc =  v.findViewById(R.id.mtv_desc);
            miv_logo =  v.findViewById(R.id.miv_logo);
            toasts = new Toast(context);
//            toast = Toast.makeText(getApplicationContext(), "ceshi", Toast.LENGTH_SHORT);
            toasts.setDuration(Toast.LENGTH_SHORT);
            toasts.setView(v);
            toasts.setGravity(Gravity.CENTER, 0, 0);
        }
        mtv_toasts.setText(content);
        if (imgSource > 0) {
            miv_logo.setVisibility(View.VISIBLE);
            miv_logo.setImageResource(imgSource);
        }else {
            miv_logo.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(desc)) {
            mtv_desc.setVisibility(View.GONE);
        } else {
            mtv_desc.setText(desc);
            mtv_desc.setVisibility(View.VISIBLE);
        }
        toasts.show();
    }

    /**
     * 判断昵称是否合法
     *
     * @param nickname 最多12个汉字 24个字母
     * @return 昵称过长返回false
     */
    public static boolean isNicknameLegitimate(String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            return true;
        }
        String Reg = "^[\u4e00-\u9fa5]{1}$";  //汉字的正规表达式
        int charLength = 0;
        String[] split = nickname.split("");
        for (int i = 0; i < split.length; i++) {
            if (Pattern.matches(Reg, split[i])) {
                charLength += 2;
            } else {
                charLength++;
            }
        }
        if (charLength > 25) {
            Common.staticToast(getResources().getString(R.string.RegisterTwoAct_ncszgc));
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断字符是否是汉字
     */
    public static boolean isChineseCharacters(String s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static void urlToPage(Context context, String url) {
        //LogUtil.httpLogW("链接:" + url);
        if (TextUtils.isEmpty(url))
            return;
        try {
            url=java.net.URLDecoder.decode(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (url.startsWith("slmall://")) {
            String type = interceptBody(url);
            if (!TextUtils.isEmpty(type)) {
                String id = "";
                String id1 = "";
                String id2 = "";
                String id3 = "";
                String id4 = "";
                try {
                    if (!TextUtils.isEmpty(Common.getURLParameterValue(url, "id")))
                        id=java.net.URLDecoder.decode(Common.getURLParameterValue(url, "id"));
                    if (!TextUtils.isEmpty(Common.getURLParameterValue(url, "id1")))
                        id1 = java.net.URLDecoder.decode(Common.getURLParameterValue(url, "id1"));
                    if (!TextUtils.isEmpty(Common.getURLParameterValue(url, "id2")))
                        id2 = java.net.URLDecoder.decode(Common.getURLParameterValue(url, "id2"));
                    if (!TextUtils.isEmpty(Common.getURLParameterValue(url, "id3")))
                        id3 = java.net.URLDecoder.decode(Common.getURLParameterValue(url, "id3"));
                    if (!TextUtils.isEmpty(Common.getURLParameterValue(url, "id4")))
                        id4 = java.net.URLDecoder.decode(Common.getURLParameterValue(url, "id4"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Common.goGoGo(context, type, id, id1,id2,id3,id4);
            }
        }
    }

    /**
     * 完整url slmall://goods/item.json?goodsId=138471
     * 截取之后goods/item.json
     *
     * @param url
     * @return
     */
    private static String interceptBody(String url) {
        String[] split = url.split("\\?");
        String s = split[0];
        if (!TextUtils.isEmpty(s)) {
            String[] split1 = s.split("//");
            if (!TextUtils.isEmpty(split1[1])) {
                return split1[1];
            }
        }
        return null;
    }

    /**
     * 截取商品id
     *
     * @param url
     * @return
     */
    private static String interceptId(String url) {
        String[] split = url.split("\\?");
        String s = split[1];
        String[] split1 = s.split("=");
        String s1 = split1[1];
        return s1;
    }

    /**
     * 解析剪切板内容
     *
     * @param context
     */
    public static void parseClipboard(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (!TextUtils.isEmpty(cm.getText()) && cm.getText().toString().contains("slmall://")) {
            Common.urlToPage(context, cm.getText().toString());
            cm.setText("");
        }
    }

    /**
     * 判断是否是plus会员
     *
     * @return
     */

    public static boolean isPlus() {
        String plus = SharedPrefUtil.getSharedUserString("plus_role", "");
        if (!TextUtils.isEmpty(plus) && Integer.parseInt(plus) > 0)
            return true;
        return false;
    }


    /**
     * 判断某个Activity 界面是否在前台
     * @param context
     * @param className 某个界面名称 getClass().getName()
     * @return
     */
    public static boolean  isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;

    }

}
