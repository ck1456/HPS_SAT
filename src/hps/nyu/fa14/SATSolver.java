package hps.nyu.fa14;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import solver.BruteForceSolver;
import solver.GLSSolver;
import solver.ISolver;
import solver.RandomSolver;

public class SATSolver {

  static Assignment solve(Formula f) {
    // TODO: Implement something much smarter here
    ISolver solver = new GLSSolver();

    return solver.solve(f);
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
    outFile.getParentFile().mkdirs();
    a.write(new FileOutputStream(outFile));
  }

  private static void usage() {
    // How to use it
    System.out.println("java -jar SATSolver <input> <output>");
    System.exit(1);
  }

}
