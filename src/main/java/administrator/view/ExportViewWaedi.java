package administrator.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.vaadin.haijian.Exporter;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import administrator.bean.ExportBelegungKomplettBean;
import administrator.bean.ExportBelegungNormalBean;
import administrator.bean.ExportBenutzungsstatistikBean;
import administrator.bean.ExportWintikurierMonatBean;
import administrator.bean.ExportWintikurierTagBean;
import allgemein.model.StandortEnum;
import allgemein.view.StartseiteView;
import belegung.db.BelegungsDatenbank;
import belegung.model.Arbeitsplätze;
import belegung.model.Belegung;
import belegung.model.Kapazität;
import belegung.model.Stockwerk;
import belegung.model.UhrzeitEnum;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Internerkurier;
import benutzungsstatistik.model.Telefonkontakt;

/**
 * View um alle Daten für Wädenswil zu exportieren
 * 
 * @author Marvin Bindemann
 */
public class ExportViewWaedi extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Export-Waedi";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bExportBenutzung;
	private Button bExportInternerkurierTag;
	private Button bExportInternerkurierMonat;
	private Button bExportBelegung;
	private Button bExportAllBelegung;
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	private BelegungsDatenbank belegungDB = new BelegungsDatenbank();
	private LocalDate startDate;
	private LocalDate endDate;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private Grid<ExportBenutzungsstatistikBean> tabelleBenutzungsstatistik;
	private Grid<ExportWintikurierTagBean> tabelleWintikurierTag;
	private Grid<ExportWintikurierMonatBean> tabelleWintikurierMonat;
	private Grid<ExportBelegungKomplettBean> tabelleBelegungKomplett;
	private Grid<ExportBelegungNormalBean> tabelleBelegungNormal;
	private CheckBoxGroup<String> checkUhrzeit;

	/**
	 * Bildet das AbsoluteLayout, als Wrapper um die ganze View
	 * 
	 * @return AbsoluteLayout
	 */
	private AbsoluteLayout buildMainLayout() {
		mainLayout = new AbsoluteLayout();
		// Setzt die Hintergrundfarbe auf Grün
		mainLayout.addStyleName("backgroundErfassung");
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		return mainLayout;
	}

	/**
	 * Setzt den CompositionRoot auf ein AbsoluteLayout. Ruft initComponents auf,
	 * welches alle Komponenten dem Layout hinzufügt
	 * 
	 * @return AbsoluteLayout
	 */
	public AbsoluteLayout init() {
		AbsoluteLayout absolutLayout = buildMainLayout();
		initData();
		initComponents();

		return absolutLayout;
	}

	public ExportViewWaedi() {
		setCompositionRoot(init());
	}

	@SuppressWarnings("static-access")
	private void initData() {
		// Nimmt den heutigen Tag als Start- und Enddatum
		startDate = startDate.now();
		endDate = endDate.now();
	}

	/**
	 * Initialisieren der GUI Komponente. Fügt alle Komponenten dem Layout hinzu
	 */
	private void initComponents() {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener());
		
		Label lText = new Label();
		lText.setValue("Daten exportieren - Wädenswil");
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		DateField datumVon = new DateField();
		datumVon.setCaption("Datum Von");
		datumVon.setDateFormat("dd.MM.yyyy");
		datumVon.setValue(startDate);
		datumVon.addValueChangeListener(event -> {
			// Setzt das gewählte Datum
			startDate = event.getValue();
		});

		DateField datumBis = new DateField();
		datumBis.setCaption("Datum Bis");
		datumBis.setDateFormat("dd.MM.yyyy");
		datumBis.setValue(endDate);
		datumBis.addValueChangeListener(event -> {
			// Setzt das gewählte Datum
			endDate = event.getValue();
		});

		// Benutzung
		Label lBenutzung = new Label();
		lBenutzung.setValue("Benutzungsstatistik");
		lBenutzung.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		// Download Excelfile für Benutzungsstatistik
		bExportBenutzung = new Button();
		bExportBenutzung.setCaption("Benutzungsstatistik exportieren");
		bExportBenutzung.addClickListener(createClickListener());
		tabelleBenutzungsstatistik = new Grid<>(ExportBenutzungsstatistikBean.class);
		StreamResource excelStreamResource4 = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(tabelleBenutzungsstatistik),
				"Benutzungsstatistik.xls");
		FileDownloader excelFileDownloader4 = new FileDownloader(excelStreamResource4);
		excelFileDownloader4.extend(bExportBenutzung);

		// Download Excelfile für Interner Kurier pro Tag
		bExportInternerkurierTag = new Button();
		bExportInternerkurierTag.setCaption("Interner Kurier pro Tag exportieren");
		bExportInternerkurierTag.addClickListener(createClickListener());
		tabelleWintikurierTag = new Grid<>(ExportWintikurierTagBean.class);
		StreamResource excelStreamResource = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(tabelleWintikurierTag),
				"InternerkurierTag.xls");
		FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
		excelFileDownloader.extend(bExportInternerkurierTag);

		// Download Excelfile für Interner Kurier pro Monat
		bExportInternerkurierMonat = new Button();
		bExportInternerkurierMonat.setCaption("Interner Kurier pro Monat exportieren");
		bExportInternerkurierMonat.addClickListener(createClickListener());
		tabelleWintikurierMonat = new Grid<>(ExportWintikurierMonatBean.class);
		StreamResource excelStreamResource3 = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(tabelleWintikurierMonat),
				"InternerkurierMonat.xls");
		FileDownloader excelFileDownloader3 = new FileDownloader(excelStreamResource3);
		excelFileDownloader3.extend(bExportInternerkurierMonat);

		// Belegung
		Label lBelegung = new Label();
		lBelegung.setValue("Belegung");
		lBelegung.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		// Checkbox um die Zeit zu wählen
		List<String> dataUhrzeit = Arrays.asList("11 Uhr", "15 Uhr");
		checkUhrzeit = new CheckBoxGroup<>("Uhrzeit", dataUhrzeit);
		checkUhrzeit.select(dataUhrzeit.get(1));
		checkUhrzeit.addValueChangeListener(event -> {
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
		});

		// Download Excelfile für Belegung
		bExportBelegung = new Button();
		bExportBelegung.setCaption("Belegung exportieren");
		bExportBelegung.addClickListener(createClickListener());
		tabelleBelegungNormal = new Grid<>(ExportBelegungNormalBean.class);
		StreamResource excelStreamResource6 = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(tabelleBelegungNormal),
				"BelegungNormal.xls");
		FileDownloader excelFileDownloader6 = new FileDownloader(excelStreamResource6);
		excelFileDownloader6.extend(bExportBelegung);

		// Download Excelfile für Belegung detailliert
		bExportAllBelegung = new Button();
		bExportAllBelegung.setCaption("Belegung komplett exportieren");
		bExportAllBelegung.addClickListener(createClickListener());
		tabelleBelegungKomplett = new Grid<>(ExportBelegungKomplettBean.class);
		StreamResource excelStreamResource5 = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(tabelleBelegungKomplett),
				"BelegungKomplett.xls");
		FileDownloader excelFileDownloader5 = new FileDownloader(excelStreamResource5);
		excelFileDownloader5.extend(bExportAllBelegung);

		// Layout
		AbsoluteLayout overallLayout = new AbsoluteLayout();
		overallLayout.addComponent(bZurueck, "top:10%; left:3%");
		overallLayout.addComponent(lText, "top:10%; left:20%");

		// Datum
		HorizontalLayout datumLayout = new HorizontalLayout();
		datumLayout.addComponent(datumVon);
		datumLayout.addComponent(datumBis);
		overallLayout.addComponent(datumLayout, "top:18%; left:3%");

		HorizontalLayout exportLayout = new HorizontalLayout();

		// Benutzung
		VerticalLayout benutzungsLayout = new VerticalLayout();
		benutzungsLayout.addComponent(lBenutzung);
		benutzungsLayout.addComponent(bExportBenutzung);
		benutzungsLayout.addComponent(bExportInternerkurierTag);
		benutzungsLayout.addComponent(bExportInternerkurierMonat);
		exportLayout.addComponent(benutzungsLayout);

		// Belegung
		VerticalLayout belegungsLayout = new VerticalLayout();
		HorizontalLayout uhrzeitStockwerkLayout = new HorizontalLayout();
		uhrzeitStockwerkLayout.addComponent(checkUhrzeit);
		belegungsLayout.addComponent(lBelegung);
		belegungsLayout.addComponent(uhrzeitStockwerkLayout);
		belegungsLayout.addComponent(bExportBelegung);
		belegungsLayout.addComponent(bExportAllBelegung);
		exportLayout.addComponent(belegungsLayout);

		overallLayout.addComponent(exportLayout, "top:25%;");

		mainLayout.addComponent(overallLayout, "left:15%");
	}

	/**
	 * Sammelt alle Daten für den export der Benutzungsstatistik
	 */
	private void exportBenutzungsstatistik() {
		List<ExportBenutzungsstatistikBean> beanListe = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			// Wenn der Wochentag nicht Sonntag ist, Sonntags arbeitet niemand
			if (!getWochentagForDate(date).equals("Sonntag")) {

				Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

				// Such die Benutzungsstatistik für das ausgewählte Datum für Standort Wädenswil
				Benutzungsstatistik benutzungsstatistik = benutzungsstatistikDB
						.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WÄDENSWIL);

				// Setzt die Rechercheberatung
//				beanListe.add(new ExportBenutzungsstatistikBean(getKWForDate(datum), getWochentagForDate(date),
//						sdf.format(datum), "", "Rechercheberatung", benutzungsstatistik.getAnzahl_Rechercheberatung()));

				// Geht durch alle Uhrzeiten eines Tages
				for (int i = 8; i <= 19; i++) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("HH");

					// Setze die Uhrzeit für den Export
					String uhrzeit = getUhrzeitStringByInt(i);

					// Setzt die Email der Benutzungsstatistik
					int emailzaehler = 0;
					for (Emailkontakt e : benutzungsstatistik.getEmailkontaktListe()) {
						if (Integer.parseInt(dateFormat.format(e.getTimestamp().getTime())) == i) {
							emailzaehler++;
						}
					}
					beanListe.add(new ExportBenutzungsstatistikBean(getKWForDate(datum), getWochentagForDate(date),
							sdf.format(datum), uhrzeit, "Email", emailzaehler));

					// Setzt die Intensivfrage der Benutzungsstatistik
					int intensivzaehler = 0;
					for (Intensivfrage in : benutzungsstatistik.getIntensivfrageListe()) {
						if (Integer.parseInt(dateFormat.format(in.getTimestamp().getTime())) == i) {
							intensivzaehler++;
						}
					}
					beanListe.add(new ExportBenutzungsstatistikBean(getKWForDate(datum), getWochentagForDate(date),
							sdf.format(datum), uhrzeit, "Intensive Frage", intensivzaehler));

					// Setzt den Benutzerkontakt der Benutzungsstatistik
					int benutzerzaehler = 0;
					for (Benutzerkontakt k : benutzungsstatistik.getBenutzerkontaktListe()) {
						if (Integer.parseInt(dateFormat.format(k.getTimestamp().getTime())) == i) {
							benutzerzaehler++;
						}
					}
					beanListe.add(new ExportBenutzungsstatistikBean(getKWForDate(datum), getWochentagForDate(date),
							sdf.format(datum), uhrzeit, "Benutzerkontakt", benutzerzaehler));

					// Setzt den Telefonkontakt der Benutzungsstatistik
					int telefonzaehler = 0;
					for (Telefonkontakt t : benutzungsstatistik.getTelefonkontaktListe()) {
						if (Integer.parseInt(dateFormat.format(t.getTimestamp().getTime())) == i) {
							telefonzaehler++;
						}
					}
					beanListe.add(new ExportBenutzungsstatistikBean(getKWForDate(datum), getWochentagForDate(date),
							sdf.format(datum), uhrzeit, "Telefon", telefonzaehler));
				}
			}
		}

		// Stellt die gesamte Liste in einer Tabelle da
		tabelleBenutzungsstatistik.setItems(beanListe);
	}

	/**
	 * Gibt die Uhrzeit im Format 00:00 als String zurück
	 * 
	 * @param i, Uhrzeit als Integer
	 * @return String
	 */
	private String getUhrzeitStringByInt(int i) {

		String uhrzeit;
		if (i < 10) {
			uhrzeit = "0" + i + ":00";
		} else {
			uhrzeit = i + ":00";
		}

		return uhrzeit;
	}

	/**
	 * Sammelt alle Daten für den export des Internen Kuriers pro Tag
	 */
	private void exportInternerkurierTag() {
		List<ExportWintikurierTagBean> beanListe = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			// Wenn der Wochentag nicht Sonntag ist, Sonntags arbeitet niemand
			if (!getWochentagForDate(date).equals("Sonntag")) {

				Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

				// Such den Internen Kurier für das ausgewählte Datum für Wädenswil
				Internerkurier internerkurier = benutzungsstatistikDB
						.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WÄDENSWIL).getInternerkurier();

				// Fügt für den ausgewählten Tag alle Einträge in eine Liste ein
				beanListe.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "Kampus Reidbach", internerkurier.getAnzahl_Reidbach()));
				beanListe.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "Kampus RA", internerkurier.getAnzahl_RA()));
				beanListe.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "Kampus GS", internerkurier.getAnzahl_GS()));
			}
		}

		// Stellt die gesamte Liste in einem Grid zusammen für den Export
		tabelleWintikurierTag.setItems(beanListe);
	}

	/**
	 * Sammelt alle Daten für den export des Internen Kuriers pro Monat
	 */
	private void exportInternerkurierMonat() {
		List<ExportWintikurierTagBean> beanListeTag = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

			// Such den Internen Kurier für das ausgewählte Datum für Wädenswil
			Internerkurier internerkurier = benutzungsstatistikDB
					.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WÄDENSWIL).getInternerkurier();

			if (!getWochentagForDate(date).equals("Sonntag")) {
				// Fügt für den ausgewählten Tag alle Einträge in eine Liste ein
				beanListeTag.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "Kampus Reidbach", internerkurier.getAnzahl_Reidbach()));
				beanListeTag.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "Kampus RA", internerkurier.getAnzahl_RA()));
				beanListeTag.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "Kampus GS", internerkurier.getAnzahl_GS()));
			}
		}

		// Geht durch alle Einträge durch und erstellt ExportWintikurierMonatBean
		List<ExportWintikurierMonatBean> beanListeMonat = new ArrayList<>();

		// Geht durch alle Jahre
		for (int jahr = startDate.getYear(); jahr <= endDate.getYear(); jahr++) {
			// Geht durch alle Monate
			for (int monat = 0; monat <= 11; monat++) {
				// Setzt die Zaehler am Anfang des Monats auf 0
				int zaehlerRE = 0;
				int zaehlerRA = 0;
				int zaehlerGS = 0;

				// Geht durch die Liste aller Einträge für einen Tag
				for (ExportWintikurierTagBean e : beanListeTag) {
					Date datum = null;
					try {
						datum = sdf.parse(e.getDatum());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(datum);
					// Wenn das Jahr und der Monat für einen Eintrag identisch ist, erhöhe Zähler um
					// 1
					if (calendar.get(Calendar.YEAR) == jahr && calendar.get(Calendar.MONTH) == monat) {
						if (e.getDepartement().equals("Kampus Reidbach")) {
							zaehlerRE += e.getTotal();
						} else if (e.getDepartement().equals("Kampus RA")) {
							zaehlerRA += e.getTotal();
						} else if (e.getDepartement().equals("Kampus GS")) {
							zaehlerGS += e.getTotal();
						}
					}
				}

				// Erstellt Einträge für einen Monat
				beanListeMonat
						.add(new ExportWintikurierMonatBean(getMonatForInt(monat), jahr, "Kampus Reidbach", zaehlerRE));
				beanListeMonat.add(new ExportWintikurierMonatBean(getMonatForInt(monat), jahr, "Kampus RA", zaehlerRA));
				beanListeMonat.add(new ExportWintikurierMonatBean(getMonatForInt(monat), jahr, "Kampus GS", zaehlerGS));
			}
		}

		// Stellt die gesamte Liste in einem Grid zusammen für den Export
		tabelleWintikurierMonat.setItems(beanListeMonat);
	}

	/**
	 * Sammelt alle Daten für den export der Belegung - Komplett und detailliert
	 */
	private void exportBelegungKomplett() {
		List<ExportBelegungKomplettBean> beanListe = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			// Wenn der Wochentag nicht Sonntag ist, Sonntags arbeitet niemand
			if (!getWochentagForDate(date).equals("Sonntag")) {

				Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

				// Such die Benutzungsstatistik für das ausgewählte Datum für Wädenswil
				Belegung belegung = belegungDB.selectBelegungForDateAndStandort(datum, StandortEnum.WÄDENSWIL);
				String bereich = "Wädenswil";

				// Geht durch das Stockwerk
				for (Stockwerk stockwerk : belegung.getStockwerkListe()) {
					String stockwerkName = "EG";
					Kapazität kapazität = stockwerk.getKapzität();

					List<UhrzeitEnum> enumListe = new ArrayList<>();
					enumListe.add(UhrzeitEnum.ELF);
					enumListe.add(UhrzeitEnum.FÜNFZEHN);

					for (UhrzeitEnum uhrzeitEnum : enumListe) {

						String uhrzeit = null;

						switch (uhrzeitEnum) {
						case ELF:
							uhrzeit = getUhrzeitStringByInt(11);
							break;
						case FÜNFZEHN:
							uhrzeit = getUhrzeitStringByInt(15);
							break;
						default:
							break;
						}

						for (Arbeitsplätze arbeitsplätze : stockwerk.getArbeitsplatzListe()) {
							if (uhrzeitEnum == arbeitsplätze.getUhrzeit()) {
								int auslastung = arbeitsplätze.getAnzahlPersonen() * 100
										/ kapazität.getHunderProzentArbeitsplätze();
								beanListe.add(new ExportBelegungKomplettBean(getKWForDate(datum),
										getWochentagForDate(date), sdf.format(datum), uhrzeit, bereich, stockwerkName,
										"Arbeitsplätze", arbeitsplätze.getAnzahlPersonen(), auslastung + "%"));
							}
						}
					}
				}

			}
		}

		// Stellt die gesamte Liste in einem Grid zusammen für den Export
		tabelleBelegungKomplett.setItems(beanListe);
	}

	/**
	 * Sammelt alle Daten für den export der Belegung - Normal, zusammengezählt
	 */
	private void exportBelegungNormal() {
		List<ExportBelegungNormalBean> beanListe = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			// Wenn der Wochentag nicht Sonntag ist, Sonntags arbeitet niemand
			if (!getWochentagForDate(date).equals("Sonntag")) {

				Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

				// Such die Benutzungsstatistik für das ausgewählte Datum für Wädenswil
				Belegung belegung = belegungDB.selectBelegungForDateAndStandort(datum, StandortEnum.WÄDENSWIL);

				List<UhrzeitEnum> enumListe = new ArrayList<>();
				Set<String> selectedUhrzeit = checkUhrzeit.getSelectedItems();
				// Nur die ausgewählten Objekte der Checkbox für die Datensammlung benutzen
				for (String s : selectedUhrzeit) {
					if (s.equals("11 Uhr"))
						enumListe.add(UhrzeitEnum.ELF);
					if (s.equals("15 Uhr"))
						enumListe.add(UhrzeitEnum.FÜNFZEHN);
				}

				// Geht durch alle Uhrzeiten
				for (UhrzeitEnum uhrzeitEnum : enumListe) {

					String uhrzeit = null;

					switch (uhrzeitEnum) {
					case ELF:
						uhrzeit = getUhrzeitStringByInt(11);
						break;
					case FÜNFZEHN:
						uhrzeit = getUhrzeitStringByInt(15);
						break;
					default:
						break;
					}

					// Geht durch Bibliothek
					String bereich = "Wädenswil";

					int maxAlleKapazitäten = 0;
					int personenZaehler = 0;

					// Geht durch das Stockwerk
					for (Stockwerk stockwerk : belegung.getStockwerkListe()) {

						Kapazität kapazität = stockwerk.getKapzität();
						int maxKapazität = kapazität.getHunderProzentArbeitsplätze();
						maxAlleKapazitäten += maxKapazität;

						for (Arbeitsplätze arbeitsplätze : stockwerk.getArbeitsplatzListe()) {
							if (uhrzeitEnum == arbeitsplätze.getUhrzeit()) {
								personenZaehler += arbeitsplätze.getAnzahlPersonen();
							}
						}

						int auslastung = personenZaehler * 100 / maxAlleKapazitäten;
						beanListe.add(new ExportBelegungNormalBean(getKWForDate(datum), getWochentagForDate(date),
								sdf.format(datum), uhrzeit, bereich, personenZaehler, auslastung + "%"));
					}
				}
			}
		}

		// Stellt die gesamte Liste in einem Grid zusammen für den Export
		tabelleBelegungNormal.setItems(beanListe);
	}

	/**
	 * Gibt einen Monat als String zurück für eine Zahl
	 * 
	 * @param monat
	 * @return String
	 */
	private String getMonatForInt(int monat) {

		switch (monat) {
		case 0:
			return "Januar";
		case 1:
			return "Februar";
		case 2:
			return "März";
		case 3:
			return "April";
		case 4:
			return "Mai";
		case 5:
			return "Juni";
		case 6:
			return "Juli";
		case 7:
			return "August";
		case 8:
			return "September";
		case 9:
			return "Oktober";
		case 10:
			return "November";
		case 11:
			return "Dezember";
		}

		return null;
	}

	/**
	 * Gibt den Wochentag als String zurück für ein Datum
	 * 
	 * @param date
	 * @return String
	 */
	private String getWochentagForDate(LocalDate date) {

		switch (date.getDayOfWeek().toString()) {
		case "MONDAY":
			return "Montag";
		case "TUESDAY":
			return "Dienstag";
		case "WEDNESDAY":
			return "Mittwoch";
		case "THURSDAY":
			return "Donnerstag";
		case "FRIDAY":
			return "Freitag";
		case "SATURDAY":
			return "Samstag";
		case "SUNDAY":
			return "Sonntag";
		}

		return null;
	}

	/**
	 * Ermittelt die KW für ein Datum
	 * 
	 * @param datum
	 * @return int
	 */
	private int getKWForDate(Date datum) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(datum);
		int kw = cal.get(Calendar.WEEK_OF_YEAR);
		return kw;
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {

				if (e.getSource() == bZurueck) {
					getUI().getNavigator().navigateTo(StartseiteView.NAME);
				}
				
				if (e.getSource() == bExportBenutzung) {
					exportBenutzungsstatistik();
				}

				if (e.getSource() == bExportInternerkurierTag) {
					exportInternerkurierTag();
				}

				if (e.getSource() == bExportInternerkurierMonat) {
					exportInternerkurierMonat();
				}

				if (e.getSource() == bExportBelegung) {
					exportBelegungNormal();
				}

				if (e.getSource() == bExportAllBelegung) {
					exportBelegungKomplett();
				}

			}
		};

	}

}
