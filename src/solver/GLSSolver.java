package solver;

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

  @Override
  public Assignment solve(Formula f) {
    List<List<Assignment>> populations = new ArrayList<List<Assignment>>();
    List<Assignment> population = new ArrayList<Assignment>();
    for (int i = 0; i < populationSize; i++) {
      Assignment a = new Assignment(f.maxLiteral());
      population.add(a);
    }
    populations.add(population);

    int t = 0;
    // System.out.println(f.numClausesSatisfied(population.get(0)));
    // population.add(localSearch(population.get(t), f));
    // population.add(population.get(0));
    // int num = f.numClausesSatisfied(population.get(1));
    // System.out.println(num);
    // int iterations = 0;
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
    }
    return getBest(populations.get(t - 1), f);
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
    Random rand = new Random();
    Assignment a = new Assignment(b.values.length - 1);
    for (int i = 0; i < a.values.length; i++) {
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
    Random rand = new Random();
    for (int i = 0; i < size; i++) {
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

  public Assignment localSearch(Assignment b, Formula f) {
    int improve = 1;
    Assignment a = new Assignment(f.maxLiteral());
    for (int j = 1; j < b.values.length; j++) {
      a.values[j] = b.values[j];
    }
    int num = f.numClausesSatisfied(a);
    int temp = 0;
    while (improve > 0) {
      improve = 0;
      int i = 0;
      while (i < a.values.length - 1) {
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
}
