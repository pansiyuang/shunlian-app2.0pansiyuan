package com.shunlian.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private SharedPreferences spUserInfo;

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        	processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            String extVal = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.i("--extVal---", "--extVal---" + extVal);
            String toPage = "";
            String id = "";
            String id1 = "";
            String id2 = "";
            String id3 = "";
            String id4 = "";
            String id5 = "";
            String id6 = "";

            //适用于IM推送相关
            String to_shop_id = "";
            String from_shop_id = "";
            String from_nickname = "";
            String from_type = "";
            String to_type = "";
            String from_user_id = "";
            String to_user_id = "";

            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(extVal);
                toPage = jsonObj.optString("to_page");
                id = jsonObj.optString("id");
                id1 = jsonObj.optString("id1");
                id2 = jsonObj.optString("id2");
                id3 = jsonObj.optString("id3");
                id4 = jsonObj.optString("id4");
                id5 = jsonObj.optString("id5");
                id6 = jsonObj.optString("id6");

                to_shop_id = jsonObj.optString("to_shop_id");
                from_shop_id = jsonObj.optString("from_shop_id");
                from_nickname = jsonObj.optString("from_nickname");
                from_type = jsonObj.optString("from_type");
                to_type = jsonObj.optString("to_type");
                from_user_id = jsonObj.optString("from_user_id");
                to_user_id = jsonObj.optString("to_user_id");

                Log.i("--toPage---", "--toPage---" + toPage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Constant.JPUSH = new ArrayList<>();
                Constant.JPUSH.add(toPage);
                Constant.JPUSH.add(id);
                Constant.JPUSH.add(id1);
                Constant.JPUSH.add(id2);
                Constant.JPUSH.add(id3);
                Constant.JPUSH.add(id4);
                Constant.JPUSH.add(id5);
                Constant.JPUSH.add(id6);

                //适用于IM
                Constant.JPUSH.add(to_shop_id);
                Constant.JPUSH.add(from_shop_id);
                Constant.JPUSH.add(from_nickname);
                Constant.JPUSH.add(from_type);
                Constant.JPUSH.add(to_type);
                Constant.JPUSH.add(from_user_id);
                Constant.JPUSH.add(to_user_id);

                Common.goGoGo(context, toPage, id, id1,id2, id3,id4, id5,id6,to_shop_id, from_shop_id, from_nickname, from_type, to_type, from_user_id, to_user_id);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "推送数据异常" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

//    private void toLoginPage(Context context, String target, String param1_id, String param2_id) {
//        Intent mIntent = new Intent(context, LoginAct.class);
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        mIntent.putExtra("target", target);
//        mIntent.putExtra("param1_id", param1_id);
//        mIntent.putExtra("param2_id", param2_id);
//        context.startActivity(mIntent);
//    }

}
