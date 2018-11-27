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
 * Das ist die Datenklasse mit allen Attributen, damit man eine ExterneGruppe in
 * die Tabelle 'ExterneGruppe' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@Entity
@Table(name = "externeGruppe")
public class ExterneGruppe implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int externeGruppe_ID;
	
	private String name;
	private int anzahl_Personen;
	private String erfasstUm;
	

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benutzungsstatistik_ID")
	private Benutzungsstatistik benutzungsstatistik;
	
	// Für Hibernate
	public ExterneGruppe(){
				
	}
	
	public ExterneGruppe(String name, int anzahl_Personen, String erfasstUm, Benutzungsstatistik benutzungsstatistik) {
		this.name = name;
		this.anzahl_Personen = anzahl_Personen;
		this.erfasstUm = erfasstUm;
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
	
	public String getErfasstUm() {
		return erfasstUm;
	}

	public void setErfasstUm(String erfasstUm) {
		this.erfasstUm = erfasstUm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anzahl_Personen;
		result = prime * result + ((benutzungsstatistik == null) ? 0 : benutzungsstatistik.hashCode());
		result = prime * result + ((erfasstUm == null) ? 0 : erfasstUm.hashCode());
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
		if (erfasstUm == null) {
			if (other.erfasstUm != null)
				return false;
		} else if (!erfasstUm.equals(other.erfasstUm))
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
				+ anzahl_Personen + ", erfasstUm=" + erfasstUm + ", benutzungsstatistik=" + benutzungsstatistik + "]";
	}

}
