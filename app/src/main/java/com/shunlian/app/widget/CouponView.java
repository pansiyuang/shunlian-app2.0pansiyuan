package com.shunlian.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.shunlian.app.R;

/**
 * Created by Administrator on 2017/11/15.
 */

public class CouponView extends MyRelativeLayout {

    private MyTextView mtv_price;
    private MyTextView mtv_name;
    private MyTextView mtv_full_cut;
    private MyRelativeLayout mrl_bg;

    public CouponView(Context context) {
        this(context,null);
    }

    public CouponView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CouponView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_coupon, null, false);
        addView(inflate);
        mtv_price = (MyTextView) inflate.findViewById(R.id.mtv_price);
        mtv_name = (MyTextView) inflate.findViewById(R.id.mtv_name);
        mtv_full_cut = (MyTextView) inflate.findViewById(R.id.mtv_full_cut);
        mrl_bg = (MyRelativeLayout) inflate.findViewById(R.id.mrl_bg);
    }

    /**
     * 设置优惠券价格
     * @param price
     */
    public void setPrice(String price){
        mtv_price.setText(price);
    }

    /**
     * 设置优惠券类型
     * @param type
     */
    public void setCouponType(String type){
        mtv_name.setText(type);
    }

    /**
     * 设置优惠券满多少可用
     * @param fullCut
     */
    public void setFullCut(String fullCut){
        mtv_full_cut.setText(fullCut);
    }

    /**
     * status为true是未领取状态，否则领取
     * @param status
     */
    public void setReceiveStatus(boolean status){
        if (status){
            mrl_bg.setBackgroundResource(R.mipmap.img_youhuiquan_1);
        }else {
            mrl_bg.setBackgroundResource(R.mipmap.img_youhuiquan_2);
        }
    }
}
