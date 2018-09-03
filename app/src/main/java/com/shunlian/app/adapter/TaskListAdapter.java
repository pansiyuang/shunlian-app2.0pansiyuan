package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.TaskListEntity;
import com.shunlian.app.presenter.TaskCenterPresenter;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/8/31.
 */

public class TaskListAdapter extends BaseRecyclerAdapter<TaskListEntity.ItemTask> {

    private int mTtaskState;

    public TaskListAdapter(Context context, List<TaskListEntity.ItemTask> lists, int current_task_state) {
        super(context, false, lists);
        mTtaskState = current_task_state;
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_task, parent, false);
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
        GlideUtils.getInstance().loadOverrideImage(context,mHolder.mivIcon,itemTask.icon_url,32,32);

        mHolder.mtvTaskName.setText(itemTask.title);
        mHolder.mtvTaskTip.setText(itemTask.info);
        mHolder.mtvEggsCount.setText(itemTask.gold_num);


        if ("0".equals(itemTask.task_status)){//0 未完成；1已完成
            if (position == 0 || (position == 1 && mTtaskState == TaskCenterPresenter.NEW_USER_TASK)) {
                visible(mHolder.mivObtainBg);
                mHolder.mtvObtainTip.setText("点击领取");
                mHolder.mtvObtainTip.setTextColor(getColor(R.color.white));
                mHolder.mtvObtainTip.setBackgroundDrawable(null);
            }else {
                mHolder.mtvObtainTip.setText("去完成");
                mHolder.mtvObtainTip.setTextColor(getColor(R.color.pink_color));
                mHolder.mtvObtainTip.setBackgroundDrawable(getNotCompleteDrawable());
                gone(mHolder.mivObtainBg);
            }
        }else {
            mHolder.mtvObtainTip.setText("已完成");
            mHolder.mtvObtainTip.setTextColor(getColor(R.color.white));
            mHolder.mtvObtainTip.setBackgroundDrawable(getCompleteDrawable());
            gone(mHolder.mivObtainBg);
        }
    }


    private GradientDrawable getCompleteDrawable(){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getColor(R.color.color_value_6c));
        drawable.setCornerRadius(TransformUtil.dip2px(context,10));
        int i = TransformUtil.dip2px(context, 20);
        drawable.setSize(i*3,i);
        return drawable;
    }

    private GradientDrawable getNotCompleteDrawable(){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getColor(R.color.white));
        drawable.setCornerRadius(TransformUtil.dip2px(context,10));
        int i = TransformUtil.dip2px(context, 20);
        drawable.setSize(i*3,i);
        drawable.setStroke(TransformUtil.dip2px(context,1),getColor(R.color.pink_color));
        return drawable;
    }

    public class TaskListHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView mivIcon;

        @BindView(R.id.mtv_task_name)
        MyTextView mtvTaskName;

        @BindView(R.id.mtv_task_tip)
        MyTextView mtvTaskTip;

        @BindView(R.id.miv_obtain_bg)
        MyImageView mivObtainBg;

        @BindView(R.id.mtv_obtain_tip)
        MyTextView mtvObtainTip;

        @BindView(R.id.flayout_obtain)
        FrameLayout flayoutObtain;

        @BindView(R.id.mtv_eggs_count)
        MyTextView mtvEggsCount;

        @BindView(R.id.llayout_right)
        LinearLayout llayout_right;

        public TaskListHolder(View itemView) {
            super(itemView);
            llayout_right.setOnClickListener(v -> {
                TaskListEntity.ItemTask itemTask = lists.get(getAdapterPosition());
                if (listener != null && "0".equals(itemTask.task_status))
                    listener.onItemClick(v,getAdapterPosition());
            });
        }
    }
}
