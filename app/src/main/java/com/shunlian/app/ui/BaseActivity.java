package com.shunlian.app.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.mylibrary.BarHide;
import com.shunlian.mylibrary.ImmersionBar;

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
        ImmersionBar.with(this).init();
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        finishAct();
        initListener();
        initData();
        SharedPrefUtil.saveSharedPrfString("localVersion", getVersionName());
    }

    private void finishAct() {
        View byId = ButterKnife.findById(this, R.id.miv_close);
        byId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        SharedPrefUtil.saveSharedPrfString("Net-Type", NetworkUtils.getNetWorkStatusName(this));
    }

    /**
     * 设置状态栏的颜色
     * @param statusBarColor
     */
    public void setStatusBarColor(@ColorRes int statusBarColor){
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(statusBarColor).init();
    }

    /**
     * 当状态栏颜色为白色
     * 设置状态栏字体的颜色，仅限于魅族flymeos4以上，小米MIUI6以上，Android6.0以上
     * 如果不支持修改颜色就给状态栏设置为透明度
     */
    public void setStatusBarFontDark(){
        ImmersionBar.with(this).statusBarDarkFont(true,0.2f).init();
    }

    /**
     * 隐藏状态栏
     */
    public void setHideStatus(){
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
    }

    /**
     * 如果此设备有导航栏的话，调用该方法隐藏导航栏
     */
    public void setHideNavigation(){
        if (ImmersionBar.hasNavigationBar(this))
            ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
    }

    /**
     * 隐藏导航栏和状态栏
     */
    public void setHideStatusAndNavigation(){
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
    }

    /**
     * 恢复显示导航栏和状态栏
     */
    public void setShowStatusAndNavigation(){
        ImmersionBar.with(this).hideBar(BarHide.FLAG_SHOW_BAR).init();
    }
    /**
     * 显示view
     * @param views
     */
    protected void visible(View... views){
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }



    /**
     * 隐藏view
     * @param views
     */
    protected void gone(View... views){
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null){
            unbinder.unbind();
        }
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
