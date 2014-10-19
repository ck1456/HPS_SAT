package hps.nyu.fa14;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import solver.BruteForceSolver;
import solver.ISolver;

public class DataTest {

	@Test
	public void test_all_sat_8() throws Exception {
		ISolver solver = new BruteForceSolver(); 
		for(int i = 0; i < 5; i++){
			String file = String.format("data/prob0/sat_8_%d.txt", i);
			test_sat_file(solver, file);
		}
	}
	
	@Ignore // Doesn't work with brute force
	@Test
	public void test_all_sat_32() throws Exception {
		ISolver solver = new BruteForceSolver(); 
		for(int i = 0; i < 5; i++){
			String file = String.format("data/prob1/sat_32_%d.txt", i);
			test_sat_file(solver, file);
		}
	}
	
	private static long MAX_EXECUTION_TIME_SEC = 10000;
	private void test_sat_file(ISolver solver, String file) throws Exception {
		long start = System.currentTimeMillis();
		Formula f = Formula.parseFile(file);
		Assignment a = solver.solve(f);
		assertTrue(String.format("Formula is not satisfied [%s]", file), f.isSatisfied(a));
		long elapsed = System.currentTimeMillis() - start;
		assertTrue(String.format("Solution took too long to find [%s]", file), elapsed < (MAX_EXECUTION_TIME_SEC * 1000));
	}

}
