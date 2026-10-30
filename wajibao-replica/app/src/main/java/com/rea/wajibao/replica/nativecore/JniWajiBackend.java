package com.rea.wajibao.replica.nativecore;
import com.bingce.wajibao.wajibaoModule;
import com.bingce.coordlib.proj4j.model.coordModelModule;

/** Calls the unchanged original ARM64 library through its original exported JNI names. */
public final class JniWajiBackend implements WajiBackend {
 public JniWajiBackend() { System.loadLibrary("bcCore"); }
 @Override public boolean demoAuthorized() { return com.bingce.utils.SdkUtils.isWjbEmulatorValidate(); }
 @Override public long create() { return wajibaoModule.new_WajiInner(); }
 @Override public boolean initMachine(long pointer, OriginalProfile c) {
  double[] v=c.machineValues(); int[] s=c.installSides();
  return wajibaoModule.WajiInner_initMachine(pointer,null,c.workMode,v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8], v[9], v[10], v[11], v[12], v[13], v[14], v[15], v[16], v[17], v[18], v[19], v[20], v[21], v[22], v[23], v[24], v[25], v[26], v[27], v[28], v[29], v[30], v[31],s[0],s[1],s[2],s[3],c.primaryAntennaFront);
 }
 @Override public boolean initAttachment(long pointer, OriginalProfile c) {
  double[] v=c.attachmentValues();
  return wajibaoModule.WajiInner_initAttachment(pointer,null,c.attachmentMode,v[0], v[1], v[2], v[3], v[4], v[5], v[6]);
 }
 @Override public void calculate(long pointer, OriginalFrame f) {
  double[] p=f.poseValues(), a=f.antennaValues();
  wajibaoModule.WajiInner_calculate(pointer,null,p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8], p[9], p[10], p[11], p[12], p[13],f.bucketMode,a[0], a[1], a[2], a[3],f.demo);
 }
 @Override public Result result(long pointer) {
  long center=wajibaoModule.WajiInner_getBucketCenterBottom(pointer,null);
  long left=wajibaoModule.WajiInner_getBucketLeftBottom(pointer,null);
  long right=wajibaoModule.WajiInner_getBucketRightBottom(pointer,null);
  if(center==0 || left==0 || right==0) throw new IllegalStateException("原版核心未产生斗底坐标");
  return new Result(coordModelModule.CCoordinate_getX(center,null),coordModelModule.CCoordinate_getY(center,null),
   coordModelModule.CCoordinate_getZ(center,null),wajibaoModule.WajiInner_getWjFrontAzimuth(pointer,null),
   coordModelModule.CCoordinate_getZ(left,null),coordModelModule.CCoordinate_getZ(right,null));
 }
 @Override public void delete(long pointer) { wajibaoModule.delete_WajiInner(pointer); }
}
