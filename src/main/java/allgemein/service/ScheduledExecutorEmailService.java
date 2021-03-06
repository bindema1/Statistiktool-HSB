package allgemein.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import allgemein.db.EmailDatenbank;
import allgemein.model.Email;
import allgemein.model.StandortEnum;
import belegung.db.BelegungsDatenbank;
import belegung.model.Arbeitsplätze;
import belegung.model.Belegung;
import belegung.model.Carrels;
import belegung.model.Gruppenräume;
import belegung.model.SektorA;
import belegung.model.SektorB;
import belegung.model.Stockwerk;
import belegung.model.StockwerkEnum;
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
	private static EmailDatenbank emailDB = new EmailDatenbank();

	public ScheduledExecutorEmailService() {
		sendeMailWegenLeererBelegungOderWegenKassenbeleg();
	}

	/**
	 * Sendet eine Email, wenn eine Belegung nach einer Stunde immer noch leer ist
	 * Sendet eine Mail, wenn am Ende des Tages der Kassenbeleg auf true ist
	 */
	@SuppressWarnings("deprecation")
	public static void sendeMailWegenLeererBelegungOderWegenKassenbeleg() {

		// Der Task soll am Anfang mit initalDelay genau Nachts um 00.00 starten
//		LocalDateTime localNow = LocalDateTime.now();
//		ZoneId currentZone = ZoneId.of("Europe/Berlin");
//		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
//		ZonedDateTime zonedNext5;
//		zonedNext5 = zonedNow.withHour(0).withMinute(0).withSecond(0);
//		if (zonedNow.compareTo(zonedNext5) > 0)
//			zonedNext5 = zonedNext5.plusDays(1);
//		Duration duration = Duration.between(zonedNow, zonedNext5);
//		long initalDelay = duration.getSeconds();

		// Task für die Belegung und Kassenbeleg
		ScheduledExecutorService execService = Executors.newScheduledThreadPool(1);
		// Der Task wird alle 30 Minuten ausgeführt
		execService.scheduleAtFixedRate(() -> {
			// Task welcher gemacht werden soll
			System.out.println("Running repetitive task at: " + new java.util.Date());

			// Es wird geschaut wann zuletzt eine Mail versendet wurde
			long zuletztVersendeteMailZeit = emailDB.getEmail().getVersendetTimestamp().getTime();
			// 300000 Millisekunden sind 5 Minuten
			long jetzigeZeitMinusFuenfMinuten = new Date().getTime() - 300000;

			// Nur falls die letzte Email länger her ist als 5 Minuten, soll wieder geprüft
			// werden, dies verhindert, dass mehrere Emails hintereinander eintreffen in
			// sekunden abständen
			if (jetzigeZeitMinusFuenfMinuten >= zuletztVersendeteMailZeit) {

				int minutes = new Date().getMinutes();
				// Sendet eine Email 1 Stunde nach einer leeren Belegung
				if (minutes >= 55) {
					pruefeLeereBelegung();
				}

				// Email wenn Kassenbeleg == true am 19.30 Mo-Fr und Sa 15.30
				if (minutes >= 28 && minutes <= 32) {
					pruefeKassenbeleg();
				}
			}

		}, 3, 5, TimeUnit.MINUTES);
//		}, initalDelay, 30, TimeUnit.MINUTES); //Delay wird nicht verwendet
	}

	@SuppressWarnings("deprecation")
	private static void pruefeKassenbeleg() {

		Benutzungsstatistik benutzung = benutzungsDB.selectBenutzungsstatistikForDateAndStandort(new Date(),
				StandortEnum.WINTERTHUR_BB);

		// Wenn der Kassenbeleg auf true ist
		if (benutzung.isKassenbeleg() == true) {

			int wochentag = 0;

			switch (LocalDate.now().getDayOfWeek().toString()) {
			case "MONDAY":
				wochentag = 1;
				break;
			case "TUESDAY":
				wochentag = 2;
				break;
			case "WEDNESDAY":
				wochentag = 3;
				break;
			case "THURSDAY":
				wochentag = 4;
				break;
			case "FRIDAY":
				wochentag = 5;
				break;
			case "SATURDAY":
				wochentag = 6;
				break;
			case "SUNDAY":
				wochentag = 7;
				break;
			}

			if (wochentag >= 1 && wochentag <= 5) {
				// Sende Mail um 19:30
				if (new Date().getHours() == 19 && new Date().getMinutes() >= 1) {
					sendeEmailKassenbeleg();
				}
			} else if (wochentag == 6) {
				// Sende Mail um 15:30
				if (new Date().getHours() == 15 && new Date().getMinutes() >= 1) {
					sendeEmailKassenbeleg();
				}
			}
			// Für Sonntag, mache nichts
		}

	}

	private static void sendeEmailKassenbeleg() {
		List<String> to = new ArrayList<>();
		to.add("ausleihe.winterthur.hsb@zhaw.ch");

		// Nachricht des Textes
		String subject = "Kassenbeleg ausfüllen";
		String text = "Der Kassenbeleg muss noch erstellt und in der Applikation auf Off gestellt werden.";

		// Mail wird versendet
		sendEmail(to, subject, text);
	}

	@SuppressWarnings("deprecation")
	private static void pruefeLeereBelegung() {

		Date date = new Date();

		// Nicht Sonntags
		if (!LocalDate.now().getDayOfWeek().toString().equals("SUNDAY")) {

			// Samstags ist die letzte Zählung 15 Uhr
			int letzteZaehlung;
			if (LocalDate.now().getDayOfWeek().toString().equals("SATURDAY")) {
				letzteZaehlung = 15;
			} else {
				// Montag-Freitags
				letzteZaehlung = 19;
			}

			if (date.getHours() >= 8 && date.getHours() <= letzteZaehlung) {

				// Fügt alle Belegungen einer Liste hinzu
				List<Belegung> belegungsListe = new ArrayList<>();
				Belegung belegungBB = belegungsDB.selectBelegungForDateAndStandort(new Date(),
						StandortEnum.WINTERTHUR_BB);
				Belegung belegungLL = belegungsDB.selectBelegungForDateAndStandort(new Date(),
						StandortEnum.WINTERTHUR_LL);
				Belegung belegungWaedi = belegungsDB.selectBelegungForDateAndStandort(new Date(),
						StandortEnum.WÄDENSWIL);

				belegungsListe.add(belegungBB);
				belegungsListe.add(belegungLL);
				belegungsListe.add(belegungWaedi);

				// Setzt die aktuelle Uhrzeit
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

				// Geht durch alle Belegungen
				for (Belegung belegung : belegungsListe) {

					// Wädenswil hat nur um 15 Uhr eine Zählung
					if (belegung.getStandort() == StandortEnum.WÄDENSWIL) {
						if (uhrzeitEnum == UhrzeitEnum.NEUN || uhrzeitEnum == UhrzeitEnum.DREIZEHN
								|| uhrzeitEnum == UhrzeitEnum.ELF || uhrzeitEnum == UhrzeitEnum.SIEBZEHN
								|| uhrzeitEnum == UhrzeitEnum.NEUNZEHN)
							uhrzeitEnum = null;
					}

					if (uhrzeitEnum != null) {

						// Prüft ob es einen Nullwert gibt
						boolean nullWert = false;
						// Fügt am Ende alle stockWerkNullWerte zusammen
						String nullWerteString = "";

						// Geht durch alle Stockwerke
						for (Stockwerk stockwerk : belegung.getStockwerkListe()) {

							// Der String der später in der Mail versendet wird
							String stockWerkNullWert = "";

							// Prüft ob ein Eintrag zu dieser Uhrzeit überhaupt vorhanden ist
							boolean eintragInDBVorhanden;

							// Nur LL hat keine Arbeitsplätze
							if (stockwerk.getName() != StockwerkEnum.LL) {
								// Geht durch alle Arbeitsplätze des Stockwerks
								eintragInDBVorhanden = false;
								for (Arbeitsplätze arbeitsplätze : stockwerk.getArbeitsplatzListe()) {
									if (arbeitsplätze.getUhrzeit() == uhrzeitEnum) {
										if (arbeitsplätze.getAnzahlPersonen() == 0) {
											nullWert = true;
											stockWerkNullWert += " Arbeitsplätze";
										}
										// Falls ein Eintrag für die Uhrzeit existiert
										eintragInDBVorhanden = true;
									}
								}
								// Falls es keinen Eintrag zu der Uhrzeit gibt
								if (eintragInDBVorhanden == false) {
									nullWert = true;
									stockWerkNullWert += " Arbeitsplätze";
								}
							}

							// Nur LL hat SektorA
							if (stockwerk.getName() == StockwerkEnum.LL) {
								// Geht durch alle SektorA des Stockwerks
								eintragInDBVorhanden = false;
								for (SektorA sektorA : stockwerk.getSektorAListe()) {
									if (sektorA.getUhrzeit() == uhrzeitEnum) {
										if (sektorA.getAnzahlPersonen() == 0) {
											nullWert = true;
											stockWerkNullWert += " SektorA";
										}
										// Falls ein Eintrag für die Uhrzeit existiert
										eintragInDBVorhanden = true;
									}
								}
								// Falls es keinen Eintrag zu der Uhrzeit gibt
								if (eintragInDBVorhanden == false) {
									nullWert = true;
									stockWerkNullWert += " SektorA";
								}
							}

							// Nur LL hat SektorB
							if (stockwerk.getName() == StockwerkEnum.LL) {
								// Geht durch alle SektorB des Stockwerks
								eintragInDBVorhanden = false;
								for (SektorB sektorB : stockwerk.getSektorBListe()) {
									if (sektorB.getUhrzeit() == uhrzeitEnum) {
										if (sektorB.getAnzahlPersonen() == 0) {
											nullWert = true;
											stockWerkNullWert += " SektorB";
										}
										// Falls ein Eintrag für die Uhrzeit existiert
										eintragInDBVorhanden = true;
									}
								}
								// Falls es keinen Eintrag zu der Uhrzeit gibt
								if (eintragInDBVorhanden == false) {
									nullWert = true;
									stockWerkNullWert += " SektorB";
								}
							}

							// Nur EG und LL hat Gruppenräume
							if (stockwerk.getName() == StockwerkEnum.LL || stockwerk.getName() == StockwerkEnum.EG) {
								// Geht durch alle Gruppenräume des Stockwerks
								eintragInDBVorhanden = false;
								for (Gruppenräume gruppenräume : stockwerk.getGruppenräumeListe()) {
									if (gruppenräume.getUhrzeit() == uhrzeitEnum) {
										if (gruppenräume.getAnzahlPersonen() == 0) {
											nullWert = true;
											stockWerkNullWert += " Gruppenräume";
										}
										// Falls ein Eintrag für die Uhrzeit existiert
										eintragInDBVorhanden = true;
									}
								}
								// Falls es keinen Eintrag zu der Uhrzeit gibt
								if (eintragInDBVorhanden == false) {
									nullWert = true;
									stockWerkNullWert += " Gruppenräume";
								}
							}

							// Nur LL hat Carrels
							if (stockwerk.getName() == StockwerkEnum.LL) {
								// Geht durch alle Carrels des Stockwerks
								eintragInDBVorhanden = false;
								for (Carrels carrels : stockwerk.getCarrelsListe()) {
									if (carrels.getUhrzeit() == uhrzeitEnum) {
										if (carrels.getAnzahlPersonen() == 0) {
											nullWert = true;
											stockWerkNullWert += " Carrels";
										}
										// Falls ein Eintrag für die Uhrzeit existiert
										eintragInDBVorhanden = true;
									}
								}
								// Falls es keinen Eintrag zu der Uhrzeit gibt
								if (eintragInDBVorhanden == false) {
									nullWert = true;
									stockWerkNullWert += " Carrels";
								}
							}

							if (!stockWerkNullWert.equals("")) {
								// Der String der später in der Mail versendet wird
								stockWerkNullWert = ". Auf Stockwerk " + stockwerk.getName().toString()
										+ " gibt es leere Bereiche:" + stockWerkNullWert;
							}
							nullWerteString += stockWerkNullWert;
						}

						// Wenn es einen Null-Wert hat, sende eine Mail
						if (nullWert == true) {
							if(belegung.getStandort() == StandortEnum.WÄDENSWIL && LocalDate.now().getDayOfWeek().toString().equals("SATURDAY")) {
								// Samstags hat Wädenswil keine Belegung, daher soll keine Mail verschickt werden
							}else {
								sendeMailBelegung(uhrzeitEnum, nullWerteString, belegung.getStandort());
							}
						}
					}
				}
			}
		}

	}

	private static void sendeMailBelegung(UhrzeitEnum uhrzeitEnum, String nullWerteString, StandortEnum standortEnum) {
		List<String> to = new ArrayList<>();
		if (standortEnum == StandortEnum.WÄDENSWIL) {
			to.add("waedenswil.hsb@zhaw.ch");
		} else {
			to.add("ausleihe.winterthur.hsb@zhaw.ch");
		}

		// Nachricht des Textes
		String subject = "Belegung nicht ausgefüllt";
		String uhrzeit = null;
		switch (uhrzeitEnum) {
		case NEUN:
			uhrzeit = "9";
			break;
		case ELF:
			uhrzeit = "11";
			break;
		case DREIZEHN:
			uhrzeit = "13";
			break;
		case FÜNFZEHN:
			uhrzeit = "15";
			break;
		case SIEBZEHN:
			uhrzeit = "17";
			break;
		case NEUNZEHN:
			uhrzeit = "19";
			break;
		}
		String text = "Die Belegung wurde noch nicht ausgefüllt um " + uhrzeit + " Uhr" + nullWerteString;

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

			// Aktualisiert den Eintrag in der Datenbank
			Email email = emailDB.getEmail();
			email.setVersendetTimestamp(new Date());
			emailDB.updateEmail(email);

			// Sendet die Email
			springEmailService.send(from, to, subject, text);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
