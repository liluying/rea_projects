/* Return/argument types refined from static ABI evidence; not original source. */

/* bingce::ImuVector3D::dot(bingce::ImuVector3D const&) const */

double bingce::ImuVector3D::dot(RecoveryVec3d *arg0,RecoveryVec3d *arg1)

{
  return arg0->y * arg1->y + arg1->x * arg0->x + arg1->z * arg0->z;
}
