package com.lqy.funnyday.model.map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by mrliu on 16-9-14.
 */
public class MyDirectionSensorListener implements SensorEventListener {

    private Context context;
    /**
     * 传感器相关
     */
    private SensorManager sensorManager;
    private Sensor sensor;

    /**
     * 坐标相关
     */
    private float lastX; //x轴数据
    private OnOrientationListener orientation; //通过接口对象回调坐标轴数据

    public MyDirectionSensorListener(Context context) {
        this.context = context;
    }



    public void setOrientation(OnOrientationListener orientation) {
        this.orientation = orientation;
    }

    /**
     * 开启方向传感器
     */
    public void start() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    /**
     * 关闭方向传感器
     */
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float x = event.values[SensorManager.DATA_X];
            if (Math.abs(x - lastX) > 1.0) {
                if (orientation != null) {
                    orientation.onOrientationChanged(x);
                }
            }
            lastX = x;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        //精度改变
    }

    /**
     * 设置回调的接口放回x轴数据
     */
    public interface OnOrientationListener {
        void onOrientationChanged(float x);
    }
}
