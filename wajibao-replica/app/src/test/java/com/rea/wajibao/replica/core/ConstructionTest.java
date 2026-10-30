package com.rea.wajibao.replica.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructionTest {
    @Test public void levelPlaneIsIndependentOfLateralPosition() {
        Design d = Design.level(75.5);
        assertEquals(75.5, d.elevationAt(-100), 0); assertEquals(75.5, d.elevationAt(100), 0);
    }
    @Test public void trenchHasFlatBottomSymmetricSlopesAndGroundCap() {
        Design d = Design.trench(10, 2, 4, 1.5);
        assertEquals(8, d.elevationAt(0), 0); assertEquals(8, d.elevationAt(2), 0);
        assertEquals(9, d.elevationAt(3.5), 1e-12);
        assertEquals(d.elevationAt(-3.5), d.elevationAt(3.5), 0);
        assertEquals(10, d.elevationAt(50), 0);
    }
    @Test public void verticalTrenchWallNeverDividesByZero() {
        Design d = Design.trench(10, 2, 4, 0);
        assertEquals(8, d.elevationAt(2), 0); assertEquals(10, d.elevationAt(2.01), 0);
    }
    @Test public void planarSolverRespectsDegreesAndAbsoluteAngles() {
        PoseSolver solver = new DemoPoseSolver(); MachineConfig m = new MachineConfig("试验", 3, 2, 1);
        PoseSolver.Pose p = solver.solve(m, 10, 0, 0, 0, 1.2);
        assertEquals(6, p.reach, 1e-12); assertEquals(10, p.elevation, 1e-12); assertEquals(1.2, p.lateral, 0);
        p = solver.solve(m, 10, 90, -90, 90, 0);
        assertEquals(12, p.elevation, 1e-12); assertEquals(0, p.reach, 1e-12);
    }
    @Test public void cutFillSignAndToleranceAreConsistent() {
        Design d = Design.level(10);
        assertTrue(new Guidance(d, new PoseSolver.Pose(0, 0, 10.03)).onGrade);
        assertTrue(new Guidance(d, new PoseSolver.Pose(0, 0, 10.2)).delta > 0);
        assertTrue(new Guidance(d, new PoseSolver.Pose(0, 0, 9.8)).delta < 0);
    }
    @Test public void bothWorkflowsCanReachDesignAndProduceGuidance() {
        MachineConfig m = new MachineConfig("测试机械", 3, 2, 1);
        PoseSolver.Pose p = new DemoPoseSolver().solve(m, 10, 0, 0, 0, 0);
        assertTrue(new Guidance(Design.level(10), p).onGrade);
        assertTrue(new Guidance(Design.trench(12, 2, 4, 1), p).onGrade);
    }
    @Test public void rejectsInvalidMachineAndDesignInputs() {
        assertThrows(IllegalArgumentException.class, () -> new MachineConfig("", 1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new MachineConfig("a", Double.NaN, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> Design.level(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> Design.trench(10, 0, 2, 1));
        assertThrows(IllegalArgumentException.class, () -> Design.trench(10, 1, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> Design.trench(10, 1, 2, -1));
    }
}
