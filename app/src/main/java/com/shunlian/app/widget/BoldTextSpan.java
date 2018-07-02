package com.shunlian.app.widget;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by Administrator on 2018/3/17.
 */

public class BoldTextSpan extends UnderlineSpan {




    public void updateMeasureState(TextPaint p) {
        p.setFakeBoldText(true);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setFakeBoldText(true);
    }

    /**
     * Return a special type identifier for this span class.
     */
    @Override
    public int getSpanTypeId() {
        return 0;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     * @see #CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
