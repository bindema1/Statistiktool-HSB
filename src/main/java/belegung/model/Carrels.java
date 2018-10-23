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
 * Das ist die Datenklasse mit allen Attributen, damit man die Carrels in
 * die Tabelle 'Carrels' der Datenbank schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@Entity
@Table(name = "carrels")
public class Carrels {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long carrels_ID;
	
	private int anzahlPersonen;
	private int anzahlRäume;
	
	@Enumerated(EnumType.STRING)
	private UhrzeitEnum uhrzeit;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockwerk_ID")
	private Stockwerk stockwerk;
	
	// Für Hibernate
	public Carrels() {
		
	}
	
	public Carrels(int anzahlPersonen, int anzahlRäume, UhrzeitEnum uhrzeit, Stockwerk stockwerk) {
		this.anzahlPersonen = anzahlPersonen;
		this.anzahlRäume = anzahlRäume;
		this.uhrzeit = uhrzeit;
		this.stockwerk = stockwerk;
	}

	public Long getCarrels_ID() {
		return carrels_ID;
	}
	
	public int getAnzahlRäume() {
		return anzahlRäume;
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
	public void setCarrels_ID(Long carrels_ID) {
		this.carrels_ID = carrels_ID;
	}

	public void setAnzahlPersonen(int anzahlPersonen) {
		this.anzahlPersonen = anzahlPersonen;
	}
	
	public void setAnzahlRäume(int anzahlRäume) {
		this.anzahlRäume = anzahlRäume;
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
		result = prime * result + anzahlRäume;
		result = prime * result + ((carrels_ID == null) ? 0 : carrels_ID.hashCode());
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
		Carrels other = (Carrels) obj;
		if (anzahlPersonen != other.anzahlPersonen)
			return false;
		if (anzahlRäume != other.anzahlRäume)
			return false;
		if (carrels_ID == null) {
			if (other.carrels_ID != null)
				return false;
		} else if (!carrels_ID.equals(other.carrels_ID))
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
