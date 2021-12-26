package com.nineone.verificationcode.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class LocationService extends Service {
    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("MissingPermission")
    private void init() {
        //1.获取位置的管理者
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //2.获取定位方式
        //2.1获取所有的定位方式，true:表示返回所有可用定位方式
        List<String> providers = locationManager.getProviders(false);
        for (String string : providers) {
            Log.e("string", "===" + string);
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置定位精准度
        criteria.setAltitudeRequired(false);
        //是否要求海拔
        criteria.setBearingRequired(true);
        //是否要求方向
        criteria.setCostAllowed(true);
        //是否要求收费
        criteria.setSpeedRequired(true);
        //是否要求速度
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        //设置电池耗电要求
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        //设置方向精确度
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
        //设置速度精确度
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        //设置水平方向精确度
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        //设置垂直方向精确度

//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                for (String s:intent.getExtras().keySet()){
//                    Log.e("onReceive", "====" +s+"   "+intent.getExtras().get(s) );
//
//                }
//
//            }
//        }, new IntentFilter("test"));

        String provider = locationManager.getBestProvider(criteria, true);
        Log.e("provider", "===" + provider+"    "+locationManager.isProviderEnabled(provider));
//        Intent intent = new Intent();
//        intent.setAction("test");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, pendingIntent);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(LocationService.this, "定位成功::   " + location.getLatitude(), Toast.LENGTH_SHORT).show();
                Log.e("onLocationChanged", "===" + location.getLatitude() + "    " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e("onStatusChanged", "===");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e("onProviderEnabled", "===" + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.e("onProviderDisabled", "===" + provider);
            }
        });

    }
}
