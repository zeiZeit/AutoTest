package com.wind.zuozhuang.factorymmi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by zuozhuang on 2017/12/18.
 */
public class TestService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("zeit", "in testService onCreate()");
        super.onCreate();
    }
}
