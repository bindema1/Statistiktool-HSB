package belegung.model;

/**
 * Enum fuer Stockwerk <br>
 * EG, <br>
 * ZG1, <br>
 * ZG2, <br>
 * LL, <br>
 * WÄDI, <br>
 * In dieser Klasse sind alle Werte fuer das Enum 'Stockwerk' drin gespeichert.
 * Man benoetigt diese, damit man den richtigen Wert in die Datenbank
 * reinschreiben kann und somit weiss, welche Stockwerke es alle gibt und für
 * welches Stockwerk etwas erstellt wird
 * 
 * @author Marvin Bindemann
 */
public enum StockwerkEnum {

	EG, ZG1, ZG2, LL, WÄDI, TEST;

	@Override
	public String toString() {
		return name().toUpperCase();
	}
}
