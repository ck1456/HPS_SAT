package solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;

public class BruteForceSolver implements ISolver {

	@Override
	public Assignment solve(Formula f) {
		
		Assignment a = new Assignment(f.maxLiteral());
		
		if(solve(f, a, 0)){
			return a;
		}
		
		return null; // no solution
	}

	/**
	 * Generate successive solutions recursively in a depth-first manner
	 * @param f
	 * @param a
	 * @param bitPosition
	 * @return
	 */
	private boolean solve(Formula f, Assignment a, int bitPosition){
		if(bitPosition > f.maxLiteral()){
			return false;
		}
		// Try one option
		a.values[bitPosition] = false;
		if(f.isSatisfied(a)){
			return true;
		}		
		if(solve(f, a, bitPosition + 1)){
			return true;
		}
		// Try the other option
		a.values[bitPosition] = true;
		if(f.isSatisfied(a)){
			return true;
		}		
		if(solve(f, a, bitPosition + 1)){
			return true;
		}
		return false; // no solution here
	}
}
