package administrator.bean;

import java.io.Serializable;

/**
 * Bean für den Export. Das Bean ist sogesagt nur ein Container für Dateninhalt
 * 
 * @author Marvin Bindemann
 */
public class ExportWintikurierTagBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private int kw;
	private String wochentag;
	private String datum;
	private String departement;
	private int total;
	
	public ExportWintikurierTagBean(int kw, String wochentag, String datum, String departement, int total) {
		super();
		this.kw = kw;
		this.wochentag = wochentag;
		this.datum = datum;
		this.departement = departement;
		this.total = total;
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

	public String getDepartement() {
		return departement;
	}

	public void setDepartement(String departement) {
		this.departement = departement;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
}
