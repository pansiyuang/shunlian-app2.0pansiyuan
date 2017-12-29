package com.shunlian.app.ui;

import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.broadcast.NetDialog;
import com.shunlian.app.broadcast.NetworkBroadcast;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.mylibrary.BarHide;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private Unbinder unbinder;
    public ImmersionBar immersionBar;
    private NetworkBroadcast networkBroadcast;
    private Resources resources;
    public static Map<Integer,NetDialog> dialogLists = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        immersionBar = ImmersionBar.with(this);
        immersionBar.init();
        setContentView(getLayoutId());
        resources = getResources();
        unbinder = ButterKnife.bind(this);
        finishAct();
        initListener();
        initData();
        SharedPrefUtil.saveSharedPrfString("localVersion", getVersionName());

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkBroadcast = new NetworkBroadcast();
        registerReceiver(networkBroadcast, filter);
        networkBroadcast.setOnUpdateUIListenner(new NetworkBroadcast.UpdateUIListenner() {

            @Override
            public void updateUI(boolean isShow) {
                showPopup(isShow);
            }
        });
    }

    public void showPopup(boolean isShow){
        boolean b = dialogLists.containsKey(this.hashCode());
        if (!b){
            NetDialog netDialog = new NetDialog(this);
            dialogLists.put(this.hashCode(),netDialog);
        }
        if (isShow) {
            dialogLists.get(this.hashCode()).show();
        }else {
            dismissDialog(true);
        }
    }

    private void finishAct() {
        View byId = ButterKnife.findById(this, R.id.miv_close);
        if (byId != null) {
            TransformUtil.expandViewTouchDelegate(byId, 50, 50, 50, 50);
            byId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
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

    public Map<String, String> setWebviewHeader(){
        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("User-Agent", SharedPrefUtil.getSharedPrfString("User-Agent", "Shunlian Android 5.1.1/1.0.0"));
        extraHeaders.put("X-Device-ID", SharedPrefUtil.getSharedPrfString("X-Device-ID", "744D9FC3-5DBD-3EDD-A589-56D77BDB0E5D"));
        extraHeaders.put("resolution", SharedPrefUtil.getSharedPrfString("resolution", "720x1184"));
        extraHeaders.put("DeviceIp", SharedPrefUtil.getSharedPrfString("DeviceIp", "192.168.1.1"));
        extraHeaders.put("Accept-Encoding", "gzip,deflate");
        extraHeaders.put("Content-Type", "application/json");
        extraHeaders.put("Net-Type", SharedPrefUtil.getSharedPrfString("Net-Type",""));
        extraHeaders.put("SAFE-TYPE", SharedPrefUtil.getSharedPrfString("SAFE-TYPE", "ON"));
        return extraHeaders;
    }
    /**
     * 设置状态栏的颜色
     *
     * @param statusBarColor
     */
    public void setStatusBarColor(@ColorRes int statusBarColor) {
        if (immersionBar == null){
            immersionBar = ImmersionBar.with(this);
        }
        immersionBar.fitsSystemWindows(true).statusBarColor(statusBarColor).init();
    }

    /**
     * 当状态栏颜色为白色
     * 设置状态栏字体的颜色，仅限于魅族flymeos4以上，小米MIUI6以上，Android6.0以上
     * 如果不支持修改颜色就给状态栏设置为透明度
     */
    public void setStatusBarFontDark() {
        if (immersionBar == null){
            immersionBar = ImmersionBar.with(this);
        }
        immersionBar.statusBarDarkFont(true, 0.2f).init();
    }

    /**
     * 隐藏状态栏
     */
    public void setHideStatus() {
        if (immersionBar == null){
            immersionBar = ImmersionBar.with(this);
        }
        immersionBar.hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
    }

    /**
     * 如果此设备有导航栏的话，调用该方法隐藏导航栏
     */
    public void setHideNavigation() {
        if (immersionBar == null){
            immersionBar = ImmersionBar.with(this);
        }
        if (ImmersionBar.hasNavigationBar(this))
            immersionBar.hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
    }

    /**
     * 隐藏导航栏和状态栏
     */
    public void setHideStatusAndNavigation() {
        if (immersionBar == null){
            immersionBar = ImmersionBar.with(this);
        }
        immersionBar.hideBar(BarHide.FLAG_HIDE_BAR).init();
    }

    /**
     * 恢复显示导航栏和状态栏
     */
    public void setShowStatusAndNavigation() {
        if (immersionBar == null){
            immersionBar = ImmersionBar.with(this);
        }
        immersionBar.hideBar(BarHide.FLAG_SHOW_BAR).init();
    }

    /**
     * 显示view
     *
     * @param views
     */
    protected void visible(View... views) {
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
     *
     * @param views
     */
    protected void gone(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void setEdittextFocusable(boolean focusable,EditText... editText){
        for (int i = 0; i < editText.length; i++) {
            editText[i].setFocusable(focusable);
            editText[i].setFocusableInTouchMode(focusable);
            if (focusable){
                editText[i].requestFocus();
            }
        }
    }

    /**
     * 获取文字资源
     *
     * @param stringResouce
     */
    protected String getStringResouce(int stringResouce) {
        return resources.getString(stringResouce);
    }

    /**
     * 获取颜色资源
     *
     *  @param colorResouce
     */
    protected int getColorResouce(int colorResouce) {
        return resources.getColor(colorResouce);
    }

    /**
     * 获取图片资源
     *
     *  @param drawableResouce
     */
    protected Drawable getDrawableResouce(int drawableResouce) {
        return resources.getDrawable(drawableResouce);
    }

    /**
     * 判断集合内容是否为空
     * @param list
     * @return
     */
    protected boolean isEmpty(List list){
        if (list == null){
            return true;
        }

        if (list.size() == 0){
            return true;
        }else {
            return false;
        }
    }

    protected boolean isEmpty(CharSequence sequence){
        return TextUtils.isEmpty(sequence);
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
        if (immersionBar != null){
            immersionBar.destroy();
        }
        if (networkBroadcast != null){
            unregisterReceiver(networkBroadcast);
        }
        dismissDialog(false);
    }

    private void dismissDialog(boolean isClearAll) {
        if (!isClearAll){
            NetDialog netDialog = dialogLists.get(hashCode());
            if (netDialog != null && netDialog.isShowing()){
                netDialog.dismiss();
                netDialog = null;
            }
            return;
        }
        if (dialogLists != null && dialogLists.size() > 0){
            Iterator<Integer> iterator = dialogLists.keySet().iterator();
            while (iterator.hasNext()){
                Integer next = iterator.next();
                NetDialog netDialog = dialogLists.get(next);
                if (netDialog != null && netDialog.isShowing()){
                    netDialog.dismiss();
                }
                netDialog = null;
            }
            dialogLists.clear();
        }
    }
    @Override
    public void onClick(View view) {
        if (FastClickListener.isFastClick()) {
            return;
        }
    }
}
