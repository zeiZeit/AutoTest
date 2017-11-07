package com.wind.zuozhuang.factorymmi.testcase;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by zuozhuang on 2017/8/9.
 */
public class AudioLoopTest {

    public static void Test(Context context, String params, Handler handler)
    {
        Log.i("zz","得到了audioManager,下一步调用setparameters方法");
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Log.i("zz","得到了audioManager,下一步调用setparameters方法");

        mAudioManager.setParameters("SET_LOOPBACK_TYPE"+params);
        String s = "AudioSystem不管用，原因未知";
        Message ms =Message.obtain();
        ms.obj = s;
        handler.sendMessage(ms);
    }


}
