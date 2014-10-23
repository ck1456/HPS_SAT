package hps.nyu.fa14.solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;
import hps.nyu.fa14.ISolutionMonitor;

import java.util.ArrayList;
import java.util.List;

public class MultiSolver extends AbstractSolver implements ISolutionMonitor {

	private final List<ISolver> solvers = new ArrayList<ISolver>();

	public void addSolver(ISolver solver) {
		solver.addSolutionMonitor(this);
		solvers.add(solver);
	}

	private Formula currentFormula;

	@Override
	public Assignment solve(Formula f) {
		currentFormula = f;

		List<Thread> runningThreads = new ArrayList<Thread>();
		for (ISolver s : solvers) {
			SolutionRunner newRunner = new SolutionRunner(s);

			Thread t = new Thread(newRunner);
			runningThreads.add(t);
			t.start();
		}

		// Wait on these threads to complete
		for (Thread t : runningThreads) {
			try {
				t.join();
			} catch (Exception ex) {
				System.out.println("Thread exception");
				ex.printStackTrace();
			}
		}
		synchronized (currentFormula) {
			return bestAssignment;
		}
	}

	@Override
	public void updateAssignment(Assignment a) {
		updateIfBest(a);
	}

	private Assignment bestAssignment;
	private int bestFitness;

	private void updateIfBest(Assignment a) {
		synchronized (currentFormula) {
			int newFitness = currentFormula.numClausesSatisfied(a);
			if (newFitness > bestFitness) {
				bestAssignment = a;
			}
		}
	}

	private class SolutionRunner implements Runnable {

		private ISolver solver;

		public SolutionRunner(ISolver s) {
			solver = s;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Assignment a = solver.solve(currentFormula);
			updateIfBest(a); // see if this is actually the best
		}

	}

}
