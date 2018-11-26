package com.example.mysterious.common.net.tobiz;

import com.bitauto.libcommon.net.model.HttpResult;
import com.bitauto.libcommon.tools.RxUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by sudi on 2018/6/28.
 * Email：sudi@yiche.com
 */
public class RequestManager {

    private BPNetCallback callback;
    private CompositeDisposable disposables;

    public RequestManager(BPNetCallback c) {
        this.callback = c;
    }

    public <T> void doRequest(final String tag, Observable<HttpResult<T>> request) {
        if (callback != null) {
            callback.onRequestStart(tag);
        }
        _doReq(tag, request);
    }

    private <T> void _doReq(final String tag, Observable<HttpResult<T>> request) {
        request.compose(RxUtil.<HttpResult<T>>getTransformer()).
                subscribe(new Observer<HttpResult<T>>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(HttpResult<T> tHttpResult) {
                        if (callback != null) {
                            callback.onRequestSuccess(tag, tHttpResult.data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        removeDisposable(d);
                        if (callback != null) {
                            callback.onRequestFail(tag, e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        removeDisposable(d);
                    }
                });
    }

    private void addDisposable(Disposable d) {
        if (disposables == null || disposables.isDisposed()) {
            disposables = new CompositeDisposable();
        }
        disposables.add(d);
    }

    private void removeDisposable(Disposable d) {
        if (disposables != null && d != null) {
            disposables.remove(d);
        }
    }

    public void unDisposable() {
        if (disposables != null) {
            disposables.dispose();
        }
        if (this.callback != null) {
            this.callback = null;
        }
    }
}
