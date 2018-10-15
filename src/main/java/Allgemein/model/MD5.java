package Allgemein.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Die Klasse MD5 hat nur eine Methode convertMD5(String passwort){}. Sie
 * verschlüsselt die Eingabe des Users auf MD5. Von anderen Klassen wird dann
 * verglichen, ob die verschlüsselte Eingabe des Users die selbe
 * MD5-Verschlüsselung ist, wie auf der Datenbank.
 * 
 * @author Marvin Bindemann
 */
public class MD5 {
	/**
	 * Konstruktor für MD5
	 */
	public MD5() {

	}

	/**
	 * Konvertiert einen String (unverschluesselt) in einen verschluesselten
	 * String
	 * 
	 * @param passwort
	 * @return konvertiertes Passwort. Bei Fehler null
	 */
	public String convertMD5(String passwort) {

		try {
			final MessageDigest digest = MessageDigest.getInstance("MD5");
			final byte[] hash = digest.digest(passwort.getBytes());

			return new String(hash);
		} catch (final NoSuchAlgorithmException nsae) {
			System.out.println("Error, kann nicht zu MD5 gewandelt werden "
					+ nsae);
		}

		return null;
	}

}
