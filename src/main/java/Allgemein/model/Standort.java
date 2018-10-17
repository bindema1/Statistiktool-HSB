package allgemein.model;

import java.io.Serializable;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man einen Standort in die
 * Tabelle 'Standort' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
public class Standort implements Serializable {

	int standort_ID;
	String name;

	// Für Hibernate
	public Standort(){
			
	}

	public Standort(String name) {
		this.name = name;
	}

	public int getStandort_ID() {
		return standort_ID;
	}

	public String getName() {
		return name;
	}


	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setStandort_ID(int standort_ID) {
		this.standort_ID = standort_ID;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + standort_ID;
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
		Standort other = (Standort) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (standort_ID != other.standort_ID)
			return false;
		return true;
	}
	
}
