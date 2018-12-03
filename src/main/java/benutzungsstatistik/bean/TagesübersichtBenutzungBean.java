package benutzungsstatistik.bean;

import java.io.Serializable;

/**
 * Beenklasse um Inhalt in Tabellen zu füllen
 * 
 * @author Marvin Bindemann
 */
public class TagesübersichtBenutzungBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String uhrzeit;
	private int kontakt;
	private int intensiv;
	private int email;
	private int telefon;
	private int beantwortungBibi;
	
	public TagesübersichtBenutzungBean() {
		
	}

	public String getUhrzeit() {
		return uhrzeit;
	}

	public void setUhrzeit(String uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	public int getKontakt() {
		return kontakt;
	}

	public void setKontakt(int kontakt) {
		this.kontakt = kontakt;
	}

	public int getIntensiv() {
		return intensiv;
	}

	public void setIntensiv(int intensiv) {
		this.intensiv = intensiv;
	}

	public int getEmail() {
		return email;
	}

	public void setEmail(int email) {
		this.email = email;
	}

	public int getTelefon() {
		return telefon;
	}

	public void setTelefon(int telefon) {
		this.telefon = telefon;
	}

	public int getBeantwortungBibi() {
		return beantwortungBibi;
	}

	public void setBeantwortungBibi(int beantwortungBibi) {
		this.beantwortungBibi = beantwortungBibi;
	}
	
}
