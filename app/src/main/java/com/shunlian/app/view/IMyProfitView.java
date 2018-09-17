package com.shunlian.app.view;

import com.shunlian.app.bean.MyProfitEntity;
import com.shunlian.app.bean.SalesChartEntity;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface IMyProfitView extends IView {

    /**
     * 设置用户信息
     * @param userInfo
     */
    void setUserInfo(MyProfitEntity.UserInfo userInfo);

    /**
     * 设置收益信息
     * @param profitInfo
     */
    void setProfitInfo(MyProfitEntity.ProfitInfo profitInfo);

    /**
     * 收益折线图
     * @param charts
     */
    void setProfitCharts(SalesChartEntity charts);

    /**
     * 领取月/周奖励
     * @param type 1周奖励 2月奖励
     * @param available_profit
     */
    void receiveReward(String type, String available_profit);

    /**
     * 收益说明
     * @param tip
     */
    void setProfitTip(String tip);
}
