package com.shunlian.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


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

/**
 * Created by zhang on 2017/6/26 17 : 35.
 */

public abstract class CustomAnimation extends View {

    public CustomAnimation(Context context) {
        super(context);
    }


    public CustomAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public CustomAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 开启动画处理逻辑
     */
    public abstract void startAnimation();


    /**
     * 停止动画逻辑处理
     */
    public abstract void stopAnimation();


    /**
     * 下拉过程中的刷新头显示的高度
     * @param height
     */
    public void pullDownHeight(int height){

    }
}
