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
import android.widget.Toast;

import com.shunlian.app.App;

import java.util.regex.Pattern;

/**
 * Created by zhang on 2017/4/14 11 : 42.
 */

public class Common {

    private static Toast toast;

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


    public static boolean regularPwd(String pwd){
        String regular = "[a-zA-Z0-9]{8,16}";
        boolean matches = Pattern.matches(regular, pwd);
        return matches;
    }

    public static void staticToast(String content){
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT);
        }else{
            toast.setText(content);
        }
        toast.show();
    }
}
