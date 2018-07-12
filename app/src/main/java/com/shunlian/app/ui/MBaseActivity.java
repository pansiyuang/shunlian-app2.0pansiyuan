package com.shunlian.app.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class MBaseActivity extends FragmentActivity implements View.OnClickListener{
    private Unbinder unbinder;
    private Resources resources;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void finishAct() {
        View byId = ButterKnife.findById(this, R.id.miv_close);
        if (byId != null) {
            TransformUtil.expandViewTouchDelegate(byId, 50, 50, 50, 50);
            byId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Constant.JPUSH != null && Common.isBottomActivity(Common.transClassName(Constant.JPUSH.get(0)))) {
                        MainActivity.startAct(getBaseContext(), "");
                    }
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
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
        SharedPrefUtil.saveSharedPrfString("User-Agent", "ShunLian Android " + Build.VERSION.RELEASE + "/" + SharedPrefUtil.getSharedPrfString("localVersion", "1.0.0"));
        SharedPrefUtil.saveSharedPrfString("X-Device-ID", UUID.nameUUIDFromBytes(Build.SERIAL.getBytes()).toString().toUpperCase());
        SharedPrefUtil.saveSharedPrfString("resolution", DeviceInfoUtil.getDeviceRatio(this));
        SharedPrefUtil.saveSharedPrfString("deviceWidth", String.valueOf(DeviceInfoUtil.getDeviceWidth(this)));
        SharedPrefUtil.saveSharedPrfString("deviceHeight", String.valueOf(DeviceInfoUtil.getDeviceHeight(this)));
        SharedPrefUtil.saveSharedPrfString("UserAgent", DeviceInfoUtil.getUserAgent(this));
        SharedPrefUtil.saveSharedPrfString("DeviceIp", DeviceInfoUtil.getDeviceIp(this));
        SharedPrefUtil.saveSharedPrfString("Net-Type", NetworkUtils.getNetWorkStatusName(this));
    }

    public Map<String, String> setWebviewHeader() {
        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("User-Agent", SharedPrefUtil.getSharedPrfString("User-Agent", "ShunLian Android 5.1.1/1.0.0"));
        extraHeaders.put("X-Device-ID", SharedPrefUtil.getSharedPrfString("X-Device-ID", "744D9FC3-5DBD-3EDD-A589-56D77BDB0E5D"));
        extraHeaders.put("resolution", SharedPrefUtil.getSharedPrfString("resolution", "720x1184"));
        extraHeaders.put("DeviceIp", SharedPrefUtil.getSharedPrfString("DeviceIp", "192.168.1.1"));
        extraHeaders.put("Accept-Encoding", "gzip,deflate");
        extraHeaders.put("Content-Type", "application/json");
        extraHeaders.put("Net-Type", SharedPrefUtil.getSharedPrfString("Net-Type", ""));
        extraHeaders.put("SAFE-TYPE", SharedPrefUtil.getSharedPrfString("SAFE-TYPE", "ON"));
        return extraHeaders;
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
     * @param colorResouce
     */
    protected int getColorResouce(int colorResouce) {
        return resources.getColor(colorResouce);
    }

    /**
     * 获取图片资源
     *
     * @param drawableResouce
     */
    protected Drawable getDrawableResouce(int drawableResouce) {
        return resources.getDrawable(drawableResouce);
    }

    /**
     * 判断集合内容是否为空
     *
     * @param list
     * @return
     */
    protected boolean isEmpty(List list) {
        if (list == null) {
            return true;
        }

        if (list.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    protected boolean isEmpty(CharSequence sequence) {
        return TextUtils.isEmpty(sequence);
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    protected void setEdittextFocusable(boolean focusable, EditText... editText) {
        for (int i = 0; i < editText.length; i++) {
            editText[i].setFocusable(focusable);
            editText[i].setFocusableInTouchMode(focusable);
            if (focusable) {
                editText[i].requestFocus();
            }
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
    public void onBackPressed() {
        if (Constant.JPUSH != null && Common.isBottomActivity(Common.transClassName(Constant.JPUSH.get(0)))) {
            MainActivity.startAct(getBaseContext(), "");
        }
        super.onBackPressed();
    }
}
