package com.nineone.verificationcode.ImageLoader;

public interface ProgressListener {
    /**
     * 下载进度监听
     * @param progress 进度值0-100
     */
    void onProgress(int progress);
}
