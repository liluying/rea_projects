/* Return/argument types refined from static ABI evidence; not original source. */

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* bingce::evcavator::basisToEulerXYZ(bingce::evcavator::Vec3 const&, bingce::evcavator::Vec3
   const&, bingce::evcavator::Vec3 const&) */

RecoveryVec3f
bingce::evcavator::basisToEulerXYZ(RecoveryVec3f *arg0,RecoveryVec3f *arg1,RecoveryVec3f *arg2)

{
  double dVar1;
  double dVar2;
  float fVar3;
  double dVar4;
  float fVar5;
  float fVar6;
  RecoveryVec3f RVar7;

  dVar4 = (double)arg2->x;
  if (1.0 < dVar4) {
    dVar4 = 1.0;
  }
  dVar2 = -1.0;
  if (-1.0 <= arg2->x) {
    dVar2 = dVar4;
  }
  dVar4 = asin_plt(dVar2);
  dVar1 = cos_plt(dVar4);
  if (ABS(dVar1) <= _UNK_017e90e0) {
    dVar1 = atan2_plt((double)arg0->y,(double)arg1->y);
    fVar5 = 0.0;
    if (dVar2 < 0.0) {
      dVar1 = -dVar1;
    }
  }
  else {
    fVar5 = arg1->x;
    fVar6 = arg0->x;
    dVar1 = atan2_plt(-(double)arg2->y,(double)arg2->z);
    dVar2 = atan2_plt(-(double)fVar5,(double)fVar6);
    fVar5 = (float)(dVar2 * _UNK_017e9208);
  }
  for (fVar6 = (float)(dVar1 * _UNK_017e9208); 180.0 < fVar6; fVar6 = fVar6 + -360.0) {
  }
  for (; fVar6 <= -180.0; fVar6 = fVar6 + 360.0) {
  }
  for (fVar3 = (float)(dVar4 * _UNK_017e9208); 180.0 < fVar3; fVar3 = fVar3 + -360.0) {
  }
  for (; fVar3 <= -180.0; fVar3 = fVar3 + 360.0) {
  }
  for (; 180.0 < fVar5; fVar5 = fVar5 + -360.0) {
  }
  for (; fVar5 <= -180.0; fVar5 = fVar5 + 360.0) {
  }
  RVar7.y = fVar3;
  RVar7.x = fVar6;
  RVar7.z = fVar5;
  return RVar7;
}
