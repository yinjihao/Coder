package com.example.mysterious.common.net.okhttp;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogIntercetor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
      /*  if(!RootInit.isDebug()){
            return chain.proceed(chain.request());
        }*/
        long t1 = System.currentTimeMillis();//请求发起的时间
        Request request = chain.request();
        Response response = chain.proceed(request);
        long t2 = System.currentTimeMillis();//收到响应的时间

        StringBuilder sb = new StringBuilder()
                .append(" \n\n=========START=========\n")
                .append("| URL        :").append(request.url().toString())
                .append("\n")
                .append("| METHOD        :").append(request.method())
                .append("\n")
                .append("| CostTime   :").append(t2-t1).append("ms")
                .append("\n")
                .append("| Response   :").append(response.peekBody(1024*1024).string())
                .append("\n")
                .append("=====RequestHEAD:\n").append(request.headers().toString())
                .append("\n")
                .append("=====ResponsHEAD:\n").append(response.headers().toString())
                .append("=========END=========\n ");
        Log.i("net", sb.toString());
        return response;
    }
}
