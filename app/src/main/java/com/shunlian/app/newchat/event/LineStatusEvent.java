package com.shunlian.app.newchat.event;

import com.shunlian.app.newchat.entity.StatusEntity;

/**
 * Created by Administrator on 2017/11/6.
 */

public class LineStatusEvent extends BaseEvent {

    private StatusEntity statusEntity;

    /**
     * 构造
     *
     * @param type
     */
    public LineStatusEvent(EventType type) {
        super(type);
    }

    public StatusEntity getStatusEntity() {
        return statusEntity;
    }

    public LineStatusEvent setStatusEntity(StatusEntity statusEntity) {
        this.statusEntity = statusEntity;
        return this;
    }
}
