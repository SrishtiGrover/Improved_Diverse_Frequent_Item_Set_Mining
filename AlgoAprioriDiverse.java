package ca.pfv.spmf.algorithms.frequentpatterns.apriori_diverse;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import ca.pfv.spmf.algorithms.ArraysAlgos;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;
import ca.pfv.spmf.tools.MemoryLogger;

/**
 * This is an optimized implementation of the Apriori algorithm that uses binary search to
 * check if subsets of a candidate are frequent and other optimizations.
 *  <br/><br/>
 *  
 * The apriori algorithm is described  in :
 * <br/><br/>
 *  
 * Agrawal R, Srikant R. "Fast Algorithms for Mining Association Rules", VLDB.
 * Sep 12-15 1994, Chile, 487-99,
 * <br/><br/>
 *  
 * The Apriori algorithm finds all the frequents itemsets and their support in a
 * transaction database provided by the user.
 * <br/><br/>
 *  
 * This is an optimized version that saves the result to a file
 * or keep it into memory if no output path is provided
 * by the user to the runAlgorithm() method.
 * 
 * @see Itemset
 * @see Itemsets
 * @author Philippe Fournier-Viger
 */



public class AlgoAprioriDiverse {

	// the current level k in the breadth-first search
	protected int k; 

	// variables for statistics
	protected int totalCandidateCount = 0; // number of candidate generated during last execution
	protected long startTimestamp; // start time of last execution
	protected long endTimestamp; // end time of last execution
	private int itemsetCount;  // itemset found during last execution
	private int databaseSize;
	
	// the minimum support set by the user
	private int minsupRelative;
	
	// A memory representation of the database.
	// Each position in the list represents a transaction
	private List<int[]> database = null;
	
	// The  patterns that are found 
	// (if the user want to keep them into memory)
	protected Itemsets patterns = null;

	// object to write the output file (if the user wants to write to a file)
	BufferedWriter writer = null; 
	
	/**
	 * Default constructor
	 */
	public AlgoAprioriDiverse() {
		
	}

	/**
	 * Method to run the algorithm
	 * @param minsup  a minimum support value as a percentage
	 * @param input  the path of an input file
	 * @param output the path of an input if the result should be saved to a file. If null,
	 *               the result will be kept into memory and this
	 *               method will return the result.
	 * @throws IOException exception if error while writting or reading the input/output file
	 */
	public Itemsets runAlgorithm(double minsup,double minDiv,int height_of_hierarchy, String input, String output,int tot_branches) throws IOException 
	{
		
		// if the user want to keep the result into memory
		if(output == null){
			writer = null;
			patterns =  new Itemsets("FREQUENT ITEMSETS");
	    }else{ // if the user want to save the result to a file
			patterns = null;
			writer = new BufferedWriter(new FileWriter(output)); 
		}
		
		// record the start time
		startTimestamp = System.currentTimeMillis();
		
		// set the number of itemset found to zero
		itemsetCount = 0;
		// set the number of candidate found to zero
		totalCandidateCount = 0;
		// reset the utility for checking the memory usage
		MemoryLogger.getInstance().reset();
		Map<String,String> encode = Encode("/workspace1/fp growth/ca/pfv/spmf/algorithms/frequentpatterns/apriori_diverse/hierarchy.txt");
		
		// READ THE INPUT FILE
		// variable to count the number of transactions
		databaseSize = 0;
		// Map to count the support of each item
		// Key: item  Value : support
		Map<Integer, Integer> mapItemCount = new HashMap<Integer, Integer>(); // to count the support of each item
		
		database = new ArrayList<int[]>(); // the database in memory (intially empty)
		
		// scan the database to load it into memory and count the support of each single item at the same time
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line;
		// for each line (transactions) until the end of the file
		while (((line = reader.readLine()) != null)) { 
			// if the line is  a comment, is  empty or is a
			// kind of metadata
			if (line.isEmpty() == true ||
					line.charAt(0) == '#' || line.charAt(0) == '%'
							|| line.charAt(0) == '@') {
				continue;
			}
			// split the line according to spaces
			String[] lineSplited = line.split(" ");
			
			// create an array of int to store the items in this transaction
			int transaction[] = new int[lineSplited.length];
			
			// for each item in this line (transaction)
			for (int i=0; i< lineSplited.length; i++) { 
				// transform this item from a string to an integer
				Integer item = Integer.parseInt(lineSplited[i]);
				// store the item in the memory representation of the database
				transaction[i] = item;
				// increase the support count
				Integer count = mapItemCount.get(item);
				if (count == null) {
					mapItemCount.put(item, 1);
				} else {
					mapItemCount.put(item, ++count);
				}
			}
			// add the transaction to the database
			database.add(transaction);
			// increase the number of transaction
			databaseSize++;
		}
		// close the input file
		reader.close();
		
		// convert the minimum support as a percentage to a 
		// relative minimum support as an integer
		this.minsupRelative = (int) Math.ceil(minsup * databaseSize);
		
		// we start looking for itemset of size 1
		k = 1;
		
		// We add all frequent items to the set of candidate of size 1
		List<Integer> frequent1 = new ArrayList<Integer>();
		for(Entry<Integer, Integer> entry : mapItemCount.entrySet()){
			if(entry.getValue() >= minsupRelative){
				frequent1.add(entry.getKey());
				//saveItemsetToFile(entry.getKey(), entry.getValue());
			}
		}
		mapItemCount = null;
		
		// We sort the list of candidates by lexical order
		// (Apriori need to use a total order otherwise it does not work)
		Collections.sort(frequent1, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});
		
		
		// If no frequent item, the algorithm stops!
		if(frequent1.size() == 0){
			// close the output file if we used it
			if(writer != null){
				writer.close();
			}
			return patterns; 
		}
		
		// add the frequent items of size 1 to the total number of candidates
		totalCandidateCount += frequent1.size();
		
		
		
		
		float plf[] = new float[10];  //  ///// ***************** change if concept heirarchy is higher than 10
		int lev;
		for (lev = height_of_hierarchy-1; lev >=1 ; lev--) 
		{
			plf[lev] = plf(lev,height_of_hierarchy);	
		}		
		
		
		
		
		
		// Now we will perform a loop to find all frequent itemsets of size > 1
		// starting from size k = 2.
		// The loop will stop when no candidates can be generated.
		List<Itemset> level = null;
		k = 2;
		do{
			// we check the memory usage
			MemoryLogger.getInstance().checkMemory();
			
			// Generate candidates of size K
			List<Itemset> candidatesK;
			
			// if we are at level k=2, we use an optimization to generate candidates
			if(k ==2)
			{
				candidatesK = generateCandidate2(frequent1);
			}
			else
			{
			// otherwise we use the regular way to generate candidates
				candidatesK = generateCandidateSizeK(level);
			}
			
			// we add the number of candidates generated to the total
			totalCandidateCount += candidatesK.size();

			// We scan the database one time to calculate the support
			// of each candidates and keep those with higher support.
			 // We also calculate the diverse rank of the candidate        ###################################################
			
			// For each transaction:
			for(int[] transaction: database)
			{
				// NEW OPTIMIZATION 2013: Skip transactions shorter than k!
				if(transaction.length < k) 
				{
//					System.out.println("test");
					continue;
				}
				// END OF NEW OPTIMIZATION
				
				// for each candidate:
	 loopCand:	for(Itemset candidate : candidatesK)
	            {
		 			// a variable that will be use to check if 
		 			// all items of candidate are in this transaction
					int pos = 0;
					// for each item in this transaction
					for(int item: transaction)
					{
						// if the item correspond to the current item of candidate
						if(item == candidate.itemset[pos])
						{
							// we will try to find the next item of candidate next
							pos++;
							// if we found all items of candidate in this transaction
							if(pos == candidate.itemset.length)
							{
								// we increase the support of this candidate
								candidate.support++;
								continue loopCand;
							}
					    // Because of lexical order, we don't need to 
						// continue scanning the transaction if the current item
						// is larger than the one that we search  in candidate.
						}
						else if(item > candidate.itemset[pos])
						{
							continue loopCand;
						}
					}
				}
			}
			
				//***************** ADD ********************
				
			    // Calculate the divRank
				for(Itemset candidate : candidatesK)
				{	String [] encodedcandidate= new String[candidate.itemset.length];
							    
					for (int i = 0; i < candidate.itemset.length; i++) 
					{
						encodedcandidate[i]=encode.get(Integer.toString(candidate.itemset[i]));
					}
					ArrayList<Double> DRarr=FindDivRank(encodedcandidate,plf,height_of_hierarchy,minDiv,tot_branches); 
					candidate.DivRank=DRarr.get(0);
					candidate.maxDivRank=DRarr.get(1);
					
				}
				
							
							
			// We build the level k+1 with all the candidates that have
			// a support higher than the minsup threshold.
			level = new ArrayList<Itemset>();
			for (Itemset candidate : candidatesK) 
			{
				
		//////// REMOVE ***************************************************
			       String [] encodedcandidate= new String[candidate.itemset.length];   
								    
				   for (int i = 0; i < candidate.itemset.length; i++) 
				   {
					encodedcandidate[i]=encode.get(Integer.toString(candidate.itemset[i]));
				   }
									
				///////Remove **********************************************8
				  
			
				// if the support is > minsup
				if (candidate.getAbsoluteSupport() >= minsupRelative) 
				{
					if(candidate.getDivRank()>=minDiv)
					{				
						
						// the itemset is frequent so save it into results
						saveItemset(candidate);	
						saveItemsetEncoded(encodedcandidate) ;      //  $$$$$$$$$$$$$$$$$$$$$$$$  remove
											
						
					}	
					
					// if the DR(Z) > minDiv 
					if( candidate.getmaxDivRank() >= minDiv) 
					   {    // add the candidate
							level.add(candidate);
							
					   }
					else
					{
						System.out.println("PRUNED !!!!!!!!!!!!");
					}
					
					
				}				
			}
			
			// we will generate larger itemsets next.
			k++;
		}while(level.isEmpty() == false);

		// record end time
		endTimestamp = System.currentTimeMillis();
		// check the memory usage
		MemoryLogger.getInstance().checkMemory();

		// close the output file if the result was saved to a file.
		if(writer != null){
			writer.close();
		}
		
		return patterns;
	}

	

	/**
	 * Return the number of transactions in the last database read by the algorithm.
	 * @return the number of transactions.
	 */
	public int getDatabaseSize() {
		return databaseSize;
	}

	/**
	 * This method generates candidates itemsets of size 2 based on
	 * itemsets of size 1.
	 * @param frequent1  the list of frequent itemsets of size 1.
	 * @return a List of Itemset that are the candidates of size 2.
	 */                                                                                                                                                                                                                                                                                                                                                                                            
	private List<Itemset> generateCandidate2(List<Integer> frequent1) {
		List<Itemset> candidates = new ArrayList<Itemset>();
		
		// For each itemset I1 and I2 of level k-1
		for (int i = 0; i < frequent1.size(); i++) {
			Integer item1 = frequent1.get(i);
			for (int j = i + 1; j < frequent1.size(); j++) {
				Integer item2 = frequent1.get(j);

				// Create a new candidate by combining itemset1 and itemset2
				candidates.add(new Itemset(new int []{item1, item2}));
			}
		}
		return candidates;
	}

	/**
	 * Method to generate itemsets of size k from frequent itemsets of size K-1.
	 * @param levelK_1  frequent itemsets of size k-1
	 * @return itemsets of size k
	 */
	protected List<Itemset> generateCandidateSizeK(List<Itemset> levelK_1) {
		// create a variable to store candidates
		List<Itemset> candidates = new ArrayList<Itemset>();

		// For each itemset I1 and I2 of level k-1
		loop1: for (int i = 0; i < levelK_1.size(); i++) {
			int[] itemset1 = levelK_1.get(i).itemset;
			loop2: for (int j = i + 1; j < levelK_1.size(); j++) {
				int[] itemset2 = levelK_1.get(j).itemset;

				// we compare items of itemset1 and itemset2.
				// If they have all the same k-1 items and the last item of
				// itemset1 is smaller than
				// the last item of itemset2, we will combine them to generate a
				// candidate
				for (int k = 0; k < itemset1.length; k++) {
					// if they are the last items
					if (k == itemset1.length - 1) {
						// the one from itemset1 should be smaller (lexical
						// order)
						// and different from the one of itemset2
						if (itemset1[k] >= itemset2[k]) {
							continue loop1;
						}
					}
					// if they are not the last items, and
					else if (itemset1[k] < itemset2[k]) {
						continue loop2; // we continue searching
					} else if (itemset1[k] > itemset2[k]) {
						continue loop1; // we stop searching: because of lexical
										// order
					}
				}

				// Create a new candidate by combining itemset1 and itemset2
				int newItemset[] = new int[itemset1.length+1];
				System.arraycopy(itemset1, 0, newItemset, 0, itemset1.length);
				newItemset[itemset1.length] = itemset2[itemset2.length -1];

				// The candidate is tested to see if its subsets of size k-1 are
				// included in
				// level k-1 (they are frequent).
				if (allSubsetsOfSizeK_1AreFrequent(newItemset, levelK_1)) {
					candidates.add(new Itemset(newItemset));
				}
			}
		}
		return candidates; // return the set of candidates
	}

	/**
	 * Method to check if all the subsets of size k-1 of a candidate of size k are freuqnet
	 * @param candidate a candidate itemset of size k
	 * @param levelK_1  the frequent itemsets of size k-1
	 * @return true if all the subsets are frequet
	 */
	protected boolean allSubsetsOfSizeK_1AreFrequent(int[] candidate, List<Itemset> levelK_1) {
		// generate all subsets by always each item from the candidate, one by one
		for(int posRemoved=0; posRemoved< candidate.length; posRemoved++){

			// perform a binary search to check if  the subset appears in  level k-1.
	        int first = 0;
	        int last = levelK_1.size() - 1;
	       
	        // variable to remember if we found the subset
	        boolean found = false;
	        // the binary search
	        while( first <= last )
	        {
	        	int middle = ( first + last ) >>1 ; // >>1 means to divide by 2

	        	int comparison = ArraysAlgos.sameAs(levelK_1.get(middle).getItems(), candidate, posRemoved);
	            if(comparison < 0 ){
	            	first = middle + 1;  //  the itemset compared is larger than the subset according to the lexical order
	            }
	            else if(comparison  > 0 ){
	            	last = middle - 1; //  the itemset compared is smaller than the subset  is smaller according to the lexical order
	            }
	            else{
	            	found = true; //  we have found it so we stop
	                break;
	            }
	        }

			if(found == false){  // if we did not find it, that means that candidate is not a frequent itemset because
				// at least one of its subsets does not appear in level k-1.
				return false;
			}
		}
		return true;
	}

	 void saveItemset(Itemset itemset) throws IOException {
		itemsetCount++;
		
		// if the result should be saved to a file                  // CHNAGE *************************************
		if(writer != null){
			//writer.write(itemset.toString() + " #SUP: "
			//		+ itemset.getAbsoluteSupport() + " #DR: "+ itemset.getDivRank());
			//writer.newLine(); 
		}// otherwise the result is kept into memory
		else{
			patterns.addItemset(itemset, itemset.size());
		}
	}
	
	 
	 
	 void saveItemsetEncoded(String [] encitemset) throws IOException {
			//itemsetCount++;
			
			// if the result should be saved to a file
			if(writer != null){
				List<String> list =Arrays.asList(encitemset);
				
				writer.write(String.join(" ", list));
				writer.newLine();
			}// otherwise the result is kept into memory
			else{
				//patterns.addItemset(itemset, itemset.size());
			}
		}
		
	 
	 
	 
	 
	 void saveItemsetToFile(Integer item, Integer support) throws IOException {
		itemsetCount++;
		
		// if the result should be saved to a file
		if(writer != null){
			writer.write(item + " #SUP: " + support);
			writer.newLine();
		}// otherwise the result is kept into memory
		else{
			Itemset itemset = new Itemset(item);
			itemset.setAbsoluteSupport(support);
			patterns.addItemset(itemset, 1);
		}
	}
	 
	 
	 
	 
	 public static Map<String,String> Encode(String filename)
	 {

			Map<String,String> encode = new HashMap<String,String>();
			
			try {
				String line;
				BufferedReader br = new BufferedReader(new FileReader(filename));
				int maxlen = -1;

				while ((line = br.readLine()) != null) {
					
					String [] arr = line.split(":");
					String [] children = arr[1].split(",");
					String parent = arr[0];
					
					String pcode = encode.get(parent);
					if(pcode==null){
						encode.put(parent, "1");
						pcode = "1";
					}
					
					int i;
					int l = children.length;
					
					for(i=0;i<l;i++){
						String mycode = pcode + Integer.toString(i+1);
						encode.put(children[i],mycode);
						if(maxlen<mycode.length())
							maxlen = mycode.length();
					}
				}
				
				for (Map.Entry<String, String> entry : encode.entrySet()){
				    String key = entry.getKey();
				    String val =entry.getValue();
				    while(val.length()<maxlen){
				    	val = val + "*";
				    }
				    encode.put(key, val);
				}
				
				br.close();

			} catch (IOException e) {
				   System.out.println("Working Directory = " +  System.getProperty("user.dir"));
				e.printStackTrace();
			} 
			
			return encode;
			
		}
	 
	 
	 static ArrayList<String> GFPGen(String [] patt, int l,int h)
	 {
			ArrayList<String> gfp = new ArrayList<String>();
			for(String item: patt)
			{
				int i;
				String parent="";
				for(i=0;i<=l;i++)
				{
					parent+=item.charAt(i);
				}
				for(i=l+1;i<=h;i++)
				{
					parent+='*';
				}
				if(!gfp.contains(parent))
				{
					gfp.add(parent);
				}
			}
			return gfp;
		}

	  static float plf(int level, int height) 
	   {
			float div = (height-1)*height;
			float above = height-level;
			float second = 2*above;
			float plf = second/div;
			return plf;
		}
		
		private ArrayList<Double> FindDivRank(String[] efp,float[] plf, int h,double minDiv,int tot_branches)
		{
			
			//System.out.println("new itemset's DR calculation started");
	        //Scanner s= new Scanner(System.in);	
			//String str=s.nextLine();
			
			ArrayList<Double> Ranksarr=new ArrayList<Double>();  // first: DR ,second: MaxDR of superset
			double dr = 0;
			double maxdr=0;
			int cnt_branches=num_branches(efp);
			int adders=tot_branches-cnt_branches;
			
			int l = h-1;
			ArrayList<String> gfp =new ArrayList<String>();
			
			// to calculate  dr
			while(true)
			{
				    //System.out.println("inter dr="+ dr);     
				    //System.out.println("inter maxdr="+ maxdr);  

				
				   //   ******************  OPTIMISATIONS TO DO: 1) GFPgen array 2) maxdr extra
				    if(l==0)      
				    {
				    	break;
				    }
				
					gfp = GFPGen(efp,l,h);
					//System.out.println("gfp.size= "+gfp.size());            
					
					float mf,maxmf;
					float num = gfp.size() - 1;
					float den = GFPGen(efp,l+1,h).size() - 1;
				
					if(gfp.size()>1)
					{
						mf = num/den;
						dr = dr + (plf[l] * mf);	
					}
           		    maxmf=(num+adders)/(den+adders);
					maxdr=maxdr + (plf[l]*maxmf);
					
					
					l--;
			}
			Ranksarr.add(dr);
			Ranksarr.add(maxdr);
			return Ranksarr;
			
		}
		
		private int num_branches(String[] arr) 
		{
			int num_branches=0;
			String temp=new String();
			
			for (String item : arr)
			{
				temp=temp+ item.charAt(1);
			}
			
			num_branches=(int) temp.chars().distinct().count();
			return num_branches;
		}
	 
	 
	
	/**
	 * Print statistics about the algorithm execution to System.out.
	 */
	public long printStats() 
	{
		//System.out.println("=============  APRIORI - STATS =============");
		//System.out.println(" Candidates count : " + totalCandidateCount);
		//System.out.println(" The algorithm stopped at size " + (k - 1)+ ", because there is no candidate");
		//System.out.println(" Frequent itemsets count : " + itemsetCount);
		//double currentMemory = (Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory()) / 1024d / 1024d;
		        //System.out.println(" memory usage : "+currentMemory + " mb");
		
		        //System.out.println(" Total time ~ " + (endTimestamp - startTimestamp) + " ms");
		//System.out.println("===================================================");
		
		return (endTimestamp - startTimestamp);
	}
}
