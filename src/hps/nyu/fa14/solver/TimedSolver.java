package hps.nyu.fa14.solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;
import hps.nyu.fa14.ISolutionMonitor;

public class TimedSolver extends AbstractSolver implements ISolutionMonitor,
    Runnable {

  private final int maxSeconds;
  private final ISolver solver;
  private Formula currentFormula;
  // Make sure to give yourself this much overhead for setting up the thread
  private final int SETUP_MILLIS = 150;

  public TimedSolver(ISolver solver, int seconds) {
    this.solver = solver;
    maxSeconds = seconds;
  }

  @Override
  public Assignment solve(Formula f) {

    // Set up the solver to report the best solution reported so far
    currentFormula = f;

    // run a thread.
    Thread solveThread = new Thread(this);
    solveThread.start();
    try {
      // Wait until the thread finishes or we time out
      solveThread.join((maxSeconds * 1000) - SETUP_MILLIS);
    } catch (Exception ex) {/* suppress */
      System.out.println();
    }

    if (!solveThread.isAlive()) {
      solveThread.interrupt();
    }

    // Wait a certain amount of time, then kill and output
    return bestAssignment;
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    solver.addSolutionMonitor(this);
    Assignment a = solver.solve(currentFormula);
    // If we get this far, assume the solver returned the best assignment
    bestAssignment = a;
  }

  // we depend on the Solvers to only give us increasingly good assignments.
  // This just outputs the last one received.
  private Assignment bestAssignment;

  @Override
  public void updateAssignment(Assignment a) {
    bestAssignment = a;
  }
}
