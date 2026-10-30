package com.rea.wajibao.replica.core;

/** New demo implementation: three links with absolute angles from the horizontal, in degrees.
 * GNSS height is treated as a synthetic boom pivot. NOT equivalent to the original excavator solver.
 */
public final class DemoPoseSolver implements PoseSolver {
    @Override public Pose solve(MachineConfig m, double base, double boom, double stick,
                                double bucket, double lateral) {
        for (double v : new double[]{base, boom, stick, bucket, lateral})
            if (!Double.isFinite(v)) throw new IllegalArgumentException("姿态参数必须是有限数值");
        double a = Math.toRadians(boom), b = Math.toRadians(stick), c = Math.toRadians(bucket);
        return new Pose(m.boom * Math.cos(a) + m.stick * Math.cos(b) + m.bucket * Math.cos(c),
                lateral, base + m.boom * Math.sin(a) + m.stick * Math.sin(b) + m.bucket * Math.sin(c));
    }
}
