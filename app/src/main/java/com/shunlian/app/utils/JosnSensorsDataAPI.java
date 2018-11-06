package com.shunlian.app.utils;

//import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class JosnSensorsDataAPI {
    public static boolean isRecommend = false;
    public static boolean isHistory = false;
    /**
     * 点击banner事件统计
     * @return
     */
    public static void bannerClick(String pageType,String bannerLocation,String bannerName,
                                         String bannerID,String url,String bannerRank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("pageType", pageType);
            properties.put("bannerLocation", bannerLocation);
            properties.put("bannerName", bannerName);
            properties.put("bannerID", bannerID);
            properties.put("url", url);
            properties.put("bannerRank", bannerRank);
//            SensorsDataAPI.sharedInstance().track("bannerClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     * @return
     */
    public static void login(String loginMethod){
        try {
            JSONObject properties = new JSONObject();
            properties.put("loginMethod", loginMethod);
//            SensorsDataAPI.sharedInstance().track("login", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册
     * @return
     */
    public static void signUp(String signUpMethod,String code){
        try {
            JSONObject properties = new JSONObject();
            properties.put("signUpMethod", signUpMethod);
            properties.put("code", code);
//            SensorsDataAPI.sharedInstance().track("signUp", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索
     * @return
     */
    public static void search(String keyWord,boolean hasResult,boolean isHistory,boolean isRecommend){
        try {
            JSONObject properties = new JSONObject();
            properties.put("keyWord", keyWord);
            properties.put("hasResult", hasResult);
            properties.put("isHistory", isHistory);
            properties.put("isRecommend", isRecommend);
//            SensorsDataAPI.sharedInstance().track("signUp", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
