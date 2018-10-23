package belegung.model;

public enum StockwerkEnum {

	EG, ZG1, ZG2, LL, WÄDI, TEST;
	
	@Override
	public String toString() {
		return name().toUpperCase();
	}
}
