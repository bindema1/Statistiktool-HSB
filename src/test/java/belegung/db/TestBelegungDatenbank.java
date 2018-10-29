package belegung.db;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import allgemein.model.StandortEnum;
import belegung.model.Arbeitsplätze;
import belegung.model.Belegung;
import belegung.model.Carrels;
import belegung.model.Gruppenräume;
import belegung.model.Kapazität;
import belegung.model.SektorA;
import belegung.model.SektorB;
import belegung.model.Stockwerk;
import belegung.model.StockwerkEnum;
import belegung.model.UhrzeitEnum;

/**
 * Testet alle Methoden der BenutzungsstatistikDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestBelegungDatenbank {

	BelegungsDatenbank2 belegungsDB = new BelegungsDatenbank2();
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	Date date;
	Belegung belegung;
	
	@Before
	public void initComponents() {
		date = new Date();
		
		try {
			date = sdf.parse("22.10.2018");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		belegung = new Belegung(date, StandortEnum.TEST);

		Kapazität kapazität = new Kapazität(StockwerkEnum.TEST, 80, 120, 135, 12, 24);
		Stockwerk stockwerk = new Stockwerk(StockwerkEnum.TEST, true, true, true, true, kapazität);
		Arbeitsplätze arbeitsplatz1 = new Arbeitsplätze(7, UhrzeitEnum.NEUN, stockwerk);
		Arbeitsplätze arbeitsplatz2 = new Arbeitsplätze(34, UhrzeitEnum.ELF, stockwerk);
		SektorA sektorA1 = new SektorA(34, UhrzeitEnum.NEUN, stockwerk);
		SektorA sektorA2 = new SektorA(56, UhrzeitEnum.ELF, stockwerk);
		SektorB sektorB1 = new SektorB(39, UhrzeitEnum.NEUN, stockwerk);
		SektorB sektorB2 = new SektorB(64, UhrzeitEnum.ELF, stockwerk);
		Gruppenräume gruppenräume1 = new Gruppenräume(5, 2, UhrzeitEnum.NEUN, stockwerk);
		Gruppenräume gruppenräume2 = new Gruppenräume(8, 4, UhrzeitEnum.ELF, stockwerk);
		Carrels carrels1 = new Carrels(4, 2, UhrzeitEnum.NEUN, stockwerk);
		Carrels carrels2 = new Carrels(5, 2, UhrzeitEnum.ELF, stockwerk);
		
		//Daten in die Objekte füllen
		belegung.addStockwerk(stockwerk);		
		stockwerk.addArbeitsplätze(arbeitsplatz1);
		stockwerk.addArbeitsplätze(arbeitsplatz2);		
		stockwerk.addSektorA(sektorA1);
		stockwerk.addSektorA(sektorA2);
		stockwerk.addGruppenräume(gruppenräume1);
		stockwerk.addGruppenräume(gruppenräume2);
		stockwerk.addCarrels(carrels1);
		stockwerk.addCarrels(carrels2);
	}
	
	@Test
	public void testinsertBelegungen() {
		belegungsDB.insertBelegung(belegung);
	}
	
	@Test
	public void testselectAllBelegungen() {
		belegungsDB.insertBelegung(belegung);
		
		List<Belegung> belegungsListe = new ArrayList<Belegung>();
		belegungsListe = belegungsDB.selectAllBelegungen();

		assertEquals(belegungsListe.get(1).getStandort(), belegung.getStandort());
		assertEquals(belegungsListe.get(1).getStockwerkListe().get(0).getName(), belegung.getStockwerkListe().get(0).getName());
		assertEquals(sdf.format(belegungsListe.get(1).getDatum()), sdf.format(belegung.getDatum()));
	}
	
	@Test
	public void testupdateBelegung() {
		belegungsDB.insertBelegung(belegung);
		Date dateTest = null;
		try {
			dateTest = sdf.parse("23.10.2018");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		belegung.setDatum(dateTest);
		belegungsDB.updateBelegung(belegung);
		
		List<Belegung> belegungsListe = new ArrayList<Belegung>();
		belegungsListe = belegungsDB.selectAllBelegungen();

		assertEquals(belegungsListe.get(0).getStandort(), belegung.getStandort());
		assertEquals(belegungsListe.get(0).getStockwerkListe().get(0).getName(), belegung.getStockwerkListe().get(0).getName());
		assertEquals(sdf.format(belegungsListe.get(0).getDatum()), sdf.format(belegung.getDatum()));
	}
	
	@Test
	public void testselectBenutzungsstatistikForDateAndStandort() {
		belegungsDB.insertBelegung(belegung);
		
		Belegung b = belegungsDB.selectBelegungForDateAndStandort(date, StandortEnum.TEST);

		assertEquals(b.getStandort(), belegung.getStandort());
		assertEquals(b.getStockwerkListe().get(0).getName(), belegung.getStockwerkListe().get(0).getName());
		assertEquals(sdf.format(b.getDatum()), sdf.format(belegung.getDatum()));
	}
	
}
