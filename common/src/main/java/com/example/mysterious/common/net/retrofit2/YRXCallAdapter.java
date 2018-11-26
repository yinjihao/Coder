package com.example.mysterious.common.net.retrofit2;


import com.example.mysterious.common.net.retrofit2.call.FatherObservable;
import com.example.mysterious.common.net.retrofit2.call.RxJava2CallAdapterFactory2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * 包装rxjava的适配器
 * Created by hanbo on 2018/3/12.
 */

public class YRXCallAdapter extends CallAdapter.Factory {

    RxJava2CallAdapterFactory2 factory;

    public YRXCallAdapter(RxJava2CallAdapterFactory2 rxJava2CallAdapterFactory) {
        factory = rxJava2CallAdapterFactory;
    }

    public static YRXCallAdapter create() {
        return new YRXCallAdapter(RxJava2CallAdapterFactory2.create());
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RXC(factory.get(returnType,annotations,retrofit));
    }


    public static class RXC implements CallAdapter{

        CallAdapter<?, ?> callAdapter;

        public RXC(CallAdapter<?, ?> callAdapter) {
            this.callAdapter = callAdapter;
        }

        @Override
        public Type responseType() {
            return callAdapter.responseType();
        }

        @Override
        public Object adapt(Call call) {
            Object o = callAdapter.adapt(call);

            if(o instanceof Observable){
                return new FatherObservable((Observable) o,call);
            }
            return o;
        }
    }
}