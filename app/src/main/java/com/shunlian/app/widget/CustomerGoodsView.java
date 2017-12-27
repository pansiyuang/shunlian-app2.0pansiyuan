package com.shunlian.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by Administrator on 2017/12/27.
 */

public class CustomerGoodsView extends LinearLayout implements View.OnClickListener {

    private MyLinearLayout mllayout_count;
    private MyImageView miv_goods_icon;
    private MyTextView tv_goods_name;
    private MyTextView tv_goods_price;
    private MyTextView tv_goods_param;
    private MyTextView tv_goods_count;
    private MyTextView mtv_count_reduce;
    private MyTextView mtv_count;
    private MyTextView mtv_count_add;
    private MyTextView mtv_refund_price;
    private MyTextView textView;
    private MyImageView imgShop;
    private int maxCount = 10;
    private IChangeCountListener mListener;

    public CustomerGoodsView(Context context) {
        this(context, null);
    }

    public CustomerGoodsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerGoodsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(Color.WHITE);
        int px = TransformUtil.dip2px(getContext(), 0.5f);

        LinearLayout layoutName = new LinearLayout(getContext());

        LinearLayout.LayoutParams layoutParamsName = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsName.leftMargin = TransformUtil.dip2px(getContext(),10);
        layoutName.setLayoutParams(layoutParamsName);

        layoutName.setGravity(Gravity.CENTER_VERTICAL);

        //图标
        imgShop = new MyImageView(getContext());
        imgShop.setImageResource(R.mipmap.img_shoppingcar_shop);
        layoutName.addView(imgShop);
        LinearLayout.LayoutParams imgParams = (LayoutParams) imgShop.getLayoutParams();
        imgParams.rightMargin = TransformUtil.dip2px(getContext(),7.5f);
        imgShop.setLayoutParams(imgParams);

        //店铺名
        textView = new MyTextView(getContext());
        textView.setTextColor(getResources().getColor(R.color.new_text));
        textView.setTextSize(14);
        int padding = TransformUtil.dip2px(getContext(), 13);
        textView.setPadding(0,padding,0,padding);
        layoutName.addView(textView);

        addView(layoutName);

        //分割线
        View viewLine = new View(getContext());
        viewLine.setBackgroundColor(getResources().getColor(R.color.light_gray_three));
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,px);
        viewLine.setLayoutParams(layoutParams);
        addView(viewLine);


        //商品信息
        View view = LayoutInflater.from(getContext()).inflate(R.layout.goods_info, null);
        addView(view);

        mllayout_count = (MyLinearLayout) view.findViewById(R.id.mllayout_count);
        GradientDrawable background = (GradientDrawable) mllayout_count.getBackground();
        background.setColor(Color.WHITE);

        background.setStroke(px, Color.parseColor("#AAAAAA"));

        miv_goods_icon = (MyImageView) view.findViewById(R.id.miv_goods_icon);

        tv_goods_name = (MyTextView) view.findViewById(R.id.tv_goods_name);
        tv_goods_price = (MyTextView) view.findViewById(R.id.tv_goods_price);
        tv_goods_param = (MyTextView) view.findViewById(R.id.tv_goods_param);
        tv_goods_count = (MyTextView) view.findViewById(R.id.tv_goods_count);
        mtv_count_reduce = (MyTextView) view.findViewById(R.id.mtv_count_reduce);
        mtv_count = (MyTextView) view.findViewById(R.id.mtv_count);
        mtv_count_add = (MyTextView) view.findViewById(R.id.mtv_count_add);
        mtv_refund_price = (MyTextView) view.findViewById(R.id.mtv_refund_price);


        mtv_count_add.setOnClickListener(this);
        mtv_count_reduce.setOnClickListener(this);
    }

    /**
     * 获取商品icon控件
     *
     * @return
     */
    public MyImageView getGoodsIcon() {
        return miv_goods_icon;
    }

    /**
     * 商品title
     * @param sequence
     * @return
     */
    public CustomerGoodsView setGoodsTitle(CharSequence sequence) {
        tv_goods_name.setText(sequence);
        return this;
    }

    /**
     * 商品属性
     * @param sequence
     * @return
     */
    public CustomerGoodsView setGoodsParams(CharSequence sequence) {
        tv_goods_param.setText(sequence);
        return this;
    }

    /**
     * 商品价格
     * @param sequence
     * @return
     */
    public CustomerGoodsView setGoodsPrice(CharSequence sequence) {
        tv_goods_price.setText(sequence);
        return this;
    }

    /**
     * 商品数量
     * @param sequence
     * @return
     */
    public CustomerGoodsView setGoodsCount(CharSequence sequence) {
        mllayout_count.setVisibility(GONE);
        tv_goods_count.setVisibility(VISIBLE);
        tv_goods_count.setText(sequence);
        return this;
    }

    /**
     * 设置标签名字
     * @param sequence
     * @param isStoreName 第一个参数如果是店铺名，该参数是true
     * @return
     */
    public CustomerGoodsView setLabelName(CharSequence sequence,boolean isStoreName){
        if (isStoreName) {
            imgShop.setVisibility(VISIBLE);
        }else {
            imgShop.setVisibility(GONE);
        }
        textView.setText(sequence);
        return this;
    }

    /**
     * 设置选择商品的最大数量
     * @param maxCount
     */
    public void selectCount(int maxCount){
        this.maxCount = maxCount;
        tv_goods_count.setVisibility(GONE);
        mllayout_count.setVisibility(VISIBLE);

    }

    /**
     * 获取商品数量
     * @return
     */
    public int getCurrentCount() {
        return mtv_count != null ? Integer.parseInt(mtv_count.getText().toString()) : 1;
    }

    /**
     * 设置退款金额
     * @param sequence
     * @return
     */
    public CustomerGoodsView setRefundPrice(CharSequence sequence){
        mtv_refund_price.setVisibility(VISIBLE);
        mtv_refund_price.setText(sequence);
        return this;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (mtv_count == null){
            return;
        }
        CharSequence text = mtv_count.getText();
        int i = Integer.parseInt(text.toString());
        switch (v.getId()){
            case R.id.mtv_count_add:
                if (i >= maxCount){
                    return;
                }
                i++;
                break;
            case R.id.mtv_count_reduce:
                if (i <= 1){
                    return;
                }
                i--;
                break;
        }
        mtv_count.setText(String.valueOf(i));

        if (mListener != null){
            mListener.onChangeCount(i);
        }
    }

    /**
     * 数量改变监听
     * @param listener
     */
    public void setIChangeCountListener(IChangeCountListener listener){
        mListener = listener;
    }

    public interface IChangeCountListener{
        /**
         * 数量改变
         * @param count
         */
        void onChangeCount(int count);
    }
}
