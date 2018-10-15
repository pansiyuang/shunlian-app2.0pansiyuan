package com.shunlian.app.presenter;

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.TaskListAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.SignEggEntity;
import com.shunlian.app.bean.TaskHomeEntity;
import com.shunlian.app.bean.TaskListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.LuckWheelPanActivity;
import com.shunlian.app.ui.coupon.CouponListAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.view.ITaskCenterView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.wxapi.WXEntryActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/8/31.
 */

public class TaskCenterPresenter extends BasePresenter<ITaskCenterView> {

    public static final int NEW_USER_TASK = 1;//新手任务
    public static final int DAILY_TASK = 2;//日常任务
    public int current_task_state = NEW_USER_TASK;//当前任务状态  默认新手任务
    private List<TaskListEntity.ItemTask> taskLists = new ArrayList<>();
    private List<TaskListEntity.ItemTask> newUserTaskLists;//新手任务列表
    private List<TaskListEntity.ItemTask> dailyTaskLists;//日常任务列表
    private TaskListAdapter taskListAdapter;
    private Call<BaseEntity<TaskHomeEntity>> homeCall;
    private Call<BaseEntity<TaskListEntity>> taskListCall;
    private Call<BaseEntity<SignEggEntity>> signEggsCall;
    private Call<BaseEntity<TaskHomeEntity>> goldegglimitCall;
    private Call<BaseEntity<TaskHomeEntity>> byCodeCall;
    private Call<BaseEntity<TaskHomeEntity>> videoCall;
    private Call<BaseEntity<CommonEntity>> getPrizeByRegisterCall;
    private String share_pic_url;
    private int updatePosition;
    private boolean isShowLoading;//是否显示加载动画

    public TaskCenterPresenter(Context context, ITaskCenterView iView) {
        super(context, iView);
        isShowLoading = true;
        initApi();
        getTaskList();
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {
        isShowLoading = false;
        initApi();
        getTaskList();
    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

        if (homeCall != null && homeCall.isExecuted()){
            homeCall.cancel();
            homeCall = null;
        }

        if (taskListCall != null && taskListCall.isExecuted()) {
            taskListCall.cancel();
            taskListCall = null;
        }

        if (byCodeCall != null && byCodeCall.isExecuted()){
            byCodeCall.cancel();
            byCodeCall = null;
        }

        if (signEggsCall != null && signEggsCall.isExecuted()){
            signEggsCall.cancel();
            signEggsCall = null;
        }

        if (goldegglimitCall != null && goldegglimitCall.isExecuted()){
            goldegglimitCall.cancel();
            goldegglimitCall = null;
        }

        if (getPrizeByRegisterCall != null && getPrizeByRegisterCall.isExecuted()){
            getPrizeByRegisterCall.cancel();
            getPrizeByRegisterCall = null;
        }

        if (taskLists != null){
            taskLists.clear();
            taskLists = null;
        }
        if (newUserTaskLists != null){
            newUserTaskLists.clear();
            newUserTaskLists = null;
        }
        if (dailyTaskLists != null){
            dailyTaskLists.clear();
            dailyTaskLists = null;
        }
        if (taskListAdapter != null){
            taskListAdapter.unbind();
            taskListAdapter = null;
        }
    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        homeCall = getApiService().taskHome(map);
        getNetData(isShowLoading, homeCall,
                new SimpleNetDataCallback<BaseEntity<TaskHomeEntity>>() {
                    @Override
                    public void onSuccess(BaseEntity<TaskHomeEntity> entity) {
                        super.onSuccess(entity);
                        TaskHomeEntity data = entity.data;
                        share_pic_url = data.share_pic_url;
                        iView.setGoldEggsCount(data.gold_egg);
                        iView.obtainDownTime(data.gold_egg_second, data.gold_egg_total_second,data.task_status);
                        iView.setSignContinueNum(data.sign_continue_num);
                        iView.setPic(data.ad_pic_url, data.ad_url);
                        iView.setTip(data.faq_url, data.rule_url);
                        iView.setSignData(data.sign_days);
                        iView.popAd(data.pop_ad_pic_url,data.pop_ad_url);
                        //LogUtil.zhLogW("TaskHomeEntity:>>>>"+data.toString());
                    }
                });
    }

    public void initDialogs(CommonEntity data) {
        Dialog dialog_new = new Dialog(context, R.style.popAd);
        dialog_new.setContentView(R.layout.dialog_new);
        MyImageView miv_close = dialog_new.findViewById(R.id.miv_close);
        NewTextView ntv_aOne = dialog_new.findViewById(R.id.ntv_aOne);
        NewTextView ntv_get = dialog_new.findViewById(R.id.ntv_get);
        NewTextView ntv_check = dialog_new.findViewById(R.id.ntv_check);
        NewTextView ntv_use = dialog_new.findViewById(R.id.ntv_use);
        MyLinearLayout mllayout_before = dialog_new.findViewById(R.id.mllayout_before);
        MyLinearLayout mllayout_after = dialog_new.findViewById(R.id.mllayout_after);
        miv_close.setOnClickListener(view -> dialog_new.dismiss());
        mllayout_before.setVisibility(View.GONE);
        mllayout_after.setVisibility(View.VISIBLE);
        SpannableStringBuilder spannableStringBuilders = Common.changeTextSize(data.prize+getStringResouce(R.string.new_yuan) , getStringResouce(R.string.new_yuan), 24);
        ntv_aOne.setText(spannableStringBuilders);
        ntv_get.setVisibility(View.GONE);
        ntv_use.setVisibility(View.VISIBLE);
        ntv_check.setVisibility(View.VISIBLE);
        ntv_use.setOnClickListener(view -> {
            Common.goGoGo(context,data.type,data.item_id);
            dialog_new.dismiss();
        });
        ntv_check.setOnClickListener(view -> {
            CouponListAct.startAct(context);
            dialog_new.dismiss();
        });
        dialog_new.setCancelable(false);
        dialog_new.show();
    }

     private void getPrizeByRegister() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

         getPrizeByRegisterCall = getApiService().getPrizeByRegister(map);
        getNetData(true, getPrizeByRegisterCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null && !isEmpty(data.prize) && Float.parseFloat(data.prize) > 0) {
                    initDialogs(data);
                    if (current_task_state==NEW_USER_TASK)
                    updateItem(0,"1");
                }
            }
        });
    }

    /**
     * 获取任务列表
     */
    public void getTaskList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        taskListCall = getApiService().taskList(map);
        getNetData(false, taskListCall,
                new SimpleNetDataCallback<BaseEntity<TaskListEntity>>() {
                    @Override
                    public void onSuccess(BaseEntity<TaskListEntity> entity) {
                        super.onSuccess(entity);

                        TaskListEntity data = entity.data;
                        if (isEmpty(data.new_user_tasks)) {
                            current_task_state = DAILY_TASK;
                            iView.closeNewUserList(true);
                        }else {
                            iView.closeNewUserList(false);
                            if (newUserTaskLists == null)
                                newUserTaskLists = new ArrayList<>();

                            newUserTaskLists.clear();
                            newUserTaskLists.addAll(data.new_user_tasks);
                        }
                        if (dailyTaskLists == null)
                            dailyTaskLists = new ArrayList<>();
                        dailyTaskLists.clear();
                        dailyTaskLists.addAll(data.daily_tasks);

                        taskLists.clear();
                        if (current_task_state == NEW_USER_TASK) {
                            taskLists.addAll(newUserTaskLists);
                        } else {
                            taskLists.addAll(dailyTaskLists);
                        }

                        creatAdapter();
                    }
                });
    }

    public void cacheTaskList(){
        taskLists.clear();
        if (current_task_state == NEW_USER_TASK&&!isEmpty(newUserTaskLists)) {
            taskLists.addAll(newUserTaskLists);
        } else if (!isEmpty(dailyTaskLists)){
            taskLists.addAll(dailyTaskLists);
        }
        if (isEmpty(taskLists)){
            getTaskList();
        }
        creatAdapter();
    }

    private void creatAdapter() {
        if (taskListAdapter == null) {
            taskListAdapter = new TaskListAdapter(context, taskLists);
            iView.setAdapter(taskListAdapter);
            taskListAdapter.setOnItemClickListener((view, position) -> {
                TaskListEntity.ItemTask itemTask = taskLists.get(position);
                updatePosition = position;
                if (current_task_state == NEW_USER_TASK) {
                    handlerNewUserTask(position,itemTask.code);
                } else {
                    handlerDailyTask(position,itemTask.code);
                }
            });
        } else {
            taskListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 需要更新条目的位置
     * @return
     */
    public int getUpdatePosition() {
        return updatePosition;
    }

    public void updateItem(int position,String state){
        if (!isEmpty(taskLists) && taskListAdapter != null){
            if (position >= taskLists.size() || position < 0)return;
            TaskListEntity.ItemTask itemTask = taskLists.get(position);
            itemTask.task_status = state;
            taskListAdapter.notifyItemChanged(position,itemTask);
        }
    }

    /**
     * 处理日常任务
     *
     * @param position
     * @param code
     */
    private void handlerDailyTask(int position, String code) {
        try {
            switch (TaskListAdapter.TASK_TYPE.valueOf(code)) {
                case task_daily_hour_gold://限时领金蛋
                    goldegglimit();
                    break;
                case task_daily_lottery://抽奖
                    LuckWheelPanActivity.startAct(context);
                    break;
                case task_daily_goods_view://逛商场捡金蛋（去首页）
                    Common.goGoGo(context, "home");
                    break;
                case task_daily_share://分享赚金蛋
                    TaskListEntity.ItemTask itemTask = taskLists.get(position);
                    TaskListEntity.Url url = itemTask.ad_url;
                    if (url != null){
                        Common.goGoGo(context,url.type,url.item_id);
                    }
                    break;
                case task_daily_show_income://晒收入赚金蛋
                    TaskListEntity.ItemTask task = taskLists.get(position);
                    share_pic_url = task.share_pic_url;
                    share();
                    break;
            }
        }catch (Exception e){

        }
    }
    /*
    晒单分享
     */
    public void share() {
        if (isEmpty(share_pic_url)){
            Common.staticToast("晒单活动已关闭，请下次再来");
        }else {
            ShareInfoParam shareInfoParam = new ShareInfoParam();
            shareInfoParam.photo = share_pic_url;
            WXEntryActivity.startAct(context, "shareFriend", shareInfoParam);
            Constant.SHARE_TYPE = "income";
        }
    }

    /**
     * 处理新手任务
     *
     * @param position
     * @param code
     */
    private void handlerNewUserTask(int position, String code) {
        try {
            switch (TaskListAdapter.TASK_TYPE.valueOf(code)) {
                case task_new_user_gift://注册猜红包
                    getPrizeByRegister();
                    break;
                case task_new_user_invite://邀请码得金蛋
                    getGoldByCode();
                    break;
                case task_new_user_video://看视频得金蛋
                    if (Common.isPlus()){//plus用户直接领取金蛋
                        getGoldByWatchVideo();
                    }else {//非plus观看视频
                        if (taskLists.get(position) != null && !isEmpty(taskLists.get(position).video_url)) {
                            Common.goGoGo(context, "url", taskLists.get(position).video_url);
                        }
                    }
                    break;
            }
        }catch (Exception e){

        }
    }

    /**
     * 签到
     * @param
     */
    public void signEgg(String data) {
        Map<String, String> map = new HashMap<>();
        map.put("date", data);
        sortAndMD5(map);
        signEggsCall = getApiService().signEgg(map);
        getNetData(signEggsCall, new SimpleNetDataCallback<BaseEntity<SignEggEntity>>() {
            @Override
            public void onSuccess(BaseEntity<SignEggEntity> entity) {
                super.onSuccess(entity);
                SignEggEntity signEggEntity = entity.data;
                iView.signEgg(signEggEntity);
            }

        });
    }

    /**
     * 限时领金蛋
     */
    public void goldegglimit(){
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        goldegglimitCall = getApiService().goldegglimit(map);
        getNetData(goldegglimitCall, new SimpleNetDataCallback<BaseEntity<TaskHomeEntity>>() {
            @Override
            public void onSuccess(BaseEntity<TaskHomeEntity> entity) {
                super.onSuccess(entity);
                TaskHomeEntity data = entity.data;
                iView.obtainDownTime(data.gold_egg_second,data.gold_egg_total_second,"1");
                iView.showGoldEggsNum(data.got_eggs);
                iView.setGoldEggsCount(data.account_eggs);
                if (current_task_state == DAILY_TASK)
                    updateItem(0,"1");
            }
        });
    }

    /**
     * 邀请码得金蛋
     */
    public void getGoldByCode(){
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        byCodeCall = getApiService().getGoldByCode(map);
        getNetData(byCodeCall, new SimpleNetDataCallback<BaseEntity<TaskHomeEntity>>() {
            @Override
            public void onSuccess(BaseEntity<TaskHomeEntity> entity) {
                super.onSuccess(entity);
                TaskHomeEntity data = entity.data;
                iView.showGoldEggsNum(data.got_eggs);
                iView.setGoldEggsCount(data.account_eggs);
                if (current_task_state == NEW_USER_TASK)
                    updateItem(getUpdatePosition(),"1");
            }
        });
    }


    /**
     * 看视频得金蛋
     */
    public void getGoldByWatchVideo(){
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        videoCall = getApiService().getGoldByWatchVideo(map);
        getNetData(videoCall, new SimpleNetDataCallback<BaseEntity<TaskHomeEntity>>() {
            @Override
            public void onSuccess(BaseEntity<TaskHomeEntity> entity) {
                super.onSuccess(entity);
                TaskHomeEntity data = entity.data;
                iView.showGoldEggsNum(data.got_eggs);
                iView.setGoldEggsCount(data.account_eggs);
                if (current_task_state == NEW_USER_TASK)
                    updateItem(getUpdatePosition(),"1");
            }
        });
    }

}
