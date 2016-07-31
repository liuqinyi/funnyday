package com.lqy.funnyday.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.lqy.funnyday.R;
import com.lqy.funnyday.model.location.domain.MyLocation;


/**
 * Created by mrliu on 16-7-21.
 */
public class SplashActivity extends Activity {

    private ImageView imageView; //logo
    private AnimationSet animationSet; //动作设置对象
    private AlphaAnimation alphaAnimation; //淡入淡出动作对象
    private ScaleAnimation scaleAnimation; //旋转动作对象
    private MyLocation myLocation;
    private LocationManager locationManager;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        imageView = (ImageView)findViewById(R.id.imgV_logo);
        animationSet = new AnimationSet(true);
        alphaAnimation = new AlphaAnimation(0.1f,1.0f);
        alphaAnimation.setDuration(2000);
        animationSet.addAnimation(alphaAnimation);
        imageView.startAnimation(animationSet);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                myLocation =  new MyLocation(SplashActivity.this);
                location = myLocation.getMyLocation(locationManager);
                if (location == null){
                    Toast.makeText(SplashActivity.this, "无法定位", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}
