package hps.nyu.fa14;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import solver.EvolutionarySolver;
import solver.ISolver;
import solver.TimedSolver;

public class TimedSolverTest {

	@Test
	public void testContstrainTime() throws Exception {
		for (int m = 10; m > 0; m = m / 2) {
			int maxTime = m;
			String file = "data/prob3/sat_128_0.txt";
			long start = System.currentTimeMillis();
			Formula f = Formula.parseFile(file);
			ISolver ev = new EvolutionarySolver();
			ISolver timedSolver = new TimedSolver(ev, maxTime);
			Assignment a = timedSolver.solve(f);
			assertFalse(String.format("Formula is satisfied [%s]", file),
					f.isSatisfied(a));
			long elapsed = System.currentTimeMillis() - start;
			assertTrue(
					String.format("Solution took too long to find [%s]", file),
					elapsed < (maxTime * 1000));
		}
	}

}
