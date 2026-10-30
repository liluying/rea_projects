package com.bingce.coordlib.proj4j.model;


public class coordModelModule {
    public static final native double CCoordinate_getX(long j, CCoordinate cCoordinate);

    public static final native double CCoordinate_getY(long j, CCoordinate cCoordinate);

    public static final native double CCoordinate_getZ(long j, CCoordinate cCoordinate);

    public static final native void CCoordinate_setX(long j, CCoordinate cCoordinate, double d10);

    public static final native void CCoordinate_setY(long j, CCoordinate cCoordinate, double d10);

    public static final native void CCoordinate_setZ(long j, CCoordinate cCoordinate, double d10);

    public static final native void delete_CCoordinate(long j);
}