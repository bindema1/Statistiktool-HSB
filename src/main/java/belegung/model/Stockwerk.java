package belegung.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man ein Stockwerk in die
 * Tabelle 'Stockwerk' der Datenbank schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@Entity
@Table(name = "stockwerk")
public class Stockwerk implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "stockwerk_ID")
	private Long stockwerk_ID;

	@Enumerated(EnumType.STRING)
	private StockwerkEnum name;

	private boolean hatSektorA;
	private boolean hatSektorB;
	private boolean hatGruppenräume;
	private boolean hatCarrels;

	@OneToOne(cascade = CascadeType.ALL)
	private Kapazität kapzität;

	@OneToMany(mappedBy = "stockwerk", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Arbeitsplätze> arbeitsplatzListe = new ArrayList<>();

	@OneToMany(mappedBy = "stockwerk", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SektorA> sektorAListe = new ArrayList<>();

	@OneToMany(mappedBy = "stockwerk", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SektorB> sektorBListe = new ArrayList<>();

	@OneToMany(mappedBy = "stockwerk", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Gruppenräume> gruppenräumeListe = new ArrayList<>();

	@OneToMany(mappedBy = "stockwerk", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Carrels> carrelsListe = new ArrayList<>();

	@ManyToMany(mappedBy = "stockwerkListe")
	private List<Belegung> belegungsListe = new ArrayList<>();

	// Für Hibernate
	public Stockwerk() {

	}

	public Stockwerk(StockwerkEnum name, boolean hatSektorA, boolean hatSektorB, boolean hatGruppenräume,
			boolean hatCarrels, Kapazität kapzität) {
		this.name = name;
		this.hatSektorA = hatSektorA;
		this.hatSektorB = hatSektorB;
		this.hatGruppenräume = hatGruppenräume;
		this.hatCarrels = hatCarrels;
		this.kapzität = kapzität;
		this.name = name;
	}

	public Long getStockwerk_ID() {
		return stockwerk_ID;
	}

	public StockwerkEnum getName() {
		return name;
	}

	public boolean isHatSektorA() {
		return hatSektorA;
	}

	public boolean isHatSektorB() {
		return hatSektorB;
	}

	public boolean isHatGruppenräume() {
		return hatGruppenräume;
	}

	public boolean isHatCarrels() {
		return hatCarrels;
	}

	public Kapazität getKapzität() {
		return kapzität;
	}

	public List<Arbeitsplätze> getArbeitsplatzListe() {
		return arbeitsplatzListe;
	}

	public void addArbeitsplätze(Arbeitsplätze arbeitsplätze) {
		arbeitsplatzListe.add(arbeitsplätze);
		arbeitsplätze.setStockwerk(this);
	}

	public void removeArbeitsplätze(Arbeitsplätze arbeitsplätze) {
		arbeitsplatzListe.remove(arbeitsplätze);
		arbeitsplätze.setStockwerk(null);
	}

	public List<SektorA> getSektorAListe() {
		return sektorAListe;
	}

	public void setArbeitsplatzListe(List<Arbeitsplätze> arbeitsplatzListe) {
		this.arbeitsplatzListe = arbeitsplatzListe;
	}

	public void setSektorAListe(List<SektorA> sektorAListe) {
		this.sektorAListe = sektorAListe;
	}

	public void setSektorBListe(List<SektorB> sektorBListe) {
		this.sektorBListe = sektorBListe;
	}

	public void setGruppenräumeListe(List<Gruppenräume> gruppenräumeListe) {
		this.gruppenräumeListe = gruppenräumeListe;
	}

	public void setCarrelsListe(List<Carrels> carrelsListe) {
		this.carrelsListe = carrelsListe;
	}

	public void addSektorA(SektorA sektorA) {
		sektorAListe.add(sektorA);
		sektorA.setStockwerk(this);
	}

	public void removeSektorA(SektorA sektorA) {
		sektorAListe.remove(sektorA);
		sektorA.setStockwerk(null);
	}

	public List<SektorB> getSektorBListe() {
		return sektorBListe;
	}

	public void addSektorB(SektorB sektorB) {
		sektorBListe.add(sektorB);
		sektorB.setStockwerk(this);
	}

	public void removeSektorB(SektorB sektorB) {
		sektorBListe.remove(sektorB);
		sektorB.setStockwerk(null);
	}

	public List<Gruppenräume> getGruppenräumeListe() {
		return gruppenräumeListe;
	}

	public void addGruppenräume(Gruppenräume gruppenräume) {
		gruppenräumeListe.add(gruppenräume);
		gruppenräume.setStockwerk(this);
	}

	public void removeGruppenräume(Gruppenräume gruppenräume) {
		gruppenräumeListe.remove(gruppenräume);
		gruppenräume.setStockwerk(null);
	}

	public List<Carrels> getCarrelsListe() {
		return carrelsListe;
	}

	public void addCarrels(Carrels carrels) {
		carrelsListe.add(carrels);
		carrels.setStockwerk(this);
	}

	public void removeCarrels(Carrels carrels) {
		carrelsListe.remove(carrels);
		carrels.setStockwerk(null);
	}

	public List<Belegung> getBelegungsListe() {
		return belegungsListe;
	}


	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setStockwerk_ID(Long stockwerk_ID) {
		this.stockwerk_ID = stockwerk_ID;
	}
	
	public void setBelegungsListe(List<Belegung> belegungsListe) {
		this.belegungsListe = belegungsListe;
	}

	public void setName(StockwerkEnum name) {
		this.name = name;
	}

	public void setHatSektorA(boolean hatSektorA) {
		this.hatSektorA = hatSektorA;
	}

	public void setHatSektorB(boolean hatSektorB) {
		this.hatSektorB = hatSektorB;
	}

	public void setHatGruppenräume(boolean hatGruppenräume) {
		this.hatGruppenräume = hatGruppenräume;
	}

	public void setHatCarrels(boolean hatCarrels) {
		this.hatCarrels = hatCarrels;
	}

	public void setKapzität(Kapazität kapzität) {
		this.kapzität = kapzität;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arbeitsplatzListe == null) ? 0 : arbeitsplatzListe.hashCode());
		result = prime * result + ((carrelsListe == null) ? 0 : carrelsListe.hashCode());
		result = prime * result + ((gruppenräumeListe == null) ? 0 : gruppenräumeListe.hashCode());
		result = prime * result + (hatCarrels ? 1231 : 1237);
		result = prime * result + (hatGruppenräume ? 1231 : 1237);
		result = prime * result + (hatSektorA ? 1231 : 1237);
		result = prime * result + (hatSektorB ? 1231 : 1237);
		result = prime * result + ((kapzität == null) ? 0 : kapzität.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((sektorAListe == null) ? 0 : sektorAListe.hashCode());
		result = prime * result + ((sektorBListe == null) ? 0 : sektorBListe.hashCode());
		result = prime * result + ((stockwerk_ID == null) ? 0 : stockwerk_ID.hashCode());
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
		Stockwerk other = (Stockwerk) obj;
		if (arbeitsplatzListe == null) {
			if (other.arbeitsplatzListe != null)
				return false;
		} else if (!arbeitsplatzListe.equals(other.arbeitsplatzListe))
			return false;
		if (carrelsListe == null) {
			if (other.carrelsListe != null)
				return false;
		} else if (!carrelsListe.equals(other.carrelsListe))
			return false;
		if (gruppenräumeListe == null) {
			if (other.gruppenräumeListe != null)
				return false;
		} else if (!gruppenräumeListe.equals(other.gruppenräumeListe))
			return false;
		if (hatCarrels != other.hatCarrels)
			return false;
		if (hatGruppenräume != other.hatGruppenräume)
			return false;
		if (hatSektorA != other.hatSektorA)
			return false;
		if (hatSektorB != other.hatSektorB)
			return false;
		if (kapzität == null) {
			if (other.kapzität != null)
				return false;
		} else if (!kapzität.equals(other.kapzität))
			return false;
		if (name != other.name)
			return false;
		if (sektorAListe == null) {
			if (other.sektorAListe != null)
				return false;
		} else if (!sektorAListe.equals(other.sektorAListe))
			return false;
		if (sektorBListe == null) {
			if (other.sektorBListe != null)
				return false;
		} else if (!sektorBListe.equals(other.sektorBListe))
			return false;
		if (stockwerk_ID == null) {
			if (other.stockwerk_ID != null)
				return false;
		} else if (!stockwerk_ID.equals(other.stockwerk_ID))
			return false;
		return true;
	}

}
