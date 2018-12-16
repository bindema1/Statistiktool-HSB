package datenimport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import allgemein.db.AngestellterDatenbank;
import allgemein.model.Angestellter;
import allgemein.model.StandortEnum;
import belegung.db.BelegungsDatenbank;
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
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
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

	SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
	Date date;
	Logger logger = LoggerFactory.getLogger(TestDaten.class);
	AngestellterDatenbank angestelltenDB = new AngestellterDatenbank();

	public TestDaten() {

		try {
			if (angestelltenDB.selectAllAngestellte().size() == 0) {
				logger.info("Testdaten werden eingelesen");
				initAllgemein();
				initBenutzungsstatistik();
				initBelegung();
				System.out.println("TestDaten eingelesen");
			}
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("Fehler");
		}
	}

	private void initAllgemein() throws ParseException {

		Date date = sdf2.parse("01.12.2018");

//		MD5 md5 = new MD5();

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
	}

	private void initBenutzungsstatistik() throws ParseException {

		BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();

		date = sdf2.parse("01.12.2018");

		Benutzungsstatistik benutzungsstatistik1 = new Benutzungsstatistik(date, 8, true, StandortEnum.WINTERTHUR_BB,
				new Wintikurier(6, 2, 9, 5));
		Benutzungsstatistik benutzungsstatistikLL = new Benutzungsstatistik(date, StandortEnum.WINTERTHUR_LL);

//		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datumheute = sdf.format(date);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date timestamp8 = sdf2.parse(datumheute + " 08:30:00.0");
		Date timestamp9 = sdf2.parse(datumheute + " 09:59:00.0");
		Date timestamp10 = sdf2.parse(datumheute + " 10:30:00.0");
		Date timestamp11 = sdf2.parse(datumheute + " 11:30:00.0");
		Date timestamp12 = sdf2.parse(datumheute + " 12:30:00.0");
		Date timestamp13 = sdf2.parse(datumheute + " 13:30:00.0");
		Date timestamp14 = sdf2.parse(datumheute + " 14:00:00.0");
		Date timestamp15 = sdf2.parse(datumheute + " 15:30:00.0");
		Date timestamp16 = sdf2.parse(datumheute + " 16:30:00.0");
		Date timestamp17 = sdf2.parse(datumheute + " 17:30:00.0");

		Benutzerkontakt benutzerkontakt1 = new Benutzerkontakt(timestamp8, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt2 = new Benutzerkontakt(timestamp9, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt3 = new Benutzerkontakt(timestamp9, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt4 = new Benutzerkontakt(timestamp9, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt5 = new Benutzerkontakt(timestamp14, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt6 = new Benutzerkontakt(timestamp15, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt7 = new Benutzerkontakt(timestamp16, benutzungsstatistik1);
		benutzungsstatistik1.addBenutzerkontakt(benutzerkontakt1);
		benutzungsstatistik1.addBenutzerkontakt(benutzerkontakt2);
		benutzungsstatistik1.addBenutzerkontakt(benutzerkontakt3);
		benutzungsstatistik1.addBenutzerkontakt(benutzerkontakt4);
		benutzungsstatistik1.addBenutzerkontakt(benutzerkontakt5);
		benutzungsstatistik1.addBenutzerkontakt(benutzerkontakt6);
		benutzungsstatistik1.addBenutzerkontakt(benutzerkontakt7);
		Benutzerkontakt benutzerkontakt8 = new Benutzerkontakt(timestamp8, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt9 = new Benutzerkontakt(timestamp9, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt10 = new Benutzerkontakt(timestamp9, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt11 = new Benutzerkontakt(timestamp9, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt12 = new Benutzerkontakt(timestamp14, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt13 = new Benutzerkontakt(timestamp15, benutzungsstatistik1);
		Benutzerkontakt benutzerkontakt14 = new Benutzerkontakt(timestamp16, benutzungsstatistik1);
		benutzungsstatistikLL.addBenutzerkontakt(benutzerkontakt8);
		benutzungsstatistikLL.addBenutzerkontakt(benutzerkontakt9);
		benutzungsstatistikLL.addBenutzerkontakt(benutzerkontakt10);
		benutzungsstatistikLL.addBenutzerkontakt(benutzerkontakt11);
		benutzungsstatistikLL.addBenutzerkontakt(benutzerkontakt12);
		benutzungsstatistikLL.addBenutzerkontakt(benutzerkontakt13);
		benutzungsstatistikLL.addBenutzerkontakt(benutzerkontakt14);

		Intensivfrage intensivfrage1 = new Intensivfrage(timestamp9, benutzungsstatistik1);
		Intensivfrage intensivfrage2 = new Intensivfrage(timestamp10, benutzungsstatistik1);
		Intensivfrage intensivfrage3 = new Intensivfrage(timestamp12, benutzungsstatistik1);
		Intensivfrage intensivfrage4 = new Intensivfrage(timestamp13, benutzungsstatistik1);
		Intensivfrage intensivfrage5 = new Intensivfrage(timestamp14, benutzungsstatistik1);
		Intensivfrage intensivfrage6 = new Intensivfrage(timestamp15, benutzungsstatistik1);
		Intensivfrage intensivfrage7 = new Intensivfrage(timestamp16, benutzungsstatistik1);
		Intensivfrage intensivfrage8 = new Intensivfrage(timestamp17, benutzungsstatistik1);
		benutzungsstatistik1.addIntensivfrage(intensivfrage1);
		benutzungsstatistik1.addIntensivfrage(intensivfrage2);
		benutzungsstatistik1.addIntensivfrage(intensivfrage3);
		benutzungsstatistik1.addIntensivfrage(intensivfrage4);
		benutzungsstatistik1.addIntensivfrage(intensivfrage5);
		benutzungsstatistik1.addIntensivfrage(intensivfrage6);
		benutzungsstatistik1.addIntensivfrage(intensivfrage7);
		benutzungsstatistik1.addIntensivfrage(intensivfrage8);
		Intensivfrage intensivfrage9 = new Intensivfrage(timestamp9, benutzungsstatistik1);
		Intensivfrage intensivfrage10 = new Intensivfrage(timestamp10, benutzungsstatistik1);
		Intensivfrage intensivfrage11 = new Intensivfrage(timestamp12, benutzungsstatistik1);
		Intensivfrage intensivfrage12 = new Intensivfrage(timestamp13, benutzungsstatistik1);
		Intensivfrage intensivfrage13 = new Intensivfrage(timestamp14, benutzungsstatistik1);
		Intensivfrage intensivfrage14 = new Intensivfrage(timestamp15, benutzungsstatistik1);
		Intensivfrage intensivfrage15 = new Intensivfrage(timestamp16, benutzungsstatistik1);
		Intensivfrage intensivfrage16 = new Intensivfrage(timestamp17, benutzungsstatistik1);
		benutzungsstatistikLL.addIntensivfrage(intensivfrage9);
		benutzungsstatistikLL.addIntensivfrage(intensivfrage10);
		benutzungsstatistikLL.addIntensivfrage(intensivfrage11);
		benutzungsstatistikLL.addIntensivfrage(intensivfrage12);
		benutzungsstatistikLL.addIntensivfrage(intensivfrage13);
		benutzungsstatistikLL.addIntensivfrage(intensivfrage14);
		benutzungsstatistikLL.addIntensivfrage(intensivfrage15);
		benutzungsstatistikLL.addIntensivfrage(intensivfrage16);

		Telefonkontakt telefonkontakt1 = new Telefonkontakt(timestamp8, benutzungsstatistik1);
		Telefonkontakt telefonkontakt2 = new Telefonkontakt(timestamp10, benutzungsstatistik1);
		Telefonkontakt telefonkontakt3 = new Telefonkontakt(timestamp12, benutzungsstatistik1);
		Telefonkontakt telefonkontakt4 = new Telefonkontakt(timestamp13, benutzungsstatistik1);
		Telefonkontakt telefonkontakt5 = new Telefonkontakt(timestamp14, benutzungsstatistik1);
		Telefonkontakt telefonkontakt6 = new Telefonkontakt(timestamp15, benutzungsstatistik1);
		Telefonkontakt telefonkontakt7 = new Telefonkontakt(timestamp16, benutzungsstatistik1);
		Telefonkontakt telefonkontakt8 = new Telefonkontakt(timestamp16, benutzungsstatistik1);
		Telefonkontakt telefonkontakt9 = new Telefonkontakt(timestamp17, benutzungsstatistik1);
		benutzungsstatistik1.addTelefonkontakt(telefonkontakt1);
		benutzungsstatistik1.addTelefonkontakt(telefonkontakt2);
		benutzungsstatistik1.addTelefonkontakt(telefonkontakt3);
		benutzungsstatistik1.addTelefonkontakt(telefonkontakt4);
		benutzungsstatistik1.addTelefonkontakt(telefonkontakt5);
		benutzungsstatistik1.addTelefonkontakt(telefonkontakt6);
		benutzungsstatistik1.addTelefonkontakt(telefonkontakt7);
		benutzungsstatistik1.addTelefonkontakt(telefonkontakt8);
		benutzungsstatistik1.addTelefonkontakt(telefonkontakt9);

		Emailkontakt emailkontakt1 = new Emailkontakt(timestamp8, benutzungsstatistik1);
		Emailkontakt emailkontakt2 = new Emailkontakt(timestamp9, benutzungsstatistik1);
		Emailkontakt emailkontakt3 = new Emailkontakt(timestamp11, benutzungsstatistik1);
		Emailkontakt emailkontakt4 = new Emailkontakt(timestamp11, benutzungsstatistik1);
		Emailkontakt emailkontakt5 = new Emailkontakt(timestamp13, benutzungsstatistik1);
		Emailkontakt emailkontakt6 = new Emailkontakt(timestamp13, benutzungsstatistik1);
		Emailkontakt emailkontakt7 = new Emailkontakt(timestamp14, benutzungsstatistik1);
		Emailkontakt emailkontakt8 = new Emailkontakt(timestamp15, benutzungsstatistik1);
		Emailkontakt emailkontakt9 = new Emailkontakt(timestamp17, benutzungsstatistik1);
		benutzungsstatistik1.addEmailkontakt(emailkontakt1);
		benutzungsstatistik1.addEmailkontakt(emailkontakt2);
		benutzungsstatistik1.addEmailkontakt(emailkontakt3);
		benutzungsstatistik1.addEmailkontakt(emailkontakt4);
		benutzungsstatistik1.addEmailkontakt(emailkontakt5);
		benutzungsstatistik1.addEmailkontakt(emailkontakt6);
		benutzungsstatistik1.addEmailkontakt(emailkontakt7);
		benutzungsstatistik1.addEmailkontakt(emailkontakt8);
		benutzungsstatistik1.addEmailkontakt(emailkontakt9);

		BeantwortungBibliothekspersonal beantwortungBibliothekspersonal1 = new BeantwortungBibliothekspersonal(
				timestamp8, benutzungsstatistik1);
		BeantwortungBibliothekspersonal beantwortungBibliothekspersonal2 = new BeantwortungBibliothekspersonal(
				timestamp9, benutzungsstatistik1);
		BeantwortungBibliothekspersonal beantwortungBibliothekspersonal3 = new BeantwortungBibliothekspersonal(
				timestamp11, benutzungsstatistik1);
		BeantwortungBibliothekspersonal beantwortungBibliothekspersonal4 = new BeantwortungBibliothekspersonal(
				timestamp11, benutzungsstatistik1);
		BeantwortungBibliothekspersonal beantwortungBibliothekspersonal5 = new BeantwortungBibliothekspersonal(
				timestamp13, benutzungsstatistik1);
		BeantwortungBibliothekspersonal beantwortungBibliothekspersonal6 = new BeantwortungBibliothekspersonal(
				timestamp13, benutzungsstatistik1);
		BeantwortungBibliothekspersonal beantwortungBibliothekspersonal7 = new BeantwortungBibliothekspersonal(
				timestamp14, benutzungsstatistik1);
		BeantwortungBibliothekspersonal beantwortungBibliothekspersonal8 = new BeantwortungBibliothekspersonal(
				timestamp15, benutzungsstatistik1);
		BeantwortungBibliothekspersonal beantwortungBibliothekspersonal9 = new BeantwortungBibliothekspersonal(
				timestamp17, benutzungsstatistik1);
		benutzungsstatistikLL.addBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal1);
		benutzungsstatistikLL.addBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal2);
		benutzungsstatistikLL.addBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal3);
		benutzungsstatistikLL.addBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal4);
		benutzungsstatistikLL.addBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal5);
		benutzungsstatistikLL.addBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal6);
		benutzungsstatistikLL.addBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal7);
		benutzungsstatistikLL.addBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal8);
		benutzungsstatistikLL.addBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal9);

		ExterneGruppe externeGruppe1 = new ExterneGruppe("Test Gruppe", 10, "10:30", benutzungsstatistik1);
		ExterneGruppe externeGruppe2 = new ExterneGruppe("Winterthur Stadtführung", 14, "12:15", benutzungsstatistik1);
		ExterneGruppe externeGruppe3 = new ExterneGruppe("Fussballverein Winterthur", 7, "16:24", benutzungsstatistik1);
		benutzungsstatistik1.addExterneGruppe(externeGruppe1);
		benutzungsstatistik1.addExterneGruppe(externeGruppe2);
		benutzungsstatistik1.addExterneGruppe(externeGruppe3);

		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik1);
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistikLL);
	}

	private void initBelegung() throws ParseException {

		BelegungsDatenbank belegungDB = new BelegungsDatenbank();

		Date date = sdf2.parse("01.12.2018 12:00:00");

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

		// Daten Wädenswil
		Arbeitsplätze arbeitsplatzW1 = new Arbeitsplätze(39, UhrzeitEnum.ELF, stockwerkW);
		Arbeitsplätze arbeitsplatzW2 = new Arbeitsplätze(61, UhrzeitEnum.FÜNFZEHN, stockwerkW);

		// Daten in die Objekte füllen
		belegungBB.addStockwerk(stockwerkEG);
		belegungBB.addStockwerk(stockwerk1ZG);
		belegungBB.addStockwerk(stockwerk2ZG);
		belegungLL.addStockwerk(stockwerkLL);
		belegungW.addStockwerk(stockwerkW);

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

		stockwerkW.addArbeitsplätze(arbeitsplatzW1);
		stockwerkW.addArbeitsplätze(arbeitsplatzW2);

		belegungDB.insertBelegung(belegungBB);
		belegungDB.insertBelegung(belegungLL);
		belegungDB.insertBelegung(belegungW);

		// Ausgabe von allen Daten
//		for(Belegung belegung : belegungDB.selectAllBelegungen()) {
//			System.out.println(belegung.getBelegungs_ID());
//			for(Stockwerk stockwerk : belegung.getStockwerkListe()) {
//				System.out.println(stockwerk.getName());
//				for(Arbeitsplätze a : stockwerk.getArbeitsplatzListe()) {
//					System.out.println(a.getAnzahlPersonen() + "   " +a.getUhrzeit());
//				}
//				for(SektorA a : stockwerk.getSektorAListe()) {
//					System.out.println(a.getAnzahlPersonen() + "   " +a.getUhrzeit());
//				}
//				for(SektorB a : stockwerk.getSektorBListe()) {
//					System.out.println(a.getAnzahlPersonen() + "   " +a.getUhrzeit());
//				}
//				for(Gruppenräume a : stockwerk.getGruppenräumeListe()) {
//					System.out.println(a.getAnzahlPersonen() + "   " +a.getAnzahlRäume() +"   " +a.getUhrzeit());
//				}
//				for(Carrels a : stockwerk.getCarrelsListe()) {
//					System.out.println(a.getAnzahlPersonen() + "   " +a.getAnzahlRäume() +"   " +a.getUhrzeit());
//				}
//			}
//		}
	}

	public static void main(String[] args) {
		new TestDaten();
	}

}
