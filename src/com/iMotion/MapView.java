package com.iMotion;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.iMotion.AR.ARActivity;
import com.iMotion.bubble.BubbleManager;
import com.iMotion.location.Location_Geo;

import java.util.ArrayList;

/**
 * Created by 逢双 on 14-1-30.
 */
public class MapView extends Activity {

    public static DemoApplication app;
    private com.baidu.mapapi.map.MapView mMapView = null;
    private MapController mMapController = null;
    MKMapViewListener mMapListener = null;
    MKMapTouchListener mapTouchListener = null;
    LocationClient mLocClient;
    public static LocationData locData = new LocationData();
    public MyLocationListener myListener = new MyLocationListener();
    boolean isRequest = false;//是否手动触发请求定位
    boolean isFirstLoc = true;//是否首次定位

    GeoPoint currentGeoPoint;
    TextOverlay textOverlay;
    public static BubbleManager bubbleManager = new BubbleManager();

    /*NavigationDrawer*/
    private String[] mTitles;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle toggle;

    /*Overlay*/
    private MyOverlay mOverlay = null;
    private ArrayList<OverlayItem> mItems = null;
    private OverlayItem mCurItem = null;
    public static Bitmap[] bubblePiece = new Bitmap[3];
    public static DisplayMetrics dm;

    Connect connect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*初始化mBMapManager*/
        initMapManager();

        /*布局*/
        setContentView(R.layout.mapview);

        /*初始化NavigationDrawer*/
        initNavigationDrawer();

        /*初始化地图设置*/
        initMapView();

        /*初始化地图事件监听*/
        initListener();

        /*初始化全局定位模块*/
        Location_Geo.init();

        /*初始化本类中定位模块*/
        initLocation();

        /*初始化Bubble图层*/
        initBubbleOverlay();

        /*联网获取信息*/
        initInformation();

//        bubbleManager.loadBubble();暂时搁置的读取本地文件


        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close){
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(toggle);

    }


    private void initInformation() {
        mOverlay = new MapView.MyOverlay(getResources().getDrawable(R.drawable.bubble), mMapView);
        mMapView.getOverlays().add(mOverlay);
        connect = new Connect(mMapView, mOverlay, this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                connect.getBubble();
                mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.bubble), mMapView);
                mMapView.getOverlays().add(mOverlay);
                for (int i = 0; i < bubbleManager.getTotalNumber(); i++) {
                    mOverlay.addItem(bubbleManager.getBubbleArray().get(i).getOverlayItem());
                }

                //执行地图刷新使生效
                mMapView.refresh();
            }
        }).start();
    }

    private void initBubbleOverlay() {
        mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.icon_marka), mMapView);
        bubblePiece[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_left);
        bubblePiece[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_center);
        bubblePiece[2] = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_right);
        dm = getResources().getDisplayMetrics();
        //    Toast.makeText(this, "screan width:"  + dm.widthPixels+ "", Toast.LENGTH_SHORT).show();
    }

    private void initNavigationDrawer() {
        mTitles = new String[]{"POST MY MOOD","IN MY SCOPE","AROUND ME","MOOD BOX","LEAVE"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mTitles));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                isRequest = true;
                mLocClient.requestLocation();
                break;
            case R.id.new_status:
                Intent intent = new Intent();
                intent.setClass(MapView.this, BubbleAdd.class);
                intent.putExtra("locationX", (int) (locData.latitude * 1e6));
                intent.putExtra("locationY", (int) (locData.longitude * 1e6));
                startActivity(intent);
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void selectItem(int position) {
        if (position == 0) {
            Intent intent = new Intent();
            intent.setClass(MapView.this, BubbleAdd.class);
            intent.putExtra("locationX", (int) (locData.latitude * 1e6));
            intent.putExtra("locationY", (int) (locData.longitude * 1e6));
            startActivity(intent);
        } else if (position == 2) {
            Intent intent = new Intent();
            intent.setClass(MapView.this, NearbyDiscuss.class);
            startActivity(intent);
        } else if (position == 3) {
            Intent intent = new Intent();
            intent.setClass(MapView.this, EmotionBox.class);
            startActivity(intent);
//        } else if (position == 3) {
//            isRequest = true;
//            mLocClient.requestLocation();
        } else if (position == 1) {
            Intent intent = new Intent();
            intent.setClass(MapView.this, ARActivity.class);
            startActivity(intent);
        }
        else if (position==4){
            finish();
        }
//        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void initMapManager() {
        app = (DemoApplication) this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(DemoApplication.strKey, new DemoApplication.MyGeneralListener());
        }
    }

    private void initMapView() {
        mMapView = (com.baidu.mapapi.map.MapView) findViewById(R.id.simpleMapView);
        //    mMapView.setSatellite(true);
        mMapController = mMapView.getController();
        mMapController.enableClick(true);
        mMapController.setZoom(30);

        currentGeoPoint = new GeoPoint((int) (31.3 * 1E6), (int) (121.51 * 1E6));
        mMapController.setCenter(currentGeoPoint);
        bubbleManager = new BubbleManager();
    }

    private void initLocation() {
        //定位初始化
        mLocClient = new LocationClient(getApplicationContext());
        locData = new LocationData();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(1000);
        option.setPriority(LocationClientOption.NetWorkFirst);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mLocClient.requestLocation();
        mMapView.refresh();
    }

    private void initListener() {
        /**
         * 设置地图点击事件监听
         */
        mapTouchListener = new MKMapTouchListener() {
            @Override
            public void onMapClick(GeoPoint point) {
            }

            @Override
            public void onMapDoubleClick(GeoPoint point) {
            }

            @Override
            public void onMapLongClick(GeoPoint point) {
                currentGeoPoint = point;
                Intent intent = new Intent();
                intent.setClass(MapView.this, BubbleAdd.class);
                intent.putExtra("locationX", currentGeoPoint.getLatitudeE6());
                intent.putExtra("locationY", currentGeoPoint.getLongitudeE6());
                startActivity(intent);
            }
        };
        mMapView.regMapTouchListner(mapTouchListener);
        /**
         * 设置地图事件监听
         */
        mMapListener = new MKMapViewListener() {
            @Override
            public void onMapMoveFinish() {
            }

            @Override
            public void onClickMapPoi(MapPoi mapPoiInfo) {
                String title = "";
                if (mapPoiInfo != null) {
                    title = mapPoiInfo.strText;
                    Toast.makeText(MapView.this, title, Toast.LENGTH_SHORT).show();
                    mMapController.animateTo(mapPoiInfo.geoPt);
                }
            }

            @Override
            public void onGetCurrentMap(Bitmap b) {
            }

            @Override
            public void onMapAnimationFinish() {
            }

            @Override
            public void onMapLoadFinish() {
            }
        };
        mMapView.regMapViewListener(DemoApplication.getInstance().mBMapManager, mMapListener);
    }

    @Override
    protected void onDestroy() {
        //退出时销毁定位
        Location_Geo.timer.cancel();
        if (mLocClient != null)
            mLocClient.stop();
        //    locData = null;
        mMapView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                bubbleManager.statusArray = Connect.refreshStatus();
            }
        }).start();

        mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.bubble), mMapView);
        mMapView.getOverlays().add(mOverlay);
        for (int i = 0; i < bubbleManager.getTotalNumber(); i++) {
            mOverlay.addItem(bubbleManager.getBubbleArray().get(i).getOverlayItem());
        }
        //执行地图刷新使生效
        mMapView.refresh();

//        bubbleManager.saveBubble();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            //如果不显示定位精度圈，将accuracy赋值为0即可
            locData.accuracy = location.getRadius();
            // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
            locData.direction = location.getDerect();
            //更新图层数据执行刷新后生效
            mMapView.refresh();
            //是手动触发请求或首次定位时，移动到定位点
            if (isRequest || isFirstLoc) {
                //移动地图到定位点
                Log.d("LocationOverlay", "receive Location_Geo, animate to it");
                mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));
                isRequest = false;
            }
            //首次定位完成
            isFirstLoc = false;
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public class MyOverlay extends ItemizedOverlay {

        public MyOverlay(Drawable defaultMarker, com.baidu.mapapi.map.MapView mapView) {
            super(defaultMarker, mapView);
        }


        @Override
        public boolean onTap(int index) {
            /*OverlayItem item = getItem(index);
            mCurItem = item ;*/
            Intent intent = new Intent();
            intent.setClass(MapView.this, Discussion.class);
            intent.putExtra("position", index);
            startActivity(intent);
            return true;
        }

        @Override
        public boolean onTap(GeoPoint pt, com.baidu.mapapi.map.MapView mMapView) {
            return false;
        }

    }
}
