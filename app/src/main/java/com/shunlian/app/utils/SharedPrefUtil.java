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
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.shunlian.app.bean.DiscoverActivityEntity;
import com.shunlian.app.ui.integral_team.TeamIntegralActivity;

import java.util.Set;

/**
 * Created by zhang on 2017/4/14 11 : 43.
 */

public class SharedPrefUtil {

    public static final String config = "shunlian_config";
    public static final String shunlian_cache = "shunlian_cache";//缓存
    public static final String COLLECTION_GOODS_HISTORY = "collection_goods_";//收藏搜索商品历史
    public static final String COLLECTION_STORE_HISTORY = "collection_shop_";//收藏搜索店铺历史

    public static SharedPreferences getsharedPreferences(){
        return Common.getApplicationContext().getSharedPreferences(config, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getsharedPreferences(String file_name){
        return Common.getApplicationContext().getSharedPreferences(file_name, Context.MODE_PRIVATE);
    }

    /**
     * 从sp中获取boolean
     * @param key
     * @param def
     * @return
     */
    public static boolean getCacheSharedPrfBoolean(String key, boolean def){
        return getsharedPreferences(shunlian_cache).getBoolean(key,def);
    }

    /**
     * 将boolean保存到sp中
     * @param key
     * @param value
     * @return
     */
    public static boolean saveCacheSharedPrfBoolean(String key, boolean value){
        return getsharedPreferences(shunlian_cache).edit().putBoolean(key,value).commit();
    }

    /**
     * 从sp中获取long
     * @param key
     * @param def
     * @return
     */
    public static long getCacheSharedPrfLong(String key, long def){
        return getsharedPreferences(shunlian_cache).getLong(key,def);
    }

    /**
     * 将long保存到sp中
     * @param key
     * @param value
     * @return
     */
    public static boolean saveCacheSharedPrfLong(String key, long value){
        return getsharedPreferences(shunlian_cache).edit().putLong(key,value).commit();
    }

    /**
     * 从sp中获取string
     * @param key
     * @param def
     * @return
     */
    public static String getSharedUserString(String key, String def){
       return getsharedPreferences().getString(key,def);
    }

    /**
     * 将string保存到sp中
     * @param key
     * @param value
     * @return
     */
    public static boolean saveSharedUserString(String key, String value){
         return getsharedPreferences().edit().putString(key,value).commit();
    }

    /**
     * 将boolean保存到sp中
     * @param key
     * @param value
     * @return
     */
    public static boolean saveSharedUserBoolean(String key, boolean value){
        return getsharedPreferences().edit().putBoolean(key,value).commit();
    }

    /**
     * 从sp中获取long
     * @param key
     * @param def
     * @return
     */
    public static boolean getSharedUserBoolean(String key, boolean def){
        return getsharedPreferences().getBoolean(key,def);
    }

    /**
     * 将Set<String>保存到sp中
     * @param key
     * @param strings
     * @return
     */
    public static boolean saveSharedUserStringss(String key, Set<String> strings){
         return getsharedPreferences().edit().putStringSet(key,strings).commit();
    }


    /**
     * 从sp中获取Set<String>
     * @param key
     * @param strings
     * @return
     */
    public static Set<String> getSharedUserStringss(String key, Set<String> strings){
        return getsharedPreferences().getStringSet(key,strings);
    }
    /**
     * 清空sp文件内容
     */
    public static void clearSharedPreferences(){
        getsharedPreferences().edit().clear().commit();
    }


    /**
     * 从sp中获取string
     * @param key
     * @param def
     * @return
     */
    public static String getCacheSharedPrf(String key,String def){
        return getsharedPreferences(shunlian_cache).getString(key,def);
}

    /**
     * 将string保存到sp中
     * @param key
     * @param value
     * @return
     */
    public static boolean saveCacheSharedPrf(String key,String value){
        return getsharedPreferences(shunlian_cache).edit().putString(key,value).commit();
    }

    /**
     * 从sp中获取string
     * @param key
     * @param def
     * @return
     */
    public static Integer getCacheSharedPrfInt(String key,int def){
        return getsharedPreferences(shunlian_cache).getInt(key,def);
}

    /**
     * 将string保存到sp中
     * @param key
     * @param value
     * @return
     */
    public static boolean saveCacheSharedPrfInt(String key,int value){
        return getsharedPreferences(shunlian_cache).edit().putInt(key,value).commit();
    }

    /**
     * 清空sp文件内容
     */
    public static void clearCacheSharedPref(){
        getsharedPreferences(shunlian_cache).edit().clear().commit();
    }

    /**
     * 跳转到微信
     */
    public static void getWechatApi(Activity context){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            Common.staticToastAct(context,"检查到您手机没有安装微信，请安装后使用该功能");
        }
    }
}
