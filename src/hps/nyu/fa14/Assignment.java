package hps.nyu.fa14;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

public class Assignment {

	/**
	 * There is no index zero literal, so this is just ignored, but the array is actually one larger than strictly necessary
	 */
	final boolean[] values;

	Assignment(int literalCount) {
		values = new boolean[literalCount + 1];
	}
	
	private static final Random RAND = new Random();
	
	static Assignment random(int literalCount){
		Assignment a = new Assignment(literalCount);
		for(int i = 1; i < a.values.length; i++){
			a.values[i] = RAND.nextBoolean();
		}
		return a;
	}
	
	/**
	 * returns an assignment with all values set to the specified value
	 * @param literalCount
	 * @return
	 */
	static Assignment allSet(int literalCount, boolean value){
		Assignment a = new Assignment(literalCount);
		for(int i = 1; i < a.values.length; i++){
			a.values[i] = value;
		}
		return a;
	}
	
	void write(OutputStream output) throws IOException{
		
		StringBuilder sb = new StringBuilder();
		// There is no 0 literal
		for(int i = 1; i < values.length; i++){
			sb.append(i * (values[i] ? 1 : -1));
			sb.append(" ");
		}
		sb.append(0); // Line is always terminated with a 0
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
		bw.write(sb.toString());
		bw.close();
	}

}
