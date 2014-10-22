package hps.nyu.fa14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Formula {

  public final List<List<Integer>> clauses;

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

  public int clauseCount() {
    return clauses.size();
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
    int count = 0;
    // Iterate over the clauses and see if it is satisfied
    for (List<Integer> c : clauses) {
      for (int signedVar : c) {
        int var = Math.abs(signedVar);
        // If the literal exists in the assignment in the same polarity, this
        // clause is true
        if ((a.values[var] && (var == signedVar))
            || (!a.values[var] && (var != signedVar))) {
          count++;
          break; // This clause is satisfied by this assignment
        }
      }
    }
    return count;
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
