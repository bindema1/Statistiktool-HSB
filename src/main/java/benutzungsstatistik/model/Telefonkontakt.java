package benutzungsstatistik.model;

import java.io.Serializable;
/**
 * Das ist die Datenklasse mit allen Attributen, damit man einen Telefonkontakt in
 * die Tabelle 'Telefonkontakt' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
import java.sql.Timestamp;

public class Telefonkontakt implements Serializable {

	int telefonkontakt_ID;
	Timestamp timestamp;
	Benutzungsstatistik benutzungsstatistik;
	
	// Für Hibernate
	public Telefonkontakt(){
				
	}
	
	public Telefonkontakt(Timestamp timestamp, Benutzungsstatistik benutzungsstatistik) {
		this.timestamp = timestamp;
		this.benutzungsstatistik = benutzungsstatistik;
	}

	public int getTelefonkontakt_ID() {
		return telefonkontakt_ID;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Benutzungsstatistik getBenutzungsstatistik() {
		return benutzungsstatistik;
	}

	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setTelefonkontakt_ID(int telefonkontakt_ID) {
		this.telefonkontakt_ID = telefonkontakt_ID;
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
		result = prime * result + telefonkontakt_ID;
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
		Telefonkontakt other = (Telefonkontakt) obj;
		if (benutzungsstatistik == null) {
			if (other.benutzungsstatistik != null)
				return false;
		} else if (!benutzungsstatistik.equals(other.benutzungsstatistik))
			return false;
		if (telefonkontakt_ID != other.telefonkontakt_ID)
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}
	
}
