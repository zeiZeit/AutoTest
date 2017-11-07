package com.wind.zuozhuang.factorymmi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.wind.zuozhuang.factorymmi.testcase.AudioLoopTest;
import com.wind.zuozhuang.factorymmi.testcase.CameraTest;
import com.wind.zuozhuang.factorymmi.testcase.RecordTest;
import com.wind.zuozhuang.factorymmi.testcase.SimCardTest;

public class MainActivity extends AppCompatActivity{

    private TextView tv;
    private SurfaceView mSurface;
    Context mContext;



    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv.setText(msg.obj.toString().trim());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        tv = (TextView) findViewById(R.id.tv);
        mSurface = new SurfaceView(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

    }



    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_startService:
                break;
            case R.id.bt_startBackTest:
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
                RecordTest.getInstance().startRecord();
                break;
            case R.id.bt_stopRecord:
                RecordTest.getInstance().stopRecord();
                break;
            case R.id.bt_playRecord:
                RecordTest.getInstance().startPlay();
                break;
            default:
                break;
        }
    }
}
