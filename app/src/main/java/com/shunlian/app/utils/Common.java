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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableString;
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

import java.util.regex.Pattern;

/**
 * Created by zhang on 2017/4/14 11 : 42.
 */

public class Common {

    private static Toast toast;
    private static MyTextView mtv_toast;

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
}
