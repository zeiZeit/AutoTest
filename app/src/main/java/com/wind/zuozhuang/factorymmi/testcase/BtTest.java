package com.wind.zuozhuang.factorymmi.testcase;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * Created by zuozhuang on 2017/11/14.
 */
public class BtTest {


    private static BtTest mInstance = null;
    private boolean mIsTesting = false;
    private Context mContext = null;
    private Handler mHandler = null;
    private BluetoothAdapter mAdapter = null;

    private BtTest(Context context){
        mContext = context;
    }

    public static BtTest getInstance(Context context){
        if(mInstance == null){
            mInstance = new BtTest(context);
        }
        return mInstance;
    }

    private final Thread mThread = new Thread(){
      public void run(){
          mContext.unregisterReceiver(mReceiver);
          mIsTesting = false;
          mAdapter.disable();
      }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("zeizeit", "onReceive: comein 11111111");
            if(intent != null){
                Log.i("zeizeit", "onReceive: intent.action:"+intent.getAction());
                if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.i("zeizeit", "deviceName:  "+device.getName());
                    if (device.getBondState()!=BluetoothDevice.BOND_BONDED){
                        mHandler.removeCallbacks(mThread);
                        Log.i("zeizeit", "deviceName:  "+device.getName());
                        String name = device.getName();
                        Message ms = Message.obtain();
                        ms.obj= name;
                        mHandler.sendMessage(ms);
                        mAdapter.disable();
                        context.unregisterReceiver(mReceiver);
                        mIsTesting = false;

                    }
                }else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){
                    Log.i("zeizeit", "onReceive: 2222222222");
                    if (BluetoothAdapter.STATE_ON == intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.STATE_OFF)){
                        if(mAdapter.isDiscovering()){
                            mAdapter.cancelDiscovery();
                        }
                        mAdapter.startDiscovery();
                    }
                }
            }
        }
    };

    public void test(Context context, int delayTime ,Handler handler){
        if(!mIsTesting){
            mIsTesting = true;
            mContext = context;
            mHandler = handler;
            Log.i("zeizeit", "test: comein 1111111");
            mAdapter = BluetoothAdapter.getDefaultAdapter();
            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mReceiver,filter);
            if(!mAdapter.isEnabled()){
                mAdapter.enable();
            }else if (mAdapter.getState()==BluetoothAdapter.STATE_ON){
                if(mAdapter.isDiscovering()){
                    mAdapter.cancelDiscovery();
                }

                mAdapter.startDiscovery();
            }

            handler.postDelayed(mThread,delayTime*1000);
        }
    }
}
