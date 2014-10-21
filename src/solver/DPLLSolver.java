package solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Attempts to satisfy the most number of clauses using Davis-Putnam (DPLL)
 * approach. Implements several satisfiability preserving transformations
 * 
 * @author ck1456@nyu.edu
 * 
 */
public class DPLLSolver implements ISolver {

	@Override
	public Assignment solve(Formula f) {
		return solve(f, new Assignment(f.maxLiteral()));
	}

	public Assignment solve(Formula f, Assignment partialAssignment) {

		boolean reduced = true; // Indicates whether we made forward progress
		while (reduced) {
			reduced = false;
			// While it is getting smaller..
			// do unit propagation and pure literal removal
			int unit = getUnitClause(f);
			if (unit != 0) {
				// record unit as set
				partialAssignment.values[Math.abs(unit)] = (unit == Math
						.abs(unit));
				reduced = true;
				// System.out.println("Unit propagation " + unit);
				f = unitPropagation(f, unit);
			}

			int pureLiteral = getPureLiteral(f);
			// Record this literal as set
			if (pureLiteral != 0) {
				partialAssignment.values[Math.abs(pureLiteral)] = (pureLiteral == Math
						.abs(pureLiteral));
				// System.out.println("Removing pure literal " + pureLiteral);
				f = removePureLiteral(f, pureLiteral);
				reduced = true;
			}
			// if (reduced) {
			// System.out.println("Reduced: " + f.getSizeDescription());
			// }
			if (hasEmptyClause(f)) {
				// System.out.println("- Unsatisfiable!");
				return null; // Fail and backtrack
			}
			if (hasNoClauses(f)) {
				// System.out.println("+ Satisfiable!");
				return partialAssignment; // We're done!
			}
		}

		// Splitting rule here
		int splitLit = getSymbolInMaxClauses(f);
		{
			Formula pos = f.clone();
			pos.clauses.add(new ArrayList<Integer>(Arrays.asList(splitLit)));
			// If this is satisfiable, mark this assignment and return it
			DPLLSolver recSolver = new DPLLSolver();
			Assignment a = recSolver.solve(pos, partialAssignment);
			if (a != null) {
				return a; // with the additional assignment
			}
		}
		{ // Otherwise, do it with -splitLit
			Formula neg = f.clone();
			neg.clauses.add(new ArrayList<Integer>(Arrays.asList(-splitLit)));
			// If this is satisfiable, mark this assignment and return it
			DPLLSolver recSolver = new DPLLSolver();
			Assignment a = recSolver.solve(neg, partialAssignment);
			return a; // with the additional assignment
		}
	}

	/**
	 * Returns a literal from a unit clause if it exists, 0 otherwise
	 * 
	 * @param f
	 * @return
	 */
	private static int getUnitClause(Formula f) {
		for (List<Integer> c : f.clauses) {
			if (c.size() == 1) {
				return c.get(0);
			}
		}
		return 0;
	}

	/**
	 * Suppose (p) is a unit clause, - Remove any instances of -p from the
	 * formula - Remove any clauses containing p (including the unit clause
	 * itself)
	 * 
	 * @param f
	 * @param unit
	 * @return
	 */
	private static Formula unitPropagation(Formula f, int unit) {
		f = f.clone(); // return a copy of f - don't modify it directly
		List<List<Integer>> clausesToRemove = new ArrayList<List<Integer>>();

		// Remove any clauses containing unit
		for (List<Integer> c : f.clauses) {
			for (Integer lit : c) {
				if (lit == unit) {
					clausesToRemove.add(c);
					break;
				}
			}
		}
		f.clauses.removeAll(clausesToRemove);
		// System.out.println(String.format("Removed %d clauses",
		// clausesToRemove.size()));

		// Remove any instances of -unit
		//int unitsRemoved = 0;
		Integer negUnit = -unit; // Must be an object so that list removal works
		for (List<Integer> c : f.clauses) {
			while (c.remove(negUnit)) {
				//unitsRemoved++;
			}
		}
		// System.out.println(String.format("Removed %d negative units",
		// unitsRemoved));
		return f;
	}

	/**
	 * Returns a literal that occurs only positively, or only negatively, or 0
	 * otherwise
	 * 
	 * @param f
	 * @return
	 */
	private static int getPureLiteral(Formula f) {

		Set<Integer> pureLiterals = new HashSet<Integer>();
		Set<Integer> seenLiterals = new HashSet<Integer>();
		for (List<Integer> c : f.clauses) {
			for (int lit : c) {
				if (!seenLiterals.contains(lit) && !seenLiterals.contains(-lit)) {
					seenLiterals.add(lit);
					// If we haven't seen it or its complement yet, it is a pure
					// literal (so far)
					pureLiterals.add(lit);
				}
				if (pureLiterals.contains(-lit)) {
					// We know this is not a pure literal
					pureLiterals.remove(-lit);
				}
			}
		}

		if (pureLiterals.size() > 0) {
			// Found a pure literal, return it
			return pureLiterals.toArray(new Integer[0])[0]; // the first element
															// in the set
		}
		return 0;
	}

	/**
	 * If a literal appears only positively or negatively, delete all clauses
	 * containing that literal
	 * 
	 * @param f
	 * @return
	 */
	private static Formula removePureLiteral(Formula f, int lit) {
		f = f.clone();
		List<List<Integer>> clausesToRemove = new ArrayList<List<Integer>>();
		// Delete all clauses containing this literal
		for (List<Integer> c : f.clauses) {
			for (Integer literal : c) {
				if (literal == lit) {
					clausesToRemove.add(c);
					break;
				}
			}
		}
		f.clauses.removeAll(clausesToRemove);
		return f;
	}

	/**
	 * Implies that the formula is unsatisfiable
	 * 
	 * @param f
	 * @return
	 */
	private static boolean hasEmptyClause(Formula f) {
		for (List<Integer> c : f.clauses) {
			if (c.size() == 0) {
				return true;
			}
		}
		return false; // Haven't proved unsatisfiability
	}

	/**
	 * Implies the formula is satisfiable (trivially)
	 * 
	 * @param f
	 * @return
	 */
	private static boolean hasNoClauses(Formula f) {
		return f.clauses.size() == 0; // ==> Satisfiable
	}

	/**
	 * Returns the symbol that occurs (positively or negatively) in the most
	 * number of clauses
	 */
	private int getSymbolInMaxClauses(Formula f) {
		Map<Integer, Integer> symbolCounts = new HashMap<Integer, Integer>();
		for (List<Integer> c : f.clauses) {
			for (int lit : c) {
				int posLit = Math.abs(lit);
				if (!symbolCounts.containsKey(posLit)) {
					symbolCounts.put(posLit, 0);
				}
				// Increment symbol
				symbolCounts.put(posLit, symbolCounts.get(posLit) + 1);
			}
		}
		int maxCount = 0;
		int maxSymbol = 0;
		for (Integer s : symbolCounts.keySet()) {
			if (symbolCounts.get(s) > maxCount) {
				maxSymbol = s;
				maxCount = symbolCounts.get(s);
			}
		}
		return maxSymbol;
	}
}
