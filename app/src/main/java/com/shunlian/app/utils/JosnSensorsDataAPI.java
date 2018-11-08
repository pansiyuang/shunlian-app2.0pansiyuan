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
    public static void bannerClick(String pageType,String bannerName,
                                         String bannerID,String url,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("pageType", pageType);
//            properties.put("bannerLocation", bannerLocation);
            properties.put("bannerName", bannerName);
            properties.put("bannerID", bannerID);
            properties.put("url", url);
            properties.put("rank", rank);
            SensorsDataAPI.sharedInstance().track("bannerClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 点击banner事件统计
     * @return
     */
    public static void fristIconClick(String iconName,String goTiltle,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("iconName", iconName);
//            properties.put("bannerLocation", bannerLocation);
            properties.put("goTiltle", goTiltle);
            properties.put("rank", rank);
            SensorsDataAPI.sharedInstance().track("fristIconClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 品质热推点击
     * @return
     */
    public static void fristQualityHotClick(String mouleType,String goType,String itemId,String channe_id ,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("mouleType", mouleType);
//            properties.put("bannerLocation", bannerLocation);
            properties.put("goTiltle", goType);
            properties.put("itemId", itemId);
            properties.put("channe_id", channe_id);
            properties.put("rank", rank);
            SensorsDataAPI.sharedInstance().track("fristQualityHotClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 天天特惠商品点击
     * @return
     */
    public static void daydayGoodClick(String nowTime,String gooId,String goodName,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("nowTime", nowTime);
//            properties.put("bannerLocation", bannerLocation);
            properties.put("gooId", gooId);
            properties.put("goodName", goodName);
            properties.put("rank", rank);
            SensorsDataAPI.sharedInstance().track("fristQualityHotClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 品牌特卖店铺点击
     * @return
     */
    public static void pinpaiStoreClick(String storeId,String storeName,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("storeId", storeId);
//            properties.put("bannerLocation", bannerLocation);
            properties.put("storeName", storeName);
            properties.put("rank", rank);
            SensorsDataAPI.sharedInstance().track("pinpaiStoreClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 品牌特卖店铺商品点击
     * @return
     */
    public static void pinpaiStoreGoodClick(String storeId,String storeName,String goodId,String goodName,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("storeId", storeId);
            properties.put("storeName", storeName);
            properties.put("goodId", goodId);
            properties.put("goodName", goodName);
            properties.put("rank", rank);
            SensorsDataAPI.sharedInstance().track("pinpaiStoreGoodClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 爱上新品商品点击
     * @return
     */
    public static void loveNewGoodClick(String channelName,String goodId,String goodName,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("channelName", channelName);
            properties.put("goodId", goodId);
            properties.put("goodName", goodName);
            properties.put("rank", rank);
            SensorsDataAPI.sharedInstance().track("loveNewGoodClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 频道页商品点击
     * @return
     */
    public static void channelGoodClick(String homeChannel,String indexChannel,String goodId,String goodName,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("homeChannel", homeChannel);
            properties.put("indexChannel", indexChannel);
            properties.put("goodId", goodId);
            properties.put("goodName", goodName);
            properties.put("rank", rank);
            SensorsDataAPI.sharedInstance().track("channelGoodClick", properties);
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
