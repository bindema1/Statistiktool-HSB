package benutzungsstatistik.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man einen
 * BeantwortungBibliothekspersonal in die Tabelle
 * 'BeantwortungBibliothekspersonal' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
public class BeantwortungBibliothekspersonal implements Serializable {

	int beantwortungBibliothekspersonal_ID;
	Timestamp timestamp;
	Benutzungsstatistik benutzungsstatistik;

	// Für Hibernate
	public BeantwortungBibliothekspersonal(){
				
	}

	public BeantwortungBibliothekspersonal(Timestamp timestamp, Benutzungsstatistik benutzungsstatistik) {
		this.timestamp = timestamp;
		this.benutzungsstatistik = benutzungsstatistik;
	}

	public int getBeantwortungBibliothekspersonal_ID() {
		return beantwortungBibliothekspersonal_ID;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Benutzungsstatistik getBenutzungsstatistik() {
		return benutzungsstatistik;
	}
	
	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setBeantwortungBibliothekspersonal_ID(int beantwortungBibliothekspersonal_ID) {
		this.beantwortungBibliothekspersonal_ID = beantwortungBibliothekspersonal_ID;
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
		result = prime * result + beantwortungBibliothekspersonal_ID;
		result = prime * result + ((benutzungsstatistik == null) ? 0 : benutzungsstatistik.hashCode());
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
		BeantwortungBibliothekspersonal other = (BeantwortungBibliothekspersonal) obj;
		if (beantwortungBibliothekspersonal_ID != other.beantwortungBibliothekspersonal_ID)
			return false;
		if (benutzungsstatistik == null) {
			if (other.benutzungsstatistik != null)
				return false;
		} else if (!benutzungsstatistik.equals(other.benutzungsstatistik))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

}
