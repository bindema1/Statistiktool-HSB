package benutzungsstatistik.db;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import allgemein.db.StandortDatenbank;
import allgemein.model.Standort;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.db.TelefonkontaktDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Telefonkontakt;

/**
 * Testet alle Methoden der TelefonkontaktDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestTelefonkontaktDatenbank {

	TelefonkontaktDatenbank telefonkontaktDB = new TelefonkontaktDatenbank();
	Telefonkontakt telefonkontakt;
	StandortDatenbank standortDB = new StandortDatenbank();
	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	
	@Before
	public void initComponents() {
		Standort standort = new Standort("Test Standort");
		standortDB.insertStandort(standort);
		Benutzungsstatistik benutzungsstatistik = new Benutzungsstatistik(new Date(), 8, true, standort);
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);
		
		telefonkontakt = new Telefonkontakt(Timestamp.valueOf("2018-10-10 10:10:10.0"), benutzungsstatistik);
	}

	@Test
	public void testinsertTelefonkontakt() {
		telefonkontaktDB.insertTelefonkontakt(telefonkontakt);
	}

	@Test
	public void testselectAllTelefonkontakte() {
		telefonkontaktDB.insertTelefonkontakt(telefonkontakt);
		
		List<Telefonkontakt> telefonkontaktListe = new ArrayList<Telefonkontakt>();
		telefonkontaktListe = telefonkontaktDB.selectAllTelefonkontakte();
		
		assertEquals(telefonkontaktListe.get(telefonkontaktListe.size()-1).getTelefonkontakt_ID(), telefonkontakt.getTelefonkontakt_ID());
		assertEquals(telefonkontaktListe.get(telefonkontaktListe.size()-1).getTimestamp(), telefonkontakt.getTimestamp());
		assertEquals(telefonkontaktListe.get(telefonkontaktListe.size()-1).getBenutzungsstatistik().getBenutzungsstatistik_ID(), telefonkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testselectAllTelefonkontakteForBenutzungsstatistik() {
		telefonkontaktDB.insertTelefonkontakt(telefonkontakt);
		
		List<Telefonkontakt> telefonkontaktListe = new ArrayList<Telefonkontakt>();
		telefonkontaktListe = telefonkontaktDB.selectAllTelefonkontakteForBenutzungsstatistik(telefonkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());

		assertEquals(telefonkontaktListe.get(0).getTelefonkontakt_ID(), telefonkontakt.getTelefonkontakt_ID());
		assertEquals(telefonkontaktListe.get(0).getTimestamp(), telefonkontakt.getTimestamp());
		assertEquals(telefonkontaktListe.get(0).getBenutzungsstatistik().getBenutzungsstatistik_ID(), telefonkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testdeleteTelefonkontakt() {
		List<Telefonkontakt> telefonkontaktListe = new ArrayList<Telefonkontakt>();
		telefonkontaktListe = telefonkontaktDB.selectAllTelefonkontakte();

		for(Telefonkontakt k : telefonkontaktListe) {
			telefonkontaktDB.deleteTelefonkontakt(k);
		}
		
		telefonkontaktListe = telefonkontaktDB.selectAllTelefonkontakte();
		
		assertEquals(telefonkontaktListe.size(), 0);
	}
}
