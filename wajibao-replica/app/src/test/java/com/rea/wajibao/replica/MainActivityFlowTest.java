package com.rea.wajibao.replica;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;
import java.time.Duration;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
@LooperMode(LooperMode.Mode.PAUSED)
public class MainActivityFlowTest {
    @Before public void clearData() throws Exception {
        Application app = RuntimeEnvironment.getApplication();
        app.getSharedPreferences("replica", Context.MODE_PRIVATE).edit().clear().commit();
        app.getSharedPreferences("replica", Context.MODE_PRIVATE).edit().putBoolean("useOriginal", false).commit();
        new RecordStore(app).clear();
    }
    private View find(View root, java.util.function.Predicate<View> predicate) {
        if (predicate.test(root)) return root;
        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            for (int i = 0; i < group.getChildCount(); i++) {
                View found = find(group.getChildAt(i), predicate); if (found != null) return found;
            }
        }
        return null;
    }
    private void click(MainActivity a, String label) {
        View view = find(a.getWindow().getDecorView(), v -> v instanceof Button && ((Button) v).getText().toString().equals(label));
        assertNotNull("Missing action: " + label, view); assertTrue(view.performClick());
    }
    private void edit(MainActivity a, String label, String text) {
        View view = find(a.getWindow().getDecorView(), v -> v instanceof EditText && label.contentEquals(v.getContentDescription()));
        assertNotNull(label, view); ((EditText) view).setText(text);
    }
    @Test public void levelAndTrenchSavePersistentRecordsAndExportCsv() throws Exception {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).setup()) {
            MainActivity a = controller.get();
            click(a, "下一步 · 设计参数"); click(a, "进入模拟施工"); click(a, "下一帧"); click(a, "保存当前施工点");
            RecordStore store = new RecordStore(a); JSONArray rows = store.load();
            assertEquals(1, rows.length()); assertEquals("LEVEL", rows.getJSONObject(0).getString("mode"));
            assertEquals(2, rows.getJSONObject(0).getInt("frame"));
            click(a, "返回修改设计"); click(a, "○ 挖沟 · 梯形横断面");
            edit(a, "沟深  m", "1.5"); click(a, "进入模拟施工"); click(a, "保存当前施工点");
            rows = store.load(); assertEquals(2, rows.length());
            JSONObject trench = rows.getJSONObject(1); assertEquals("TRENCH", trench.getString("mode"));
            assertEquals(75.3, trench.getDouble("targetZ"), 1e-9);
            assertEquals(trench.getDouble("tipZ") - trench.getDouble("targetZ"), trench.getDouble("delta"), 1e-9);
            assertTrue(store.csv().contains("demo-planar-v1"));
            click(a, "施工记录"); click(a, "返回"); click(a, "保存当前施工点"); assertEquals(3, store.load().length());
        }
    }
    @Test public void invalidDraftDoesNotCorruptPersistedConfiguration() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).setup()) {
            MainActivity a = controller.get(); click(a, "下一步 · 设计参数");
            click(a, "○ 挖沟 · 梯形横断面"); edit(a, "沟深  m", "-3");
            click(a, "○ 找平 · 水平设计面");
            assertFalse(a.getSharedPreferences("replica", Context.MODE_PRIVATE).contains("depth"));
            controller.recreate();
            click(controller.get(), "进入模拟施工"); click(controller.get(), "保存当前施工点");
        }
    }
    @Test public void playbackStopsWhileActivityIsPaused() throws Exception {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).setup()) {
            MainActivity a = controller.get(); click(a, "下一步 · 设计参数"); click(a, "进入模拟施工");
            click(a, "播放演示"); org.robolectric.Shadows.shadowOf(Looper.getMainLooper()).idleFor(Duration.ofMillis(600));
            click(a, "保存当前施工点"); int before = new RecordStore(a).load().getJSONObject(0).getInt("frame"); assertTrue(before > 1);
            controller.pause(); org.robolectric.Shadows.shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(5)); controller.resume();
            click(a, "保存当前施工点"); int after = new RecordStore(a).load().getJSONObject(1).getInt("frame"); assertEquals(before, after);
        }
    }
    @Test public void rotationPreservesLiveFrameAndDesign() throws Exception {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).setup()) {
            MainActivity a = controller.get(); click(a, "下一步 · 设计参数"); edit(a, "设计高程  m", "72.5");
            click(a, "进入模拟施工"); click(a, "下一帧"); controller.recreate();
            click(controller.get(), "保存当前施工点"); JSONObject row = new RecordStore(controller.get()).load().getJSONObject(0);
            assertEquals(72.5, row.getDouble("targetZ"), 0); assertEquals(2, row.getInt("frame"));
        }
    }
    @Test public void unavailableOriginalCoreNeverSavesFakeNativeRecords() throws Exception {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).setup()) {
            MainActivity a = controller.get(); click(a, "○ 原版核心 · ARM64");
            click(a, "下一步 · 设计参数"); click(a, "进入模拟施工");
            assertNotNull(org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog());
            assertEquals(0, new RecordStore(a).load().length());
            assertNull(find(a.getWindow().getDecorView(), v -> v instanceof Button && ((Button) v).getText().toString().equals("保存当前施工点")));
            assertTrue(a.getSharedPreferences("replica", Context.MODE_PRIVATE).getBoolean("useOriginal", false));
        }
    }
}
