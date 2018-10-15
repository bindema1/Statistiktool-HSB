package benutzungsstatistik.model;

import java.io.Serializable;
/**
 * Das ist die Datenklasse mit allen Attributen, damit man einen Kundenkontakt in
 * die Tabelle 'Kundenkontakt' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
import java.sql.Timestamp;

public class Kundenkontakt implements Serializable {

	
	int kundenkontakt_ID;
	Timestamp timestamp;
	Benutzungsstatistik benutzungsstatistik;
	
	// Für Hibernate
	public Kundenkontakt(){
				
	}
	
	public Kundenkontakt(Timestamp timestamp, Benutzungsstatistik benutzungsstatistik) {
		this.timestamp = timestamp;
		this.benutzungsstatistik = benutzungsstatistik;
	}

	public int getKundenkontakt_ID() {
		return kundenkontakt_ID;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Benutzungsstatistik getBenutzungsstatistik() {
		return benutzungsstatistik;
	}
	
	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setKundenkontakt_ID(int kundenkontakt_ID) {
		this.kundenkontakt_ID = kundenkontakt_ID;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public void setBenutzungsstatistik(Benutzungsstatistik benutzungsstatistik) {
		this.benutzungsstatistik = benutzungsstatistik;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((benutzungsstatistik == null) ? 0 : benutzungsstatistik.hashCode());
		result = prime * result + kundenkontakt_ID;
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		Kundenkontakt other = (Kundenkontakt) obj;
		if (benutzungsstatistik == null) {
			if (other.benutzungsstatistik != null)
				return false;
		} else if (!benutzungsstatistik.equals(other.benutzungsstatistik))
			return false;
		if (kundenkontakt_ID != other.kundenkontakt_ID)
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}
	
}
