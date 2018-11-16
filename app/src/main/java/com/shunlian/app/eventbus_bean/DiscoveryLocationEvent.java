package com.shunlian.app.eventbus_bean;

/**
 * Created by Administrator on 2018/11/5.
 */

public class DiscoveryLocationEvent {
    public int[] location;
    public int imgWidth;

    public DiscoveryLocationEvent(int[] l, int width) {
        location = l;
        imgWidth = width;
    }
}
