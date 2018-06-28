package com.shunlian.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.shunlian.app.R;
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
        final String[] cities = {"测试接口", "预发布接口","正式"};
        //    设置一个下拉的列表选择项
        builder.setItems(cities, (dialog, which) -> {
            Toast.makeText(activity, "选择的主机为：" + cities[which], Toast.LENGTH_SHORT).show();
            if (which == 0) {
                //测试
                InterentTools.HTTPADDR = "http://v20-front-api.shunliandongli.com/";
                InterentTools.HTTPADDR_IM = "ws://123.207.107.21:8086";
                InterentTools.H5_HOST = "http://v20-wx.shunliandongli.com/";
                InterentTools.DOMAIN = "v20-wx.shunliandongli.com";
            } else if (which == 1) {
                //预发布
                InterentTools.HTTPADDR = "http://api-front.v2.shunliandongli.com/";
                InterentTools.H5_HOST ="http://front.v2.shunliandongli.com/";
                InterentTools.DOMAIN ="front.v2.shunliandongli.com";
                InterentTools.HTTPADDR_IM = "ws://ws.v2.shunliandongli.com";
            } else {
                //正式
                InterentTools.HTTPADDR = "https://api.shunliandongli.com/v2/front/";
                InterentTools.H5_HOST ="http://wx-tmp.shunliandongli.com/";
                InterentTools.DOMAIN ="wx-tmp.shunliandongli.com";
                InterentTools.HTTPADDR_IM = "wss://api.shunliandongli.com/v2/im/";
            }

            SharedPrefUtil.saveCacheSharedPrf("netState", InterentTools.HTTPADDR);
            Common.clearLoginInfo();
            JpushUtil.setJPushAlias();
            Constant.JPUSH = null;
            LoginAct.startAct(activity);
            activity.finish();
        });
        builder.show();
    }

    public static void setHostMethod() {
        String netState = SharedPrefUtil.getCacheSharedPrf("netState", "");
        if (!TextUtils.isEmpty(netState)) {
            InterentTools.HTTPADDR = netState;
            if (netState.contains("v20-front-api")) {//测试

                InterentTools.HTTPADDR_IM = "ws://123.207.107.21:8086";
                InterentTools.H5_HOST = "http://v20-wx.shunliandongli.com/";
                InterentTools.DOMAIN = "v20-wx.shunliandongli.com";
            } else if (netState.contains("api-front.v2")) {//预发布

                InterentTools.H5_HOST ="http://front.v2.shunliandongli.com/";
                InterentTools.DOMAIN ="front.v2.shunliandongli.com";
                InterentTools.HTTPADDR_IM = "ws://ws.v2.shunliandongli.com";
            }else{//正式

                InterentTools.H5_HOST ="http://wx-tmp.shunliandongli.com/";
                InterentTools.DOMAIN ="wx-tmp.shunliandongli.com";
                InterentTools.HTTPADDR_IM = "wss://api.shunliandongli.com/v2/im/";
            }
        }else {
            //此句话保证开发环境没有切换环境的情况下也能选择帐号
            SharedPrefUtil.saveCacheSharedPrf("netState", InterentTools.HTTPADDR);
        }
    }
}
