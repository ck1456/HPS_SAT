package hps.nyu.fa14;

import static org.junit.Assert.*;
import hps.nyu.fa14.solver.GLSSolver;
import hps.nyu.fa14.solver.ISolver;
import hps.nyu.fa14.solver.MultiSolver;
import hps.nyu.fa14.solver.TimedSolver;

import org.junit.Test;

public class MultiSolverTest {

	@Test
	public void test() throws Exception {
		
		String file = "data/prob3/sat_128_0.txt";
		Formula f = Formula.parseFile(file);
		int runTime = 60;
		
		ISolver single = new TimedSolver(new GLSSolver(), runTime);
		int singleFitness = f.numClausesSatisfied(single.solve(f));
		
		long start = System.currentTimeMillis();

		int processors = Runtime.getRuntime().availableProcessors();
		MultiSolver ms = new MultiSolver();
		for(int s = 0; s < processors - 1; s++){
			ISolver ev = new TimedSolver(new GLSSolver(), runTime);
			
			ms.addSolver(ev);
		}
		Assignment a = ms.solve(f);
		assertNotNull(a);
		
		long elapsed = System.currentTimeMillis() - start;
		assertTrue(
				String.format("Solution took too long to find [%s]", file),
				elapsed < (runTime * 1000));
		int multiFitness = f.numClausesSatisfied(a);
		
		// Make sure this is better than a single invocation
		assertTrue(String.format("Multi %d !> Single %d", multiFitness, singleFitness), multiFitness > singleFitness);
		
	}

}
