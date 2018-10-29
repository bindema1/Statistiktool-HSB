package benutzungsstatistik.db;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import allgemein.model.StandortEnum;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;

/**
 * Testet alle Methoden der EmailkontaktDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestEmailkontaktDatenbank {

	EmailkontaktDatenbank emailkontaktDB = new EmailkontaktDatenbank();
	Emailkontakt emailkontakt;
	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	
	@Before
	public void initComponents() {
		Benutzungsstatistik benutzungsstatistik = new Benutzungsstatistik(new Date(), 8, true, StandortEnum.TEST);
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);
		
		emailkontakt = new Emailkontakt(Timestamp.valueOf("2018-10-10 10:10:10.0"), benutzungsstatistik);
	}

	@Test
	public void testinsertEmailkontakt() {
		emailkontaktDB.insertEmailkontakt(emailkontakt);
	}

	@Test
	public void testselectAllEmailkontakte() {
		emailkontaktDB.insertEmailkontakt(emailkontakt);
		
		List<Emailkontakt> emailkontaktListe = new ArrayList<Emailkontakt>();
		emailkontaktListe = emailkontaktDB.selectAllEmailkontakte();
		
		assertEquals(emailkontaktListe.get(emailkontaktListe.size()-1).getEmailkontakt_ID(), emailkontakt.getEmailkontakt_ID());
		assertEquals(emailkontaktListe.get(emailkontaktListe.size()-1).getTimestamp(), emailkontakt.getTimestamp());
		assertEquals(emailkontaktListe.get(emailkontaktListe.size()-1).getBenutzungsstatistik().getBenutzungsstatistik_ID(), emailkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testselectAllEmailkontakteForBenutzungsstatistik() {
		emailkontaktDB.insertEmailkontakt(emailkontakt);
		
		List<Emailkontakt> emailkontaktListe = new ArrayList<Emailkontakt>();
		emailkontaktListe = emailkontaktDB.selectAllEmailkontakteForBenutzungsstatistik(emailkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());

		assertEquals(emailkontaktListe.get(0).getEmailkontakt_ID(), emailkontakt.getEmailkontakt_ID());
		assertEquals(emailkontaktListe.get(0).getTimestamp(), emailkontakt.getTimestamp());
		assertEquals(emailkontaktListe.get(0).getBenutzungsstatistik().getBenutzungsstatistik_ID(), emailkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testdeleteEmailkontakt() {
		List<Emailkontakt> emailkontaktListe = new ArrayList<Emailkontakt>();
		emailkontaktListe = emailkontaktDB.selectAllEmailkontakte();

		for(Emailkontakt k : emailkontaktListe) {
			emailkontaktDB.deleteEmailkontakt(k);
		}
		
		emailkontaktListe = emailkontaktDB.selectAllEmailkontakte();
		
		assertEquals(emailkontaktListe.size(), 0);
	}
}
