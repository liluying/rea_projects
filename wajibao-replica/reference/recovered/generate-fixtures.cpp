// Cross-language regression oracle from the earlier independent C++ transcription.
// This does not execute the original library and is not a native-equivalence oracle.
#include "recovered_math.hpp"
#include <iostream>
#include <iomanip>
using namespace wjb::recovered;
int main() {
 std::cout << std::setprecision(17);
 for(int i=0;i<128;i++) {
  double r=(i-64)*.041,p=(i%17-8)*.12,y=(i%29-14)*.2;
  Vec3d v{(i-51)*.071,(i%13-6)*.23,(i%23-11)*.31},o{3844277.123,474561.456,75.789};
  auto n=imu_to_neu(v,o,r,p,y);
  std::cout<<r<<'\t'<<p<<'\t'<<y<<'\t'<<v.x<<'\t'<<v.y<<'\t'<<v.z<<'\t'<<n.x<<'\t'<<n.y<<'\t'<<n.z<<'\n';
 }
}
