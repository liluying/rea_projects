package com.bingce.utils;

/** Read-only exported SDK feature checks; no tokens or authorization state are modified. */
public final class SdkUtils {
    private SdkUtils() { }
    public static native boolean isWjbValidate();
    public static native boolean isWjbEmulatorValidate();
}
