/* Return/argument types refined from static ABI evidence; not original source. */

/* bingce::evcavator::normalCandidate(bingce::evcavator::Vec3 const&, bingce::evcavator::Vec3
   const&, bingce::evcavator::Vec3 const&) */

RecoveryVec3f
bingce::evcavator::normalCandidate(RecoveryVec3f *arg0,RecoveryVec3f *arg1,RecoveryVec3f *arg2)

{
  float fVar1;
  float fVar2;
  float fVar3;
  float fVar4;
  float fVar5;
  float fVar6;
  RecoveryVec3f RVar7;

  fVar5 = arg1->x - arg0->x;
  fVar6 = arg1->y - arg0->y;
  fVar3 = arg2->y - arg0->y;
  fVar2 = arg2->x - arg0->x;
  fVar4 = arg1->z - arg0->z;
  fVar1 = arg2->z - arg0->z;
  RVar7.y = fVar1 * -fVar5 + fVar2 * fVar4;
  RVar7.x = fVar3 * -fVar4 + fVar1 * fVar6;
  RVar7.z = fVar2 * -fVar6 + fVar3 * fVar5;
  return RVar7;
}
