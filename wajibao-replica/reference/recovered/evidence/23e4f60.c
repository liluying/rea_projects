/* Return/argument types refined from static ABI evidence; not original source. */

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* bingce::evcavator::calculateBestPlaneNormal(bingce::evcavator::Vec3 const&,
   bingce::evcavator::Vec3 const&, bingce::evcavator::Vec3 const&, bingce::evcavator::Vec3 const&)
    */

RecoveryVec3f
bingce::evcavator::calculateBestPlaneNormal
          (RecoveryVec3f *arg0,RecoveryVec3f *arg1,RecoveryVec3f *arg2,RecoveryVec3f *arg3)

{
  undefined8 uVar1;
  undefined8 uVar2;
  undefined4 *puVar3;
  float fVar4;
  float extraout_s0;
  float fVar5;
  float fVar6;
  float fVar7;
  float fVar8;
  float fVar9;
  float fVar10;
  float fVar11;
  float fVar12;
  float fVar13;
  float fVar14;
  float fVar15;
  float fVar16;
  float fVar17;
  float fVar18;
  float fVar19;
  float fVar20;
  float fVar21;
  float fVar22;
  RecoveryVec3f RVar23;
  RecoveryVec3f RVar24;
  double dStack_68;
  undefined1 *puStack_60;
  code *pcStack_58;
  undefined8 uStack_50;
  double dStack_48;

  fVar5 = arg1->x;
  fVar6 = arg1->y;
  fVar7 = arg0->x;
  fVar8 = arg0->y;
  fVar9 = arg1->z;
  fVar18 = arg2->x - fVar5;
  fVar10 = arg0->z;
  fVar15 = fVar5 - fVar7;
  fVar19 = arg2->x - fVar7;
  fVar16 = fVar9 - fVar10;
  fVar12 = fVar6 - fVar8;
  fVar20 = arg2->z - fVar10;
  fVar13 = arg2->z - fVar9;
  fVar9 = arg3->z - fVar9;
  fVar22 = arg2->y - fVar8;
  fVar11 = arg2->y - fVar6;
  fVar6 = arg3->y - fVar6;
  fVar5 = arg3->x - fVar5;
  fVar10 = arg3->z - fVar10;
  fVar7 = arg3->x - fVar7;
  fVar8 = arg3->y - fVar8;
  fVar4 = fVar20 * -fVar15 + fVar19 * fVar16;
  fVar14 = fVar9 * -fVar18 + fVar5 * fVar13;
  fVar17 = fVar6 * -fVar13 + fVar9 * fVar11;
  fVar13 = fVar10 * -fVar15 + fVar7 * fVar16;
  fVar9 = fVar22 * -fVar16 + fVar20 * fVar12;
  fVar18 = fVar5 * -fVar11 + fVar6 * fVar18;
  fVar16 = fVar8 * -fVar16 + fVar10 * fVar12;
  fVar11 = fVar10 * -fVar19 + fVar7 * fVar20;
  fVar5 = fVar19 * -fVar12 + fVar22 * fVar15;
  fVar21 = fVar14 * fVar14 + fVar17 * fVar17 + fVar18 * fVar18;
  fVar12 = fVar7 * -fVar12 + fVar8 * fVar15;
  fVar10 = fVar8 * -fVar20 + fVar10 * fVar22;
  fVar6 = fVar4 * fVar4 + fVar9 * fVar9 + fVar5 * fVar5;
  fVar15 = fVar13 * fVar13 + fVar16 * fVar16 + fVar12 * fVar12;
  fVar7 = fVar7 * -fVar22 + fVar8 * fVar19;
  if (fVar6 < fVar21) {
    fVar5 = fVar18;
    fVar6 = fVar21;
    fVar4 = fVar14;
    fVar9 = fVar17;
  }
  fVar8 = fVar11 * fVar11 + fVar10 * fVar10 + fVar7 * fVar7;
  if (fVar6 < fVar15) {
    fVar5 = fVar12;
    fVar6 = fVar15;
    fVar4 = fVar13;
    fVar9 = fVar16;
  }
  if (fVar6 < fVar8) {
    fVar5 = fVar7;
    fVar6 = fVar8;
    fVar4 = fVar11;
    fVar9 = fVar10;
  }
  if (1e-12 < fVar6) {
    fVar6 = SQRT(fVar4 * fVar4 + fVar9 * fVar9 + fVar5 * fVar5);
    if (fVar6 <= 1e-06) {
      uVar1 = func_0x0461f0d0(0x10);
      func_0x020fd144(uVar1,&UNK_0178903d);
      uVar2 = func_0x0461f0f0(uVar1,PTR__ZTISt16invalid_argument_0490f6f0,
                              PTR__ZNSt11logic_errorD2Ev_0490f6f8);
      func_0x0461f120(uVar1);
      puVar3 = (undefined4 *)func_0x0461aba0(uVar2);
      pcStack_58 = rotateX;
      puStack_60 = &stack0xffffffffffffffe0;
      uStack_50 = uVar1;
      sincos_plt(((double)extraout_s0 * _UNK_017e8938) / 180.0,&dStack_48,&dStack_68);
      RVar24.x = *puVar3;
      RVar24.y = (float)dStack_48 * -(float)puVar3[2] + (float)puVar3[1] * (float)dStack_68;
      RVar24.z = (float)puVar3[2] * (float)dStack_68 + (float)puVar3[1] * (float)dStack_48;
      return RVar24;
    }
    fVar9 = fVar9 / fVar6;
    fVar4 = fVar4 / fVar6;
    fVar5 = fVar5 / fVar6;
  }
  RVar23.y = fVar9;
  RVar23.x = fVar4;
  RVar23.z = fVar5;
  return RVar23;
}
