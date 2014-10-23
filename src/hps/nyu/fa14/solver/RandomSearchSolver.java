package hps.nyu.fa14.solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;

/**
 * Guesses randomly, but then does local search, a specified number of times
 * 
 * @author ck1456@nyu.edu
 * 
 */
public class RandomSearchSolver extends AbstractSolver {

	private final int restarts;

	public RandomSearchSolver(int restarts) {
		this.restarts = restarts;
	}

	@Override
	public Assignment solve(Formula f) {

		for (int i = 0; i < restarts; i++) {
			Assignment a = GLSSolver.localSearch(
					Assignment.random(f.maxLiteral()), f);
			updateIfBest(a, f);
		}
		return bestAssignment;
	}

	private Assignment bestAssignment;
	private int bestFitness;

	private void updateIfBest(Assignment a, Formula f) {
		int newFitness = f.numClausesSatisfied(a);
		if (newFitness > bestFitness) {
			bestAssignment = a;
			notifyNewAssignment(bestAssignment);
		}
	}

}
