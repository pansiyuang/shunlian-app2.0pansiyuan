package com.shunlian.app.newchat.event;

import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.UserInfoEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/4.
 */

public class UpdateListEvent extends BaseEvent {
    /**
     * 构造
     *
     * @param type
     */

    private BaseMessage baseMessage;

    private String userId;

    private List<UserInfoEntity.Info.Friend> mList;

    public UpdateListEvent(EventType type) {
        super(type);
    }

    public UpdateListEvent setBaseMessage(BaseMessage msg) {
        this.baseMessage = msg;
        return this;
    }

    public BaseMessage getBaseMessage() {
        return baseMessage;
    }

    public String getUserId() {
        return userId;
    }

    public UpdateListEvent setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public List<UserInfoEntity.Info.Friend> getmList() {
        return mList;
    }

    public UpdateListEvent setmList(List<UserInfoEntity.Info.Friend> mList) {
        this.mList = mList;
        return this;
    }
}
