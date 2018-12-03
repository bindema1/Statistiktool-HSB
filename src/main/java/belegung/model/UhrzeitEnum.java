package belegung.model;

/**
 * Enum fuer Uhrzeiten der Belegungserfassung <br>
 * NEUN, <br>
 * ELF, <br>
 * DREIZEHN, <br>
 * FÜNFZEHN, <br>
 * SIEBZEHN, <br>
 * NEUNZEHN <br>
 * In dieser Klasse sind alle Werte fuer das Enum 'Uhrzeit' drin gespeichert.
 * Man benoetigt diese, damit man den richtigen Wert in die Datenbank
 * reinschreiben kann und somit weiss, welche Uhrzeiten es für die Erfassung der
 * Belegung gibt und zu welcher Uhrzeit, welche erfassung gehört
 * 
 * @author Marvin Bindemann
 */
public enum UhrzeitEnum {

	NEUN, ELF, DREIZEHN, FÜNFZEHN, SIEBZEHN, NEUNZEHN;

	@Override
	public String toString() {
		return name().toUpperCase();
	}

}
