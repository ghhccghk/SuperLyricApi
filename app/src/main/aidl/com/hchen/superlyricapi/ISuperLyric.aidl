// ISuperLyric.aidl
package com.hchen.superlyricapi;

// Declare any non-default types here with import statements
parcelable SuperLyricData;

interface ISuperLyric {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    //void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
    //        double aDouble, String aString);

    void onStop();

    void onSuperLyric(in SuperLyricData data);
}