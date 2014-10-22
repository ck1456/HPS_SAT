package solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;

import java.util.ArrayList;
import java.util.List;

public class GLSSolver implements ISolver {

  int populationSize = 50;

  @Override
  public Assignment solve(Formula f) {
    List<Assignment> population = new ArrayList<Assignment>();
    Assignment a = new Assignment(f.maxLiteral());
    population.add(a);

    int t = 0;
    population.set(t, localSearch(population.get(t)));
    population.add(population.get(0));
    int num = f.numClausesSatisfied(population.get(t));
    while (num < (f.clauseCount())) {
      t++;
      while (f.numClausesSatisfied(population.get(t)) > f
          .numClausesSatisfied(population.get(t - 1))) {
        Assignment b = new Assignment(f.maxLiteral());
        for (int j = 0; j < f.maxLiteral() / 2; j++) {
          b.values[j] = population.get(t - 1).values[j];
        }
        for (int j = f.maxLiteral() / 2; j < f.maxLiteral(); j++) {
          b.values[j] = population.get(t).values[j];
        }
        b = localSearch(b);
        population.add(b);
      }
    }
    System.out.println("Num " + num);
    return population.get(population.size() - 1);
  }

  public Assignment localSearch(Assignment a) {
    int improve = 1;
    while (improve > 0) {
      improve = 0;
      int i = 1;
      while (i < a.values.length - 1) {
        a.values[i] = !a.values[i];
        // calculate gain
        int gain = 0;
        if (gain >= 0) {
          // do nothing (keep flip)
          improve += gain;
        } else {
          a.values[i] = !a.values[i];
        }
        i++;
      }
    }
    return a;
  }

  /**
   * Generate successive solutions recursively in a depth-first manner
   * 
   * @param f
   * @param a
   * @param bitPosition
   * @return
   */
  private boolean solve(Formula f, Assignment a, int bitPosition) {
    if (bitPosition > f.maxLiteral()) {
      return false;
    }
    // Try one option
    a.values[bitPosition] = false;
    if (f.isSatisfied(a)) {
      return true;
    }
    if (solve(f, a, bitPosition + 1)) {
      return true;
    }
    // Try the other option
    a.values[bitPosition] = true;
    if (f.isSatisfied(a)) {
      return true;
    }
    if (solve(f, a, bitPosition + 1)) {
      return true;
    }
    return false; // no solution here
  }
}
