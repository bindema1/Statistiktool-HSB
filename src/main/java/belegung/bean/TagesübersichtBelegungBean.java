package belegung.bean;

import java.io.Serializable;

/**
 * Beenklasse um Inhalt in Tabellen zu füllen von der Tagesübersicht einer Belegung
 * 
 * @author Marvin Bindemann
 */
public class TagesübersichtBelegungBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String uhrzeit;
	private int arbeitsplätze;
	private int sektorA;
	private int sektorB;
	private int gruppenräumePersonen;
	private int gruppenräume;
	private int carrelsPersonen;
	private int carrels;
	
	public TagesübersichtBelegungBean() {
		
	}

	public String getUhrzeit() {
		return uhrzeit;
	}

	public void setUhrzeit(String uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	public int getArbeitsplätze() {
		return arbeitsplätze;
	}

	public void setArbeitsplätze(int arbeitsplätze) {
		this.arbeitsplätze = arbeitsplätze;
	}

	public int getSektorA() {
		return sektorA;
	}

	public void setSektorA(int sektorA) {
		this.sektorA = sektorA;
	}

	public int getSektorB() {
		return sektorB;
	}

	public void setSektorB(int sektorB) {
		this.sektorB = sektorB;
	}

	public int getGruppenräumePersonen() {
		return gruppenräumePersonen;
	}

	public void setGruppenräumePersonen(int gruppenräumePersonen) {
		this.gruppenräumePersonen = gruppenräumePersonen;
	}

	public int getGruppenräume() {
		return gruppenräume;
	}

	public void setGruppenräume(int gruppenräume) {
		this.gruppenräume = gruppenräume;
	}

	public int getCarrelsPersonen() {
		return carrelsPersonen;
	}

	public void setCarrelsPersonen(int carrelsPersonen) {
		this.carrelsPersonen = carrelsPersonen;
	}

	public int getCarrels() {
		return carrels;
	}

	public void setCarrels(int carrels) {
		this.carrels = carrels;
	}

}
