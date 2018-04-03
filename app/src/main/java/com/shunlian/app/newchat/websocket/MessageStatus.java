package com.shunlian.app.newchat.websocket;

/**
 * Created by Administrator on 2017/9/14.
 */

public class MessageStatus {
    public static final int Invalid = 0;
    public static final int Sending = 1;
    public static final int SendSucc = 2;
    public static final int SendFail = 3;
    public static final int HasDeleted = 4;
}
