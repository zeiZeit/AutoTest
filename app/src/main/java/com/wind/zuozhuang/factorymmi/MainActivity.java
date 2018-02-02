package com.wind.zuozhuang.factorymmi;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.wind.zuozhuang.factorymmi.testcase.AudioLoopTest;
import com.wind.zuozhuang.factorymmi.testcase.BtTest;
import com.wind.zuozhuang.factorymmi.testcase.CameraTest;
import com.wind.zuozhuang.factorymmi.testcase.FlashLightTest;
import com.wind.zuozhuang.factorymmi.testcase.GpsTest;
import com.wind.zuozhuang.factorymmi.testcase.RecordTest;
import com.wind.zuozhuang.factorymmi.testcase.SimCardTest;
import com.wind.zuozhuang.factorymmi.testcase.WifiTest;

public class MainActivity extends Activity implements View.OnClickListener{
    private final String TAG = "MainActivity.zeit";
    private TextView tv;
    private SurfaceView mSurface;
    Context mContext;



    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj!=null){
                tv.setText(msg.obj.toString().trim());
            }else {
                tv.setText("返回值为空");
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        tv = (TextView) findViewById(R.id.tv);
        mSurface = new SurfaceView(this);
        //申请相机运行时权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        //申请录音运行时权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        //申请存储运行时权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        //申请wifi运行时权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        //申请蓝牙运行时权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_startService:
                //int emodeled =Settings.System.getInt(getContentResolver(),"EmodeLedTest",-2);
                Intent intentt = new Intent(MainActivity.this,TestService.class);

                startService(intentt);
                Log.i("zuozhuang", "onClick: emodeLed:");
                break;
            case R.id.bt_startBackTest:
                Intent intent = new Intent();

                //intent.setPackage("com.wind.factorybackgroundtest");
                intent.setComponent(new ComponentName("com.wind.zuozhuang.factorymmi","com.wind.zuozhuang.factorymmi.IOpenAtci"));
                intent.setAction("com.wind.zuozhuang.factorymmi.IOpenAtci");

                startService(intent);
                 //Log.i(TAG, "startBackTest,intent.action:"+intent.getAction().toString());
                break;
            case R.id.bt_FrontCameraTest:
                CameraTest.getInstance(1).test("frontcamera.png",mSurface,false,mHandler);
                break;
            case R.id.bt_BackCameraTest:
                CameraTest.getInstance(0).test("backcamera.png",mSurface,false,mHandler);
                break;
            case R.id.bt_SubCameraTest:
                CameraTest.getInstance(2).test("subcamera.png",mSurface,false,mHandler);
                break;
            case R.id.bt_HeadsetMikeToReceiver:
                AudioLoopTest.Test(this,"22,1",mHandler);
                break;
            case R.id.bt_MainMicToEarphone:
                AudioLoopTest.Test(this,"21,2",mHandler);
                break;
            case R.id.bt_SubMicToEarphone:
                AudioLoopTest.Test(this,"25,2",mHandler);
                break;
            case R.id.bt_SIMCardTest:
                SimCardTest.getInstance().test(this,mHandler);
                break;
            case R.id.bt_startRecord:
                RecordTest.getInstance(mContext).startRecord(mHandler);
                break;
            case R.id.bt_stopRecord:
                RecordTest.getInstance(mContext).stopRecord(mHandler);
                break;
            case R.id.bt_playRecord:
                RecordTest.getInstance(mContext).startPlay(mHandler);
                break;
            case R.id.bt_Flash1Test:
                FlashLightTest.getInstance(0).test("1",mHandler);
                break;
            case R.id.bt_Flash2Test:
                FlashLightTest.getInstance(2).test("1",mHandler);
                break;
            case R.id.bt_CloseFlash:
                FlashLightTest.getInstance(0).test("0",mHandler);
                FlashLightTest.getInstance(2).test("0",mHandler);
                break;
            case R.id.bt_WiFiTest:
                WifiTest.getInstance(mContext).test(mContext,mHandler);
                break;
            case R.id.bt_BlueToothTest:
                BtTest.getInstance(mContext).test(mContext,50,mHandler);
                break;
            case R.id.bt_GpsTest:
                GpsTest.getInstance().test(mContext,mHandler);

                break;

            default:
                break;
        }
    }
}
