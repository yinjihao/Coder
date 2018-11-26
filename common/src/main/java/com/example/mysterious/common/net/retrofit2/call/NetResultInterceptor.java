package com.example.mysterious.common.net.retrofit2.call;


import com.example.mysterious.common.net.model.HttpResult;

import retrofit2.Call;

/**
 * retrofit请求结果的一个切面拦截器
 */
public interface NetResultInterceptor {


    void interceptorSucc(Call call, HttpResult hr);

    void interceptorAPIError(Call call, HttpResult hr);

    void interceptorError(Call call, Throwable e);



    NET_INTERCEPTOR mInstance = new NET_INTERCEPTOR();
    class NET_INTERCEPTOR  implements NetResultInterceptor {

        private NetResultInterceptor mReference;

        public void set(NetResultInterceptor ycSuccInterceptor){
            mReference = ycSuccInterceptor;
        }

        private NetResultInterceptor get(){
            return mReference==null? EMPTY:mReference;
        }

        @Override
        public void interceptorSucc(Call call,HttpResult hr) {
            get().interceptorSucc(call,hr);
        }

        @Override
        public void interceptorAPIError(Call call, HttpResult hr) {
            get().interceptorAPIError(call,hr);
        }

        @Override
        public void interceptorError(Call call, Throwable e) {
            get().interceptorError(call,e);
        }

        private static final NetResultInterceptor EMPTY =  new NetResultInterceptor() {
            @Override
            public void interceptorSucc(Call call,HttpResult hr) {
            }
            @Override
            public void interceptorAPIError(Call call, HttpResult hr) {
            }
            @Override
            public void interceptorError(Call call, Throwable e) {
            }
        };
    }

}
