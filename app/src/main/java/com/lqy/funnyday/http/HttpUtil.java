package com.lqy.funnyday.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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

    private Context context;
    private RequestQueue mRequestQueue;

    public HttpUtil(Context context){
        this.context = context;
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


    public RequestQueue getVolleyRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(context, new OkHttpStack());
        }
        return mRequestQueue;
    }

}
