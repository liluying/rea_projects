/* Return/argument types refined from static ABI evidence; not original source. */

/* bingce::evcavator::ExcavatorPoseCalculator::distance(bingce::evcavator::Vec3 const&,
   bingce::evcavator::Vec3 const&) */

float bingce::evcavator::ExcavatorPoseCalculator::distance(RecoveryVec3f *arg0,RecoveryVec3f *arg1)

{
  float fVar1;
  float fVar2;
  float fVar3;

  fVar1 = arg1->y - arg0->y;
  fVar2 = arg1->x - arg0->x;
  fVar3 = arg1->z - arg0->z;
  return SQRT(fVar1 * fVar1 + fVar2 * fVar2 + fVar3 * fVar3);
}
