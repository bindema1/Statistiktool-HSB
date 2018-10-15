package benutzungsstatistik.model;

import java.io.Serializable;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man eine ExterneGruppe in
 * die Tabelle 'ExterneGruppe' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
public class ExterneGruppe implements Serializable {

	int externeGruppe_ID;
	String name;
	int anzahl_Personen;
	Benutzungsstatistik benutzungsstatistik;
	
	// Für Hibernate
	public ExterneGruppe(){
				
	}
	
	public ExterneGruppe(String name, int anzahl_Personen, Benutzungsstatistik benutzungsstatistik) {
		this.name = name;
		this.anzahl_Personen = anzahl_Personen;
		this.benutzungsstatistik = benutzungsstatistik;
	}

	public int getExterneGruppe_ID() {
		return externeGruppe_ID;
	}

	public String getName() {
		return name;
	}

	public int getAnzahl_Personen() {
		return anzahl_Personen;
	}

	public Benutzungsstatistik getBenutzungsstatistik() {
		return benutzungsstatistik;
	}

	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setExterneGruppe_ID(int externeGruppe_ID) {
		this.externeGruppe_ID = externeGruppe_ID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAnzahl_Personen(int anzahl_Personen) {
		this.anzahl_Personen = anzahl_Personen;
	}

	public void setBenutzungsstatistik(Benutzungsstatistik benutzungsstatistik) {
		this.benutzungsstatistik = benutzungsstatistik;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anzahl_Personen;
		result = prime * result + ((benutzungsstatistik == null) ? 0 : benutzungsstatistik.hashCode());
		result = prime * result + externeGruppe_ID;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ExterneGruppe other = (ExterneGruppe) obj;
		if (anzahl_Personen != other.anzahl_Personen)
			return false;
		if (benutzungsstatistik == null) {
			if (other.benutzungsstatistik != null)
				return false;
		} else if (!benutzungsstatistik.equals(other.benutzungsstatistik))
			return false;
		if (externeGruppe_ID != other.externeGruppe_ID)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExterneGruppe [externeGruppe_ID=" + externeGruppe_ID + ", name=" + name + ", anzahl_Personen="
				+ anzahl_Personen + ", benutzungsstatistik=" + benutzungsstatistik + "]";
	}
	
}
