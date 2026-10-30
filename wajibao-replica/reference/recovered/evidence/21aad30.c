/* Return/argument types refined from static ABI evidence; not original source. */

/* bingce::ImuQuaternion::multiply(bingce::ImuQuaternion const&) const */

RecoveryQuaternion
bingce::ImuQuaternion::multiply(RecoveryQuaternion *arg0,RecoveryQuaternion *arg1)

{
  double dVar1;
  double dVar2;
  double dVar3;
  double dVar4;
  double dVar5;
  double dVar6;
  double dVar7;
  double dVar8;
  RecoveryQuaternion RVar9;

  dVar6 = arg0->w;
  dVar1 = arg0->x;
  dVar2 = arg1->w;
  dVar4 = arg1->x;
  dVar3 = arg1->y;
  dVar5 = arg1->z;
  dVar7 = arg0->y;
  dVar8 = arg0->z;
  RVar9.x = (dVar2 * dVar1 + dVar4 * dVar6 + dVar5 * dVar7) - dVar3 * dVar8;
  RVar9.w = ((dVar4 * -dVar1 + dVar2 * dVar6) - dVar3 * dVar7) - dVar5 * dVar8;
  RVar9.z = ((dVar1 * dVar3 + dVar5 * dVar6) - dVar4 * dVar7) + dVar2 * dVar8;
  RVar9.y = dVar5 * -dVar1 + dVar3 * dVar6 + dVar2 * dVar7 + dVar4 * dVar8;
  return RVar9;
}
