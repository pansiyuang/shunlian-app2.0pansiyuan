package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.shunlian.app.R;
import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.presenter.DayDayPresenter;
import com.shunlian.app.ui.activity.DayDayAct;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.timer.DDPDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class DayListAdapter extends BaseRecyclerAdapter<ActivityListEntity.MData.Good.MList> {
    public String isStart, title, content, time, from, id;
    private DayDayPresenter dayDayPresenter;

    public DayListAdapter(Context context, boolean isShowFooter, List<ActivityListEntity.MData.Good.MList> datas, DayDayPresenter dayDayPresenter, ActivityListEntity activityListEntity) {
        super(context, isShowFooter, datas);
        this.isStart = activityListEntity.datas.sale;
        this.dayDayPresenter = dayDayPresenter;
        this.title = activityListEntity.datas.title;
        this.content = activityListEntity.datas.content;
        this.time = activityListEntity.datas.time;
        this.from = activityListEntity.from;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new OneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_list, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OneHolder) {
            final OneHolder oneHolder = (OneHolder) holder;
            ActivityListEntity.MData.Good.MList data = lists.get(position);
            oneHolder.mtv_title.setText(data.title);
            oneHolder.mtv_priceM.setText(context.getResources().getString(R.string.common_yuan) + data.market_price);
            oneHolder.mtv_priceM.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
            oneHolder.mtv_priceA.setText(context.getResources().getString(R.string.common_yuan) + data.act_price);
            oneHolder.goods_id = data.goods_id;
            oneHolder.position = position;
            if (position == 0) {
                oneHolder.mrlayout_time.setVisibility(View.VISIBLE);
                oneHolder.view_title.setVisibility(View.VISIBLE);
                oneHolder.mtv_titles.setText(title);
                oneHolder.mtv_content.setText(content);
//                if ("1".equals(isStart)) {//1：活动进行中   0：活动未开始
//                    oneHolder.ddp_downTime.setLabelBackgroundColor(getColor(R.color.new_text));
//                    oneHolder.ddp_downTime.setTimeUnitTextColor(getColor(R.color.new_text));
//                } else {
//                    oneHolder.ddp_downTime.setLabelBackgroundColor(getColor(R.color.value_2096F2));
//                    oneHolder.ddp_downTime.setTimeUnitTextColor(getColor(R.color.value_2096F2));
//                }

                if (!oneHolder.isStartDownTime) {
                    String times = isEmpty(time) ? "0" : time;
                    oneHolder.ddp_downTime.setDownTime(Integer.parseInt(times));
                    oneHolder.ddp_downTime.startDownTimer();
                    oneHolder.isStartDownTime = true;
                    oneHolder.ddp_downTime.setDownTimerListener(new OnCountDownTimerListener() {
                        @Override
                        public void onFinish() {
                            oneHolder.isStartDownTime = false;
                            if (context instanceof DayDayAct) {
                                DayDayAct act = (DayDayAct) context;
                                if (act.isFinishing()) {
                                    oneHolder.ddp_downTime.cancelDownTimer();
                                    return;
                                }
                                act.minitData();
                            }
                        }
                    });
                }
            } else {
                oneHolder.mrlayout_time.setVisibility(View.GONE);
                oneHolder.view_title.setVisibility(View.GONE);
            }
            GradientDrawable copyBackground = (GradientDrawable) oneHolder.mllayout_remind.getBackground();
            if ("1".equals(isStart)) {
                //设置圆角背景
                oneHolder.miv_clock.setVisibility(View.GONE);
                oneHolder.mtv_quxiao.setText(R.string.day_lijiqianggou);
                copyBackground.setColor(getColor(R.color.pink_color));//设置填充色
                oneHolder.mtv_priceA.setTextColor(getColor(R.color.pink_color));
                oneHolder.seekbar_grow.setProgress(data.percent);
                oneHolder.mtv_desc.setText(data.str_surplus_stock);
                oneHolder.seekbar_grow.setVisibility(View.VISIBLE);
//                startDownTimer(data.percent/8,oneHolder.seekbar_grow);
                oneHolder.seekbar_grow.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
                oneHolder.mtv_number.setVisibility(View.INVISIBLE);
            } else {
                if ("1".equals(data.remind_status)) {
                    oneHolder.isRemind = true;
                    oneHolder.miv_clock.setVisibility(View.GONE);
                    oneHolder.mtv_quxiao.setText(getString(R.string.day_quxiaotixing));
                    copyBackground.setColor(getColor(R.color.color_value_6c));//设置填充色
                } else {
                    oneHolder.isRemind = false;
                    oneHolder.miv_clock.setVisibility(View.VISIBLE);
                    oneHolder.mtv_quxiao.setText(getString(R.string.day_tixinwo));
                    copyBackground.setColor(getColor(R.color.value_2096F2));//设置填充色
                }
                oneHolder.mtv_priceA.setTextColor(getColor(R.color.value_2096F2));
                oneHolder.seekbar_grow.setVisibility(View.GONE);
                if (!isEmpty(data.remind_count)&&Float.parseFloat(data.remind_count)>0){
                    oneHolder.mtv_number.setVisibility(View.VISIBLE);
                    oneHolder.mtv_number.setText(String.format(getString(R.string.day_yiyoutixing), data.remind_count));
                }
            }
            GlideUtils.getInstance().loadImage(context, oneHolder.miv_img, data.goods_pic);
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

        @BindView(R.id.mtv_titles)
        MyTextView mtv_titles;

        @BindView(R.id.mtv_content)
        MyTextView mtv_content;

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.miv_clock)
        MyImageView miv_clock;

        @BindView(R.id.view_title)
        View view_title;

        @BindView(R.id.mrlayout_time)
        MyRelativeLayout mrlayout_time;

        @BindView(R.id.ddp_downTime)
        DDPDownTimerView ddp_downTime;

        @BindView(R.id.seekbar_grow)
        SeekBar seekbar_grow;

        private boolean isRemind = false, isStartDownTime = false;
        private String goods_id;
        private int position;

        OneHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mllayout_remind.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.mllayout_remind:
                    if ("1".equals(isStart)) {
//                        from//踩点
                        GoodsDetailAct.startAct(context, goods_id);
                    } else if (isRemind) {
                        dayDayPresenter.cancleRemind(id, goods_id, position);
                    } else {
                        dayDayPresenter.settingRemind(id, goods_id, position);
                    }
                    break;
                default:
                    if (listener != null) {
                        listener.onItemClick(view, getAdapterPosition());
                    }
                    break;
            }
        }
    }

}
