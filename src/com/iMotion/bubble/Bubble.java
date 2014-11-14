package com.iMotion.bubble;

import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;

import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.iMotion.Login;
import com.iMotion.MapView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 逢双 on 14-2-10.
 */
public class Bubble {

    private String user;
    private Emotion emotion;
    private String message;
    private String location;
    private String time;
    private double latitude;
    private double longitude;
    private boolean visible = true;
    private OverlayItem overlayItem = null;

    public Bubble(String message, Emotion emotion, String location, double latitude, double longitude){
        this.user = Login.USER;
        this.emotion = emotion;
        this.time = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis()));
        this.message = message;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Bubble(String message, Emotion emotion, String location, double latitude, double longitude, String time){
        this.user = Login.USER;
        this.emotion = emotion;
        this.time = time;
        this.message = message;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public OverlayItem getOverlayItem(){
        overlayItem = new OverlayItem(new GeoPoint((int)(latitude * 1e6), (int)(longitude * 1e6)), user + time, "");
        overlayItem.setMarker(getBubbleImage(getMessageLength()));
        return overlayItem;
    }

    public int getMessageLength(){
        double length = 0;
        for (int i = 0; i < message.length(); i++) {
            if(message.charAt(i) >= 32 && message.charAt(i) < 128){
                length += 1;
            }
            else {
                length += 2;
            }
        }
        return (int)length;
    }

    public BitmapDrawable getBubbleImage(int length){
        Bitmap[] bubblePiece = MapView.bubblePiece;
        DisplayMetrics dm = MapView.dm;
        int center_number = length / 2;
        int width = bubblePiece[0].getWidth() + bubblePiece[1].getWidth() * center_number + bubblePiece[2].getWidth();
        int height = bubblePiece[0].getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bubblePiece[0], 0, 0, null);
        for (int i = 0; i < center_number; i++) {
            canvas.drawBitmap(bubblePiece[1], bubblePiece[0].getWidth() + bubblePiece[1].getWidth() * i, 0, null);
        }
        canvas.drawBitmap(bubblePiece[2], width - bubblePiece[2].getWidth(), 0, null);

        Paint paint = new Paint();
        String familyName ="宋体";
        Typeface font = Typeface.create(familyName,Typeface.NORMAL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(font);
        paint.setTextSize((float)(13 * dm.widthPixels/320));
        canvas.drawText(message, (float)(8 * dm.widthPixels/320), (float)(12 * dm.widthPixels/320), paint);
        return new BitmapDrawable(result);
    }

    public String getMessage() {
        return message;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public String getTime() {
        return time;
    }
}
