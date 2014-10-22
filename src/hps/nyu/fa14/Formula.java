package hps.nyu.fa14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Formula {

  public final List<List<Integer>> clauses;

  /**
   * Specifies whether clauses are satisfied 0 - in the normal manner of boolean
   * logic (default) 1 - iff it contains exactly 1 literal that evaluates to
   * true 2 - iff it contains exactly 2 literals that evaluate to true
   */
  public int ExactSatisifiabilityConstraint = 0;

  public Formula() {
    clauses = new ArrayList<List<Integer>>();
  }

  public int maxLiteral() {
    int max = Integer.MIN_VALUE;
    for (List<Integer> c : clauses) {
      for (int lit : c) {
        max = Math.max(max, Math.abs(lit));
      }
    }
    return max;
  }

  public int literalCount() {
    Set<Integer> literals = new HashSet<Integer>();
    for (List<Integer> c : clauses) {
      for (int lit : c) {
        if (!literals.contains(-lit)) {
          literals.add(lit);
        }
      }
    }
    return literals.size();
  }

  public int clauseCount() {
    return clauses.size();
  }

  public String getSizeDescription() {
    return String.format("%d x %d", literalCount(), clauseCount());
  }

  /**
   * Applies the provided assignment to this formula and determines if every
   * clause is satisfied
   * 
   * @param a
   * @return
   */
  public boolean isSatisfied(Assignment a) {
    // If every clause is satisfied then the whole formula is satisfied
    return numClausesSatisfied(a) == clauses.size();
  }

  /**
   * Applies the provided assignment to this formula and reports the number of
   * clauses which evaluate to true
   * 
   * @param a
   * @return
   */
  public int numClausesSatisfied(Assignment a) {
    int clauseCount = 0;
    // Iterate over the clauses and see if it is satisfied
    for (List<Integer> c : clauses) {
      int litTrueCount = 0;
      for (int signedVar : c) {
        int var = Math.abs(signedVar);
        // If the literal exists in the assignment in the same polarity, this
        // clause is true
        if ((a.values[var] && (var == signedVar))
            || (!a.values[var] && (var != signedVar))) {
          litTrueCount++;
          if (ExactSatisifiabilityConstraint == 0) {
            // can short-circuit here in default satisfiability mode
            break;
          }
        }
      }
      // This will only get hit when ExactSatisfiability > 0 or the clause is
      // false
      if (ExactSatisifiabilityConstraint == 0 && litTrueCount > 0) {
        clauseCount++; // This clause is satisfied by this assignment
      } else if ((ExactSatisifiabilityConstraint > 0)
          && (litTrueCount == ExactSatisifiabilityConstraint)) {
        clauseCount++; // This clause is satisfied exactly by this assignment
      }
    }
    return clauseCount;
  }

  /**
   * Create and return a deep copy of a formula
   */
  public Formula clone() {
    Formula newFormula = new Formula();
    newFormula.ExactSatisifiabilityConstraint = ExactSatisifiabilityConstraint;

    for (List<Integer> c : clauses) {
      List<Integer> newC = new ArrayList<Integer>();
      for (int lit : c) {
        newC.add(lit);
      }
      newFormula.clauses.add(newC);
    }
    return newFormula;
  }

  /**
   * Just a convenience method
   * 
   * @param path
   * @return
   * @throws IOException
   * @throws FileNotFoundException
   */
  public static Formula parseFile(String path) throws FileNotFoundException,
      IOException {
    return Formula.parse(new FileInputStream(new File(path)));
  }

  public static Formula parse(InputStream input) throws IOException {
    Formula f = new Formula();

    BufferedReader br = new BufferedReader(new InputStreamReader(input));
    // read and ignore any line that starts with a c (for comment)
    String line;
    while ((line = br.readLine()) != null) {
      String type = line.split("\\s")[0].trim().toLowerCase();
      if (type.equals("c")) {
        // ignore comment lines
      } else if (type.equals("p")) {
        // parse problem type (if we care)
      } else if (type.equals("exactly-1")) {
        f.ExactSatisifiabilityConstraint = 1;
      } else if (type.equals("exactly-2")) {
        f.ExactSatisifiabilityConstraint = 2;
      } else {
        // parse this as a clause
        List<Integer> newClause = new ArrayList<Integer>();
        for (String lit : line.split("\\s")) {
          int intLit = Integer.parseInt(lit.trim());
          if (intLit == 0) {
            // signifies end of clause
            // continue;
          } else {
            newClause.add(intLit);
          }
        }
        f.clauses.add(newClause);
      }
    }
    return f;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (List<Integer> c : clauses) {
      sb.append("(");
      for (int lit : c) {
        sb.append("" + lit + " ");
      }
      sb.append(") ");
    }
    return sb.toString();
  }
}
