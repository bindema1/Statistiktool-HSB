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
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.BeantwortungBibliothekspersonal;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.ExterneGruppe;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Telefonkontakt;
import benutzungsstatistik.model.Wintikurier;

public class Benutzungsimporter {

	private String line = "";
	// CSV separiert mit ;
	private String cvsSplitBy = ";";
	private BenutzungsstatistikDatenbank benutzungsDB = new BenutzungsstatistikDatenbank();
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	public Benutzungsimporter() {
		importBenutzungsstatistikBB();
		importBenutzungsstatistikWintikurier();
		importBenutzungsstatistikExterneGruppen();
		importBenutzungsstatistikLL();
	}

	private void importBenutzungsstatistikBB() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classloader.getResourceAsStream("/import/Benutzungsstatistik_Auswertung_EG.csv");
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

					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
					Date timestamp = null;
					boolean email = false;
					boolean telefon = false;
					boolean intensiv = false;

					try {
						switch (uhrzeit[2]) {
						case "8 - 9 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 08:30");
							break;
						case "9 - 10 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 09:30");
							break;
						case "10 - 11 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 10:30");
							break;
						case "11 - 12 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 11:30");
							break;
						case "12 - 13 Uhr":
							SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
							timestamp = sdf4.parse(sdf2.format(datum) + " 12:30 PM");
							break;
						case "13 - 14 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 13:30");
							break;
						case "14 - 15 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 14:30");
							break;
						case "15 - 16 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 15:30");
							break;
						case "16 - 17 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 16:30");
							break;
						case "17 - 18 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 17:30");
							break;
						case "18 - 19 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 18:30");
							break;
						case "19 - 20 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 19:30");
							break;
						case "Telefon":
							if (uhrzeit[0].equals("Samstag,")) {
								timestamp = sdf3.parse(sdf2.format(datum) + " 15:55");
							} else {
								timestamp = sdf3.parse(sdf2.format(datum) + " 19:55");
							}
							telefon = true;
							break;
						case "Email":
							if (uhrzeit[0].equals("Samstag,")) {
								timestamp = sdf3.parse(sdf2.format(datum) + " 15:55");
							} else {
								timestamp = sdf3.parse(sdf2.format(datum) + " 19:55");
							}
							email = true;
							break;
						case "Intensiv":
							if (uhrzeit[0].equals("Samstag,")) {
								timestamp = sdf3.parse(sdf2.format(datum) + " 15:55");
							} else {
								timestamp = sdf3.parse(sdf2.format(datum) + " 19:55");
							}
							intensiv = true;
							break;
						default:
							timestamp = null;
							break;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (timestamp != null) {
						Benutzungsstatistik benutzungsstatistik = benutzungsDB
								.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WINTERTHUR_BB);

						int anzahl = Integer.parseInt(uhrzeit[3]);

						if (intensiv == false && email == false && telefon == false) {
							for (int i = 1; i <= anzahl; i++) {
								benutzungsstatistik
										.addBenutzerkontakt(new Benutzerkontakt(timestamp, benutzungsstatistik));
							}

							// Intensivfragen nach Uhrzeit
							int anzahlIntensiv = 0;
							if (uhrzeit.length >= 5) {
								anzahlIntensiv = Integer.parseInt(uhrzeit[4]);
							}
							for (int i = 1; i <= anzahlIntensiv; i++) {
								benutzungsstatistik.addIntensivfrage(new Intensivfrage(timestamp, benutzungsstatistik));
							}

							// Rechercheberatung
							int anzahlRecherche = 0;
							if (uhrzeit.length >= 6) {
								anzahlRecherche = Integer.parseInt(uhrzeit[5]);
							}
							int anzahl_Rechercheberatung = benutzungsstatistik.getAnzahl_Rechercheberatung()
									+ anzahlRecherche;
							benutzungsstatistik.setAnzahl_Rechercheberatung(anzahl_Rechercheberatung);

						} else if (email == true) {

							if (uhrzeit.length >= 5) {
								anzahl += Integer.parseInt(uhrzeit[4]);
							}
							if (uhrzeit.length >= 6) {
								anzahl += Integer.parseInt(uhrzeit[5]);
							}
							for (int i = 1; i <= anzahl; i++) {
								benutzungsstatistik.addEmailkontakt(new Emailkontakt(timestamp, benutzungsstatistik));
							}
						} else if (telefon == true) {
							if (uhrzeit.length >= 5) {
								anzahl += Integer.parseInt(uhrzeit[4]);
							}
							if (uhrzeit.length >= 6) {
								anzahl += Integer.parseInt(uhrzeit[5]);
							}
							for (int i = 1; i <= anzahl; i++) {
								benutzungsstatistik
										.addTelefonkontakt(new Telefonkontakt(timestamp, benutzungsstatistik));
							}
						} else if (intensiv == true) {
							if (uhrzeit.length >= 5) {
								anzahl += Integer.parseInt(uhrzeit[4]);
							}
							if (uhrzeit.length >= 6) {
								anzahl += Integer.parseInt(uhrzeit[5]);
							}
							for (int i = 1; i <= anzahl; i++) {
								benutzungsstatistik.addIntensivfrage(new Intensivfrage(timestamp, benutzungsstatistik));
							}
						}
						System.out.println(datum + " " + timestamp);

						// Speichert den Wert in der Datenbank
						benutzungsDB.updateBenutzungsstatistik(benutzungsstatistik);
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void importBenutzungsstatistikWintikurier() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classloader.getResourceAsStream("/import/Benutzungsstatistik_Wintikurier.csv");
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

					Benutzungsstatistik benutzungsstatistik = benutzungsDB
							.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WINTERTHUR_BB);

					Wintikurier wintikurier = benutzungsstatistik.getWintikurier();
					wintikurier.setAnzahl_Gesundheit(Integer.parseInt(uhrzeit[2]));
					wintikurier.setAnzahl_Linguistik(Integer.parseInt(uhrzeit[3]));
					wintikurier.setAnzahl_Technik(Integer.parseInt(uhrzeit[4]));
					wintikurier.setAnzahl_Wirtschaft(Integer.parseInt(uhrzeit[5]));

					System.out.println(datum + "  " + wintikurier.toString());

					// Speichert den Wert in der Datenbank
					benutzungsDB.updateBenutzungsstatistik(benutzungsstatistik);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void importBenutzungsstatistikExterneGruppen() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classloader.getResourceAsStream("/import/Benutzungsstatistik_ExterneGruppen.csv");
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
		try (BufferedReader br = new BufferedReader(streamReader)) {

			// Datum der Belegung
			Date datum = null;

			while ((line = br.readLine()) != null) {

				String[] uhrzeit = line.split(cvsSplitBy);

				if (uhrzeit.length >= 1) {

					try {
						// Wandelt das Datum in ein Date
						datum = sdf.parse(uhrzeit[0]);
					} catch (ParseException e) {
						e.printStackTrace();
					}

					Benutzungsstatistik benutzungsstatistik = benutzungsDB
							.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WINTERTHUR_BB);

					ExterneGruppe externeGruppe = new ExterneGruppe(uhrzeit[2], Integer.parseInt(uhrzeit[3]), uhrzeit[1], benutzungsstatistik);
					benutzungsstatistik.addExterneGruppe(externeGruppe);

					System.out.println(datum + "  " + externeGruppe.toString());

					// Speichert den Wert in der Datenbank
					benutzungsDB.updateBenutzungsstatistik(benutzungsstatistik);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void importBenutzungsstatistikLL() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classloader.getResourceAsStream("/import/Benutzungsstatistik_Auswertung_LL.csv");
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

					SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
					SimpleDateFormat sdf3 = new SimpleDateFormat("dd.MM.yyyy hh:mm");
					Date timestamp = null;

					try {
						switch (uhrzeit[2]) {
						case "8 - 9 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 08:30");
							break;
						case "9 - 10 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 09:30");
							break;
						case "10 - 11 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 10:30");
							break;
						case "11 - 12 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 11:30");
							break;
						case "12 - 13 Uhr":
							SimpleDateFormat sdf4 = new SimpleDateFormat("dd.MM.yyyy hh:mm a");
							timestamp = sdf4.parse(sdf2.format(datum) + " 12:30 PM");
							break;
						case "13 - 14 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 13:30");
							break;
						case "14 - 15 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 14:30");
							break;
						case "15 - 16 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 15:30");
							break;
						case "16 - 17 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 16:30");
							break;
						case "17 - 18 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 17:30");
							break;
						case "18 - 19 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 18:30");
							break;
						case "19 - 20 Uhr":
							timestamp = sdf3.parse(sdf2.format(datum) + " 19:30");
							break;
						default:
							timestamp = null;
							break;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (timestamp != null) {
						Benutzungsstatistik benutzungsstatistik = benutzungsDB
								.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WINTERTHUR_LL);

						// Benutzerkontakt
						int anzahl = Integer.parseInt(uhrzeit[3]);
						for (int i = 1; i <= anzahl; i++) {
							benutzungsstatistik.addBenutzerkontakt(new Benutzerkontakt(timestamp, benutzungsstatistik));
						}

						// Intensivfragen nach Uhrzeit
						int anzahlIntensiv = 0;
						if (uhrzeit.length >= 5) {
							anzahlIntensiv = Integer.parseInt(uhrzeit[4]);
						}
						for (int i = 1; i <= anzahlIntensiv; i++) {
							benutzungsstatistik.addIntensivfrage(new Intensivfrage(timestamp, benutzungsstatistik));
						}

						// Rechercheberatung
						int anzahlBeantwortung = 0;
						if (uhrzeit.length >= 6) {
							anzahlBeantwortung = Integer.parseInt(uhrzeit[5]);
						}
						for (int i = 1; i <= anzahlBeantwortung; i++) {
							benutzungsstatistik.addBeantwortungBibliothekspersonal(
									new BeantwortungBibliothekspersonal(timestamp, benutzungsstatistik));
						}
						System.out.println(datum + " " + timestamp);

						// Speichert den Wert in der Datenbank
						benutzungsDB.updateBenutzungsstatistik(benutzungsstatistik);
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
