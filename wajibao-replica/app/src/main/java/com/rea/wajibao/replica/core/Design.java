package com.rea.wajibao.replica.core;

public final class Design {
    public enum Mode { LEVEL, TRENCH }
    public final Mode mode;
    public final double surface, depth, width, sideRatio;
    private Design(Mode mode, double surface, double depth, double width, double sideRatio) {
        if (!Double.isFinite(surface) || Math.abs(surface) > 10000)
            throw new IllegalArgumentException("设计高程应在 -10000 至 10000 米之间");
        this.mode = mode; this.surface = surface; this.depth = depth;
        this.width = width; this.sideRatio = sideRatio;
    }
    public static Design level(double elevation) { return new Design(Mode.LEVEL, elevation, 0, 0, 0); }
    public static Design trench(double surface, double depth, double width, double sideRatio) {
        if (!Double.isFinite(depth) || depth <= 0 || depth > 20)
            throw new IllegalArgumentException("沟深应大于 0 且不超过 20 米");
        if (!Double.isFinite(width) || width <= 0 || width > 30)
            throw new IllegalArgumentException("沟底宽应大于 0 且不超过 30 米");
        if (!Double.isFinite(sideRatio) || sideRatio < 0 || sideRatio > 5)
            throw new IllegalArgumentException("边坡水平/垂直比应在 0 至 5 之间");
        return new Design(Mode.TRENCH, surface, depth, width, sideRatio);
    }
    /** Cross section centered on lateral=0. Side ratio is horizontal / vertical. */
    public double elevationAt(double lateral) {
        if (!Double.isFinite(lateral)) throw new IllegalArgumentException("横向偏移无效");
        if (mode == Mode.LEVEL) return surface;
        double beyondBottom = Math.abs(lateral) - width / 2;
        if (beyondBottom <= 0) return surface - depth;
        if (sideRatio == 0) return surface;
        return Math.min(surface, surface - depth + beyondBottom / sideRatio);
    }
    public String label() { return mode == Mode.LEVEL ? "找平施工" : "挖沟施工"; }
}
