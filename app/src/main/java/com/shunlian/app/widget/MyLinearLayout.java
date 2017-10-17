package com.shunlian.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;


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
 * Created by zhang on 2017/6/23 16 : 21.
 */

public class MyLinearLayout extends LinearLayout {

    private boolean aBoolean;
    private int control_width;
    private int control_height;

    public MyLinearLayout(Context context) {
        this(context,null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyLinearLayout, defStyleAttr,0);
        aBoolean = a.getBoolean(R.styleable.MyLinearLayout_ll_control_adapter, false);
        control_width = a.getInteger(R.styleable.MyLinearLayout_ll_control_width, 0);
        control_height = a.getInteger(R.styleable.MyLinearLayout_ll_control_height, 0);
        a.recycle();
    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (aBoolean) {
            int mode_width = MeasureSpec.getMode(widthMeasureSpec);
            int mode_height = MeasureSpec.getMode(heightMeasureSpec);

            int[] realWH = TransformUtil.countRealWH(getContext(),control_width, control_height);

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(realWH[0], mode_width);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(realWH[1], mode_height);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置控件大小，宽高都为像素
     * @param width
     * @param height
     */
    public void setWHProportion(int width,int height){
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int[] realWH = TransformUtil.countRealWH(getContext(),width, height);
        int real_w = realWH[0];
        int real_h = realWH[1];

        layoutParams.width = real_w;
        layoutParams.height = real_h;
        setLayoutParams(layoutParams);
    }
}
