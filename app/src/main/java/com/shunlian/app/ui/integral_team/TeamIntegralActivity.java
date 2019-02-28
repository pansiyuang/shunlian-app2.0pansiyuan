package com.shunlian.app.ui.integral_team;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.exoplayer2.C;
import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.TeamIndexEntity;
import com.shunlian.app.listener.ICallBackResult;
import com.shunlian.app.presenter.TeamIntegralPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TextLengthUtil;
import com.shunlian.app.utils.TextSwitcherAnimation;
import com.shunlian.app.utils.TimeUtil;
import com.shunlian.app.view.TeamIntegralView;
import com.shunlian.app.widget.AutoPollRecyclerView;
import com.shunlian.app.widget.MyImageView;
import com.zh.chartlibrary.common.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *新人专享页面
 */

public class TeamIntegralActivity extends BaseActivity implements TeamIntegralView ,View.OnClickListener {
    private int STATE_TYEPE  = 1;//1：我要当对长 2：邀请  3：满员未开奖 4：下场倒计时
    private String password;

    TeamIntegralPresenter teamIntegralPresenter;

    @BindView(R.id.recycler_list)
    AutoPollRecyclerView recycler_list;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.textSwitcher)//切换显示动画
    TextSwitcher textSwitcher;

    @BindView(R.id.miv_close)
    MyImageView miv_close;
    @BindView(R.id.tv_right_bar)
    TextView tv_right_bar;

    @BindView(R.id.rela_show_bg)
    RelativeLayout rela_show_bg;

    @BindView(R.id.tv_activit_induce)//活动介绍
    TextView tv_activit_induce;

    @BindView(R.id.tv_team_fill_num)//满员显示金蛋
    TextView tv_team_fill_num;

    @BindView(R.id.tv_team_desc)//金蛋介绍
    TextView tv_team_desc;

    @BindView(R.id.tv_team_time)//开奖时间
    TextView tv_team_time;
    @BindView(R.id.tv_team_num)//成为队长最高瓜分1280金蛋 和 小于两人的介绍
    TextView tv_team_num;

    @BindView(R.id.tv_look_history)//参队记录
    TextView tv_look_history;

    @BindView(R.id.line_submit)//邀请，当对长
     LinearLayout line_submit;
    @BindView(R.id.tv_submit_title)//邀请，当对长，满员，下场倒计时，
     TextView tv_submit_title;
    @BindView(R.id.tv_submit_desc)//1：活动结束下场倒计时 2：满员提示（开奖后可到金蛋明细中查看）
    TextView tv_submit_desc;

    @BindView(R.id.view)
     View view;

    LinearLayoutManager lineManage;
    SimpleRecyclerAdapter recyclerAdapter;

    private List<TeamIndexEntity.EggHistory> eggHistories;
    private List<TeamIndexEntity.TeamPlayer> teamPlayers;

    AutoPollRecyclerView.AutoPollAdapter adapter;
    private int defaultUserNum = 6;//默认用户数
    TeamIndexEntity teamIndexEntity;

    CommonDialogUtil commonDialogUtil;

    private  CountDownTimer timer;
    /**
     * 启动定时器
     * @param time
     */
    private void startDownTimer(int time){
        timer = new CountDownTimer(time*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(tv_submit_desc!=null)
                tv_submit_desc.setText(TimeUtil.timeFormHHmmss(millisUntilFinished));
            }
            @Override
            public void onFinish() {
                if(tv_submit_desc!=null)
                tv_submit_desc.setText("00:00:00");
                if(teamIntegralPresenter!=null){
                    for (int i=0;i<teamPlayers.size();i++){
                        teamPlayers.get(i).isUser  = false;
                    }
                    teamIntegralPresenter.getTeamIndex(false,password,null);
                }
            }
        }.start();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_integral_team;
    }
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        password = this.getIntent().getStringExtra("password");
        eggHistories = new ArrayList<>();
        teamPlayers  = new ArrayList<>();
        commonDialogUtil = new CommonDialogUtil(this);
        for (int i=0;i<defaultUserNum;i++){
            TeamIndexEntity.TeamPlayer teamPlayer = new TeamIndexEntity.TeamPlayer();
            teamPlayer.isUser  = false;
            teamPlayers.add(teamPlayer);
        }
        teamIntegralPresenter = new TeamIntegralPresenter(this,this);
        lineManage =   new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        recycler_list.setLayoutManager(lineManage);
        adapter = new AutoPollRecyclerView.AutoPollAdapter(eggHistories,recycler_list);
        recycler_list.setEnabled(false);
        recycler_list.setFocusableInTouchMode(false);
        recycler_list.setNestedScrollingEnabled(false);
        recycler_list.setAdapter(adapter);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        recycler_list.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(teamIndexEntity==null){
                    return;
                }
                 if(eggHistories.size()==3){
                     if(recycler_list.getLayoutManager().findViewByPosition(0)!=null
                             &&recycler_list.getLayoutManager().findViewByPosition(1)!=null
                             &&recycler_list.getLayoutManager().findViewByPosition(2)!=null) {
                         recycler_list.getLayoutManager().findViewByPosition(0).setAlpha(0.36f);
                         recycler_list.getLayoutManager().findViewByPosition(1).setAlpha(0.66f);
                         recycler_list.getLayoutManager().findViewByPosition(2).setAlpha(1.0f);
                     }
                }else if(eggHistories.size()==2){
                     if(recycler_list.getLayoutManager().findViewByPosition(0)!=null
                             &&recycler_list.getLayoutManager().findViewByPosition(1)!=null
                             &&recycler_list.getLayoutManager().findViewByPosition(2)!=null) {
                         recycler_list.getLayoutManager().findViewByPosition(0).setAlpha(0.0f);
                         recycler_list.getLayoutManager().findViewByPosition(1).setAlpha(0.66f);
                         recycler_list.getLayoutManager().findViewByPosition(2).setAlpha(1.0f);
                     }
                 }else if(eggHistories.size()==1){
                     if(recycler_list.getLayoutManager().findViewByPosition(0)!=null
                             &&recycler_list.getLayoutManager().findViewByPosition(1)!=null
                             &&recycler_list.getLayoutManager().findViewByPosition(2)!=null) {
                         recycler_list.getLayoutManager().findViewByPosition(0).setAlpha(0.0f);
                         recycler_list.getLayoutManager().findViewByPosition(1).setAlpha(0.0f);
                         recycler_list.getLayoutManager().findViewByPosition(2).setAlpha(1.0f);
                     }
                }else if(eggHistories.size()==0){
                     if(recycler_list.getLayoutManager().findViewByPosition(0)!=null
                             &&recycler_list.getLayoutManager().findViewByPosition(1)!=null
                             &&recycler_list.getLayoutManager().findViewByPosition(2)!=null) {
                         recycler_list.getLayoutManager().findViewByPosition(0).setAlpha(0.0f);
                         recycler_list.getLayoutManager().findViewByPosition(1).setAlpha(0.0f);
                         recycler_list.getLayoutManager().findViewByPosition(2).setAlpha(0.0f);
                     }
                 }
            }
        });
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView t = new TextView(TeamIntegralActivity.this);
                t.setHeight(DensityUtil.dip2px(TeamIntegralActivity.this,35));
                t.setGravity(Gravity.CENTER);
                t.setTextColor(getResources().getColor(R.color.color_ff5011));
                t.setTextSize(30);
                return t;
            }
        });
        textSwitcher.setText("0");
        miv_close.setOnClickListener(this);
        tv_activit_induce.setOnClickListener(this);
        tv_right_bar.setOnClickListener(this);
        tv_look_history.setOnClickListener(this);
        recy_view.setLayoutManager(new GridLayoutManager(this,3));
        recyclerAdapter = new SimpleRecyclerAdapter<TeamIndexEntity.TeamPlayer>(this, R.layout.item_team_user, teamPlayers) {
            @Override
            public void convert(SimpleViewHolder holder, TeamIndexEntity.TeamPlayer teamPlayer, int position) {
                ImageView image_head = holder.getView(R.id.image_head);
                TextView tv_user_num = holder.getView(R.id.tv_user_num);
                TextView tv_user_egg = holder.getView(R.id.tv_user_egg);
                TextView tv_team_title = holder.getView(R.id.tv_team_title);
                ImageView img_team_state = holder.getView(R.id.img_team_state);
                TextView tv_team_name  = holder.getView(R.id.tv_team_name);
                if(teamPlayer.isUser&&position==0){//显示对长
                    tv_team_title.setVisibility(View.VISIBLE);
                }else{
                    tv_team_title.setVisibility(View.GONE);
                }
                if(teamPlayer.isUser){//显示邀请和用户信息
                    GlideUtils.getInstance().loadCircleAvarRound(TeamIntegralActivity.this,image_head,teamPlayer.avatar);
                    img_team_state.setVisibility(View.GONE);
                    tv_team_name.setText(TextLengthUtil.handleText(teamPlayer.nickname,7));
                    tv_team_name.setTextColor(getResources().getColor(R.color.white));
                    if(!TextUtils.isEmpty(teamPlayer.egg_num)&&Integer.valueOf(teamPlayer.egg_num)>0){
                        tv_user_egg.setVisibility(View.VISIBLE);
                        tv_user_egg.setText("赚:"+teamPlayer.egg_num);
                    }else{
                        tv_user_egg.setVisibility(View.GONE);
                    }
                    if(!TextUtils.isEmpty(teamPlayer.join_num)&&Integer.valueOf(teamPlayer.join_num)>0){
                        tv_user_num.setVisibility(View.VISIBLE);
                        tv_user_num.setText("邀请"+teamPlayer.join_num+"人");
                    }else{
                        tv_user_num.setVisibility(View.GONE);
                    }
                }else{
                    image_head.setImageResource(R.mipmap.ic_guafenjindan_tianjia);
                    img_team_state.setVisibility(View.VISIBLE);
                    tv_team_name.setText("邀请好友");
                    tv_team_name.setTextColor(getResources().getColor(R.color.value_fed86c));
                    tv_user_num.setVisibility(View.GONE);
                    tv_user_egg.setVisibility(View.GONE);
                }
                holder.addOnClickListener(R.id.image_head);
            }
        };
        /**
         * 既然是动画，就会有时间，我们把动画执行时间变大一点来看一看效果
         */
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recy_view.setItemAnimator(defaultItemAnimator);
        recy_view.setNestedScrollingEnabled(false);
        recy_view.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener((view, position) -> {
            if(!TextUtils.isEmpty(teamIndexEntity.password)&&!teamPlayers.get(position).isUser&&STATE_TYEPE!=4) {
                startWeixinFriend();
            }else if(STATE_TYEPE==1&&!teamPlayers.get(position).isUser){
                Common.staticToastAct(this,"成为队长获得最高金蛋奖励！");
            }
        });

        if(!TextUtils.isEmpty(password)){
            teamIntegralPresenter.getTeamIndex(true,password,null);
        }else{
            teamIntegralPresenter.getTeamIndex(true,null,null);
        }
        tv_submit_title.setOnClickListener(this);
    }


    public static void startAct(Context context,String password) {
        Intent intent = new Intent(context, TeamIntegralActivity.class);
        intent.putExtra("password", password);
        context.startActivity(intent);
        if(context instanceof  TeamIntegralActivity) {
            ((TeamIntegralActivity) context).finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_submit_title:
                if (!Common.isAlreadyLogin()) {
                    Common.goGoGo(this, "login");
                    return;
                }
                if(STATE_TYEPE==1) {
                    teamIntegralPresenter.getTeamIndex(false, password, "1");
                }else if(STATE_TYEPE==2) {
                    startWeixinFriend();
                }
                break;
            case R.id.miv_close:
                 finish();
//                teamIndexEntity.pop_2.content ="金蛋奖池+1200金蛋";
//                teamIndexEntity.pop_2.content_egg ="1200";
//                teamIndexEntity.pop_2.text ="队员人数满6人啦！";
//                teamIndexEntity.pop_2.text2 ="恭喜！金蛋奖池额外增加888金蛋";
//                teamIndexEntity.pop_2.qcode_pic ="http://www.shunliandongli.com/static/download.png";
//                commonDialogUtil.teamFillCommonDialog(teamIndexEntity.pop_2);

//                  teamIndexEntity.pop_1.content ="金蛋奖池+20金蛋";
//                  teamIndexEntity.pop_1.content_egg ="20";
//                  teamIndexEntity.pop_1.text ="您被好友李白邀请了";
//                  teamIndexEntity.pop_1.nickname2 ="李白";
//                  teamIndexEntity.pop_1.avatar ="http://img.v2.shunliandongli.com/uploads/20180731/20180731182411341r.png_128x128.jpg";
//                  teamIndexEntity.pop_1.nickname="自己的昵称";
//                  commonDialogUtil.teamIntoCommonDialog(teamIndexEntity.pop_1);
                break;
            case R.id.tv_right_bar:
                if(teamIndexEntity!=null&&!TextUtils.isEmpty(teamIndexEntity.strategy_url))
                H5X5Act.startAct(this, teamIndexEntity.strategy_url, H5X5Act.MODE_SONIC);
                break;
            case R.id.tv_activit_induce:
                if(teamIndexEntity!=null&&!TextUtils.isEmpty(teamIndexEntity.rule_url))
                 commonDialogUtil.teamH5CommonDialog(teamIndexEntity.rule_url);
                break;
            case R.id.tv_look_history:
                if (!Common.isAlreadyLogin()) {
                     Common.goGoGo(this, "login");
                     return;
                  }
                    TeamHistoryActivity.startAct(this);
                break;
        }
    }



    private void startWeixinFriend(){
        commonDialogUtil.shareTeamWeixinCommonDialog(new ICallBackResult<String>() {
            @Override
            public void onTagClick(String data) {
                if(teamIndexEntity!=null&&!TextUtils.isEmpty(teamIndexEntity.password))
                commonDialogUtil.shareCopyCommonDialog(teamIndexEntity.password);
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }


    @Override
    protected void onDestroy() {
        if(timer!=null){
            timer.cancel();
        }
        if(eggHistories.size()>3&&recycler_list!=null){
            recycler_list.stop();
        }
        super.onDestroy();
    }

    @Override
    public void teamIndex(TeamIndexEntity teamIndexEntity) {
        this.teamIndexEntity = teamIndexEntity;
        if(!TextUtils.isEmpty(teamIndexEntity.button_type))
        STATE_TYEPE = Integer.valueOf(teamIndexEntity.button_type);

        if(!TextUtils.isEmpty(teamIndexEntity.text)){
            tv_team_desc.setVisibility(View.VISIBLE);
            tv_team_desc.setText(teamIndexEntity.text);
        }else{
            tv_team_desc.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(teamIndexEntity.text2)){
            tv_team_num.setVisibility(View.VISIBLE);
            tv_team_num.setText(teamIndexEntity.text2);
            tv_team_time.setVisibility(View.VISIBLE);
        }else{
            tv_team_num.setVisibility(View.GONE);
            tv_team_time.setVisibility(View.GONE);
        }

        if(STATE_TYEPE==3){
            rela_show_bg.setBackgroundResource(R.mipmap.bg_zuduiguafen_image);
            textSwitcher.setVisibility(View.GONE);
            tv_team_fill_num.setVisibility(View.VISIBLE);
            tv_submit_desc.setVisibility(View.VISIBLE);
            tv_team_fill_num.setText(teamIndexEntity.total_egg);
            tv_submit_title.setText("等待10:00开抢");
            tv_submit_desc.setText("开奖后可到金蛋明细中查看");
            tv_submit_desc.setTextSize(10);
        }else{
            tv_team_fill_num.setVisibility(View.GONE);
            rela_show_bg.setBackgroundResource(R.mipmap.bg_zuduiguafen_jidan_image);
            textSwitcher.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(teamIndexEntity.total_egg)){
                new TextSwitcherAnimation(textSwitcher,teamIndexEntity.total_egg).create();
            }

            if(STATE_TYEPE==1){
                tv_submit_title.setText("我要当队长");
                tv_submit_desc.setVisibility(View.GONE);
            }else if(STATE_TYEPE==2){
                tv_submit_title.setText("立即邀请");
                tv_submit_desc.setVisibility(View.GONE);
            }else if(STATE_TYEPE==4){
                tv_submit_desc.setVisibility(View.VISIBLE);
                tv_submit_title.setText("下场活动倒计时");
                tv_submit_desc.setTextSize(13);
                tv_submit_desc.setText(TimeUtil.timeFormHHmmss(Long.valueOf(teamIndexEntity.down_time)));
                if(Integer.valueOf(teamIndexEntity.down_time)>0) {
                    startDownTimer(Integer.valueOf(teamIndexEntity.down_time));
                }
            }
        }
        if(teamIndexEntity!=null&&teamIndexEntity.team_player!=null&&teamIndexEntity.team_player.size()>0){
            for (int i=0;i<teamIndexEntity.team_player.size();i++){
                if(i<defaultUserNum) {
                    TeamIndexEntity.TeamPlayer teamPlayer = teamPlayers.get(i);
                    teamPlayer.isUser = true;
                    teamPlayer.avatar = teamIndexEntity.team_player.get(i).avatar;
                    teamPlayer.nickname = teamIndexEntity.team_player.get(i).nickname;
                    teamPlayer.egg_num = teamIndexEntity.team_player.get(i).egg_num;
                    teamPlayer.join_num = teamIndexEntity.team_player.get(i).join_num;
                }
            }
            recyclerAdapter.notifyDataSetChanged();
        }
        if(teamIndexEntity!=null&&teamIndexEntity.egg_history!=null&&teamIndexEntity.egg_history.size()>0){
            eggHistories.clear();
            recycler_list.setVisibility(View.VISIBLE);
            eggHistories.addAll(teamIndexEntity.egg_history);
            adapter.notifyDataSetChanged();
        }
        recycler_list.scrollToPosition(0);
        if(eggHistories.size()>3) {
            recycler_list.start();
        }else{
            recycler_list.stop();
        }

        if(teamIndexEntity.pop_3!=null&&!TextUtils.isEmpty(teamIndexEntity.pop_3.text)){
            commonDialogUtil.teamIntoFillCommonDialog(teamIndexEntity.pop_3, new ICallBackResult<String>() {
                @Override
                public void onTagClick(String data) {
                    password = null;
                    teamIntegralPresenter.getTeamIndex(true,null,null);
                }
            });
        }else if(teamIndexEntity.pop_2!=null&&!TextUtils.isEmpty(teamIndexEntity.pop_2.qcode_pic)){
            commonDialogUtil.teamFillCommonDialog(teamIndexEntity.pop_2);
        }else if(teamIndexEntity.pop_1!=null&&!TextUtils.isEmpty(teamIndexEntity.pop_1.content)){
            commonDialogUtil.teamIntoCommonDialog(teamIndexEntity.pop_1);
        }
    }
}
