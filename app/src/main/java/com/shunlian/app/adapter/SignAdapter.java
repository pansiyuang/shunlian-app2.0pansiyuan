package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.bean.TaskHomeEntity;
import com.shunlian.app.ui.task.NewTaskCenterAct;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.NewTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class SignAdapter extends BaseRecyclerAdapter<TaskHomeEntity.SignDaysBean> {
    NewTaskCenterAct newTaskCenterAct;
    public MyImageView miv_denglong;
    public LottieAnimationView animation_view;

    public SignAdapter(Context context, boolean isShowFooter, List<TaskHomeEntity.SignDaysBean> lists) {
        super(context, isShowFooter, lists);
        newTaskCenterAct= (NewTaskCenterAct) context;
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sign, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        TaskHomeEntity.SignDaysBean ad = lists.get(position);
        mHolder.ntv_score.setText(ad.gold_num);
        mHolder.ntv_date.setText(ad.date);
       switch (ad.sign_status){
           case "0":
               miv_denglong=mHolder.miv_denglong;
               animation_view=mHolder.animation_view;
               newTaskCenterAct.setGoldEggsAnim("sign_before.json",miv_denglong,animation_view,true,"eggs/sign_two.png");
               newTaskCenterAct.ntv_sign.setText("签到" );
               newTaskCenterAct.ntv_sign.setClickable(true);
               break;
           case "1":
               mHolder.miv_denglong.setBackgroundResource(R.mipmap.img_task_yiqiandao);
               break;
           case "-2":
               mHolder.miv_denglong.setBackgroundResource(R.mipmap.img_task_weiqiandao);
               break;
           case "-1":
               mHolder.miv_denglong.setBackgroundResource(R.mipmap.img_task_weiqiandao);
               break;
       }
    }



    public class ActivityMoreHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.ntv_score)
        NewTextView ntv_score;
        @BindView(R.id.ntv_date)
        NewTextView ntv_date;
        @BindView(R.id.miv_denglong)
        MyImageView miv_denglong;
        @BindView(R.id.animation_view)
        LottieAnimationView animation_view;


        public ActivityMoreHolder(View itemView) {
            super(itemView);
        }

    }
}
