package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.ui.new_user.NewUserPageActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SwipeMenuLayout;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.timer.DayNewUserDownTimerView;
import com.shunlian.app.utils.timer.DayNoBlackDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ProgressViewLayout;
import com.zh.chartlibrary.common.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/22.
 */

public class NewUserGoodsAdapter extends BaseRecyclerAdapter<NewUserGoodsEntity.Goods> {
    private int second=(int)(System.currentTimeMillis()/1000);
    private int fristPostionNewGood = -1;
    private int fristPostionVeryGood = -1;
    private IAddShoppingCarListener mCarListener;
    public String type;
    private boolean isNew;
    public NewUserGoodsAdapter(Context context, List<NewUserGoodsEntity.Goods> lists,String type,boolean isNew) {
        super(context, true, lists);
        this.type = type;
        this.isNew = isNew;
    }


    public void updateTypeUser(String type,boolean isNew){
        this.type = type;
        this.isNew = isNew;
    }
    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods_new_user,
                parent, false);
        return new CollectionGoodsHolder(view);
    }

    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setText(getString(R.string.i_have_a_bottom_line));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (!isEmpty(payloads) && holder instanceof CollectionGoodsHolder){
            CollectionGoodsHolder mHolder = (CollectionGoodsHolder) holder;
            CollectionGoodsEntity.Goods goods = null;
            if (payloads.get(0) instanceof CollectionGoodsEntity.Goods){
                goods = (CollectionGoodsEntity.Goods) payloads.get(0);
            }else {
                List<CollectionGoodsEntity.Goods>
                        goodsList = (List<CollectionGoodsEntity.Goods>) payloads.get(0);
                goods = goodsList.get(position);
            }
        }else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
         if (holder instanceof CollectionGoodsHolder) {
            CollectionGoodsHolder mHolder = (CollectionGoodsHolder) holder;
            NewUserGoodsEntity.Goods goods = lists.get(position);
            GlideUtils.getInstance().loadOverrideImage(context,
                    mHolder.miv_goods_pic, goods.thumb,220,220);
            mHolder.mtv_title.setText(goods.title);
            String source = getString(R.string.rmb).concat(goods.price);
            mHolder.mtv_price.setText(Common.changeTextSize(source, getString(R.string.rmb), 12));
            mHolder.mtv_discount_price.setStrikethrough();
            if(!TextUtils.isEmpty(goods.market_price)) {
                mHolder.mtv_discount_price.setVisibility(View.VISIBLE);
                mHolder.mtv_discount_price.setText(getString(R.string.rmb)+goods.market_price);
            }else{
                mHolder.mtv_discount_price.setVisibility(View.GONE);
            }

            if(fristPostionNewGood==-1&&!goods.is_recommend){
                fristPostionNewGood = position;
            }

            if(isNew) {
                    if(goods.is_add_cart==1) {
                        mHolder.tv_user_shopping_car.setEnabled(false);
                        mHolder.tv_user_shopping_car.setBackgroundResource(R.drawable.rounded_corner_solid_ga_50px);
                        mHolder.tv_user_shopping_car.setText("加入购物车");
                    }else{
                        if(NewUserPageActivity.CURRENT_NUM==NewUserPageActivity.MAX_COUNT){
                            mHolder.tv_user_shopping_car.setEnabled(false);
                            mHolder.tv_user_shopping_car.setBackgroundResource(R.drawable.rounded_corner_solid_ga_50px);
                            mHolder.tv_user_shopping_car.setText("加入购物车");
                        }else {
                            mHolder.tv_user_shopping_car.setEnabled(true);
                            mHolder.tv_user_shopping_car.setBackgroundResource(R.drawable.rounded_corner_solid_pink_50px);
                            mHolder.tv_user_shopping_car.setText("加入购物车");
                        }
                    }
                    mHolder.tv_show_num.setVisibility(View.GONE);
                }else{
                    mHolder.tv_user_shopping_car.setEnabled(true);
                    mHolder.tv_user_shopping_car.setBackgroundResource(R.drawable.rounded_corner_solid_pink_50px);
                    mHolder.tv_show_num.setVisibility(View.VISIBLE);
                    mHolder.tv_user_shopping_car.setText("立即分享");
              }
             mHolder.progress_view.setVisibility(View.VISIBLE);
             mHolder.progress_view.setSecond(goods.process,100);
              if(!goods.is_recommend) {
                  mHolder.line_time_view.setVisibility(View.GONE);
                  mHolder.view_head_view.setVisibility(View.GONE);
                  if (position == fristPostionNewGood) {
                      mHolder.line_new_title.setVisibility(View.VISIBLE);
                      mHolder.line_new_title.setBackgroundColor(context.getResources().getColor(R.color.white));
                      mHolder.tv_usew_desc.setText("新人专区");
                      Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_xinren_zhuanqu);
                      drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                      mHolder.tv_usew_desc.setCompoundDrawables(drawable, null, null, null);
                      mHolder.tv_usew_desc.setCompoundDrawablePadding(DensityUtil.dip2px(context,5));
                  } else {
                      mHolder.line_new_title.setVisibility(View.GONE);
                  }
              }else if(position==0&&goods.is_recommend){//推荐
                   mHolder.line_new_title.setVisibility(View.VISIBLE);
                   mHolder.line_time_view.setVisibility(View.VISIBLE);
                   mHolder.view_head_view.setVisibility(View.VISIBLE);
                   mHolder.tv_usew_desc.setText("新人专区爆品");
                   Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_xinren_bangping);
                   drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                   mHolder.tv_usew_desc.setCompoundDrawables(drawable, null, null, null);
                   mHolder.tv_usew_desc.setCompoundDrawablePadding(DensityUtil.dip2px(context,5));
                   mHolder.line_new_title.setBackgroundResource(R.drawable.bg_border_line_bottom);
                  int seconds=(int)(System.currentTimeMillis()/1000)-second;
                  mHolder.downTime_firsts.cancelDownTimer();
                  try {
                      if(goods.finish!=0) {
                          mHolder.downTime_firsts.setLabelBackgroundColor(context.getResources().getColor(R.color.black));
                          mHolder.downTime_firsts.setDownTime(goods.finish - seconds);
                          mHolder.downTime_firsts.startDownTimer();
                      }
                  }catch (Exception e){

                  }

              }

        }
    }


    public class CollectionGoodsHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_show_num)
        TextView tv_show_num;

        @BindView(R.id.miv_goods_pic)
        MyImageView miv_goods_pic;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_discount_price)
        MyTextView mtv_discount_price;

        @BindView(R.id.tv_user_shopping_car)
        MyTextView tv_user_shopping_car;

        @BindView(R.id.mrlayout_item)
        MyRelativeLayout mrlayout_item;

        @BindView(R.id.tv_usew_desc)
        TextView tv_usew_desc;

        @BindView(R.id.line_new_title)
        LinearLayout line_new_title;

        @BindView(R.id.view_head_view)
        View view_head_view;

        @BindView(R.id.progress_view)
        ProgressViewLayout progress_view;

        @BindView(R.id.line_time_view)
        LinearLayout line_time_view;

        @BindView(R.id.tv_time_title)
        TextView tv_time_title;

        @BindView(R.id.downTime_firsts)
        DayNewUserDownTimerView downTime_firsts;

//        @BindView(R.id.tv_time_day)
//        TextView tv_time_day;
//        @BindView(R.id.tv_time_hh)
//        TextView tv_time_hh;
//        @BindView(R.id.tv_time_mm)
//        TextView tv_time_mm;

        public CollectionGoodsHolder(View itemView) {
            super(itemView);
            mrlayout_item.setOnClickListener(this);
            tv_user_shopping_car.setOnClickListener(this);
            downTime_firsts.setDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onFinish() {
                    if (downTime_firsts!=null)
                        downTime_firsts.cancelDownTimer();
                }
            });
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_user_shopping_car:
                    if (mCarListener != null){
                        mCarListener.onGoodsId(v,getAdapterPosition());
                    }
                    break;
                case R.id.mrlayout_item:
                    if (listener != null){
                        listener.onItemClick(v,getAdapterPosition());
                    }
                    break;
            }
        }


    }

    /**
     * 添加购物车
     * @param carListener
     */
    public void setAddShoppingCarListener(IAddShoppingCarListener carListener){
        mCarListener = carListener;
    }



    public interface IAddShoppingCarListener{
        void onGoodsId(View view, int position);
    }

}
