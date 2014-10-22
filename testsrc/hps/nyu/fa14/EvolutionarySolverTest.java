package hps.nyu.fa14;

import static org.junit.Assert.assertTrue;
import hps.nyu.fa14.solver.EvolutionarySolver;
import hps.nyu.fa14.solver.ISolver;

import org.junit.Test;


public class EvolutionarySolverTest {

	@Test
	public void testEvolve0() throws Exception {

		//TODO: Make sure it can find these fast enough
		ISolver solver = new EvolutionarySolver();
		String file = "data/prob3/sat_128_0.txt";
		Formula f = Formula.parseFile(file);
		Assignment a = solver.solve(f.clone());
		int clausesSatisfied = f.numClausesSatisfied(a);
		assertTrue(clausesSatisfied >= 364);
		assertTrue("Not enough clauses satisfied", (double)clausesSatisfied / f.clauseCount() >= .5);
	}

}
