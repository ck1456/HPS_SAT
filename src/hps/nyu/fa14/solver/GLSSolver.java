package hps.nyu.fa14.solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GLSSolver extends AbstractSolver {

  int populationSize = 50;
  int generations = 50;
  double mutationProb = 0.9;
  double flipProb = 0.5;
  int maxRestarts = 10;
  int numGenerationsBeforeRestart = 3;
  int numOfgenerationsWithoutImprov = 0;
  private Random rand = new Random();

  /*
   * (non-Javadoc)
   * 
   * @see hps.nyu.fa14.solver.AbstractSolver#solve(hps.nyu.fa14.Formula) solve
   * uses localsearch exactly as given in the paper. It is a simple heuristic
   * But seems to work for a majority of the problems.
   */
  @Override
  public Assignment solve(Formula f) {
    List<List<Assignment>> populations = new ArrayList<List<Assignment>>();
    List<Assignment> population = new ArrayList<Assignment>();
    for (int i = 0; i < populationSize; i++) {
      Assignment a = Assignment.random(f.maxLiteral());
      population.add(a);
    }
    populations.add(population);
    int globalResult = 0;
    Assignment globalBest = Assignment.random(f.literalCount());
    int t = 0;
    while (t < generations) {
      // select parents
      t++;
      int[] parents = getParents(populations.get(t - 1), f);
      populations.add(new ArrayList<Assignment>());
      while (populations.get(t).size() < populations.get(t - 1).size()) {
        // parents selected
        // recombine parents
        Assignment b = combineParents(populations.get(t - 1).get(parents[0]),
            populations.get(t - 1).get(parents[1]));
        // mutate after recombination
        b = mutate(b);
        b = localSearch(b, f);
        populations.get(t).add(b);
        // System.out.println(f.numClausesSatisfied(b));
      }
      Assignment best = getBest(populations.get(t), f);
      int bestResult = f.numClausesSatisfied(best);
      if (globalResult < bestResult) {
        globalBest = Assignment.clone(best);
        notifyNewAssignment(globalBest);
        globalResult = bestResult;
      }
    }
    // Assignment best = getBest(populations.get(t - 1), f);
    // int bestResult = f.numClausesSatisfied(best);
    System.out.println("Best Result: " + globalResult * 1.0 / f.clauseCount());
    return globalBest;
  }

  /*
   * solve2 uses localsearch2 which evaluates the highest gain possible by each
   * bit flip before actually flipping.
   */

  public Assignment solve2(Formula f) {

    List<List<Assignment>> populations = new ArrayList<List<Assignment>>();
    List<Assignment> population = new ArrayList<Assignment>();
    for (int i = 0; i < populationSize; i++) {
      Assignment a = Assignment.random(f.maxLiteral());
      population.add(a);
    }
    populations.add(population);

    int t = 0;
    int improvement = f.numClausesSatisfied(population.get(0));
    int numGenerationsWithoutImprov = 0;
    int numRestarts = 0;
    int globalBest = 0;
    Assignment globalBestSolution = Assignment.random(f.maxLiteral());
    while (t < generations) {
      // select parents
      t++;
      Assignment bestNewGen = getBest(population, f);
      int[] parents = getParents(populations.get(t - 1), f);
      populations.add(new ArrayList<Assignment>());
      while (populations.get(t).size() < populations.get(t - 1).size()) {
        // parents selected
        // recombine parents
        Assignment b = combineParents(populations.get(t - 1).get(parents[0]),
            populations.get(t - 1).get(parents[1]));
        // mutate after recombination
        b = mutate(b);
        b = localSearch2(b, f);
        populations.get(t).add(b);
        // System.out.println(f.numClausesSatisfied(b));
      }
      Assignment best = getBest(populations.get(t - 1), f);
      int bestResult = f.numClausesSatisfied(best);
      if (globalBest < bestResult) {
        globalBest = bestResult;
        globalBestSolution = Assignment.clone(best);
        notifyNewAssignment(globalBestSolution);
      } else {
        numOfgenerationsWithoutImprov++;
        if (numOfgenerationsWithoutImprov == numGenerationsBeforeRestart
            && numRestarts <= maxRestarts) {
          numGenerationsWithoutImprov = 0;
          numRestarts++;
          population = new ArrayList<Assignment>();
          for (int i = 0; i < populationSize; i++) {
            Assignment a = Assignment.random(f.maxLiteral());
            population.add(a);
          }
          populations.set(0, population);
          t = 0;
        }
      }
    }
    // Assignment best = getBest(populations.get(t - 1), f);
    // int bestResult = f.numClausesSatisfied(best);
    System.out.println("Best Result: " + globalBest * 1.0 / f.clauseCount());
    return globalBestSolution;
  }

  private Assignment getBest(List<Assignment> population, Formula f) {
    int max = -1;
    int index = 0;
    int i = 0;
    for (Assignment a : population) {
      int numClauses = f.numClausesSatisfied(a);
      if (max < numClauses) {
        max = numClauses;
        index = i;
      }
      i++;
    }
    return population.get(index);
  }

  private Assignment mutate(Assignment b) {
    Assignment a = new Assignment(b.values.length - 1);
    for (int i = 1; i < a.values.length; i++) {
      int n = rand.nextInt(100);
      if (n < mutationProb * 100) {
        // mutate with prob = flipProb
        if (rand.nextBoolean()) {
          a.values[i] = !b.values[i];
        } else {
          a.values[i] = b.values[i];
        }
      }
    }
    return a;
  }

  private Assignment combineParents(Assignment parent1, Assignment parent2) {
    int size = parent1.values.length;
    Assignment a = new Assignment(size - 1);
    for (int i = 1; i < size; i++) {
      if (rand.nextBoolean()) {
        a.values[i] = parent1.values[i];
      } else {
        a.values[i] = parent2.values[i];
      }
    }
    return a;
  }

  private int[] getParents(List<Assignment> population, Formula f) {
    int max1 = -1;
    int max2 = -1;
    int i = 0;
    int[] topTwo = new int[2];
    for (Assignment a : population) {
      int numClauses = f.numClausesSatisfied(a);
      if (max1 < numClauses) {
        max2 = max1;
        topTwo[1] = topTwo[0];
        max1 = numClauses;
        topTwo[0] = i;
      }
      i++;
    }
    return topTwo;
  }

  public static Assignment localSearch(Assignment b, Formula f) {
    int improve = 1;
    Assignment a = new Assignment(f.maxLiteral());
    for (int j = 1; j < b.values.length; j++) {
      a.values[j] = b.values[j];
    }
    int num = f.numClausesSatisfied(a);
    int temp = 0;
    while (improve > 0) {
      improve = 0;
      int i = 1;
      while (i < a.values.length) {
        a.values[i] = !a.values[i];
        temp = f.numClausesSatisfied(a);
        int gain = temp - num;
        if (gain >= 0) {
          // do nothing (keep flip)
          num = temp;
          improve += gain;
        } else {
          // flip back
          a.values[i] = !a.values[i];
        }
        i++;
      }
    }
    return a;
  }

  public Assignment localSearch2(Assignment b, Formula f) {
    int gain = 1;
    Assignment a = new Assignment(f.maxLiteral());
    for (int j = 1; j < b.values.length; j++) {
      a.values[j] = b.values[j];
    }
    int num = f.numClausesSatisfied(a);
    int temp = 0;
    int maxGainIndex = 0;
    while (gain > 0) {
      gain = 0;
      num = f.numClausesSatisfied(a);
      int maxGain = gain;
      int i = 1;
      while (i < a.values.length) {
        a.values[i] = !a.values[i];
        temp = f.numClausesSatisfied(a);
        gain = temp - num;
        if (maxGain < gain) {
          maxGain = gain;
          maxGainIndex = i;
        }
        a.values[i] = !a.values[i];
        i++;
      }
      a.values[maxGainIndex] = !a.values[maxGainIndex];
    }
    return a;
  }
}
