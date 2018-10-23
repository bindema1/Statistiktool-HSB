package belegung.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import allgemein.model.Standort;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man eine Belegung in
 * die Tabelle 'Belegung' der Datenbank schreiben kann.
 * 
 * @author Marvin Bindemann
 */
public class Belegung {

	int belegungs_ID;
	Date datum;
	Standort standort;
	List<Stockwerk> stockwerkListe = new ArrayList<>();
	
	// Für Hibernate
	public Belegung() {
		
	}
	
	public Belegung(Date datum, Standort standort) {
		this.datum = datum;
		this.standort = standort;
	}

	public int getBelegungs_ID() {
		return belegungs_ID;
	}

	public Date getDatum() {
		return datum;
	}

	public Standort getStandort() {
		return standort;
	}
	
	public List<Stockwerk> getStockwerkListe() {
		return stockwerkListe;
	}
	
	public void addStockwerk(Stockwerk stockwerk) {
		stockwerkListe.add(stockwerk);
	}

	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setBelegungs_ID(int belegungs_ID) {
		this.belegungs_ID = belegungs_ID;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public void setStandort(Standort standort) {
		this.standort = standort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + belegungs_ID;
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + ((standort == null) ? 0 : standort.hashCode());
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
		Belegung other = (Belegung) obj;
		if (belegungs_ID != other.belegungs_ID)
			return false;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (standort == null) {
			if (other.standort != null)
				return false;
		} else if (!standort.equals(other.standort))
			return false;
		return true;
	}
	
	
}
