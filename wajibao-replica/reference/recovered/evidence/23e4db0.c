/* Return/argument types refined from static ABI evidence; not original source. */

/* bingce::evcavator::ExcavatorPoseCalculator::calculateScaleX(float, float) */

float bingce::evcavator::ExcavatorPoseCalculator::calculateScaleX(float arg0,float arg1)

{
  undefined8 uVar1;
  undefined8 uVar2;
  float *pfVar3;
  float *pfVar4;
  undefined1 auVar5 [16];

  if (1e-06 < arg1) {
    return arg0 / arg1;
  }
  uVar1 = func_0x0461f0d0(0x10);
  func_0x020fd144(uVar1,&UNK_01789804);
  uVar2 = func_0x0461f0f0(uVar1,PTR__ZTISt16invalid_argument_0490f6f0,
                          PTR__ZNSt11logic_errorD2Ev_0490f6f8);
  func_0x0461f120(uVar1);
  auVar5 = func_0x0461aba0(uVar2);
  pfVar4 = auVar5._8_8_;
  pfVar3 = auVar5._0_8_;
  return SQRT((pfVar4[1] - pfVar3[1]) * (pfVar4[1] - pfVar3[1]) +
              (*pfVar4 - *pfVar3) * (*pfVar4 - *pfVar3) +
              (pfVar4[2] - pfVar3[2]) * (pfVar4[2] - pfVar3[2]));
}
