package solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;

public class RandomSolver implements ISolver {

  @Override
  public Assignment solve(Formula f) {
    return Assignment.random(f.maxLiteral());
  }

}
