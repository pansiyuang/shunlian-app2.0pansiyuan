package com.shunlian.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.view.Display;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.SwitchHostUtil;
import com.shunlian.app.utils.sideslip.ActivityHelper;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


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

/**
 * Created by zhang on 2017/4/13 15 : 17.
 */

public class App extends Application {
    /**
     * Sensors Analytics 采集数据的地址
     */
    private final static String SA_SERVER_URL = "http://shence.sldlcdn.com/sa?project=default";
    private final static String SA_OFFLINE_SERVER_URL = "http://shence.sldlcdn.com/sa?project=default";
    private final SensorsDataAPI.DebugMode SA_DEBUG_MODE = SensorsDataAPI.DebugMode.DEBUG_AND_TRACK;
    private final SensorsDataAPI.DebugMode SA_OFFLINE_MODE = SensorsDataAPI.DebugMode.DEBUG_OFF;
    public static App mApp;
    private ActivityHelper mActivityHelper;
    private static Context context;
    public static String CACHE_PATH;
    public static String DOWNLOAD_PATH;
    public static int widthPixels = 0;
    public static int hightPixels = 0;
    public static ActivityHelper getActivityHelper() {
        return mApp.mActivityHelper;
    }

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    public static Context getContext() {
        return context;
    }
    private void initJPush() {
        try {
            JPushInterface.setDebugMode(BuildConfig.DEBUG);    // 设置开启日志,发布时请关闭日志
            JPushInterface.init(this);            // 初始化 JPush
            JpushUtil.setJPushAlias();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        context = getApplicationContext();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        widthPixels = dm.widthPixels;
        hightPixels = dm.heightPixels;
        //初始化bugly,建议在测试阶段建议设置成true，发布时设置为false。
        if (BuildConfig.DEBUG) {
            Constant.BUGLY_ID="9f3fcbbba0";//测试
        }
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(this, Constant.BUGLY_ID, BuildConfig.DEBUG);
        CrashReport.initCrashReport(getApplicationContext(), Constant.BUGLY_ID, BuildConfig.DEBUG);
        String member_id = SharedPrefUtil.getSharedUserString("member_id","");
        CrashReport.setUserId(member_id);
        //bugly动态设置会被manifest中的设置所覆盖
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
//            strategy.setAppChannel("myChannel");  //设置渠道
            strategy.setAppVersion(packageInfo.versionName);      //App的版本
//            strategy.setAppPackageName("com.tencent.xx");  //App的包名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //注册侧滑返回生命周期回调
        mActivityHelper = new ActivityHelper();
        registerActivityLifecycleCallbacks(mActivityHelper);

        if (Common.hasSD() && Common.getSDFreeSize() > 1024 * 1024) {
            CACHE_PATH = Constant.CACHE_PATH_EXTERNAL;
            DOWNLOAD_PATH = Constant.DOWNLOAD_PATH_EXTERNAL;
            try {
                File dirs = new File(App.CACHE_PATH);
                if (!dirs.exists()) {
                    dirs.mkdirs();
                }
                File dirss = new File(App.DOWNLOAD_PATH);
                if (!dirss.exists()) {
                    dirss.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Common.getMemoryFreeSize(this) > 1024 * 1024) {
            CACHE_PATH = this.getCacheDir().getPath();
            DOWNLOAD_PATH = this.getCacheDir().getPath();
        }
        initJPush();
        //默认关闭，去首页获取开关状态,此方法除非卸载app，重启也有效保留状态
        JPushInterface.stopPush(Common.getApplicationContext());
        if (BuildConfig.DEBUG){
            SwitchHostUtil.setHostMethod();
//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                return;

//            }
//            LeakCanary.install(this);
        }

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtil.augusLogW(" onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        //优化x5内核首次启动卡顿黑屏现象
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
        initSensorsDataAPI();
    }

    /**
     * 初始化 Sensors Analytics SDK
     */
    private void initSensorsDataAPI() {
        SensorsDataAPI.sharedInstance(
                this,                               // 传入 Context
                SA_SERVER_URL,                      // 数据接收的 URL
                BuildConfig.DEBUG?SA_OFFLINE_MODE:SA_OFFLINE_MODE);                     // Debug 模式选项
        // 打开自动采集, 并指定追踪哪些 AutoTrack 事件
        List<SensorsDataAPI.AutoTrackEventType> eventTypeList = new ArrayList<>();
        // $AppStart
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_START);
        // $AppEnd
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_END);
        // $AppViewScreen
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_VIEW_SCREEN);
        // $AppClick
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_CLICK);
        SensorsDataAPI.sharedInstance(this).enableAutoTrack(eventTypeList);
        SensorsDataAPI.sharedInstance().trackFragmentAppViewScreen();

        if(Common.isAlreadyLogin()) {
            SensorsDataAPI.sharedInstance().login(SharedPrefUtil.getSharedUserString("member_id", ""));
        }
    }

}
