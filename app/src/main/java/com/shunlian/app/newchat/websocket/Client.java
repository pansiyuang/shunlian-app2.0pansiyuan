package com.shunlian.app.newchat.websocket;

import com.shunlian.app.utils.LogUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by Administrator on 2018/5/17.
 */

public class Client extends WebSocketClient {
    private OnClientConnetListener mListener;
    //超时时间
    private static long timeout = 15 * 1000;
    private TimeOutThread timeOutThread;

    public Client(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    /**
     * 连接超时检测线程
     *
     * @author lucher
     */
    public class TimeOutThread extends Thread {
        //是否取消
        private boolean cancel;

        @Override
        public synchronized void run() {
            try {
                wait(timeout);
                if (!cancel) {
                    close();
                    if (mListener != null) {
                        mListener.onTimeOut();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 取消
         */
        public void cancel() {
            cancel = true;
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LogUtil.httpLogW("onOpen111");
        if (mListener != null) {
            mListener.onOpen();
        }
        if (timeOutThread != null && timeOutThread.isAlive()) {
            timeOutThread.cancel();
        }
    }

    @Override
    public void onMessage(String s) {
        if (mListener != null) {
            mListener.onMessage(s);
        }
    }

    @Override
    public boolean connectBlocking() throws InterruptedException {
        if (timeOutThread == null) {
            timeOutThread = new TimeOutThread();//开启超时任务线程
        }
        timeOutThread.start();
        return super.connectBlocking();
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        LogUtil.httpLogW("连接关闭:" + i + "," + s + "," + b);
        if (mListener != null) {
            mListener.onClose(i, s);
        }
    }

    @Override
    public void onError(Exception e) {
        if (mListener != null) {
            mListener.onError(e);
        }
    }

    public void setOnClientConnetListener(OnClientConnetListener listener) {
        this.mListener = listener;
    }


    public interface OnClientConnetListener {

        void onOpen();

        void onMessage(String s);

        void onClose(int code, String errorStr);

        void onTimeOut();

        void onError(Exception e);
    }
}
