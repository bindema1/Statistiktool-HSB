package benutzungsstatistik.db;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import allgemein.db.StandortDatenbank;
import allgemein.model.Standort;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.db.WintikurierDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Wintikurier;

/**
 * Testet alle Methoden der WintikurierDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestWintikurierDatenbank {

	WintikurierDatenbank wintikurierDB = new WintikurierDatenbank();
	Wintikurier wintikurier;
	StandortDatenbank standortDB = new StandortDatenbank();
	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();

	@Before
	public void initComponents() {
		Standort standort = new Standort("Test Standort");
		standortDB.insertStandort(standort);
		
		wintikurier = new Wintikurier(5, 7, 2, 10);
		
		Benutzungsstatistik benutzungsstatistik = new Benutzungsstatistik(new Date(), 8, true, standort, wintikurier);
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);

	}

	@Test
	public void testinsertWintikurier() {
		wintikurierDB.insertWintikurier(wintikurier);
	}

	@Test
	public void testselectAllWintikuriere() {
		wintikurierDB.insertWintikurier(wintikurier);

		List<Wintikurier> wintikurierListe = new ArrayList<Wintikurier>();
		wintikurierListe = wintikurierDB.selectAllWintikuriere();

		assertEquals(wintikurierListe.get(wintikurierListe.size() - 1).getWintikurier_ID(),
				wintikurier.getWintikurier_ID());
		assertEquals(wintikurierListe.get(wintikurierListe.size() - 1).getAnzahl_Linguistik(), wintikurier.getAnzahl_Linguistik());
		assertEquals(wintikurierListe.get(wintikurierListe.size() - 1).getAnzahl_Technik(),	wintikurier.getAnzahl_Technik());
		assertEquals(wintikurierListe.get(wintikurierListe.size() - 1).getAnzahl_Wirtschaft(),	wintikurier.getAnzahl_Wirtschaft());
		assertEquals(wintikurierListe.get(wintikurierListe.size() - 1).getAnzahl_Gesundheit(),	wintikurier.getAnzahl_Gesundheit());
	}

	@Test
	public void testupdateWintikurier() {
		wintikurierDB.insertWintikurier(wintikurier);
		wintikurier.setAnzahl_Linguistik(2);
		wintikurier.setAnzahl_Technik(3);
		wintikurier.setAnzahl_Gesundheit(4);
		wintikurier.setAnzahl_Wirtschaft(9);
		wintikurierDB.updateWintikurier(wintikurier);

		List<Wintikurier> wintikurierListe = new ArrayList<Wintikurier>();
		wintikurierListe = wintikurierDB.selectAllWintikuriere();

		assertEquals(wintikurierListe.get(wintikurierListe.size() - 1).getAnzahl_Linguistik(), wintikurier.getAnzahl_Linguistik());
		assertEquals(wintikurierListe.get(wintikurierListe.size() - 1).getAnzahl_Technik(),	wintikurier.getAnzahl_Technik());
		assertEquals(wintikurierListe.get(wintikurierListe.size() - 1).getAnzahl_Wirtschaft(),	wintikurier.getAnzahl_Wirtschaft());
		assertEquals(wintikurierListe.get(wintikurierListe.size() - 1).getAnzahl_Gesundheit(),	wintikurier.getAnzahl_Gesundheit());
	}
}
