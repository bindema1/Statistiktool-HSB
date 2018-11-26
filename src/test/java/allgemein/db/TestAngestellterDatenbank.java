package allgemein.db;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import allgemein.db.AngestellterDatenbank;
import allgemein.model.Angestellter;
import allgemein.model.StandortEnum;
/**
 * Testet alle Methoden der AngestellterDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestAngestellterDatenbank {

	AngestellterDatenbank angestellterDB = new AngestellterDatenbank();
	Angestellter angestellter;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Before
	public void initComponents() {
		Date date = new Date();
		angestellter = new Angestellter("Hans Müller", "123", date, true, StandortEnum.TEST);
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
		assertEquals(angestellterListe.get(0).getStandort(), angestellter.getStandort());
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
	
	@Test
	public void testgetAngestellterByName() {
		angestellter = new Angestellter("Peter Meier", "123", new Date(), true, StandortEnum.TEST);
		angestellterDB.insertAngestellter(angestellter);
		Angestellter testAngestellter = angestellterDB.getAngestellterByName("Peter Meier");

		assertEquals(testAngestellter.getName(), angestellter.getName());
		assertEquals(testAngestellter.getPasswort(), angestellter.getPasswort());
	}
}
