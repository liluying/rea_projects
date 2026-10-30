/* Return/argument types refined from static ABI evidence; not original source. */

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* bingce::IMUtoNEUConverter::neuToIMU(bingce::ImuVector3D const&, bingce::ImuVector3D const&,
   double, double, double) */

RecoveryVec3d
bingce::IMUtoNEUConverter::neuToIMU
          (RecoveryVec3d *arg0,RecoveryVec3d *arg1,double arg2,double arg3,double arg4)

{
  long lVar1;
  double dVar2;
  RecoveryVec3d RVar3;
  RecoveryVec3d RVar4;
  RecoveryQuaternion RVar5;
  double dStack_158;
  double dStack_150;
  double dStack_148;
  double dStack_140;
  double dStack_138;
  double dStack_130;
  RecoveryQuaternion RStack_128;
  RecoveryQuaternion RStack_108;
  RecoveryVec3d RStack_e8;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  double dStack_c0;
  RecoveryQuaternion RStack_b8;
  RecoveryQuaternion RStack_98;
  RecoveryQuaternion RStack_78;
  long lStack_58;

  lVar1 = tpidr_el0;
  lStack_58 = *(long *)(lVar1 + 0x28);
  RVar3 = ImuVector3D::_ZNK6bingce11ImuVector3D8subtractERKS0__plt(arg0,arg1);
  RStack_e8.z = RVar3.z;
  RStack_e8.y = RVar3.y;
  RStack_e8.x = RVar3.x;
  sincos_plt(arg4 * 0.5,&dStack_150,&dStack_158);
  sincos_plt(arg3 * 0.5,&dStack_140,&dStack_148);
  sincos_plt(arg2 * 0.5,&dStack_130,&dStack_138);
  ImuQuaternion::_ZN6bingce13ImuQuaternionC1Edddd_plt
            ((ImuQuaternion *)&RStack_78,
             dStack_130 * dStack_140 * dStack_150 + dStack_158 * dStack_138 * dStack_148,
             dStack_150 * -(dStack_138 * dStack_140) + dStack_158 * dStack_130 * dStack_148,
             dStack_130 * dStack_148 * dStack_150 + dStack_158 * dStack_138 * dStack_140,
             dStack_158 * -(dStack_130 * dStack_140) + dStack_150 * dStack_138 * dStack_148);
  RVar5 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9normalizeEv_plt(&RStack_78);
  RStack_108.z = RVar5.z;
  RStack_108.y = RVar5.y;
  RStack_108.x = RVar5.x;
  RStack_108.w = RVar5.w;
  RVar5 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(&RStack_108);
  RStack_128.z = RVar5.z;
  RStack_128.y = RVar5.y;
  RStack_128.x = RVar5.x;
  RStack_128.w = RVar5.w;
  RVar5 = ImuVector3D::_ZNK6bingce11ImuVector3D16toPureQuaternionEv_plt(&RStack_e8);
  RStack_78.z = RVar5.z;
  RStack_78.y = RVar5.y;
  RStack_78.x = RVar5.x;
  RStack_78.w = RVar5.w;
  RVar5 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(&RStack_128);
  RStack_98.z = RVar5.z;
  RStack_98.y = RVar5.y;
  RStack_98.x = RVar5.x;
  RStack_98.w = RVar5.w;
  RVar5 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_128,&RStack_78);
  RStack_b8.z = RVar5.z;
  RStack_b8.y = RVar5.y;
  RStack_b8.x = RVar5.x;
  RStack_b8.w = RVar5.w;
  RVar5 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_b8,&RStack_98);
  func_0x0462e440(RVar5.x,RVar5.y,RVar5.z,&RStack_b8);
  func_0x0462e440(RStack_b8.w,RStack_b8.x,-RStack_b8.y,&uStack_d0);
  RVar3.y = (double)uStack_c8;
  RVar3.x = (double)uStack_d0;
  RVar3.z = dStack_c0;
  if (*(long *)(lVar1 + 0x28) == lStack_58) {
    return RVar3;
  }
  dVar2 = (double)func_0x0461e8c0();
  RVar4.x = (dVar2 * _UNK_017e8938) / 180.0;
  RVar4.y = 180.0;
  RVar4.z = dStack_c0;
  return RVar4;
}
