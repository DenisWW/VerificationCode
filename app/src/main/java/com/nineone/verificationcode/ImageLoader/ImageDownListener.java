package com.nineone.verificationcode.ImageLoader;

public interface ImageDownListener {
    void onDownStarted(String url, String targetPath);

    void onDownFailed(String url, String targetPath, String exceptionMessage);

    void onDownSuccess(String url, String targetPath);
}
