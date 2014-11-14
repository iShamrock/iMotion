package com.iMotion.location;

/**
 * Created by 逢双 on 14-2-14.
 */
public class Google_location {
    /*初始化locationManager*/
        /*locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0.000001f, locationListener);
*/
        /*Drawer*/
        /*// Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());*/



       /* public void getMyLoc() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        Location Location_Geo = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        if(Location_Geo == null){
            Toast.makeText(MapView.this, "没有GPS信号", Toast.LENGTH_SHORT).show();
            currentGeoPoint = new GeoPoint((int)(31.3 * 1E6), (int)(121.5* 1E6));
            currentGeoPoint = new GeoPoint((int)(0 * 1E6), (int)(0* 1E6));
        }
        else {
            currentGeoPoint = new GeoPoint((int)(Location_Geo.getLatitude() * 1E6), (int)(Location_Geo.getLongitude() * 1E6));
            Toast.makeText(MapView.this, "" + (Location_Geo.getLatitude()) + "  "  + (Location_Geo.getLongitude()), Toast.LENGTH_SHORT).show();
            System.out.println("" + (int)(Location_Geo.getLatitude()) + "  "  + (int)(Location_Geo.getLongitude()));
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location Location_Geo) {
            getMyLoc();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
*/
}
