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

    private SensorManager sensorManager;
    private Context context;
    private Sensor sensor;

    private float lastX;


    public MyDirectionSensorListener(Context context){
        this.context = context;
    }

    public void start(){
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if (sensor != null){
            
        }
    }

    public void stop(){

    }
    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
