package utilities;


public class Operator {
	
	private int code; 
	private double marketShare;
	private String name ;
	
	
	public Operator(int code, String name, double marketShare) {
		super();
		this.code = code;
		this.marketShare = marketShare;
		this.name = name;	
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMarketShare() {
		return marketShare;
	}
	
}
