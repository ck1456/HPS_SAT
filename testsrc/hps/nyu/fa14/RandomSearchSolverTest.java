package hps.nyu.fa14;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import hps.nyu.fa14.solver.ISolver;
import hps.nyu.fa14.solver.RandomSearchSolver;
import hps.nyu.fa14.solver.TimedSolver;

import org.junit.Ignore;
import org.junit.Test;

public class RandomSearchSolverTest {

	@Ignore
	@Test
	public void testRandomSolver() throws Exception {
		String file = "data/prob3/sat_128_0.txt";
		Formula f = Formula.parseFile(file);
		ISolver rs = new RandomSearchSolver(100);
		
		Assignment a = rs.solve(f);
		assertNotNull(a);
		int clausesSatisfied = f.numClausesSatisfied(a);
		System.out.println("Clauses satisfied: " + clausesSatisfied);
		assertTrue(clausesSatisfied >= 480);
	}

	@Ignore
	@Test
	public void testTimedRandomSolver() throws Exception {
		String file = "data/prob3/sat_128_0.txt";
		Formula f = Formula.parseFile(file);
		ISolver rs = new RandomSearchSolver(10000);
		ISolver ts = new TimedSolver(rs, 60);
		Assignment a = ts.solve(f);
		assertNotNull(a);
		int clausesSatisfied = f.numClausesSatisfied(a);
		System.out.println("Clauses satisfied: " + clausesSatisfied);
		assertTrue(clausesSatisfied >= 480);
	}

}
