package solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;

public class RandomSolver extends AbstractSolver {

  @Override
  public Assignment solve(Formula f) {
    return Assignment.random(f.maxLiteral());
  }

}
