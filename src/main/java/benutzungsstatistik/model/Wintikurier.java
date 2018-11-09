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
@SuppressWarnings("serial")
@Entity
@Table(name = "wintikurier")
public class Wintikurier implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int wintikurier_ID;
	
	private int anzahl_Gesundheit;
	private int anzahl_Technik;
	private int anzahl_Wirtschaft;
	private int anzahl_Linguistik;

	// Für Hibernate
	public Wintikurier() {

	}

	public Wintikurier(int anzahl_Gesundheit, int anzahl_Technik, int anzahl_Wirtschaft, int anzahl_Linguistik) {
		this.anzahl_Gesundheit = anzahl_Gesundheit;
		this.anzahl_Technik = anzahl_Technik;
		this.anzahl_Wirtschaft = anzahl_Wirtschaft;
		this.anzahl_Linguistik = anzahl_Linguistik;
	}

	public int getWintikurier_ID() {
		return wintikurier_ID;
	}

	public int getAnzahl_Gesundheit() {
		return anzahl_Gesundheit;
	}

	public void increaseAnzahl_Gesundheit() {
		anzahl_Gesundheit++;
	}

	public void decreaseAnzahl_Gesundheit() {
		anzahl_Gesundheit--;
	}

	public int getAnzahl_Technik() {
		return anzahl_Technik;
	}

	public void increaseAnzahl_Technik() {
		anzahl_Technik++;
	}

	public void decreaseAnzahl_Technik() {
		anzahl_Technik--;
	}

	public int getAnzahl_Wirtschaft() {
		return anzahl_Wirtschaft;
	}

	public void increaseAnzahl_Wirtschaft() {
		anzahl_Wirtschaft++;
	}

	public void decreaseAnzahl_Wirtschaft() {
		anzahl_Wirtschaft--;
	}

	public int getAnzahl_Linguistik() {
		return anzahl_Linguistik;
	}

	public void increaseAnzahl_Linguistik() {
		anzahl_Linguistik++;
	}

	public void decreaseAnzahl_Linguistik() {
		anzahl_Linguistik--;
	}

	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setWintikurier_ID(int wintikurier_ID) {
		this.wintikurier_ID = wintikurier_ID;
	}

	public void setAnzahl_Gesundheit(int anzahl_Gesundheit) {
		this.anzahl_Gesundheit = anzahl_Gesundheit;
	}

	public void setAnzahl_Technik(int anzahl_Technik) {
		this.anzahl_Technik = anzahl_Technik;
	}

	public void setAnzahl_Wirtschaft(int anzahl_Wirtschaft) {
		this.anzahl_Wirtschaft = anzahl_Wirtschaft;
	}

	public void setAnzahl_Linguistik(int anzahl_Linguistik) {
		this.anzahl_Linguistik = anzahl_Linguistik;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anzahl_Gesundheit;
		result = prime * result + anzahl_Linguistik;
		result = prime * result + anzahl_Technik;
		result = prime * result + anzahl_Wirtschaft;
		result = prime * result + wintikurier_ID;
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
		Wintikurier other = (Wintikurier) obj;
		if (anzahl_Gesundheit != other.anzahl_Gesundheit)
			return false;
		if (anzahl_Linguistik != other.anzahl_Linguistik)
			return false;
		if (anzahl_Technik != other.anzahl_Technik)
			return false;
		if (anzahl_Wirtschaft != other.anzahl_Wirtschaft)
			return false;
		if (wintikurier_ID != other.wintikurier_ID)
			return false;
		return true;
	}

}
