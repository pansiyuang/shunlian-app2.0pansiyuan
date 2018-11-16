package com.shunlian.app.utils.download;
/**
 * Description: 下载进度回调
 */
public interface JsDownloadListener {
    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload(String filePath,boolean isCancel);

    void onFail(String errorInfo);

    void onFinishEnd();
}
