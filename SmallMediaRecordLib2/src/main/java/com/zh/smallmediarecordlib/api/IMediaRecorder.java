package com.zh.smallmediarecordlib.api;

/**
 * 视频录制接口
 * 
 * @author yixia.com
 *
 */
public interface IMediaRecorder {
    /**
     * 开始合成视频
     */
    void startMergeRecordVideo();

    /**
     * 完成合成
     * @param url
     */
    void completeMergeRecordVideo(String url);
}
