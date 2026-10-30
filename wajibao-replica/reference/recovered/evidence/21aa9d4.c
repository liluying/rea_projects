/* Return/argument types refined from static ABI evidence; not original source. */

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* bingce::IMUtoNEUConverter::imuToNEU(bingce::ImuVector3D const&, bingce::ImuVector3D const&,
   double, double, double) */

RecoveryVec3d
bingce::IMUtoNEUConverter::imuToNEU
          (RecoveryVec3d *arg0,RecoveryVec3d *arg1,double arg2,double arg3,double arg4)

{
  long lVar1;
  RecoveryVec3d *arg0_00;
  RecoveryVec3d *arg1_00;
  double extraout_d0;
  double dVar2;
  RecoveryVec3d RVar4;
  RecoveryVec3d RVar5;
  RecoveryQuaternion RVar6;
  double dStack_278;
  double dStack_270;
  double dStack_268;
  double dStack_260;
  double dStack_258;
  double dStack_250;
  RecoveryQuaternion RStack_248;
  RecoveryQuaternion RStack_228;
  RecoveryVec3d RStack_208;
  undefined8 uStack_1f0;
  undefined8 uStack_1e8;
  double dStack_1e0;
  RecoveryQuaternion RStack_1d8;
  RecoveryQuaternion RStack_1b8;
  RecoveryQuaternion RStack_198;
  long lStack_178;
  double dStack_170;
  undefined8 uStack_160;
  double dStack_158;
  double dStack_150;
  double dStack_148;
  undefined1 *puStack_140;
  code *pcStack_138;
  double dStack_120;
  double dStack_118;
  double dStack_110;
  double dStack_108;
  double dStack_100;
  double dStack_f8;
  RecoveryQuaternion RStack_f0;
  RecoveryVec3d RStack_d0;
  RecoveryQuaternion RStack_b8;
  RecoveryQuaternion RStack_98;
  RecoveryQuaternion RStack_78;
  long lStack_58;
  double dVar3;

  lVar1 = tpidr_el0;
  lStack_58 = *(long *)(lVar1 + 0x28);
  func_0x0462e440(arg0->x,arg0->y,-arg0->z,&RStack_d0);
  sincos_plt(arg4 * 0.5,&dStack_118,&dStack_120);
  sincos_plt(arg3 * 0.5,&dStack_108,&dStack_110);
  sincos_plt(arg2 * 0.5,&dStack_f8,&dStack_100);
  ImuQuaternion::_ZN6bingce13ImuQuaternionC1Edddd_plt
            ((ImuQuaternion *)&RStack_78,
             dStack_f8 * dStack_108 * dStack_118 + dStack_120 * dStack_100 * dStack_110,
             dStack_118 * -(dStack_100 * dStack_108) + dStack_120 * dStack_f8 * dStack_110,
             dStack_f8 * dStack_110 * dStack_118 + dStack_120 * dStack_100 * dStack_108,
             dStack_120 * -(dStack_f8 * dStack_108) + dStack_118 * dStack_100 * dStack_110);
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9normalizeEv_plt(&RStack_78);
  RStack_f0.z = RVar6.z;
  RStack_f0.y = RVar6.y;
  RStack_f0.x = RVar6.x;
  RStack_f0.w = RVar6.w;
  RVar6 = ImuVector3D::_ZNK6bingce11ImuVector3D16toPureQuaternionEv_plt(&RStack_d0);
  RStack_78.z = RVar6.z;
  RStack_78.y = RVar6.y;
  RStack_78.x = RVar6.x;
  RStack_78.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(&RStack_f0);
  RStack_98.z = RVar6.z;
  RStack_98.y = RVar6.y;
  RStack_98.x = RVar6.x;
  RStack_98.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_f0,&RStack_78);
  RStack_b8.z = RVar6.z;
  RStack_b8.y = RVar6.y;
  RStack_b8.x = RVar6.x;
  RStack_b8.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_b8,&RStack_98);
  func_0x0462e440(RVar6.x,RVar6.y,RVar6.z,&RStack_b8);
  RStack_78.x = RStack_b8.x;
  RStack_78.w = RStack_b8.w;
  RStack_78.y = RStack_b8.y;
  RVar4 = ImuVector3D::_ZNK6bingce11ImuVector3D3addERKS0__plt((RecoveryVec3d *)&RStack_78,arg1);
  dVar3 = RVar4.z;
  dVar2 = RVar4.y;
  if (*(long *)(lVar1 + 0x28) == lStack_58) {
    return RVar4;
  }
  arg0_00 = (RecoveryVec3d *)func_0x0461e8c0();
  dStack_170 = dStack_108;
  uStack_160 = 0x3fe0000000000000;
  dStack_158 = dStack_110;
  dStack_150 = dStack_118;
  dStack_148 = dStack_120;
  pcStack_138 = neuToIMU;
  lVar1 = tpidr_el0;
  lStack_178 = *(long *)(lVar1 + 0x28);
  puStack_140 = &stack0xffffffffffffffd8;
  RVar4 = ImuVector3D::_ZNK6bingce11ImuVector3D8subtractERKS0__plt(arg0_00,arg1_00);
  RStack_208.z = RVar4.z;
  RStack_208.y = RVar4.y;
  RStack_208.x = RVar4.x;
  sincos_plt(dVar3 * 0.5,&dStack_270,&dStack_278);
  sincos_plt(dVar2 * 0.5,&dStack_260,&dStack_268);
  sincos_plt(extraout_d0 * 0.5,&dStack_250,&dStack_258);
  ImuQuaternion::_ZN6bingce13ImuQuaternionC1Edddd_plt
            ((ImuQuaternion *)&RStack_198,
             dStack_250 * dStack_260 * dStack_270 + dStack_278 * dStack_258 * dStack_268,
             dStack_270 * -(dStack_258 * dStack_260) + dStack_278 * dStack_250 * dStack_268,
             dStack_250 * dStack_268 * dStack_270 + dStack_278 * dStack_258 * dStack_260,
             dStack_278 * -(dStack_250 * dStack_260) + dStack_270 * dStack_258 * dStack_268);
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9normalizeEv_plt(&RStack_198);
  RStack_228.z = RVar6.z;
  RStack_228.y = RVar6.y;
  RStack_228.x = RVar6.x;
  RStack_228.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(&RStack_228);
  RStack_248.z = RVar6.z;
  RStack_248.y = RVar6.y;
  RStack_248.x = RVar6.x;
  RStack_248.w = RVar6.w;
  RVar6 = ImuVector3D::_ZNK6bingce11ImuVector3D16toPureQuaternionEv_plt(&RStack_208);
  RStack_198.z = RVar6.z;
  RStack_198.y = RVar6.y;
  RStack_198.x = RVar6.x;
  RStack_198.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(&RStack_248);
  RStack_1b8.z = RVar6.z;
  RStack_1b8.y = RVar6.y;
  RStack_1b8.x = RVar6.x;
  RStack_1b8.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_248,&RStack_198);
  RStack_1d8.z = RVar6.z;
  RStack_1d8.y = RVar6.y;
  RStack_1d8.x = RVar6.x;
  RStack_1d8.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_1d8,&RStack_1b8);
  func_0x0462e440(RVar6.x,RVar6.y,RVar6.z,&RStack_1d8);
  func_0x0462e440(RStack_1d8.w,RStack_1d8.x,-RStack_1d8.y,&uStack_1f0);
  RVar4.y = (double)uStack_1e8;
  RVar4.x = (double)uStack_1f0;
  RVar4.z = dStack_1e0;
  if (*(long *)(lVar1 + 0x28) == lStack_178) {
    return RVar4;
  }
  dVar2 = (double)func_0x0461e8c0();
  RVar5.x = (dVar2 * _UNK_017e8938) / 180.0;
  RVar5.y = 180.0;
  RVar5.z = dStack_1e0;
  return RVar5;
}
