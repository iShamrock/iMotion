package com.iMotion.AR;

import android.util.DisplayMetrics;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * Created by 逢双 on 14-2-22.
 */
public class Angle {
    private boolean show;
    private double angle;
    private double oriental;
    private double xPercent;
    private double height;
    private double dAngle;
    private GeoPoint here;
    private GeoPoint there;
    private double distance;
    private DisplayMetrics dm = ARActivity.dm;

    public Angle(double oriental, GeoPoint here, GeoPoint there){
//        System.out.println("oriental is :" + oriental);
        this.oriental = oriental;
        this.here = here;
        this.there = there;
//        height = Math.random() * dm.heightPixels * 0.8 + dm.heightPixels * 0.1;
        calculateAngle();
        calculateDAngle();
        toShow();
        calculateXPercent();
        calculateDistance();
        height = Math.log10(distance) * dm.heightPixels / 7;
    }

    private void calculateAngle(){
        int dLatitude = there.getLatitudeE6() - here.getLatitudeE6();
        int dLongitude = there.getLongitudeE6() - here.getLongitudeE6();
        angle = Math.atan((double)dLongitude / (double)dLatitude);
        if(dLatitude < 0){
            angle += Math.PI;
        }
//        System.out.println("angle is :" + angle);
    }

    private void calculateDAngle(){
        double a = angle - oriental;
        double b = angle - oriental + 2 * Math.PI;
        double c = angle - oriental - 2 * Math.PI;
        if(Math.abs(a) < Math.abs(b) && Math.abs(a) < Math.abs(b)){
            dAngle = a;
        }
        else if(Math.abs(b) < Math.abs(a) && Math.abs(b) < Math.abs(c)){
            dAngle = b;
        }
        else {
            dAngle = c;
        }
//        System.out.println("dAngle is :" + dAngle);
    }

    private void toShow(){
        if(Math.abs(dAngle) * 2 < Math.PI){
            show = true;
        }
    }

    private void calculateXPercent(){
        xPercent = dAngle / Math.PI + 0.5;
    }

    private void calculateDistance(){
        distance = Math.pow(Math.pow(here.getLatitudeE6() - there.getLatitudeE6(), 2) + Math.pow(here.getLongitudeE6() - there.getLongitudeE6(), 2), 0.5);
    }

    public void reset(double oriental, GeoPoint here){
        this.oriental = oriental;
        this.here = here;
        calculateAngle();
        calculateDAngle();
        toShow();
        calculateXPercent();
        calculateDistance();
        height = Math.log10(distance) * dm.heightPixels / 7;
    }

    public boolean isShow() {
        return show;
    }

    public double getAngle() {
        return angle;
    }

    public double getOriental() {
        return oriental;
    }

    public double getxPercent() {
        return xPercent;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }
}
