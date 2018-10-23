package belegung.model;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man den SektorB in
 * die Tabelle 'SektorB' der Datenbank schreiben kann.
 * 
 * @author Marvin Bindemann
 */
public class SektorB {

	int sektorB_ID;
	int anzahlPersonen;
	UhrzeitEnum uhrzeit;
	Stockwerk stockwerk;
	
	// Für Hibernate
	public SektorB() {
		
	}
	
	public SektorB(int anzahlPersonen, UhrzeitEnum uhrzeit, Stockwerk stockwerk) {
		this.anzahlPersonen = anzahlPersonen;
		this.uhrzeit = uhrzeit;
		this.stockwerk = stockwerk;
	}

	public int getSektorB_ID() {
		return sektorB_ID;
	}

	public int getAnzahlPersonen() {
		return anzahlPersonen;
	}

	public UhrzeitEnum getUhrzeit() {
		return uhrzeit;
	}

	public Stockwerk getStockwerk() {
		return stockwerk;
	}

	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setSektorB_ID(int sektorB_ID) {
		this.sektorB_ID = sektorB_ID;
	}

	public void setAnzahlPersonen(int anzahlPersonen) {
		this.anzahlPersonen = anzahlPersonen;
	}

	public void setUhrzeit(UhrzeitEnum uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	public void setStockwerk(Stockwerk stockwerk) {
		this.stockwerk = stockwerk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anzahlPersonen;
		result = prime * result + sektorB_ID;
		result = prime * result + ((stockwerk == null) ? 0 : stockwerk.hashCode());
		result = prime * result + ((uhrzeit == null) ? 0 : uhrzeit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SektorB other = (SektorB) obj;
		if (anzahlPersonen != other.anzahlPersonen)
			return false;
		if (sektorB_ID != other.sektorB_ID)
			return false;
		if (stockwerk == null) {
			if (other.stockwerk != null)
				return false;
		} else if (!stockwerk.equals(other.stockwerk))
			return false;
		if (uhrzeit != other.uhrzeit)
			return false;
		return true;
	}
	
}
