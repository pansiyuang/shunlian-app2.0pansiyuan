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
import android.content.res.Resources;

import com.shunlian.app.App;

/**
 * Created by zhang on 2017/4/14 11 : 42.
 */

public class Common {

    /**
     * 获取全局上下文
     * @return
     */
    public static Context getApplicationContext(){
        return App.mApp;
    }

    /**
     * 获取只有文件
     * @return
     */
    public static Resources getResources(){
        return getApplicationContext().getResources();
    }
}
