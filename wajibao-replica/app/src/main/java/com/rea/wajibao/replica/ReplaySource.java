package com.rea.wajibao.replica;

import android.content.Context;
import com.rea.wajibao.replica.core.Design;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

final class ReplaySource {
    static final class Frame {
        final double x, y, z;
        Frame(JSONArray a) throws Exception {
            x = a.getDouble(0); y = a.getDouble(1); z = a.getDouble(2);
            if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z))
                throw new IOException("演示坐标无效");
        }
    }
    final int intervalMs;
    private final Frame[] frames;
    ReplaySource(Context context, Design.Mode mode) throws Exception {
        String json;
        try (InputStream in = context.getAssets().open("replay.json")) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192]; int count;
            while ((count = in.read(buffer)) != -1) bytes.write(buffer, 0, count);
            json = bytes.toString(StandardCharsets.UTF_8.name());
        }
        JSONObject section = new JSONObject(json).getJSONObject(mode == Design.Mode.LEVEL ? "level" : "trenching");
        intervalMs = section.getInt("intervalMs");
        JSONArray array = section.getJSONArray("frames");
        if (array.length() == 0 || intervalMs < 50) throw new IOException("演示数据为空或频率无效");
        frames = new Frame[array.length()];
        for (int i = 0; i < frames.length; i++) frames[i] = new Frame(array.getJSONArray(i));
    }
    Frame get(int index) { return frames[Math.floorMod(index, frames.length)]; }
    int count() { return frames.length; }
}
