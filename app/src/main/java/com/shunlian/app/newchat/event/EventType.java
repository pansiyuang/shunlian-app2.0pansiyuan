package com.shunlian.app.newchat.event;

/**
 * EventBus事件类别定义
 *
 * @author lucher
 */
public enum EventType {
    /**
     * 连接中
     */
    CONNECTING,
    /**
     * 建立连接事件
     */
    OPEN,
    /**
     * 断开连接事件
     */
    CLOSE,
    /**
     * 消息事件
     */
    MESSAGE,
    /**
     * 片段
     */
    FRAGMENT,
    /**
     * 错误
     */
    ERROR,
    /**
     * 超时事件
     */
    TIMEOUT,

    /**
     * 刷新消息列表
     */
    FRIENDLIST,

    /**
     * 用户在线状态
     */
    LINESTATUS,

    /**
     * 未读消息更新
     */
    UNREADCOUNT
}
