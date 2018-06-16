package com.shunlian.app.newchat.util;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.widget.MyImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/6/15.
 */

public class ChatToast {
    private MyImageView miv_icon;
    private TextView tv_name;
    private TextView tv_content;
    private TextView tv_date;

    private WindowManager.LayoutParams mParams;
    private boolean mShowTime;
    private boolean mIsShow;
    private WindowManager mWdm;
    private View mToastView;
    private Toast toast;
    private Timer mTimer;
    private Context mContext;

    private ChatToast(Context context, String text, boolean showTime) {
        mContext = context;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_chat_toast, null);
        miv_icon = (MyImageView) rootView.findViewById(R.id.miv_icon);
        tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        tv_content = (TextView) rootView.findViewById(R.id.tv_content);
        tv_date = (TextView) rootView.findViewById(R.id.tv_date);


        mShowTime = showTime;//记录Toast的显示长短类型
        mIsShow = false;//记录当前Toast的内容是否已经在显示
        mWdm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //通过Toast实例获取当前android系统的默认Toast的View布局
        toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        toast.setView(rootView);
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
        mToastView = toast.getView();
        mToastView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mToastView.bringToFront();
        mTimer = new Timer();
        //设置布局参数
        setParams();
    }

    public static ChatToast MakeText(Context context, String text, boolean showTime) {
        ChatToast result = new ChatToast(context, text, showTime);
        return result;
    }

    private void setParams() {
        // TODO Auto-generated method stub
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.ChatToastAnim;//设置进入退出动画效果
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
    }

    public void show() {
        if (!mIsShow) {//如果Toast没有显示，则开始加载显示
            mIsShow = true;
            mWdm.addView(mToastView, mParams);//将其加载到windowManager上
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mWdm.removeView(mToastView);
                    mIsShow = false;
                }
            }, (long) (mShowTime ? 3500 : 2000));
        }
    }
}
