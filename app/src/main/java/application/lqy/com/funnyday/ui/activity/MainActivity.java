package application.lqy.com.funnyday.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import application.lqy.com.funnyday.R;
import application.lqy.com.funnyday.model.dynamic.ui.DynamicFragment;
import application.lqy.com.funnyday.model.lbs.ui.ChoiceCityActivity;
import application.lqy.com.funnyday.model.news.NewsFragment;
import application.lqy.com.funnyday.model.own.OwnFragment;
import application.lqy.com.funnyday.model.weather.ui.WeatherFragment;
import application.lqy.com.funnyday.thread.HttpThread;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{

    private static final String TAG = "MainActivity";
    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;

    //Fragment Object
    private DynamicFragment dynamicFragment;
    private WeatherFragment weatherFragment;
    private NewsFragment newsFragment;
    private OwnFragment ownFragment;
    private FragmentManager fManager;
    private Toolbar toolbar;
    private Button button;

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

        //创建网络请求子线程
        HttpThread httpThread = new HttpThread(MainActivity.this);
        Thread netHttp = new Thread(httpThread);
        netHttp.start();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (Button)findViewById(R.id.btn_toolbar_choice_city);
        if(button != null){
            button.setOnClickListener(this);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_toolbar_choice_city:
                Intent intent = new Intent(MainActivity.this,ChoiceCityActivity.class);
                break;
        }
    }

}
