package com.shunlian.app.widget;

import android.os.Parcel;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.shunlian.app.listener.OnTagClickListener;

/**
 * Created by Administrator on 2018/4/4.
 */

public class ClickableColorSpan extends ClickableSpan {


    private final int mTagId;
    private int mColor;
    private OnTagClickListener mListener;

    public ClickableColorSpan(int tagId,@ColorInt int color) {
        mTagId = tagId;
        mColor = color;
    }

    public ClickableColorSpan(Parcel src) {
        mTagId = src.readInt();
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }


    public int getSpanTypeIdInternal() {
        return 11;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /** @hide */
    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(mTagId);
    }

    public int getTagId() {
        return mTagId;
    }
    /**
     * Performs the click action associated with this span.
     *
     * @param widget
     */
    @Override
    public void onClick(View widget) {
        if (mListener != null){
            mListener.onTagClick(mTagId);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(mColor);
    }

    public void setOnClickItemListener(OnTagClickListener listener){
        mListener = listener;
    }
}
