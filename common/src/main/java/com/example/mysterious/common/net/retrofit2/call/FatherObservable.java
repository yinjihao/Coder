package com.example.mysterious.common.net.retrofit2.call;

import com.example.mysterious.common.net.model.HttpResult;
import com.example.mysterious.common.net.retrofit2.YCGsonConverterFactory;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;

/**
 * 包装下RX的适配器 处理下奖励、错误之类的
 * Created by hanbo on 2018/3/12.
 */

public class FatherObservable<T> extends YObservable<T> {

    private Observable<T> upstream;
    private Call call;

    public FatherObservable(Observable<T> upstream, Call call) {
        this.upstream = upstream;
        this.call = call;
    }

    @Override
    protected void subscribeActual(Observer observer) {
        this.upstream.subscribe(new BodyObserver(observer,call));
    }

    private static class BodyObserver<R> implements Observer<R> {
        private final Observer<? super R> observer;
        private boolean terminated; //标记是在onNext里出错处理
        private Call call;

        BodyObserver(Observer<? super R> observer, Call call) {
            this.observer = observer;
            this.call = call;
        }

        @Override
        public void onSubscribe(Disposable disposable) {
            this.observer.onSubscribe(disposable);
        }

        @Override
        public void onNext(R r) {
            if(!(r instanceof HttpResult)){
                this.observer.onNext(r);
                return;
            }

            HttpResult hr = (HttpResult) r;
            if (hr.isSuccess()) {
                //成功
                try {//处理拦截
                    NetResultInterceptor.mInstance.interceptorSucc(call,hr);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                this.observer.onNext(r);
            } else {
                //API异常
                this.terminated = true;
                try {//处理拦截
                    NetResultInterceptor.mInstance.interceptorAPIError(call,hr);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                   /* this.observer.onError(new YCCallAdapter.APIRuntimeError(hr.message,hr.status, hr.originJsonString));*/
                } catch (Throwable var4) {
                    Exceptions.throwIfFatal(var4);
                    RxJavaPlugins.onError(var4);
                }
            }
        }


        @Override
        public void onComplete() {
            if (!this.terminated) {

                this.observer.onComplete();
            }

        }
        @Override
        public void onError(Throwable e) {
            if (!this.terminated) {
                Throwable throwable = null;
                if(e instanceof YCGsonConverterFactory.ParseError){
                   /* throwable =  new YCCallAdapter.ParseRunTimeError((YCGsonConverterFactory.ParseError) e,e.getCause());*/
                }else{
                  //  throwable =  new RuntimeException(YCAPI.SERVER_FAIL_DESC,e);
                }

                try {//处理拦截
                    NetResultInterceptor.mInstance.interceptorError(call,e);
                } catch (Throwable es) {
                   // YLog.e(es);
                }

                this.observer.onError(throwable);
            } else {
                //正常不应该发生
                Throwable broken = new AssertionError("This should never happen! Report as a bug with the full stacktrace.");
                broken.initCause(e);
                RxJavaPlugins.onError(broken);
            }

        }

    }
}
