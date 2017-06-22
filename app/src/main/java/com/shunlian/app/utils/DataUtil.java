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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017/4/26 16 : 12.
 */

public class DataUtil {


    public static List<String> getListString(int count,String content){
        List<String> lists = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            lists.add(content+i);
        }
        return lists;
    }
}
