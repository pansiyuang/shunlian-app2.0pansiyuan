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
            properties.put("bannerBelongs", pageType);
//            properties.put("bannerName", bannerName);
            properties.put("bannerID", bannerID);
            if(bannerName!=null&&bannerName.equals("goods")&&!url.contains("商品")){
                properties.put("bannerURL", "商品："+url);
            }else if(bannerName!=null&&bannerName.equals("shop")&&!url.contains("店铺")){
                properties.put("bannerURL", "店铺："+url);
            }else {
                properties.put("bannerURL", url);
            }
            properties.put("bannerRank", rank);
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
            properties.put("iconURL", goTiltle);
            properties.put("iconRank", rank);
            SensorsDataAPI.sharedInstance().track("iconClick", properties);
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
            properties.put("pzrtType", mouleType);
//            properties.put("bannerLocation", bannerLocation);
            properties.put("pzrtURL", itemId+":"+channe_id);
            properties.put("pzrtLocationName", rank);
            properties.put("pzrtRank", rank);
            SensorsDataAPI.sharedInstance().track("pzrtClick", properties);
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
            properties.put("ttthNowTime", nowTime);
//            properties.put("bannerLocation", bannerLocation);
            properties.put("ttthGoodId", gooId);
            properties.put("ttthGoodName", goodName);
            properties.put("ttthGoodRank", rank);
            SensorsDataAPI.sharedInstance().track("ttthGoodsClick", properties);
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
            properties.put("pptmStoreId", storeId);
//            properties.put("bannerLocation", bannerLocation);
            properties.put("pptmStoreName", storeName);
            properties.put("pptmStoreRank", rank);
            SensorsDataAPI.sharedInstance().track("pptmStoreClick", properties);
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
            properties.put("pptmStoreId", storeId);
            properties.put("pptmStoreName", storeName);
            properties.put("pptmStoreGoodId", goodId);
            properties.put("pptmStoreGoodName", goodName);
            properties.put("pptmStoreGoodRank", rank);
            SensorsDataAPI.sharedInstance().track("pptmStoreGoodsClick", properties);
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
            properties.put("asxpChannelName", channelName);
            properties.put("asxpDoodId", goodId);
            properties.put("asxpGoodName", goodName);
            properties.put("asxpGoodRank", rank);
            SensorsDataAPI.sharedInstance().track("asxpGoodsClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 口碑热销商品点击
     * @return
     */
    public static void koubeiGoodClick(String channelName,String goodId,String goodName,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("kbrxFirstChannelName", channelName);
            properties.put("kbrxGoodId", goodId);
            properties.put("kbrxGoodName", goodName);
            properties.put("kbrxGoodRank", rank);
            SensorsDataAPI.sharedInstance().track("kbrxGoodsClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 优品商品点击
     * @return
     */
    public static void youpinGoodClick(String type,String goodId,String goodName,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("type", type);
            properties.put("goodId", goodId);
            properties.put("goodName", goodName);
            properties.put("rank", rank);
            SensorsDataAPI.sharedInstance().track("youpinGoodClick", properties);
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
            properties.put("pdBelongs", homeChannel);
            properties.put("pdType", indexChannel);
            properties.put("pdGoodId", goodId);
            properties.put("pdGoodName", goodName);
            properties.put("pdGoodRank", rank);
            SensorsDataAPI.sharedInstance().track("pdGoodsClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 登录
     * @return
     */
    public static void login(String loginMethod){
//        try {
//            JSONObject properties = new JSONObject();
//            properties.put("loginMethod", loginMethod);
//            SensorsDataAPI.sharedInstance().track("login", properties);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 注册
     * @return
     */
    public static void signUp(String signUpMethod,String code){
//        try {
//            JSONObject properties = new JSONObject();
//            properties.put("signUpMethod", signUpMethod);
//            properties.put("code", code);
//            SensorsDataAPI.sharedInstance().track("signUp", properties);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
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
            properties.put("from", preseat);
            properties.put("goods_id", commodityID);
            properties.put("goods_title", commodityName);
            properties.put("cate1", firstCommodity);
            properties.put("cate2", secondCommodity);
            properties.put("price", pricePerCommodity);
            properties.put("store_id", storeID);
            properties.put("store_name", storeName);
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
//        try {
//            JSONObject properties = new JSONObject();
//            properties.put("preseat", preseat);
//            properties.put("commodityID", commodityID);
//            properties.put("commodityName", commodityName);
//            properties.put("firstCommodity", firstCommodity);
//            properties.put("secondCommodity", secondCommodity);
//            properties.put("commodityNumber", commodityNumber);
//            properties.put("addToShoppingcart", addToShoppingcart);
//            SensorsDataAPI.sharedInstance().track("addToShoppingcart", properties);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 提交订单
     * @return
     */
    public static void submitOrder(String orderID,String orderAmount,String addressId,String transportationCosts,String discountId
                        ,String IntegralDiscountAmount){
//        try {
//            JSONObject properties = new JSONObject();
//            properties.put("orderID", orderID);
//            properties.put("orderAmount", orderAmount);
//            properties.put("addressId", addressId);
////            properties.put("receiverProvince", receiverProvince);
////            properties.put("receiverCity", receiverCity);
////            properties.put("receiverArea", receiverArea);
//            properties.put("transportationCosts", transportationCosts);
//            properties.put("discountId", discountId);
////            properties.put("discountAmount", discountAmount);
////            properties.put("ifUseLntegral", ifUseLntegral);
////            properties.put("numberOfLntegral", numberOfLntegral);
//            properties.put("IntegralDiscountAmount", IntegralDiscountAmount);
//            SensorsDataAPI.sharedInstance().track("submitOrder", properties);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 取消订单
     * @return
     */
    public static void cancelPayOrder(String orderID,String orderAmount,String actualPaymentAmount,String paymentMethod,String ifUseDiscount
            ,String IntegralDiscountAmount){
//        try {
//            JSONObject properties = new JSONObject();
//            properties.put("orderID", orderID);
//            properties.put("orderAmount", orderAmount);
//            properties.put("actualPaymentAmount", actualPaymentAmount);
////            properties.put("receiverProvince", receiverProvince);
////            properties.put("receiverCity", receiverCity);
////            properties.put("receiverArea", receiverArea);
//            properties.put("paymentMethod", paymentMethod);
//            properties.put("ifUseDiscount", ifUseDiscount);
////            properties.put("discountAmount", discountAmount);
////            properties.put("ifUseLntegral", ifUseLntegral);
////            properties.put("numberOfLntegral", numberOfLntegral);
//            properties.put("IntegralDiscountAmount", IntegralDiscountAmount);
//            SensorsDataAPI.sharedInstance().track("cancelPayOrder", properties);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
