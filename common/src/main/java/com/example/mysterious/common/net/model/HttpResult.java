package com.example.mysterious.common.net.model;

import android.support.annotation.Keep;

/**
 * Description: 统一接口返回格式
 * author: Lyongwang
 * date: 2017/5/16 下午5:34
 */
@Keep
public class HttpResult<T> {
    public static final int SUCC_STATE = 1;
    public String Status;
    /** 状态码*/
    public int status;
    /** 状态信息*/
    public String message;
    /** 具体数据*/
    public T data;

    @Override
    public String toString() {
        return "HttpResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * Description: 是否获取到正确数据
     * author: Lyongwang
     * date: 2017/5/16 下午5:37
     * @return 是 true 否 false
     */
    public boolean isSuccess() {
        return status == SUCC_STATE || Status != null;
    }

    /**
     * Description: 设置数据
     * author: Lyongwang
     * date: 2017/5/16 下午5:37
     * @param data 具体数据
     * @return HttpResult对象
     */
    public HttpResult<T> setData(T data) {
        this.data = data;
        return this;
    }


    /**
     * 接口返回的jsonString，可能为null
     * 和Status无关
     */
    public String originJsonString;

}
