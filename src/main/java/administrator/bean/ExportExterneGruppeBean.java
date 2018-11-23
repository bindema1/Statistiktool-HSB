package administrator.bean;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class ExportExterneGruppeBean implements Serializable{

	private int kw;
	private String wochentag;
	private String datum;
	private String name_Gruppe;
	private int anzahl;
	
	public ExportExterneGruppeBean(int kW, String wochentag, String datum, String name_Gruppe, int anzahl) {
		super();
		this.kw = kW;
		this.wochentag = wochentag;
		this.datum = datum;
		this.name_Gruppe = name_Gruppe;
		this.anzahl = anzahl;
	}

	public int getkW() {
		return kw;
	}

	public void setkW(int kW) {
		this.kw = kW;
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

	public String getName_Gruppe() {
		return name_Gruppe;
	}

	public void setName_Gruppe(String name_Gruppe) {
		this.name_Gruppe = name_Gruppe;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
	
}
