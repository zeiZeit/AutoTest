package com.wind.zuozhuang.factorymmi.testcase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.List;

/**
 * Created by zuozhuang on 2017/11/8.
 */
public class WifiTest {
    private WifiManager mWm = null;
    private boolean mWifiEnable = false;
    private boolean mIsTesting ;
    private Context mContext = null;
    private Handler mHandler = null;
    private static WifiTest mInstance = null;


    private WifiTest(Context context){
        mWm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mIsTesting = false;
    }

    public static WifiTest getInstance(Context context){
        if(mInstance == null){
            mInstance = new WifiTest(context);
        }
        return mInstance;
    }


    public void test(Context context,Handler handler){
        mContext = context;
        mHandler = handler;
        //initReciever();
        if(!mIsTesting){
            mIsTesting = true;
            mWifiEnable = mWm.isWifiEnabled();
            if (!mWifiEnable){
                mWm.setWifiEnabled(true);
            }
        }
        mHandler.postDelayed(mThread,3000);

    }

    private void initReciever() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mContext.registerReceiver(mReciever,intentFilter);

    }

    private final BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)){
                int state = intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);
                if (state==WifiManager.WIFI_STATE_ENABLED){

                }
            }else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)){

            }
        }
    };


    private final Thread mThread = new Thread()
    {
        public void run(){
            countResult();
        }
    };

    List<ScanResult> list;
    StringBuilder mDeviceList;
    int size;

    private void countResult(){
        mDeviceList = new StringBuilder();
        mDeviceList.append("扫描到以下WiFi：");
        mDeviceList.append("\n");
        mWm.startScan();
        list = mWm.getScanResults();
        size = list.size();
        if (list == null) {
            Log.i("zz", "countResult: 获取的wifi列表为空。 ");
            if(mWm.getWifiState()==3){
                mDeviceList.append("当前区域没有无线网络");
            }else if(mWm.getWifiState()==2){
                mDeviceList.append("WiFi正在开启，请稍后重新点击扫描");
            }else{
                mDeviceList.append("WiFi没有开启，无法扫描");
            }
        }
        if(size>0){
            for (ScanResult scanResult:list){
                if (!mDeviceList.toString().contains(scanResult.SSID)){
                    mDeviceList.append(scanResult.SSID);
                    mDeviceList.append("\n");
                }
            }

            Log.i("zz", "wifiTest:"+mDeviceList.toString()+list.toString());
            Message ms =Message.obtain();
            ms.obj = mDeviceList;
            mHandler.sendMessage(ms);
            mIsTesting = false;
            mWm.setWifiEnabled(mWifiEnable);
        }else{
            mHandler.postDelayed(mThread,1000);
        }

    }
}
