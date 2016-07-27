package com.lqy.funnyday.http;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mrliu on 16-7-21.
 */
public class HttpUtil implements Runnable{
    private static final String TAG = "HttpUtil";

    private OkHttpClient okHttpClient;
    private Request request;
    private Response response;
    private String result;


    public HttpUtil(){

    }

    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //OkHttp 同步get()方法,响应值为String result
    public String doGet(String url) {
        this.okHttpClient = new OkHttpClient();
        request = new Request.Builder().url(url).build();
        return result;
    }

    @Override
    public void run() {
        try {
            response = okHttpClient.newCall(request).execute();
            if(!response.isSuccessful()) throw new IOException("Unexpected Code" + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

/*
    public String post(String url, String json) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
*/
}
