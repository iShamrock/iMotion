package com.iMotion.location;

import android.widget.EditText;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.iMotion.DemoApplication;
import com.iMotion.MapView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 逢双 on 14-2-14.
 */
public class Location_Geo {
    public static DemoApplication app = MapView.app;
    public static MKSearch mSearch = null;
    public static String location;
    public static LocationData locData;
    public static GeoPoint point;
    private static EditText bubbleLocation;
    private static int condition = -1;
    public static Timer timer;

    public static void init(){
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener() {
            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
            }

            public void onGetAddrResult(MKAddrInfo res, int error) {
                if (error != 0) {
                    String str = String.format("错误号：%d", error);
                    return;
                }

                if (res.type == MKAddrInfo.MK_GEOCODE) {
                    //地理编码：通过地址检索坐标点
                    point = new GeoPoint(res.geoPt.getLatitudeE6(), res.geoPt.getLongitudeE6());
                }
                if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
                    //反地理编码：通过坐标点检索详细地址及周边poi
                    location = res.strAddr;
                    if(condition == 0){
                        bubbleLocation.setText(location);
                        condition = -1;
                    }
                }
            }

            public void onGetPoiResult(MKPoiResult res, int type, int error) {

            }

            public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
            }

            public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
            }

            public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
            }

            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            }

            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
            }

            @Override
            public void onGetShareUrlResult(MKShareUrlResult result, int type,
                                            int error) {
                // TODO Auto-generated method stub

            }
        });
        timer = new Timer();
        TimerTask timerTask = new TimerTask(){
            public void run() {
                locData = MapView.locData;
                mSearch.reverseGeocode(new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));
            }
        };
        timer.schedule(timerTask, 0, 5000);
    }

    public static String getLocation(){
        return location;
    }

    public static void setBubbleLocation(EditText bubbleLocation1, GeoPoint point){
        bubbleLocation = bubbleLocation1;
        condition = 0;
        mSearch.reverseGeocode(point);
    }

    public static GeoPoint getCurrentGeoPoint(){
        return new GeoPoint((int)(locData.latitude * 1e6), (int)(locData.longitude * 1e6));
    }
}
