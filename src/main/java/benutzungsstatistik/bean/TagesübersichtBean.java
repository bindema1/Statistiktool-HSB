package benutzungsstatistik.bean;

import java.io.Serializable;

/**
 * Beenklasse um Inhalt in Tabellen zu füllen
 * @author Marvin Bindemann
 *
 */
public class TagesübersichtBean implements Serializable{

	private String uhrzeit;
	private Integer kontakt;
	private Integer intensiv;
	private Integer email;
	private Integer telefon;
	
	public TagesübersichtBean() {
		
	}

	public String getUhrzeit() {
		return uhrzeit;
	}

	public void setUhrzeit(String uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	public Integer getKontakt() {
		return kontakt;
	}

	public void setKontakt(Integer kontakt) {
		this.kontakt = kontakt;
	}

	public Integer getIntensiv() {
		return intensiv;
	}

	public void setIntensiv(Integer intensiv) {
		this.intensiv = intensiv;
	}

	public Integer getEmail() {
		return email;
	}

	public void setEmail(Integer email) {
		this.email = email;
	}

	public Integer getTelefon() {
		return telefon;
	}

	public void setTelefon(Integer telefon) {
		this.telefon = telefon;
	}
	
	
}
