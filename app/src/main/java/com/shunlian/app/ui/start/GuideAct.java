package com.shunlian.app.ui.start;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.ui.MBaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/4/12 0012.
 */
public class GuideAct extends MBaseActivity {
    @BindView(R.id.vp_guide)
    ViewPager vp_guide;

    @BindView(R.id.mtv_start)
    MyTextView mtv_start;

    @BindView(R.id.mllayout_point_group)
    MyLinearLayout mllayout_point_group;// 小灰点

     @BindView(R.id.mrlayout_point)
     MyRelativeLayout mrlayout_point;

     @BindView(R.id.view_point)
    View view_point;// 小红点

    private int[] mGuideLists;
    private int mPointWidth;// 小圆点之间的距离

    public static void startAct(Context context) {
        Intent intent = new Intent(context, GuideAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_guide;
    }

    protected void initData() {
        mGuideLists = new int[]{R.mipmap.guide_one, R.mipmap.guide_two,
                R.mipmap.guide_three};
//        mGuideLists = new int[]{R.mipmap.guide_one, R.mipmap.guide_two,
//                R.mipmap.guide_three};
        initDot();
        vp_guide.setAdapter(new GuideAdapter());
        vp_guide.setOnPageChangeListener(new GudiePagerChangeListener());
    }

    /**
     * 初始化点
     */
    private void initDot() {
        for (int i = 0; i < mGuideLists.length; i++) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.point_bg_guide);

            // dp和px的关系: dp = px/设备密度
            float density = getResources().getDisplayMetrics().density;
            int width = (int) (5 * density);
            int height = (int) (5 * density);
            //System.out.println("设备密度:" + density);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            if (i > 0) {
                params.leftMargin = width*2;
            }
            view.setLayoutParams(params);
            mllayout_point_group.addView(view);
        }

        mllayout_point_group.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mllayout_point_group.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        mPointWidth = mllayout_point_group.getChildAt(1).getLeft()
                                - mllayout_point_group.getChildAt(0).getLeft();
                    }
                });
    }

    private class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mGuideLists.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MyImageView imageView = new MyImageView(baseAct);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(mGuideLists[position]);
            container.addView(imageView);
            return imageView;
        }

        /* 销毁pager */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private class GudiePagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            float leftWidth = mPointWidth * positionOffset + position * mPointWidth;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view_point
                    .getLayoutParams();// 获取小红点的布局参数
            layoutParams.leftMargin = (int) leftWidth;// 设置小红点的左边距
            view_point.setLayoutParams(layoutParams);
        }

        @Override
        public void onPageSelected(int position) {
//            if (position == 3) {
            if (position == 2) {
                mrlayout_point.setVisibility(View.GONE);
                mtv_start.setVisibility(View.VISIBLE);
                mtv_start.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        MainActivity.startAct(baseAct,"");
                        finish();
                    }
                });
            } else {
                mtv_start.setVisibility(View.GONE);
                mrlayout_point.setVisibility(View.GONE);
            }
        }

    }
}