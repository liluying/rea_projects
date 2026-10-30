package com.rea.wajibao.replica;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.rea.wajibao.replica.core.*;
import com.rea.wajibao.replica.nativecore.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class MainActivity extends Activity {
    private static final int INK = 0xff102c31, TEAL = 0xff147d72, MUTED = 0xff667c7c;
    private static final int EXPORT = 41;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Map<String, EditText> fields = new LinkedHashMap<>();
    private final Map<String, String> drafts = new LinkedHashMap<>();
    private final PoseSolver solver = new DemoPoseSolver();
    private LinearLayout content;
    private SharedPreferences prefs;
    private RecordStore records;
    private MachineConfig machine;
    private Design design;
    private ReplaySource replay;
    private int stage = 1, previousStage = 1, frame;
    private Design.Mode mode = Design.Mode.LEVEL;
    private boolean playing;
    private boolean useOriginal;
    private OriginalProfile originalProfile;
    private OriginalReplayController original;
    private static final String CORE_HASH = "443a89c357df2277ea3e604db699188fc0b52eba3cd2306332f4e544b49bfa32";
    private double boom = 25, stick = -55, bucket = -75, lateral;
    private TextView deltaText, statusText, coordinateText, replayText;
    private Button playButton;
    private ProfileView profile;
    private Bundle restore;
    private final Runnable tick = new Runnable() {
        @Override public void run() {
            if (!playing || stage != 3) return;
            if (frame + 1 >= replay.count()) { pause(); toast("本段演示已结束，可重播"); return; }
            frame++; renderGuidance(); handler.postDelayed(this, replay.intervalMs);
        }
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state); prefs = getSharedPreferences("replica", MODE_PRIVATE);
        records = new RecordStore(this); restore = state;
        useOriginal = prefs.getBoolean("useOriginal", nativeAbiSupported());
        mode = Design.Mode.valueOf(prefs.getString("mode", "LEVEL"));
        if (state != null) {
            useOriginal = state.getBoolean("useOriginal", useOriginal);
            for (String key : state.keySet()) if (key.startsWith("field_")) drafts.put(key.substring(6), state.getString(key));
            mode = Design.Mode.valueOf(state.getString("mode", mode.name()));
            stage = state.getInt("stage", 1); previousStage = state.getInt("previousStage", 1);
            frame = state.getInt("frame"); boom = state.getDouble("boom", 25);
            stick = state.getDouble("stick", -55); bucket = state.getDouble("bucket", -75);
            lateral = state.getDouble("lateral");
        }
        loadConfig(); showPage();
    }
    private static boolean nativeAbiSupported() {
        for (String abi : Build.SUPPORTED_ABIS) if (abi.equals("arm64-v8a")) return true;
        return false;
    }
    private OriginalProfile originalProfile() throws Exception {
        if (originalProfile == null) originalProfile = new OriginalProfile(prefs.getString("originalProfile",
                OriginalReplayController.asset(this, "native-profile.json")));
        return originalProfile;
    }
    private String machineLabel() { return useOriginal && originalProfile != null ? originalProfile.name : machine.name; }
    private void closeOriginal() { if (original != null) { original.close(); original = null; } }
    private void loadConfig() {
        machine = new MachineConfig(prefs.getString("machine", "演示挖掘机"),
                saved("boomLength", 5.6), saved("stickLength", 2.9), saved("bucketLength", 1.35));
        design = mode == Design.Mode.LEVEL ? Design.level(saved("level", 75.6))
                : Design.trench(saved("surface", 76.8), saved("depth", 1.2), saved("width", 2), saved("ratio", 1));
    }
    private double saved(String key, double fallback) {
        try { return Double.parseDouble(prefs.getString(key, Double.toString(fallback))); }
        catch (NumberFormatException e) { return fallback; }
    }
    private void showPage() {
        closeOriginal();
        fields.clear();
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(0xfff3f6f5);
        LinearLayout bar = new LinearLayout(this); bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setBackgroundColor(INK); bar.setPadding(dp(20), dp(10), dp(16), dp(10));
        TextView brand = text("施工复现", 21, Color.WHITE, true);
        bar.addView(brand, new LinearLayout.LayoutParams(0, dp(48), 1));
        Button history = button("施工记录", false); history.setTextColor(Color.WHITE); history.setBackgroundColor(INK);
        history.setOnClickListener(v -> { if (stage == 4) return; rememberFields(); pause(); previousStage = stage; stage = 4; showPage(); });
        bar.addView(history); root.addView(bar);
        ScrollView scroll = new ScrollView(this); scroll.setFillViewport(true);
        content = new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(dp(20), dp(20), dp(20), dp(28));
        scroll.addView(content); root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1)); setContentView(root);
        if (stage == 4) { historyPage(); return; }
        label(useOriginal ? "原版 ARM64 核心  ·  需要 SDK 授权" : "平面演示算法  ·  离线模拟设备", 12, TEAL, false);
        label(stage == 1 ? "配置机械，开始施工" : stage == 2 ? "设置你的设计面" : design.label(), 27, INK, true);
        label("01 机械参数    →    02 设计参数    →    03 模拟施工", 12, MUTED, false);
        gap(16);
        if (stage == 1) machinePage(); else if (stage == 2) designPage(); else constructionPage();
    }
    private void machinePage() {
        LinearLayout engine = card(); heading(engine, "计算引擎");
        Button nativeButton = button((useOriginal ? "● " : "○ ") + "原版核心 · ARM64", false);
        Button demoButton = button((!useOriginal ? "● " : "○ ") + "平面演示 · 不依赖 SDK", false);
        nativeButton.setOnClickListener(v -> { rememberFields(); useOriginal = true; showPage(); });
        demoButton.setOnClickListener(v -> { rememberFields(); useOriginal = false; showPage(); });
        engine.addView(nativeButton); engine.addView(demoButton); content.addView(engine); gap(14);
        if (useOriginal) { originalMachinePage(); return; }
        LinearLayout card = card(); heading(card, "机械与附件");
        field(card, "machine", "机械名称", machine.name, false);
        field(card, "boomLength", "大臂长度  m", f(machine.boom), true);
        field(card, "stickLength", "小臂长度  m", f(machine.stick), true);
        field(card, "bucketLength", "铲斗参考长度  m", f(machine.bucket), true);
        TextView note = text("使用三连杆平面演示模型，可在施工页手动调节姿态。", 13, MUTED, false);
        card.addView(note); content.addView(card); gap(20);
        action("下一步 · 设计参数", true, () -> {
            try {
                machine = new MachineConfig(value("machine"), number("boomLength"), number("stickLength"), number("bucketLength"));
                prefs.edit().putBoolean("useOriginal", false).apply();
                persistFields(); restore = null; stage = 2; showPage();
            } catch (IllegalArgumentException e) { showError(e.getMessage()); }
        });
    }
    private void originalMachinePage() {
        LinearLayout card = card(); heading(card, "原版机械与附件标定");
        card.addView(text("完整保留 32 项机械数值、7 项附件数值、安装侧和工作模式。校正角为弧度，尺寸为米。默认值来自原 APK 的双天线演示配置（模式 5）。", 13, MUTED, false));
        try {
            EditText json = new EditText(this); json.setTextSize(13); json.setMinLines(10); json.setGravity(Gravity.TOP);
            json.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            json.setText(drafts.containsKey("originalProfile") ? drafts.get("originalProfile") : new JSONObject(originalProfile().canonicalJson).toString(2));
            json.setContentDescription("原版机械与附件参数 JSON"); fields.put("originalProfile", json);
            card.addView(json, new LinearLayout.LayoutParams(-1, dp(320))); content.addView(card); gap(14);
            action("检查原版核心状态", false, () -> {
                try {
                    if (!nativeAbiSupported()) throw new IllegalStateException("当前设备不支持 ARM64 原版库，请在 ARM64 手机或平板上检查。");
                    JniWajiBackend backend = new JniWajiBackend();
                    new AlertDialog.Builder(this).setTitle("原版核心状态").setMessage("libbcCore.so 已加载\n演示功能授权：" + (backend.demoAuthorized() ? "通过" : "未通过") + "\n原版施工功能授权：" + (com.bingce.utils.SdkUtils.isWjbValidate() ? "通过" : "未通过")).setPositiveButton("知道了", null).show();
                } catch (Exception | LinkageError e) { showError(e.getMessage()); }
            });
            action("下一步 · 设计参数", true, () -> {
                try {
                    originalProfile = new OriginalProfile(value("originalProfile")); rememberFields();
                    prefs.edit().putString("originalProfile", originalProfile.canonicalJson).putBoolean("useOriginal", true).apply();
                    restore = null; stage = 2; showPage();
                } catch (Exception e) { showError("原版参数无效：" + e.getMessage()); }
            });
        } catch (Exception e) { content.addView(card); showError("读取原版配置失败：" + e.getMessage()); }
    }
    private void designPage() {
        LinearLayout card = card(); heading(card, "选择施工模式");
        Button level = button((mode == Design.Mode.LEVEL ? "● " : "○ ") + "找平 · 水平设计面", false);
        Button trench = button((mode == Design.Mode.TRENCH ? "● " : "○ ") + "挖沟 · 梯形横断面", false);
        level.setOnClickListener(v -> switchMode(Design.Mode.LEVEL)); trench.setOnClickListener(v -> switchMode(Design.Mode.TRENCH));
        card.addView(level); card.addView(trench); content.addView(card); gap(14);
        LinearLayout form = card(); heading(form, "设计参数");
        if (mode == Design.Mode.LEVEL) field(form, "level", "设计高程  m", f(saved("level", 75.6)), true);
        else {
            field(form, "surface", "地面高程  m", f(saved("surface", 76.8)), true);
            field(form, "depth", "沟深  m", f(saved("depth", 1.2)), true);
            field(form, "width", "沟底宽度  m", f(saved("width", 2)), true);
            field(form, "ratio", "边坡比  水平 / 垂直", f(saved("ratio", 1)), true);
        }
        content.addView(form); gap(18);
        action("进入模拟施工", true, () -> {
            try {
                design = mode == Design.Mode.LEVEL ? Design.level(number("level"))
                        : Design.trench(number("surface"), number("depth"), number("width"), number("ratio"));
                persistFields(); prefs.edit().putString("mode", mode.name()).apply();
                frame = 0; lateral = 0; restore = null; stage = 3; showPage();
            } catch (IllegalArgumentException e) { showError(e.getMessage()); }
        });
        action("返回机械参数", false, () -> { rememberFields(); restore = null; stage = 1; showPage(); });
    }
    private void switchMode(Design.Mode selected) {
        rememberFields(); mode = selected; restore = null; showPage();
    }
    private void constructionPage() {
        try { replay = new ReplaySource(this, mode); frame = Math.floorMod(frame, replay.count()); }
        catch (Exception e) { showError("无法读取演示数据：" + e.getMessage()); stage = 2; showPage(); return; }
        if (useOriginal) {
            try {
                if (!nativeAbiSupported()) throw new IllegalStateException("原版核心仅支持 ARM64 设备。可返回机械参数显式选择平面演示。");
                original = new OriginalReplayController(this, mode, originalProfile());
                if (original.count() != replay.count()) throw new IllegalStateException("原版与演示回放帧数不一致");
            } catch (Exception | LinkageError e) {
                closeOriginal(); stage = 2; showPage(); showError(e.getMessage()); return;
            }
        }
        LinearLayout summary = card();
        TextView machineName = text(machineLabel() + (useOriginal ? "  /  原版机具核心" : "  /  模拟机具"), 14, MUTED, false); summary.addView(machineName);
        deltaText = text("", 39, TEAL, true); summary.addView(deltaText);
        statusText = text("", 15, INK, true); summary.addView(statusText);
        coordinateText = text("", 13, MUTED, false); coordinateText.setPadding(0, dp(10), 0, 0); summary.addView(coordinateText);
        content.addView(summary); gap(14);
        profile = new ProfileView(this); content.addView(profile, new LinearLayout.LayoutParams(-1, dp(240))); gap(12);
        replayText = text("", 12, MUTED, false); content.addView(replayText);
        LinearLayout playback = new LinearLayout(this);
        playButton = button("播放演示", true); playButton.setOnClickListener(v -> { if (playing) pause(); else startReplay(); });
        Button next = button("下一帧", false); next.setOnClickListener(v -> { pause(); frame = (frame + 1) % replay.count(); renderGuidance(); });
        Button reset = button("重置", false); reset.setOnClickListener(v -> { pause(); frame = 0; renderGuidance(); });
        playback.addView(playButton, new LinearLayout.LayoutParams(0, dp(54), 1.4f));
        playback.addView(next, new LinearLayout.LayoutParams(0, dp(54), 1));
        playback.addView(reset, new LinearLayout.LayoutParams(0, dp(54), 1)); content.addView(playback); gap(14);
        LinearLayout controls = card(); heading(controls, useOriginal ? "原版完整输入回放" : "手动姿态 · 角度相对水平面");
        if (useOriginal) {
            controls.addView(text("GNSS、机身/大臂/小臂/铲斗姿态及天线参数均来自原版回放。这里的设计高差仍由本版设计面计算。", 13, MUTED, false));
            slider(controls, "设计中心线偏移", -4, 4, lateral, v -> lateral = v, " m");
        } else {
            slider(controls, "大臂", -10, 70, boom, v -> boom = v, "°");
            slider(controls, "小臂", -100, 10, stick, v -> stick = v, "°");
            slider(controls, "铲斗", -110, 10, bucket, v -> bucket = v, "°");
            slider(controls, "横向偏移", -4, 4, lateral, v -> lateral = v, " m");
        }
        content.addView(controls); gap(14);
        action("保存当前施工点", true, this::saveRecord);
        action("返回修改设计", false, () -> { pause(); stage = 2; showPage(); });
        renderGuidance();
    }
    private void startReplay() {
        if (frame + 1 >= replay.count()) frame = 0;
        playing = true; playButton.setText("暂停演示"); renderGuidance();
        handler.removeCallbacks(tick); handler.postDelayed(tick, replay.intervalMs);
    }
    private void pause() {
        playing = false; handler.removeCallbacks(tick);
        if (playButton != null) playButton.setText("播放演示");
    }
    private PoseSolver.Pose currentPose() {
        if (useOriginal) {
            if (original == null) throw new IllegalStateException("原版计算会话未建立");
            return original.pose(frame, lateral);
        }
        return solver.solve(machine, replay.get(frame).z, boom, stick, bucket, lateral);
    }
    private void renderGuidance() {
        if (stage != 3 || profile == null || replay == null) return;
        PoseSolver.Pose pose;
        try { pose = currentPose(); }
        catch (RuntimeException e) { pause(); deltaText.setText("计算已停止"); statusText.setText(e.getMessage()); coordinateText.setText(""); profile.clear(); return; }
        Guidance g = new Guidance(design, pose);
        deltaText.setText(String.format(Locale.CHINA, "%+.3f m", g.delta));
        deltaText.setTextColor(g.onGrade ? TEAL : g.delta > 0 ? 0xffa56a16 : 0xffbf514b);
        statusText.setText(g.instruction);
        coordinateText.setText(String.format(Locale.CHINA, "斗齿高程  %.3f m     设计高程  %.3f m\n水平距离  %.2f m     横向偏移  %+.2f m", pose.elevation, g.target, pose.reach, pose.lateral));
        replayText.setText(String.format(Locale.CHINA, "演示 GNSS  ·  第 %d / %d 帧  ·  %.1f s", frame + 1, replay.count(), frame * replay.intervalMs / 1000.0));
        profile.update(design, pose);
    }
    private void saveRecord() {
        try {
            PoseSolver.Pose pose = currentPose(); Guidance g = new Guidance(design, pose); ReplaySource.Frame gnss = replay.get(frame);
            JSONObject row = new JSONObject();
            row.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ROOT).format(new Date()));
            row.put("machine", machineLabel()); row.put("mode", mode.name()); row.put("frame", frame + 1);
            row.put("gnssX", gnss.x); row.put("gnssY", gnss.y); row.put("gnssZ", gnss.z);
            row.put("tipZ", pose.elevation); row.put("targetZ", g.target); row.put("delta", g.delta); row.put("lateral", pose.lateral);
            if (useOriginal) {
                WajiBackend.Result r = original.result(); row.put("nativeX", r.x); row.put("nativeY", r.y); row.put("nativeZ", r.z);
                row.put("nativeLeftZ", r.leftZ); row.put("nativeRightZ", r.rightZ); row.put("nativeHeading", r.heading);
                row.put("nativeProfile", original.profile.canonicalJson); row.put("nativeFrame", original.input(frame).json);
                row.put("designCenterlineOffset", lateral); row.put("librarySha256", CORE_HASH);
            } else {
                row.put("boomLength", machine.boom); row.put("stickLength", machine.stick); row.put("bucketLength", machine.bucket);
                row.put("boomAngle", boom); row.put("stickAngle", stick); row.put("bucketAngle", bucket);
            }
            row.put("surface", design.surface); row.put("depth", design.depth); row.put("width", design.width); row.put("sideRatio", design.sideRatio);
            row.put("solver", useOriginal ? "original-bcCore-" + CORE_HASH : "demo-planar-v1"); records.append(row); toast("施工点已保存");
        } catch (Exception e) { showError("保存失败：" + e.getMessage()); }
    }
    private void historyPage() {
        label("施工记录", 27, INK, true); label("本机离线保存 · 最多 500 条 · 可导出 CSV", 13, MUTED, false); gap(16);
        action("返回", false, () -> { stage = previousStage; showPage(); });
        try {
            JSONArray list = records.load();
            if (list.length() == 0) { label("还没有施工记录", 19, INK, true); label("进入模拟施工，点击“保存当前施工点”。", 14, MUTED, false); return; }
            action("导出全部记录 · CSV", true, () -> {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT); intent.setType("text/csv"); intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_TITLE, "construction-" + new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.ROOT).format(new Date()) + ".csv");
                try { startActivityForResult(intent, EXPORT); } catch (Exception e) { showError("设备没有可用的文件保存应用"); }
            });
            action("清空本机记录", false, () -> new AlertDialog.Builder(this).setTitle("清空施工记录？")
                    .setMessage("建议先导出 CSV。清空后无法撤销。").setNegativeButton("取消", null)
                    .setPositiveButton("清空", (dialog, which) -> { try { records.clear(); showPage(); } catch (Exception e) { showError(e.getMessage()); } }).show());
            label("共 " + list.length() + " 个施工点", 14, MUTED, false); gap(10);
            for (int i = list.length() - 1; i >= 0; i--) {
                JSONObject r = list.getJSONObject(i); LinearLayout card = card();
                heading(card, (r.getString("mode").equals("LEVEL") ? "找平" : "挖沟") + "  ·  " + r.getString("machine"));
                card.addView(text(r.getString("time"), 12, MUTED, false));
                card.addView(text(String.format(Locale.CHINA, "斗齿 %.3f m   /   设计 %.3f m\n高差 %+.3f m   ·   第 %d 帧", r.getDouble("tipZ"), r.getDouble("targetZ"), r.getDouble("delta"), r.getInt("frame")), 14, INK, false));
                content.addView(card); gap(10);
            }
        } catch (Exception e) { showError("读取记录失败，未覆盖原数据：" + e.getMessage()); }
    }
    @Override protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (request != EXPORT || result != RESULT_OK || data == null || data.getData() == null) return;
        try (OutputStream out = getContentResolver().openOutputStream(data.getData(), "wt")) {
            if (out == null) throw new IllegalStateException("无法打开目标文件");
            out.write(records.csv().getBytes(StandardCharsets.UTF_8)); toast("CSV 已导出");
        } catch (Exception e) { showError("导出失败：" + e.getMessage()); }
    }
    private interface ValueChange { void set(double value); }
    private void slider(LinearLayout parent, String title, double min, double max, double current, ValueChange change, String unit) {
        TextView label = text(title + "  " + String.format(Locale.CHINA, "%.2f", current) + unit, 13, INK, false); parent.addView(label);
        SeekBar seek = new SeekBar(this); seek.setMax(1000); seek.setProgress((int) Math.round((current - min) / (max - min) * 1000));
        seek.setContentDescription(title + "调节"); parent.addView(seek, new LinearLayout.LayoutParams(-1, dp(44)));
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                if (!fromUser) return; pause(); double value = min + (max - min) * progress / 1000;
                change.set(value); label.setText(title + "  " + String.format(Locale.CHINA, "%.2f", value) + unit); renderGuidance();
            }
            @Override public void onStartTrackingTouch(SeekBar s) { pause(); }
            @Override public void onStopTrackingTouch(SeekBar s) { }
        });
    }
    private void field(LinearLayout card, String key, String label, String initial, boolean numeric) {
        card.addView(text(label, 13, MUTED, false)); EditText edit = new EditText(this);
        edit.setSingleLine(true); edit.setTextSize(18); edit.setTextColor(INK);
        edit.setInputType(numeric ? InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED : InputType.TYPE_CLASS_TEXT);
        edit.setText(drafts.containsKey(key) ? drafts.get(key) : initial);
        edit.setContentDescription(label); card.addView(edit, new LinearLayout.LayoutParams(-1, dp(53))); fields.put(key, edit);
    }
    private String value(String key) { return fields.get(key).getText().toString().trim(); }
    private double number(String key) {
        try { double v = Double.parseDouble(value(key)); if (!Double.isFinite(v)) throw new NumberFormatException(); return v; }
        catch (NumberFormatException e) { fields.get(key).setError("请输入有效数字"); throw new IllegalArgumentException("请检查参数，所有数值均以米或页面所示单位输入"); }
    }
    private void rememberFields() { for (String key : fields.keySet()) drafts.put(key, value(key)); }
    private void persistFields() { rememberFields(); SharedPreferences.Editor e = prefs.edit(); for (String key : fields.keySet()) e.putString(key, value(key)); e.apply(); }
    private LinearLayout card() {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(18), dp(18), dp(18), dp(18));
        GradientDrawable bg = new GradientDrawable(); bg.setColor(Color.WHITE); bg.setCornerRadius(dp(14)); box.setBackground(bg); return box;
    }
    private TextView text(String value, int sp, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(sp); t.setTextColor(color); t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD); t.setPadding(0, dp(3), 0, dp(3)); return t;
    }
    private void label(String value, int sp, int color, boolean bold) { content.addView(text(value, sp, color, bold)); }
    private void heading(LinearLayout box, String value) { TextView title = text(value, 17, INK, true); title.setPadding(0, 0, 0, dp(12)); box.addView(title); }
    private Button button(String title, boolean primary) {
        Button b = new Button(this); b.setText(title); b.setAllCaps(false); b.setTextSize(14); b.setTextColor(primary ? Color.WHITE : TEAL);
        GradientDrawable bg = new GradientDrawable(); bg.setColor(primary ? TEAL : 0xffe7efeb); bg.setCornerRadius(dp(10));
        b.setBackground(bg); b.setPadding(dp(12), dp(8), dp(12), dp(8)); b.setMinHeight(dp(48)); return b;
    }
    private void action(String title, boolean primary, Runnable runnable) {
        Button b = button(title, primary); b.setOnClickListener(v -> runnable.run());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(52)); lp.bottomMargin = dp(10); content.addView(b, lp);
    }
    private void gap(int height) { View space = new View(this); content.addView(space, new LinearLayout.LayoutParams(1, dp(height))); }
    private int dp(int n) { return Math.round(n * getResources().getDisplayMetrics().density); }
    private String f(double value) { return String.format(Locale.ROOT, "%.3f", value); }
    private void toast(String value) { Toast.makeText(this, value, Toast.LENGTH_SHORT).show(); }
    private void showError(String value) { new AlertDialog.Builder(this).setTitle("请检查").setMessage(value).setPositiveButton("知道了", null).show(); }
    @Override protected void onPause() { pause(); super.onPause(); }
    @Override protected void onDestroy() { handler.removeCallbacksAndMessages(null); closeOriginal(); super.onDestroy(); }
    @Override public void onBackPressed() {
        rememberFields(); pause(); restore = null;
        if (stage == 4) stage = previousStage; else if (stage > 1) stage--; else { super.onBackPressed(); return; }
        showPage();
    }
    @Override protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state); state.putInt("stage", stage); state.putInt("previousStage", previousStage);
        state.putString("mode", mode.name()); state.putInt("frame", frame); state.putDouble("boom", boom);
        state.putBoolean("useOriginal", useOriginal);
        state.putDouble("stick", stick); state.putDouble("bucket", bucket); state.putDouble("lateral", lateral);
        rememberFields(); for (String key : drafts.keySet()) state.putString("field_" + key, drafts.get(key));
    }
}
