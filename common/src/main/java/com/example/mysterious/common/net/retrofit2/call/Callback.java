package com.example.mysterious.common.net.retrofit2.call;

/**
 * Created by yiche on 2018/7/9.
 */

public interface Callback<T> {
    void onSuccess(String action, T t);
    void onFail(String action, Throwable e);
}
