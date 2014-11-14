package com.iMotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.iMotion.bubble.Bubble;
import com.iMotion.bubble.Emotion;
import com.iMotion.database.Status;
import com.iMotion.location.Location_Geo;

/**
 * Created by 逢双 on 14-2-2.
 */
public class BubbleAdd extends Activity{


    EditText location;
    EditText message;
    EditText emotion;
    Button giveUp;
    Button broadcast;
    Intent intent;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bubbleadd);

        //获取intent
        intent = getIntent();
        latitude = intent.getIntExtra("locationX", 0) * 1E-6;
        longitude = intent.getIntExtra("locationY", 0) * 1E-6;
        location = (EditText)findViewById(R.id.bubbleAdd_location_editText);
        GeoPoint point = new GeoPoint((int)(latitude * 1E6), (int)(longitude* 1E6));
        Location_Geo.setBubbleLocation(location, point);



        //获取控件
        message = (EditText)findViewById(R.id.bubbleAdd_text_editText);
        emotion = (EditText)findViewById(R.id.emotion_bubbleAdd_editText);
        giveUp=(Button)findViewById(R.id.giveup);
        giveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        broadcast = (Button)findViewById(R.id.bubbleAdd_broadcast_button);

        broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean add = Connect.addBubble(new Status(message.getText().toString(), "lalala",
                                Login.USER, latitude, longitude, location.getText().toString()));
                        MapView.bubbleManager.addBubble(new Bubble(message.getText().toString(),
                                new Emotion(emotion.getText().toString()), location.getText().toString(), latitude, longitude));
                        finish();
                    }
                }).start();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
