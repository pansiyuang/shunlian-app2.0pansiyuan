package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.timer.DayNoBlackDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class PinpaiAdapter extends BaseRecyclerAdapter<CorePingEntity.MData> {
    private int second=(int)(System.currentTimeMillis()/1000);
    public PinpaiAdapter(Context context, boolean isShowFooter,
                         List<CorePingEntity.MData> lists) {
        super(context, isShowFooter, lists);
    }

    /**
     * 设置baseFooterHolder  layoutparams
     *
     * @param baseFooterHolder
     */
    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setText(getString(R.string.no_more_goods));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ping_pai, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        CorePingEntity.MData data = lists.get(position);
        GlideUtils.getInstance().loadImage(context, mHolder.miv_photo, data.bg_img);
        GlideUtils.getInstance().loadImage(context, mHolder.miv_logo, data.logo);
        mHolder.mtv_title.setText(data.content);
        mHolder.mtv_desc.setText(data.slogan);
        int seconds=(int)(System.currentTimeMillis()/1000)-second;
        mHolder.downTime_firsts.cancelDownTimer();
        mHolder.downTime_firsts.setDownTime(Integer.parseInt(data.count_down)-seconds);
        mHolder.downTime_firsts.startDownTimer();
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.miv_logo)
        MyImageView miv_logo;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.downTime_firsts)
        DayNoBlackDownTimerView downTime_firsts;

        public ActivityMoreHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            downTime_firsts.setDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onFinish() {
                    if (downTime_firsts!=null)
                    downTime_firsts.cancelDownTimer();
                }

            });
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
