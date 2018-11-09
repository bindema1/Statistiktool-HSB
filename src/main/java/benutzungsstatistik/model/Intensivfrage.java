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
 * Das ist die Datenklasse mit allen Attributen, damit man eine Intensivfrage in
 * die Tabelle 'Intensivfrage' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "intensivfrage")
public class Intensivfrage implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int intensivfrage_ID;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benutzungsstatistik_ID")
	private Benutzungsstatistik benutzungsstatistik;
	
	// Für Hibernate
	public Intensivfrage(){
				
	}
	
	public Intensivfrage(Date timestamp, Benutzungsstatistik benutzungsstatistik) {
		this.timestamp = timestamp;
		this.benutzungsstatistik = benutzungsstatistik;
	}

	public int getIntensivfrage_ID() {
		return intensivfrage_ID;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Benutzungsstatistik getBenutzungsstatistik() {
		return benutzungsstatistik;
	}

	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setIntensivfrage_ID(int intensivfrage_ID) {
		this.intensivfrage_ID = intensivfrage_ID;
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
