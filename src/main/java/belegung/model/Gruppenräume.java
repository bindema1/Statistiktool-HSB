package belegung.model;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man die Gruppenräume in
 * die Tabelle 'Gruppenräume' der Datenbank schreiben kann.
 * 
 * @author Marvin Bindemann
 */
public class Gruppenräume {

	int gruppenräume_ID;
	int anzahlPersonen;
	int anzahlRäume;
	UhrzeitEnum uhrzeit;
	Stockwerk stockwerk;
	
	// Für Hibernate
	public Gruppenräume() {
		
	}
	
	public Gruppenräume(int anzahlPersonen, int anzahlRäume, UhrzeitEnum uhrzeit, Stockwerk stockwerk) {
		this.anzahlPersonen = anzahlPersonen;
		this.anzahlRäume = anzahlRäume;
		this.uhrzeit = uhrzeit;
		this.stockwerk = stockwerk;
	}

	public int getGruppenräume_ID() {
		return gruppenräume_ID;
	}
	
	public int getAnzahlRäume() {
		return anzahlRäume;
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
	public void setGruppenräume_ID(int gruppenräume_ID) {
		this.gruppenräume_ID = gruppenräume_ID;
	}

	public void setAnzahlPersonen(int anzahlPersonen) {
		this.anzahlPersonen = anzahlPersonen;
	}
	
	public void setAnzahlRäume(int anzahlRäume) {
		this.anzahlRäume = anzahlRäume;
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
		result = prime * result + anzahlRäume;
		result = prime * result + gruppenräume_ID;
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
		Gruppenräume other = (Gruppenräume) obj;
		if (anzahlPersonen != other.anzahlPersonen)
			return false;
		if (anzahlRäume != other.anzahlRäume)
			return false;
		if (gruppenräume_ID != other.gruppenräume_ID)
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
