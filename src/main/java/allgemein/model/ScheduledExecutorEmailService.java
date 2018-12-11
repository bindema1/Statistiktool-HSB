package allgemein.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.ui.Notification;

import belegung.db.BelegungsDatenbank;
import belegung.model.Arbeitsplätze;
import belegung.model.Belegung;
import belegung.model.Carrels;
import belegung.model.Gruppenräume;
import belegung.model.SektorA;
import belegung.model.SektorB;
import belegung.model.Stockwerk;
import belegung.model.UhrzeitEnum;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;

/**
 * Die Klasse versendet Mails. Ausserdem läuft ein Task im Hintergrund, welcher
 * eine Stunde nach einer leeren Belegung sowie am Ende des Tages, wenn
 * Kassenbeleg noch true ist, eine Mail
 * 
 * @author Marvin Bindemann
 */
@Service
public class ScheduledExecutorEmailService {

	@Autowired
	private static SpringEmailService springEmailService;
	private static BelegungsDatenbank belegungsDB = new BelegungsDatenbank();
	private static BenutzungsstatistikDatenbank benutzungsDB = new BenutzungsstatistikDatenbank();

	/**
	 * Sendet eine Email, wenn eine Belegung nach einer Stunde immer noch leer ist
	 * Sendet eine Mail, wenn am Ende des Tages der Kassenbeleg auf true ist
	 */
	public static void sendeMailWegenLeererBelegungOderWegenKassenbeleg() {

		// Der Task soll am Anfang mit initalDelay genau Nachts um 00.00 starten
		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.of("Europe/Berlin");
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime zonedNext5;
		zonedNext5 = zonedNow.withHour(17).withMinute(46).withSecond(0);
		if (zonedNow.compareTo(zonedNext5) > 0)
			zonedNext5 = zonedNext5.plusDays(1);
		Duration duration = Duration.between(zonedNow, zonedNext5);
		long initalDelay = duration.getSeconds();

		// Task für die Belegung
		ScheduledExecutorService execServiceBelegung = Executors.newScheduledThreadPool(1);
		// Der Task wird alle 30 Minuten ausgeführt
		execServiceBelegung.scheduleAtFixedRate(() -> {
			// Task welcher gemacht werden soll
			System.out.println("Running repetitive task at: " + new java.util.Date());

			// Sendet eine Email 1 Stunde nach einer leeren Belegung
			pruefeLeereBelegung();

		}, initalDelay, 2, TimeUnit.HOURS);

		// Task für den Kassenbeleg
		ScheduledExecutorService execServiceKassenbeleg = Executors.newScheduledThreadPool(1);
		// Der Task wird alle 30 Minuten ausgeführt
		execServiceKassenbeleg.scheduleAtFixedRate(() -> {
			// Task welcher gemacht werden soll
			System.out.println("Running repetitive task at: " + new java.util.Date());

			// Email wenn Kassenbeleg == true am 19.30 Mo-Fr und Sa 15.30
			pruefeKassenbeleg();

		}, initalDelay, 30, TimeUnit.MINUTES);

	}

	@SuppressWarnings("deprecation")
	private static void pruefeKassenbeleg() {

		Benutzungsstatistik benutzung = benutzungsDB.selectBenutzungsstatistikForDateAndStandort(new Date(),
				StandortEnum.WINTERTHUR_BB);

		if (benutzung.isKassenbeleg() == true) {

			int wochentag = 0;

			switch (LocalDate.now().getDayOfWeek().toString()) {
			case "MONDAY":
				wochentag = 1;
			case "TUESDAY":
				wochentag = 2;
			case "WEDNESDAY":
				wochentag = 3;
			case "THURSDAY":
				wochentag = 4;
			case "FRIDAY":
				wochentag = 5;
			case "SATURDAY":
				wochentag = 6;
			case "SUNDAY":
				wochentag = 7;
			}

			System.out.println("Wochentag: " + wochentag);

			if (wochentag >= 1 && wochentag <= 5) {
				// Sende Mail um 19:30
				if (new Date().getHours() == 18 && new Date().getMinutes() >= 1) {
					sendeEmailKassenbeleg();
				}
			} else if (wochentag == 6) {
				// Sende Mail um 15:30
				if (new Date().getHours() == 14 && new Date().getMinutes() >= 1) {
					sendeEmailKassenbeleg();
				}
			}
			// Für Sonntag, mache nichts
		}

	}

	private static void sendeEmailKassenbeleg() {
		List<String> to = new ArrayList<>();
//		to.add("ausleihe.winterthur.hsb@zhaw.ch");
		to.add("m.bindemann@yahoo.de");

		// Nachricht des Textes
		String subject = "Kassenbeleg ausfüllen";
		String text = "Der Kassenbeleg wurde noch nicht ausgefüllt und auf Off gestellt.";

		// Mail wird versendet
		sendEmail(to, subject, text);
	}

	@SuppressWarnings("deprecation")
	private static void pruefeLeereBelegung() {

		Date date = new Date();
		
		if(date.getHours() >= 8 && date.getHours() <= 19) {
			List<Belegung> belegungsListe = new ArrayList<>();
			Belegung belegungBB = belegungsDB.selectBelegungForDateAndStandort(new Date(), StandortEnum.WINTERTHUR_BB);
			Belegung belegungLL = belegungsDB.selectBelegungForDateAndStandort(new Date(), StandortEnum.WINTERTHUR_LL);
			Belegung belegungWaedi = belegungsDB.selectBelegungForDateAndStandort(new Date(), StandortEnum.WÄDENSWIL);
	
			belegungsListe.add(belegungBB);
			belegungsListe.add(belegungLL);
			belegungsListe.add(belegungWaedi);
			
			UhrzeitEnum uhrzeitEnum;
			switch (date.getHours()) {
			case 9:
				uhrzeitEnum = UhrzeitEnum.NEUN;
				break;
			case 11:
				uhrzeitEnum = UhrzeitEnum.ELF;
				break;
			case 13:
				uhrzeitEnum = UhrzeitEnum.DREIZEHN;
				break;
			case 15:
				uhrzeitEnum = UhrzeitEnum.FÜNFZEHN;
				break;
			case 17:
				uhrzeitEnum = UhrzeitEnum.SIEBZEHN;
				break;
			case 19:
				uhrzeitEnum = UhrzeitEnum.NEUNZEHN;
				break;
			default:
				uhrzeitEnum = null;
				break;
			}
	
			if(uhrzeitEnum != null) {
				for (Belegung belegung : belegungsListe) {
					
					boolean nullWert = false;
					String nullWerteString = "";
					
					for (Stockwerk stockwerk : belegung.getStockwerkListe()) {
						
						String stockWerkNullWert = "auf Stockwerk "+stockwerk.getName().toString() +" gibt es leere Bereiche:";
		
						// Geht durch alle Arbeitsplätze des Stockwerks
						for (Arbeitsplätze arbeitsplätze : stockwerk.getArbeitsplatzListe()) {
							if(arbeitsplätze.getUhrzeit() == uhrzeitEnum) {
								if(arbeitsplätze.getAnzahlPersonen() == 0) {
									nullWert = true;
									stockWerkNullWert += " Arbeitsplätze";
								}
							}
						}
		
						// Geht durch alle SektorA des Stockwerks
						for (SektorA sektorA : stockwerk.getSektorAListe()) {
							if(sektorA.getUhrzeit() == uhrzeitEnum) {
								if(sektorA.getAnzahlPersonen() == 0) {
									nullWert = true;
									stockWerkNullWert += " SektorA";
								}
							}
						}
		
						// Geht durch alle SektorB des Stockwerks
						for (SektorB sektorB : stockwerk.getSektorBListe()) {
							if(sektorB.getUhrzeit() == uhrzeitEnum) {
								if(sektorB.getAnzahlPersonen() == 0) {
									nullWert = true;
									stockWerkNullWert += " SektorB";
								}
							}
						}
		
						// Geht durch alle Gruppenräume des Stockwerks
						for (Gruppenräume gruppenräume : stockwerk.getGruppenräumeListe()) {
							if(gruppenräume.getUhrzeit() == uhrzeitEnum) {
								if(gruppenräume.getAnzahlPersonen() == 0) {
									nullWert = true;
									stockWerkNullWert += " Gruppenräume";
								}
							}
						}
		
						// Geht durch alle Carrels des Stockwerks
						for (Carrels carrels : stockwerk.getCarrelsListe()) {
							if(carrels.getUhrzeit() == uhrzeitEnum) {
								if(carrels.getAnzahlPersonen() == 0) {
									nullWert = true;
									stockWerkNullWert += " Carrels";
								}
							}
						}
						
						nullWerteString += stockWerkNullWert;
					}
					
					if(nullWert == true) {
						sendeMailBelegung(nullWerteString, belegung.getStandort());
					}
				}
			}
		}

	}

	private static void sendeMailBelegung(String nullWerteString, StandortEnum standortEnum) {
		List<String> to = new ArrayList<>();
		if(standortEnum == StandortEnum.WÄDENSWIL) {
//			to.add("waedenswil.hsb@zhaw.ch");
		}else {
//			to.add("ausleihe.winterthur.hsb@zhaw.ch");
		}
		to.add("m.bindemann@yahoo.de");

		// Nachricht des Textes
		String subject = "Belegung nicht ausgefüllt";
		String text = "Die Belegung wurde noch nicht ausgefüllt " +nullWerteString;

		// Mail wird versendet
		sendEmail(to, subject, text);
	}

	/**
	 * Sendet eine Email an die Bibliothek Winterthur oder Wädenswil
	 * 
	 * @param to ausleihe.winterthur.hsb@zhaw.ch || waedenswil.hsb@zhaw.ch
	 */
	@SuppressWarnings({ "static-access" })
	public static void sendEmail(List<String> to, String subject, String text) {
		try {
			String from = "statistiktoolhsb@gmail.com";

			springEmailService.send(from, to, subject, text);

			Notification.show("Email gesendet");

		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Error sending the email", Notification.Type.ERROR_MESSAGE);
		}
	}
}
