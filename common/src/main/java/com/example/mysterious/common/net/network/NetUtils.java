package com.example.mysterious.common.net.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.io.Closeable;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 */
public class NetUtils {


    /**
     * 从head里获取Charset
     *
     * @param headers
     * @param defaultCharset
     * @return
     */
    public static String parseCharset(Map<String, String> headers, String defaultCharset) {
        if (headers == null || headers.isEmpty()) {
            return defaultCharset;
        }
        String contentType = headers.get("Content-Type");
        if (contentType != null) {
            String[] params = contentType.split(";");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }
        return defaultCharset;
    }

    /**
     * 从head里获取Charset
     *
     * @param headers
     * @return
     */
    public static String parseCharset(Map<String, String> headers) {
        // "ISO-8859-1"
        return parseCharset(headers, "UTF-8");
    }




    /**
     * 猜文件的mimetype
     *
     * @param path
     * @return
     */
    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    /**
     * 获取网络状态
     */
    public static String getNetworkState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            return "unConnected";
        }
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        if (isWiFi) {
            return "wifi";
        }
        // 取得Mobile信息 2G、3G还是4G
        TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }
    }

    /**
     * 关闭流文件
     */
    public static void silentCloseStream(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取根域名
     * @param url
     * @return
     */
    public static String getRootDomain(String url) {
        String domain = "";
        Pattern pattern = Pattern
                .compile("[\\w-]+\\.(com.cn|net.cn|gov.cn|org.cn|com|net|org|gov|cc|biz|info|cn|co)\\b()*");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            domain = matcher.group();
        }
        if (domain == null || domain.trim().equals(""))
            return null;
        else {
            return domain;
        }
    }


}
