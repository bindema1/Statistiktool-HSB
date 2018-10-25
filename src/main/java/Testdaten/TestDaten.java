package Testdaten;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import allgemein.db.AngestellterDatenbank;
import allgemein.db.StandortDatenbank;
import allgemein.model.Angestellter;
import allgemein.model.Standort;
import allgemein.model.StandortEnum;
import belegung.db.BelegungsDatenbank2;
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
import benutzungsstatistik.db.BeantwortungBibliothekspersonalDatenbank;
import benutzungsstatistik.db.BenutzerkontaktDatenbank;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.db.EmailkontaktDatenbank;
import benutzungsstatistik.db.ExterneGruppeDatenbank;
import benutzungsstatistik.db.IntensivfrageDatenbank;
import benutzungsstatistik.db.TelefonkontaktDatenbank;
import benutzungsstatistik.db.WintikurierDatenbank;
import benutzungsstatistik.model.BeantwortungBibliothekspersonal;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.ExterneGruppe;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Telefonkontakt;
import benutzungsstatistik.model.Wintikurier;

/**
 * Klasse um alle Testdaten einzulesen
 * 
 * @author Marvin Bindemann
 *
 */
public class TestDaten {

	Standort standortBB;
	Standort standortLL;
	SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
	Date date;

	public TestDaten() {
		
		try {
			initAllgemein();
			initBenutzungsstatistik();
			initBelegung();
			System.out.println("TestDaten eingelesen");
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("Fehler");
		}
	}

	private void initAllgemein() throws ParseException {
		
		AngestellterDatenbank angestelltenDB = new AngestellterDatenbank();
		StandortDatenbank standortDB = new StandortDatenbank();
		
		standortBB = new Standort(StandortEnum.WINTERTHUR_BB);
		standortLL = new Standort(StandortEnum.WINTERTHUR_LL);
		Standort standortWädi = new Standort(StandortEnum.WÄDENSWIL);
		standortDB.insertStandort(standortBB);
		standortDB.insertStandort(standortLL);
		standortDB.insertStandort(standortWädi);

		SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
		Date date = sdf2.parse("10.10.2018");

		Angestellter angestellter1 = new Angestellter("Mitarbeiter Winterthur", "123", date, false, standortBB);
		Angestellter angestellter2 = new Angestellter("Studentische Mitarbeiter Winterthur", "123", date, false,
				standortLL);
		Angestellter angestellter3 = new Angestellter("Admin Winterthur", "123", date, true, standortBB);
		Angestellter angestellter4 = new Angestellter("Mitarbeiter Wädenswil", "123", date, false, standortWädi);
		Angestellter angestellter5 = new Angestellter("Admin Wädenswil", "123", date, true, standortWädi);
		angestelltenDB.insertAngestellter(angestellter1);
		angestelltenDB.insertAngestellter(angestellter2);
		angestelltenDB.insertAngestellter(angestellter3);
		angestelltenDB.insertAngestellter(angestellter4);
		angestelltenDB.insertAngestellter(angestellter5);
	}

	private void initBenutzungsstatistik() throws ParseException {

		EmailkontaktDatenbank emailKontaktDB = new EmailkontaktDatenbank();
		ExterneGruppeDatenbank externeGruppeDB = new ExterneGruppeDatenbank();
		BeantwortungBibliothekspersonalDatenbank beantwortungBibliothekspersonalDB = new BeantwortungBibliothekspersonalDatenbank();
		BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
		IntensivfrageDatenbank intensivFrageDB = new IntensivfrageDatenbank();
		BenutzerkontaktDatenbank benutzerKontaktDB = new BenutzerkontaktDatenbank();
		TelefonkontaktDatenbank telefonKontaktDB = new TelefonkontaktDatenbank();
		WintikurierDatenbank wintikurierDB = new WintikurierDatenbank();
		
		date = sdf2.parse("10.10.2018");

		Wintikurier wintikurier1 = new Wintikurier(6, 2, 9, 5);
		wintikurierDB.insertWintikurier(wintikurier1);

		Benutzungsstatistik benutzungsstatistik1 = new Benutzungsstatistik(date, 8, true, standortBB, wintikurier1);
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik1);

//		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datumheute = sdf.format(date);
		Timestamp timestamp8 = Timestamp.valueOf(datumheute + " 08:30:00.0");
		Timestamp timestamp9 = Timestamp.valueOf(datumheute + " 09:59:00.0");
		Timestamp timestamp10 = Timestamp.valueOf(datumheute + " 10:30:00.0");
		Timestamp timestamp11 = Timestamp.valueOf(datumheute + " 11:30:00.0");
		Timestamp timestamp12 = Timestamp.valueOf(datumheute + " 12:30:00.0");
		Timestamp timestamp13 = Timestamp.valueOf(datumheute + " 13:30:00.0");
		Timestamp timestamp14 = Timestamp.valueOf(datumheute + " 14:00:00.0");
		Timestamp timestamp15 = Timestamp.valueOf(datumheute + " 15:30:00.0");
		Timestamp timestamp16 = Timestamp.valueOf(datumheute + " 16:30:00.0");
		Timestamp timestamp17 = Timestamp.valueOf(datumheute + " 17:30:00.0");

		Benutzerkontakt benutzerkontakt1 = new Benutzerkontakt(timestamp8, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt2 = new Benutzerkontakt(timestamp9, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt3 = new Benutzerkontakt(timestamp9, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt4 = new Benutzerkontakt(timestamp9, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt5 = new Benutzerkontakt(timestamp14, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt6 = new Benutzerkontakt(timestamp15, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt7 = new Benutzerkontakt(timestamp16, benutzungsstatistik1);
		benutzerKontaktDB.insertBenutzerkontakt(benutzerkontakt1);
		benutzerKontaktDB.insertBenutzerkontakt(benutzerkontakt2);
		benutzerKontaktDB.insertBenutzerkontakt(benutzerkontakt3);
		benutzerKontaktDB.insertBenutzerkontakt(benutzerkontakt4);
		benutzerKontaktDB.insertBenutzerkontakt(benutzerkontakt5);
		benutzerKontaktDB.insertBenutzerkontakt(benutzerkontakt6);
		benutzerKontaktDB.insertBenutzerkontakt(benutzerkontakt7);

		Intensivfrage intensivfrage1 = new Intensivfrage(timestamp9, benutzungsstatistik1);
		Intensivfrage intensivfrage2 = new Intensivfrage(timestamp10, benutzungsstatistik1);
		Intensivfrage intensivfrage3 = new Intensivfrage(timestamp12, benutzungsstatistik1);
		Intensivfrage intensivfrage4 = new Intensivfrage(timestamp13, benutzungsstatistik1);
		Intensivfrage intensivfrage5 = new Intensivfrage(timestamp14, benutzungsstatistik1);
		Intensivfrage intensivfrage6 = new Intensivfrage(timestamp15, benutzungsstatistik1);
		Intensivfrage intensivfrage7 = new Intensivfrage(timestamp16, benutzungsstatistik1);
		Intensivfrage intensivfrage8 = new Intensivfrage(timestamp17, benutzungsstatistik1);
		intensivFrageDB.insertIntensivfrage(intensivfrage1);
		intensivFrageDB.insertIntensivfrage(intensivfrage2);
		intensivFrageDB.insertIntensivfrage(intensivfrage3);
		intensivFrageDB.insertIntensivfrage(intensivfrage4);
		intensivFrageDB.insertIntensivfrage(intensivfrage5);
		intensivFrageDB.insertIntensivfrage(intensivfrage6);
		intensivFrageDB.insertIntensivfrage(intensivfrage7);
		intensivFrageDB.insertIntensivfrage(intensivfrage8);

		Telefonkontakt telefonkontakt1 = new Telefonkontakt(timestamp8, benutzungsstatistik1);
		Telefonkontakt telefonkontakt2 = new Telefonkontakt(timestamp10, benutzungsstatistik1);
		Telefonkontakt telefonkontakt3 = new Telefonkontakt(timestamp12, benutzungsstatistik1);
		Telefonkontakt telefonkontakt4 = new Telefonkontakt(timestamp13, benutzungsstatistik1);
		Telefonkontakt telefonkontakt5 = new Telefonkontakt(timestamp14, benutzungsstatistik1);
		Telefonkontakt telefonkontakt6 = new Telefonkontakt(timestamp15, benutzungsstatistik1);
		Telefonkontakt telefonkontakt7 = new Telefonkontakt(timestamp16, benutzungsstatistik1);
		Telefonkontakt telefonkontakt8 = new Telefonkontakt(timestamp16, benutzungsstatistik1);
		Telefonkontakt telefonkontakt9 = new Telefonkontakt(timestamp17, benutzungsstatistik1);
		telefonKontaktDB.insertTelefonkontakt(telefonkontakt1);
		telefonKontaktDB.insertTelefonkontakt(telefonkontakt2);
		telefonKontaktDB.insertTelefonkontakt(telefonkontakt3);
		telefonKontaktDB.insertTelefonkontakt(telefonkontakt4);
		telefonKontaktDB.insertTelefonkontakt(telefonkontakt5);
		telefonKontaktDB.insertTelefonkontakt(telefonkontakt6);
		telefonKontaktDB.insertTelefonkontakt(telefonkontakt7);
		telefonKontaktDB.insertTelefonkontakt(telefonkontakt8);
		telefonKontaktDB.insertTelefonkontakt(telefonkontakt9);

		Emailkontakt emailkontakt1 = new Emailkontakt(timestamp8, benutzungsstatistik1);
		Emailkontakt emailkontakt2 = new Emailkontakt(timestamp9, benutzungsstatistik1);
		Emailkontakt emailkontakt3 = new Emailkontakt(timestamp11, benutzungsstatistik1);
		Emailkontakt emailkontakt4 = new Emailkontakt(timestamp11, benutzungsstatistik1);
		Emailkontakt emailkontakt5 = new Emailkontakt(timestamp13, benutzungsstatistik1);
		Emailkontakt emailkontakt6 = new Emailkontakt(timestamp13, benutzungsstatistik1);
		Emailkontakt emailkontakt7 = new Emailkontakt(timestamp14, benutzungsstatistik1);
		Emailkontakt emailkontakt8 = new Emailkontakt(timestamp15, benutzungsstatistik1);
		Emailkontakt emailkontakt9 = new Emailkontakt(timestamp17, benutzungsstatistik1);
		emailKontaktDB.insertEmailkontakt(emailkontakt1);
		emailKontaktDB.insertEmailkontakt(emailkontakt2);
		emailKontaktDB.insertEmailkontakt(emailkontakt3);
		emailKontaktDB.insertEmailkontakt(emailkontakt4);
		emailKontaktDB.insertEmailkontakt(emailkontakt5);
		emailKontaktDB.insertEmailkontakt(emailkontakt6);
		emailKontaktDB.insertEmailkontakt(emailkontakt7);
		emailKontaktDB.insertEmailkontakt(emailkontakt8);
		emailKontaktDB.insertEmailkontakt(emailkontakt9);

		BeantwortungBibliothekspersonal beantwortungBibliothekspersonal1 = new BeantwortungBibliothekspersonal(
				timestamp8, benutzungsstatistik1);
		beantwortungBibliothekspersonalDB.insertBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal1);

		ExterneGruppe externeGruppe1 = new ExterneGruppe("Test Gruppe", 10, benutzungsstatistik1);
		ExterneGruppe externeGruppe2 = new ExterneGruppe("Winterthur Stadtführung", 14, benutzungsstatistik1);
		ExterneGruppe externeGruppe3 = new ExterneGruppe("Fussballverein Winterthur", 7, benutzungsstatistik1);
		externeGruppeDB.insertExterneGruppe(externeGruppe1);
		externeGruppeDB.insertExterneGruppe(externeGruppe2);
		externeGruppeDB.insertExterneGruppe(externeGruppe3);

	}

	private void initBelegung() throws ParseException {

	    BelegungsDatenbank2 belegungDB = new BelegungsDatenbank2();
		
//		date = sdf2.parse("22.10.2018");
	    date = new Date();

		Belegung belegungBB = new Belegung(date, standortBB);
		Belegung belegungLL = new Belegung(date, standortLL);

		Kapazität kapazitätEG = new Kapazität(StockwerkEnum.EG, 80, 0, 0, 0, 12);
		Kapazität kapazität1ZG = new Kapazität(StockwerkEnum.ZG1, 80, 0, 0, 0, 0);
		Kapazität kapazität2ZG = new Kapazität(StockwerkEnum.ZG2, 160, 0, 0, 0, 0);
		Kapazität kapazitätLL = new Kapazität(StockwerkEnum.LL, 0, 120, 135, 12, 24);

		Stockwerk stockwerkEG = new Stockwerk(StockwerkEnum.EG, false, false, true, false, kapazitätEG);
		Stockwerk stockwerk1ZG = new Stockwerk(StockwerkEnum.ZG1, false, false, false, false, kapazität1ZG);
		Stockwerk stockwerk2ZG = new Stockwerk(StockwerkEnum.ZG2, false, false, false, false, kapazität2ZG);
		Stockwerk stockwerkLL = new Stockwerk(StockwerkEnum.LL, true, true, true, true, kapazitätLL);

		// Daten EG
		Arbeitsplätze arbeitsplatzEG1 = new Arbeitsplätze(6, UhrzeitEnum.NEUN, stockwerkEG);
		Arbeitsplätze arbeitsplatzEG2 = new Arbeitsplätze(22, UhrzeitEnum.ELF, stockwerkEG);
		Arbeitsplätze arbeitsplatzEG3 = new Arbeitsplätze(45, UhrzeitEnum.DREIZEHN, stockwerkEG);
		Arbeitsplätze arbeitsplatzEG4 = new Arbeitsplätze(61, UhrzeitEnum.FÜNFZEHN, stockwerkEG);
		Arbeitsplätze arbeitsplatzEG5 = new Arbeitsplätze(32, UhrzeitEnum.SIEBZEHN, stockwerkEG);
		Arbeitsplätze arbeitsplatzEG6 = new Arbeitsplätze(0, UhrzeitEnum.NEUNZEHN, stockwerkEG);
		Gruppenräume gruppenräumeEG1 = new Gruppenräume(4, 2, UhrzeitEnum.NEUN, stockwerkEG);
		Gruppenräume gruppenräumeEG2 = new Gruppenräume(5, 2, UhrzeitEnum.ELF, stockwerkEG);
		Gruppenräume gruppenräumeEG3 = new Gruppenräume(7, 3, UhrzeitEnum.DREIZEHN, stockwerkEG);
		Gruppenräume gruppenräumeEG4 = new Gruppenräume(12, 4, UhrzeitEnum.FÜNFZEHN, stockwerkEG);
		Gruppenräume gruppenräumeEG5 = new Gruppenräume(8, 4, UhrzeitEnum.SIEBZEHN, stockwerkEG);
		Gruppenräume gruppenräumeEG6 = new Gruppenräume(2, 1, UhrzeitEnum.NEUNZEHN, stockwerkEG);

		// Daten 1.ZG
		Arbeitsplätze arbeitsplatz1ZG1 = new Arbeitsplätze(2, UhrzeitEnum.NEUN, stockwerk1ZG);
		Arbeitsplätze arbeitsplatz1ZG2 = new Arbeitsplätze(12, UhrzeitEnum.ELF, stockwerk1ZG);
		Arbeitsplätze arbeitsplatz1ZG3 = new Arbeitsplätze(33, UhrzeitEnum.DREIZEHN, stockwerk1ZG);
		Arbeitsplätze arbeitsplatz1ZG4 = new Arbeitsplätze(39, UhrzeitEnum.FÜNFZEHN, stockwerk1ZG);
		Arbeitsplätze arbeitsplatz1ZG5 = new Arbeitsplätze(23, UhrzeitEnum.SIEBZEHN, stockwerk1ZG);
		Arbeitsplätze arbeitsplatz1ZG6 = new Arbeitsplätze(4, UhrzeitEnum.NEUNZEHN, stockwerk1ZG);

		// Daten 2.ZG
		Arbeitsplätze arbeitsplatz2ZG1 = new Arbeitsplätze(7, UhrzeitEnum.NEUN, stockwerk2ZG);
		Arbeitsplätze arbeitsplatz2ZG2 = new Arbeitsplätze(34, UhrzeitEnum.ELF, stockwerk2ZG);
		Arbeitsplätze arbeitsplatz2ZG3 = new Arbeitsplätze(67, UhrzeitEnum.DREIZEHN, stockwerk2ZG);
		Arbeitsplätze arbeitsplatz2ZG4 = new Arbeitsplätze(122, UhrzeitEnum.FÜNFZEHN, stockwerk2ZG);
		Arbeitsplätze arbeitsplatz2ZG5 = new Arbeitsplätze(78, UhrzeitEnum.SIEBZEHN, stockwerk2ZG);
		Arbeitsplätze arbeitsplatz2ZG6 = new Arbeitsplätze(35, UhrzeitEnum.NEUNZEHN, stockwerk2ZG);

		// Daten LL
		SektorA sektorA1 = new SektorA(34, UhrzeitEnum.NEUN, stockwerkLL);
		SektorA sektorA2 = new SektorA(56, UhrzeitEnum.ELF, stockwerkLL);
		SektorA sektorA3 = new SektorA(99, UhrzeitEnum.DREIZEHN, stockwerkLL);
		SektorA sektorA4 = new SektorA(108, UhrzeitEnum.FÜNFZEHN, stockwerkLL);
		SektorA sektorA5 = new SektorA(76, UhrzeitEnum.SIEBZEHN, stockwerkLL);
		SektorA sektorA6 = new SektorA(42, UhrzeitEnum.NEUNZEHN, stockwerkLL);
		SektorB sektorB1 = new SektorB(39, UhrzeitEnum.NEUN, stockwerkLL);
		SektorB sektorB2 = new SektorB(64, UhrzeitEnum.ELF, stockwerkLL);
		SektorB sektorB3 = new SektorB(110, UhrzeitEnum.DREIZEHN, stockwerkLL);
		SektorB sektorB4 = new SektorB(131, UhrzeitEnum.FÜNFZEHN, stockwerkLL);
		SektorB sektorB5 = new SektorB(94, UhrzeitEnum.SIEBZEHN, stockwerkLL);
		SektorB sektorB6 = new SektorB(51, UhrzeitEnum.NEUNZEHN, stockwerkLL);
		Gruppenräume gruppenräumeLL1 = new Gruppenräume(5, 2, UhrzeitEnum.NEUN, stockwerkLL);
		Gruppenräume gruppenräumeLL2 = new Gruppenräume(8, 4, UhrzeitEnum.ELF, stockwerkLL);
		Gruppenräume gruppenräumeLL3 = new Gruppenräume(14, 6, UhrzeitEnum.DREIZEHN, stockwerkLL);
		Gruppenräume gruppenräumeLL4 = new Gruppenräume(21, 7, UhrzeitEnum.FÜNFZEHN, stockwerkLL);
		Gruppenräume gruppenräumeLL5 = new Gruppenräume(5, 3, UhrzeitEnum.SIEBZEHN, stockwerkLL);
		Gruppenräume gruppenräumeLL6 = new Gruppenräume(3, 1, UhrzeitEnum.NEUNZEHN, stockwerkLL);
		Carrels carrelsLL1 = new Carrels(4, 2, UhrzeitEnum.NEUN, stockwerkLL);
		Carrels carrelsLL2 = new Carrels(5, 2, UhrzeitEnum.ELF, stockwerkLL);
		Carrels carrelsLL3 = new Carrels(7, 3, UhrzeitEnum.DREIZEHN, stockwerkLL);
		Carrels carrelsLL4 = new Carrels(12, 4, UhrzeitEnum.FÜNFZEHN, stockwerkLL);
		Carrels carrelsLL5 = new Carrels(8, 4, UhrzeitEnum.SIEBZEHN, stockwerkLL);
		Carrels carrelsLL6 = new Carrels(2, 1, UhrzeitEnum.NEUNZEHN, stockwerkLL);
		
		
		//Daten in die Objekte füllen
		belegungBB.addStockwerk(stockwerkEG);
		belegungBB.addStockwerk(stockwerk1ZG);
		belegungBB.addStockwerk(stockwerk2ZG);
		belegungLL.addStockwerk(stockwerkLL);
		
		stockwerkEG.addArbeitsplätze(arbeitsplatzEG1);
		stockwerkEG.addArbeitsplätze(arbeitsplatzEG2);
		stockwerkEG.addArbeitsplätze(arbeitsplatzEG3);
		stockwerkEG.addArbeitsplätze(arbeitsplatzEG4);
		stockwerkEG.addArbeitsplätze(arbeitsplatzEG5);
		stockwerkEG.addArbeitsplätze(arbeitsplatzEG6);
		stockwerkEG.addGruppenräume(gruppenräumeEG1);
		stockwerkEG.addGruppenräume(gruppenräumeEG2);
		stockwerkEG.addGruppenräume(gruppenräumeEG3);
		stockwerkEG.addGruppenräume(gruppenräumeEG4);
		stockwerkEG.addGruppenräume(gruppenräumeEG5);
		stockwerkEG.addGruppenräume(gruppenräumeEG6);
		
		stockwerk1ZG.addArbeitsplätze(arbeitsplatz1ZG1);
		stockwerk1ZG.addArbeitsplätze(arbeitsplatz1ZG2);
		stockwerk1ZG.addArbeitsplätze(arbeitsplatz1ZG3);
		stockwerk1ZG.addArbeitsplätze(arbeitsplatz1ZG4);
		stockwerk1ZG.addArbeitsplätze(arbeitsplatz1ZG5);
		stockwerk1ZG.addArbeitsplätze(arbeitsplatz1ZG6);
		
		stockwerk2ZG.addArbeitsplätze(arbeitsplatz2ZG1);
		stockwerk2ZG.addArbeitsplätze(arbeitsplatz2ZG2);
		stockwerk2ZG.addArbeitsplätze(arbeitsplatz2ZG3);
		stockwerk2ZG.addArbeitsplätze(arbeitsplatz2ZG4);
		stockwerk2ZG.addArbeitsplätze(arbeitsplatz2ZG5);
		stockwerk2ZG.addArbeitsplätze(arbeitsplatz2ZG6);
		
		stockwerkLL.addSektorA(sektorA1);
		stockwerkLL.addSektorA(sektorA2);
		stockwerkLL.addSektorA(sektorA3);
		stockwerkLL.addSektorA(sektorA4);
		stockwerkLL.addSektorA(sektorA5);
		stockwerkLL.addSektorA(sektorA6);
		stockwerkLL.addSektorB(sektorB1);
		stockwerkLL.addSektorB(sektorB2);
		stockwerkLL.addSektorB(sektorB3);
		stockwerkLL.addSektorB(sektorB4);
		stockwerkLL.addSektorB(sektorB5);
		stockwerkLL.addSektorB(sektorB6);
		stockwerkLL.addGruppenräume(gruppenräumeLL1);
		stockwerkLL.addGruppenräume(gruppenräumeLL2);
		stockwerkLL.addGruppenräume(gruppenräumeLL3);
		stockwerkLL.addGruppenräume(gruppenräumeLL4);
		stockwerkLL.addGruppenräume(gruppenräumeLL5);
		stockwerkLL.addGruppenräume(gruppenräumeLL6);
		stockwerkLL.addCarrels(carrelsLL1);
		stockwerkLL.addCarrels(carrelsLL2);
		stockwerkLL.addCarrels(carrelsLL3);
		stockwerkLL.addCarrels(carrelsLL4);
		stockwerkLL.addCarrels(carrelsLL5);
		stockwerkLL.addCarrels(carrelsLL6);
		
		
		belegungDB.insertBelegung(belegungBB);
		belegungDB.insertBelegung(belegungLL);
		
		
		for(Belegung belegung : belegungDB.selectAllBelegungen()) {
			System.out.println(belegung.getBelegungs_ID());
			for(Stockwerk stockwerk : belegung.getStockwerkListe()) {
				System.out.println(stockwerk.getName());
				for(Arbeitsplätze a : stockwerk.getArbeitsplatzListe()) {
					System.out.println(a.getAnzahlPersonen() + "   " +a.getUhrzeit());
				}
				for(SektorA a : stockwerk.getSektorAListe()) {
					System.out.println(a.getAnzahlPersonen() + "   " +a.getUhrzeit());
				}
				for(SektorB a : stockwerk.getSektorBListe()) {
					System.out.println(a.getAnzahlPersonen() + "   " +a.getUhrzeit());
				}
				for(Gruppenräume a : stockwerk.getGruppenräumeListe()) {
					System.out.println(a.getAnzahlPersonen() + "   " +a.getAnzahlRäume() +"   " +a.getUhrzeit());
				}
				for(Carrels a : stockwerk.getCarrelsListe()) {
					System.out.println(a.getAnzahlPersonen() + "   " +a.getAnzahlRäume() +"   " +a.getUhrzeit());
				}
			}
		}
	}

	public static void main(String[] args) {
		new TestDaten();
	}

}
