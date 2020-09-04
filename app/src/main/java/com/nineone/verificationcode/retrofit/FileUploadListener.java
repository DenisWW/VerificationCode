package com.nineone.verificationcode.retrofit;

public interface FileUploadListener {
    void onRequestProgress(long byteWrited, long contentLength, boolean done);
}
