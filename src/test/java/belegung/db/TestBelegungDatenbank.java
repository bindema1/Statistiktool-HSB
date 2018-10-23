package belegung.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;

import allgemein.db.StandortDatenbank;
import allgemein.model.Standort;
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

	Standort standort = new Standort(StandortEnum.TEST);
	StandortDatenbank standortDB = new StandortDatenbank();
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	Date date;
	Belegung belegung;
	
	@Before
	public void initComponents() {
		date = new Date();
		standortDB.insertStandort(standort);
		
		try {
			date = sdf.parse("22.10.2018");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		belegung = new Belegung(date, standort);

		Kapazität kapazität = new Kapazität(80, 120, 135, 12, 24);
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

	
}
