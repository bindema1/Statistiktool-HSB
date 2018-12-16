package datenimport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import allgemein.db.AngestellterDatenbank;
import allgemein.model.Angestellter;
import allgemein.model.StandortEnum;
import belegung.db.BelegungsDatenbank;
import belegung.model.Belegung;
import belegung.model.Kapazität;
import belegung.model.Stockwerk;
import belegung.model.StockwerkEnum;

public class DatenbankAufsetzen {

	public DatenbankAufsetzen() {
//		userUndKapazitätAufsetzen();
//		new BelegungsImporter();
	}

	/**
	 * Setzt die User und Kapazitäten auf
	 */
	private void userUndKapazitätAufsetzen() {

		SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
		Date date = null;
		try {
			date = sdf2.parse("01.01.2015");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		AngestellterDatenbank angestelltenDB = new AngestellterDatenbank();

		Angestellter angestellter1 = new Angestellter("Mitarbeitende Winterthur", "123", date, false,
				StandortEnum.WINTERTHUR_BB);
		Angestellter angestellter2 = new Angestellter("Studentische Mitarbeitende Winterthur", "123", date, false,
				StandortEnum.WINTERTHUR_LL);
		Angestellter angestellter3 = new Angestellter("Admin Winterthur", "123", date, true,
				StandortEnum.WINTERTHUR_BB);
		Angestellter angestellter4 = new Angestellter("Mitarbeitende Wädenswil", "123", date, false,
				StandortEnum.WÄDENSWIL);
		Angestellter angestellter5 = new Angestellter("Admin Wädenswil", "123", date, true,
				StandortEnum.WÄDENSWIL);
		angestelltenDB.insertAngestellter(angestellter1);
		angestelltenDB.insertAngestellter(angestellter2);
		angestelltenDB.insertAngestellter(angestellter3);
		angestelltenDB.insertAngestellter(angestellter4);
		angestelltenDB.insertAngestellter(angestellter5);
		
		
		BelegungsDatenbank belegungDB = new BelegungsDatenbank();

		Belegung belegungBB = new Belegung(date, StandortEnum.WINTERTHUR_BB);
		Belegung belegungLL = new Belegung(date, StandortEnum.WINTERTHUR_LL);
		Belegung belegungW = new Belegung(date, StandortEnum.WÄDENSWIL);

		Kapazität kapazitätEG = new Kapazität(StockwerkEnum.EG, 80, 0, 0, 0, 0, 3, 12, 100, 0, 0, 0, 0, 3, 25);
		Kapazität kapazität1ZG = new Kapazität(StockwerkEnum.ZG1, 80, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0);
		Kapazität kapazität2ZG = new Kapazität(StockwerkEnum.ZG2, 160, 0, 0, 0, 0, 0, 0, 190, 0, 0, 0, 0, 0, 0);
		Kapazität kapazitätLL = new Kapazität(StockwerkEnum.LL, 0, 120, 135, 12, 12, 8, 24, 0, 160, 150, 12, 25, 8, 45);
		Kapazität kapazitätW = new Kapazität(StockwerkEnum.WÄDI, 91, 0, 0, 0, 0, 0, 0, 130, 0, 0, 0, 0, 0, 0);

		Stockwerk stockwerkEG = new Stockwerk(StockwerkEnum.EG, false, false, true, false, kapazitätEG);
		Stockwerk stockwerk1ZG = new Stockwerk(StockwerkEnum.ZG1, false, false, false, false, kapazität1ZG);
		Stockwerk stockwerk2ZG = new Stockwerk(StockwerkEnum.ZG2, false, false, false, false, kapazität2ZG);
		Stockwerk stockwerkLL = new Stockwerk(StockwerkEnum.LL, true, true, true, true, kapazitätLL);
		Stockwerk stockwerkW = new Stockwerk(StockwerkEnum.WÄDI, false, false, false, false, kapazitätW);

		// Daten in die Objekte füllen
		belegungBB.addStockwerk(stockwerkEG);
		belegungBB.addStockwerk(stockwerk1ZG);
		belegungBB.addStockwerk(stockwerk2ZG);
		belegungLL.addStockwerk(stockwerkLL);
		belegungW.addStockwerk(stockwerkW);
		
		belegungDB.insertBelegung(belegungBB);
		belegungDB.insertBelegung(belegungLL);
		belegungDB.insertBelegung(belegungW);
	}
}
