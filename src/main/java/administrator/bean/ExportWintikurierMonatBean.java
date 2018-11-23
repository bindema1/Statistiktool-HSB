package administrator.bean;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class ExportWintikurierMonatBean implements Serializable{

	private String monat;
	private int jahr;
	private String department;
	private int anzahl;
	
	public ExportWintikurierMonatBean(String monat, int jahr, String department, int anzahl) {
		super();
		this.monat = monat;
		this.jahr = jahr;
		this.department = department;
		this.anzahl = anzahl;
	}

	public String getMonat() {
		return monat;
	}

	public void setMonat(String monat) {
		this.monat = monat;
	}

	public int getJahr() {
		return jahr;
	}

	public void setJahr(int jahr) {
		this.jahr = jahr;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
	
}
