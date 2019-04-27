package com.shunlian.app.demo;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.timer.DDPDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/3/27.
 */

public class DayListAdapterDemo extends BaseRecyclerAdapter {
    public String isStart, title, content, time, from, id;
    private DayDaypresenterDemo dayDayPresenter;

    public DayListAdapterDemo(Context context, boolean isShowFooter, List<ActivityListEntity.MData.Good.MList> datas,
                              DayDaypresenterDemo dayDayPresenter, ActivityListEntity activityListEntity) {
        super(context, isShowFooter, datas);
        isStart = activityListEntity.datas.sale;
        this.dayDayPresenter = dayDayPresenter;
        title = activityListEntity.datas.title;
        content = activityListEntity.datas.content;
        time = activityListEntity.datas.time;
        from = activityListEntity.from;

    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new OneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_list, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OneHolder) {
            OneHolder holder1 = (OneHolder) holder;
            ActivityListEntity.MData.Good.MList mList = (ActivityListEntity.MData.Good.MList) lists.get(position);
            holder1.mtv_title.setText(mList.title);
            holder1.mtv_priceM.setEarnMoney(mList.self_buy_earn, 14);
            SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.common_yuan) + mList.act_price, getString(R.string.common_yuan), 12);
            holder1.mtv_priceA.setText(spannableStringBuilder);
            holder1.goods_id = mList.goods_id;
            holder1.position = position;
            if (position == 0) {
                holder1.mrlayout_time.setVisibility(View.VISIBLE);
                holder1.view_title.setVisibility(View.VISIBLE);
                holder1.mtv_titles.setText(title);
                holder1.mtv_content.setText(content);

                if (!holder1.isStartDownTime) {
                    String times = isEmpty(time) ? "0" : time;
                    holder1.ddp_downTime.setDownTime(Integer.parseInt(times));
                    holder1.ddp_downTime.startDownTimer();
                    holder1.isStartDownTime = true;
                    holder1.ddp_downTime.setDownTimerListener(new OnCountDownTimerListener() {
                        @Override
                        public void onFinish() {
                            if (holder1 != null) {
                                holder1.isStartDownTime = false;
                                if (context instanceof DemoAct) {
                                    DemoAct content = (DemoAct) context;
                                    if (content.isFinishing() && holder1 != null && holder1.ddp_downTime != null) {
                                        holder1.ddp_downTime.cancelDownTimer();
                                        return;
                                    }
                                    content.minitData();
                                }

                            }
                        }
                    });
                }

            } else {
                holder1.mrlayout_time.setVisibility(View.GONE);
                holder1.view_title.setVisibility(View.GONE);
            }
            GradientDrawable background = (GradientDrawable) holder1.mllayout_remind.getBackground();
            if ("1".equals(isStart)) {
                //设置圆角背景
                holder1.miv_clock.setVisibility(View.GONE);
                holder1.mtv_quxiao.setText(R.string.day_lijiqianggou);
                background.setColor(getColor(R.color.pink_color));//设置填充色
                holder1.mtv_priceA.setTextColor(getColor(R.color.new_text));
                holder1.mtv_number.setText(mList.sales_desc);
                holder1.mtv_number.setVisibility(View.VISIBLE);

            } else {
                if ("1".equals(mList.remind_status)) {
                    holder1.isRemind = true;
                    holder1.miv_clock.setVisibility(View.GONE);
                    holder1.mtv_quxiao.setText(getString(R.string.day_quxiaotixing));
                    background.setColor(getColor(R.color.color_value_6c));//设置填充色
                }else {
                    holder1.isRemind = false;
                    holder1.miv_clock.setVisibility(View.VISIBLE);
                    holder1.mtv_quxiao.setText(getString(R.string.day_tixinwo));
                    background.setColor(getColor(R.color.value_2096F2));//设置填充色
                }
                holder1.mtv_priceA.setTextColor(getColor(R.color.value_2096F2));
                holder1.mtv_number.setVisibility(View.INVISIBLE);
            }

            GlideUtils.getInstance().loadImage(context, holder1.miv_img, mList.goods_pic);

        }

    }

    public class OneHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
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

//        @BindView(R.id.mtv_desc)
//        MyTextView mtv_desc;

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

//        @BindView(R.id.mrlayout_progress)
//        MyRelativeLayout mrlayout_progress;

        @BindView(R.id.ddp_downTime)
        DDPDownTimerView ddp_downTime;

//        @BindView(R.id.seekbar_grow)
//        SeekBar seekbar_grow;

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
