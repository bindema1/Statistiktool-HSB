package benutzungsstatistik.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man einen Wintikurier in
 * die Tabelle 'Wintikurier' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@Entity
@Table(name = "internerkurier")
public class Internerkurier implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int internerkurier_ID;
	
	private int anzahl_Reidbach;
	private int anzahl_RA;
	private int anzahl_GS;

	// Für Hibernate
	public Internerkurier() {

	}

	public Internerkurier(int anzahl_Reidbach, int anzahl_RA, int anzahl_GS) {
		this.anzahl_Reidbach = anzahl_Reidbach;
		this.anzahl_RA = anzahl_RA;
		this.anzahl_GS = anzahl_GS;
	}

	public void increaseAnzahl_Reidbach() {
		anzahl_Reidbach++;
	}

	public void decreaseAnzahl_Reidbach() {
		anzahl_Reidbach--;
	}
	
	public void increaseAnzahl_RA() {
		anzahl_RA++;
	}

	public void decreaseAnzahl_RA() {
		anzahl_RA--;
	}
	
	public void increaseAnzahl_GS() {
		anzahl_GS++;
	}

	public void decreaseAnzahl_GS() {
		anzahl_GS--;
	}

	public int getInternerkurier_ID() {
		return internerkurier_ID;
	}

	public int getAnzahl_Reidbach() {
		return anzahl_Reidbach;
	}

	public int getAnzahl_RA() {
		return anzahl_RA;
	}

	public int getAnzahl_GS() {
		return anzahl_GS;
	}

	// Für Hibernate alle Set-Methoden, Hashcode und equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anzahl_GS;
		result = prime * result + anzahl_RA;
		result = prime * result + anzahl_Reidbach;
		result = prime * result + internerkurier_ID;
		return result;
	}

	public void setInternerkurier_ID(int internerkurier_ID) {
		this.internerkurier_ID = internerkurier_ID;
	}

	public void setAnzahl_Reidbach(int anzahl_Reidbach) {
		this.anzahl_Reidbach = anzahl_Reidbach;
	}

	public void setAnzahl_RA(int anzahl_RA) {
		this.anzahl_RA = anzahl_RA;
	}

	public void setAnzahl_GS(int anzahl_GS) {
		this.anzahl_GS = anzahl_GS;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Internerkurier other = (Internerkurier) obj;
		if (anzahl_GS != other.anzahl_GS)
			return false;
		if (anzahl_RA != other.anzahl_RA)
			return false;
		if (anzahl_Reidbach != other.anzahl_Reidbach)
			return false;
		if (internerkurier_ID != other.internerkurier_ID)
			return false;
		return true;
	}
	
}
