package com.wind.zuozhuang.factorymmi.testcase;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

/**
 * Created by zuozhuang on 2017/11/3.
 */
public class RecordTest {

    private static RecordTest mRecordTest = null;
    private final String mPath = "/sdcard/recordtest.amr";
    private boolean mIsRecording = false;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;


    private RecordTest() {

    }

    public static RecordTest getInstance(){
        if (mRecordTest == null){
             mRecordTest = new RecordTest();
        }
        return mRecordTest;
    }

    public void startRecord(){
        if (!mIsRecording) {
            new File(mPath).delete();
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
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
        }
    }


    public void stopRecord(){
        if (mIsRecording) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            mIsRecording = false;
        }
    }

    public void startPlay(){

    }

}
