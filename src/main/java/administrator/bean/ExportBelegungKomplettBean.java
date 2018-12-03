package administrator.bean;

import java.io.Serializable;

/**
 * Bean für den Export. Das Bean ist sogesagt nur ein Container für Dateninhalt
 * 
 * @author Marvin Bindemann
 */
public class ExportBelegungKomplettBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private int kw;
	private String wochentag;
	private String datum;
	private String uhrzeit;
	private String bereich;
	private String stockwerk;
	private String sektor;	
	private int anzahl;
	private String auslastung;
	
	public ExportBelegungKomplettBean(int kw, String wochentag, String datum, String uhrzeit, String bereich,
			String stockwerk, String sektor, int anzahl, String auslastung) {
		super();
		this.kw = kw;
		this.wochentag = wochentag;
		this.datum = datum;
		this.uhrzeit = uhrzeit;
		this.bereich = bereich;
		this.stockwerk = stockwerk;
		this.sektor = sektor;
		this.anzahl = anzahl;
		this.auslastung = auslastung;
	}

	public int getKw() {
		return kw;
	}

	public void setKw(int kw) {
		this.kw = kw;
	}

	public String getWochentag() {
		return wochentag;
	}

	public void setWochentag(String wochentag) {
		this.wochentag = wochentag;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getUhrzeit() {
		return uhrzeit;
	}

	public void setUhrzeit(String uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	public String getBereich() {
		return bereich;
	}

	public void setBereich(String bereich) {
		this.bereich = bereich;
	}

	public String getStockwerk() {
		return stockwerk;
	}

	public void setStockwerk(String stockwerk) {
		this.stockwerk = stockwerk;
	}

	public String getSektor() {
		return sektor;
	}

	public void setSektor(String sektor) {
		this.sektor = sektor;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

	public String getAuslastung() {
		return auslastung;
	}

	public void setAuslastung(String auslastung) {
		this.auslastung = auslastung;
	}
	
}
