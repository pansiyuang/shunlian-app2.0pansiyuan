package com.shunlian.app.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.TaskHomeEntity;
import com.shunlian.app.eventbus_bean.GoldEggsTaskEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhanghe on 2018/9/1.
 */

public class SignGoldEggsLayout extends LinearLayout {


    private Unbinder bind;

    @BindViews({R.id.mtv_date1,R.id.mtv_date2,R.id.mtv_date3,
            R.id.mtv_date4,R.id.mtv_date5,R.id.mtv_date6,R.id.mtv_date7})
    List<MyTextView> mtv_date;

    @BindViews({R.id.mtv_eggs_count1,R.id.mtv_eggs_count2,R.id.mtv_eggs_count3,
            R.id.mtv_eggs_count4,R.id.mtv_eggs_count5,R.id.mtv_eggs_count6,R.id.mtv_eggs_count7})
    List<MyTextView> mtv_eggs_count;

    @BindView(R.id.miv_sign_state3)
    MyImageView miv_sign_state3;

    @BindViews({R.id.miv_sign_state1,R.id.miv_sign_state2,R.id.miv_sign_state3})
    List<MyImageView> miv_sign_state;

    @BindView(R.id.view_line1)
    View view_line;
    private int gray;
    private int pink;
    private List<TaskHomeEntity.SignDaysBean> mSignDaysBeans;
    private boolean isCanSign;//是否可以签到
    private String sign_date;

    public SignGoldEggsLayout(Context context) {
        this(context,null);
    }

    public SignGoldEggsLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SignGoldEggsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.item_sign_eggs,this);
        bind = ButterKnife.bind(this);

        gray = Color.parseColor("#EFEFEF");
        pink = Color.parseColor("#FB0036");
    }


    /**
     * 显示view
     *
     * @param views
     */
    protected void visible(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    /**
     * invisible
     *
     * @param views
     */
    protected void invisible(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     * 判断集合内容是否为空
     * @param list
     * @return
     */
    protected boolean isEmpty(List list){
        if (list == null){
            return true;
        }

        if (list.size() == 0){
            return true;
        }else {
            return false;
        }
    }

    protected boolean isEmpty(CharSequence sequence){
        return TextUtils.isEmpty(sequence);
    }

    public void setData(List<TaskHomeEntity.SignDaysBean> signDaysBeans){
        mSignDaysBeans = signDaysBeans;
        if (!isEmpty(signDaysBeans)){
            for (int i = 0; i < signDaysBeans.size(); i++) {
                if (i >= 7)break;
                TaskHomeEntity.SignDaysBean bean = signDaysBeans.get(i);
                mtv_date.get(i).setText(bean.date);
                MyTextView textView = mtv_eggs_count.get(i);
                if (!isEmpty(bean.gold_num)){
                    visible(textView);
                    textView.setText(bean.gold_num);
                }else {
                    invisible(textView);
                }

                if (i < 3){
                    if ("1".equals(bean.sign_status)){//已签到
                        miv_sign_state.get(i).setImageResource(R.mipmap.icon_renwu_yiqiandao);
                    }else if ("0".equals(bean.sign_status)){//可以签到
                        miv_sign_state.get(i).setImageResource(R.mipmap.btn_renwu_qiandao);
                        scaleAnim(miv_sign_state.get(i));
                        if (i == 2){
                            isCanSign = true;
                            sign_date = bean.date;
                        }
                    }else {//不能签到，或者错过签到
                        miv_sign_state.get(i).setImageResource(R.mipmap.icon_renwu_weiqiandao);
                        view_line.setBackgroundColor(i == 1 ? gray : pink);
                    }
                }
            }
        }
    }

    @OnClick(R.id.miv_sign_state3)
    public void sign(){
        if (isCanSign){
            GoldEggsTaskEvent event = new GoldEggsTaskEvent();
            event.isClickSign = true;
            event.sign_date = sign_date;
            EventBus.getDefault().post(event);
        }
    }


    private void scaleAnim(ImageView view){
        ValueAnimator va = ValueAnimator.ofFloat(1.0f,0.96f,1.0f);
        va.setDuration(1500);
        va.setRepeatMode(ValueAnimator.REVERSE);
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setInterpolator(new LinearInterpolator());
        va.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            view.setScaleX(value);
            view.setScaleY(value);
        });
        va.start();
    }


    public void  detachView(){
        if (bind != null){
            bind.unbind();
        }
        isCanSign = false;
    }
}
