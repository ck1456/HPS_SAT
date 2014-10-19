package hps.nyu.fa14;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SATSolver {

	static Assignment solve(Formula f) {
		// TODO: Implement something much smarter here

		return Assignment.random(f.maxLiteral());
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		if (args.length != 2) {
			usage();
		}
		// first parameter is input
		String inputFile = args[0];
		String outputFile = args[1];

		Formula f = Formula.parse(new FileInputStream(new File(inputFile)));
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
