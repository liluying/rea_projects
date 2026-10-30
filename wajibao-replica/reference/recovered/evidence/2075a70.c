/* Return/argument types refined from static ABI evidence; not original source. */

/* bingce::CTriangle::computeZOnPlane(double, double) const */

double bingce::CTriangle::computeZOnPlane(void *arg0,double arg1,double arg2)

{
  double *pdVar1;
  double *pdVar2;
  double *pdVar3;
  double dVar4;
  double dVar5;
  double dVar6;
  double dVar7;

  pdVar1 = *(double **)((long)arg0 + 8);
  pdVar2 = *(double **)((long)arg0 + 0x18);
  pdVar3 = *(double **)((long)arg0 + 0x28);
  dVar6 = *pdVar1;
  dVar5 = pdVar1[1];
  dVar7 = (*pdVar3 - dVar6) * -(pdVar2[1] - dVar5) + (pdVar3[1] - dVar5) * (*pdVar2 - dVar6);
  if (dVar7 == 0.0) {
    return 0.0;
  }
  dVar4 = pdVar1[2];
  return dVar4 - ((arg2 - dVar5) *
                  ((pdVar3[2] - dVar4) * -(*pdVar2 - dVar6) +
                  (*pdVar3 - dVar6) * (pdVar2[2] - dVar4)) +
                 (arg1 - dVar6) *
                 ((pdVar3[1] - dVar5) * -(pdVar2[2] - dVar4) +
                 (pdVar3[2] - dVar4) * (pdVar2[1] - dVar5))) / dVar7;
}
