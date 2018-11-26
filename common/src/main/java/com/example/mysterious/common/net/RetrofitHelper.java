package com.example.mysterious.common.net;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitHelper {

    private static Retrofit.Builder builder_with_error_process = new Retrofit.Builder()
           // .addConverterFactory(YCGsonConverterFactory.create())
          //  .baseUrl("http://api.ycapp.yiche.com")
           // .addCallAdapterFactory(YCCallAdapter.create())
           // .addCallAdapterFactory(YRXCallAdapter.create())
            .client(OkNet.getOkHttpClient());

    private static Retrofit mDefault = builder_with_error_process.build();

    static<T> T getService2(Class<T> clazz) {
        return mDefault.create(clazz);
    }

    static Retrofit getRetrofit(String baseUrl) {
        return getRetrofit2(baseUrl,null);
    }

    static Retrofit getRetrofit2(String baseUrl, Interceptor interceptor) {
        if(interceptor!=null){
            OkHttpClient client = OkNet.getOkHttpClient().newBuilder().addInterceptor(interceptor).build();
            return builder_with_error_process.baseUrl(baseUrl).client(client).build();
        }else{
            return baseUrl==null?mDefault:builder_with_error_process.baseUrl(baseUrl).build();
        }
    }
}
