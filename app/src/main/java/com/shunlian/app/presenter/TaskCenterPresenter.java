package com.shunlian.app.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.NewTaskListAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.DayGiveEggEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.bean.NewEggDetailEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.SignEggEntity;
import com.shunlian.app.bean.TaskHomeEntity;
import com.shunlian.app.bean.TaskListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.LuckWheelPanActivity;
import com.shunlian.app.ui.coupon.CouponListAct;
import com.shunlian.app.ui.new3_login.EditInviteCodeDialog;
import com.shunlian.app.ui.new3_login.New3LoginInfoTipEntity;
import com.shunlian.app.ui.new3_login.VerifyPicDialog;
import com.shunlian.app.ui.new_user.NewUserPageActivity;
import com.shunlian.app.ui.setting.PersonalDataAct;
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
//    private TaskListAdapter taskListAdapter;
    private NewTaskListAdapter taskListAdapter;
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
    private List<NewEggDetailEntity.In> mDatas = new ArrayList<>();
    private EditInviteCodeDialog mInviteCodeDialog;
    private VerifyPicDialog mVerifyPicDialog;

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
        initApis();
    }


    public void initApis() {
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
                        iView.setPic(data.ad_pic_url, data.ad_url,data.ad_roll);
                        iView.setTip(data.faq_url, data.rule_url);
                        iView.setSignData(data.sign_days,data.sign_continue_num);
                        iView.setMid(data.get_gold_egg);
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

    public void refreshBaby() {
        if (!pageIsLoading && currentPage <= allPage) {
            pageIsLoading = true;
            getEggDetail();
        }
    }

    public void setRemind() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().setRemind(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
            }
        });

    }

    public void everyDayGiveEgg() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<DayGiveEggEntity>> baseEntityCall = getApiService().everyDayGiveEgg(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<DayGiveEggEntity>>() {
            @Override
            public void onSuccess(BaseEntity<DayGiveEggEntity> entity) {
                super.onSuccess(entity);
                DayGiveEggEntity data = entity.data;
                iView.dayGiveEgg(data);
            }
        });

    }

    public void getEggDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", "10");
        sortAndMD5(map);

        Call<BaseEntity<NewEggDetailEntity>> baseEntityCall = getApiService().eggDetail2(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<NewEggDetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<NewEggDetailEntity> entity) {
                super.onSuccess(entity);
                NewEggDetailEntity data = entity.data;
                pageIsLoading = false;
                currentPage++;
                allPage = Integer.parseInt(data.total);
                allPage = Integer.parseInt(data.total);
                mDatas.addAll(data.list);
                iView.getEggDetail(allPage,currentPage,mDatas);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
                pageIsLoading=false;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
                pageIsLoading=false;
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
//            taskListAdapter = new TaskListAdapter(context, taskLists);
            taskListAdapter = new NewTaskListAdapter(context, taskLists);
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
            switch (NewTaskListAdapter.TASK_TYPE.valueOf(code)) {
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
                case newer_download_app://邀请码得金蛋
                    TaskListEntity.ItemTask itemTask1 = taskLists.get(position);
                    TaskListEntity.Url url1 = itemTask1.ad_url;
                    if (url1 != null){
                        Common.goGoGo(context,url1.type,url1.item_id);
                    }
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
            switch (NewTaskListAdapter.TASK_TYPE.valueOf(code)) {
                case task_new_user_gift://注册猜红包
                    getPrizeByRegister();
                    break;
                case task_new_user_invite://邀请码得金蛋
                    //getGoldByCode();
                    checkBindShareidV2();
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
                case new_area_orders://新人专区下单获金蛋
                    NewUserPageActivity.startAct(context);
                    break;
                case fill_personal_data://完善个人信息得金蛋
                    PersonalDataAct.startAct(context);
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


    public void checkBindShareidV2(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>>
                baseEntityCall = getApiService().checkBindShareidV2(map);

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){

            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null && "2".equals(data.share_status)){//需要绑定上级
                    isCanBindShareID();
                }
            }
        });
    }

    /**
     * 是否需要绑定上级
     */
    private void isCanBindShareID() {
        //share_status == 2 表示该用户没有上级，需要绑定上级才能购买
        loginInfoTip();
        mInviteCodeDialog = new EditInviteCodeDialog((Activity) context);
        mInviteCodeDialog.setOnClickListener(v -> {
            mInviteCodeDialog.release();
        }, v -> {
            String inviteCode = mInviteCodeDialog.getInviteCode();
            if (isEmpty(inviteCode)) {
                Common.staticToast("请填写邀请码");
            } else {
                codeDetail(inviteCode);
            }
        });
        mInviteCodeDialog.show();
    }

    /**
     * 邀请码详情
     * @param id
     */
    public void codeDetail(String id){
        Map<String,String> map = new HashMap<>();
        map.put("code",id);
        sortAndMD5(map);

        Call<BaseEntity<MemberCodeListEntity>>
                baseEntityCall = getApiService().codeInfo(map);

        getNetData(true,baseEntityCall,new SimpleNetDataCallback
                <BaseEntity<MemberCodeListEntity>>(){

            @Override
            public void onSuccess(BaseEntity<MemberCodeListEntity> entity) {
                super.onSuccess(entity);

                if (mInviteCodeDialog != null)mInviteCodeDialog.dismiss();


                MemberCodeListEntity bean = entity.data;
                if (bean != null) {
                    mVerifyPicDialog = new VerifyPicDialog((Activity) context);
                    mVerifyPicDialog.setTvSureColor(R.color.pink_color);
                    mVerifyPicDialog.setTvSureBgColor(Color.WHITE);
                    mVerifyPicDialog.setMessage("请确认您的导购专员");
                    mVerifyPicDialog.showState(2);
                    mVerifyPicDialog.setMemberDetail(bean.info);
                    mVerifyPicDialog.setSureAndCancleListener("确认", v -> {
                        bindShareid(bean.info.code);
                        mVerifyPicDialog.dismiss();
                    }, "返回", v -> {
                        mVerifyPicDialog.dismiss();
                        if (mInviteCodeDialog != null)mInviteCodeDialog.show();
                    }).show();
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
            }
        });
    }

    /**
     * 登录界面提示信息
     */
    public void loginInfoTip(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<New3LoginInfoTipEntity>> baseEntityCall = getApiService().loginInfoTip(map);
        getNetData(false,baseEntityCall,new
                SimpleNetDataCallback<BaseEntity<New3LoginInfoTipEntity>>(){
                    @Override
                    public void onSuccess(BaseEntity<New3LoginInfoTipEntity> entity) {
                        super.onSuccess(entity);
                        if (entity.data != null && mInviteCodeDialog != null){
                            mInviteCodeDialog.setStrategyUrl(entity.data.incite_code_url);
                        }
                    }
                });
    }

    /**
     * 绑定上级
     * @param code
     */
    public void bindShareid(String code){
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>>
                baseEntityCall = getAddCookieApiService().bindShareidV2(getRequestBody(map));

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                Common.staticToasts(context,"已确认",R.mipmap.icon_common_duihao);
                if (mVerifyPicDialog != null){
                    mVerifyPicDialog.release();
                }
                if (mInviteCodeDialog != null){
                    mInviteCodeDialog.release();
                }
                if (current_task_state == NEW_USER_TASK)
                    updateItem(getUpdatePosition(),"1");
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
            }
        });
    }
}
