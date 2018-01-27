package ca.pfv.spmf.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;

/**
 * Example of how to use APRIORI algorithm from the source code.
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class MainTestApriori_saveToFile {

	public static void main(String [] arg) throws IOException{

		String input = "C:/Users/Sonakshi Grover/Desktop/iiitd/3rd summer term/Research Data mining/code/mushroom.txt";
		String output = "C:/Users/Sonakshi Grover/Desktop/output_apriori.txt";  // the path for saving the frequent itemsets found
		
		double minsup = 0.4; // means a minsup of 2 transaction (we used a relative support)
		
		// Applying the Apriori algorithm
		AlgoApriori apriori = new AlgoApriori();
		apriori.runAlgorithm(minsup, input, output);
		apriori.printStats();
	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestApriori_saveToFile.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
