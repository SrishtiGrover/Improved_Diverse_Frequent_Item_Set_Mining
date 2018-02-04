# Improved_Diverse_Frequent_Item_Set_Mining

## Synopsis
 I have proposed a new algorithm that improves the time complexity of mining diverse frequent item sets from large sized datasets by simultaneously mining diverse item sets and frequent item sets and thus reducing the search space for mining.

## Code Description

To implement my algorithm, I have modified the source code of FP-Growth algorithm present in SPMF library. SPMF is an open-source data mining library written in Java, specialized in pattern mining. I have used the spmf.zip file source code of SPMF Library provided at the site: http://www.philippe-fournier-viger.com/spmf/index.php?link=download.php .
All the instructions for installing and running the source code can be found at:http://www.philippe-fournier-viger.com/spmf/how_to_install.txt . Moreover, information about the source code organization can be found by reading the developer's guide: http://www.philippe-fournier-viger.com/spmf/developers.php

## Motivation

I am a Data Mining, Machine Learning and Natural Language Processing enthusiast, who likes to explore these domains. One such endeavour has been this project, where I have tried to improve the time complexity of diverse frequent item set mining.

## Installation and Running the Code

#Requirements
To run this project, the following is required:
1) JDK
2) IDE- Eclipse, Netbeans, etc
3) Dataset - The above codes can be run on a dataset similar to Mushroom dataset 
4) SPMF Library- 

#Modified Folders and Files Description:
I have two folders- fpgrowth_diverse and apriori_diverse (with file path: ca/pfv/spmf/algorithms/frequentpatterns/)
The main files of my project are : 
1. AlgoFPGrowthDiverse.java (with file path: ca/pfv/spmf/algorithms/frequentpatterns/fpgrowth_diverse/) 
2. AlgoAprioriDiverse.java (with file path: ca/pfv/spmf/algorithms/frequentpatterns/apriori_diverse/)

These two files require the support of files present in SPMF library and can be included as jar files in the library or can be run as separate class files. 

## Contributors

Srishti Grover
