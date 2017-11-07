package com.wind.zuozhuang.factorymmi.testcase;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by zuozhuang on 2017/7/28.
 */
public class CameraTest {

    private static  CameraTest mInstance[] = {null,null,null};
    private int cId;
    private String mName = null;
    private boolean isRunning = false;
    private Handler mHandler;

    private CameraTest(int cId) {
        this.cId = cId;
    }

    public static CameraTest getInstance (int id)  //多例模式
    {
        if (mInstance[id] == null)
        {
            mInstance[id] = new CameraTest(id);
        }

        return mInstance[id];

    }

    public void test(String name, SurfaceView surface, boolean testWhiteCard, Handler handler)
    {   mHandler = handler;
        mName =name;
        Camera cam =null;
        if(!isRunning)
        {
            cam=Camera.open(cId);
            if (cam !=null)
            {
                isRunning = true;
            }else
            {
                Log.i("zuozhuang","camera can not open!");
            }
        }else
        {
            Log.i("zuozhuang","camera is running!");
            return;
        }
        try {
            cam.setPreviewDisplay(surface.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Camera.Parameters parameters = cam.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG); //设置图片格式，PixelFormat是像素格式的意思
        parameters.setJpegQuality(100);//设置质量
        List<Camera.Size> lis_size = parameters.getSupportedPictureSizes();//得到所有支持图片的size.
        Camera.Size size = lis_size.get(lis_size.size()-1);//拿到最后一个size
        parameters.setPictureSize(size.width,size.height);
        if(cId == Camera.CameraInfo.CAMERA_FACING_BACK)
        {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//这是对焦方式
        }
        cam.setParameters(parameters);
        cam.startPreview();//这个很重要，更新预览
        if(cId== Camera.CameraInfo.CAMERA_FACING_BACK)
        {
            if(testWhiteCard)
            {
                takePic(cam);
            }else
            {
                cam.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean b, Camera camera) {
                        if(b) {
                            takePic(camera);
                        }
                        else
                        {
                            camera.stopPreview();
                            camera.release();
                            isRunning = false;
                        }
                    }
                });

                //takePic(cam);
            }
        }
        else
        {
            takePic(cam);
        }

    }

    private void takePic(Camera cam)
    {
        cam.takePicture(null,null, new MyPictureCallback());
    }

    private final class MyPictureCallback implements Camera.PictureCallback
    {

        public void onPictureTaken(byte[] data, Camera camera)
        {
            String s;
            camera.stopPreview();
            camera.release();
            isRunning = false;

            File extDir = Environment.getExternalStorageDirectory();
            String filename = mName;
            File fullFilename = new File(extDir, filename);
            Log.i("zz", "onPictureTaken: "+fullFilename.toString());
            try {

                fullFilename.createNewFile();
                fullFilename.setWritable(Boolean.TRUE);

                if (!fullFilename.exists())
                {
                    File dir = new File(mName.substring(0,mName.lastIndexOf("/")));
                    if (!dir.exists())
                    {
                        dir.mkdir();
                    }
                }
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fullFilename));

                bos.write(data);
                bos.flush();
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                s = "拍照失败，路径错误";
                Message ms = Message.obtain();
                ms.obj = s;
                mHandler.sendMessage(ms);
            } catch (IOException e) {
                e.printStackTrace();
                s = "拍照失败，数据传输异常";
                Message ms = Message.obtain();
                ms.obj = s;
                mHandler.sendMessage(ms);
            }finally {
                s = "拍照成功，存储路径为"+fullFilename.toString();
                Message ms = Message.obtain();
                ms.obj = s;
                mHandler.sendMessage(ms);
            }
        }

    }


}
