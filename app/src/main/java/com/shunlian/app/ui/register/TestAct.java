package com.shunlian.app.ui.register;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.RippleTextView;
import com.shunlian.app.widget.RollNumView;

import butterknife.BindView;

public class TestAct extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.tv3)
    TextView tv3;

    @BindView(R.id.rnview)
    RollNumView rnview;

    @BindView(R.id.rtv_car)
    RippleTextView rtv_car;

    @BindView(R.id.iv_logo)
    MyImageView iv_logo;

    @BindView(R.id.et_test)
    EditText et_test;
    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, TestAct.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        rtv_car.setOnClickListener(this);

        et_test.addTextChangedListener(new TextWatcher() {
            String Reg="^[\u4e00-\u9fa5]{1}$";  //汉字的正规表达式
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean b = Common.regularPwd(s.toString());
                if (b) {
                    Common.staticToast("正确");
                }else {
                    Common.staticToast("错误");
                }
                String[] split = s.toString().split("");
                for (int i = 0; i < split.length; i++) {
                    System.out.println("==="+split[i]);
                }

               /* int charLength = 0;
                String[] split = s.toString().split("");
                for (int i = 0; i < split.length; i++) {
                    if (i == 0){
                        continue;
                    }
                    if (Pattern.matches(Reg,split[i])){
                        charLength += 2;
                    }else {
                        charLength++;
                    }
                }
                if (charLength > 24){
                    Common.staticToast(getString(R.string.RegisterTwoAct_ncszgc));
                }*/

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        rnview.setMode(RollNumView.Mode.UP);
        rnview.setTextColor(Color.WHITE);
        rnview.setTextSize(16);
        rnview.setNumber(0);
        rnview.setTargetNumber(11);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.tv1:
                RegisterAct.startAct(this,RegisterAct.UNBIND_SUPERIOR_USER,null);
                break;
            case R.id.tv2:
                RegisterAct.startAct(this,RegisterAct.UNBIND_NEW_USER,null);
                break;
            case R.id.tv3:
                RegisterAct.startAct(this,RegisterAct.UNBIND_OLD_USER,null);
                break;
            case  R.id.rtv_car:
                System.out.println("点击按钮");
                iv_logo.setVisibility(View.VISIBLE);

                AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
                alphaAnimation.setDuration(200);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        parabolaAnimation();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
              iv_logo.setAnimation(alphaAnimation);

                break;
        }
    }

    private void parabolaAnimation() {

        int[] startPoint = new int[2];
        rtv_car.getLocationInWindow(startPoint);

        int[] endPoint = new int[2];
        rnview.getLocationInWindow(endPoint);

        // 开始绘制贝塞尔曲线
        Path path = new Path();
        // 移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startPoint[0] + rtv_car.getMeasuredWidth() / 2, startPoint[1] + rtv_car.getMeasuredHeight() / 2);
        // 使用二阶贝塞尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startPoint[0] - endPoint[0]) / 2 + endPoint[0], startPoint[1]-600, endPoint[0], endPoint[1]- rnview.getMeasuredHeight());
        mPathMeasure = new PathMeasure(path, false);

        // 属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(500);

        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                // mCurrentPosition此时就是中间距离点的坐标值
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                System.out.println("mCurrentPosition[0]=="+mCurrentPosition[0]);
                System.out.println("mCurrentPosition[1]=="+mCurrentPosition[1]);
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                iv_logo.setTranslationX(mCurrentPosition[0]);
                iv_logo.setTranslationY(mCurrentPosition[1]);
            }
        });

        // 开始执行动画
        valueAnimator.start();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iv_logo.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public void changeCoordinates(float x, float y) {
        View v = findViewById(R.id.iv_logo);
        v.setX(x);
        v.setY(y);
    }


//                ObjectAnimator mAnimator = ObjectAnimator.ofMultiFloat(this, "changeCoordinates", path);
//                mAnimator.setDuration(10000);
//                mAnimator.setRepeatMode(Animation.RESTART);
//                mAnimator.setRepeatCount(ValueAnimator.INFINITE);
//                mAnimator.setInterpolator(new LinearInterpolator());
//                mAnimator.start();

}
