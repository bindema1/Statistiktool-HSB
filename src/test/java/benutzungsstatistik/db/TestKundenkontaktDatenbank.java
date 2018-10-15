package benutzungsstatistik.db;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Allgemein.db.StandortDatenbank;
import Allgemein.model.Standort;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.db.KundenkontaktDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Kundenkontakt;

/**
 * Testet alle Methoden der KudnenkontaktDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestKundenkontaktDatenbank {

	KundenkontaktDatenbank kundenkontaktDB = new KundenkontaktDatenbank();
	Kundenkontakt kundenkontakt;
	StandortDatenbank standortDB = new StandortDatenbank();
	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	
	@Before
	public void initComponents() {
		Standort standort = new Standort("Test Standort");
		standortDB.insertStandort(standort);
		Benutzungsstatistik benutzungsstatistik = new Benutzungsstatistik(new Date(), 8, true, standort);
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);
		
		kundenkontakt = new Kundenkontakt(Timestamp.valueOf("2018-10-10 10:10:10.0"), benutzungsstatistik);
	}

	@Test
	public void testinsertKundenkontakt() {
		kundenkontaktDB.insertKundenkontakt(kundenkontakt);
	}

	@Test
	public void testselectAllKundenkontakte() {
		kundenkontaktDB.insertKundenkontakt(kundenkontakt);
		
		List<Kundenkontakt> kundenkontaktListe = new ArrayList<Kundenkontakt>();
		kundenkontaktListe = kundenkontaktDB.selectAllKundenkontakte();
		
		assertEquals(kundenkontaktListe.get(kundenkontaktListe.size()-1).getKundenkontakt_ID(), kundenkontakt.getKundenkontakt_ID());
		assertEquals(kundenkontaktListe.get(kundenkontaktListe.size()-1).getTimestamp(), kundenkontakt.getTimestamp());
		assertEquals(kundenkontaktListe.get(kundenkontaktListe.size()-1).getBenutzungsstatistik().getBenutzungsstatistik_ID(), kundenkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testselectAllKundenkontakteForBenutzungsstatistik() {
		kundenkontaktDB.insertKundenkontakt(kundenkontakt);
		
		List<Kundenkontakt> kundenkontaktListe = new ArrayList<Kundenkontakt>();
		kundenkontaktListe = kundenkontaktDB.selectAllKundenkontakteForBenutzungsstatistik(kundenkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());

		assertEquals(kundenkontaktListe.get(0).getKundenkontakt_ID(), kundenkontakt.getKundenkontakt_ID());
		assertEquals(kundenkontaktListe.get(0).getTimestamp(), kundenkontakt.getTimestamp());
		assertEquals(kundenkontaktListe.get(0).getBenutzungsstatistik().getBenutzungsstatistik_ID(), kundenkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testdeleteKundenkontakt() {
		List<Kundenkontakt> kundenkontaktListe = new ArrayList<Kundenkontakt>();
		kundenkontaktListe = kundenkontaktDB.selectAllKundenkontakte();

		for(Kundenkontakt k : kundenkontaktListe) {
			kundenkontaktDB.deleteKundenkontakt(k);
		}
		
		kundenkontaktListe = kundenkontaktDB.selectAllKundenkontakte();
		
		assertEquals(kundenkontaktListe.size(), 0);
	}
}
