/* Return/argument types refined from static ABI evidence; not original source. */

/* bingce::ImuQuaternion::normalize() const */

RecoveryQuaternion bingce::ImuQuaternion::normalize(RecoveryQuaternion *arg0)

{
  double dVar1;
  double dVar2;
  double dVar3;
  double dVar4;
  double dVar5;
  RecoveryQuaternion RVar6;

  dVar1 = arg0->w;
  dVar2 = arg0->x;
  dVar3 = arg0->y;
  dVar4 = arg0->z;
  dVar5 = SQRT(dVar2 * dVar2 + dVar1 * dVar1 + dVar3 * dVar3 + dVar4 * dVar4);
  if (dVar5 == 0.0) {
    return *arg0;
  }
  RVar6.w = dVar1 / dVar5;
  RVar6.x = dVar2 / dVar5;
  RVar6.y = dVar3 / dVar5;
  RVar6.z = dVar4 / dVar5;
  return RVar6;
}
