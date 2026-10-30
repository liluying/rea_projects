package com.rea.wajibao.replica.core;

/** Replacement boundary for a future verified WajiInner adapter. No native ABI is guessed here. */
public interface PoseSolver {
    Pose solve(MachineConfig machine, double baseElevation, double boomDegrees,
               double stickDegrees, double bucketDegrees, double lateral);
    final class Pose {
        public final double reach, lateral, elevation;
        public Pose(double reach, double lateral, double elevation) {
            this.reach = reach; this.lateral = lateral; this.elevation = elevation;
        }
    }
}
