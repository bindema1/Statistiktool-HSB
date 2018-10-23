package belegung.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Das ist die Datenklasse mit allen Attributen, damit man die Kapazität in die
 * Tabelle 'Kapazität' der Datenbank schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@Entity
@Table(name = "kapazität")
public class Kapazität {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long kapzität_ID;
	
	private int maxArbeitsplätze;
	private int maxSektorA;
	private int maxSektorB;
	private int maxCarrels;
	private int maxGruppenräume;

	
	// Für Hibernate
	public Kapazität() {

	}

	public Kapazität(int maxArbeitsplätze, int maxSektorA, int maxSektorB, int maxCarrels, int maxGruppenräume) {
		this.maxArbeitsplätze = maxArbeitsplätze;
		this.maxSektorA = maxSektorA;
		this.maxSektorB = maxSektorB;
		this.maxCarrels = maxCarrels;
		this.maxGruppenräume = maxGruppenräume;
	}

	public Long getKapzität_ID() {
		return kapzität_ID;
	}

	public int getMaxArbeitsplätze() {
		return maxArbeitsplätze;
	}

	public int getMaxSektorA() {
		return maxSektorA;
	}

	public int getMaxSektorB() {
		return maxSektorB;
	}

	public int getMaxCarrels() {
		return maxCarrels;
	}

	public int getMaxGruppenräume() {
		return maxGruppenräume;
	}

	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setKapzität_ID(Long kapzität_ID) {
		this.kapzität_ID = kapzität_ID;
	}

	public void setMaxArbeitsplätze(int maxArbeitsplätze) {
		this.maxArbeitsplätze = maxArbeitsplätze;
	}

	public void setMaxSektorA(int maxSektorA) {
		this.maxSektorA = maxSektorA;
	}

	public void setMaxSektorB(int maxSektorB) {
		this.maxSektorB = maxSektorB;
	}

	public void setMaxCarrels(int maxCarrels) {
		this.maxCarrels = maxCarrels;
	}

	public void setMaxGruppenräume(int maxGruppenräume) {
		this.maxGruppenräume = maxGruppenräume;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kapzität_ID == null) ? 0 : kapzität_ID.hashCode());
		result = prime * result + maxArbeitsplätze;
		result = prime * result + maxCarrels;
		result = prime * result + maxGruppenräume;
		result = prime * result + maxSektorA;
		result = prime * result + maxSektorB;
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
		Kapazität other = (Kapazität) obj;
		if (kapzität_ID == null) {
			if (other.kapzität_ID != null)
				return false;
		} else if (!kapzität_ID.equals(other.kapzität_ID))
			return false;
		if (maxArbeitsplätze != other.maxArbeitsplätze)
			return false;
		if (maxCarrels != other.maxCarrels)
			return false;
		if (maxGruppenräume != other.maxGruppenräume)
			return false;
		if (maxSektorA != other.maxSektorA)
			return false;
		if (maxSektorB != other.maxSektorB)
			return false;
		return true;
	}

}
