/* Return/argument types refined from static ABI evidence; not original source. */

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* bingce::evcavator::rotateZ(bingce::evcavator::Vec3 const&, float) */

RecoveryVec3f bingce::evcavator::rotateZ(RecoveryVec3f *arg0,float arg1)

{
  RecoveryVec3f RVar1;
  double dStack_28;
  double dStack_8;

  sincos_plt(((double)arg1 * _UNK_017e8938) / 180.0,&dStack_8,&dStack_28);
  RVar1.x = (float)dStack_8 * -arg0->y + (float)dStack_28 * arg0->x;
  RVar1.z = arg0->z;
  RVar1.y = arg0->y * (float)dStack_28 + (float)dStack_8 * arg0->x;
  return RVar1;
}
