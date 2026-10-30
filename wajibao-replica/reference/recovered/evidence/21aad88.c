/* Return/argument types refined from static ABI evidence; not original source. */

/* bingce::ImuQuaternion::conjugate() const */

RecoveryQuaternion bingce::ImuQuaternion::conjugate(RecoveryQuaternion *arg0)

{
  RecoveryQuaternion RVar1;

  RVar1.w = arg0->w;
  RVar1.x = -arg0->x;
  RVar1.y = -arg0->y;
  RVar1.z = -arg0->z;
  return RVar1;
}
