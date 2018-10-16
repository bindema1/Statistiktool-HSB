package benutzungsstatistik.model;

import java.io.Serializable;

import java.sql.Timestamp;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man einen Benutzerkontakt
 * in die Tabelle 'Benutzerkontakt' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
public class Benutzerkontakt implements Serializable {

	int benutzerkontakt_ID;
	Timestamp timestamp;
	Benutzungsstatistik benutzungsstatistik;

	// Für Hibernate
	public Benutzerkontakt() {

	}

	public Benutzerkontakt(Timestamp timestamp, Benutzungsstatistik benutzungsstatistik) {
		this.timestamp = timestamp;
		this.benutzungsstatistik = benutzungsstatistik;
	}

	public int getBenutzerkontakt_ID() {
		return benutzerkontakt_ID;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Benutzungsstatistik getBenutzungsstatistik() {
		return benutzungsstatistik;
	}

	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setBenutzerkontakt_ID(int benutzerkontakt_ID) {
		this.benutzerkontakt_ID = benutzerkontakt_ID;
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
		result = prime * result + benutzerkontakt_ID;
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
		Benutzerkontakt other = (Benutzerkontakt) obj;
		if (benutzungsstatistik == null) {
			if (other.benutzungsstatistik != null)
				return false;
		} else if (!benutzungsstatistik.equals(other.benutzungsstatistik))
			return false;
		if (benutzerkontakt_ID != other.benutzerkontakt_ID)
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

}
