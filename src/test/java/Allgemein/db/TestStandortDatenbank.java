package allgemein.db;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import allgemein.db.StandortDatenbank;
import allgemein.model.Standort;
import allgemein.model.StandortEnum;

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
		standort = new Standort(StandortEnum.TEST);
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
		
		assertEquals(standortListe.get(0).getName(), standort.getName());
	}
	
	@Test
	public void testgetStandort() {
		standortDB.insertStandort(standort);
		
		Standort s = standortDB.getStandort(StandortEnum.TEST);
		
		assertEquals(s.getName(), StandortEnum.TEST);
	}
	
}
