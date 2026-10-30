package com.rea.wajibao.replica;

import android.content.Context;
import com.rea.wajibao.replica.core.Design;
import com.rea.wajibao.replica.core.PoseSolver;
import com.rea.wajibao.replica.nativecore.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/** Replays the complete original frame stream. Rewinds recreate the stateful native session. */
final class OriginalReplayController implements AutoCloseable {
    final OriginalProfile profile;
    private final OriginalFrame[] frames;
    private OriginalSession session;
    private int last = -1;
    private WajiBackend.Result reference, latest;
    OriginalReplayController(Context context, Design.Mode mode, OriginalProfile profile) throws Exception {
        this.profile = profile;
        JSONObject replay = new JSONObject(asset(context, "native-replay.json"));
        JSONArray array = replay.getJSONObject(mode == Design.Mode.LEVEL ? "level" : "trenching").getJSONArray("frames");
        frames = new OriginalFrame[array.length()];
        for (int i = 0; i < frames.length; i++) frames[i] = new OriginalFrame(array.getJSONObject(i));
        try { update(0); } catch (RuntimeException | Error e) { close(); throw e; }
    }
    static String asset(Context context, String path) throws Exception {
        try (InputStream in = context.getAssets().open(path); ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
            byte[] buf = new byte[8192]; int count;
            while ((count = in.read(buf)) != -1) bytes.write(buf, 0, count);
            return bytes.toString(StandardCharsets.UTF_8.name());
        }
    }
    int count() { return frames.length; }
    private void update(int index) {
        if (index < 0 || index >= frames.length) throw new IllegalArgumentException("原版回放帧越界");
        if (session == null || index < last) {
            close(); session = new OriginalSession(new JniWajiBackend(), profile); last = -1;
        }
        while (last < index) {
            int next = last + 1;
            try { latest = session.calculate(frames[next]); last = next; }
            catch (RuntimeException | Error e) { close(); latest = null; last = -1; throw e; }
            if (last == 0) reference = latest;
        }
    }
    PoseSolver.Pose pose(int index, double designCenterlineOffset) {
        update(index);
        double dx = latest.x - reference.x, dy = latest.y - reference.y;
        double lateral = -Math.sin(reference.heading) * dx + Math.cos(reference.heading) * dy - designCenterlineOffset;
        double reach = Math.hypot(latest.x - frames[index].x(), latest.y - frames[index].y());
        return new PoseSolver.Pose(reach, lateral, latest.z);
    }
    WajiBackend.Result result() { return latest; }
    OriginalFrame input(int index) { return frames[index]; }
    @Override public void close() { if (session != null) { session.close(); session = null; } }
}
