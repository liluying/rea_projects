#pragma once
// Independently transcribed from the observed ARM64/Ghidra math helpers.
// NOT original source, NOT the complete WajiInner solver or Android business layer.
// Finite-input numerical semantics only; guards explicitly add safer failure behavior.
#include <array>
#include <algorithm>
#include <cmath>
#include <stdexcept>

namespace wjb::recovered {
constexpr double pi=3.14159265358979323846;
struct Vec3f { float x,y,z; };
struct Vec3d { double x,y,z; };
struct Quaternion { double w,x,y,z; };
inline Vec3f sub(Vec3f a,Vec3f b){return {a.x-b.x,a.y-b.y,a.z-b.z};}
inline float dot(Vec3f a,Vec3f b){return a.x*b.x+a.y*b.y+a.z*b.z;}
inline Vec3f cross(Vec3f a,Vec3f b){return {a.y*b.z-a.z*b.y,a.z*b.x-a.x*b.z,a.x*b.y-a.y*b.x};}
inline float distance(Vec3f a,Vec3f b){auto v=sub(a,b);return std::sqrt(dot(v,v));}
inline float calculate_scale_x(float measured,float design){
 if(!(design>1e-6f)){throw std::invalid_argument("invalid design length");}
 return measured/design;
}
inline float normalize_degree(float value){
 // Native loops have no nonfinite guard. This explicit guard is a new safeguard.
 if(!std::isfinite(value))throw std::invalid_argument("non-finite degree");
 while(value>180.f)value-=360.f;
 while(value<=-180.f)value+=360.f;
 return value;
}
inline double radians_to_degrees(double a){return a*180.0/pi;}
inline double degrees_to_radians(double a){return a*pi/180.0;}
inline Vec3f rotate_x(Vec3f p,float degrees){double a=degrees_to_radians(degrees);float s=static_cast<float>(std::sin(a)),c=static_cast<float>(std::cos(a));return {p.x,c*p.y-s*p.z,c*p.z+s*p.y};}
inline Vec3f rotate_y(Vec3f p,float degrees){double a=degrees_to_radians(degrees);float s=static_cast<float>(std::sin(a)),c=static_cast<float>(std::cos(a));return {s*p.z+c*p.x,p.y,c*p.z-s*p.x};}
inline Vec3f rotate_z(Vec3f p,float degrees){double a=degrees_to_radians(degrees);float s=static_cast<float>(std::sin(a)),c=static_cast<float>(std::cos(a));return {c*p.x-s*p.y,c*p.y+s*p.x,p.z};}
inline Vec3f normal_candidate(Vec3f a,Vec3f b,Vec3f c){return cross(sub(b,a),sub(c,a));}
inline Vec3f best_plane_normal(Vec3f a,Vec3f b,Vec3f c,Vec3f d){
 // Observed selection is largest triangle cross-product magnitude, NOT PCA fitting.
 std::array<Vec3f,4> candidates{normal_candidate(a,b,c),normal_candidate(b,c,d),normal_candidate(a,b,d),normal_candidate(a,c,d)};
 Vec3f best=candidates[0];float best_sq=dot(best,best);
 for(unsigned i=1;i<candidates.size();++i){float sq=dot(candidates[i],candidates[i]);if(best_sq<sq){best=candidates[i];best_sq=sq;}}
 if(best_sq>1e-12f){float length=std::sqrt(dot(best,best));if(length<=1e-6f)throw std::invalid_argument("zero normal");best={best.x/length,best.y/length,best.z/length};}
 // Tiny candidates are returned without normalization in the observed helper.
 return best;
}
inline Vec3f basis_to_euler_xyz(Vec3f x_axis,Vec3f y_axis,Vec3f z_axis){
 double s=std::clamp(static_cast<double>(z_axis.x),-1.0,1.0);double y=std::asin(s);double x,z;
 if(std::abs(std::cos(y))<=static_cast<double>(1e-6f)){
  x=std::atan2(x_axis.y,y_axis.y);if(s<0)x=-x;z=0;
 }else{x=std::atan2(-z_axis.y,z_axis.z);z=std::atan2(-y_axis.x,x_axis.x);}
 return {normalize_degree(static_cast<float>(radians_to_degrees(x))),normalize_degree(static_cast<float>(radians_to_degrees(y))),normalize_degree(static_cast<float>(radians_to_degrees(z)))};
}
inline Vec3d add(Vec3d a,Vec3d b){return {a.x+b.x,a.y+b.y,a.z+b.z};}
inline Vec3d sub(Vec3d a,Vec3d b){return {a.x-b.x,a.y-b.y,a.z-b.z};}
inline double dot(Vec3d a,Vec3d b){return a.x*b.x+a.y*b.y+a.z*b.z;}
inline double norm(Quaternion a){return std::sqrt(a.w*a.w+a.x*a.x+a.y*a.y+a.z*a.z);}
inline Quaternion normalize(Quaternion a){double n=norm(a);if(!(n>0)||!std::isfinite(n))throw std::invalid_argument("invalid quaternion");return {a.w/n,a.x/n,a.y/n,a.z/n};}
inline Quaternion conjugate(Quaternion a){return {a.w,-a.x,-a.y,-a.z};}
inline Quaternion multiply(Quaternion a,Quaternion b){return {a.w*b.w-a.x*b.x-a.y*b.y-a.z*b.z,a.w*b.x+a.x*b.w+a.y*b.z-a.z*b.y,a.w*b.y-a.x*b.z+a.y*b.w+a.z*b.x,a.w*b.z+a.x*b.y-a.y*b.x+a.z*b.w};}
inline Quaternion pure(Vec3d p){return {0,p.x,p.y,p.z};}
inline Vec3d rotate_vector(Quaternion q,Vec3d p){auto a=multiply(multiply(q,pure(p)),conjugate(q));return {a.x,a.y,a.z};}
inline Quaternion euler_quaternion(double roll_rad,double pitch_rad,double yaw_rad){
 double cr=std::cos(roll_rad*.5),sr=std::sin(roll_rad*.5),cp=std::cos(pitch_rad*.5),sp=std::sin(pitch_rad*.5),cy=std::cos(yaw_rad*.5),sy=std::sin(yaw_rad*.5);
 return normalize({cr*cp*cy+sr*sp*sy,sr*cp*cy-cr*sp*sy,cr*sp*cy+sr*cp*sy,cr*cp*sy-sr*sp*cy});
}
inline Vec3d imu_to_neu(Vec3d local,Vec3d origin,double roll_rad,double pitch_rad,double yaw_rad){return add(rotate_vector(euler_quaternion(roll_rad,pitch_rad,yaw_rad),{local.x,local.y,-local.z}),origin);}
inline Vec3d neu_to_imu(Vec3d point,Vec3d origin,double roll_rad,double pitch_rad,double yaw_rad){auto p=rotate_vector(conjugate(euler_quaternion(roll_rad,pitch_rad,yaw_rad)),sub(point,origin));return {p.x,p.y,-p.z};}
inline double triangle_height(Vec3d a,Vec3d b,Vec3d c,double x,double y){
 double ux=b.x-a.x,uy=b.y-a.y,uz=b.z-a.z,vx=c.x-a.x,vy=c.y-a.y,vz=c.z-a.z;double den=ux*vy-uy*vx;
 if(den==0)return 0; // Observed exact-degenerate fallback, not validity advice.
 return a.z+((uz*vy-uy*vz)*(x-a.x)+(ux*vz-uz*vx)*(y-a.y))/den;
}
}
