package administrator.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ExportBenutzungsstatistikBean implements Serializable{

	private int kw;
	private String wochentag;
	private String datum;
	private String uhrzeit;
	private String typ;
	private int anzahl;
	
	public ExportBenutzungsstatistikBean(int kw, String wochentag, String datum, String uhrzeit, String typ,
			int anzahl) {
		super();
		this.kw = kw;
		this.wochentag = wochentag;
		this.datum = datum;
		this.uhrzeit = uhrzeit;
		this.typ = typ;
		this.anzahl = anzahl;
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

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
	
}
