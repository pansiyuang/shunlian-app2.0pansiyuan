package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.TaskListEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/8/31.
 */

public class NewTaskListAdapter extends BaseRecyclerAdapter<TaskListEntity.ItemTask> {


    public NewTaskListAdapter(Context context, List<TaskListEntity.ItemTask> lists) {
        super(context, false, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_task_new, parent, false);
        return new TaskListHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        TaskListHolder mHolder = (TaskListHolder) holder;

        TaskListEntity.ItemTask itemTask = lists.get(position);
        GlideUtils.getInstance().loadOverrideImage(context,
                mHolder.mivIcon, itemTask.icon_url, 32, 32);

        mHolder.mtvTaskName.setText(itemTask.title);
        mHolder.mtvTaskTip.setText(itemTask.info);
        if (isEmpty(itemTask.gold_num)){
            gone(mHolder.mtvEggsCount,mHolder.llayout_eggs_count);
        }else {
            mHolder.mtvEggsCount.setText(itemTask.gold_num);
            visible(mHolder.mtvEggsCount,mHolder.llayout_eggs_count);
        }


        changeState(position, mHolder, itemTask);
    }

    private void changeState(int position, TaskListHolder mHolder, TaskListEntity.ItemTask itemTask) {
        try {
            if ("0".equals(itemTask.task_status)) {//0 未完成；1已完成
                /*if (TASK_TYPE.task_new_user_gift == TASK_TYPE.valueOf(itemTask.code)
                        || TASK_TYPE.task_new_user_invite == TASK_TYPE.valueOf(itemTask.code)
                         || TASK_TYPE.task_daily_hour_gold == TASK_TYPE.valueOf(itemTask.code)) {
                    mHolder.mtvObtainTip.setText("领取");
                    mHolder.mtvObtainTip.setTextColor(getColor(R.color.white));
                    mHolder.mtvObtainTip.setBackgroundDrawable(getBtnStatusDrawable(2));
                } else {

                }*/
                mHolder.mtvObtainTip.setText("去完成");
                mHolder.mtvObtainTip.setTextColor(getColor(R.color.pink_color));
                mHolder.mtvObtainTip.setBackgroundDrawable(getBtnStatusDrawable(1));
            } else {
                mHolder.mtvObtainTip.setText("已领取");
                mHolder.mtvObtainTip.setTextColor(getColor(R.color.white));
                mHolder.mtvObtainTip.setBackgroundDrawable(getBtnStatusDrawable(3));
            }
        } catch (IllegalArgumentException e) {

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (isEmpty(payloads)) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            TaskListEntity.ItemTask itemTask = (TaskListEntity.ItemTask) payloads.get(0);
            TaskListHolder mHolder = (TaskListHolder) holder;
            changeState(position, mHolder, itemTask);
        }
    }

    private GradientDrawable getCompleteDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getColor(R.color.color_value_6c));
        drawable.setCornerRadius(TransformUtil.dip2px(context, 10));
        int i = TransformUtil.dip2px(context, 20);
        drawable.setSize(i * 3, i);
        return drawable;
    }

    /**
     *
     * @param status 1 去完成  2 领取   3 已完成
     * @return
     */
    private GradientDrawable getBtnStatusDrawable(int status) {
        GradientDrawable drawable = new GradientDrawable();
        switch (status){
            case 1:
                drawable.setColor(getColor(R.color.white));
                drawable.setStroke(TransformUtil.dip2px(context, 1),
                        getColor(R.color.pink_color));
                break;

            case 2:
                drawable = null;
                drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                        new int[]{Color.parseColor("#FD6503"),
                                Color.parseColor("#FF2703")});
                break;

            case 3:
                drawable.setColor(getColor(R.color.color_value_6c));
                break;
        }

        drawable.setCornerRadius(TransformUtil.dip2px(context, 10));
//        int i = TransformUtil.dip2px(context, 20);
//        drawable.setSize(i * 3, i);
        return drawable;
    }

    public class TaskListHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView mivIcon;

        @BindView(R.id.mtv_task_name)
        MyTextView mtvTaskName;

        @BindView(R.id.mtv_task_tip)
        MyTextView mtvTaskTip;

        @BindView(R.id.mtv_obtain_tip)
        MyTextView mtvObtainTip;

        @BindView(R.id.mtv_eggs_count)
        MyTextView mtvEggsCount;

        @BindView(R.id.llayout_eggs_count)
        LinearLayout llayout_eggs_count;

        public TaskListHolder(View itemView) {
            super(itemView);
            mtvObtainTip.setOnClickListener(v -> {
                try {
                    TaskListEntity.ItemTask itemTask = lists.get(getAdapterPosition());
                    if (listener != null && "0".equals(itemTask.task_status))
                        listener.onItemClick(v, getAdapterPosition());
                }catch (Exception e){

                }
            });
            itemView.setOnClickListener(null);
        }
    }

    public enum TASK_TYPE {
        /***
         * 注册拆红包
         */
        task_new_user_gift,
        /**
         * 邀请码得金蛋
         */
        task_new_user_invite,
        /**
         * 看视频收金蛋
         */
        task_new_user_video,
        /**
         * 限时领金蛋
         */
        task_daily_hour_gold,
        /**
         * 周六金蛋抽奖
         */
        task_daily_lottery,
        /**
         * 逛商城捡金蛋
         */
        task_daily_goods_view,
        /**
         * //乐分享赚金蛋
         */
        task_daily_share,
        /**
         * 晒收入赚金蛋
         */
        task_daily_show_income,
        /**
         * 邀新用户下单获红包
         */
        newer_download_app
    }
}
