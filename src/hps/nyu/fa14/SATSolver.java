package hps.nyu.fa14;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import solver.DPLLSolver;
import solver.GLSSolver;
import solver.ISolver;

public class SATSolver {

  static Assignment solve(Formula f) {
    long start = System.currentTimeMillis();
    // TODO: Implement something much smarter here
    ISolver solver = new DPLLSolver(); // Best for Problem 1 or (0)
    // ISolver solver = new GLSSolver();
    if (f.maxLiteral() > 34) {
      // Problems of type 2 or 3
      solver = // new RandomSolver();
      // new BruteForceSolver(); // Works for Problem 0
      // new DPLLSolver(); // Best for Problem 1
      new GLSSolver(); // Best for Problem 3
    }
    Assignment a = solver.solve(f);
    long elapsed = System.currentTimeMillis() - start;
    System.out
        .println(String.format("Time to Solve: %2.2f ", elapsed / 1000.0));
    return a;
  }

  public static void main(String[] args) throws FileNotFoundException,
      IOException {

    if (args.length != 2) {
      usage();
    }
    // first parameter is input
    String inputFile = args[0];
    String outputFile = args[1];

    Formula f = Formula.parseFile(inputFile);
    Assignment a = solve(f);

    // Make directory for the output file if it does not exist
    File outFile = new File(outputFile);
    outFile.getAbsoluteFile().getParentFile().mkdirs();
    a.write(new FileOutputStream(outFile));
  }

  private static void usage() {
    // How to use it
    System.out.println("java -jar SATSolver <input> <output>");
    System.exit(1);
  }

}
