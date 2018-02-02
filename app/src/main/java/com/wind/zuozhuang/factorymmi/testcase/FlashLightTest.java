package com.wind.zuozhuang.factorymmi.testcase;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

/**
 * Created by zuozhuang on 2017/11/8.
 */
public class FlashLightTest {
    private static FlashLightTest[] mInstance = {null,null,null};
    private int mId = 0;
    private Camera mCamera = null;
    private Camera.Parameters mParameters;

    private FlashLightTest(int id){
        mId = id;
        mCamera = Camera.open(id);
    }

    public static FlashLightTest getInstance(int id){
        if (mInstance[id]==null){
            mInstance[id] = new FlashLightTest(id);
        }

        return mInstance[id];
    }

    public void test(String value ,Handler handler){

        if("1".equals(value)){
            if(mCamera == null){
                mCamera = Camera.open(mId);
            }
            mCamera.startPreview();
            mParameters = mCamera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(mParameters);
            String s = "闪光灯"+mId+"已打开";
            Message ms =Message.obtain();
            ms.obj = s;
            handler.sendMessage(ms);
        }else {
            if (mCamera!=null){
                mParameters = mCamera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(mParameters);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                String s = "闪光灯已关闭";
                Message ms =Message.obtain();
                ms.obj = s;
                handler.sendMessage(ms);
            }
        }

    }


}
