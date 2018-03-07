package com.shunlian.app.adapter.first_page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.shunlian.app.R;
import com.shunlian.app.bean.MainPageEntity;
import com.shunlian.app.ui.activity.DayDayAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.timer.DDPDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/1.
 */

public class OnePlusTwoLayoutAdapter extends DelegateAdapter.Adapter {

    private final LayoutHelper mHelper;
    private MainPageEntity.NewGoods mNewGoods;
    private MainPageEntity.DaySpecial mDaySpecial;
    private final Context mContext;

    public OnePlusTwoLayoutAdapter(MainPageEntity.NewGoods newGoods,
                                   MainPageEntity.DaySpecial daySpecial,
                                   Context context, LayoutHelper helper){
        mNewGoods = newGoods;
        mDaySpecial = daySpecial;
        mContext = context;
        mHelper = helper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.day_new_layout, parent, false);
        return new OnePlusTwoLayoutHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OnePlusTwoLayoutHolder mHolder = (OnePlusTwoLayoutHolder) holder;

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class OnePlusTwoLayoutHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_day)
        MyImageView miv_day;

        @BindView(R.id.miv_new)
        MyImageView miv_new;

        @BindView(R.id.ddp_downTime)
        DDPDownTimerView ddp_downTime;
        public OnePlusTwoLayoutHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ddp_downTime.setDownTime(3605);
            ddp_downTime.setDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onFinish() {
                    ddp_downTime.cancelDownTimer();
                }

            });
            ddp_downTime.startDownTimer();

            miv_day.setOnClickListener(this);
            miv_new.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.miv_day:
                    DayDayAct.startAct(mContext);
                    break;
                case R.id.miv_new:
                    StoreAct.startAct(mContext,mNewGoods.item_id);
                    break;
            }
        }
    }
}
