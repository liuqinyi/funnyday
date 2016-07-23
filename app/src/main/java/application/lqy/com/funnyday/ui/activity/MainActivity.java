package application.lqy.com.funnyday.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import application.lqy.com.funnyday.R;
import application.lqy.com.funnyday.db.WeatherDB;
import application.lqy.com.funnyday.http.HttpUtil;
import application.lqy.com.funnyday.model.dynamic.ui.DynamicFragment;
import application.lqy.com.funnyday.model.news.NewsFragment;
import application.lqy.com.funnyday.model.own.OwnFragment;
import application.lqy.com.funnyday.model.weather.ui.WeatherFragment;
import application.lqy.com.funnyday.util.AnalysisUtil;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private static final String TAG = "MainActivity";
    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;

    //Fragment Object
    private DynamicFragment dynamicFragment;
    private WeatherFragment weatherFragment;
    private NewsFragment newsFragment;
    private OwnFragment ownFragment;
    private FragmentManager fManager;

    private HttpUtil httpUtil;

    private WeatherDB weatherDB;

    private AnalysisUtil analysisUtil;


    private static final String provinceAddress = "http://www.weather.com.cn/data/list3/city.xml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fManager = getSupportFragmentManager();
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);
        //获取第一个单选按钮，并设置其为选中状态
        rb_channel = (RadioButton) findViewById(R.id.rb_dynamic);
        rb_channel.setChecked(true);

        saveCityToDb();

    }

    private synchronized void saveCityToDb() {
        httpUtil = new HttpUtil(); //获取HttpUtil实例,访问网络数;
        analysisUtil = new AnalysisUtil();
        weatherDB = new WeatherDB(this);

        new Thread(){
//        WeatherDB weatherDB = WeatherDB.getInstance(MainActivity.this);  //获取weatherDB实例操作数据

            @Override
            public void run() {
                super.run();
                String response = httpUtil.get(provinceAddress);
                Log.d(TAG, "获取全国省份代码" + response );
                analysisUtil.handleProvincesResponse(weatherDB,response);
            }
        }.start();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (checkedId){
            case R.id.rb_dynamic:
                if(dynamicFragment == null){
                    dynamicFragment = new DynamicFragment("动态\n暂无内容");
                    fTransaction.add(R.id.ly_content, dynamicFragment);
                }else{
                    fTransaction.show(dynamicFragment);
                }
                break;
            case R.id.rb_weather:
                if(weatherFragment == null){
                    weatherFragment = new WeatherFragment("第二个Fragment");
                    fTransaction.add(R.id.ly_content,weatherFragment);
                }else{
                    fTransaction.show(weatherFragment);
                }
                break;
            case R.id.rb_news:
                if(newsFragment == null){
                    newsFragment = new NewsFragment("第三个Fragment");
                    fTransaction.add(R.id.ly_content,newsFragment);
                }else{
                    fTransaction.show(newsFragment);
                }
                break;
            case R.id.rb_own:
                if(ownFragment == null){
                    ownFragment = new OwnFragment("第四个Fragment");
                    fTransaction.add(R.id.ly_content,ownFragment);
                }else{
                    fTransaction.show(ownFragment);
                }
                break;
        }
        fTransaction.commit();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(dynamicFragment != null)fragmentTransaction.hide(dynamicFragment);
        if(weatherFragment != null)fragmentTransaction.hide(weatherFragment);
        if(newsFragment != null)fragmentTransaction.hide(newsFragment);
        if(ownFragment != null)fragmentTransaction.hide(ownFragment);
    }



}
