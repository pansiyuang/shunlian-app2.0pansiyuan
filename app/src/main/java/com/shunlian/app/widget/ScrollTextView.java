package com.shunlian.app.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

/**
 * Created by zhanghe on 2018/12/18.
 */

public class ScrollTextView extends AppCompatTextView {

    // scrolling feature
    private Scroller mSlr;

    // milliseconds for a round of scrolling
    private int mRndDuration = 10000;

    // the X offset when paused
    private int mXPaused = 0;

    // whether it's being paused
    private boolean mPaused = true;

    private boolean isBackScroll = false;

    /*
    * constructor
    */
    public ScrollTextView(Context context) {
        this(context, null);
        // customize the TextView
        setSingleLine();
        setEllipsize(null);
    }

    /*
    * constructor
    */
    public ScrollTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
        // customize the TextView
        setSingleLine();
        setEllipsize(null);
    }

    /*
    * constructor
    */
    public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // customize the TextView
        setSingleLine();
        setEllipsize(null);
    }

    public void startScroll(){
        mPaused = true;
        mXPaused = 0;
        isBackScroll = false;
        resumeScroll();
    }

    /**
     * resume the scroll from the pausing point
     */
    private void resumeScroll() {
        if (!mPaused)
            return;

        // Do not know why it would not scroll sometimes
        // if setHorizontallyScrolling is called in constructor.
        setHorizontallyScrolling(true);

        // use LinearInterpolator for steady scrolling
        mSlr = new Scroller(this.getContext(), new LinearInterpolator());
        setScroller(mSlr);

        int scrollingLen = calculateScrollingLen();
        int distance = getWidth();
        int duration = (new Double(mRndDuration * distance * 1.00000
                / scrollingLen)).intValue();

        setVisibility(VISIBLE);
        if (mXPaused != 0){
            distance = - distance;
        }
        mSlr.startScroll(mXPaused, 0, distance, 0, duration);
        invalidate();
        mPaused = false;
    }

    private void backScroll(){
        mPaused = true;
        resumeScroll();
    }

    /**
     * calculate the scrolling length of the text in pixel
     *
     * @return the scrolling length in pixels
     */
    private int calculateScrollingLen() {
        TextPaint tp = getPaint();
        Rect rect = new Rect();
        String strTxt = getText().toString();
        tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
        int scrollingLen = rect.width() + getWidth();
        rect = null;
        return scrollingLen;
    }

    @Override
     /*
     * override the computeScroll to restart scrolling when finished so as that
     * the text is scrolled forever
     */
    public void computeScroll() {
        super.computeScroll();

        if (null == mSlr) return;

        mXPaused = mSlr.getCurrX();
        if (mSlr.isFinished() && (!mPaused) && !isBackScroll) {
            this.backScroll();
            isBackScroll = true;
        }
    }
}
