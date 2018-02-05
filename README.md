# Improved_Diverse_Frequent_Item_Set_Mining

## Synopsis
While exploring some data mining projects and research papers, my interest was ignited by research paper on diverse frequent item set mining. After going through this paper, I decided to implement it and tried proposing my own algorithm. The link for the paper that I referred is https://www.cse.iitb.ac.in/~comad/2011/images/papers/research/5_bw_comad2011_submission_43_discovering%20diverse-frequent%20patterns%20in%20a%20transacational%20database.pdf. 
I have proposed a new algorithm that improves the time complexity of mining diverse frequent item sets from large sized datasets by simultaneously mining diverse item sets and frequent item sets and thus reducing the search space for mining.

## Code Description

To implement my algorithm, I have modified the source code of FP-Growth algorithm present in SPMF library. SPMF is an open-source data mining library written in Java, specialized in pattern mining. I have used the spmf.zip file source code of SPMF Library provided at the site: http://www.philippe-fournier-viger.com/spmf/index.php?link=download.php .
All the instructions for installing and running the source code can be found at:http://www.philippe-fournier-viger.com/spmf/how_to_install.txt . Moreover, information about the source code organization can be found by reading the developer's guide: http://www.philippe-fournier-viger.com/spmf/developers.php

## Motivation

I am a Data Mining, Machine Learning and Natural Language Processing enthusiast, who likes to explore these domains. One such endeavour has been this project, where I have tried to improve the time complexity of diverse frequent item set mining.

## Installation and Running the Code

*Requirements
To run this project, the following is required:
1) JDK
2) IDE- Eclipse, Netbeans, etc
3) Dataset - The above codes can be run on a dataset similar to Mushroom dataset 
4) SPMF Library- 

*Modified Folders and Files Description:
I have added two folders - fpgrowth_diverse and apriori_diverse (with file path: Improved_Diverse_Frequent_Item_Set_Mining/ca/pfv/spmf/algorithms/frequentpatterns/)

The main file of my project is : OurDiverse_Main which is present in the 'test' folder (with file path - Improved_Diverse_Frequent_Item_Set_Mining/ca/pfv/spmf/test)

This main file calls the two files :
1. AlgoFPGrowthDiverse.java (with file path: ca/pfv/spmf/algorithms/frequentpatterns/fpgrowth_diverse/) 
2. AlgoAprioriDiverse.java (with file path: ca/pfv/spmf/algorithms/frequentpatterns/apriori_diverse/)
These two files require the support of files present in SPMF library and can be included as jar files in the library or can be run as separate class files. 

The input data is present in the 2 text files- mushroom.txt and hierarchy.txt which can be found in  Improved_Diverse_Frequent_Item_Set_Mining/ca/pfv/spmf/input/diverse_data

The output data is saved in the 2 text files- output_DiverseApriori.txt and output_DiverseFpgrowth.txt which can be found in  Improved_Diverse_Frequent_Item_Set_Mining/ca/pfv/spmf/output

## Contributors

Srishti Grover
