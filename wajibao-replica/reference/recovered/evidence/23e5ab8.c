/* Return/argument types refined from static ABI evidence; not original source. */

/* bingce::evcavator::normalizeDegree(float) */

float bingce::evcavator::normalizeDegree(float arg0)

{
  for (; 180.0 < arg0; arg0 = arg0 + -360.0) {
  }
  for (; arg0 <= -180.0; arg0 = arg0 + 360.0) {
  }
  return arg0;
}
