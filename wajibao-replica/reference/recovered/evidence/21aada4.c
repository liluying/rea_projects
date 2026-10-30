/* Return/argument types refined from static ABI evidence; not original source. */

/* bingce::ImuQuaternion::norm() const */

double bingce::ImuQuaternion::norm(RecoveryQuaternion *arg0)

{
  return SQRT(arg0->x * arg0->x + arg0->w * arg0->w + arg0->y * arg0->y + arg0->z * arg0->z);
}
