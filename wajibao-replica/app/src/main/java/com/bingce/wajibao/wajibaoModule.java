// JNI contract recovered from the supplied APK; not recovered C++ source.
package com.bingce.wajibao;


public class wajibaoModule {
    public static final native double CTiltCalculator_calculatePoleBottomAzimuthRadian(double d10, double d11, double d12);

    public static final native double CTiltCalculator_calculatePoleTiltAngle(double d10, double d11, double d12);

    public static final native int WajiInner_DUAL_ANTENNA_ON_BODY_WIRED_ON_BODY_BOOM_STICK_BUCKET_get();

    public static final native int WajiInner_DUAL_ANTENNA_ON_BOOM_WIRED_ON_BOOM_STICK_BUCKET_get();

    public static final native int WajiInner_DUAL_ANTENNA_ON_BOOM_WIRED_ON_STICK_BUCKET_get();

    public static final native int WajiInner_DUAL_ANTENNA_ON_GRAB_NO_SENSOR_get();

    public static final native int WajiInner_RTK_ON_ATTACHMENT_NO_SENSOR_get();

    public static final native int WajiInner_RTK_ON_BOOM_WIRED_ON_STICK_BUCKET_get();

    public static final native int WajiInner_RTK_ON_BOOM_WIRED_ON_STICK_get();

    public static final native int WajiInner_RTK_ON_BOOM_WIRELESS_ON_STICK_BUCKET_get();

    public static final native int WajiInner_RTK_ON_STICK_NO_SENSOR_get();

    public static final native int WajiInner_RTK_ON_STICK_WIRELESS_ON_BUCKET_get();

    public static final native void WajiInner_calculate(long j, WajiInner wajiInner, double d10, double d11, double d12, double d13, double d14, double d15, double d16, double d17, double d18, double d19, double d20, double d21, double d22, double d23, int i, double d24, double d25, double d26, double d27, boolean z10);

    public static final native long WajiInner_getA(long j, WajiInner wajiInner);

    public static final native long WajiInner_getALeftInnerPoint(long j, WajiInner wajiInner);

    public static final native long WajiInner_getALeftOuterPoint(long j, WajiInner wajiInner);

    public static final native long WajiInner_getARightInnerPoint(long j, WajiInner wajiInner);

    public static final native long WajiInner_getARightOuterPoint(long j, WajiInner wajiInner);

    public static final native double WajiInner_getAb(long j, WajiInner wajiInner);

    public static final native double WajiInner_getAd(long j, WajiInner wajiInner);

    public static final native double WajiInner_getAiAzimuth(long j, WajiInner wajiInner);

    public static final native double WajiInner_getAj(long j, WajiInner wajiInner);

    public static final native double WajiInner_getAk(long j, WajiInner wajiInner);

    public static final native double WajiInner_getAkCorrect(long j, WajiInner wajiInner);

    public static final native long WajiInner_getB(long j, WajiInner wajiInner);

    public static final native double WajiInner_getBc(long j, WajiInner wajiInner);

    public static final native long WajiInner_getBodyCenter(long j, WajiInner wajiInner);

    public static final native long WajiInner_getBoomBottom(long j, WajiInner wajiInner);

    public static final native long WajiInner_getBucketCenterBottom(long j, WajiInner wajiInner);

    public static final native long WajiInner_getBucketFourthBottom(long j, WajiInner wajiInner);

    public static final native long WajiInner_getBucketLeftBottom(long j, WajiInner wajiInner);

    public static final native long WajiInner_getBucketRightBottom(long j, WajiInner wajiInner);

    public static final native long WajiInner_getBucketSecondBottom(long j, WajiInner wajiInner);

    public static final native long WajiInner_getC(long j, WajiInner wajiInner);

    public static final native long WajiInner_getCLeftInnerPoint(long j, WajiInner wajiInner);

    public static final native long WajiInner_getCRightInnerPoint(long j, WajiInner wajiInner);

    public static final native double WajiInner_getCd(long j, WajiInner wajiInner);

    public static final native double WajiInner_getCrossIncline(long j, WajiInner wajiInner, double d10, double d11, double d12);

    public static final native long WajiInner_getD(long j, WajiInner wajiInner);

    public static final native long WajiInner_getDLeftInnerPoint(long j, WajiInner wajiInner);

    public static final native long WajiInner_getDLeftOuterPoint(long j, WajiInner wajiInner);

    public static final native long WajiInner_getDRightInnerPoint(long j, WajiInner wajiInner);

    public static final native long WajiInner_getDRightOuterPoint(long j, WajiInner wajiInner);

    public static final native String WajiInner_getDebugInfo(long j, WajiInner wajiInner);

    public static final native long WajiInner_getG(long j, WajiInner wajiInner);

    public static final native long WajiInner_getGLeftPoint(long j, WajiInner wajiInner);

    public static final native long WajiInner_getGRightPoint(long j, WajiInner wajiInner);

    public static final native long WajiInner_getI(long j, WajiInner wajiInner);

    public static final native long WajiInner_getJ(long j, WajiInner wajiInner);

    public static final native double WajiInner_getJaAzimuth(long j, WajiInner wajiInner);

    public static final native double WajiInner_getJg(long j, WajiInner wajiInner);

    public static final native double WajiInner_getJgAzimuth(long j, WajiInner wajiInner);

    public static final native double WajiInner_getJzCorrect(long j, WajiInner wajiInner);

    public static final native long WajiInner_getK(long j, WajiInner wajiInner);

    public static final native long WajiInner_getKLeftPoint(long j, WajiInner wajiInner);

    public static final native long WajiInner_getKRightPoint(long j, WajiInner wajiInner);

    public static final native double WajiInner_getKz(long j, WajiInner wajiInner);

    public static final native double WajiInner_getMn(long j, WajiInner wajiInner);

    public static final native long WajiInner_getModelBodyCenter(long j, WajiInner wajiInner);

    public static final native double WajiInner_getPitchCorrect(long j, WajiInner wajiInner);

    public static final native double WajiInner_getPq(long j, WajiInner wajiInner);

    public static final native double WajiInner_getRtkPitch(long j, WajiInner wajiInner);

    public static final native double WajiInner_getRtkPitch2(long j, WajiInner wajiInner);

    public static final native double WajiInner_getRtkRoll(long j, WajiInner wajiInner);

    public static final native double WajiInner_getRtkRoll2(long j, WajiInner wajiInner);

    public static final native double WajiInner_getRtkYaw(long j, WajiInner wajiInner);

    public static final native double WajiInner_getRtkYaw2(long j, WajiInner wajiInner);

    public static final native long WajiInner_getS(long j, WajiInner wajiInner);

    public static final native double WajiInner_getSt(long j, WajiInner wajiInner);

    public static final native long WajiInner_getStickBottom(long j, WajiInner wajiInner);

    public static final native long WajiInner_getStickTop(long j, WajiInner wajiInner);

    public static final native double WajiInner_getSw(long j, WajiInner wajiInner);

    public static final native double WajiInner_getSwOffset(long j, WajiInner wajiInner);

    public static final native double WajiInner_getWaterHeight(long j, WajiInner wajiInner);

    public static final native double WajiInner_getWjFrontAzimuth(long j, WajiInner wajiInner);

    public static final native double WajiInner_getYawCorrect(long j, WajiInner wajiInner);

    public static final native long WajiInner_getZ(long j, WajiInner wajiInner);

    public static final native double WajiInner_getZkCorrect(long j, WajiInner wajiInner);

    public static final native double WajiInner_getZsCorrect(long j, WajiInner wajiInner);

    public static final native boolean WajiInner_initAttachment(long j, WajiInner wajiInner, int i, double d10, double d11, double d12, double d13, double d14, double d15, double d16);

    public static final native boolean WajiInner_initMachine(long j, WajiInner wajiInner, int i, double d10, double d11, double d12, double d13, double d14, double d15, double d16, double d17, double d18, double d19, double d20, double d21, double d22, double d23, double d24, double d25, double d26, double d27, double d28, double d29, double d30, double d31, double d32, double d33, double d34, double d35, double d36, double d37, double d38, double d39, double d40, double d41, int i10, int i11, int i12, int i13, boolean z10);

    public static final native boolean WajiInner_isAttachmentInitialized(long j, WajiInner wajiInner);

    public static final native boolean WajiInner_isMachineInitialized(long j, WajiInner wajiInner);

    public static final native void WajiInner_setAttachmentInitialized(long j, WajiInner wajiInner, boolean z10);

    public static final native void WajiInner_setBucketCenterBottomZ(long j, WajiInner wajiInner, double d10);

    public static final native void WajiInner_setBucketFourthBottomZ(long j, WajiInner wajiInner, double d10);

    public static final native void WajiInner_setBucketLeftBottomZ(long j, WajiInner wajiInner, double d10);

    public static final native void WajiInner_setBucketRightBottomZ(long j, WajiInner wajiInner, double d10);

    public static final native void WajiInner_setBucketSecondBottomZ(long j, WajiInner wajiInner, double d10);

    public static final native void WajiInner_setDebug(long j, WajiInner wajiInner, boolean z10);

    public static final native void WajiInner_setMachineInitialized(long j, WajiInner wajiInner, boolean z10);

    public static final native void WajiInner_setModelSw(long j, WajiInner wajiInner, double d10);

    public static final native void WajiInner_setModelSwHd(long j, WajiInner wajiInner, double d10);

    public static final native void WajiInner_setModelSwOffset(long j, WajiInner wajiInner, double d10);

    public static final native void WajiInner_setPitchCorrect(long j, WajiInner wajiInner, double d10);

    public static final native void WajiInner_setYawCorrect(long j, WajiInner wajiInner, double d10);

    public static final native void delete_WajiInner(long j);

    public static final native long new_WajiInner();
}