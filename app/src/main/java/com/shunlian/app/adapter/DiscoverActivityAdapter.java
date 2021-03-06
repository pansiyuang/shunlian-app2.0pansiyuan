package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.DiscoverActivityEntity;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.HorizonItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/18.
 */

public class DiscoverActivityAdapter extends BaseRecyclerAdapter<DiscoverActivityEntity.Activity> {

    public DiscoverActivityAdapter(Context context, List<DiscoverActivityEntity.Activity> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ActivityViewHolder(LayoutInflater.from(context).inflate(R.layout.item_activity, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ActivityViewHolder) {
            ActivityViewHolder activityViewHolder = (ActivityViewHolder) holder;
            DiscoverActivityEntity.Activity activity = lists.get(position);
            GlideUtils.getInstance().loadImage(context, activityViewHolder.miv_icon, activity.thumb);
            activityViewHolder.tv_topic.setText(activity.title);

            if (isEmpty(activity.refer_member_num)) {
                activityViewHolder.tv_join.setText("0人在参与");
            } else {
                activityViewHolder.tv_join.setText(activity.refer_member_num + "人在参与");
                activityViewHolder.tv_join.setVisibility(View.VISIBLE);
            }

            if (!isEmpty(activity.members)) {
                List<String> pics = new ArrayList<>();
                for (DiscoverActivityEntity.Member member : activity.members) {
                    pics.add(member.avatar);
                }
                TieziAvarAdapter tieziAvarAdapter = new TieziAvarAdapter(context, pics, TransformUtil.dip2px(context, 20));
                activityViewHolder.recycler_list.setAdapter(tieziAvarAdapter);
                activityViewHolder.recycler_list.setVisibility(View.VISIBLE);
            } else {
                activityViewHolder.recycler_list.setVisibility(View.GONE);
            }

            int screenWidth = DeviceInfoUtil.getDeviceWidth(context) - TransformUtil.dip2px(context, 32);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) activityViewHolder.miv_icon.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
//            double height = screenWidth / 710d * 400;
            double height = screenWidth / 686d * 350;
            layoutParams.height = (int) Math.round(height);
            activityViewHolder.miv_icon.setLayoutParams(layoutParams);
        }
    }

    public class ActivityViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_topic)
        TextView tv_topic;

        @BindView(R.id.recycler_list)
        RecyclerView recycler_list;

        @BindView(R.id.tv_join)
        TextView tv_join;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recycler_list.addItemDecoration(new HorizonItemDecoration(TransformUtil.dip2px(context, -8)));
            recycler_list.setLayoutManager(linearLayoutManager);
        }
    }
}
