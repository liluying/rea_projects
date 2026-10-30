package com.rea.wajibao.replica.core;

/** Lengths in metres. This is a demo planar machine, not a recovered calibration model. */
public final class MachineConfig {
    public final String name;
    public final double boom, stick, bucket;
    public MachineConfig(String name, double boom, double stick, double bucket) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("请填写机械名称");
        checkLength(boom); checkLength(stick); checkLength(bucket);
        this.name = name.trim(); this.boom = boom; this.stick = stick; this.bucket = bucket;
    }
    private static void checkLength(double value) {
        if (!Double.isFinite(value) || value <= 0 || value > 30)
            throw new IllegalArgumentException("各连杆长度应大于 0 且不超过 30 米");
    }
}
