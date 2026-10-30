package com.rea.wajibao.validation;
import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.virtualmodule.android.AndroidModule;
import com.rea.wajibao.replica.nativecore.*;
import org.json.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.HexFormat;
public class NativeSmoke {
 public static void main(String[] args) throws Exception {
  Path lib=Path.of(args[0]),assets=Path.of(args[1]),output=Path.of(args[2]);
  String hash=HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(lib)));
  if(!hash.equals("443a89c357df2277ea3e604db699188fc0b52eba3cd2306332f4e544b49bfa32"))throw new IllegalArgumentException("Unexpected library");
  OriginalProfile profile=new OriginalProfile(Files.readString(assets.resolve("native-profile.json")));
  JSONObject replay=new JSONObject(Files.readString(assets.resolve("native-replay.json")));JSONArray samples=new JSONArray();
  JSONObject report=new JSONObject().put("librarySha256",hash).put("backend","Unidbg 0.9.8 / Unicorn2 ARM64 / AndroidResolver23").put("actualOriginalNativeCodeExecuted",true).put("originalAppUiCompared",false).put("workMode",profile.workMode).put("nativeBinaryModified",false);
  try(AndroidEmulator e=AndroidEmulatorBuilder.for64Bit().setProcessName("com.rea.wajibao.replica.validation").addBackendFactory(new Unicorn2Factory(true)).build()) {
   e.getMemory().setLibraryResolver(new AndroidResolver(23)); VM vm=e.createDalvikVM();vm.setJni(new AbstractJni(){});vm.setVerbose(false);
   new AndroidModule(e,vm).register(e.getMemory()); DalvikModule loaded=vm.loadLibrary(lib.toFile(),true);
   EmulatedWajiBackend backend=new EmulatedWajiBackend(e,vm,loaded.getModule());
   boolean demo=backend.demoAuthorized(),work=backend.workAuthorized();
   report.put("originalLibraryLoaded",true).put("demoAuthorized",demo).put("workAuthorized",work);
   DvmClass cls=vm.resolveClass("com/bingce/wajibao/wajibaoModule");
   String[] constants={"RTK_ON_STICK_WIRELESS_ON_BUCKET","RTK_ON_STICK_NO_SENSOR","RTK_ON_BOOM_WIRELESS_ON_STICK_BUCKET","RTK_ON_BOOM_WIRED_ON_STICK_BUCKET","RTK_ON_BOOM_WIRED_ON_STICK","DUAL_ANTENNA_ON_BOOM_WIRED_ON_STICK_BUCKET","RTK_ON_ATTACHMENT_NO_SENSOR","DUAL_ANTENNA_ON_GRAB_NO_SENSOR","DUAL_ANTENNA_ON_BODY_WIRED_ON_BODY_BOOM_STICK_BUCKET","DUAL_ANTENNA_ON_BOOM_WIRED_ON_BOOM_STICK_BUCKET"};
   for(int i=0;i<constants.length;i++)if(cls.callStaticJniMethodInt(e,"WajiInner_"+constants[i]+"_get()I")!=i)throw new IllegalStateException("Mode constant mismatch");
   report.put("allTenModeConstantsVerified",true);
   for(String mode:new String[]{"level","trenching"}) {
    JSONArray frames=replay.getJSONObject(mode).getJSONArray("frames"); long ptr=backend.create();
    if(ptr==0)throw new IllegalStateException("Null WajiInner");
    try {
     if(!backend.initMachine(ptr,profile)||!backend.initAttachment(ptr,profile))throw new IllegalStateException("Native initialization rejected");
     double ab=backend.configuredValue(ptr,"Ab"); if(Math.abs(ab-profile.machineValues()[0])>1e-12)throw new IllegalStateException("FP parameter/return mismatch: "+ab);
     JSONObject checked=new JSONObject();
     String[] getters={"Ab","Pq","Sw","AkCorrect","ZkCorrect"}; int[] slots={0,10,11,24,25};
     for(int n=0;n<getters.length;n++) {double value=backend.configuredValue(ptr,getters[n]);if(Math.abs(value-profile.machineValues()[slots[n]])>1e-12)throw new IllegalStateException("Spilled FP argument mismatch "+getters[n]+": "+value);checked.put(getters[n],value);}
     report.put("checkedMachineArguments",checked);
     report.put("machineInitialized",true).put("attachmentInitialized",true).put("configuredAb",ab).put("aapcs64PackingChecked",true);
     for(int i=0;i<Math.min(frames.length(),8);i++){
      OriginalFrame frame=new OriginalFrame(frames.getJSONObject(i));
      // Diagnostic invokes the unmodified function to observe its existing feature gate.
      backend.calculate(ptr,frame); WajiBackend.Result r=backend.result(ptr);
      boolean zero=r.x==0&&r.y==0&&r.z==0;
      if(!demo&&!zero)throw new IllegalStateException("Unexpected unlicensed result");
      samples.put(new JSONObject().put("mode",mode).put("frameIndex",i).put("x",r.x).put("y",r.y).put("z",r.z).put("validForConstruction",demo&&!zero));
     }
    } finally {backend.delete(ptr);}
   }
   OriginalFrame first=new OriginalFrame(replay.getJSONObject("level").getJSONArray("frames").getJSONObject(0));
   if(!demo){
    boolean blocked=false;try(OriginalSession guarded=new OriginalSession(backend,profile)) {try{guarded.calculate(first);}catch(NativeAuthorizationException expected){blocked=true;}}
    if(!blocked)throw new IllegalStateException("Application guard failed");report.put("unauthorizedApplicationComputeBlocked",true);
   }
   report.put("status",demo?"NATIVE_OUTPUT_REQUIRES_APP_COMPARISON":"BLOCKED_SDK_AUTHORIZATION").put("nativeCalculationValidated",false);
  }
  report.put("diagnosticsPassed",true).put("rawDiagnosticCalls",samples).put("sampleCount",samples.length());
  Files.createDirectories(output.toAbsolutePath().getParent());Files.writeString(output,report.toString(2));System.out.println(report.toString(2));
 }
}
