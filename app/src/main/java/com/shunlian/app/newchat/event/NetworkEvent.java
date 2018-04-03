package com.shunlian.app.newchat.event;

/**
 * Created by Administrator on 2017/9/27.
 */

public class NetworkEvent extends BaseEvent {

    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 正在连接网络
     */
    public static final int NETWORK_ENABLE = 0;

    private int connetStatus;

    /**
     * 构造
     *
     * @param type
     */
    public NetworkEvent(EventType type) {
        super(type);
    }


    public int getConnetStatus() {
        return connetStatus;
    }

    public NetworkEvent setConnetStatus(int connetStatus) {
        this.connetStatus = connetStatus;
        return this;
    }
}
