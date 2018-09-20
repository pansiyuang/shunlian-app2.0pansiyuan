package com.shunlian.app.view;

import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.SignEggEntity;
import com.shunlian.app.bean.TaskHomeEntity;

import java.util.List;

/**
 * Created by zhanghe on 2018/8/31.
 */

public interface ITaskCenterView extends IView {

    /**
     * 金蛋数量
     * @param count
     */
    void setGoldEggsCount(String count);

    /**
     * ；连续签到天数
     * @param num
     */
    void setSignContinueNum(String num);

    /**
     *
     * @param second 倒计时秒数
     * @param maxProgress 最大进度
     */
    void obtainDownTime(String second,String maxProgress,String task_status);

    /**
     * 广告图
     * @param url
     * @param urlBean
     */
    void setPic(String url, TaskHomeEntity.AdUrlBean urlBean);

    /**
     *
     * @param question 常见问题
     * @param rule 签到规则
     */
    void setTip(String question,String rule);

    void setAdapter(BaseRecyclerAdapter adapter);

    /**
     * 关闭新手任务列表
     */
    void closeNewUserList(boolean isClose);

    /**
     * 签到
     */
    void setSignData(List<TaskHomeEntity.SignDaysBean> list);

    /**
     * 签到成功
     * @param signEggEntity
     */
    void signEgg(SignEggEntity signEggEntity);

    /**
     * 限时领金蛋弹窗
     * @param got_eggs
     */
    void showGoldEggsNum(String got_eggs);
}
