package allgemein.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man einen Angestellten in
 * die Tabelle 'Angestellter' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "angestellter")
public class Angestellter implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int angestellter_ID;
	
	private String name;
	private String passwort;
	
	@Temporal(TemporalType.DATE)
	private Date passwort_datum;
	
	private boolean rechteAdmin;
	
	@Enumerated(EnumType.STRING)
	private StandortEnum standort;

	// Für Hibernate
	public Angestellter(){
				
	}

	public Angestellter(String name, String passwort, Date passwort_datum, boolean rechteAdmin, StandortEnum standort) {
		this.name = name;
		this.passwort = passwort;
		this.passwort_datum = passwort_datum;
		this.rechteAdmin = rechteAdmin;
		this.standort = standort;
	}

	public int getAngestellter_ID() {
		return angestellter_ID;
	}

	public String getName() {
		return name;
	}

	public String getPasswort() {
		return passwort;
	}

	public Date getPasswort_datum() {
		return passwort_datum;
	}

	public boolean isRechteAdmin() {
		return rechteAdmin;
	}

	public StandortEnum getStandort() {
		return standort;
	}
	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setAngestellter_ID(int angestellter_ID) {
		this.angestellter_ID = angestellter_ID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public void setPasswort_datum(Date passwort_datum) {
		this.passwort_datum = passwort_datum;
	}

	public void setRechteAdmin(boolean rechteAdmin) {
		this.rechteAdmin = rechteAdmin;
	}

	public void setStandort(StandortEnum standort) {
		this.standort = standort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + angestellter_ID;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((passwort == null) ? 0 : passwort.hashCode());
		result = prime * result + ((passwort_datum == null) ? 0 : passwort_datum.hashCode());
		result = prime * result + (rechteAdmin ? 1231 : 1237);
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
		Angestellter other = (Angestellter) obj;
		if (angestellter_ID != other.angestellter_ID)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (passwort == null) {
			if (other.passwort != null)
				return false;
		} else if (!passwort.equals(other.passwort))
			return false;
		if (passwort_datum == null) {
			if (other.passwort_datum != null)
				return false;
		} else if (!passwort_datum.equals(other.passwort_datum))
			return false;
		if (rechteAdmin != other.rechteAdmin)
			return false;
		if (standort != other.standort)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	

}
