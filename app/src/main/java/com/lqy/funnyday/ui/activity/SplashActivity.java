package com.lqy.funnyday.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.lqy.funnyday.R;
import com.lqy.funnyday.util.PreferenceUtil;
import com.lqy.funnyday.util.WeatherInfoUtil;


/**
 * Created by mrliu on 16-7-21.
 */
public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private ImageView imageView; //logo
    private AnimationSet animationSet; //动作设置对象
    private AlphaAnimation alphaAnimation; //淡入淡出动作对象
    private ScaleAnimation scaleAnimation; //旋转动作对象

    //存储相关
    private PreferenceUtil preferenceUtil;
    private WeatherInfoUtil weatherInfoUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        initAnimation();

    }

    private void initAnimation() {
        imageView = (ImageView) findViewById(R.id.imgV_logo);
        animationSet = new AnimationSet(true);
        alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(2000);
        animationSet.addAnimation(alphaAnimation);
        imageView.startAnimation(animationSet);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            /**
             * 动画开始，开始加载服务，包括定位
             * @param animation
             */
            @Override
            public void onAnimationStart(Animation animation) {
                initService();
            }

            /**
             * 动画结束跳转至MainActivity
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initService() {
        initData();
    }

    private void initData() {
        preferenceUtil = new PreferenceUtil(this);
        weatherInfoUtil = new WeatherInfoUtil(this);
        String cityCode = "010101";
        preferenceUtil.saveToPreference(PreferenceUtil.NAME_WEATHER, PreferenceUtil.KEY_CITY_CODE, cityCode);
        weatherInfoUtil.queryWeatherCode(cityCode); //通过城市代码，查询天气信息并将天气数据存入SharedPreference文件中
    }


}
