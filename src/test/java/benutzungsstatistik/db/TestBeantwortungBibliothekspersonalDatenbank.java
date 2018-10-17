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
import benutzungsstatistik.db.BeantwortungBibliothekspersonalDatenbank;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.BeantwortungBibliothekspersonal;
import benutzungsstatistik.model.Benutzungsstatistik;

/**
 * Testet alle Methoden der BeantwortungBibliothekspersonalDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestBeantwortungBibliothekspersonalDatenbank {

	BeantwortungBibliothekspersonalDatenbank beantwortungBibliothekspersonalDB = new BeantwortungBibliothekspersonalDatenbank();
	BeantwortungBibliothekspersonal beantwortungBibliothekspersonal;
	StandortDatenbank standortDB = new StandortDatenbank();
	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	
	@Before
	public void initComponents() {
		Standort standort = new Standort("Test Standort");
		standortDB.insertStandort(standort);
		Benutzungsstatistik benutzungsstatistik = new Benutzungsstatistik(new Date(), 8, true, standort);
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);
		
		beantwortungBibliothekspersonal = new BeantwortungBibliothekspersonal(Timestamp.valueOf("2018-10-10 10:10:10.0"), benutzungsstatistik);
	}

	@Test
	public void testinsertBeantwortungBibliothekspersonal() {
		beantwortungBibliothekspersonalDB.insertBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal);
	}

	@Test
	public void testselectAllBeantwortungBibliothekspersonale() {
		beantwortungBibliothekspersonalDB.insertBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal);
		
		List<BeantwortungBibliothekspersonal> beantwortungBibliothekspersonalListe = new ArrayList<BeantwortungBibliothekspersonal>();
		beantwortungBibliothekspersonalListe = beantwortungBibliothekspersonalDB.selectAllBeantwortungBibliothekspersonalen();
		
		assertEquals(beantwortungBibliothekspersonalListe.get(beantwortungBibliothekspersonalListe.size()-1).getBeantwortungBibliothekspersonal_ID(), beantwortungBibliothekspersonal.getBeantwortungBibliothekspersonal_ID());
		assertEquals(beantwortungBibliothekspersonalListe.get(beantwortungBibliothekspersonalListe.size()-1).getTimestamp(), beantwortungBibliothekspersonal.getTimestamp());
		assertEquals(beantwortungBibliothekspersonalListe.get(beantwortungBibliothekspersonalListe.size()-1).getBenutzungsstatistik().getBenutzungsstatistik_ID(), beantwortungBibliothekspersonal.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testselectAllBeantwortungBibliothekspersonaleForBenutzungsstatistik() {
		beantwortungBibliothekspersonalDB.insertBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal);
		
		List<BeantwortungBibliothekspersonal> beantwortungBibliothekspersonalListe = new ArrayList<BeantwortungBibliothekspersonal>();
		beantwortungBibliothekspersonalListe = beantwortungBibliothekspersonalDB.selectAllBeantwortungBibliothekspersonalenForBenutzungsstatistik(beantwortungBibliothekspersonal.getBenutzungsstatistik().getBenutzungsstatistik_ID());

		assertEquals(beantwortungBibliothekspersonalListe.get(0).getBeantwortungBibliothekspersonal_ID(), beantwortungBibliothekspersonal.getBeantwortungBibliothekspersonal_ID());
		assertEquals(beantwortungBibliothekspersonalListe.get(0).getTimestamp(), beantwortungBibliothekspersonal.getTimestamp());
		assertEquals(beantwortungBibliothekspersonalListe.get(0).getBenutzungsstatistik().getBenutzungsstatistik_ID(), beantwortungBibliothekspersonal.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}
	
	@Test
	public void testdeleteBeantwortungBibliothekspersonal() {
		List<BeantwortungBibliothekspersonal> beantwortungBibliothekspersonalListe = new ArrayList<BeantwortungBibliothekspersonal>();
		beantwortungBibliothekspersonalListe = beantwortungBibliothekspersonalDB.selectAllBeantwortungBibliothekspersonalen();

		for(BeantwortungBibliothekspersonal k : beantwortungBibliothekspersonalListe) {
			beantwortungBibliothekspersonalDB.deleteBeantwortungBibliothekspersonal(k);
		}
		
		beantwortungBibliothekspersonalListe = beantwortungBibliothekspersonalDB.selectAllBeantwortungBibliothekspersonalen();
		
		assertEquals(beantwortungBibliothekspersonalListe.size(), 0);
	}
}
