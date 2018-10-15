package benutzungsstatistik.bean;

import java.io.Serializable;
/**
 * Beenklasse um Inhalt in Tabellen zu füllen
 * @author Marvin Bindemann
 *
 */
public class ExterneGruppeBean implements Serializable{

	private String name;
	private int anzahl_personen;
	
	public ExterneGruppeBean() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAnzahl_personen() {
		return anzahl_personen;
	}

	public void setAnzahl_personen(int anzahl_personen) {
		this.anzahl_personen = anzahl_personen;
	}
	
}
