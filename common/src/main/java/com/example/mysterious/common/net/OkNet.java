package com.example.mysterious.common.net;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by hanbo1 on 2016/4/5.
 */
public class OkNet {
    private static volatile OkHttpClient mClient;

    public static OkHttpClient getOkHttpClient() {
        return mClient;
    }

    private static OkHttpClient initOkHttpClient(Application context, boolean debug, Interceptor... interceptors) {
        if (mClient == null) {
            synchronized (OkNet.class) {
                if (mClient == null) {
                    OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                          //  .cookieJar(new CookieJarImpl(new PersistentCookieStore(context)))
                          //  .addInterceptor(new OkIntercetor())
                          //  .addInterceptor(new BpHostInterceptor())
                           // .addInterceptor(new LogIntercetor())
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .pingInterval(10, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS);

                    if (debug) {
                     //   okHttpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
                    }
                    if (interceptors != null) {
                        for (int i = 0; i < interceptors.length; i++) {
                            okHttpClientBuilder.addInterceptor(interceptors[i]);
                        }
                    }

//                    DumpInterceptor dumpInterceptor = new DumpInterceptor(context, 1 * 1000 * 1000);
//                    dumpInterceptor.setLevel(DumpInterceptor.Level.BODY);
//                    okHttpClientBuilder.addInterceptor(dumpInterceptor);

                    /*try {
                        File cacheFile = new File(AutoEasyApplication.getInstance().getCacheDir(), "ok_cache");
                        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);
                        if(cache!=null){
                            okHttpClientBuilder.cache(cache);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                   /* okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });*/

                    mClient = okHttpClientBuilder.build();
                }
            }
        }
        return mClient;
    }

    public static void init(Application context, boolean debug, Interceptor... interceptors) {
        initOkHttpClient(context, debug, interceptors);
        /*java.net.CookieManager cookieManager = new java.net.CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);*/

    }

   /* static void clearCookie() {
        CookieJar cookieJar = mClient.cookieJar();
        if (cookieJar != null && cookieJar instanceof CookieJarImpl) {
            CookieJarImpl cookie = (CookieJarImpl) cookieJar;
            cookie.getCookieStore().removeAll();
        }
    }*/

  /*  static boolean isCookieExpired() {
        PersistentCookieStore cookieStore = getCookieStore();
        if (cookieStore == null || CollectionsWrapper.isEmpty(cookieStore.getCookies())) {
            return false;
        }
        boolean cookieExpired = cookieStore.getCookies().get(0).expiresAt() <= System.currentTimeMillis();
        if (cookieExpired) {
            StringBuilder sb = new StringBuilder("Cookie expired!!!! \n errormsg:")
                    .append("authCookie failed!")
                    .append("\ndeviceid:")
                    .append(DeviceInfoUtils.getInstance().getDeviceId())
                    .append("\nPhoneModel:")
                    .append(DeviceInfoUtils.getInstance().getPhoneModel())
                    .append("\ncookieExpiredAt:")
                    .append(cookieStore.getCookies().get(0).expiresAt())
                    .append("\nSystemNowTime:")
                    .append(System.currentTimeMillis());
        }
        return cookieExpired;
    }*/


/*    private static PersistentCookieStore getCookieStore() {
        CookieJar cookieJar = mClient.cookieJar();
        if (cookieJar == null || !(cookieJar instanceof CookieJarImpl)) {
            return null;
        }
        CookieJarImpl cookie = (CookieJarImpl) cookieJar;
        return cookie.getCookieStore();
    }


    //获取所有cookie
    static List<FakeCookie> getAllCookies() {
        List<Cookie> cookies = getCookieStore().getCookies();
        if (CollectionsWrapper.isEmpty(cookies)) {
            return null;
        }
        List<FakeCookie> fakeCookies = new ArrayList<>(cookies.size());
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            FakeCookie fakeCookie = new FakeCookie(cookie.name(),
                    cookie.value(), cookie.expiresAt(), cookie.domain(), cookie.path());
            fakeCookies.add(fakeCookie);
        }
        return fakeCookies;
    }*/
}
