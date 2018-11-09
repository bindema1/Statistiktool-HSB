package belegung.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man den SektorA in die
 * Tabelle 'SektorA' der Datenbank schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "sektorA")
public class SektorA implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sektorA_ID;

	private int anzahlPersonen;

	@Enumerated(EnumType.STRING)
	private UhrzeitEnum uhrzeit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stockwerk_ID")
	private Stockwerk stockwerk;

	// Für Hibernate
	public SektorA() {

	}

	public SektorA(int anzahlPersonen, UhrzeitEnum uhrzeit, Stockwerk stockwerk) {
		this.anzahlPersonen = anzahlPersonen;
		this.uhrzeit = uhrzeit;
		this.stockwerk = stockwerk;
	}

	public int getSektorA_ID() {
		return sektorA_ID;
	}

	public int getAnzahlPersonen() {
		return anzahlPersonen;
	}

	public UhrzeitEnum getUhrzeit() {
		return uhrzeit;
	}

	public Stockwerk getStockwerk() {
		return stockwerk;
	}

	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setSektorA_ID(int sektorA_ID) {
		this.sektorA_ID = sektorA_ID;
	}

	public void setAnzahlPersonen(int anzahlPersonen) {
		this.anzahlPersonen = anzahlPersonen;
	}

	public void setUhrzeit(UhrzeitEnum uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	public void setStockwerk(Stockwerk stockwerk) {
		this.stockwerk = stockwerk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anzahlPersonen;
		result = prime * result + sektorA_ID;
		result = prime * result + ((stockwerk == null) ? 0 : stockwerk.hashCode());
		result = prime * result + ((uhrzeit == null) ? 0 : uhrzeit.hashCode());
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
		SektorA other = (SektorA) obj;
		if (anzahlPersonen != other.anzahlPersonen)
			return false;
		if (sektorA_ID != other.sektorA_ID)
			return false;
		if (stockwerk == null) {
			if (other.stockwerk != null)
				return false;
		} else if (!stockwerk.equals(other.stockwerk))
			return false;
		if (uhrzeit != other.uhrzeit)
			return false;
		return true;
	}

}
