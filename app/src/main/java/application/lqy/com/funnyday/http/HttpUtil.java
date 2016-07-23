package application.lqy.com.funnyday.http;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mrliu on 16-7-21.
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";


    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //OkHttp 同步get()方法,响应值为String result
    public String get(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        Response response = null;
        String result = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if(!response.isSuccessful()) throw new IOException("Unexpected Code" + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
