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
import benutzungsstatistik.db.BeantwortungBibliothekspersonalDatenbank;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.db.EmailkontaktDatenbank;
import benutzungsstatistik.db.ExterneGruppeDatenbank;
import benutzungsstatistik.db.IntensivfrageDatenbank;
import benutzungsstatistik.db.BenutzerkontaktDatenbank;
import benutzungsstatistik.db.TelefonkontaktDatenbank;
import benutzungsstatistik.db.WintikurierDatenbank;
import benutzungsstatistik.model.BeantwortungBibliothekspersonal;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.ExterneGruppe;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Telefonkontakt;
import benutzungsstatistik.model.Wintikurier;

/**
 * Klasse um alle Testdaten einzulesen
 * @author Marvin Bindemann
 *
 */
public class TestDaten {
	
	AngestellterDatenbank angestelltenDB = new AngestellterDatenbank();
	StandortDatenbank standortDB = new StandortDatenbank();
	EmailkontaktDatenbank emailKontaktDB = new EmailkontaktDatenbank();
	ExterneGruppeDatenbank externeGruppeDB = new ExterneGruppeDatenbank();
	BeantwortungBibliothekspersonalDatenbank beantwortungBibliothekspersonalDB = new BeantwortungBibliothekspersonalDatenbank();
	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	IntensivfrageDatenbank intensivFrageDB = new IntensivfrageDatenbank();
	BenutzerkontaktDatenbank benutzerKontaktDB = new BenutzerkontaktDatenbank();
	TelefonkontaktDatenbank telefonKontaktDB = new TelefonkontaktDatenbank();
	WintikurierDatenbank wintikurierDB = new WintikurierDatenbank();
	
	public TestDaten(){
		try {
			init();
			System.out.println("TestDaten eingelesen");
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("Fehler");
		}
	}
	

	private void init() throws ParseException {
		Standort standort1 = new Standort(StandortEnum.Winterthur_BB);
		Standort standort2 = new Standort(StandortEnum.Winterthur_LL);
		Standort standort3 = new Standort(StandortEnum.Wädenswil);
		standortDB.insertStandort(standort1);
		standortDB.insertStandort(standort2);
		standortDB.insertStandort(standort3);
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
		Date date = sdf2.parse("10.10.2018");
		
		Angestellter angestellter1 = new Angestellter("Mitarbeiter Winterthur", "123", date, false, standort1);
		Angestellter angestellter2 = new Angestellter("Studentische Mitarbeiter Winterthur", "123", date, false, standort2);
		Angestellter angestellter3 = new Angestellter("Admin Winterthur", "123", date, true, standort1);
		Angestellter angestellter4 = new Angestellter("Mitarbeiter Wädenswil", "123", date, false, standort3);
		Angestellter angestellter5 = new Angestellter("Admin Wädenswil", "123", date, true, standort3);
		angestelltenDB.insertAngestellter(angestellter1);
		angestelltenDB.insertAngestellter(angestellter2);
		angestelltenDB.insertAngestellter(angestellter3);
		angestelltenDB.insertAngestellter(angestellter4);
		angestelltenDB.insertAngestellter(angestellter5);
		
		Wintikurier wintikurier1 = new Wintikurier(6, 2, 9, 5);
		wintikurierDB.insertWintikurier(wintikurier1);
		
		Benutzungsstatistik benutzungsstatistik1 = new Benutzungsstatistik(date, 8, true, standort1, wintikurier1);
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik1);
		
//		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datumheute = sdf.format(date);
		Timestamp timestamp8 = Timestamp.valueOf(datumheute +" 08:30:00.0");
		Timestamp timestamp9 = Timestamp.valueOf(datumheute +" 09:59:00.0");
		Timestamp timestamp10 = Timestamp.valueOf(datumheute +" 10:30:00.0");
		Timestamp timestamp11 = Timestamp.valueOf(datumheute +" 11:30:00.0");
		Timestamp timestamp12 = Timestamp.valueOf(datumheute +" 12:30:00.0");
		Timestamp timestamp13 = Timestamp.valueOf(datumheute +" 13:30:00.0");
		Timestamp timestamp14 = Timestamp.valueOf(datumheute +" 14:00:00.0");
		Timestamp timestamp15 = Timestamp.valueOf(datumheute +" 15:30:00.0");
		Timestamp timestamp16 = Timestamp.valueOf(datumheute +" 16:30:00.0");
		Timestamp timestamp17 = Timestamp.valueOf(datumheute +" 17:30:00.0");
		
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
		
		BeantwortungBibliothekspersonal beantwortungBibliothekspersonal1 = new BeantwortungBibliothekspersonal(timestamp8, benutzungsstatistik1);
		beantwortungBibliothekspersonalDB.insertBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal1);
		
		ExterneGruppe externeGruppe1 = new ExterneGruppe("Test Gruppe", 10, benutzungsstatistik1);
		ExterneGruppe externeGruppe2 = new ExterneGruppe("Winterthur Stadtführung", 14, benutzungsstatistik1);
		ExterneGruppe externeGruppe3 = new ExterneGruppe("Fussballverein Winterthur", 7, benutzungsstatistik1);
		externeGruppeDB.insertExterneGruppe(externeGruppe1);
		externeGruppeDB.insertExterneGruppe(externeGruppe2);
		externeGruppeDB.insertExterneGruppe(externeGruppe3);
		
	}


	public static void main(String[] args){
		new TestDaten();
	}

}
