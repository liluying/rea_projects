package com.rea.wajibao.replica.nativecore;

import org.json.JSONObject;

/** Original replay units are degrees; the JNI compute path takes radians. */
public final class OriginalFrame {
    private final double[] pose, antenna;
    public final int bucketMode;
    public final boolean demo;
    public final String json;
    public OriginalFrame(JSONObject frame) throws Exception {
        JSONObject g = frame.getJSONObject("gnss"), s = frame.getJSONObject("sensor");
        pose = new double[]{g.getDouble("normalX"),g.getDouble("normalY"),g.getDouble("normalZ"),
                rad(g,"rtkRoll"),rad(g,"rtkPitch"),rad(g,"rtkYaw"),
                rad(s,"bodyRoll"),rad(s,"bodyPitch"),rad(s,"boomRoll"),rad(s,"boomPitch"),
                rad(s,"stickRoll"),rad(s,"stickPitch"),rad(s,"bucketRoll"),rad(s,"bucketPitch")};
        antenna = new double[]{g.getDouble("L1L2PoleHeight"),g.getDouble("L1L2BottomHeight"),0,g.getDouble("dualAntennaLength")};
        // The recovered Java demo call uses zero bottom correction, and simple-bucket mode 2.
        bucketMode = g.getInt("bucketMode"); demo = true; json = frame.toString();
        for (double v : pose) if (!Double.isFinite(v)) throw new IllegalArgumentException("姿态数据非有限");
        for (double v : antenna) if (!Double.isFinite(v)) throw new IllegalArgumentException("天线参数非有限");
        if (bucketMode < 0 || bucketMode > 2) throw new IllegalArgumentException("简化斗模式无效");
    }
    private static double rad(JSONObject o, String key) throws Exception { return Math.toRadians(o.getDouble(key)); }
    public double[] poseValues() { return pose.clone(); }
    public double[] antennaValues() { return antenna.clone(); }
    public double x() { return pose[0]; }
    public double y() { return pose[1]; }
    public double z() { return pose[2]; }
}
