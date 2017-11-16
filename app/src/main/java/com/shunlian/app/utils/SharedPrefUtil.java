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
import android.content.SharedPreferences;

/**
 * Created by zhang on 2017/4/14 11 : 43.
 */

public class SharedPrefUtil {

    public static final String config = "shunlian_config";

    public static SharedPreferences getsharedPreferences(){
        return Common.getApplicationContext().getSharedPreferences(config, Context.MODE_PRIVATE);
    }

    /**
     * 从sp中获取string
     * @param key
     * @param def
     * @return
     */
    public static String getSharedPrfString(String key,String def){
       return getsharedPreferences().getString(key,def);
    }

    /**
     * 将string保存到sp中
     * @param key
     * @param value
     * @return
     */
    public static boolean saveSharedPrfString(String key,String value){
         return getsharedPreferences().edit().putString(key,value).commit();
    }

    /**
     * 清空sp文件内容
     */
    public static void clearSharedPreferences(){
        getsharedPreferences().edit().clear().commit();
    }
}
