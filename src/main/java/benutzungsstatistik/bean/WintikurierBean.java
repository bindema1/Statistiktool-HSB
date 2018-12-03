package benutzungsstatistik.bean;

import java.io.Serializable;
/**
 * Beenklasse um Inhalt in Tabellen zu füllen
 * 
 * @author Marvin Bindemann
 */
public class WintikurierBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String department;
	private int anzahl;
	
	public WintikurierBean() {
		
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
