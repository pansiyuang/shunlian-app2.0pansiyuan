package com.shunlian.app.utils;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

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
            SensorsDataAPI.sharedInstance().track("bannerClick", properties);
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
            SensorsDataAPI.sharedInstance().track("login", properties);
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
            SensorsDataAPI.sharedInstance().track("signUp", properties);
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
            SensorsDataAPI.sharedInstance().track("signUp", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品信息
     * @return
     */
    public static void commodityDetail(String preseat,String commodityID,String commodityName,String firstCommodity,
                                       String secondCommodity,String pricePerCommodity,String storeID,String storeName){
        try {
            JSONObject properties = new JSONObject();
            properties.put("preseat", preseat);
            properties.put("commodityID", commodityID);
            properties.put("commodityName", commodityName);
            properties.put("firstCommodity", firstCommodity);
            properties.put("secondCommodity", secondCommodity);
            properties.put("pricePerCommodity", pricePerCommodity);
            properties.put("storeID", storeID);
            properties.put("storeName", storeName);
            SensorsDataAPI.sharedInstance().track("commodityDetail", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入购物车
     * @return
     */
    public static void addToShoppingcart(String preseat,String commodityID,String commodityName,String firstCommodity,
                                       String secondCommodity,String commodityNumber,String addToShoppingcart){
        try {
            JSONObject properties = new JSONObject();
            properties.put("preseat", preseat);
            properties.put("commodityID", commodityID);
            properties.put("commodityName", commodityName);
            properties.put("firstCommodity", firstCommodity);
            properties.put("secondCommodity", secondCommodity);
            properties.put("commodityNumber", commodityNumber);
            properties.put("addToShoppingcart", addToShoppingcart);
            SensorsDataAPI.sharedInstance().track("addToShoppingcart", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交订单
     * @return
     */
    public static void submitOrder(String orderID,String orderAmount,String addressId,String transportationCosts,String discountId
                        ,String IntegralDiscountAmount){
        try {
            JSONObject properties = new JSONObject();
            properties.put("orderID", orderID);
            properties.put("orderAmount", orderAmount);
            properties.put("addressId", addressId);
//            properties.put("receiverProvince", receiverProvince);
//            properties.put("receiverCity", receiverCity);
//            properties.put("receiverArea", receiverArea);
            properties.put("transportationCosts", transportationCosts);
            properties.put("discountId", discountId);
//            properties.put("discountAmount", discountAmount);
//            properties.put("ifUseLntegral", ifUseLntegral);
//            properties.put("numberOfLntegral", numberOfLntegral);
            properties.put("IntegralDiscountAmount", IntegralDiscountAmount);
            SensorsDataAPI.sharedInstance().track("submitOrder", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消订单
     * @return
     */
    public static void cancelPayOrder(String orderID,String orderAmount,String actualPaymentAmount,String paymentMethod,String ifUseDiscount
            ,String IntegralDiscountAmount){
        try {
            JSONObject properties = new JSONObject();
            properties.put("orderID", orderID);
            properties.put("orderAmount", orderAmount);
            properties.put("actualPaymentAmount", actualPaymentAmount);
//            properties.put("receiverProvince", receiverProvince);
//            properties.put("receiverCity", receiverCity);
//            properties.put("receiverArea", receiverArea);
            properties.put("paymentMethod", paymentMethod);
            properties.put("ifUseDiscount", ifUseDiscount);
//            properties.put("discountAmount", discountAmount);
//            properties.put("ifUseLntegral", ifUseLntegral);
//            properties.put("numberOfLntegral", numberOfLntegral);
            properties.put("IntegralDiscountAmount", IntegralDiscountAmount);
            SensorsDataAPI.sharedInstance().track("cancelPayOrder", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
