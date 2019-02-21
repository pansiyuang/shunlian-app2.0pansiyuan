package com.shunlian.app.widget.luckWheel;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cretin on 2017/12/26.
 */

public class WheelSurfView extends RelativeLayout {
    //当前的圆盘VIew
    private WheelSurfPanView mWheelSurfPanView;
    //Context
    private Context mContext;
    //开始按钮
    private ImageView mStart;
    //动画回调监听
    private RotateListener rotateListener;

    private LinearLayout llRoot;
    private TextView tvStart;
    private TextView tvGold;
    private boolean isAdd;
    private boolean isEnable = false; //子控件是否可以点击

    public void setRotateListener(RotateListener rotateListener) {
        mWheelSurfPanView.setRotateListener(rotateListener);
        this.rotateListener = rotateListener;
    }

    public WheelSurfView(Context context) {
        super(context);
        init(context, null);
    }

    public WheelSurfView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WheelSurfView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //开始抽奖的图标
    private Integer mGoImgRes;

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs != null) {
            //获得这个控件对应的属性。
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.wheelSurfView);
            try {
                mGoImgRes = typedArray.getResourceId(R.styleable.wheelSurfView_goImg, 0);
            } finally { //回收这个对象
                typedArray.recycle();
            }
        }

        //添加圆盘视图
        mWheelSurfPanView = new WheelSurfPanView(mContext, attrs);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mWheelSurfPanView.setLayoutParams(layoutParams);
        removeAllViews();
        addView(mWheelSurfPanView);

        //添加开始按钮
        mStart = new ImageView(mContext);
        //如果用户没有设置自定义的图标就使用默认的
        if (mGoImgRes == 0) {
            mStart.setImageResource(R.mipmap.img_choujiang_zhizhen);
        } else {
            mStart.setImageResource(mGoImgRes);
        }
        //给图片设置LayoutParams
        RelativeLayout.LayoutParams llStart = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llStart.addRule(RelativeLayout.CENTER_IN_PARENT);
        mStart.setLayoutParams(llStart);
        addView(mStart);

        mStart.setOnClickListener(v -> {
            //调用此方法是将主动权交个调用者 由调用者调用开始旋转的方法
            if (rotateListener != null)
                rotateListener.rotateBefore((ImageView) v);
        });
    }

    public void setConfig(Builder builder) {
        if (builder.mColors != null)
            mWheelSurfPanView.setmColors(builder.mColors);
        if (builder.mDeses != null)
            mWheelSurfPanView.setmDeses(builder.mDeses);
        if (builder.mHuanImgRes != 0)
            mWheelSurfPanView.setmHuanImgRes(builder.mHuanImgRes);
        if (builder.mIcons != null)
            mWheelSurfPanView.setmIcons(builder.mIcons);
        if (builder.mMainImgRes != 0)
            mWheelSurfPanView.setmMainImgRes(builder.mMainImgRes);
        if (builder.mMinTimes != 0)
            mWheelSurfPanView.setmMinTimes(builder.mMinTimes);
        if (builder.mTextColor != 0)
            mWheelSurfPanView.setmTextColor(builder.mTextColor);
        if (builder.mTextSize != 0)
            mWheelSurfPanView.setmTextSize(builder.mTextSize);
        if (builder.mType != 0)
            mWheelSurfPanView.setmType(builder.mType);
        if (builder.mVarTime != 0)
            mWheelSurfPanView.setmVarTime(builder.mVarTime);
        if (builder.mTypeNum != 0)
            mWheelSurfPanView.setmTypeNum(builder.mTypeNum);

        initStartContentLayout(builder.mStartContent);
        mWheelSurfPanView.show();
    }

    public void initStartContentLayout(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (!isAdd) {
            llRoot = new LinearLayout(mContext);
            llRoot.setGravity(Gravity.CENTER);
            llRoot.setOrientation(LinearLayout.VERTICAL);
            tvStart = new TextView(mContext);
            tvGold = new TextView(mContext);
            tvStart.setText("抽奖");
            tvGold.setText(content);
            tvStart.setTextColor(mContext.getResources().getColor(R.color.white));
            tvGold.setTextColor(mContext.getResources().getColor(R.color.white));
            tvGold.setMaxLines(2);
            tvGold.setTextSize(8);
            tvStart.setGravity(Gravity.CENTER);
            tvStart.getPaint().setFakeBoldText(true);
            tvGold.setGravity(Gravity.CENTER);
            tvStart.setTextSize(12);

            llRoot.addView(tvStart);
            llRoot.addView(tvGold);
            RelativeLayout.LayoutParams rootLayout = new RelativeLayout.LayoutParams(TransformUtil.dip2px(mContext, 30), ViewGroup.LayoutParams.WRAP_CONTENT);
            rootLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
            llRoot.setLayoutParams(rootLayout);
            addView(llRoot);
            isAdd = true;
        } else {
            tvGold.setText(content);
        }
    }

    public void clearHistory(int typeNum) {
        if (mWheelSurfPanView != null) {
            mWheelSurfPanView.initDefault(typeNum);
        }
    }

    /**
     * 开始旋转
     *
     * @param position 旋转最终的位置 注意 从1 开始 而且是逆时针递增
     */
    public void startRotate(int position) {
        if (mWheelSurfPanView != null && !mWheelSurfPanView.isRotating()) {
            mWheelSurfPanView.startRotate(position);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //视图是个正方形的 所以有宽就足够了 默认值是500 也就是WRAP_CONTENT的时候
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        // Children are just made to fill our space.
        final int childWidthSize = getMeasuredWidth();
        //高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);

        //onMeasure调用获取到当前视图大小之后，
        // 手动按照一定的比例计算出中间开始按钮的大小，
        // 再设置给那个按钮，免得造成用户传的图片不合适之后显示贼难看
        // 只设置一次
        if (isFirst) {
            isFirst = !isFirst;
            //获取中间按钮的大小
            ViewTreeObserver vto = mStart.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onGlobalLayout() {
                    mStart.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    float w = mStart.getMeasuredWidth();
                    float h = mStart.getMeasuredHeight();
                    //计算新的大小 默认为整个大小最大值的0.17 至于为什么是0.17  我只想说我乐意。。。。
                    int newW = (int) (((float) childWidthSize) * 0.17);
                    int newH = (int) (((float) childWidthSize) * 0.17 * h / w);
                    ViewGroup.LayoutParams layoutParams = mStart.getLayoutParams();
                    layoutParams.width = newW;
                    layoutParams.height = newH;
                    mStart.setLayoutParams(layoutParams);
                }
            });
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //记录当前是否是第一次回调onMeasure
    private boolean isFirst = true;


    //建造者模式
    public static final class Builder {
        //当前类型 1 自定义模式 2 暴力模式
        private int mType = 0;
        //最低圈数 默认值3 也就是说每次旋转都会最少转3圈
        private int mMinTimes = 0;
        //分类数量 如果数量为负数  通过代码设置样式
        private int mTypeNum = 0;
        //每个扇形旋转的时间
        private int mVarTime = 0;
        //文字描述集合
        private String[] mDeses;
        //自定义图标集合
        private List<Bitmap> mIcons;
        //背景颜色
        private Integer[] mColors;
        //整个旋转图的背景 只有类型为2时才需要
        private Integer mMainImgRes = 0;
        //GO图标
        private Integer mGoImgRes = 0;
        //圆环的图片引用
        private Integer mHuanImgRes = 0;
        //文字大小
        private float mTextSize = 0;
        //文字颜色
        private int mTextColor = 0;
        //抽奖内容
        private String mStartContent;

        public final WheelSurfView.Builder setmType(int mType) {
            this.mType = mType;
            return this;
        }

        public final WheelSurfView.Builder setmTypeNum(int mTypeNum) {
            this.mTypeNum = mTypeNum;
            return this;
        }

        public final WheelSurfView.Builder setmGoImgRes(int mGoImgRes) {
            this.mGoImgRes = mGoImgRes;
            return this;
        }

        public final WheelSurfView.Builder setmMinTimes(int mMinTimes) {
            this.mMinTimes = mMinTimes;
            return this;
        }

        public final WheelSurfView.Builder setmVarTime(int mVarTime) {
            this.mVarTime = mVarTime;
            return this;
        }

        public final WheelSurfView.Builder setmDeses(String[] mDeses) {
            this.mDeses = mDeses;
            return this;
        }

        public final WheelSurfView.Builder setmIcons(List<Bitmap> mIcons) {
            this.mIcons = mIcons;
            return this;
        }

        public final WheelSurfView.Builder setmColors(Integer[] mColors) {
            this.mColors = mColors;
            return this;
        }

        public final WheelSurfView.Builder setmMainImgRes(Integer mMainImgRes) {
            this.mMainImgRes = mMainImgRes;
            return this;
        }

        public final WheelSurfView.Builder setmHuanImgRes(Integer mHuanImgRes) {
            this.mHuanImgRes = mHuanImgRes;
            return this;
        }

        public final WheelSurfView.Builder setmTextSize(float mTextSize) {
            this.mTextSize = mTextSize;
            return this;
        }

        public final WheelSurfView.Builder setmTextColor(int mTextColor) {
            this.mTextColor = mTextColor;
            return this;
        }

        public final WheelSurfView.Builder setStartContent(String startContent) {
            this.mStartContent = startContent;
            return this;
        }

        public final Builder build() {
            return this;
        }
    }

    //旋转图片
    public static List<Bitmap> rotateBitmaps(List<Bitmap> source) {
        float mAngle = (float) (360.0 / source.size());
        List<Bitmap> result = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            Bitmap bitmap = source.get(i);
            int ww = bitmap.getWidth();
            int hh = bitmap.getHeight();
            // 定义矩阵对象
            Matrix matrix = new Matrix();
            // 缩放原图
            matrix.postScale(1f, 1f);
            // 向左旋转45度，参数为正则向右旋转
            matrix.postRotate(mAngle * i);
            //bmp.getWidth(), 500分别表示重绘后的位图宽高
            Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, ww, hh, matrix, true);
            result.add(dstbmp);
        }
        return result;
    }

    public void setEnable(boolean b) {
        isEnable = b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isEnable;
    }
}
