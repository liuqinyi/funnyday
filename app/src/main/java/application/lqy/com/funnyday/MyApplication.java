package application.lqy.com.funnyday;

import android.app.Application;
import android.content.Context;

/**
 * Created by mrliu on 16-7-20.
 */
public class MyApplication extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

}
