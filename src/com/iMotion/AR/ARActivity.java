package com.iMotion.AR;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.iMotion.Discussion;
import com.iMotion.MapView;
import com.iMotion.R;
import com.iMotion.bubble.Bubble;
import com.iMotion.bubble.BubbleManager;
import com.iMotion.location.Location_Geo;

import java.util.ArrayList;

/**
 * Created by 逢双 on 14-2-22.
 */
public class ARActivity extends Activity {

    GeoPoint currentGeoPoint;
    CameraPreview cameraPreview;
    SensorManager sensorManager;
    Sensor orientalSensor;
    BubbleManager bubbleManager;
    ARTextView arTextView;
    Context context;
    public static DisplayMetrics dm;
    public static ArrayList<Angle> angleArray;
    float values[] = new float[3];
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                arTextView.invalidate();
            } else {
                Intent intent = new Intent(context, Discussion.class);
                intent.putExtra("position", msg.what);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        bubbleManager = MapView.bubbleManager;
        dm = getResources().getDisplayMetrics();
        currentGeoPoint = Location_Geo.getCurrentGeoPoint();
        cameraPreview = new CameraPreview(this);
        this.context = this;
        setContentView(cameraPreview);
        initOrientalSensor();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAngles();
                addARTextView();
                while (true) {
                    try {
                        Thread.sleep(1000);
                        for (int i = 0; i < angleArray.size(); i++) {
                            angleArray.get(i).reset((values[0] * Math.PI / 180), currentGeoPoint);
                        }
                        handler.sendEmptyMessage(-1);
                    } catch (Exception e) {
                        System.out.println("eeee");
                    }
                }
            }
        }).start();
    }

    private void addARTextView() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        arTextView = new ARTextView(this, this);
        addContentView(arTextView, layoutParams);
    }

    private void getAngles() {
        double angle = values[0] * Math.PI / 180;
        ArrayList<Bubble> bubbleArray = bubbleManager.getBubbleArray();
        angleArray = new ArrayList<Angle>();
        for (int i = 0; i < bubbleArray.size(); i++) {
            GeoPoint point = new GeoPoint((int) (bubbleArray.get(i).getLatitude() * 1E6), (int) (bubbleArray.get(i).getLongitude() * 1E6));
            angleArray.add(new Angle(
                    angle,
                    currentGeoPoint,
                    point));
        }
    }

    private void initOrientalSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        orientalSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(sensorEventListener, orientalSensor, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            values = event.values;
        /*    for (int i = 0; i < angleArray.size(); i++) {
                angleArray.get(i).reset((double)values[0], currentGeoPoint);
            }*/
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
