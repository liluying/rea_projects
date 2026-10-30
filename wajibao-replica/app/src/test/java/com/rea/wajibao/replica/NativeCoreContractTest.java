package com.rea.wajibao.replica;

import com.rea.wajibao.replica.nativecore.*;
import org.json.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import java.util.*;
import java.lang.reflect.Modifier;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class NativeCoreContractTest {
    private String profileText() throws Exception { return OriginalReplayController.asset(RuntimeEnvironment.getApplication(), "native-profile.json"); }
    private OriginalProfile profile() throws Exception { return new OriginalProfile(profileText()); }
    private OriginalFrame frame() throws Exception {
        JSONObject replay = new JSONObject(OriginalReplayController.asset(RuntimeEnvironment.getApplication(), "native-replay.json"));
        return new OriginalFrame(replay.getJSONObject("trenching").getJSONArray("frames").getJSONObject(0));
    }
    private static class Fake implements WajiBackend {
        final List<String> calls = new ArrayList<>(); boolean authorized = true, machineOk = true, attachmentOk = true;
        int deleted, calculated; Result answer = new Result(1,2,3,.1,3.1,2.9);
        public boolean demoAuthorized() { calls.add("authorized"); return authorized; }
        public long create() { calls.add("create"); return 42; }
        public boolean initMachine(long ptr, OriginalProfile p) { assertEquals(42,ptr); calls.add("machine"); return machineOk; }
        public boolean initAttachment(long ptr, OriginalProfile p) { calls.add("attachment"); return attachmentOk; }
        public void calculate(long ptr, OriginalFrame f) { calls.add("calculate"); calculated++; }
        public Result result(long ptr) { calls.add("result"); return answer; }
        public void delete(long ptr) { calls.add("delete"); deleted++; }
    }
    @Test public void preservesAllMachineAndAttachmentParametersInObservedOrder() throws Exception {
        OriginalProfile p = profile(); double[] a = p.machineValues();
        assertEquals(32, a.length); assertEquals(.21,a[0],0); assertEquals(1.7,a[7],0);
        assertEquals(-1.51614941273146,a[25],1e-12); assertEquals(7,p.attachmentValues().length);
        assertEquals(1.0893885613040004,p.attachmentValues()[2],1e-12);
        a[0] = 999; assertEquals(.21,p.machineValues()[0],0);
        assertEquals(5,p.workMode); assertArrayEquals(new int[]{0,0,0,0},p.installSides());
    }
    @Test public void convertsSensorDegreesButPreservesPositionAndAntennaMetres() throws Exception {
        OriginalFrame f = frame(); double[] p = f.poseValues();
        assertEquals(3844279.8071508985,p[0],1e-9); assertEquals(77.5203839427647,p[2],1e-9);
        assertEquals(Math.toRadians(180.7697),p[5],1e-12);
        assertEquals(Math.toRadians(31.18),p[10],1e-12); assertEquals(Math.toRadians(71.84),p[12],1e-12);
        assertArrayEquals(new double[]{.054,.054,0,.7805},f.antennaValues(),1e-12);
        assertEquals(2,f.bucketMode); assertTrue(f.demo);
    }
    @Test public void nativeSignaturesHaveExactArityAndNativeModifier() {
        Map<String,Integer> counts = new HashMap<>(); counts.put("WajiInner_initMachine",40); counts.put("WajiInner_initAttachment",10); counts.put("WajiInner_calculate",22);
        for (java.lang.reflect.Method method : com.bingce.wajibao.wajibaoModule.class.getDeclaredMethods()) {
            if (counts.containsKey(method.getName())) { assertEquals((int)counts.remove(method.getName()), method.getParameterTypes().length); assertTrue(Modifier.isNative(method.getModifiers())); }
        }
        assertTrue(counts.isEmpty());
    }
    @Test public void callsNativeBackendInOrderAndClosesExactlyOnce() throws Exception {
        Fake b = new Fake(); OriginalSession s = new OriginalSession(b,profile());
        assertSame(b.answer,s.calculate(frame())); s.close(); s.close();
        assertEquals(Arrays.asList("create","machine","attachment","authorized","calculate","result","delete"),b.calls);
        assertEquals(1,b.deleted); assertThrows(IllegalStateException.class, () -> s.calculate(frame()));
    }
    @Test public void missingAuthorizationStopsBeforeCalculationWithNoFallback() throws Exception {
        Fake b = new Fake(); b.authorized = false;
        try (OriginalSession s = new OriginalSession(b,profile())) { assertThrows(NativeAuthorizationException.class, () -> s.calculate(frame())); }
        assertEquals(0,b.calculated); assertEquals(1,b.deleted); assertFalse(b.calls.contains("result"));
    }
    @Test public void invalidInitializationClosesNativeObjectAndStops() throws Exception {
        Fake b = new Fake(); b.attachmentOk = false;
        assertThrows(IllegalArgumentException.class, () -> new OriginalSession(b,profile())); assertEquals(1,b.deleted);
        assertEquals(0,b.calculated);
    }
    @Test public void rejectsZeroOutputsForNonzeroInputAndNonfiniteNativeResults() throws Exception {
        Fake b = new Fake(); b.answer = new WajiBackend.Result(0,0,0,0,0,0);
        try (OriginalSession s = new OriginalSession(b,profile())) { assertThrows(IllegalStateException.class, () -> s.calculate(frame())); }
        assertThrows(IllegalStateException.class, () -> new WajiBackend.Result(1,2,Double.NaN,0,0,0));
    }
    @Test public void malformedProfileCannotSilentlyDefaultMissingCalibration() throws Exception {
        JSONObject p = new JSONObject(profileText()); p.getJSONObject("machine").remove("zkCorrect");
        final String missing = p.toString();
        assertThrows(JSONException.class, () -> new OriginalProfile(missing));
        p = new JSONObject(profileText()); p.put("workMode", 10); final String invalid = p.toString();
        assertThrows(IllegalArgumentException.class, () -> new OriginalProfile(invalid));
    }
}
