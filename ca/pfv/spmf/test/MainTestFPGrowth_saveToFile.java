
// provide the required file paths for the input and output files
// as values for string input and string output


package ca.pfv.spmf.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;

/**
 * Example of how to use FPGrowth from the source code and save
 * the resutls to a file.
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class MainTestFPGrowth_saveToFile {

	public static void main(String [] arg) throws FileNotFoundException, IOException{
		// the file paths
		String input = "C:/Users/SRISHTI/Documents/GitHub/Improved_Diverse_Frequent_Item_Set_Mining/ca/pfv/spmf/input/diverse_data/mushroom.txt";  // the database
		String output = "C:/Users/SRISHTI/Documents/GitHub/Improved_Diverse_Frequent_Item_Set_Mining/ca/pfv/spmf/output.txt";  // the path for saving the frequent itemsets found
		
		double minsup = 0.4; // means a minsup of 2 transaction (we used a relative support)

		// Applying the FPGROWTH algorithmMainTestFPGrowth.java
		AlgoFPGrowth algo = new AlgoFPGrowth();
		algo.runAlgorithm(input, output, minsup);
		algo.printStats();
	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestFPGrowth_saveToFile.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
