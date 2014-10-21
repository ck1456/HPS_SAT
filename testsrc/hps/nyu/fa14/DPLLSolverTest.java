package hps.nyu.fa14;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import solver.DPLLSolver;
import solver.ISolver;

public class DPLLSolverTest {

	// These are all satisfiable
	@Test
	public void testReduce8() throws Exception {
		for(int i = 0; i < 5; i++){
			ISolver solver = new DPLLSolver(); 
			String file = String.format("data/prob0/sat_8_%d.txt", i);
			Formula f = Formula.parseFile(file);
			Assignment a = solver.solve(f.clone());
			assertNotNull(a);
			assertTrue(String.format("Not satisfied: %s", file), f.isSatisfied(a));
		}
	}

	// These are all satisfiable
	@Test
	public void testReduce32() throws Exception {
		for(int i = 0; i < 5; i++){
			ISolver solver = new DPLLSolver(); 
			String file = String.format("data/prob1/sat_32_%d.txt", i);
			Formula f = Formula.parseFile(file);
			Assignment a = solver.solve(f.clone());
			assertNotNull(a);
			assertTrue(String.format("Not satisfied: %s", file), f.isSatisfied(a));
		}
	}
	
	// These are all unsatisfiable
	@Test
	public void testReduce64() throws Exception {
		for(int i = 0; i < 5; i++){
			ISolver solver = new DPLLSolver(); 
			String file = String.format("data/prob2/sat_64_%d.txt", i);
			Formula f = Formula.parseFile(file);
			Assignment a = solver.solve(f.clone());
			assertNull(String.format("Actually satisfiable(!): %s", file), a); // Verify that these are all unsatisfiable
		}
	}
	
	// These are all unsatisfiable
	@Ignore // I tested this but it just takes a long time
	@Test
	public void testReduce128() throws Exception {
		for(int i = 0; i < 5; i++){
			ISolver solver = new DPLLSolver(); 
			String file = String.format("data/prob3/sat_128_%d.txt", i);
			Formula f = Formula.parseFile(file);
			Assignment a = solver.solve(f.clone());
			assertNull(String.format("Actually satisfiable(!): %s", file), a); // Verify that these are all unsatisfiable
		}
	}

}
