package com.wind.zuozhuang.factorymmi.testcase;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Iterator;

/**
 * Created by zuozhuang on 2017/11/15.
 */
public class GpsTest {
    private LocationManager locationManager;
    private static GpsTest mInstance = null;
    private Context mContext = null;
    private boolean mIsTesting = false;
    private Handler mHandler;

    private GpsTest() {

    }

    public static GpsTest getInstance() {
        if (mInstance == null) {
            mInstance = new GpsTest();
        }
        return mInstance;
    }

    public void test(final Context context, Handler handler) {
        Log.i("zeizeit", "Gps.test() come in ");
        Log.i("zeizeit", "test: ... mIsTesting:" + mIsTesting);
        if (!mIsTesting) {
            mIsTesting = true;
            mContext = context;
            mHandler = handler;
            handler.post(new Thread() {
                public void run() {
                    locationManager = (LocationManager) mContext.getSystemService(context.LOCATION_SERVICE);
                    Log.i("zeizeit", "run: gps test... ");
                    if (!locationManager.isProviderEnabled((LocationManager.GPS_PROVIDER))) {
                        try {
                            Settings.Secure.setLocationProviderEnabled(mContext.getContentResolver(), LocationManager.GPS_PROVIDER, true);
                        }catch (java.lang.SecurityException e){
                            Message msg = Message.obtain();
                            String num = "权限被拒绝，需要成为系统应用，或者需要系统的以下权限："+"\n" +
                                    "android.permission.WRITE_SECURE_SETTINGS";
                            msg.obj = num;
                            mHandler.sendMessage(msg);
                        }

                        Log.i("zeizeit", "run: set gps enable!");
                    }
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
                    locationManager.addGpsStatusListener(mSvStatusListener);

                }

            });

        }
    }

    private GpsStatus.Listener mSvStatusListener = new GpsStatus.Listener() {

        @Override
        public void onGpsStatusChanged(int event) {
            GpsStatus gs = locationManager.getGpsStatus(null);
            Iterator<GpsSatellite> satellites = gs.getSatellites().iterator();
            int n = 0;
            Log.i("zeizeit", "onGpsStatusChanged: ...");
            while (satellites.hasNext()) {
                satellites.next();
                n++;
                Log.i("zeieit", "gps number is :" + n);
                if (n >= 3) {
                    locationManager.removeGpsStatusListener(mSvStatusListener);
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.removeUpdates(mLocationListener);
                    Settings.Secure.setLocationProviderEnabled(mContext.getContentResolver(),LocationManager.GPS_PROVIDER,false);
                    mIsTesting = false;
                    Log.i("zeizeit", "Gps test success ");
                    break;
                }
            }
            Message msg = Message.obtain();
            String num = "发现"+n+"个卫星";
            msg.obj = num;
            mHandler.sendMessage(msg);
        }
    };

    private LocationListener mLocationListener = new LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            Log.i("zeizeit", "onLocationChanged:     l:"+location.getLatitude()+"lg:"+location.getLongitude());
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
}
