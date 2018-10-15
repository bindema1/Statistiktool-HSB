package Allgemein.db;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Allgemein.db.StandortDatenbank;
import Allgemein.model.Standort;

/**
 * Testet alle Methoden der StandortDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestStandortDatenbank {

	StandortDatenbank standortDB = new StandortDatenbank();
	Standort standort;

	@Before
	public void initComponents() {
		standort = new Standort("Test Standort");
	}

	@Test
	public void testinsertStandort() {
		standortDB.insertStandort(standort);
	}

	@Test
	public void testselectAllStandorte() {
		standortDB.insertStandort(standort);
		
		List<Standort> standortListe = new ArrayList<Standort>();
		standortListe = standortDB.selectAllStandorte();
		
		System.out.println(standortListe.get(0).getName());

		assertEquals(standortListe.get(0).getName(), standort.getName());
	}
}
