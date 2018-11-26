package com.example.mysterious.common.net;


import android.app.Application;
import android.support.annotation.NonNull;

import com.example.mysterious.common.net.model.HttpResult;
import com.example.mysterious.common.net.tobiz.BPNetCallback;
import com.example.mysterious.common.util.RxUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import retrofit2.Retrofit;


public class Net {


    public static void init(Application application, boolean debug, Interceptor... interceptors) {
        OkNet.init(application, debug, interceptors);
    }





    public static <T> T getService2(Class<T> clazz) {
        return RetrofitHelper.getService2(clazz);
    }

    public static Retrofit getRetrofit(String baseUrl) {

        return RetrofitHelper.getRetrofit(baseUrl);
    }

    public static Retrofit getRetrofit(String baseUrl, Interceptor interceptor) {
        return RetrofitHelper.getRetrofit2(baseUrl, interceptor);
    }

    public static <T> Disposable doRequest(final String tag, Observable<HttpResult<T>> request, final BPNetCallback callback) {
        if (check(callback)) {
            callback.onRequestStart(tag);
        }
        Observable<HttpResult<T>> observable = request.compose(RxUtil.<HttpResult<T>>getTransformer());
        return returnResult(tag, callback, observable);
    }

    private static boolean check(BPNetCallback callback) {
        return callback != null && callback.canReceive();
    }

    @NonNull
    private static <T> Disposable returnResult(final String tag, final BPNetCallback callback, Observable<HttpResult<T>> observable) {
        return observable.subscribe(new Consumer<HttpResult<T>>() {
            @Override
            public void accept(HttpResult<T> tHttpResult) throws Exception {
                if (check(callback)) {
                    callback.onRequestSuccess(tag, tHttpResult.data);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (check(callback)) {
                    callback.onRequestFail(tag, throwable);
                }
            }
        });
    }

    public static <T> void doRequest(final String tag, Observable<HttpResult<T>> request, final Observer<HttpResult<T>> pObserver) {
        request.compose(RxUtil.<HttpResult<T>>getTransformer()).
                subscribe(pObserver);
    }
}
