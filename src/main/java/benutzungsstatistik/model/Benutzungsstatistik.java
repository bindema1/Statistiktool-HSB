package benutzungsstatistik.model;

import java.io.Serializable;
import java.util.Date;

import allgemein.model.StandortEnum;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man eine Benutzungsstatistik in
 * die Tabelle 'Benutzungsstatistik' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
public class Benutzungsstatistik implements Serializable {

	int benutzungsstatistik_ID;
	Date datum;
	int anzahl_Rechercheberatung;
	boolean kassenbeleg;
	StandortEnum standort;
	Wintikurier wintikurier;

	// Für Hibernate
	public Benutzungsstatistik() {

	}

	public Benutzungsstatistik(Date datum, int anzahl_Rechercheberatung, boolean kassenbeleg, StandortEnum standort,
			Wintikurier wintikurier) {
		this.datum = datum;
		this.anzahl_Rechercheberatung = anzahl_Rechercheberatung;
		this.kassenbeleg = kassenbeleg;
		this.standort = standort;
		this.wintikurier = wintikurier;
	}
	
	//Ohne Wintikurier für Testzwecke
	public Benutzungsstatistik(Date datum, int anzahl_Rechercheberatung, boolean kassenbeleg, StandortEnum standort) {
		this.datum = datum;
		this.anzahl_Rechercheberatung = anzahl_Rechercheberatung;
		this.kassenbeleg = kassenbeleg;
		this.standort = standort;
	}

	public int getBenutzungsstatistik_ID() {
		return benutzungsstatistik_ID;
	}

	public Date getDatum() {
		return datum;
	}

	public int getAnzahl_Rechercheberatung() {
		return anzahl_Rechercheberatung;
	}

	public boolean isKassenbeleg() {
		return kassenbeleg;
	}

	public StandortEnum getStandort() {
		return standort;
	}

	public Wintikurier getWintikurier() {
		return wintikurier;
	}

	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setBenutzungsstatistik_ID(int benutzungsstatistik_ID) {
		this.benutzungsstatistik_ID = benutzungsstatistik_ID;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public void setAnzahl_Rechercheberatung(int anzahl_Rechercheberatung) {
		this.anzahl_Rechercheberatung = anzahl_Rechercheberatung;
	}

	public void setKassenbeleg(boolean kassenbeleg) {
		this.kassenbeleg = kassenbeleg;
	}

	public void setStandort(StandortEnum standort) {
		this.standort = standort;
	}

	public void setWintikurier(Wintikurier wintikurier) {
		this.wintikurier = wintikurier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anzahl_Rechercheberatung;
		result = prime * result + benutzungsstatistik_ID;
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + (kassenbeleg ? 1231 : 1237);
		result = prime * result + ((standort == null) ? 0 : standort.hashCode());
		result = prime * result + ((wintikurier == null) ? 0 : wintikurier.hashCode());
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
		Benutzungsstatistik other = (Benutzungsstatistik) obj;
		if (anzahl_Rechercheberatung != other.anzahl_Rechercheberatung)
			return false;
		if (benutzungsstatistik_ID != other.benutzungsstatistik_ID)
			return false;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (kassenbeleg != other.kassenbeleg)
			return false;
		if (standort != other.standort)
			return false;
		if (wintikurier == null) {
			if (other.wintikurier != null)
				return false;
		} else if (!wintikurier.equals(other.wintikurier))
			return false;
		return true;
	}

	
}
