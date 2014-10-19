package hps.nyu.fa14;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;

public class FormulaTest {

	@Test
	public void testParse_8_0() throws Exception {
		File sampleFile = new File("data/prob0/sat_8_0.txt");
		Formula f = Formula.parse(new FileInputStream(sampleFile));

		assertEquals(20, f.clauses.size());
		assertEquals(8, f.maxLiteral());
	}

	@Test
	public void testParse_8_4() throws Exception {
		File sampleFile = new File("data/prob0/sat_8_4.txt");
		Formula f = Formula.parse(new FileInputStream(sampleFile));

		assertEquals(5, f.clauses.size());
		assertEquals(7, f.maxLiteral());
	}

	@Test
	public void testIsSatisfied() throws Exception {
		File sampleFile = new File("data/prob0/sat_8_0.txt");
		Formula f = Formula.parse(new FileInputStream(sampleFile));

		Assignment a = new Assignment(f.maxLiteral());
		a.values[1] = false;
		a.values[2] = false;
		a.values[3] = false;
		a.values[4] = true;
		a.values[5] = true;
		a.values[6] = false;
		a.values[7] = false;
		a.values[8] = false;
		assertTrue(f.isSatisfied(a));
		assertFalse(f.isSatisfied(Assignment.allSet(f.maxLiteral(), false)));
		assertFalse(f.isSatisfied(Assignment.allSet(f.maxLiteral(), true)));
	}
	
	@Test
	public void testCountClauses() throws Exception {
		File sampleFile = new File("data/prob0/sat_8_0.txt");
		Formula f = Formula.parse(new FileInputStream(sampleFile));

		Assignment a = new Assignment(f.maxLiteral());
		a.values[1] = true;
		assertFalse(f.isSatisfied(a));
		assertEquals(16, f.numClausesSatisfied(a));
	}
}
