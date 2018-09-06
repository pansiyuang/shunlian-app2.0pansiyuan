package com.shunlian.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;

import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SwitchHostUtil;
import com.shunlian.app.utils.sideslip.ActivityHelper;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;

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
    public static App mApp;
    private ActivityHelper mActivityHelper;
    private static Context context;
    public static String CACHE_PATH;
    public static String DOWNLOAD_PATH;

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
        //初始化bugly,建议在测试阶段建议设置成true，发布时设置为false。
        if (BuildConfig.DEBUG) {
            Constant.BUGLY_ID="9f3fcbbba0";//测试
        }
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(this, Constant.BUGLY_ID, BuildConfig.DEBUG);
        CrashReport.initCrashReport(getApplicationContext(), Constant.BUGLY_ID, BuildConfig.DEBUG);
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
    }
}
