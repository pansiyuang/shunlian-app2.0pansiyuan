package com.shunlian.app.widget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;


public class Kanner extends BaseBanner<String, Kanner> {
    public Kanner(Context context) {
        this(context, null, 0);
    }


    public Kanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Kanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View onCreateItemView(final int position) {
        LinearLayout container = new LinearLayout(context);
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int deviceWidth = DeviceInfoUtil.getDeviceWidth(context);
        GlideUtils.getInstance().loadOverrideImage(getContext(),iv,list.get(position),deviceWidth,deviceWidth);
        container.addView(iv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return container;
    }

    private RoundCornerIndicaor indicator;

    @Override
    public View onCreateIndicator() {
        indicator = (RoundCornerIndicaor) LayoutInflater.from(context).inflate(layoutRes, null);
        indicator.setViewPager(vp, list.size());
        return indicator;
    }

    @Override
    public void setCurrentIndicator(int i) {
        indicator.setCurrentItem(i);
    }
}
