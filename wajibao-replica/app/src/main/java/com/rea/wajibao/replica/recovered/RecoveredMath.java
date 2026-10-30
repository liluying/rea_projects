package com.rea.wajibao.replica.recovered;

/** Independent transcription of recovered helpers, not the complete excavator solver.
 * Float geometry preserves the observed float arithmetic; IMU transforms use doubles.
 * Source/evidence and added input guards are documented in docs/independent-algorithms.md.
 */
public final class RecoveredMath {
    private RecoveredMath() {}
    public static final class F3 {
        public final float x, y, z;
        public F3(float x, float y, float z) { finite(x,y,z); this.x=x; this.y=y; this.z=z; }
    }
    public static final class V3 {
        public final double x, y, z;
        public V3(double x, double y, double z) { finite(x,y,z); this.x=x; this.y=y; this.z=z; }
    }
    public static final class Q {
        public final double w, x, y, z;
        public Q(double w, double x, double y, double z) { finite(w,x,y,z); this.w=w; this.x=x; this.y=y; this.z=z; }
    }
    private static void finite(double... values) {
        for (double v: values) if (!Double.isFinite(v)) throw new IllegalArgumentException("Non-finite input/result");
    }
    public static F3 subtract(F3 a,F3 b) { return new F3(a.x-b.x,a.y-b.y,a.z-b.z); }
    public static float dot(F3 a,F3 b) { return a.x*b.x+a.y*b.y+a.z*b.z; }
    public static F3 cross(F3 a,F3 b) { return new F3(a.y*b.z-a.z*b.y,a.z*b.x-a.x*b.z,a.x*b.y-a.y*b.x); }
    public static float distance(F3 a,F3 b) { F3 d=subtract(a,b); return (float)Math.sqrt(dot(d,d)); }
    public static float scaleX(float measured,float design) {
        finite(measured,design);
        if (!(design>1e-6f)) throw new IllegalArgumentException("Invalid design length");
        return measured/design;
    }
    /** (-180,180], including -180 -> +180. Remainder avoids unbounded native loops. */
    public static float normalizeDegrees(float degrees) {
        finite(degrees);
        float a=degrees%360f;
        if (a>180f) a-=360f;
        if (a<=-180f) a+=360f;
        return a;
    }
    public static F3 rotateX(F3 p,float degrees) {
        finite(degrees); double a=Math.toRadians(degrees); float s=(float)Math.sin(a),c=(float)Math.cos(a);
        return new F3(p.x,c*p.y-s*p.z,c*p.z+s*p.y);
    }
    public static F3 rotateY(F3 p,float degrees) {
        finite(degrees); double a=Math.toRadians(degrees); float s=(float)Math.sin(a),c=(float)Math.cos(a);
        return new F3(s*p.z+c*p.x,p.y,c*p.z-s*p.x);
    }
    public static F3 rotateZ(F3 p,float degrees) {
        finite(degrees); double a=Math.toRadians(degrees); float s=(float)Math.sin(a),c=(float)Math.cos(a);
        return new F3(c*p.x-s*p.y,c*p.y+s*p.x,p.z);
    }
    public static F3 normalCandidate(F3 a,F3 b,F3 c) { return cross(subtract(b,a),subtract(c,a)); }
    /** Largest triangle among abc,bcd,abd,acd; ties retain first. Not a least-squares fit. */
    public static F3 bestPlaneNormal(F3 a,F3 b,F3 c,F3 d) {
        F3[] candidates={normalCandidate(a,b,c),normalCandidate(b,c,d),normalCandidate(a,b,d),normalCandidate(a,c,d)};
        F3 best=candidates[0]; float square=dot(best,best);
        for (int i=1;i<4;i++) { float s=dot(candidates[i],candidates[i]); if (square<s) { best=candidates[i]; square=s; } }
        if (square>1e-12f) { float n=(float)Math.sqrt(square); return new F3(best.x/n,best.y/n,best.z/n); }
        return best;
    }
    /** XYZ basis to degrees; gimbal-lock branch and threshold follow recovered helper. */
    public static F3 basisToEulerXYZ(F3 xAxis,F3 yAxis,F3 zAxis) {
        double s=Math.max(-1,Math.min(1,(double)zAxis.x)), y=Math.asin(s),x,z;
        if (Math.abs(Math.cos(y))<=(double)1e-6f) {
            x=Math.atan2(xAxis.y,yAxis.y); if (s<0) x=-x; z=0;
        } else { x=Math.atan2(-zAxis.y,zAxis.z); z=Math.atan2(-yAxis.x,xAxis.x); }
        return new F3(normalizeDegrees((float)Math.toDegrees(x)),normalizeDegrees((float)Math.toDegrees(y)),normalizeDegrees((float)Math.toDegrees(z)));
    }
    public static Q conjugate(Q q) { return new Q(q.w,-q.x,-q.y,-q.z); }
    public static Q multiply(Q a,Q b) {
        return new Q(a.w*b.w-a.x*b.x-a.y*b.y-a.z*b.z,
                a.w*b.x+a.x*b.w+a.y*b.z-a.z*b.y,
                a.w*b.y-a.x*b.z+a.y*b.w+a.z*b.x,
                a.w*b.z+a.x*b.y-a.y*b.x+a.z*b.w);
    }
    public static Q normalize(Q q) {
        double n=Math.sqrt(q.w*q.w+q.x*q.x+q.y*q.y+q.z*q.z);
        if (!(n>0)||!Double.isFinite(n)) throw new IllegalArgumentException("Invalid quaternion");
        return new Q(q.w/n,q.x/n,q.y/n,q.z/n);
    }
    /** Does not normalize q implicitly: caller supplies a unit quaternion for rigid rotation. */
    public static V3 rotateVector(Q q,V3 v) {
        Q p=multiply(multiply(q,new Q(0,v.x,v.y,v.z)),conjugate(q)); return new V3(p.x,p.y,p.z);
    }
    public static Q eulerQuaternion(double roll,double pitch,double yaw) {
        finite(roll,pitch,yaw);
        double cr=Math.cos(roll*.5),sr=Math.sin(roll*.5),cp=Math.cos(pitch*.5),sp=Math.sin(pitch*.5),cy=Math.cos(yaw*.5),sy=Math.sin(yaw*.5);
        return normalize(new Q(cr*cp*cy+sr*sp*sy,sr*cp*cy-cr*sp*sy,cr*sp*cy+sr*cp*sy,cr*cp*sy-sr*sp*cy));
    }
    /** Angles in radians. Local Z is reversed before rotation into north/east/up. */
    public static V3 imuToNeu(V3 local,V3 origin,double roll,double pitch,double yaw) {
        V3 v=rotateVector(eulerQuaternion(roll,pitch,yaw),new V3(local.x,local.y,-local.z));
        return new V3(v.x+origin.x,v.y+origin.y,v.z+origin.z);
    }
    public static V3 neuToImu(V3 point,V3 origin,double roll,double pitch,double yaw) {
        V3 v=rotateVector(conjugate(eulerQuaternion(roll,pitch,yaw)),new V3(point.x-origin.x,point.y-origin.y,point.z-origin.z));
        return new V3(v.x,v.y,-v.z);
    }
    /** Plane interpolation/extrapolation; exact XY degeneracy returns 0 as observed. */
    public static double triangleHeight(V3 a,V3 b,V3 c,double x,double y) {
        finite(x,y);
        double ux=b.x-a.x,uy=b.y-a.y,uz=b.z-a.z,vx=c.x-a.x,vy=c.y-a.y,vz=c.z-a.z,den=ux*vy-uy*vx;
        if (den==0) return 0;
        return a.z+((uz*vy-uy*vz)*(x-a.x)+(ux*vz-uz*vx)*(y-a.y))/den;
    }
}
