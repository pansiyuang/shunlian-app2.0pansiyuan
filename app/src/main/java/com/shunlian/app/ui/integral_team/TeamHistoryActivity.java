package com.shunlian.app.ui.integral_team;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MemberUserAdapter;
import com.shunlian.app.adapter.TeamHistoryAdapter;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.bean.TeamListEntity;
import com.shunlian.app.presenter.MemberPagePresenter;
import com.shunlian.app.presenter.TeamHistoryPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IMemberPageView;
import com.shunlian.app.view.ITeamPageView;
import com.shunlian.app.widget.EditTextImage;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *新人专享页面
 */

public class TeamHistoryActivity extends BaseActivity  implements ITeamPageView {
    private List<TeamListEntity.TeamUserData> lists;
    private TeamHistoryAdapter teamHistoryAdapter;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;
    NestedSlHeader header;

    LinearLayoutManager  manager;
    TeamHistoryPresenter teamHistoryPresenter;
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_team_history;
    }
    @Override
    protected void initData() {
        lists = new ArrayList<>();
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();

        teamHistoryPresenter = new TeamHistoryPresenter(this,this);
        teamHistoryAdapter = new TeamHistoryAdapter(this,false,lists);
        header = new NestedSlHeader(this);
        header.setBackgroundColor(getColorResouce(R.color.white));
        lay_refresh.setRefreshHeaderView(header);
        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        nei_empty.setImageResource(R.mipmap.img_bangzhu_sousuo).setText("暂无参与记录").setButtonText(null);
        recy_view.setAdapter(teamHistoryAdapter);

        nei_empty.setVisibility(View.GONE);
        recy_view.setVisibility(View.GONE);

        teamHistoryPresenter.teamListInfo(true);
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, TeamHistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            if (teamHistoryPresenter != null) {
                teamHistoryPresenter.refreshData();
            }
        });
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (teamHistoryPresenter != null) {
                            teamHistoryPresenter.onRefresh();
                        }
                    }
                }
            }
        });
        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void teamListInfo(List<TeamListEntity.TeamUserData> teamUserDataList, TeamListEntity listEntity,int currentPage) {
        lay_refresh.setRefreshing(false);
        TeamListEntity.TeamUserData teamUserData =  new TeamListEntity.TeamUserData();
        teamUserData.head_1 = listEntity.total_egg;
        teamUserData.head_2 = listEntity.join_num;
        teamUserData.head_3 = listEntity.caption_time;
        if(teamUserDataList!=null&&teamUserDataList.size()==0&&currentPage==1){
            lists.clear();
            lists.add(teamUserData);
            teamHistoryAdapter.notifyDataSetChanged();
            nei_empty.setVisibility(View.GONE);
            recy_view.setVisibility(View.VISIBLE);
        }else if(teamUserDataList!=null&&teamUserDataList.size()>0){
            nei_empty.setVisibility(View.GONE);
            recy_view.setVisibility(View.VISIBLE);
            if(currentPage==1){
                lists.clear();
            }
//            lists.add(0,teamUserData);
            teamUserDataList.add(0,teamUserData);
            lists.addAll(teamUserDataList);
            teamHistoryAdapter.notifyDataSetChanged();
        }else{
            if(currentPage==1){
                lists.clear();
            }
            lists.add(teamUserData);
            teamHistoryAdapter.notifyDataSetChanged();
            nei_empty.setVisibility(View.GONE);
            recy_view.setVisibility(View.VISIBLE);
        }
    }
}
