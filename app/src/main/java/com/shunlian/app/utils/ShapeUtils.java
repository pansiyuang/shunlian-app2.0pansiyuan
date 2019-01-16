package com.shunlian.app.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;

/**
 * Created by zhanghe on 2019/1/16.
 */
public class ShapeUtils {

    /**
     * 无边框
     * @param context
     * @param color 背景颜色
     * @param radius 圆角弧度 dp
     * @return
     */
    public static GradientDrawable commonShape(Context context,int color, float radius){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(TransformUtil.dip2px(context,radius));
        return drawable;
    }

    /**
     * 无边框背景渐变色
     * @param context
     * @param orientation 渐变方向
     * @param colorsint 渐变颜色
     * @param radius 圆角弧度 dp
     * @return
     */
    public static GradientDrawable commonShadeShape(Context context, GradientDrawable.Orientation orientation,
                                               @ColorInt int[] colorsint,float radius){
        GradientDrawable drawable = new GradientDrawable(orientation,colorsint);
        drawable.setCornerRadius(TransformUtil.dip2px(context,radius));
        return drawable;
    }

    /**
     * 有边框
     * @param color 背景颜色
     * @param radius 圆角弧度 dp
     * @param strokeWidth 边框宽 dp
     * @param strokecolor 边框颜色
     * @return
     */
    public static GradientDrawable commonShape(Context context,int color, float radius,
                                               int strokeWidth,int strokecolor){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(TransformUtil.dip2px(context,radius));
        drawable.setStroke(TransformUtil.dip2px(context,strokeWidth),strokecolor);
        return drawable;
    }


    /**
     * 有边框渐变
     * @param context
     * @param orientation 渐变方向
     * @param colorsint 渐变颜色
     * @param radius 圆角弧度
     * @param strokeWidth 外边框宽度
     * @param strokecolor 外边框颜色
     * @return
     */
    public static GradientDrawable commonShadeShape(Context context,GradientDrawable.Orientation orientation,
                                               @ColorInt int[] colorsint,float radius,
                                               int strokeWidth,int strokecolor){
        GradientDrawable drawable = new GradientDrawable(orientation,colorsint);
        drawable.setCornerRadius(TransformUtil.dip2px(context,radius));
        drawable.setStroke(TransformUtil.dip2px(context,strokeWidth),strokecolor);
        return drawable;
    }
}
