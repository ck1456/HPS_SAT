package solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Attempts to satisfy the most number of clauses through an evolutionary
 * approach
 * 
 * @author ck1456@nyu.edu
 * 
 */
public class EvolutionarySolver implements ISolver {

	private static final int TARGET_POPULATION = 30;
	private static int iterations = 1000;
	private static int restarts = 500;
	private static double MUTATION_RATE = .05;
	private static double MATE_RATE = .9;
	private static Random RAND = new Random();
	private static int PLATEAU_COUNT = 50;

	private Assignment bestAssignment;
	private int bestFitness;

	private Assignment globalBestAssignment;
	private int globalBestFitness;

	@Override
	public Assignment solve(Formula f) {

		List<Assignment> population = generateInitialPopulation(f.maxLiteral(),
				TARGET_POPULATION);
		// TODO: Parallelize this
		// TODO: Run for as much time as we have
		for (int r = 0; r < restarts; r++) {
			int noGainCount = 0;
			for (int i = 0; i < iterations; i++) {

				List<RankedAssignment> ranked = rankAssignments(population, f);
				boolean gained = updateBestAssignment(ranked.get(0));
				if (!gained) {
					noGainCount++;
				}
				if (noGainCount > PLATEAU_COUNT) {
					//System.out.println("Plateau " + noGainCount++ + "/" + i);
					break;
				}

				List<Assignment> nextGen = new ArrayList<Assignment>();
				// Reweight distribution
				for (int p = 0; p < ranked.size(); p++) {
					Assignment a = getWeightedRandomAssignment(ranked);
					if (RAND.nextDouble() < MATE_RATE) {
						Assignment b = getWeightedRandomAssignment(ranked);
						nextGen.addAll(mate(a, b));
					}
					nextGen.add(a);
				}

				// TODO: Is ranking twice necessary?
				ranked = rankAssignments(nextGen, f);
				population = selectNextGeneration(ranked, TARGET_POPULATION);

			}
			// update global assignment if appropriate
			boolean globalBest = updateGlobalBestAssignment();
			//if(globalBest){
			//	System.out.println("New Global Best " + globalBestFitness + "/" + f.clauseCount());
			//}
			bestAssignment = null;
			bestFitness = 0;
		}

		return globalBestAssignment;
	}

	private Assignment getWeightedRandomAssignment(List<RankedAssignment> ranked) {
		double target = RAND.nextDouble();
		double total = 0.0;
		for (RankedAssignment r : ranked) {
			total += r.normalizedScore;
			if (target <= total) {
				return r.assignment;
			}
		}
		throw new IndexOutOfBoundsException("Ranked values are not normalized");
	}

	private boolean updateGlobalBestAssignment() {
		if (globalBestAssignment == null || bestFitness > globalBestFitness) {
			globalBestAssignment = bestAssignment;
			globalBestFitness = bestFitness;
			return true;
		}
		return false;
	}
	
	private boolean updateBestAssignment(RankedAssignment top) {
		if (bestAssignment == null || top.fitness > bestFitness) {
			bestAssignment = top.assignment;
			bestFitness = top.fitness;
			return true;
		}
		return false;
	}

	private static List<Assignment> selectNextGeneration(
			List<RankedAssignment> ranked, int count) {
		// Sort the list
		Collections.sort(ranked, RankedAssignment.RANK_BY_FITNESS);
		Collections.reverse(ranked);

		List<Assignment> newGen = new ArrayList<Assignment>();
		for (int i = 0; i < count; i++) {
			newGen.add(ranked.get(i).assignment);
		}
		return newGen;
	}

	// Generate new solutions/assignments
	private static List<Assignment> generateInitialPopulation(int maxLiteral,
			int count) {
		List<Assignment> population = new ArrayList<Assignment>();
		for (int i = 0; i < count; i++) {
			population.add(Assignment.random(maxLiteral));
		}
		return population;
	}

	// rank them all
	private static List<RankedAssignment> rankAssignments(List<Assignment> pop,
			Formula f) {
		List<RankedAssignment> ranks = new ArrayList<RankedAssignment>(
				pop.size());

		double totalFitness = 0.0;
		for (Assignment a : pop) {
			RankedAssignment newRanked = new RankedAssignment();
			newRanked.assignment = a;
			newRanked.fitness = f.numClausesSatisfied(a);
			totalFitness += newRanked.fitness;
			ranks.add(newRanked);
		}
		for (RankedAssignment r : ranks) {
			r.normalizedScore = r.fitness / totalFitness;
		}
		Collections.sort(ranks, RankedAssignment.RANK_BY_FITNESS);
		Collections.reverse(ranks);
		return ranks;
	}

	/**
	 * Combine two specified solutions into a new child, with a small
	 * possibility of random variation
	 */
	private List<Assignment> mate(Assignment p1, Assignment p2) {

		int length = p1.values.length - 1;
		Assignment c1 = new Assignment(length);
		Assignment c2 = new Assignment(length);
		// pick a random crossover point
		int crossover = RAND.nextInt(length + 1);
		int i = 0;
		// splice the two solutions
		while (++i < crossover) {
			c1.values[i] = p1.values[i];
			c2.values[i] = p2.values[i];
		}
		while (++i <= length) {
			c1.values[i] = p2.values[i];
			c2.values[i] = p1.values[i];
		}
		// Small chance that either child will have a bit flipped
		if (RAND.nextDouble() < MUTATION_RATE) {
			int position = RAND.nextInt(length + 1);
			c1.values[position] = !c1.values[position];
		}
		if (RAND.nextDouble() < MUTATION_RATE) {
			int position = RAND.nextInt(length + 1);
			c2.values[position] = !c2.values[position];
		}

		return new ArrayList<Assignment>(Arrays.asList(c1, c2));
	}

	// select the best and repeat
	private static class RankedAssignment {
		public Assignment assignment;
		public int fitness; // The number of clauses that are satisfied
		public double normalizedScore;

		public static Comparator<RankedAssignment> RANK_BY_FITNESS = new Comparator<RankedAssignment>() {

			@Override
			public int compare(RankedAssignment o1, RankedAssignment o2) {
				return (int) Math.signum(o1.fitness - o2.fitness);
			}
		};
	}
}
