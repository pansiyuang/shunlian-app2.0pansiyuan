package com.shunlian.app.ui;

import android.annotation.TargetApi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.SharedPrefUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.Unbinder;


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
 * Created by zhang on 2017/4/13 16 : 37.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        initListener();
        initData();
        SharedPrefUtil.saveSharedPrfString("localVersion", getVersionName());
    }

    /**
     * 布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化监听事件，如果需要监听重新即可
     */
    protected void initListener() {
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    public String getVersionName() {
        String versionName = null;
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    //设置https请求头
    public void setHeader() {
        SharedPrefUtil.saveSharedPrfString("User-Agent", "Shunlian Android " + Build.VERSION.RELEASE + "/" + SharedPrefUtil.getSharedPrfString("localVersion", "1.0.0"));
        SharedPrefUtil.saveSharedPrfString("X-Device-ID", UUID.nameUUIDFromBytes(Build.SERIAL.getBytes()).toString().toUpperCase());
        SharedPrefUtil.saveSharedPrfString("resolution", DeviceInfoUtil.getDeviceRatio(this));
        SharedPrefUtil.saveSharedPrfString("deviceWidth", String.valueOf(DeviceInfoUtil.getDeviceWidth(this)));
        SharedPrefUtil.saveSharedPrfString("deviceHeight", String.valueOf(DeviceInfoUtil.getDeviceHeight(this)));
        SharedPrefUtil.saveSharedPrfString("UserAgent", DeviceInfoUtil.getUserAgent(this));
        SharedPrefUtil.saveSharedPrfString("DeviceIp", DeviceInfoUtil.getDeviceIp(this));
    }

    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上
     * lightStatusBar为真时表示黑色字体
     */
    private void processMIUI(boolean lightStatusBar) {
        Class<? extends Window> clazz = getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(getWindow(), lightStatusBar ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception ignored) {

        }
    }


    /**
     * 处理Lollipop以上
     * <p>
     * Lollipop可以设置为沉浸，不能设置字体颜色(所以白色背景会很丑)
     * <p>
     * M(API23)可以设定
     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void processLollipopAbove(boolean lightStatusBar, boolean transparentStatusBar) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        int flag = getWindow().getDecorView().getSystemUiVisibility();

        if (lightStatusBar) {
        /**

         * see {@link <a href="https://developer.android.com/reference/android/R.attr.html#windowLightStatusBar"></a>}

         */
            flag |= (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (transparentStatusBar) {
            //改变字体颜色
            flag |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }
        getWindow().getDecorView().setSystemUiVisibility(flag);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    /**
     * 显示view
     * @param views
     */
    protected void visible(View... views){
       if (views != null && views.length > 0){
           for (int i = 0; i < views.length; i++) {
               views[i].setVisibility(View.VISIBLE);
           }
       }
    }

    /**
     * 隐藏view
     * @param views
     */
    protected void gone(View... views){
        if (views != null && views.length > 0){
            for (int i = 0; i < views.length; i++) {
                views[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
