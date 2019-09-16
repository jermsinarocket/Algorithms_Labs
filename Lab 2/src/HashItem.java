import java.math.BigInteger;

public class HashItem {
	
	private BigInteger key;
	private String value;
	
	HashItem(BigInteger key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public BigInteger getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
}
