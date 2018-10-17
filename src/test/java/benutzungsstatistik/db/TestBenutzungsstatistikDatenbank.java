package benutzungsstatistik.db;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import allgemein.db.StandortDatenbank;
import allgemein.model.Standort;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Wintikurier;

/**
 * Testet alle Methoden der BenutzungsstatistikDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestBenutzungsstatistikDatenbank {

	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	Benutzungsstatistik benutzungsstatistik;
	Standort standort = new Standort("Test Standort");
	StandortDatenbank standortDB = new StandortDatenbank();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date date;
	
	@Before
	public void initComponents() {
		date = new Date();
		standortDB.insertStandort(standort);
		
		WintikurierDatenbank wintikurierDB = new WintikurierDatenbank();
		Wintikurier wintikurier1 = new Wintikurier(6, 2, 9, 5);
		wintikurierDB.insertWintikurier(wintikurier1);
		
		benutzungsstatistik = new Benutzungsstatistik(date, 8, true, standort, wintikurier1);
	}

	@Test
	public void testinsertBenutzungsstatistik() {
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);
	}

	@Test
	public void testselectAllBenutzungsstatistiken() {
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);
		
		List<Benutzungsstatistik> benutzungsstatistikListe = new ArrayList<Benutzungsstatistik>();
		benutzungsstatistikListe = benutzungsstatistikDB.selectAllBenutzungsstatistiken();

		assertEquals(benutzungsstatistikListe.get(0).getAnzahl_Rechercheberatung(), benutzungsstatistik.getAnzahl_Rechercheberatung());
		assertEquals(benutzungsstatistikListe.get(0).isKassenbeleg(), benutzungsstatistik.isKassenbeleg());
		assertEquals(sdf.format(benutzungsstatistikListe.get(0).getDatum()), sdf.format(benutzungsstatistik.getDatum()));
		assertEquals(benutzungsstatistikListe.get(0).getStandort().getStandort_ID(), 1);
		assertEquals(benutzungsstatistikListe.get(0).getWintikurier().getAnzahl_Gesundheit(), 6);
	}
	
	@Test
	public void testupdateBenutzungsstatistik() {
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);
		benutzungsstatistik.setKassenbeleg(false);
		benutzungsstatistik.setAnzahl_Rechercheberatung(7);
		benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
		
		List<Benutzungsstatistik> benutzungsstatistikListe = new ArrayList<Benutzungsstatistik>();
		benutzungsstatistikListe = benutzungsstatistikDB.selectAllBenutzungsstatistiken();

		assertEquals(benutzungsstatistikListe.get(1).isKassenbeleg(), benutzungsstatistik.isKassenbeleg());
		assertEquals(benutzungsstatistikListe.get(1).getAnzahl_Rechercheberatung(), benutzungsstatistik.getAnzahl_Rechercheberatung());
		assertEquals(benutzungsstatistikListe.get(1).getWintikurier().getAnzahl_Gesundheit(), 6);
	}
	
	@Test
	public void testselectBenutzungsstatistikForDateAndStandort() {
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);
		
		Benutzungsstatistik b = benutzungsstatistikDB.selectBenutzungsstatistikForDateAndStandort(date, standort);

		assertEquals(b.getAnzahl_Rechercheberatung(), benutzungsstatistik.getAnzahl_Rechercheberatung());
		assertEquals(b.isKassenbeleg(), benutzungsstatistik.isKassenbeleg());
		assertEquals(sdf.format(b.getDatum()), sdf.format(benutzungsstatistik.getDatum()));
		assertEquals(b.getStandort().getStandort_ID(), 4);
		assertEquals(b.getWintikurier().getAnzahl_Gesundheit(), 6);
	}
}
