package ca.pfv.spmf.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


import ca.pfv.spmf.algorithms.frequentpatterns.apriori_diverse.AlgoAprioriDiverse;

import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth_diverse.AlgoFPGrowthDiverse;

/**
 * Example of how to use APRIORI algorithm from the source code.
 * @author Philippe Fournier-Viger (Copyright 2008)
 *
 */


public class OurDiverse_Main {

	public static void main(String [] arg) throws IOException
	{
        ///////////   **********     APRIORI *********** ///////////
		
		String input = "ca/pfv/spmf/input/diverse_data/mushroom.txt";
		String output = "ca/pfv/spmf/output/output_DiverseApriori.txt";  // the path for saving the frequent itemsets found
		
		double minsup = 0.4; // means a minsup of 2 transaction (we used a relative support)
		double minDiv= 0.8;
		int height_of_hierarchy=3;
		int total_branches=6;
		
		
		
		for (int i=1;i<=10;i++)
		{   
			long avg=0;
			System.out.println("minDiv="+minDiv);
			for(int j=1;j<=50;j++)
			{
				// Applying the Apriori diverse algorithm
				AlgoAprioriDiverse apriori = new AlgoAprioriDiverse();
				apriori.runAlgorithm(minsup, minDiv, height_of_hierarchy,input, output,total_branches);
				
				avg+=apriori.printStats();
					
			}
			
			System.out.println(" Total time ~ " + avg/(50.00) + " ms");
			minDiv+=0.1;
	        
		}
		
		
       	
		/*	           
		///////// ***********     FPGROWTH ******** //////////
		
		 
		String input = "ca/pfv/spmf/input/diverse_data/mushroom.txt"";
		String output = "ca/pfv/spmf/output/output_DiverseFpgrowth.txt"";  // the path for saving the frequent itemsets found
		
		double minsup = 0.4; // means a minsup of 2 transaction (we used a relative support)
		double minDiv= 0.95;
		int height_of_hierarchy=3;
		int total_branches=6;
		
		// Applying the fpgrowth algorithm
		AlgoFPGrowthDiverse fp = new AlgoFPGrowthDiverse();
		fp.runAlgorithm(minsup, minDiv, height_of_hierarchy,input, output,total_branches);
		fp.printStats();
		
		
		//////// ************** FPGROWTH OVER ********* ///////
		*/
		
	}
	
	
}
