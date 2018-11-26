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
 * Das ist die Datenklasse mit allen Attributen, damit man einen Benutzerkontakt
 * in die Tabelle 'Benutzerkontakt' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@Entity
@Table(name = "benutzerkontakt")
public class Benutzerkontakt implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int benutzerkontakt_ID;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benutzungsstatistik_ID")
	private Benutzungsstatistik benutzungsstatistik;

	// Für Hibernate
	public Benutzerkontakt() {

	}

	public Benutzerkontakt(Date timestamp, Benutzungsstatistik benutzungsstatistik) {
		this.timestamp = timestamp;
		this.benutzungsstatistik = benutzungsstatistik;
	}

	public int getBenutzerkontakt_ID() {
		return benutzerkontakt_ID;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Benutzungsstatistik getBenutzungsstatistik() {
		return benutzungsstatistik;
	}

	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setBenutzerkontakt_ID(int benutzerkontakt_ID) {
		this.benutzerkontakt_ID = benutzerkontakt_ID;
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
		result = prime * result + benutzerkontakt_ID;
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
		Benutzerkontakt other = (Benutzerkontakt) obj;
		if (benutzerkontakt_ID != other.benutzerkontakt_ID)
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
