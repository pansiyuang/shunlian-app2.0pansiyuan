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

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.shunlian.app.App;
import com.shunlian.app.R;
import com.shunlian.app.widget.MyTextView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by zhang on 2017/4/14 11 : 42.
 */

public class Common {

    private static Toast toast;
    private static MyTextView mtv_toast;
    private static AbsoluteSizeSpan sizeSpan;
    private static SpannableStringBuilder ssb;
    private static PromptDialog promptDialog;

    //获取经纬度
    public static Location getGPS(final Activity activity){
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
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); // 通过GPS获取位置
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            if(location == null){
                if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); // 通过NETWORK获取位置
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                }
            }
            return location;
        }
        else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); // 通过NETWORK获取位置
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            return location;
        } else{
            promptDialog = new PromptDialog(activity);
            promptDialog.setTvSureColor(R.color.new_text);
            promptDialog.setTvSureBg(R.drawable.bg_dialog_bottomr);
            promptDialog.setSureAndCancleListener("无法获取您的位置信息，请在设置中打开定位功能.", "去开启", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    promptDialog.dismiss();
                }
            }, "取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    promptDialog.dismiss();
                }
            }).show();

            return null;
        }
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

    public static void staticToast(String content) {
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

    //隐藏虚拟键盘
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
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
        String str = "";
        for (int i = 0; i < num; i++) {
            str += "\u3000";
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
     * 首个文字大写
     */
    public static void firstSmallText(TextView tv, String str, int size) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        SpannableString sp = new SpannableString(str);
        sp.setSpan(new AbsoluteSizeSpan(size), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(sp, TextView.BufferType.SPANNABLE);
    }

    /**
     * 格式化float 四舍五入保留两位小数
     * @param f
     * @return
     */
    public static String formatFloat(float f) {
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        String format = decimalFormat.format(f);
        return format;
    }

    /**
     * 格式化float 四舍五入保留两位小数
     * @param f
     * @return
     */
    public static String formatFloat(String f){
        float v = 0;
        if (!TextUtils.isEmpty(f)){
            v = Float.parseFloat(f);
        }
        return formatFloat(v);
    }

    /**
     * 格式化float 四舍五入保留两位小数
     * @param
     * @return
     */
    public static String formatFloat(float f1,float f2){
        return formatFloat(f1 - f2);
    }

    /**
     * 格式化float 四舍五入保留两位小数
     * @param
     * @return
     */
    public static String formatFloat(String f1,String f2){
        float v1 = 0;
        if (!TextUtils.isEmpty(f1)){
            v1 = Float.parseFloat(f1);
        }
        float v2 = 0;
        if (!TextUtils.isEmpty(f2)){
            v2 = Float.parseFloat(f2);
        }
        return formatFloat(v1 - v2);
    }

    /**
     * 将点后面的字变小
     * @param source
     * @param textSize 需要变小的文字大小
     * @return
     */
    public static SpannableStringBuilder dotAfterSmall(String source,int textSize){
        if (TextUtils.isEmpty(source)){
            return null;
        }
        if (sizeSpan == null)
            sizeSpan = new AbsoluteSizeSpan(textSize,true);
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
     * 判断应用是否在运行
     * @param context
     * @return
     */
    public static boolean isRun(Context context){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
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
}
