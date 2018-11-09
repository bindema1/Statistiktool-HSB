package benutzungsstatistik.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man einen Telefonkontakt in
 * die Tabelle 'Telefonkontakt' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "telefonkontakt")
public class Telefonkontakt implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int telefonkontakt_ID;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benutzungsstatistik_ID")
	private Benutzungsstatistik benutzungsstatistik;
	
	// Für Hibernate
	public Telefonkontakt(){
				
	}
	
	public Telefonkontakt(Date timestamp, Benutzungsstatistik benutzungsstatistik) {
		this.timestamp = timestamp;
		this.benutzungsstatistik = benutzungsstatistik;
	}

	public int getTelefonkontakt_ID() {
		return telefonkontakt_ID;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Benutzungsstatistik getBenutzungsstatistik() {
		return benutzungsstatistik;
	}

	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setTelefonkontakt_ID(int telefonkontakt_ID) {
		this.telefonkontakt_ID = telefonkontakt_ID;
	}

	public void setTimestamp(Date timestamp) {
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
		return true;
	}

}
