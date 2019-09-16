import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Data {
	
	// The number of key/value pairs that we want to insert into the hash table
	private int data_size; 
	
	//ISBN numbers are 13-digits long and will always start with a prefix of 978 or 979
	private final BigInteger upperLimit = new BigInteger("9799999999999"); // Upper Limit
	private final BigInteger lowerLimit = new BigInteger("9780000000000"); // Lower Limit
	private final BigInteger diff; 
	private final int maxNumBitLength;
	private FileReader fr;
	private BufferedReader br;
	private Random rand;
	private ArrayList<BigInteger> addedKeys = new ArrayList<BigInteger>();

	Data(int data_size) throws FileNotFoundException{
		this.data_size = data_size;
		fr = new FileReader("booksName.txt");
		br = new BufferedReader(fr);
		diff = upperLimit.subtract(lowerLimit);
		maxNumBitLength = upperLimit.bitLength();
		rand = new Random();
	}	
	
	public void generateData(HashMap hashMap,int hashFuncChoice,double loadFactor) throws IOException {
		
		int dataCounter = 0;
		BigInteger key;
		
		System.out.println("\n====================================");
		System.out.println("GENERATING DATA FOR DIVISION METHOD");
		System.out.println("=====================================");
		//Generate the amount of data according to dataSize;
		while(dataCounter < data_size) {
		    
			key = new BigInteger(maxNumBitLength, rand);
			
			if (key.compareTo(lowerLimit) < 0)
			      key = key.add(lowerLimit);
			if (key.compareTo(upperLimit) >= 0)
			      key = key.mod(diff).add(lowerLimit);
		    	
			System.out.println(key);
			String name = br.readLine();
			System.out.println("\t : " + name);
			
			// Add the data into the hash table
			if(hashMap.addItem(key, name,  hashFuncChoice) == 1) {	
				//List of all added Keys
				addedKeys.add(key);
				dataCounter++;
			}
		}

		fr.close();
		System.out.println("\nTotal <Key,Value> Pairs Generated (Load Factor = " + loadFactor +"): " + dataCounter);
	}
	
	public void generateData(HashMap hashMap,int hashFuncChoice,double loadFactor,ArrayList<BigInteger> keysList) throws IOException {
		
		
		int dataCounter = 0;
		BigInteger key;
		fr =  new FileReader("booksName.txt");
		br = new BufferedReader(fr);
		
		System.out.println("\n====================================");
		if(hashFuncChoice ==2)
			System.out.println("GENERATING DATA FOR SHIFT FOLDING");
		else
			System.out.println("GENERATING DATA FOR MID-SQUARE METHOD");
		System.out.println("=====================================");
		
		//Generate the amount of data according to dataSize;
		while(dataCounter < data_size) {
		    
		    key = keysList.get(dataCounter);
		    
		    System.out.println(key);
			String name = br.readLine();
			System.out.println("\t : " + name);
			
			// Add the data into the hash table
			if(hashMap.addItem(key, name,  hashFuncChoice) == 1) {	
				//List of all added Keys
				addedKeys.add(key);
				dataCounter++;
				
			}
		}
		
		fr.close();
		System.out.println("\nTotal <Key,Value> Pairs Generated (Load Factor = " + loadFactor +"): " + dataCounter);
	}


	public BigInteger generateUnsuccessfulKey() {
		BigInteger key;
		
		key = new BigInteger(maxNumBitLength, rand);
		
		if (key.compareTo(lowerLimit) < 0)
		      key = key.add(lowerLimit);
		if (key.compareTo(upperLimit) >= 0)
		      key = key.mod(diff).add(lowerLimit);
		
		return key;
	}
	
	public ArrayList<BigInteger> getAddedKeys() {
		return addedKeys;
	}
	
}
