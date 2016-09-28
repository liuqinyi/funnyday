package com.lqy.funnyday.util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mrliu on 16-7-21.
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";

    private static OkHttpClient okHttpClient;
    private static Request request;
    private static Response response;
    private static String result;

    /**
     * okHttp同步GET请求方法
     * 不过基本没没什么鸟用
     */
    public static String doSynGet(final String url) {
        okHttpClient = new OkHttpClient();
        request = new Request.Builder().url(url).build();
        try {
            response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected Code" + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return result;
    }

    /**
     * okHttp异步请求GET方法
     */
    public static void doAsynGet(String url , Callback callback) {
        okHttpClient = new OkHttpClient();
        request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }
}
