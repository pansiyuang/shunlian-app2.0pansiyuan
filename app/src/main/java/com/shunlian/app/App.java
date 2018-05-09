package com.shunlian.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.sideslip.ActivityHelper;

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
    }
}
