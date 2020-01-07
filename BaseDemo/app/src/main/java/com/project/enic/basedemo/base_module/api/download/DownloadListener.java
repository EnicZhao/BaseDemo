package com.project.enic.basedemo.base_module.api.download;

public interface DownloadListener {
    void onStartDownload();
    void onProgress(int progress);
    void onFinishDownload();
    void onFail(String errorInfo);
}
