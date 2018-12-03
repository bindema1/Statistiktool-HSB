package belegung.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import allgemein.model.StandortEnum;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man eine Belegung in
 * die Tabelle 'Belegung' der Datenbank schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@Entity
@Table(name = "belegung")
public class Belegung implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="belegungs_ID")
	private int belegungs_ID;
	
	@Temporal(TemporalType.DATE)
	private Date datum;
	
	@Enumerated(EnumType.STRING)
	private StandortEnum standort;
	
	//Eine Belegung hat 1-mehrer Stockwerk
	@ManyToMany(cascade = CascadeType.ALL, targetEntity=Stockwerk.class)
	@JoinTable(name = "Belegung_Stockwerk", joinColumns = @JoinColumn(name = "belegungs_ID"), inverseJoinColumns = @JoinColumn(name = "stockwerk_ID"))
	private List<Stockwerk> stockwerkListe = new ArrayList<>();
	
	// Für Hibernate
	public Belegung() {
		
	}
	
	public Belegung(Date datum, StandortEnum standort) {
		this.datum = datum;
		this.standort = standort;
	}

	public int getBelegungs_ID() {
		return belegungs_ID;
	}

	public Date getDatum() {
		return datum;
	}

	public StandortEnum getStandort() {
		return standort;
	}
	
	public List<Stockwerk> getStockwerkListe() {
		return stockwerkListe;
	}
	
	public void addStockwerk(Stockwerk stockwerk) {
		stockwerkListe.add(stockwerk);
	}
	
	public void removeStockwerk(Stockwerk stockwerk) {
		stockwerkListe.remove(stockwerk);
	}

	
	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setBelegungs_ID(int belegungs_ID) {
		this.belegungs_ID = belegungs_ID;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public void setStandort(StandortEnum standort) {
		this.standort = standort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + belegungs_ID;
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + ((standort == null) ? 0 : standort.hashCode());
		result = prime * result + ((stockwerkListe == null) ? 0 : stockwerkListe.hashCode());
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
		Belegung other = (Belegung) obj;
		if (belegungs_ID != other.belegungs_ID)
			return false;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (standort != other.standort)
			return false;
		if (stockwerkListe == null) {
			if (other.stockwerkListe != null)
				return false;
		} else if (!stockwerkListe.equals(other.stockwerkListe))
			return false;
		return true;
	}

}
