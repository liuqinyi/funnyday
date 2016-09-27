package com.lqy.funnyday.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lqy.funnyday.R;
import com.lqy.funnyday.db.LocationDB;
import com.lqy.funnyday.model.dynamic.ui.DynamicFragment;
import com.lqy.funnyday.model.location.ui.ChoiceCityActivity;
import com.lqy.funnyday.model.news.NewsFragment;
import com.lqy.funnyday.model.own.OwnFragment;
import com.lqy.funnyday.model.weather.ui.WeatherFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{

    private static final String TAG = "MainActivity";
    private static boolean isExit = false; //双击退出

    //Fragment对象
    private DynamicFragment dynamicFragment;
    private WeatherFragment weatherFragment;
    private NewsFragment newsFragment;
    private OwnFragment ownFragment;
    private FragmentManager fManager;

    //UI组件
    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;
    private Toolbar toolbar;
    private Button button;

    //数据
    private SharedPreferences sharedPreferences;


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

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        button = (Button)findViewById(R.id.toolbar_btn);
        if(button != null){
            button.setOnClickListener(this);
        }

        initData();
    }

    private void initData() {
        initPreferences();
    }

    private void initPreferences() {
        sharedPreferences = getSharedPreferences("cityCode",MODE_PRIVATE);
        sharedPreferences.getString("cityCode", LocationDB.defaultCityCode);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (checkedId){
            case R.id.rb_dynamic:
                if(dynamicFragment == null){
                    dynamicFragment = DynamicFragment.getInstance("第一个Fragment");
                    fTransaction.add(R.id.ly_content, dynamicFragment);
                }else{
                    fTransaction.show(dynamicFragment);
                }
                break;
            case R.id.rb_weather:
                if(weatherFragment == null){
                    weatherFragment = WeatherFragment.getInstance("WeatherFragment");
                    fTransaction.add(R.id.ly_content,weatherFragment);
                }else{
                    fTransaction.show(weatherFragment);
                }
                break;
            case R.id.rb_news:
                if(newsFragment == null){
                    newsFragment = NewsFragment.getInstance("第三个Fragment");
                    fTransaction.add(R.id.ly_content,newsFragment);
                }else{
                    fTransaction.show(newsFragment);
                }
                break;
            case R.id.rb_own:
                if(ownFragment == null){
                    ownFragment = OwnFragment.getInstance("第四个Fragment");
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
            case R.id.toolbar_btn:
                Intent intent = new Intent(MainActivity.this,ChoiceCityActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Timer tExit;
            if (isExit == false) {
                isExit = true; // 准备退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false; // 取消退出
                    }
                }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            } else {
                finish();
                System.exit(0);
            }
        }
        return false;
    }

}
