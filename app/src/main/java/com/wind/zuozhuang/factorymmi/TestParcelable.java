package com.wind.zuozhuang.factorymmi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zuozhuang on 2017/11/5.
 */
public class TestParcelable implements Parcelable {


    protected TestParcelable(Parcel in) {
    }

    public static final Creator<TestParcelable> CREATOR = new Creator<TestParcelable>() {
        @Override
        public TestParcelable createFromParcel(Parcel in) {
            return new TestParcelable(in);
        }

        @Override
        public TestParcelable[] newArray(int size) {
            return new TestParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
