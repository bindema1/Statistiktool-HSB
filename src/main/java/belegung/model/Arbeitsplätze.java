package belegung.model;

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
 * Das ist die Datenklasse mit allen Attributen, damit man Arbeitsplätze in
 * die Tabelle 'Arbeitsplätze' der Datenbank schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@Entity
@Table(name = "arbeitsplätze")
public class Arbeitsplätze {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long arbeitsplätze_ID;
	
	private int anzahlPersonen;

	@Enumerated(EnumType.STRING)
	private UhrzeitEnum uhrzeit;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockwerk_ID")
	private Stockwerk stockwerk;
	
	// Für Hibernate
	public Arbeitsplätze() {
		
	}
	
	public Arbeitsplätze(int anzahlPersonen, UhrzeitEnum uhrzeit, Stockwerk stockwerk) {
		this.anzahlPersonen = anzahlPersonen;
		this.uhrzeit = uhrzeit;
		this.stockwerk = stockwerk;
	}

	public Long getArbeitsplätze_ID() {
		return arbeitsplätze_ID;
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
	public void setArbeitsplätze_ID(Long arbeitsplätze_ID) {
		this.arbeitsplätze_ID = arbeitsplätze_ID;
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
		result = prime * result + ((arbeitsplätze_ID == null) ? 0 : arbeitsplätze_ID.hashCode());
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
		Arbeitsplätze other = (Arbeitsplätze) obj;
		if (anzahlPersonen != other.anzahlPersonen)
			return false;
		if (arbeitsplätze_ID == null) {
			if (other.arbeitsplätze_ID != null)
				return false;
		} else if (!arbeitsplätze_ID.equals(other.arbeitsplätze_ID))
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
