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


import android.os.Environment;

import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.ui.register.SelectRecommendAct;

import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2017/4/14 11 : 38.
 * 常量
 */

public final class Constant {
    /**
     * 效果图宽
     */
    public static final float DRAWING_WIDTH = 720.0f;
    /**
     * 效果图高
     */
    public static final float DRAWING_HEIGHT = 1280.0f;

    public static final String KEY = "sdf21111111111";
//    public static final String WX_APP_ID = "wxbffd8c8c412e4c73";// 微信开放平台申请到的app_id
//    public static final String WX_APP_ID = "wx32f8315ee438f6be";// 易云商城app_id
    public static final String WX_APP_ID = "wx823a21585581c53b";// 易云商城（测试专用）app_id
    //    public static final String WX_APP_ID = "wx4453de455c6da56a";// 微信Demo开放平台申请到的app_id
    //    public static final String WX_APP_ID ="wxaea5641b2b1fdb77";// 微信公众号的app_id
    public static final String QQ_APP_ID = "1105343909";// QQ开放平台申请到的app_id
    public static final String CACHE_PATH_EXTERNAL = Environment.getExternalStorageDirectory().getAbsolutePath() +
            "/Android/data/com.shunlian.app/ShunLian/Cache/";
    public static final String DOWNLOAD_PATH_EXTERNAL = Environment.getExternalStorageDirectory().getAbsolutePath() +
            "/Android/data/com.shunlian.app/ShunLian/DownLoad/";
    public static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    public static List<String> BRAND_IDS;//筛选品牌id
    public static List<String> BRAND_IDSBEFORE;//筛选品牌id,记录用
    public static Map<String, List<String>> BRAND_ATTRS;//筛选属性
    public static List<String> BRAND_ATTRNAME;//筛选属性名

    public static GoodsSearchParam SEARCHPARAM;//搜索参数
    public static List<String> REBRAND_IDS;//筛选品牌id(重新赋值用)
    public static Map<String, List<String>> REBRAND_ATTRS;//筛选属性(重新赋值用)
    public static GetListFilterEntity LISTFILTER;//列表属性(重新赋值用)
    public static String DINGWEI;//定位


    //测试
//    public static final int IM_SDK_APPID = 1400018006; //腾讯IM SDK appId
//    public static final int IM_ACCOUNT_TYPE = 3415;   //腾讯IM SDK  accountType
    //正式
    public static int IM_SDK_APPID = 1400008795; //腾讯IM SDK appId
    public static int IM_ACCOUNT_TYPE = 3425;   //腾讯IM SDK  accountType

    public static final String IM_XIAOMI_APPID = "2882303761517495137"; //小米推送APPID
    public static final String IM_XIAOMI_APPKEY = "5161749566137";      //小米推送KEY

    //测试
//    public static final int IM_XIAOMI_BUSID = 405;      //IM 绑定小米推送证书ID
//    public static final int IM_HUAWEI_BUSID = 406;      //IM 绑定华为推送证书ID

    //正式
    public static int IM_XIAOMI_BUSID = 431;      //IM 绑定小米推送证书ID
    public static int IM_HUAWEI_BUSID = 432;      //IM 绑定华为推送证书ID
}
