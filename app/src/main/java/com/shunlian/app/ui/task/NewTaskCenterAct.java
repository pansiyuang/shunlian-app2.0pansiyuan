package com.shunlian.app.ui.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.SignEggEntity;
import com.shunlian.app.bean.TaskHomeEntity;
import com.shunlian.app.presenter.TaskCenterPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ITaskCenterView;
import com.shunlian.app.widget.banner.MyKanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2019/1/5.
 */

public class NewTaskCenterAct extends BaseActivity implements ITaskCenterView {

    @BindView(R.id.recy_view)
    RecyclerView recyView;

    @BindView(R.id.kanner)
    MyKanner kanner;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    private TaskCenterPresenter mPresenter;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, NewTaskCenterAct.class);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_new_task_center;
    }

    @Override
    protected void initListener() {
        super.initListener();
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                visible(customView.findViewById(R.id.view_line));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                customView.findViewById(R.id.view_line).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        immersionBar.statusBarView(R.id.view_state).init();
        mPresenter = new TaskCenterPresenter(this, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        recyView.setNestedScrollingEnabled(false);


        for (int i = 0; i < 2; i++) {
            TabLayout.Tab tab = tab_layout.newTab();
            tab.setCustomView(getTabView());
            tab_layout.addTab(tab);
        }
    }

    private View getTabView() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);


        ImageView imageView = new ImageView(this);
        layout.addView(imageView);
        imageView.setImageResource(R.mipmap.img_share_logo);
        imageView.setId(R.id.iv_task);

        View view = new View(this);
        layout.addView(view);
        LinearLayout.LayoutParams viewParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        int i = TransformUtil.dip2px(this, 2);
        viewParams.width = i*32;
        viewParams.height = i;
        viewParams.topMargin = i*4;
        view.setLayoutParams(viewParams);
        view.setBackgroundColor(Color.RED);
        view.setId(R.id.view_line);
        view.setVisibility(View.INVISIBLE);
        return layout;
    }


    /**
     * 金蛋数量
     *
     * @param count
     */
    @Override
    public void setGoldEggsCount(String count) {

    }

    /**
     * ；连续签到天数
     *
     * @param num
     */
    @Override
    public void setSignContinueNum(String num) {

    }

    /**
     * @param second      倒计时秒数
     * @param maxProgress 最大进度
     * @param task_status
     */
    @Override
    public void obtainDownTime(String second, String maxProgress, String task_status) {

    }

    /**
     * 广告图
     *
     * @param url
     * @param urlBean
     * @param adUrlRollBean
     */
    @Override
    public void setPic(String url, TaskHomeEntity.AdUrlBean urlBean,
                       List<TaskHomeEntity.AdUrlRollBean> adUrlRollBean) {
        if(adUrlRollBean!=null&&adUrlRollBean.size()>0){
            visible(kanner);
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < adUrlRollBean.size(); i++) {
                strings.add(adUrlRollBean.get(i).ad_pic_url);
                if (i >= adUrlRollBean.size() - 1) {
                    kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                    kanner.setBanner(strings);
                    kanner.setOnItemClickL(position ->
                            Common.goGoGo(baseAct, adUrlRollBean.get(position).ad_url.type,
                                    adUrlRollBean.get(position).ad_url.item_id));
                }
            }
        }else{
            gone(kanner);
        }
    }

    /**
     * @param question 常见问题
     * @param rule     签到规则
     */
    @Override
    public void setTip(String question, String rule) {

    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        if (recyView != null) {
            recyView.setAdapter(adapter);
        }
    }

    /**
     * 关闭新手任务列表
     *
     * @param isClose
     */
    @Override
    public void closeNewUserList(boolean isClose) {

    }

    /**
     * 签到
     *
     * @param list
     * @param sign_continue_num
     */
    @Override
    public void setSignData(List<TaskHomeEntity.SignDaysBean> list, String sign_continue_num) {

    }

    /**
     * 签到成功
     *
     * @param signEggEntity
     */
    @Override
    public void signEgg(SignEggEntity signEggEntity) {

    }

    /**
     * 限时领金蛋弹窗
     *
     * @param got_eggs
     */
    @Override
    public void showGoldEggsNum(String got_eggs) {

    }

    /**
     * 广告弹窗
     *
     * @param url
     * @param pop_ad_url
     */
    @Override
    public void popAd(String url, TaskHomeEntity.AdUrlBean pop_ad_url) {

    }
}
