import java.util.Scanner;
import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Search {
	
	public static void main(String[] args) throws IOException{
		
		int data_size;
		String[] hashFunctions = {"Division Method", "Shift Folding", "Mid-Square Method"};
		Scanner sc = new Scanner(System.in);
		
		ArrayList<HashMap> hashMaps = new ArrayList<HashMap>();
		HashMap divHashMap = new HashMap();
		HashMap foldHashMap = new HashMap();
		HashMap midSquareHashMap = new HashMap();
		
		hashMaps.add(divHashMap);
		hashMaps.add(foldHashMap);
		hashMaps.add(midSquareHashMap);
		
		long successStartTime, successEndTime, successExecutionTime;
		long failedStartTime, failedEndTime, failedExecutionTime;
		
		
		System.out.print("\nEnter the Load Factor to insert into Hash Table (Load Factor <= 1): ");
		
		double loadFactor = sc.nextDouble();
		
		//Calculate Data Size based on Load Factor
		data_size = (int) Math.floor(HashMap.HASHTABLE_SIZE*loadFactor);
		
		Data data = new Data(data_size);
		
		//Get List of Added Keys (Searches with these keys will be successful)
		ArrayList<BigInteger>successKeys = new ArrayList<BigInteger>();
		
		for(int i = 0 ; i < hashMaps.size() ; i++) {
			if(i == 0) {
				data.generateData(hashMaps.get(i),(i+1),loadFactor);
				successKeys = (ArrayList)data.getAddedKeys().clone();
			}else{
				data.generateData(hashMaps.get(i), i+1, loadFactor, successKeys);
			}
			
		}
		
		
		System.out.println("\n=======================");
		System.out.println("TESTING WITH " + data_size + " DATA");
		System.out.println("========================");

		
		ArrayList<BigInteger>failedKeys = new ArrayList<BigInteger>();


		//Populate Unsuccessful Search array with keys that are not in Hash Table
		for(int i = 0; i < data_size; i++) {
		
		    boolean found = false;
			BigInteger testKey;
			
			do {
				
				testKey = data.generateUnsuccessfulKey();
				
				for (BigInteger element : successKeys) {
					if(testKey.intValue() == element.intValue()) {
						found = true;
						break;
					}
				 }
				
				
			}while(found == true);
			
			failedKeys.add(testKey);
			
		}
		
		int[] ttlSuccessfulComparisons = new int[3];
		long[] ttlSuccessfulExecutionTime = new long[3];
		int[] ttlFailedComparisons = new int[3];
		long[] ttlFailedExecutionTime = new long[3];	

	
		for(int i = 0; i < data_size; i++) {
			
			int displayCounter = 0;
			
			for(int j = 0; j < hashMaps.size(); j++) {
				
				successStartTime = System.nanoTime();	
				String successValue = hashMaps.get(j).get(successKeys.get(i),j+1);									
				successEndTime = System.nanoTime();
				successExecutionTime = successEndTime - successStartTime;
				
				ttlSuccessfulExecutionTime[j] += successExecutionTime;
				ttlSuccessfulComparisons[j] += HashMap.counter;
				
				if(displayCounter == 0) {
					System.out.println("Key: " + successKeys.get(i));
					System.out.println("\t" + successValue);
					displayCounter++;
				}
				
				failedStartTime = System.nanoTime();												
				String failedValue = hashMaps.get(0).get(failedKeys.get(i),j+1);									
				failedEndTime = System.nanoTime();
				failedExecutionTime = failedEndTime - failedStartTime;
				
				ttlFailedComparisons[j] += HashMap.counter;
				ttlFailedExecutionTime[j] += failedExecutionTime;
			}
														
		}
		
		for(int i = 0; i < hashMaps.size(); i++) {
			System.out.println("\n========================================" );
			System.out.println("Hashing Function Used: " + hashFunctions[i] );
			System.out.println("=========================================" );
			System.out.println("\nAverage Execution Time of Successful Searches : " + (ttlSuccessfulExecutionTime[i])/data_size);
			System.out.println("\nAverage Comparisons made for Successful Searches: " + (ttlSuccessfulComparisons[i])/data_size);
			System.out.println("\nAverage Execution Time of Unsuccessful Searches : " + (ttlFailedExecutionTime[i])/data_size);
			System.out.println("\nAverage Comparisons made for Unsuccessful Searches: " + (ttlFailedComparisons[i]/data_size));
		}
		
		
	}
	
}
