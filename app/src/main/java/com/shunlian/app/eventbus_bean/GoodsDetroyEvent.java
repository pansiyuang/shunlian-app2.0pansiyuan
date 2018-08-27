package com.shunlian.app.eventbus_bean;

/**
 * Created by zhanghe on 2018/8/27.
 */

public class GoodsDetroyEvent {

    public boolean isrelease;//详情页视频是否回收释放
    public String goods_id;//配合isrelease一起使用，过滤事件

}
