package com.shunlian.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.login.LoginAct;

/**
 * Created by Administrator on 2017/8/3.
 * 主机名切换工具
 */

public class SwitchHostUtil {


    public static void switchMethod(final Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.mipmap.icon_login_logo);
        builder.setTitle("选择一个主机");
        //    指定下拉列表的显示数据
        final String[] cities = {"测试接口", "预发布接口"};
//        final String[] cities = { "测试接口", "预发布接口","正式接口"};
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity, "选择的主机为：" + cities[which], Toast.LENGTH_SHORT).show();
                if (which == 0) {
                    //测试
                    InterentTools.HTTPADDR = "http://v20-front-api.shunliandongli.com/";
                    InterentTools.HTTPADDR_IM = "ws://123.207.107.21:8086";
                    InterentTools.H5_HOST = "http://v20-wx.shunliandongli.com/";
                    InterentTools.DOMAIN = "v20-wx.shunliandongli.com";
                    //                    Constant.IM_SDK_APPID = 1400018006;
//                    Constant.IM_ACCOUNT_TYPE = 3415;
//                    Constant.IM_XIAOMI_BUSID = 405;
//                    Constant.IM_HUAWEI_BUSID = 406;
                } else if (which == 1) {
                    //预发布
                    InterentTools.HTTPADDR = "http://api-front.v2.shunliandongli.com/";
                    InterentTools.H5_HOST ="http://front.v2.shunliandongli.com/";
                    InterentTools.DOMAIN ="front.v2.shunliandongli.com";
                    //                    Constant.IM_SDK_APPID = 1400018006;
                    InterentTools.HTTPADDR_IM = "ws://ws.v2.shunliandongli.com";
//                    Constant.IM_SDK_APPID = 1400018006;
//                    Constant.IM_ACCOUNT_TYPE = 3415;
//                    Constant.IM_XIAOMI_BUSID = 405;
//                    Constant.IM_HUAWEI_BUSID = 406;
                } else {
                    //正式

//                    MyHttpUtil.HTTPADDR = "https://api.shunliandongli.com/v1/";
//                    MyHttpUtil.HTTPADDR_IM = "https://im.api.shunliandongli.com/";
//                    Constant.IM_SDK_APPID = 1400008795;
//                    Constant.IM_ACCOUNT_TYPE = 3425;
//                    Constant.IM_XIAOMI_BUSID = 431;
//                    Constant.IM_HUAWEI_BUSID = 432;
//                    MyHttpUtil.H5_HOST = Constant.RELEASE_HOST;
                }

//                App myApp = (App) activity.getApplication();
//                myApp.edNetIsTest.putString("netIsTest", MyHttpUtil.HTTPADDR);
//                myApp.edNetIsTest.apply();
                SharedPrefUtil.saveCacheSharedPrf("netState", InterentTools.HTTPADDR);

                Common.clearLoginInfo();
                JpushUtil.setJPushAlias();
                Constant.JPUSH = null;
                EasyWebsocketClient.getInstance(activity).logout();
//                Common.goGoGo(activity, "");
                LoginAct.startAct(activity);
                activity.finish();
            }
        });
        builder.show();
    }

    public static void setHostMethod() {
        String netState = SharedPrefUtil.getCacheSharedPrf("netState", "");
        if (!TextUtils.isEmpty(netState)) {
            InterentTools.HTTPADDR = netState;
            if (netState.contains("v20-front-api")) {
                InterentTools.HTTPADDR_IM = "ws://123.207.107.21:8086";
                InterentTools.H5_HOST = "http://v20-wx.shunliandongli.com/";
                InterentTools.DOMAIN = "v20-wx.shunliandongli.com";
//                Constant.IM_SDK_APPID = 1400018006;
//                Constant.IM_ACCOUNT_TYPE = 3415;
//                Constant.IM_XIAOMI_BUSID = 405;
//                Constant.IM_HUAWEI_BUSID = 406;
            } else {
                InterentTools.H5_HOST ="http://front.v2.shunliandongli.com/";
                InterentTools.DOMAIN ="front.v2.shunliandongli.com";
                InterentTools.HTTPADDR_IM = "ws://ws.v2.shunliandongli.com";
//                Constant.IM_SDK_APPID = 1400008795;
//                Constant.IM_ACCOUNT_TYPE = 3425;
//                Constant.IM_XIAOMI_BUSID = 431;
//                Constant.IM_HUAWEI_BUSID = 432;
            }
        }
    }
}
