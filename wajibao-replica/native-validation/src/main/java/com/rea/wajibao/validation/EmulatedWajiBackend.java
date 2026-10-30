package com.rea.wajibao.validation;
import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.linux.android.dvm.*;
import com.rea.wajibao.replica.nativecore.*;
import java.util.*;
import java.nio.*;
import unicorn.Arm64Const;
import com.github.unidbg.Module;
import com.github.unidbg.Symbol;
final class EmulatedWajiBackend implements WajiBackend {
 private final AndroidEmulator emulator;
 private final DvmClass waji, coord;
 private final VM vm; private final Module module;
 private static final String HANDLE="JLcom/bingce/wajibao/WajiInner;";
 EmulatedWajiBackend(AndroidEmulator e, VM vm, Module module) {
  this.vm=vm;this.module=module;
  emulator=e;waji=vm.resolveClass("com/bingce/wajibao/wajibaoModule");
  coord=vm.resolveClass("com/bingce/coordlib/proj4j/model/coordModelModule");
 }
 @Override public boolean demoAuthorized() { return vm.resolveClass("com/bingce/utils/SdkUtils").callStaticJniMethodBoolean(emulator,"isWjbEmulatorValidate()Z"); }
 boolean workAuthorized() { return vm.resolveClass("com/bingce/utils/SdkUtils").callStaticJniMethodBoolean(emulator,"isWjbValidate()Z"); }
 double configuredValue(long ptr,String name) { waji.callStaticJniMethod(emulator,"WajiInner_get"+name+"("+HANDLE+")D",ptr,null); return readD0(); }
 @Override public long create() { return waji.callStaticJniMethodLong(emulator,"new_WajiInner()J"); }
 // Unidbg 0.9.8 places every Double in a vector register, even after D7.
 // Pack AAPCS64 ourselves: independent GP/FP registers and ordered stack spills.
 private Number invoke(String method,Object[] gp,double[] fp) {
  List<Object> args=new ArrayList<>(); args.add(vm.getJNIEnv()); args.add(0);
  args.addAll(Arrays.asList(gp));
  while(args.size()<8)args.add(0);
  for(int i=8;i<fp.length;i++)args.add(Double.doubleToRawLongBits(fp[i]));
  // Any GP arguments after x7 spill after the earlier FP arguments for initMachine.
  if(args.size()>8 && gp.length>6)throw new IllegalStateException("Use explicit spill path");
  for(int i=0;i<Math.min(8,fp.length);i++)args.add(fp[i]);
  Symbol symbol=module.findSymbolByName("Java_com_bingce_wajibao_wajibaoModule_"+method.replace("_","_1"));
  if(symbol==null)throw new IllegalStateException(method);
  return symbol.call(emulator,args.toArray());
 }
 @Override public boolean initMachine(long ptr,OriginalProfile c) {
  double[] v=c.machineValues();int[] s=c.installSides();
  List<Object> args=new ArrayList<>(Arrays.asList(vm.getJNIEnv(),0,ptr,0,c.workMode,s[0],s[1],s[2]));
  for(int i=8;i<v.length;i++)args.add(Double.doubleToRawLongBits(v[i]));
  args.add(s[3]);args.add(c.primaryAntennaFront?1:0);
  for(int i=0;i<8;i++)args.add(v[i]);
  return module.findSymbolByName("Java_com_bingce_wajibao_wajibaoModule_WajiInner_1initMachine").call(emulator,args.toArray()).intValue()!=0;
 }
 @Override public boolean initAttachment(long ptr,OriginalProfile c) {
  return invoke("WajiInner_initAttachment",new Object[]{ptr,0,c.attachmentMode},c.attachmentValues()).intValue()!=0;
 }
 @Override public void calculate(long ptr,OriginalFrame f) {
  double[] p=f.poseValues(),a=f.antennaValues(),all=Arrays.copyOf(p,18);System.arraycopy(a,0,all,14,4);
  invoke("WajiInner_calculate",new Object[]{ptr,0,f.bucketMode,f.demo?1:0},all);
 }
 private long point(long ptr,String which) { return waji.callStaticJniMethodLong(emulator,"WajiInner_get"+which+"("+HANDLE+")J",ptr,null); }
 private double coordinate(long point,String axis) {
  if(point==0)throw new IllegalStateException("Null native coordinate");
  coord.callStaticJniMethod(emulator,"CCoordinate_get"+axis+"(JLcom/bingce/coordlib/proj4j/model/CCoordinate;)D",point,null);
  double actual=com.github.unidbg.pointer.UnidbgPointer.pointer(emulator,point).getDouble(axis.equals("X")?0x18:axis.equals("Y")?0x20:0x28);
  double jni=readD0(); if(Double.doubleToLongBits(actual)!=Double.doubleToLongBits(jni))throw new IllegalStateException("JNI FP result disagrees with its known memory load"); return jni;
 }
 private double readD0(){return ByteBuffer.wrap(emulator.getBackend().reg_read_vector(Arm64Const.UC_ARM64_REG_Q0)).order(ByteOrder.LITTLE_ENDIAN).getDouble();}
 @Override public Result result(long ptr) {
  long center=point(ptr,"BucketCenterBottom"),left=point(ptr,"BucketLeftBottom"),right=point(ptr,"BucketRightBottom");
  double x=coordinate(center,"X"),y=coordinate(center,"Y"),z=coordinate(center,"Z"),lz=coordinate(left,"Z"),rz=coordinate(right,"Z");
  waji.callStaticJniMethod(emulator,"WajiInner_getWjFrontAzimuth("+HANDLE+")D",ptr,null);double heading=readD0();
  return new Result(x,y,z,heading,lz,rz);
 }
 @Override public void delete(long ptr){waji.callStaticJniMethod(emulator,"delete_WajiInner(J)V",ptr);}
}
