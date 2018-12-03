package benutzungsstatistik.model;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import allgemein.model.StandortEnum;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man eine
 * Benutzungsstatistik in die Tabelle 'Benutzungsstatistik' schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@Entity
@Table(name = "benutzungsstatistik")
public class Benutzungsstatistik implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "benutzungsstatistik_ID")
	private int benutzungsstatistik_ID;

	@Temporal(TemporalType.DATE)
	private Date datum;

	private int anzahl_Rechercheberatung;
	private boolean kassenbeleg;

	@Enumerated(EnumType.STRING)
	private StandortEnum standort;

	@OneToOne(cascade = CascadeType.ALL)
	private Wintikurier wintikurier;

	@OneToOne(cascade = CascadeType.ALL)
	private Internerkurier internerkurier;

	@OneToMany(mappedBy = "benutzungsstatistik", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Benutzerkontakt> benutzerkontaktListe = new ArrayList<>();

	@OneToMany(mappedBy = "benutzungsstatistik", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BeantwortungBibliothekspersonal> beantwortungBibliothekspersonalListe = new ArrayList<>();

	@OneToMany(mappedBy = "benutzungsstatistik", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Emailkontakt> emailkontaktListe = new ArrayList<>();

	@OneToMany(mappedBy = "benutzungsstatistik", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Telefonkontakt> telefonkontaktListe = new ArrayList<>();

	@OneToMany(mappedBy = "benutzungsstatistik", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Intensivfrage> intensivfrageListe = new ArrayList<>();

	@OneToMany(mappedBy = "benutzungsstatistik", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ExterneGruppe> externeGruppeListe = new ArrayList<>();

	// Für Hibernate
	public Benutzungsstatistik() {

	}

	// Für Benutzungsstatistik Bibliothek
	public Benutzungsstatistik(Date datum, int anzahl_Rechercheberatung, boolean kassenbeleg, StandortEnum standort,
			Wintikurier wintikurier, Internerkurier internerkurier) {
		this.datum = datum;
		this.anzahl_Rechercheberatung = anzahl_Rechercheberatung;
		this.kassenbeleg = kassenbeleg;
		this.standort = standort;
		this.wintikurier = wintikurier;
		this.internerkurier = internerkurier;
	}

	// Für Benutzungsstatistik Wädenswil
	public Benutzungsstatistik(Date datum, int anzahl_Rechercheberatung, StandortEnum standort,
			Internerkurier internerkurier) {
		this.datum = datum;
		this.anzahl_Rechercheberatung = anzahl_Rechercheberatung;
		this.standort = standort;
		this.internerkurier = internerkurier;
	}

	// Für Benutzungsstatistik Lernlandschaft
	public Benutzungsstatistik(Date datum, StandortEnum standort) {
		this.datum = datum;
		this.standort = standort;
	}

	// Ohne Wintikurier für Testzwecke
	public Benutzungsstatistik(Date datum, int anzahl_Rechercheberatung, boolean kassenbeleg, StandortEnum standort) {
		this.datum = datum;
		this.kassenbeleg = kassenbeleg;
		this.anzahl_Rechercheberatung = anzahl_Rechercheberatung;
		this.standort = standort;
	}

	// Add and Remove from List Methoden
	public void addBenutzerkontakt(Benutzerkontakt benutzerkontakt) {
		benutzerkontaktListe.add(benutzerkontakt);
		benutzerkontakt.setBenutzungsstatistik(this);
	}

	public void removeBenutzerkontakt(Benutzerkontakt benutzerkontakt) {
		benutzerkontaktListe.remove(benutzerkontakt);
		benutzerkontakt.setBenutzungsstatistik(null);
	}

	public void addBeantwortungBibliothekspersonal(BeantwortungBibliothekspersonal beantwortungBibliothekspersonal) {
		beantwortungBibliothekspersonalListe.add(beantwortungBibliothekspersonal);
		beantwortungBibliothekspersonal.setBenutzungsstatistik(this);
	}

	public void removeBeantwortungBibliothekspersonal(BeantwortungBibliothekspersonal beantwortungBibliothekspersonal) {
		beantwortungBibliothekspersonalListe.remove(beantwortungBibliothekspersonal);
		beantwortungBibliothekspersonal.setBenutzungsstatistik(null);
	}

	public void addEmailkontakt(Emailkontakt emailkontakt) {
		emailkontaktListe.add(emailkontakt);
		emailkontakt.setBenutzungsstatistik(this);
	}

	public void removeEmailkontakt(Emailkontakt emailkontakt) {
		emailkontaktListe.remove(emailkontakt);
		emailkontakt.setBenutzungsstatistik(null);
	}

	public void addTelefonkontakt(Telefonkontakt telefonkontakt) {
		telefonkontaktListe.add(telefonkontakt);
		telefonkontakt.setBenutzungsstatistik(this);
	}

	public void removeTelefonkontakt(Telefonkontakt telefonkontakt) {
		telefonkontaktListe.remove(telefonkontakt);
		telefonkontakt.setBenutzungsstatistik(null);
	}

	public void addIntensivfrage(Intensivfrage intensivfrage) {
		intensivfrageListe.add(intensivfrage);
		intensivfrage.setBenutzungsstatistik(this);
	}

	public void removeIntensivfrage(Intensivfrage intensivfrage) {
		intensivfrageListe.remove(intensivfrage);
		intensivfrage.setBenutzungsstatistik(null);
	}

	public void addExterneGruppe(ExterneGruppe externeGruppe) {
		externeGruppeListe.add(externeGruppe);
		externeGruppe.setBenutzungsstatistik(this);
	}

	public void removeExterneGruppe(ExterneGruppe externeGruppe) {
		externeGruppeListe.remove(externeGruppe);
		externeGruppe.setBenutzungsstatistik(null);
	}

	// Getter Methoden
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

	public List<Benutzerkontakt> getBenutzerkontaktListe() {
		return benutzerkontaktListe;
	}

	public List<BeantwortungBibliothekspersonal> getBeantwortungBibliothekspersonalListe() {
		return beantwortungBibliothekspersonalListe;
	}

	public List<Emailkontakt> getEmailkontaktListe() {
		return emailkontaktListe;
	}

	public List<Telefonkontakt> getTelefonkontaktListe() {
		return telefonkontaktListe;
	}

	public List<Intensivfrage> getIntensivfrageListe() {
		return intensivfrageListe;
	}

	public List<ExterneGruppe> getExterneGruppeListe() {
		return externeGruppeListe;
	}
	
	public Internerkurier getInternerkurier() {
		return internerkurier;
	}

	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setBenutzungsstatistik_ID(int benutzungsstatistik_ID) {
		this.benutzungsstatistik_ID = benutzungsstatistik_ID;
	}
	
	public void setInternerkurier(Internerkurier internerkurier) {
		this.internerkurier = internerkurier;
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

	public void setBenutzerkontaktListe(List<Benutzerkontakt> benutzerkontaktListe) {
		this.benutzerkontaktListe = benutzerkontaktListe;
	}

	public void setBeantwortungBibliothekspersonalListe(
			List<BeantwortungBibliothekspersonal> beantwortungBibliothekspersonalListe) {
		this.beantwortungBibliothekspersonalListe = beantwortungBibliothekspersonalListe;
	}

	public void setEmailkontaktListe(List<Emailkontakt> emailkontaktListe) {
		this.emailkontaktListe = emailkontaktListe;
	}

	public void setTelefonkontaktListe(List<Telefonkontakt> telefonkontaktListe) {
		this.telefonkontaktListe = telefonkontaktListe;
	}

	public void setIntensivfrageListe(List<Intensivfrage> intensivfrageListe) {
		this.intensivfrageListe = intensivfrageListe;
	}

	public void setExterneGruppeListe(List<ExterneGruppe> externeGruppeListe) {
		this.externeGruppeListe = externeGruppeListe;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anzahl_Rechercheberatung;
		result = prime * result + ((beantwortungBibliothekspersonalListe == null) ? 0
				: beantwortungBibliothekspersonalListe.hashCode());
		result = prime * result + ((benutzerkontaktListe == null) ? 0 : benutzerkontaktListe.hashCode());
		result = prime * result + benutzungsstatistik_ID;
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + ((emailkontaktListe == null) ? 0 : emailkontaktListe.hashCode());
		result = prime * result + ((externeGruppeListe == null) ? 0 : externeGruppeListe.hashCode());
		result = prime * result + ((intensivfrageListe == null) ? 0 : intensivfrageListe.hashCode());
		result = prime * result + ((internerkurier == null) ? 0 : internerkurier.hashCode());
		result = prime * result + (kassenbeleg ? 1231 : 1237);
		result = prime * result + ((standort == null) ? 0 : standort.hashCode());
		result = prime * result + ((telefonkontaktListe == null) ? 0 : telefonkontaktListe.hashCode());
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
		if (beantwortungBibliothekspersonalListe == null) {
			if (other.beantwortungBibliothekspersonalListe != null)
				return false;
		} else if (!beantwortungBibliothekspersonalListe.equals(other.beantwortungBibliothekspersonalListe))
			return false;
		if (benutzerkontaktListe == null) {
			if (other.benutzerkontaktListe != null)
				return false;
		} else if (!benutzerkontaktListe.equals(other.benutzerkontaktListe))
			return false;
		if (benutzungsstatistik_ID != other.benutzungsstatistik_ID)
			return false;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (emailkontaktListe == null) {
			if (other.emailkontaktListe != null)
				return false;
		} else if (!emailkontaktListe.equals(other.emailkontaktListe))
			return false;
		if (externeGruppeListe == null) {
			if (other.externeGruppeListe != null)
				return false;
		} else if (!externeGruppeListe.equals(other.externeGruppeListe))
			return false;
		if (intensivfrageListe == null) {
			if (other.intensivfrageListe != null)
				return false;
		} else if (!intensivfrageListe.equals(other.intensivfrageListe))
			return false;
		if (internerkurier == null) {
			if (other.internerkurier != null)
				return false;
		} else if (!internerkurier.equals(other.internerkurier))
			return false;
		if (kassenbeleg != other.kassenbeleg)
			return false;
		if (standort != other.standort)
			return false;
		if (telefonkontaktListe == null) {
			if (other.telefonkontaktListe != null)
				return false;
		} else if (!telefonkontaktListe.equals(other.telefonkontaktListe))
			return false;
		if (wintikurier == null) {
			if (other.wintikurier != null)
				return false;
		} else if (!wintikurier.equals(other.wintikurier))
			return false;
		return true;
	}

}
