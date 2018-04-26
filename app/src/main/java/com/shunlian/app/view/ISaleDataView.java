package com.shunlian.app.view;

import com.shunlian.app.bean.SaleDataEntity;
import com.shunlian.app.bean.SalesChartEntity;

/**
 * Created by Administrator on 2018/4/12.
 */

public interface ISaleDataView extends IView {
    /**
     * 设置成长值和请求码
     * @param growth_value
     * @param request_code
     */
    void setGrowthValue_RequestCode(String growth_value,String request_code);

    /**
     * 设置头像和昵称
     * @param head
     * @param nickname
     */
    void setHeadNickname(String head,String nickname);

    /**
     * 设置等级
     * @param plus_role_code
     */
    void setplusrole(String plus_role_code);

    /**
     * 设置本月销售额，今日会员，今日销售订单
     * @param this_month
     * @param today_vip
     * @param today_order
     */
    void setMonthVip_Order(String this_month,String today_vip,String today_order);

    /**
     *
     * @param xiaodao 小店销售额
     * @param fendiao 分店销售额
     * @param xiaofei 消费金额
     */
    void setSaleData(String xiaodao,String fendiao,String xiaofei);

    /**
     * 设置折线图
     * @param saleChart
     */
    void setSaleChart(SalesChartEntity saleChart);

    /**
     * 设置精英导师数据
     * @param masterInfo
     */
    void setEliteTutorData(SaleDataEntity.MasterInfo masterInfo);
}
