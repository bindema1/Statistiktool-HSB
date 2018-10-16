package benutzungsstatistik.model;

import java.io.Serializable;

import java.sql.Timestamp;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man eine Intensivfrage in
 * die Tabelle 'Intensivfrage' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
public class Intensivfrage implements Serializable {

	
	int intensivfrage_ID;
	Timestamp timestamp;
	Benutzungsstatistik benutzungsstatistik;
	
	// Für Hibernate
	public Intensivfrage(){
				
	}
	
	public Intensivfrage(Timestamp timestamp, Benutzungsstatistik benutzungsstatistik) {
		this.timestamp = timestamp;
		this.benutzungsstatistik = benutzungsstatistik;
	}

	public int getIntensivfrage_ID() {
		return intensivfrage_ID;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Benutzungsstatistik getBenutzungsstatistik() {
		return benutzungsstatistik;
	}

	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setIntensivfrage_ID(int intensivfrage_ID) {
		this.intensivfrage_ID = intensivfrage_ID;
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
		result = prime * result + intensivfrage_ID;
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
		Intensivfrage other = (Intensivfrage) obj;
		if (benutzungsstatistik == null) {
			if (other.benutzungsstatistik != null)
				return false;
		} else if (!benutzungsstatistik.equals(other.benutzungsstatistik))
			return false;
		if (intensivfrage_ID != other.intensivfrage_ID)
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}
	
}
