/* Return/argument types refined from static ABI evidence; not original source. */

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* bingce::IMUtoNEUConverter::rotateVectorByQuaternion(bingce::ImuQuaternion const&,
   bingce::ImuVector3D const&) */

RecoveryVec3d
bingce::IMUtoNEUConverter::rotateVectorByQuaternion(RecoveryQuaternion *arg0,RecoveryVec3d *arg1)

{
  long lVar1;
  RecoveryVec3d RVar2;
  undefined8 *puVar3;
  RecoveryVec3d *arg0_00;
  RecoveryVec3d *arg1_00;
  RecoveryVec3d *arg1_01;
  double extraout_d0;
  double extraout_d0_00;
  double dVar4;
  RecoveryVec3d RVar6;
  RecoveryVec3d RVar7;
  RecoveryQuaternion RVar8;
  double dStack_318;
  double dStack_310;
  double dStack_308;
  double dStack_300;
  double dStack_2f8;
  double dStack_2f0;
  RecoveryQuaternion RStack_2e8;
  RecoveryQuaternion RStack_2c8;
  RecoveryVec3d RStack_2a8;
  undefined8 uStack_290;
  undefined8 uStack_288;
  double dStack_280;
  RecoveryQuaternion RStack_278;
  RecoveryQuaternion RStack_258;
  RecoveryQuaternion RStack_238;
  long lStack_218;
  double dStack_210;
  undefined8 uStack_200;
  double dStack_1f8;
  double dStack_1f0;
  double dStack_1e8;
  undefined1 **ppuStack_1e0;
  code *pcStack_1d8;
  double dStack_1c0;
  double dStack_1b8;
  double dStack_1b0;
  double dStack_1a8;
  double dStack_1a0;
  double dStack_198;
  RecoveryQuaternion RStack_190;
  RecoveryVec3d RStack_170;
  RecoveryQuaternion RStack_158;
  RecoveryQuaternion RStack_138;
  RecoveryQuaternion RStack_118;
  long lStack_f8;
  undefined1 *puStack_c8;
  code *pcStack_c0;
  RecoveryQuaternion RStack_a0;
  RecoveryQuaternion RStack_80;
  RecoveryQuaternion RStack_60;
  undefined8 uStack_40;
  double dStack_38;
  double dStack_30;
  long lStack_28;
  double dVar5;

  lVar1 = tpidr_el0;
  lStack_28 = *(long *)(lVar1 + 0x28);
  RVar8 = ImuVector3D::_ZNK6bingce11ImuVector3D16toPureQuaternionEv_plt(arg1);
  RStack_60.z = RVar8.z;
  RStack_60.y = RVar8.y;
  RStack_60.x = RVar8.x;
  RStack_60.w = RVar8.w;
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(arg0);
  RStack_80.z = RVar8.z;
  RStack_80.y = RVar8.y;
  RStack_80.x = RVar8.x;
  RStack_80.w = RVar8.w;
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(arg0,&RStack_60);
  RStack_a0.z = RVar8.z;
  RStack_a0.y = RVar8.y;
  RStack_a0.x = RVar8.x;
  RStack_a0.w = RVar8.w;
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_a0,&RStack_80);
  func_0x0462e440(RVar8.x,RVar8.y,RVar8.z,&uStack_40);
  RVar6.y = dStack_38;
  RVar6.x = (double)uStack_40;
  RVar6.z = dStack_30;
  if (*(long *)(lVar1 + 0x28) == lStack_28) {
    return RVar6;
  }
  dVar4 = dStack_38;
  dVar5 = dStack_30;
  puVar3 = (undefined8 *)func_0x0461e8c0();
  pcStack_c0 = imuToNEU;
  lVar1 = tpidr_el0;
  lStack_f8 = *(long *)(lVar1 + 0x28);
  puStack_c8 = &stack0xffffffffffffffe0;
  func_0x0462e440(*puVar3,puVar3[1],-(double)puVar3[2],&RStack_170);
  sincos_plt(dVar5 * 0.5,&dStack_1b8,&dStack_1c0);
  sincos_plt(dVar4 * 0.5,&dStack_1a8,&dStack_1b0);
  sincos_plt(extraout_d0 * 0.5,&dStack_198,&dStack_1a0);
  ImuQuaternion::_ZN6bingce13ImuQuaternionC1Edddd_plt
            ((ImuQuaternion *)&RStack_118,
             dStack_198 * dStack_1a8 * dStack_1b8 + dStack_1c0 * dStack_1a0 * dStack_1b0,
             dStack_1b8 * -(dStack_1a0 * dStack_1a8) + dStack_1c0 * dStack_198 * dStack_1b0,
             dStack_198 * dStack_1b0 * dStack_1b8 + dStack_1c0 * dStack_1a0 * dStack_1a8,
             dStack_1c0 * -(dStack_198 * dStack_1a8) + dStack_1b8 * dStack_1a0 * dStack_1b0);
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9normalizeEv_plt(&RStack_118);
  RStack_190.z = RVar8.z;
  RStack_190.y = RVar8.y;
  RStack_190.x = RVar8.x;
  RStack_190.w = RVar8.w;
  RVar8 = ImuVector3D::_ZNK6bingce11ImuVector3D16toPureQuaternionEv_plt(&RStack_170);
  RStack_118.z = RVar8.z;
  RStack_118.y = RVar8.y;
  RStack_118.x = RVar8.x;
  RStack_118.w = RVar8.w;
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(&RStack_190);
  RStack_138.z = RVar8.z;
  RStack_138.y = RVar8.y;
  RStack_138.x = RVar8.x;
  RStack_138.w = RVar8.w;
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_190,&RStack_118);
  RStack_158.z = RVar8.z;
  RStack_158.y = RVar8.y;
  RStack_158.x = RVar8.x;
  RStack_158.w = RVar8.w;
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_158,&RStack_138);
  func_0x0462e440(RVar8.x,RVar8.y,RVar8.z,&RStack_158);
  RStack_118.x = RStack_158.x;
  RStack_118.w = RStack_158.w;
  RStack_118.y = RStack_158.y;
  RVar6 = ImuVector3D::_ZNK6bingce11ImuVector3D3addERKS0__plt((RecoveryVec3d *)&RStack_118,arg1_00);
  dVar5 = RVar6.z;
  dVar4 = RVar6.y;
  if (*(long *)(lVar1 + 0x28) == lStack_f8) {
    return RVar6;
  }
  arg0_00 = (RecoveryVec3d *)func_0x0461e8c0();
  dStack_210 = dStack_1a8;
  uStack_200 = 0x3fe0000000000000;
  dStack_1f8 = dStack_1b0;
  dStack_1f0 = dStack_1b8;
  dStack_1e8 = dStack_1c0;
  pcStack_1d8 = neuToIMU;
  lVar1 = tpidr_el0;
  lStack_218 = *(long *)(lVar1 + 0x28);
  ppuStack_1e0 = &puStack_c8;
  RVar6 = ImuVector3D::_ZNK6bingce11ImuVector3D8subtractERKS0__plt(arg0_00,arg1_01);
  RStack_2a8.z = RVar6.z;
  RStack_2a8.y = RVar6.y;
  RStack_2a8.x = RVar6.x;
  sincos_plt(dVar5 * 0.5,&dStack_310,&dStack_318);
  sincos_plt(dVar4 * 0.5,&dStack_300,&dStack_308);
  sincos_plt(extraout_d0_00 * 0.5,&dStack_2f0,&dStack_2f8);
  ImuQuaternion::_ZN6bingce13ImuQuaternionC1Edddd_plt
            ((ImuQuaternion *)&RStack_238,
             dStack_2f0 * dStack_300 * dStack_310 + dStack_318 * dStack_2f8 * dStack_308,
             dStack_310 * -(dStack_2f8 * dStack_300) + dStack_318 * dStack_2f0 * dStack_308,
             dStack_2f0 * dStack_308 * dStack_310 + dStack_318 * dStack_2f8 * dStack_300,
             dStack_318 * -(dStack_2f0 * dStack_300) + dStack_310 * dStack_2f8 * dStack_308);
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9normalizeEv_plt(&RStack_238);
  RStack_2c8.z = RVar8.z;
  RStack_2c8.y = RVar8.y;
  RStack_2c8.x = RVar8.x;
  RStack_2c8.w = RVar8.w;
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(&RStack_2c8);
  RStack_2e8.z = RVar8.z;
  RStack_2e8.y = RVar8.y;
  RStack_2e8.x = RVar8.x;
  RStack_2e8.w = RVar8.w;
  RVar8 = ImuVector3D::_ZNK6bingce11ImuVector3D16toPureQuaternionEv_plt(&RStack_2a8);
  RStack_238.z = RVar8.z;
  RStack_238.y = RVar8.y;
  RStack_238.x = RVar8.x;
  RStack_238.w = RVar8.w;
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion9conjugateEv_plt(&RStack_2e8);
  RStack_258.z = RVar8.z;
  RStack_258.y = RVar8.y;
  RStack_258.x = RVar8.x;
  RStack_258.w = RVar8.w;
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_2e8,&RStack_238);
  RStack_278.z = RVar8.z;
  RStack_278.y = RVar8.y;
  RStack_278.x = RVar8.x;
  RStack_278.w = RVar8.w;
  RVar8 = ImuQuaternion::_ZNK6bingce13ImuQuaternion8multiplyERKS0__plt(&RStack_278,&RStack_258);
  func_0x0462e440(RVar8.x,RVar8.y,RVar8.z,&RStack_278);
  func_0x0462e440(RStack_278.w,RStack_278.x,-RStack_278.y,&uStack_290);
  RVar2.y = (double)uStack_288;
  RVar2.x = (double)uStack_290;
  RVar2.z = dStack_280;
  if (*(long *)(lVar1 + 0x28) == lStack_218) {
    return RVar2;
  }
  dVar4 = (double)func_0x0461e8c0();
  RVar7.x = (dVar4 * _UNK_017e8938) / 180.0;
  RVar7.y = 180.0;
  RVar7.z = dStack_280;
  return RVar7;
}
