package com.wind.zuozhuang.factorymmi;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.Random;

/**
 * Created by zuozhuang on 2017/12/16.
 */
public class OpenAtci extends Service{

    private static int mRandom;

    private class Mybinder extends IOpenAtci.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getCode() throws RemoteException {
            return getRandomCode();
        }

        @Override
        public String returnCode(String code) throws RemoteException {
            return returnAction(code);
        }

    }

    private String returnAction(String code) {
        Log.i("zeit_openatci", "there is in returnAction() and in ="+code);
        //把code解密看和mRandom是不是一样的，如果一样，那么就返回真正的打开atci的action。
        if (code.equals(mRandom+"")){
            return "com.wind.intent.action.ATCI_OPEN";
        }else {
            return null;
        }

    }

    private int getRandomCode() {
        Random RandNum= new Random();
        mRandom = RandNum.nextInt(1000);
        Log.i("zeit_openatci", "there is in getRandomCode() and out ="+mRandom);
        return mRandom;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Mybinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("zeit_openatci", "there is in onCreate() ");
    }


}
