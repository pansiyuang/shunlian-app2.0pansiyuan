package com.shunlian.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.shunlian.app.utils.sideslip.ActivityHelper;


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

    public static ActivityHelper getActivityHelper() {
        return mApp.mActivityHelper;
    }

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        //注册侧滑返回生命周期回调
        mActivityHelper = new ActivityHelper();
        registerActivityLifecycleCallbacks(mActivityHelper);
    }
}
