import java.math.BigInteger;
import java.util.stream.IntStream;

public class HashMap {

	public final static int HASHTABLE_SIZE = 1009; //Table Size should be a prime number			
	private final static BigInteger hashtable_size_big = BigInteger.valueOf(HASHTABLE_SIZE);
	private final static BigInteger commonDenom = new BigInteger("10");
	public static int counter;	// Number of Comparisons counter
	
	HashItem[] hash_table;
	
	HashMap(){
		hash_table = new HashItem[HASHTABLE_SIZE];
		
		for(int i = 0; i < HASHTABLE_SIZE; i++) {
			hash_table[i] = null; //Set all hash table elements to null
		}
		
	}
	
	// Using different hash functions to calculate index for the key
	public int calculateHashFunction(BigInteger key, int hashFuncChoice,int choice) {
		
		int hashIndex = 0;
		
		switch(hashFuncChoice) {
		
		    //Division Method
			case 1:
				
				hashIndex = (key.mod(hashtable_size_big)).intValue();
				
				break;
				
			//Folding Method
			case 2:
				
				String keyPiecesStr = key.toString();
				
				//Split Key Pieces into 4 Equal Size Pieces and 1 Unequal Size Piece (Last Piece)  
				String[] keyPiecesStrArray = keyPiecesStr.split("(?<=\\G.{3})");

				int[] keyPiecesIntArray = new int[keyPiecesStrArray.length];
				
				for (int i=0; i < (keyPiecesStrArray.length); i++) {
					keyPiecesIntArray[i] = Integer.parseInt(keyPiecesStrArray[i]);
				}
				
				
				
				int keyPiecesSum = IntStream.of(keyPiecesIntArray).sum();
				if(choice == 0) {
					System.out.println("Key Pieces:" + java.util.Arrays.toString(keyPiecesStrArray)); 
					System.out.println("Sum of Key Pieces: " + keyPiecesSum);
				}
				
				
				hashIndex = keyPiecesSum % HASHTABLE_SIZE;

				break;
				
			//Mid-Square Method
			case 3:
				
				BigInteger seed = key;
				
				seed = seed.pow(2);
				BigInteger dividend = new BigInteger("10000000000");
				BigInteger modulo = new BigInteger("1000");
				
				//Extract a seed ( Middle 3 Digits)
				seed = seed.divide(dividend);
				seed = seed.mod(modulo);
				
				BigInteger loopDividend = new BigInteger("100");
				BigInteger loopModulo = new BigInteger("1000");
				
				//Repeat the process for N times. We set it as 3 for now.
				for(int i = 0;  i < 2 ; i ++) {
					seed = seed.pow(2);
					
					seed = seed.divide(loopDividend);
					seed = seed.mod(loopModulo);
					
				}
				
				hashIndex = seed.intValue();
				
				break;
				
			default:
				hashIndex = (key.mod(hashtable_size_big)).intValue();
				break;
		}
			
	
		if(choice == 0)
			System.out.println("Initial Hash Index: " + hashIndex);
		
		return hashIndex;
	}
	
	public int addItem(BigInteger key, String value, int hashFuncChoice) {										
		
		// Calculate a Hash Index for the key
		
		int hashIndex = calculateHashFunction(key, hashFuncChoice,0);
		
		//If the slot is not empty
		while (hash_table[hashIndex] != null) { 
			// If the item in the slot is not equal to the key we want to insert
			if (hash_table[hashIndex].getKey() != key) {  
				//rehash by moving to the next slot. Linear Probing adds item sequentially and thus we have to +1 to the key
				//hashIndex will increase by from i = 1 to HASHTABLE_SIZE - 1
				hashIndex = (hashIndex + 1) % HASHTABLE_SIZE;
			}
			else { // The item in the slot is same as the key we want to insert, we do not have to add the key again 																				
				return -1;	
			}
		}
		
		System.out.println("Final Hash Index: " + hashIndex + "\n");
		
	    //Slot is empty
		//Insert the key/value pair into to the slot
		hash_table[hashIndex] = new HashItem(key, value);	
		return 1;
	} 	
	
	//Search for a value associated with the given key
	public String get(BigInteger searchKey, int hashFuncChoice) {
		
		// Calculate a Hash Index for the key
		int hashIndex = calculateHashFunction(searchKey, hashFuncChoice,1);		
		
		//Start location of the search
		int startLocation = hashIndex;
		
		//Counter to track the total number of Comparison
		counter = 1;
		
		//If the slot is not empty
		while (hash_table[hashIndex] != null) {		
			// If the item in the slot is equal to the search key
			if ((hash_table[hashIndex].getKey()).intValue() == searchKey.intValue()) {		
				//Return the value associated with the key
				return hash_table[hashIndex].getValue();								
			}
			// If the item in the slot is not equal to the search key
			else {
				//Rehash by moving the search to the next slot. 
				// Linear Probing searches for item sequentially and thus we have to +1 to the key
				hashIndex = (hashIndex + 1) % HASHTABLE_SIZE;							
				//Increase the Comparison Counter by 1
				counter++;		
				
				// If the search is unsuccessful (Hash Index becomes the same as the index that we started our search at)
				if (hashIndex == startLocation) {									
					break;															
				}
			}
		}
		return null;															
	}
	
	public HashItem[] getHashTable() {
		return hash_table;
	}
	
}
