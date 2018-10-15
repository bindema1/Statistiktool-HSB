package Allgemein.db;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Allgemein.db.AngestellterDatenbank;
import Allgemein.db.StandortDatenbank;
import Allgemein.model.Angestellter;
import Allgemein.model.Standort;
/**
 * Testet alle Methoden der AngestellterDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestAngestellterDatenbank {

	AngestellterDatenbank angestellterDB = new AngestellterDatenbank();
	Angestellter angestellter;
	Standort standort = new Standort("Test Standort");
	StandortDatenbank standortDB = new StandortDatenbank();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Before
	public void initComponents() {
		Date date = new Date();
		standortDB.insertStandort(standort);
		angestellter = new Angestellter("Hans Müller", "123", date, true, standortDB.selectAllStandorte().get(0));
	}

	@Test
	public void testinsertAngestellter() {
		angestellterDB.insertAngestellter(angestellter);
	}

	@Test
	public void testselectAllAngestellte() {
		angestellterDB.insertAngestellter(angestellter);
		
		List<Angestellter> angestellterListe = new ArrayList<Angestellter>();
		angestellterListe = angestellterDB.selectAllAngestellte();

		assertEquals(angestellterListe.get(0).getName(), angestellter.getName());
		assertEquals(angestellterListe.get(0).getPasswort(), angestellter.getPasswort());
		assertEquals(sdf.format(angestellterListe.get(0).getPasswort_datum()), sdf.format(angestellter.getPasswort_datum()));
		assertEquals(angestellterListe.get(0).getStandort().getStandort_ID(), angestellter.getStandort().getStandort_ID());
	}
	
	@Test
	public void testupdateAngestellter() {
		angestellterDB.insertAngestellter(angestellter);
		angestellter.setName("Hansi Müller");
		angestellter.setPasswort("567");
		angestellterDB.updateAngestellter(angestellter);
		
		List<Angestellter> angestellterListe = new ArrayList<Angestellter>();
		angestellterListe = angestellterDB.selectAllAngestellte();

		assertEquals(angestellterListe.get(2).getName(), angestellter.getName());
		assertEquals(angestellterListe.get(2).getPasswort(), angestellter.getPasswort());
	}
}
