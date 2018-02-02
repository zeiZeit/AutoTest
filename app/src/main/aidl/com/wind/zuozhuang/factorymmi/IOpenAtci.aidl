// IOpenAtci.aidl
package com.wind.zuozhuang.factorymmi;

// Declare any non-default types here with import statements

interface IOpenAtci {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


    int getCode();

    String returnCode(String code);
}
