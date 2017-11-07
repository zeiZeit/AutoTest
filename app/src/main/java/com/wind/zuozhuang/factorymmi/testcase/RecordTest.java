package com.wind.zuozhuang.factorymmi.testcase;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by zuozhuang on 2017/11/3.
 */
public class RecordTest {

    private static RecordTest mRecordTest = null;
    private final String mPath = "/sdcard/recordtest.amr";
    private boolean mIsRecording = false;
    private boolean mIsPlaying = false;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private AudioManager mAM = null;

    private RecordTest(Context context) {
        mAM = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
    }

    public static RecordTest getInstance(Context context){
        if (mRecordTest == null){
             mRecordTest = new RecordTest(context);
        }
        return mRecordTest;
    }

    public void startRecord(Handler handler){
        if (!mIsRecording) {
            new File(mPath).delete();
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(mPath);
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
            mIsRecording = true;
            Log.i("zz", "startRecord: ");
            String s = "············正在录音中············";
            Message ms =Message.obtain();
            ms.obj = s;
            handler.sendMessage(ms);
        }
    }


    public void stopRecord(Handler handler){
        if (mIsRecording) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            mIsRecording = false;

            String s = "············录音完毕欧耶>_<!";
            Message ms =Message.obtain();
            ms.obj = s;
            handler.sendMessage(ms);
        }
    }

    public void startPlay(Handler handler){
        final Handler mHandler = handler;
        if(!mIsPlaying) {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setScreenOnWhilePlaying(true);
            try {
                mPlayer.setDataSource(mPath);
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.setLooping(false);
            mPlayer.setVolume(1f, 1f);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    String s = "录音播放结束········>_<";
                    Message ms =Message.obtain();
                    ms.obj = s;
                    mHandler.sendMessage(ms);
                    mIsPlaying = false;
                    mPlayer.release();
                    mPlayer = null;
                }
            });
            mPlayer.start();
            mIsPlaying = true;

            String s = "正在播放录音···········路径为："+mPath;
            Message ms =Message.obtain();
            ms.obj = s;
            handler.sendMessage(ms);
        }
    }

}
