/* Return/argument types refined from static ABI evidence; not original source. */

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* bingce::IMUtoNEUConverter::eulerToQuaternion(double, double, double) */

RecoveryQuaternion bingce::IMUtoNEUConverter::eulerToQuaternion(double arg0,double arg1,double arg2)

{
  long lVar1;
  undefined8 *puVar2;
  RecoveryVec3d *arg0_00;
  RecoveryVec3d *arg1_00;
  RecoveryVec3d *arg1_01;
  double extraout_d0;
  double extraout_d0_00;
  double dVar3;
  double dVar4;
  RecoveryVec3d RVar5;
  RecoveryQuaternion RVar6;
  RecoveryQuaternion RVar7;
  RecoveryQuaternion RVar8;
  RecoveryQuaternion RVar9;
  undefined1 auVar10 [16];
  double dStack_3b8;
  double dStack_3b0;
  double dStack_3a8;
  double dStack_3a0;
  double dStack_398;
  double dStack_390;
  RecoveryQuaternion RStack_388;
  RecoveryQuaternion RStack_368;
  RecoveryVec3d RStack_348;
  undefined8 uStack_330;
  undefined8 uStack_328;
  undefined8 uStack_320;
  RecoveryQuaternion RStack_318;
  RecoveryQuaternion RStack_2f8;
  RecoveryQuaternion RStack_2d8;
  long lStack_2b8;
  double dStack_2b0;
  undefined8 uStack_2a0;
  double dStack_298;
  double dStack_290;
  double dStack_288;
  undefined8 **ppuStack_280;
  code *pcStack_278;
  double dStack_260;
  double dStack_258;
  double dStack_250;
  double dStack_248;
  double dStack_240;
  double dStack_238;
  RecoveryQuaternion RStack_230;
  RecoveryVec3d RStack_210;
  RecoveryQuaternion RStack_1f8;
  RecoveryQuaternion RStack_1d8;
  RecoveryQuaternion RStack_1b8;
  long lStack_198;
  double dStack_190;
  double dStack_188;
  undefined8 uStack_180;
  double dStack_178;
  double dStack_170;
  undefined1 **ppuStack_168;
  code *pcStack_160;
  RecoveryQuaternion RStack_140;
  RecoveryQuaternion RStack_120;
  RecoveryQuaternion RStack_100;
  undefined8 uStack_e0;
  double dStack_d8;
  double dStack_d0;
  long lStack_c8;
  undefined1 *puStack_c0;
  code *pcStack_b8;
  double dStack_98;
  double dStack_90;
  double dStack_88;
  double dStack_80;
  double dStack_78;
  double dStack_70;
  RecoveryQuaternion RStack_68;
  long lStack_48;

  lVar1 = tpidr_el0;
  lStack_48 = *(long *)(lVar1 + 0x28);
  sincos_plt(arg2 * 0.5,&dStack_90,&dStack_98);
  sincos_plt(arg1 * 0.5,&dStack_80,&dStack_88);
  sincos_plt(arg0 * 0.5,&dStack_70,&dStack_78);
  ImuQuaternion::_ZN6bingce13ImuQuaternionC1Edddd_plt
            ((ImuQuaternion *)&RStack_68,
             dStack_70 * dStack_80 * dStack_90 + dStack_98 * dStack_78 * dStack_88,
             dStack_90 * -(dStack_78 * dStack_80) + dStack_98 * dStack_70 * dStack_88,
             dStack_70 * dStack_88 * dStack_90 + dStack_98 * dStack_78 * dStack_80,
             dStack_98 * -(dStack_70 * dStack_80) + dStack_90 * dStack_78 * dStack_88);
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9normalizeEv_plt(&RStack_68);
  if (*(long *)(lVar1 + 0x28) == lStack_48) {
    return RVar6;
  }
  auVar10 = func_0x0461e8c0();
  pcStack_b8 = rotateVectorByQuaternion;
  lVar1 = tpidr_el0;
  lStack_c8 = *(long *)(lVar1 + 0x28);
  puStack_c0 = &stack0xffffffffffffffe8;
  RVar6 = ImuVector3D::_ZNK6bingce11ImuVector3D16toPureQuaternionEv_plt(auVar10._8_8_);
  RStack_100.z = RVar6.z;
  RStack_100.y = RVar6.y;
  RStack_100.x = RVar6.x;
  RStack_100.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(auVar10._0_8_);
  RStack_120.z = RVar6.z;
  RStack_120.y = RVar6.y;
  RStack_120.x = RVar6.x;
  RStack_120.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(auVar10._0_8_,&RStack_100);
  RStack_140.z = RVar6.z;
  RStack_140.y = RVar6.y;
  RStack_140.x = RVar6.x;
  RStack_140.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_140,&RStack_120);
  dVar3 = RVar6.z;
  func_0x0462e440(RVar6.x,RVar6.y,dVar3,&uStack_e0);
  RVar6.x = dStack_d8;
  RVar6.w = (double)uStack_e0;
  if (*(long *)(lVar1 + 0x28) == lStack_c8) {
    RVar6.z = dVar3;
    RVar6.y = dStack_d0;
    return RVar6;
  }
  dVar3 = dStack_d8;
  RVar7.y = dStack_d0;
  puVar2 = (undefined8 *)func_0x0461e8c0();
  dStack_190 = dStack_80;
  dStack_188 = dStack_98;
  uStack_180 = 0x3fe0000000000000;
  dStack_178 = dStack_88;
  dStack_170 = dStack_90;
  pcStack_160 = imuToNEU;
  lVar1 = tpidr_el0;
  lStack_198 = *(long *)(lVar1 + 0x28);
  ppuStack_168 = &puStack_c0;
  func_0x0462e440(*puVar2,puVar2[1],-(double)puVar2[2],&RStack_210);
  sincos_plt(RVar7.y * 0.5,&dStack_258,&dStack_260);
  sincos_plt(dVar3 * 0.5,&dStack_248,&dStack_250);
  sincos_plt(extraout_d0 * 0.5,&dStack_238,&dStack_240);
  ImuQuaternion::_ZN6bingce13ImuQuaternionC1Edddd_plt
            ((ImuQuaternion *)&RStack_1b8,
             dStack_238 * dStack_248 * dStack_258 + dStack_260 * dStack_240 * dStack_250,
             dStack_258 * -(dStack_240 * dStack_248) + dStack_260 * dStack_238 * dStack_250,
             dStack_238 * dStack_250 * dStack_258 + dStack_260 * dStack_240 * dStack_248,
             dStack_260 * -(dStack_238 * dStack_248) + dStack_258 * dStack_240 * dStack_250);
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9normalizeEv_plt(&RStack_1b8);
  RStack_230.z = RVar6.z;
  RStack_230.y = RVar6.y;
  RStack_230.x = RVar6.x;
  RStack_230.w = RVar6.w;
  RVar6 = ImuVector3D::_ZNK6bingce11ImuVector3D16toPureQuaternionEv_plt(&RStack_210);
  RStack_1b8.z = RVar6.z;
  RStack_1b8.y = RVar6.y;
  RStack_1b8.x = RVar6.x;
  RStack_1b8.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(&RStack_230);
  RStack_1d8.z = RVar6.z;
  RStack_1d8.y = RVar6.y;
  RStack_1d8.x = RVar6.x;
  RStack_1d8.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_230,&RStack_1b8);
  RStack_1f8.z = RVar6.z;
  RStack_1f8.y = RVar6.y;
  RStack_1f8.x = RVar6.x;
  RStack_1f8.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_1f8,&RStack_1d8);
  dVar4 = RVar6.z;
  func_0x0462e440(RVar6.x,RVar6.y,dVar4,&RStack_1f8);
  RStack_1b8.x = RStack_1f8.x;
  RStack_1b8.w = RStack_1f8.w;
  RStack_1b8.y = RStack_1f8.y;
  RVar5 = ImuVector3D::_ZNK6bingce11ImuVector3D3addERKS0__plt((RecoveryVec3d *)&RStack_1b8,arg1_00);
  RVar7.y = RVar5.z;
  dVar3 = RVar5.y;
  if (*(long *)(lVar1 + 0x28) == lStack_198) {
    RVar7.z = dVar4;
    RVar7.w = (double)SUB168(RVar5._0_16_,0);
    RVar7.x = (double)SUB168(RVar5._0_16_,8);
    return RVar7;
  }
  arg0_00 = (RecoveryVec3d *)func_0x0461e8c0();
  dStack_2b0 = dStack_248;
  uStack_2a0 = 0x3fe0000000000000;
  dStack_298 = dStack_250;
  dStack_290 = dStack_258;
  dStack_288 = dStack_260;
  pcStack_278 = neuToIMU;
  lVar1 = tpidr_el0;
  lStack_2b8 = *(long *)(lVar1 + 0x28);
  ppuStack_280 = &ppuStack_168;
  RVar5 = ImuVector3D::_ZNK6bingce11ImuVector3D8subtractERKS0__plt(arg0_00,arg1_01);
  RStack_348.z = RVar5.z;
  RStack_348.y = RVar5.y;
  RStack_348.x = RVar5.x;
  sincos_plt(RVar7.y * 0.5,&dStack_3b0,&dStack_3b8);
  sincos_plt(dVar3 * 0.5,&dStack_3a0,&dStack_3a8);
  sincos_plt(extraout_d0_00 * 0.5,&dStack_390,&dStack_398);
  ImuQuaternion::_ZN6bingce13ImuQuaternionC1Edddd_plt
            ((ImuQuaternion *)&RStack_2d8,
             dStack_390 * dStack_3a0 * dStack_3b0 + dStack_3b8 * dStack_398 * dStack_3a8,
             dStack_3b0 * -(dStack_398 * dStack_3a0) + dStack_3b8 * dStack_390 * dStack_3a8,
             dStack_390 * dStack_3a8 * dStack_3b0 + dStack_3b8 * dStack_398 * dStack_3a0,
             dStack_3b8 * -(dStack_390 * dStack_3a0) + dStack_3b0 * dStack_398 * dStack_3a8);
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9normalizeEv_plt(&RStack_2d8);
  RStack_368.z = RVar6.z;
  RStack_368.y = RVar6.y;
  RStack_368.x = RVar6.x;
  RStack_368.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(&RStack_368);
  RStack_388.z = RVar6.z;
  RStack_388.y = RVar6.y;
  RStack_388.x = RVar6.x;
  RStack_388.w = RVar6.w;
  RVar6 = ImuVector3D::_ZNK6bingce11ImuVector3D16toPureQuaternionEv_plt(&RStack_348);
  RStack_2d8.z = RVar6.z;
  RStack_2d8.y = RVar6.y;
  RStack_2d8.x = RVar6.x;
  RStack_2d8.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(&RStack_388);
  RStack_2f8.z = RVar6.z;
  RStack_2f8.y = RVar6.y;
  RStack_2f8.x = RVar6.x;
  RStack_2f8.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_388,&RStack_2d8);
  RStack_318.z = RVar6.z;
  RStack_318.y = RVar6.y;
  RStack_318.x = RVar6.x;
  RStack_318.w = RVar6.w;
  RVar6 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_318,&RStack_2f8);
  dVar3 = RVar6.z;
  func_0x0462e440(RVar6.x,RVar6.y,dVar3,&RStack_318);
  func_0x0462e440(RStack_318.w,RStack_318.x,-RStack_318.y,&uStack_330);
  RVar8.x = (double)uStack_328;
  RVar8.w = (double)uStack_330;
  if (*(long *)(lVar1 + 0x28) == lStack_2b8) {
    RVar8.z = dVar3;
    RVar8.y = (double)uStack_320;
    return RVar8;
  }
  RVar7.y = (double)func_0x0461e8c0();
  RVar9.w = (RVar7.y * _UNK_017e8938) / 180.0;
  RVar9.x = 180.0;
  RVar9.z = dVar3;
  RVar9.y = (double)uStack_320;
  return RVar9;
}
