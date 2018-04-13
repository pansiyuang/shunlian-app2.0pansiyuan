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

import android.os.Handler;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static com.shunlian.app.utils.Common.getApplicationContext;

/**
 * Created by zhang on 2017/4/26 16 : 12.
 */

public class JpushUtil {
    private static final int MSG_SET_ALIAS_TAGS = 1001;
    private static String alias = "null";
    private static Set<String> tagss;
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS_TAGS:
                    LogUtil.augusLogW("Set JPUSH alias and tags in handler.");
                    // 调用 JPush 接口来设置别名。
//                    LogUtil.augusLogW("yxf--"+tagss.toString());
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            alias,
                            tagss,
                            mAliasCallback);
                    break;
                default:
                    LogUtil.augusLogW("Set JPUSH Unhandled msg.");
            }
        }
    };
    private static TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set JPUSH tag and alias success";
                    LogUtil.augusLogW(logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to Set JPUSH alias and tags due to timeout. Try again after 60s.";
                    LogUtil.augusLogW(logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS_TAGS), 1000 * 60);
                    break;
                default:
                    logs = "Failed Set JPUSH with errorCode = " + code;
                    LogUtil.augusLogW(logs);
            }
        }
    };

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static void setJPushAlias() {
        String member_id;
        member_id = SharedPrefUtil.getSharedPrfString("member_id", "null");
        tagss = SharedPrefUtil.getSharedPrfStringss("tags", null);
//        tagss=new HashSet<>();
//        tagss.add("123");
//        tagss.add("456");
        if (isValidTagAndAlias(member_id)) {
            alias = member_id;
        }
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS_TAGS));
    }


}
