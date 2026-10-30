package com.rea.wajibao.replica.nativecore;

/** Shared contract exercised by the Android JNI backend and ARM64-emulated native validation. */
public interface WajiBackend {
    boolean demoAuthorized();
    long create();
    boolean initMachine(long pointer, OriginalProfile config);
    boolean initAttachment(long pointer, OriginalProfile config);
    void calculate(long pointer, OriginalFrame frame);
    Result result(long pointer);
    void delete(long pointer);
    final class Result {
        public final double x, y, z, heading;
        public final double leftZ, rightZ;
        public Result(double x, double y, double z, double heading, double leftZ, double rightZ) {
            for (double v : new double[]{x,y,z,heading,leftZ,rightZ})
                if (!Double.isFinite(v)) throw new IllegalStateException("原版核心返回非有限结果，已停止计算");
            this.x=x; this.y=y; this.z=z; this.heading=heading; this.leftZ=leftZ; this.rightZ=rightZ;
        }
    }
}
