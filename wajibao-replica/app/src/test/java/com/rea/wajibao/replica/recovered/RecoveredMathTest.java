package com.rea.wajibao.replica.recovered;

import org.junit.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;
import static com.rea.wajibao.replica.recovered.RecoveredMath.*;

public class RecoveredMathTest {
    private static void vector(F3 v,double x,double y,double z) {
        assertEquals(x,v.x,1e-5); assertEquals(y,v.y,1e-5); assertEquals(z,v.z,1e-5);
    }
    @Test public void boundariesAndInputGuards() {
        assertEquals(180,normalizeDegrees(-180),0); assertEquals(180,normalizeDegrees(540),0);
        assertEquals(179,normalizeDegrees(-181),0); assertEquals(0,normalizeDegrees(360),0);
        assertTrue(Float.isFinite(normalizeDegrees(Float.MAX_VALUE)));
        assertThrows(IllegalArgumentException.class,()->normalizeDegrees(Float.NaN));
        assertThrows(IllegalArgumentException.class,()->scaleX(1,0));
        assertThrows(IllegalArgumentException.class,()->normalize(new Q(0,0,0,0)));
        assertThrows(IllegalArgumentException.class,()->new V3(0,Double.POSITIVE_INFINITY,0));
        assertEquals(3,scaleX(6,2),0); assertEquals(13,distance(new F3(0,0,0),new F3(3,4,12)),0);
    }
    @Test public void axesAreDegreesAndEulerOrderIsPreserved() {
        vector(rotateX(new F3(0,1,0),90),0,0,1);
        vector(rotateY(new F3(1,0,0),90),0,0,-1);
        vector(rotateZ(new F3(1,0,0),90),0,1,0);
        F3 x=new F3(1,0,0),y=new F3(0,1,0),z=new F3(0,0,1);
        vector(basisToEulerXYZ(rotateX(x,30),rotateX(y,30),rotateX(z,30)),30,0,0);
        vector(basisToEulerXYZ(rotateY(x,20),rotateY(y,20),rotateY(z,20)),0,20,0);
        vector(basisToEulerXYZ(rotateZ(x,-45),rotateZ(y,-45),rotateZ(z,-45)),0,0,-45);
        vector(basisToEulerXYZ(rotateY(x,90),rotateY(y,90),rotateY(z,90)),0,90,0);
        vector(basisToEulerXYZ(rotateY(x,-90),rotateY(y,-90),rotateY(z,-90)),0,-90,0);
    }
    @Test public void planeSelectionTiesTinyAndDegenerate() {
        F3 a=new F3(0,0,0),b=new F3(1,0,0),c=new F3(0,1,0);
        vector(bestPlaneNormal(a,b,c,new F3(1,1,0)),0,0,1);
        vector(bestPlaneNormal(a,b,c,new F3(0,0,3)),3/Math.sqrt(19),3/Math.sqrt(19),1/Math.sqrt(19));
        F3 tiny=bestPlaneNormal(a,new F3(1e-4f,0,0),new F3(0,1e-4f,0),new F3(1e-4f,1e-4f,0));
        assertTrue(tiny.z>0 && tiny.z<1e-7);
        vector(bestPlaneNormal(a,a,a,a),0,0,0);
        assertEquals(14,triangleHeight(new V3(0,0,1),new V3(1,0,3),new V3(0,1,4),2,3),0);
        assertEquals(0,triangleHeight(new V3(0,0,1),new V3(1,0,2),new V3(2,0,3),1,1),0);
    }
    @Test public void imuMatchesIndependentCppFixturesAndRoundTrips() throws Exception {
        V3 origin=new V3(3844277.123,474561.456,75.789);
        int rows=0;
        try (BufferedReader reader=new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/recovered-imu.tsv"),StandardCharsets.UTF_8))) {
            String line;
            while ((line=reader.readLine())!=null) {
                String[] cols=line.split("\t"); double[] a=new double[9];
                for(int i=0;i<9;i++) a[i]=Double.parseDouble(cols[i]);
                V3 local=new V3(a[3],a[4],a[5]);
                V3 n=imuToNeu(local,origin,a[0],a[1],a[2]);
                assertEquals(a[6],n.x,1e-8); assertEquals(a[7],n.y,1e-8); assertEquals(a[8],n.z,1e-8);
                V3 back=neuToImu(n,origin,a[0],a[1],a[2]);
                assertEquals(local.x,back.x,1e-8); assertEquals(local.y,back.y,1e-8); assertEquals(local.z,back.z,1e-8);
                rows++;
            }
        }
        assertEquals(128,rows);
        V3 zero=imuToNeu(new V3(1,2,3),new V3(10,20,30),0,0,0);
        assertEquals(11,zero.x,0); assertEquals(22,zero.y,0); assertEquals(27,zero.z,0);
    }
}
