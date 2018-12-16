package datenimport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

public class BelegungsImporter {

	private String line = "";
	// CSV separiert mit ;
	private String cvsSplitBy = ";";
	private BelegungsDatenbank belegungsDB = new BelegungsDatenbank();
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");

	public BelegungsImporter() {
		importZG("/import/Belegungsstatistik_Bibliothek_ZG1.csv", StockwerkEnum.ZG1);
		importZG("/import/Belegungsstatistik_Bibliothek_ZG2.csv", StockwerkEnum.ZG2);
		importEG();
		importLL();
	}

	private void importLL() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classloader.getResourceAsStream("/import/Belegungsstatistik_Bibliothek_LL.csv");
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
		try (BufferedReader br = new BufferedReader(streamReader)) {

			// Datum der Belegung
			Date datum = null;

			while ((line = br.readLine()) != null) {

				String[] uhrzeit = line.split(cvsSplitBy);

				if (uhrzeit.length >= 1) {

					if (!uhrzeit[1].equals("")) {
						try {
							// Wandelt das Datum in ein Date
							datum = sdf.parse(uhrzeit[1]);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					// Für welche Uhrzeit
					UhrzeitEnum uhrzeitEnum;

					switch (uhrzeit[2]) {
					case "9 Uhr":
						uhrzeitEnum = UhrzeitEnum.NEUN;
						break;
					case "11 Uhr":
						uhrzeitEnum = UhrzeitEnum.ELF;
						break;
					case "13 Uhr":
						uhrzeitEnum = UhrzeitEnum.DREIZEHN;
						break;
					case "15 Uhr":
						uhrzeitEnum = UhrzeitEnum.FÜNFZEHN;
						break;
					case "17 Uhr":
						uhrzeitEnum = UhrzeitEnum.SIEBZEHN;
						break;
					case "19 Uhr":
						uhrzeitEnum = UhrzeitEnum.NEUNZEHN;
						break;
					default:
						uhrzeitEnum = null;
						break;
					}

					if (uhrzeitEnum != null) {
						Belegung belegung = belegungsDB.selectBelegungForDateAndStandort(datum,
								StandortEnum.WINTERTHUR_LL);

						for (Stockwerk stockwerk : belegung.getStockwerkListe()) {
							if (stockwerk.getName() == StockwerkEnum.LL) {
								
								int anzahlPersonen = Integer.parseInt(uhrzeit[3]) + Integer.parseInt(uhrzeit[4]) + Integer.parseInt(uhrzeit[5]) + Integer.parseInt(uhrzeit[6]);
								SektorA sektorA = new SektorA(anzahlPersonen, uhrzeitEnum, stockwerk);
								stockwerk.addSektorA(sektorA);

								anzahlPersonen = Integer.parseInt(uhrzeit[7]) + Integer.parseInt(uhrzeit[8]);
								SektorB sektorB = new SektorB(anzahlPersonen, uhrzeitEnum, stockwerk);
								stockwerk.addSektorB(sektorB);
								
								Carrels carrels = new Carrels(Integer.parseInt(uhrzeit[9]), 0, uhrzeitEnum,
										stockwerk);
								stockwerk.addCarrels(carrels);
								
								Gruppenräume gruppenräume = new Gruppenräume(Integer.parseInt(uhrzeit[10]), Integer.parseInt(uhrzeit[11]), uhrzeitEnum,
										stockwerk);
								stockwerk.addGruppenräume(gruppenräume);
							}
						}

						// Speichert den Wert in der Datenbank
						belegungsDB.updateBelegung(belegung);
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void importEG() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classloader.getResourceAsStream("/import/Belegungsstatistik_Bibliothek_EG.csv");
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
		try (BufferedReader br = new BufferedReader(streamReader)) {

			// Datum der Belegung
			Date datum = null;

			while ((line = br.readLine()) != null) {

				String[] uhrzeit = line.split(cvsSplitBy);

				if (uhrzeit.length >= 1) {

					if (!uhrzeit[1].equals("")) {
						try {
							// Wandelt das Datum in ein Date
							datum = sdf.parse(uhrzeit[1]);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					// Für welche Uhrzeit
					UhrzeitEnum uhrzeitEnum;

					switch (uhrzeit[2]) {
					case "9 Uhr":
						uhrzeitEnum = UhrzeitEnum.NEUN;
						break;
					case "11 Uhr":
						uhrzeitEnum = UhrzeitEnum.ELF;
						break;
					case "13 Uhr":
						uhrzeitEnum = UhrzeitEnum.DREIZEHN;
						break;
					case "15 Uhr":
						uhrzeitEnum = UhrzeitEnum.FÜNFZEHN;
						break;
					case "17 Uhr":
						uhrzeitEnum = UhrzeitEnum.SIEBZEHN;
						break;
					case "19 Uhr":
						uhrzeitEnum = UhrzeitEnum.NEUNZEHN;
						break;
					default:
						uhrzeitEnum = null;
						break;
					}

					if (uhrzeitEnum != null) {
						Belegung belegung = belegungsDB.selectBelegungForDateAndStandort(datum,
								StandortEnum.WINTERTHUR_BB);

						for (Stockwerk stockwerk : belegung.getStockwerkListe()) {
							if (stockwerk.getName() == StockwerkEnum.EG) {

								int personen = 0;
								if (uhrzeit.length >= 3) {
									personen = Integer.parseInt(uhrzeit[3]);
								}
								Arbeitsplätze arbeitsplatz = new Arbeitsplätze(personen, uhrzeitEnum, stockwerk);
								stockwerk.addArbeitsplätze(arbeitsplatz);
								System.out.println(arbeitsplatz.toString() + " " + datum + " " + uhrzeitEnum.toString()
										+ " " + stockwerk.getName().toString());

								int anzahlPersonen = 0;
								int anzahlRäume = 0;
								if (uhrzeit.length >= 5) {
									anzahlPersonen = Integer.parseInt(uhrzeit[4]);
								}
								if (uhrzeit.length == 6) {
									anzahlRäume = Integer.parseInt(uhrzeit[5]);
								}
								Gruppenräume gruppenräume = new Gruppenräume(anzahlPersonen, anzahlRäume, uhrzeitEnum,
										stockwerk);
								stockwerk.addGruppenräume(gruppenräume);
							}
						}

						// Speichert den Wert in der Datenbank
						belegungsDB.updateBelegung(belegung);
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void importZG(String csvFile, StockwerkEnum stockwerkEnum) {

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classloader.getResourceAsStream(csvFile);
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
		try (BufferedReader br = new BufferedReader(streamReader)) {

			// Datum der Belegung
			Date datum = null;

			while ((line = br.readLine()) != null) {

				String[] uhrzeit = line.split(cvsSplitBy);

				if (uhrzeit.length >= 1) {

					if (!uhrzeit[1].equals("")) {
						try {
							// Wandelt das Datum in ein Date
							datum = sdf.parse(uhrzeit[1]);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					// Für welche Uhrzeit
					UhrzeitEnum uhrzeitEnum;

					switch (uhrzeit[2]) {
					case "9 Uhr":
						uhrzeitEnum = UhrzeitEnum.NEUN;
						break;
					case "11 Uhr":
						uhrzeitEnum = UhrzeitEnum.ELF;
						break;
					case "13 Uhr":
						uhrzeitEnum = UhrzeitEnum.DREIZEHN;
						break;
					case "15 Uhr":
						uhrzeitEnum = UhrzeitEnum.FÜNFZEHN;
						break;
					case "17 Uhr":
						uhrzeitEnum = UhrzeitEnum.SIEBZEHN;
						break;
					case "19 Uhr":
						uhrzeitEnum = UhrzeitEnum.NEUNZEHN;
						break;
					default:
						uhrzeitEnum = null;
						break;
					}

					if (uhrzeitEnum != null) {
						Belegung belegung = belegungsDB.selectBelegungForDateAndStandort(datum,
								StandortEnum.WINTERTHUR_BB);

						for (Stockwerk stockwerk : belegung.getStockwerkListe()) {
							if (stockwerk.getName() == stockwerkEnum) {

								int anzahl;
								if (uhrzeit.length == 4) {
									anzahl = 0;
								} else if (uhrzeit[4].equals("")) {
									anzahl = 0;
								} else {
									anzahl = Integer.parseInt(uhrzeit[4]);
								}

								Arbeitsplätze arbeitsplatz = new Arbeitsplätze(anzahl, uhrzeitEnum, stockwerk);
								stockwerk.addArbeitsplätze(arbeitsplatz);
								System.out.println(arbeitsplatz.toString() + " " + datum + " " + uhrzeitEnum.toString()
										+ " " + stockwerk.getName().toString());
							}
						}

						// Speichert den Wert in der Datenbank
						belegungsDB.updateBelegung(belegung);
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
