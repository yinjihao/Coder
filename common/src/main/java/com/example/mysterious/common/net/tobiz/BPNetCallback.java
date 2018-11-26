package com.example.mysterious.common.net.tobiz;

/**
 * Created by sudi on 2018/6/28.
 * Email：sudi@yiche.com
 */
public interface BPNetCallback {
    void onRequestStart(String tag);

    void onRequestSuccess(String tag, Object response);

    void onRequestFail(String tag, Throwable throwable);

    boolean canReceive();
}
