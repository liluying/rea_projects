/* Return/argument types refined from static ABI evidence; not original source. */

/* bingce::ImuVector3D::subtract(bingce::ImuVector3D const&) const */

RecoveryVec3d bingce::ImuVector3D::subtract(RecoveryVec3d *arg0,RecoveryVec3d *arg1)

{
  RecoveryVec3d RVar1;

  RVar1.x = arg0->x - arg1->x;
  RVar1.y = arg0->y - arg1->y;
  RVar1.z = arg0->z - arg1->z;
  return RVar1;
}
