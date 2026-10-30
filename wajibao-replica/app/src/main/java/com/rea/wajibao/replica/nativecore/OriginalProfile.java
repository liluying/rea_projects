package com.rea.wajibao.replica.nativecore;

import org.json.JSONObject;

/** Exact initMachine / initAttachment argument order observed in jc.e.G / jc.e.F. */
public final class OriginalProfile {
    private static final String[] MACHINE_KEYS = {"ab","bc","cd","az","jz","zr","ak","zk","zs","ks","pq","sw","swOffset","us","usOffset","usHd","uv","uvOffset","uvHd","st","sToWaterHd","azCorrect","abCorrect","bcCorrect","akCorrect","zkCorrect","zsCorrect","rollCorrect","pitchCorrect","yawCorrect","jzCorrect","skCorrect"};
    private static final String[] ATTACHMENT_KEYS = {"ad","aj","dj","dg","ag","simpleAjTilt","mn"};
    public final String name;
    public final int workMode, attachmentMode;
    private final double[] machine, attachment;
    private final int[] sides;
    public final boolean primaryAntennaFront;
    public final String canonicalJson;
    public OriginalProfile(String json) throws Exception {
        JSONObject root = new JSONObject(json);
        name = root.getString("name").trim();
        if (name.isEmpty()) throw new IllegalArgumentException("机械名称不能为空");
        workMode = root.getInt("workMode");
        if (workMode < 0 || workMode > 9) throw new IllegalArgumentException("原版工作模式编号应为 0 至 9");
        machine = read(root.getJSONObject("machine"), MACHINE_KEYS);
        JSONObject a = root.getJSONObject("attachment");
        attachment = read(a, ATTACHMENT_KEYS); attachmentMode = a.getInt("mode");
        if (attachmentMode < 0 || attachmentMode > 6) throw new IllegalArgumentException("附件模式编号应为 0 至 6");
        JSONObject s = root.getJSONObject("installSides");
        sides = new int[]{s.getInt("bucket"),s.getInt("stick"),s.getInt("boom"),s.getInt("body")};
        for (int side : sides) if (side < 0 || side > 1) throw new IllegalArgumentException("安装侧应为 0 或 1");
        primaryAntennaFront = root.getBoolean("primaryAntennaFront");
        canonicalJson = root.toString();
    }
    private static double[] read(JSONObject object, String[] keys) throws Exception {
        double[] values = new double[keys.length];
        for (int i = 0; i < keys.length; i++) {
            values[i] = object.getDouble(keys[i]);
            if (!Double.isFinite(values[i])) throw new IllegalArgumentException(keys[i] + " 必须是有限数值");
        }
        return values;
    }
    public double[] machineValues() { return machine.clone(); }
    public double[] attachmentValues() { return attachment.clone(); }
    public int[] installSides() { return sides.clone(); }
}
