package com.shunlian.app.utils.upload;

public interface ProgressListener {
    //要是单文件上传，就不必再根据字节去计算了，直接在requestbody中计算好进度直接返回
    void onProgress(int progress, String tag);
    //处理多文件时，需要获取每个文件的即时上传量来计算整体的进度
    void onDetailProgress(long written, long total, String tag);
}