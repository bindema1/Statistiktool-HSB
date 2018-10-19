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
import benutzungsstatistik.db.IntensivfrageDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Intensivfrage;

/**
 * Testet alle Methoden der IntensivfrageDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestIntensivfrageDatenbank {

	IntensivfrageDatenbank intensivfrageDB = new IntensivfrageDatenbank();
	Intensivfrage intensivfrage;
	StandortDatenbank standortDB = new StandortDatenbank();
	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	
	@Before
	public void initComponents() {
		Standort standort = new Standort(StandortEnum.Test);
		standortDB.insertStandort(standort);
		Benutzungsstatistik benutzungsstatistik = new Benutzungsstatistik(new Date(), 8, true, standort);
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);
		
		intensivfrage = new Intensivfrage(Timestamp.valueOf("2018-10-10 10:10:10.0"), benutzungsstatistik);
	}

	@Test
	public void testinsertIntensivfrage() {
		intensivfrageDB.insertIntensivfrage(intensivfrage);
	}

	@Test
	public void testselectAllIntensivfragee() {
		intensivfrageDB.insertIntensivfrage(intensivfrage);
		
		List<Intensivfrage> intensivfrageListe = new ArrayList<Intensivfrage>();
		intensivfrageListe = intensivfrageDB.selectAllIntensivfragen();
		
		assertEquals(intensivfrageListe.get(intensivfrageListe.size()-1).getIntensivfrage_ID(), intensivfrage.getIntensivfrage_ID());
		assertEquals(intensivfrageListe.get(intensivfrageListe.size()-1).getTimestamp(), intensivfrage.getTimestamp());
		assertEquals(intensivfrageListe.get(intensivfrageListe.size()-1).getBenutzungsstatistik().getBenutzungsstatistik_ID(), intensivfrage.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testselectAllIntensivfrageeForBenutzungsstatistik() {
		intensivfrageDB.insertIntensivfrage(intensivfrage);
		
		List<Intensivfrage> intensivfrageListe = new ArrayList<Intensivfrage>();
		intensivfrageListe = intensivfrageDB.selectAllIntensivfragenForBenutzungsstatistik(intensivfrage.getBenutzungsstatistik().getBenutzungsstatistik_ID());

		assertEquals(intensivfrageListe.get(0).getIntensivfrage_ID(), intensivfrage.getIntensivfrage_ID());
		assertEquals(intensivfrageListe.get(0).getTimestamp(), intensivfrage.getTimestamp());
		assertEquals(intensivfrageListe.get(0).getBenutzungsstatistik().getBenutzungsstatistik_ID(), intensivfrage.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testdeleteIntensivfrage() {
		List<Intensivfrage> intensivfrageListe = new ArrayList<Intensivfrage>();
		intensivfrageListe = intensivfrageDB.selectAllIntensivfragen();

		for(Intensivfrage k : intensivfrageListe) {
			intensivfrageDB.deleteIntensivfrage(k);
		}
		
		intensivfrageListe = intensivfrageDB.selectAllIntensivfragen();
		
		assertEquals(intensivfrageListe.size(), 0);
	}
}
