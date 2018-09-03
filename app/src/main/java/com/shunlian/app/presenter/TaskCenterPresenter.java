package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.TaskListAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.TaskHomeEntity;
import com.shunlian.app.bean.TaskListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ITaskCenterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/8/31.
 */

public class TaskCenterPresenter extends BasePresenter<ITaskCenterView> {

    private List<TaskListEntity.ItemTask> taskLists = new ArrayList<>();

    public static final int NEW_USER_TASK = 1;//新手任务
    public static final int DAILY_TASK = 2;//日常任务

    public int current_task_state = NEW_USER_TASK;//当前任务状态  默认新手任务
    private TaskListAdapter taskListAdapter;

    public TaskCenterPresenter(Context context, ITaskCenterView iView) {
        super(context, iView);
        initApi();
        getTaskList();
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<TaskHomeEntity>> baseEntityCall = getApiService().taskHome(map);
        getNetData(true,baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<TaskHomeEntity>>(){
            @Override
            public void onSuccess(BaseEntity<TaskHomeEntity> entity) {
                super.onSuccess(entity);
                TaskHomeEntity data = entity.data;
                iView.setGoldEggsCount(data.gold_egg);
                iView.obtainDownTime(data.gold_egg_second,data.gold_egg_total_second);
                iView.setSignContinueNum(data.sign_continue_num);
                iView.setPic(data.ad_pic_url,data.ad_url);
                iView.setTip(data.faq_url,data.rule_url);
                iView.setSignData(data.sign_days);
            }
        });
    }

    /**
     * 获取任务列表
     */
    public void getTaskList(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<TaskListEntity>>
                baseEntityCall = getApiService().taskList(map);
        getNetData(false,baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<TaskListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<TaskListEntity> entity) {
                super.onSuccess(entity);

                TaskListEntity data = entity.data;
                if (isEmpty(data.new_user_tasks)){
                    current_task_state = DAILY_TASK;
                    iView.closeNewUserList();
                }

                taskLists.clear();
                if (current_task_state == NEW_USER_TASK){
                    taskLists.addAll(data.new_user_tasks);
                }else {
                    taskLists.addAll(data.daily_tasks);
                }

                creatAdapter();
            }
        });
    }

    private void creatAdapter() {
        if (taskListAdapter == null) {
            taskListAdapter = new TaskListAdapter(context, taskLists);
            iView.setAdapter(taskListAdapter);
        }else {
            taskListAdapter.notifyDataSetChanged();
        }
    }
}
