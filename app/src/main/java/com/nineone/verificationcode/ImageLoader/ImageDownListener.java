package com.nineone.verificationcode.ImageLoader;

public interface ImageDownListener {
    //开始下载
    void onDownStarted(String url, String targetPath);

    //下载失败
    void onDownFailed(String url, String targetPath, String exceptionMessage);

    //下载成功
    void onDownSuccess(String url, String targetPath);
}
