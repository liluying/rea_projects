package com.rea.wajibao.replica.core;

public final class Guidance {
    public static final double TOLERANCE_METRES = 0.03;
    public final double target, delta;
    public final String instruction;
    public final boolean onGrade;
    public Guidance(Design design, PoseSolver.Pose pose) {
        target = design.elevationAt(pose.lateral); delta = pose.elevation - target;
        onGrade = Math.abs(delta) <= TOLERANCE_METRES + 1e-9;
        instruction = onGrade ? "已到设计面" : delta > 0 ? "高于设计 · 继续下挖" : "低于设计 · 抬升 / 回填";
    }
}
