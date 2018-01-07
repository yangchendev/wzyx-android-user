package com.allelink.wzyx.activity.baidunav;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by sushijun on 2017/11/11.
 */

public class MyOrientationListener implements SensorEventListener {
        private SensorManager mSensorManager;
        private Sensor mSensor;
        private Context mContext;
        private float lastx;
        private onOrientationListener monOrientationListener;

    public void setmSensorManager(SensorManager mSensorManager) {
        this.mSensorManager = mSensorManager;
    }

    public void setMonOrientationListener(onOrientationListener monOrientationListener) {
        this.monOrientationListener = monOrientationListener;
    }


        MyOrientationListener(Context context){
            this.mContext=context;
        }

        public void start(){
            if(mContext!=null) {
                mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            }
            if(mSensorManager!=null){
                mSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            }
            if(mSensor!=null){
                mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);
            }
        }
        public void stop(){
            mSensorManager.unregisterListener(this);
        }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
            float x=event.values[SensorManager.DATA_X];
            if(Math.abs(x-lastx)>1.0){
             if(monOrientationListener!=null){
                 monOrientationListener.onOrientationChanged(x);
             }
            }
            lastx=x;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface onOrientationListener{
         void onOrientationChanged(float x);
    }
}
