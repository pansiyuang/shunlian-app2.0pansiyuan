package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CoreNewEntity;
import com.shunlian.app.bean.StoreCategoriesEntity;
import com.shunlian.app.bean.TeamListEntity;
import com.shunlian.app.ui.store.StoreSearchAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class TeamHistoryAdapter extends BaseRecyclerAdapter<TeamListEntity.TeamUserData> {
    public static final int ITEM_HEAD = 4;
    private static final int TYPE3 = 3;//单个
    private static final int TYPE2 = 2;//多个

    public TeamHistoryAdapter(Context context, boolean isShowFooter,
                              List<TeamListEntity.TeamUserData> lists) {
        super(context, isShowFooter, lists);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE2:
                return new TeamEmptyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_join_empty, parent, false));
            case TYPE3:
                return new TeamListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_join, parent, false));
            case ITEM_HEAD:
                return new TeamHeadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_join_head, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_team_join_head, parent, false);
        return new TeamHeadHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        try {
            switch (itemViewType) {
                case TYPE2:
                    if (holder instanceof TeamEmptyHolder) {
                        TeamEmptyHolder teamEmptyHolder = (TeamEmptyHolder) holder;
                        teamEmptyHolder.tv_team_time.setText(lists.get(position).text);
                        teamEmptyHolder.tv_empty_text.setText(lists.get(position).text3);
                        teamEmptyHolder.tv_empty_text2.setText(lists.get(position).text2);
                    }
                    break;
                case TYPE3:
                    if (holder instanceof TeamListHolder) {
                        TeamListHolder teamListHolder = (TeamListHolder) holder;
                        teamListHolder.tv_team_text.setText(lists.get(position).text3);
                        teamListHolder.tv_team_time.setText(lists.get(position).text);
                        teamListHolder.recy_team_view.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                        teamListHolder.recy_team_view.setNestedScrollingEnabled(false);
                        teamListHolder.recy_team_view.setFocusableInTouchMode(false); //设置不需要焦点
                        teamListHolder.recy_team_view.requestFocus(); //设置焦点不需要
                        teamListHolder.simpleRecyclerAdapter = new SimpleRecyclerAdapter<TeamListEntity.UserData>(context,
                                R.layout.item_team_join_user,lists.get(position).data) {
                            @Override
                            public void convert(SimpleViewHolder holder, TeamListEntity.UserData userData, int position) {
                                ImageView img_level_state = holder.getView(R.id.img_level_state);
                                TextView tv_level_state = holder.getView(R.id.tv_level_state);
                                ImageView img_level_head = holder.getView(R.id.img_level_head);
                                TextView tv_level_nick = holder.getView(R.id.tv_level_nick);
                                TextView tv_is_leader = holder.getView(R.id.tv_is_leader);
                                TextView tv_team_num = holder.getView(R.id.tv_team_num);
                                TextView tv_team_egg_num = holder.getView(R.id.tv_team_egg_num);
                                if(position==0){
                                    img_level_state.setVisibility(View.VISIBLE);
                                    tv_level_state.setVisibility(View.GONE);
                                    tv_is_leader.setVisibility(View.VISIBLE);
                                    img_level_state.setImageResource(R.mipmap.ic_guafenjindan_canyujilv_one);
                                }else if(position==1){
                                    img_level_state.setVisibility(View.VISIBLE);
                                    tv_level_state.setVisibility(View.GONE);
                                    tv_is_leader.setVisibility(View.GONE);
                                    img_level_state.setImageResource(R.mipmap.ic_guafenjindan_canyujilv_two);
                                }else if(position==2){
                                    img_level_state.setVisibility(View.VISIBLE);
                                    tv_level_state.setVisibility(View.GONE);
                                    tv_is_leader.setVisibility(View.GONE);
                                    img_level_state.setImageResource(R.mipmap.ic_guafenjindan_canyujilv_three);
                                }else{
                                    img_level_state.setVisibility(View.GONE);
                                    tv_is_leader.setVisibility(View.GONE);
                                    tv_level_state.setVisibility(View.VISIBLE);
                                    tv_level_state.setText((position+1)+"");
                                }
                                GlideUtils.getInstance().loadCircleAvar(context,img_level_head,userData.avatar);
                                tv_level_nick.setText(userData.nickname);
                                tv_team_num.setText(userData.join_num);
                                tv_team_egg_num.setText(userData.end_egg);
                            }
                        };
                        teamListHolder.recy_team_view.setAdapter(teamListHolder.simpleRecyclerAdapter);
                    }
                    break;
                case ITEM_HEAD:
                    if (holder instanceof TeamHeadHolder) {
                        TeamHeadHolder teamHeadHolder = (TeamHeadHolder) holder;
                        teamHeadHolder.tv_team_head_egg.setText(lists.get(position).head_1);
                        teamHeadHolder.tv_team_head_people.setText(lists.get(position).head_2);
                        teamHeadHolder.tv_team_head_leader.setText(lists.get(position).head_3);
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemViewType(int position) {
        try {
             if(position==0) {
                return ITEM_HEAD;
            }else if(lists.get(position).data==null||lists.get(position).data.size()==0) {
                return TYPE2;
            }else if(lists.get(position).data!=null&&lists.get(position).data.size()>0) {
                return TYPE3;
            }else{
               return super.getItemViewType(position);
            }
        }catch (Exception e){
            return super.getItemViewType(position);
        }
    }

    public class TeamHeadHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_team_head_egg)
        TextView tv_team_head_egg;

        @BindView(R.id.tv_team_head_people)
        TextView tv_team_head_people;

        @BindView(R.id.tv_team_head_leader)
        TextView tv_team_head_leader;

        @BindView(R.id.tv_team_total_num)
        TextView tv_team_total_num;

        public TeamHeadHolder(View itemView) {
            super(itemView);
        }
        @Override
        public void onClick(View v) {
        }
    }

    public class TeamListHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_team_time)
        TextView tv_team_time;

        @BindView(R.id.tv_team_who)
        TextView tv_team_who;

        @BindView(R.id.tv_team_text)
        TextView tv_team_text;

        @BindView(R.id.recy_team_view)
        RecyclerView recy_team_view;

        private SimpleRecyclerAdapter<TeamListEntity.UserData> simpleRecyclerAdapter;
        public TeamListHolder(View itemView) {
            super(itemView);
        }
        @Override
        public void onClick(View v) {
        }
    }

    public class TeamEmptyHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_team_time)
        TextView tv_team_time;

        @BindView(R.id.tv_empty_text)
        TextView tv_empty_text;

        @BindView(R.id.tv_empty_text2)
        TextView tv_empty_text2;

        public TeamEmptyHolder(View itemView) {
            super(itemView);
        }
        @Override
        public void onClick(View v) {
        }
    }
}
