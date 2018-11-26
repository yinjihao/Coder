package com.example.mysterious.common.net.retrofit2.call;

import com.bitauto.libcommon.tools.RxUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by yiche on 2018/7/9.
 */

public abstract class YObservable<T> extends Observable {
    public void call(final String action, final Callback<T> callback) {
        this.compose(RxUtil.<T>getTransformer()).subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(T result) {
                callback.onSuccess(action, result);
            }

            @Override
            public void onError(Throwable e) {
                callback.onFail(action, e);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
