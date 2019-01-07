package com.shunlian.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.shunlian.app.R;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.TransformUtil;

public class CopyPopwindow extends PopupWindow {
    private Context mContext;
    private View contentview;

    public CopyPopwindow(Context context) {
        this.mContext = context;
        contentview = LayoutInflater.from(mContext).inflate(R.layout.layout_recycler, null);
        setContentView(contentview);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setWidth(DeviceInfoUtil.getDeviceWidth(mContext));
        int height = DeviceInfoUtil.getDeviceHeight(mContext);
        setHeight(height - TransformUtil.dip2px(mContext, 84.5f));

    }
}
