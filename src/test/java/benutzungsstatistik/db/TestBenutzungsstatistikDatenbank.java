package benutzungsstatistik.db;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import allgemein.model.StandortEnum;
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
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date date;
	Wintikurier wintikurier1;
	
	@Before
	public void initComponents() {
		try {
			date = sdf.parse("2018-10-10");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		WintikurierDatenbank wintikurierDB = new WintikurierDatenbank();
		wintikurier1 = new Wintikurier(6, 2, 9, 5);
		wintikurierDB.insertWintikurier(wintikurier1);
		
		benutzungsstatistik = new Benutzungsstatistik(date, 8, true, StandortEnum.TEST, wintikurier1);
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
		
		Benutzungsstatistik b = benutzungsstatistikDB.selectBenutzungsstatistikForDateAndStandort(date, StandortEnum.TEST);

		assertEquals(b.getAnzahl_Rechercheberatung(), benutzungsstatistik.getAnzahl_Rechercheberatung());
		assertEquals(b.isKassenbeleg(), benutzungsstatistik.isKassenbeleg());
		assertEquals(sdf.format(b.getDatum()), sdf.format(benutzungsstatistik.getDatum()));
		assertEquals(b.getWintikurier().getAnzahl_Gesundheit(), wintikurier1.getAnzahl_Gesundheit());
	}
}
