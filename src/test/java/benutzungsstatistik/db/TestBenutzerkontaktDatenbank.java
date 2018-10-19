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
import allgemein.model.StandortEnum;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.db.BenutzerkontaktDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Benutzerkontakt;

/**
 * Testet alle Methoden der KudnenkontaktDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestBenutzerkontaktDatenbank {

	BenutzerkontaktDatenbank benutzerkontaktDB = new BenutzerkontaktDatenbank();
	Benutzerkontakt benutzerkontakt;
	StandortDatenbank standortDB = new StandortDatenbank();
	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	
	@Before
	public void initComponents() {
		Standort standort = new Standort(StandortEnum.Test);
		standortDB.insertStandort(standort);
		Benutzungsstatistik benutzungsstatistik = new Benutzungsstatistik(new Date(), 8, true, standort);
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);
		
		benutzerkontakt = new Benutzerkontakt(Timestamp.valueOf("2018-10-10 10:10:10.0"), benutzungsstatistik);
	}

	@Test
	public void testinsertBenutzerkontakt() {
		benutzerkontaktDB.insertBenutzerkontakt(benutzerkontakt);
	}

	@Test
	public void testselectAllBenutzerkontakte() {
		benutzerkontaktDB.insertBenutzerkontakt(benutzerkontakt);
		
		List<Benutzerkontakt> benutzerkontaktListe = new ArrayList<Benutzerkontakt>();
		benutzerkontaktListe = benutzerkontaktDB.selectAllBenutzerkontakte();
		
		assertEquals(benutzerkontaktListe.get(benutzerkontaktListe.size()-1).getBenutzerkontakt_ID(), benutzerkontakt.getBenutzerkontakt_ID());
		assertEquals(benutzerkontaktListe.get(benutzerkontaktListe.size()-1).getTimestamp(), benutzerkontakt.getTimestamp());
		assertEquals(benutzerkontaktListe.get(benutzerkontaktListe.size()-1).getBenutzungsstatistik().getBenutzungsstatistik_ID(), benutzerkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testselectAllBenutzerkontakteForBenutzungsstatistik() {
		benutzerkontaktDB.insertBenutzerkontakt(benutzerkontakt);
		
		List<Benutzerkontakt> benutzerkontaktListe = new ArrayList<Benutzerkontakt>();
		benutzerkontaktListe = benutzerkontaktDB.selectAllBenutzerkontakteForBenutzungsstatistik(benutzerkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());

		assertEquals(benutzerkontaktListe.get(0).getBenutzerkontakt_ID(), benutzerkontakt.getBenutzerkontakt_ID());
		assertEquals(benutzerkontaktListe.get(0).getTimestamp(), benutzerkontakt.getTimestamp());
		assertEquals(benutzerkontaktListe.get(0).getBenutzungsstatistik().getBenutzungsstatistik_ID(), benutzerkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testdeleteBenutzerkontakt() {
		List<Benutzerkontakt> benutzerkontaktListe = new ArrayList<Benutzerkontakt>();
		benutzerkontaktListe = benutzerkontaktDB.selectAllBenutzerkontakte();

		for(Benutzerkontakt k : benutzerkontaktListe) {
			benutzerkontaktDB.deleteBenutzerkontakt(k);
		}
		
		benutzerkontaktListe = benutzerkontaktDB.selectAllBenutzerkontakte();
		
		assertEquals(benutzerkontaktListe.size(), 0);
	}
}
