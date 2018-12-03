package administrator.bean;

import java.io.Serializable;

/**
 * Bean für den Export. Das Bean ist sogesagt nur ein Container für Dateninhalt
 * 
 * @author Marvin Bindemann
 */
public class ExportExterneGruppeBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private int kw;
	private String wochentag;
	private String datum;
	private String nameGruppe;
	private int anzahl;
	
	public ExportExterneGruppeBean(int kW, String wochentag, String datum, String name_Gruppe, int anzahl) {
		super();
		this.kw = kW;
		this.wochentag = wochentag;
		this.datum = datum;
		this.nameGruppe = name_Gruppe;
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
		return nameGruppe;
	}

	public void setName_Gruppe(String name_Gruppe) {
		this.nameGruppe = name_Gruppe;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
	
}
