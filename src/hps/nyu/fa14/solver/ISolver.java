package hps.nyu.fa14.solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;
import hps.nyu.fa14.ISolutionMonitor;

public interface ISolver {

  Assignment solve(Formula f);
  void addSolutionMonitor(ISolutionMonitor monitor);
  
}
