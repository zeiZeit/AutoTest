package com.wind.zuozhuang.factorymmi.testcase;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;

/**
 * Created by zuozhuang on 2017/8/2.
 */
public class SimCardTest {
    private static SimCardTest mSim = null;

    private SimCardTest() {

    }

    public static SimCardTest getInstance(){
        if (mSim == null){
            mSim = new SimCardTest();
        }
        return  mSim;
    }

    public void  test(Context context, Handler handler)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);//取得相关系统服务
        String sb = new String();
        switch(tm.getSimState()){ //getSimState()取得sim的状态 有下面6中状态
            case TelephonyManager.SIM_STATE_ABSENT :sb = "无卡";break;
            case TelephonyManager.SIM_STATE_UNKNOWN :sb = "未知状态";break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED :sb = "需要NetworkPIN解锁";break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED :sb = "需要PIN解锁";break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED :sb = "需要PUK解锁";break;
            case TelephonyManager.SIM_STATE_READY :sb = "有卡且状态良好";break;
        }

        Message ms =Message.obtain();
        ms.obj = sb;
        handler.sendMessage(ms);
    }
}
