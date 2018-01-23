package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.shunlian.app.R;
import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class DayListAdapter extends BaseRecyclerAdapter<ActivityListEntity.MData.Good.MList> {
    public String isStart;

    public DayListAdapter(Context context, boolean isShowFooter, List<ActivityListEntity.MData.Good.MList> datas,String isStart) {
        super(context, isShowFooter, datas);
        this.isStart=isStart;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new OneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_list, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OneHolder) {
            OneHolder oneHolder = (OneHolder) holder;
            ActivityListEntity.MData.Good.MList data=lists.get(position);
            oneHolder.mtv_title.setText(data.title);
            oneHolder.mtv_priceM.setText(context.getResources().getString(R.string.common_yuan)+data.market_price);
            oneHolder.mtv_priceM.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
            oneHolder.mtv_priceA.setText(context.getResources().getString(R.string.common_yuan)+data.act_price);
            if ("1".equals(data.remind_status)){
                oneHolder.isRemind=true;
                oneHolder.miv_clock.setVisibility(View.GONE);
                oneHolder.mtv_quxiao.setText(getString(R.string.day_quxiaotixing));
            }else {
                oneHolder.isRemind=false;
                oneHolder.miv_clock.setVisibility(View.VISIBLE);
                oneHolder.mtv_quxiao.setText(getString(R.string.day_tixinwo));
            }
            GradientDrawable copyBackground = (GradientDrawable) oneHolder.mllayout_remind.getBackground();
            if ("1".equals(isStart)){
                //设置圆角背景
                oneHolder.mtv_quxiao.setText(R.string.day_lijiqianggou);
                copyBackground.setColor(getColor(R.color.pink_color));//设置填充色
                oneHolder.mtv_priceA.setTextColor(getColor(R.color.pink_color));
                oneHolder.seekbar_grow.setProgress(data.percent);
                oneHolder.mtv_desc.setText(data.str_surplus_stock);
                oneHolder.seekbar_grow.setVisibility(View.VISIBLE);
                oneHolder.seekbar_grow.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
                oneHolder.mtv_number.setVisibility(View.INVISIBLE);
            }else {
                oneHolder.mtv_priceA.setTextColor(getColor(R.color.value_2096F2));
                copyBackground.setColor(getColor(R.color.value_2096F2));//设置填充色
                oneHolder.seekbar_grow.setVisibility(View.GONE);
                oneHolder.mtv_number.setVisibility(View.VISIBLE);
                oneHolder.mtv_number.setText(String.format(getString(R.string.day_yiyoutixing),data.remind_count));
            }
            GlideUtils.getInstance().loadImage(context,oneHolder.miv_img,data.goods_pic);
        }
    }

    class OneHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_quxiao)
        MyTextView mtv_quxiao;

        @BindView(R.id.mllayout_remind)
        MyLinearLayout mllayout_remind;

        @BindView(R.id.mtv_priceA)
        MyTextView mtv_priceA;

        @BindView(R.id.mtv_priceM)
        MyTextView mtv_priceM;

        @BindView(R.id.mtv_number)
        MyTextView mtv_number;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.miv_clock)
        MyImageView miv_clock;

        @BindView(R.id.seekbar_grow)
        SeekBar seekbar_grow;

        private boolean isRemind=false;

        OneHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mllayout_remind.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null){
                listener.onItemClick(view,getAdapterPosition());
            }
            switch (view.getId()){
                case R.id.mllayout_remind:
                    if ("1".equals(isStart)){

                    } else if (isRemind){

                    }else {

                    }
                    break;
            }
        }
    }

}
