package com.shunlian.app.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.shunlian.app.App;
import com.shunlian.app.R;
import com.shunlian.app.broadcast.NetDialog;
import com.shunlian.app.broadcast.NetworkBroadcast;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.sideslip.SlideBackHelper;
import com.shunlian.app.utils.sideslip.SlideConfig;
import com.shunlian.app.utils.sideslip.callbak.OnSlideListenerAdapter;
import com.shunlian.app.utils.sideslip.widget.SlideBackLayout;
import com.shunlian.app.widget.CommondDialog;
import com.shunlian.mylibrary.BarHide;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pay.PayListActivity;


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

    protected Unbinder unbinder;
    public ImmersionBar immersionBar;
    private NetworkBroadcast networkBroadcast;
    private Resources resources;
    public static Map<Integer,NetDialog> dialogLists = new HashMap<>();
    private SlideBackLayout mSlideBackLayout;
    private OnSlideListenerAdapter mSlideBackListener;
    protected CommondDialog commondDialog;


    @Override
    protected void onRestart() {
        if (!(this instanceof ConfirmOrderAct) &&
                !(this instanceof PayListActivity) &&
                !(this instanceof SearchGoodsActivity)) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (!isEmpty(cm.getText())) {
                if (commondDialog == null)
                    commondDialog = new CommondDialog(this);
                commondDialog.parseCommond();
            }
        }
        super.onRestart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!(this instanceof MainActivity) &&
                !(this instanceof ConfirmOrderAct) &&
                !(this instanceof PayListActivity) &&
                !(this instanceof SearchGoodsActivity)) {
            try{
                openSideslipCallback();
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/

        immersionBar = ImmersionBar.with(this);
        immersionBar.init();
        setContentView(getLayoutId());
        resources = getResources();
        unbinder = ButterKnife.bind(this);
        finishAct();
        initListener();
        initData();
//        SharedPrefUtil.saveCacheSharedPrf("localVersion", getVersionName());

    }

    /**
     * 初始化侧滑返回
     */
    private void openSideslipCallback() {
        mSlideBackLayout = SlideBackHelper.attach(
                // 当前Activity
                this, App.getActivityHelper(),
                // 参数的配置
                new SlideConfig.Builder()
                        // 屏幕是否旋转
                        .rotateScreen(false)
                        // 是否侧滑
                        .edgeOnly(true)
                        // 是否禁止侧滑
                        .lock(false)
                        // 侧滑的响应阈值，0~1，对应屏幕宽度*percent
                        .edgePercent(0.1f)
                        // 关闭页面的阈值，0~1，对应屏幕宽度*percent
                        .slideOutPercent(0.3f).create(),
                // 滑动的监听
                new OnSlideListenerAdapter() {
                    @Override
                    public void onSlide(@FloatRange(from = 0.0,
                            to = 1.0) float percent) {
                        super.onSlide(percent);
                        if (mSlideBackListener != null)
                            mSlideBackListener.onSlide(percent);
                    }

                    @Override
                    public void onClose() {
                        super.onClose();
                        if (mSlideBackListener != null)
                            mSlideBackListener.onClose();
                    }

                    @Override
                    public void onOpen() {
                        super.onOpen();
                        if (mSlideBackListener != null)
                            mSlideBackListener.onOpen();
                    }
                });
    }

    /**
     * 获取侧滑边沿百分比
     * @return
     */
    public float getSlideBackEdgePercent() {
        if (mSlideBackLayout != null) {
            return mSlideBackLayout.getEdgeRangePercent();
        }
        return 0.1f;
    }

    /**
     * 侧滑是否锁定
     * @return
     */
    public boolean getIsSlideBackLock(){
        if (mSlideBackLayout != null) {
            return mSlideBackLayout.isLock();
        }
        return true;
    }

    /**
     * 侧滑监听
     * @param slideBackListener
     */
    public void setSlideBackListener(OnSlideListenerAdapter slideBackListener){
        mSlideBackListener = slideBackListener;
    }
    /**
     * 打开侧滑
     */
    protected void openSideslip(){
        if (mSlideBackLayout != null){
            mSlideBackLayout.lock(false);
        }
    }

    /**
     * 关闭侧滑
     */
    protected void closeSideslip(){
        if (mSlideBackLayout != null){
            mSlideBackLayout.lock(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(this instanceof PayListActivity) || !(this instanceof ConfirmOrderAct)){
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            networkBroadcast = new NetworkBroadcast();
            registerReceiver(networkBroadcast, filter);
            networkBroadcast.setOnUpdateUIListenner(isShow ->showPopup(isShow));
        }
    }

    public void showPopup(boolean isShow){
        if (!isShow && dialogLists.size() == 0)return;
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

    protected void finishAct() {
        View byId = findViewById(R.id.miv_close);
        if (byId != null) {
            TransformUtil.expandViewTouchDelegate(byId, 50, 50, 50, 50);
            byId.setOnClickListener(v ->  {
                    if (Constant.JPUSH!=null&&Common.isBottomActivity(Common.
                            transClassName(Constant.JPUSH.get(0)))){
                        MainActivity.startAct(getBaseContext(),"");
                    }
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    finish();
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

//    public String getVersionName() {
//        String versionName = null;
//        PackageManager pm = getPackageManager();
//        try {
//            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
//            versionName = info.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return versionName;
//    }

    //设置https请求头
    public void setHeader() {
        SharedPrefUtil.saveCacheSharedPrf("User-Agent", "ShunLian Android " + Build.VERSION.RELEASE + "/" + SharedPrefUtil.getCacheSharedPrf("localVersion", "1.0.0"));
        SharedPrefUtil.saveCacheSharedPrf("X-Device-ID", UUID.nameUUIDFromBytes(Build.SERIAL.getBytes()).toString().toUpperCase());
        SharedPrefUtil.saveCacheSharedPrf("resolution", DeviceInfoUtil.getDeviceRatio(this));
        SharedPrefUtil.saveCacheSharedPrf("deviceWidth", String.valueOf(DeviceInfoUtil.getDeviceWidth(this)));
        SharedPrefUtil.saveCacheSharedPrf("deviceHeight", String.valueOf(DeviceInfoUtil.getDeviceHeight(this)));
        SharedPrefUtil.saveCacheSharedPrf("UserAgent", DeviceInfoUtil.getUserAgent(this));
        SharedPrefUtil.saveCacheSharedPrf("DeviceIp", DeviceInfoUtil.getDeviceIp(this));
        SharedPrefUtil.saveCacheSharedPrf("Net-Type", NetworkUtils.getNetWorkStatusName(this));
    }

    public Map<String, String> setWebviewHeader(){
        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("User-Agent", SharedPrefUtil.getCacheSharedPrf("User-Agent", "ShunLian Android 5.1.1/1.0.0"));
        extraHeaders.put("X-Device-ID", SharedPrefUtil.getCacheSharedPrf("X-Device-ID", "744D9FC3-5DBD-3EDD-A589-56D77BDB0E5D"));
        extraHeaders.put("resolution", SharedPrefUtil.getCacheSharedPrf("resolution", "720x1184"));
        extraHeaders.put("DeviceIp", SharedPrefUtil.getCacheSharedPrf("DeviceIp", "192.168.1.1"));
        extraHeaders.put("Accept-Encoding", "gzip,deflate");
        extraHeaders.put("Content-Type", "application/json");
        extraHeaders.put("Net-Type", SharedPrefUtil.getCacheSharedPrf("Net-Type",""));
        extraHeaders.put("SAFE-TYPE", SharedPrefUtil.getCacheSharedPrf("SAFE-TYPE", "ON"));
        extraHeaders.put("Token", SharedPrefUtil.getSharedUserString("token", ""));
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
     * @param isFullScreen 设置全屏显示,true全屏
     */
    public void setFullScreen(boolean isFullScreen){
        if (immersionBar == null){
            immersionBar = ImmersionBar.with(this);
        }
        immersionBar.fullScreen(isFullScreen).init();
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
    protected void onStop() {
        super.onStop();
        if (networkBroadcast != null && (!(this instanceof PayListActivity)
                || !(this instanceof ConfirmOrderAct))){
            unregisterReceiver(networkBroadcast);
            networkBroadcast = null;
        }
        dismissDialog(false);
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
        GlideUtils.getInstance().clearMemory();
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
        if (MyOnClickListener.isFastClick()) {
            return;
        }
        mOnClick(view);
    }
    public void mOnClick(View view){

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Constant.JPUSH!=null&&Common.isBottomActivity(Common.transClassName(Constant.JPUSH.get(0)))){
            MainActivity.startAct(getBaseContext(),"");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        if (Constant.JPUSH!=null&&Common.isBottomActivity(Common.transClassName(Constant.JPUSH.get(0)))){
            MainActivity.startAct(getBaseContext(),"");
        }
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (Constant.JPUSH!=null&&Common.isBottomActivity(Common.transClassName(Constant.JPUSH.get(0)))){
            MainActivity.startAct(getBaseContext(),"");
        }
        super.onBackPressed();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GlideUtils.getInstance().clearMemory();
    }
}
