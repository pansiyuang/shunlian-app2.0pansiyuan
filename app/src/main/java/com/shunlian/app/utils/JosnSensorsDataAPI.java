package com.shunlian.app.utils;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class JosnSensorsDataAPI {
    public static int currentPageMain = 1;
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
            properties.put("bannerType", pageType);
//            properties.put("bannerName", bannerName);
            properties.put("bannerID", bannerID);
            String mUrl="";
            if (url!=null)
                mUrl=url;
            if(bannerName!=null&&bannerName.equals("goods")&&!mUrl.contains("商品")){
                properties.put("bannerURL", "商品："+mUrl);
            }else if(bannerName!=null&&bannerName.equals("shop")&&!mUrl.contains("店铺")){
                properties.put("bannerURL", "店铺："+mUrl);
            }else {
                properties.put("bannerURL", mUrl);
            }
            properties.put("bannerRank", rank);
            SensorsDataAPI.sharedInstance().track("bannerClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 点击icon事件统计
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
     * 品质热推和精选活动点击
     * @return
     */
    public static void fristQualityHotClick(String mouleType,String goType,String itemId,String channe_id ,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("pzrtType", mouleType);
//            properties.put("bannerLocation", bannerLocation);
            if(goType.equals("goods")) {
                properties.put("pzrtURL",  "商品:" + itemId);
            }else if(goType.equals("shop")){
                properties.put("pzrtURL",  "店铺:" + itemId);
            }else if(goType.equals("hotpush")){
                properties.put("pzrtURL",  "专场:" + channe_id);
            }
            else{
                properties.put("pzrtURL",  "url:" + itemId);
            }
            properties.put("pzrtName", channe_id);
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
    public static void pinpaiStoreClick(String promotion_type,String storeId,String storeName,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("pptmStoreId", storeId);
            properties.put("pptmStoreType", promotion_type);
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
    public static void pinpaiStoreGoodClick(String storeId,String storeName,String goodId,String goodName,int rank,String promotion_type){
        try {
            JSONObject properties = new JSONObject();
            properties.put("pptmStoreId", storeId);
            properties.put("pptmStoreName", storeName);
            properties.put("pptmStoreType", promotion_type);
            properties.put("pptmStoreGoodId", goodId);
            properties.put("pptmStoreGoodName", goodName);
            properties.put("pptmStoreGoodRank", rank);
            SensorsDataAPI.sharedInstance().track("pptmStoreGoodsClick", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 爱上新品商品点
     * @return
     */
    public static void loveNewGoodClick(String channelName,String goodId,String goodName,int rank){
        try {
            JSONObject properties = new JSONObject();
            properties.put("asxpChannelName", channelName);
            properties.put("asxpGoodId", goodId);
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
     * 商品点详情页统计
     * @return
     */
    public static void shareGoodClick(String goods_id,String goods_title,
                                      String cate1,String cate2,String price,String store_id,
                                      String store_name,String goodName){
        try {
            JSONObject properties = new JSONObject();
            properties.put("goods_id", goods_id);
            properties.put("goods_title", goods_title);
            properties.put("cate1", cate1!=null?cate1:"");
            properties.put("cate2", cate2!=null?cate2:"");
            if(price.contains("~")){
                String[] priceBtweet = price.split("~");
                if(priceBtweet.length==2){
                    if(isDoubleOrFloat(priceBtweet[0])){
                        properties.put("min_price", Float.valueOf(priceBtweet[0]));
                    }
                    if(isDoubleOrFloat(priceBtweet[1])){
                        properties.put("max_price",Float.valueOf( priceBtweet[1]));
                    }
                }
            }else{
                if(isDoubleOrFloat(price)) {
                    properties.put("min_price", Float.valueOf(price));
                    properties.put("max_price", Float.valueOf(price));
                }
            }
            properties.put("store_id", store_id!=null?store_id:"");
            properties.put("store_name", store_name!=null?store_name:"");
            properties.put("shareMethod", goodName);
            SensorsDataAPI.sharedInstance().track("share", properties);
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
//            JSONObject properties = new JSONObject();
//            properties.put("type", type);
//            properties.put("goodId", goodId);
//            properties.put("goodName", goodName);
//            properties.put("rank", rank);
//            SensorsDataAPI.sharedInstance().track("youpinGoodClick", properties);
        } catch (Exception e) {
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
    public static void search(String keyWord,boolean hasResult ,boolean isHistory,boolean isRecommend){
        try {
            JSONObject properties = new JSONObject();
            properties.put("keyWord", keyWord);
            properties.put("hasResult", hasResult);
            properties.put("isHistory", isHistory);
            properties.put("isRecommend", isRecommend);
            SensorsDataAPI.sharedInstance().track("search", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品信息
     * @return
     */
    public static void commodityDetail(String from,String commodityID,String commodityName,String firstCommodity,
                                       String secondCommodity,String pricePerCommodity,String storeID,String storeName){
        try {
            JSONObject properties = new JSONObject();
            if(from.equals("首页")&&currentPageMain==1){
                properties.put("from", from);
            }else if(from.equals("首页")&&currentPageMain==2){
                properties.put("from", "首页分类");
            }else if(from.equals("首页")&&currentPageMain==3){
                properties.put("from", "首页发现");
            }else if(from.equals("首页")&&currentPageMain==4){
                properties.put("from", "首页购物车");
            }else if(from.equals("首页")&&currentPageMain==5){
                properties.put("from", "首页我的");
            }else {
                properties.put("from", from);
            }
            properties.put("goods_id", commodityID);
            properties.put("goods_title", commodityName);
            properties.put("cate1", firstCommodity);
            properties.put("cate2", secondCommodity);
            if(pricePerCommodity.contains("~")){
               String[] priceBtweet = pricePerCommodity.split("~");
               if(priceBtweet.length==2){
                   if(isDoubleOrFloat(priceBtweet[0])){
                       properties.put("min_price", Float.valueOf(priceBtweet[0]));
                   }
                   if(isDoubleOrFloat(priceBtweet[1])){
                       properties.put("max_price",Float.valueOf( priceBtweet[1]));
                   }
               }
            }else{
                if(isDoubleOrFloat(pricePerCommodity)) {
                    properties.put("min_price", Float.valueOf(pricePerCommodity));
                    properties.put("max_price", Float.valueOf(pricePerCommodity));
                }
            }
            properties.put("store_id", storeID);
            properties.put("store_name", storeName);
            SensorsDataAPI.sharedInstance().track("goodsDetail", properties);
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

    /*
     * 是否为浮点数？double或float类型。
     * @param str 传入的字符串。
     * @return 是浮点数返回true,否则返回false。
     */
    public static boolean isDoubleOrFloat(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

}
