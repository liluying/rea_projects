package com.rea.wajibao.replica;

import android.content.Context;
import android.util.AtomicFile;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

final class RecordStore {
    private final AtomicFile file;
    RecordStore(Context context) { file = new AtomicFile(new File(context.getFilesDir(), "records.json")); }
    JSONArray load() throws Exception {
        if (!file.getBaseFile().exists()) return new JSONArray();
        return new JSONArray(new String(file.readFully(), StandardCharsets.UTF_8));
    }
    void append(JSONObject record) throws Exception {
        JSONArray records = load();
        if (records.length() >= 500) throw new IOException("已达 500 条记录，请先导出并清空记录");
        records.put(record); write(records);
    }
    void clear() throws Exception { write(new JSONArray()); }
    private void write(JSONArray records) throws Exception {
        FileOutputStream out = null;
        try {
            out = file.startWrite(); out.write(records.toString().getBytes(StandardCharsets.UTF_8));
            file.finishWrite(out);
        } catch (Exception e) { if (out != null) file.failWrite(out); throw e; }
    }
    String csv() throws Exception {
        String[] keys = {"time", "machine", "mode", "frame", "gnssX", "gnssY", "gnssZ", "tipZ", "targetZ", "delta", "lateral", "boomLength", "stickLength", "bucketLength", "boomAngle", "stickAngle", "bucketAngle", "surface", "depth", "width", "sideRatio", "solver", "nativeX", "nativeY", "nativeZ", "nativeLeftZ", "nativeRightZ", "nativeHeading", "designCenterlineOffset", "librarySha256", "nativeProfile", "nativeFrame"};
        StringBuilder csv = new StringBuilder("\uFEFF");
        row(csv, keys);
        JSONArray records = load();
        for (int i = 0; i < records.length(); i++) {
            JSONObject r = records.getJSONObject(i); String[] values = new String[keys.length];
            for (int k = 0; k < keys.length; k++) values[k] = r.optString(keys[k], "");
            // User-entered names must stay data when opened in a spreadsheet.
            if (values[1].matches("^[=+@\\-].*")) values[1] = "'" + values[1];
            row(csv, values);
        }
        return csv.toString();
    }
    private void row(StringBuilder out, String[] values) {
        for (int i = 0; i < values.length; i++) {
            if (i > 0) out.append(',');
            out.append('"').append(values[i].replace("\"", "\"\"")).append('"');
        }
        out.append("\r\n");
    }
}
