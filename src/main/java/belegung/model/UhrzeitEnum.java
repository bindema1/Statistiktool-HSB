package belegung.model;

public enum UhrzeitEnum {

	NEUN, ELF, DREIZEHN, FÜNFZEHN, SIEBZEHN, NEUNZEHN;
	
	@Override
	public String toString() {
		return name().toUpperCase();
	}
	
}
