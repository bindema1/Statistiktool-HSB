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
import administrator.bean.ExportExterneGruppeBean;
import administrator.bean.ExportWintikurierMonatBean;
import administrator.bean.ExportWintikurierTagBean;
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
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.ExterneGruppe;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Telefonkontakt;
import benutzungsstatistik.model.Wintikurier;

public class ExportView extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Export";
	private AbsoluteLayout mainLayout;
	private Button bExportBenutzung;
	private Button bExportWintikurierTag;
	private Button bExportWintikurierMonat;
	private Button bExportGruppen;
	private Button bExportBelegung;
	private Button bExportAllBelegung;
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	private BelegungsDatenbank belegungDB = new BelegungsDatenbank();
	private LocalDate startDate;
	private LocalDate endDate;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private Grid<ExportExterneGruppeBean> tabelleExterneGruppen;
	private Grid<ExportBenutzungsstatistikBean> tabelleBenutzungsstatistik;
	private Grid<ExportWintikurierTagBean> tabelleWintikurierTag;
	private Grid<ExportWintikurierMonatBean> tabelleWintikurierMonat;
	private Grid<ExportBelegungKomplettBean> tabelleBelegungKomplett;
	private Grid<ExportBelegungNormalBean> tabelleBelegungNormal;
	private CheckBoxGroup<String> checkUhrzeit;
	private CheckBoxGroup<String> checkStockwerk;

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		// mainLayout.setHeight("100%");

		return mainLayout;
	}

	public AbsoluteLayout init() {
		// common part: create layout
		AbsoluteLayout absolutLayout = buildMainLayout();
		initData();
		initComponents();

		return absolutLayout;
	}

	public ExportView() {
		setCompositionRoot(init());
	}

	@SuppressWarnings("static-access")
	private void initData() {
		startDate = startDate.now();
		endDate = endDate.now();
	}

	// Initialisieren der GUI Komponente
	private void initComponents() {

		Label lText = new Label();
		lText.setValue("Daten exportieren - Winterthur");
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		DateField datumVon = new DateField();
		datumVon.setCaption("Datum Von");
		datumVon.setDateFormat("dd.MM.yyyy");
		datumVon.setValue(startDate);
		datumVon.addValueChangeListener(event -> {
			startDate = event.getValue();
		});

		DateField datumBis = new DateField();
		datumBis.setCaption("Datum Bis");
		datumBis.setDateFormat("dd.MM.yyyy");
		datumBis.setValue(endDate);
		datumBis.addValueChangeListener(event -> {
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

		// Download Excelfile für Wintikurier pro Tag
		bExportWintikurierTag = new Button();
		bExportWintikurierTag.setCaption("Wintikurier pro Tag exportieren");
		bExportWintikurierTag.addClickListener(createClickListener());
		tabelleWintikurierTag = new Grid<>(ExportWintikurierTagBean.class);
		StreamResource excelStreamResource = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(tabelleWintikurierTag),
				"WintikurierTag.xls");
		FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
		excelFileDownloader.extend(bExportWintikurierTag);

		// Download Excelfile für Wintikurier pro Monat
		bExportWintikurierMonat = new Button();
		bExportWintikurierMonat.setCaption("Wintikurier pro Monat exportieren");
		bExportWintikurierMonat.addClickListener(createClickListener());
		tabelleWintikurierMonat = new Grid<>(ExportWintikurierMonatBean.class);
		StreamResource excelStreamResource3 = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(tabelleWintikurierMonat),
				"WintikurierMonat.xls");
		FileDownloader excelFileDownloader3 = new FileDownloader(excelStreamResource3);
		excelFileDownloader3.extend(bExportWintikurierMonat);

		// Download Excelfile für Externe Gruppen
		bExportGruppen = new Button();
		bExportGruppen.setCaption("Externe Gruppen exportieren");
		bExportGruppen.addClickListener(createClickListener());
		tabelleExterneGruppen = new Grid<>(ExportExterneGruppeBean.class);
		StreamResource excelStreamResource2 = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(tabelleExterneGruppen), "ExterneGruppe.xls");
		FileDownloader excelFileDownloader2 = new FileDownloader(excelStreamResource2);
		excelFileDownloader2.extend(bExportGruppen);

		// Belegung
		Label lBelegung = new Label();
		lBelegung.setValue("Belegung");
		lBelegung.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		List<String> dataUhrzeit = Arrays.asList("9 Uhr", "11 Uhr", "13 Uhr", "15 Uhr", "17 Uhr", "19 Uhr");
		checkUhrzeit = new CheckBoxGroup<>("Uhrzeit", dataUhrzeit);
		checkUhrzeit.select(dataUhrzeit.get(3));
		checkUhrzeit.addValueChangeListener(event -> {
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
		});

		bExportBelegung = new Button();
		bExportBelegung.setCaption("Belegung exportieren");
		bExportBelegung.setEnabled(false);
		bExportBelegung.addClickListener(createClickListener());
		tabelleBelegungNormal = new Grid<>(ExportBelegungNormalBean.class);
		StreamResource excelStreamResource6 = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(tabelleBelegungNormal),
				"BelegungNormal.xls");
		FileDownloader excelFileDownloader6 = new FileDownloader(excelStreamResource6);
		excelFileDownloader6.extend(bExportBelegung);

		List<String> dataStockwerk = Arrays.asList("Lernlandschaft", "Bibliothek");
		checkStockwerk = new CheckBoxGroup<>("Stockwerk", dataStockwerk);
		checkStockwerk.addValueChangeListener(event -> {
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);

			if (event.getValue().contains(dataStockwerk.get(0)) || event.getValue().contains(dataStockwerk.get(1))) {
				bExportBelegung.setEnabled(true);
			} else {
				bExportBelegung.setEnabled(false);
			}
		});

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
		overallLayout.addComponent(lText, "top:10%; left:5%");

		// Datum
		HorizontalLayout datumLayout = new HorizontalLayout();
		datumLayout.addComponent(datumVon);
		datumLayout.addComponent(datumBis);
		overallLayout.addComponent(datumLayout, "top:18%; left:5%");

		HorizontalLayout exportLayout = new HorizontalLayout();

		// Benutzung
		VerticalLayout benutzungsLayout = new VerticalLayout();
		benutzungsLayout.addComponent(lBenutzung);
		benutzungsLayout.addComponent(bExportBenutzung);
		benutzungsLayout.addComponent(bExportGruppen);
		benutzungsLayout.addComponent(bExportWintikurierTag);
		benutzungsLayout.addComponent(bExportWintikurierMonat);
		exportLayout.addComponent(benutzungsLayout);

		// Belegung
		VerticalLayout belegungsLayout = new VerticalLayout();
		HorizontalLayout uhrzeitStockwerkLayout = new HorizontalLayout();
		uhrzeitStockwerkLayout.addComponent(checkUhrzeit);
		uhrzeitStockwerkLayout.addComponent(checkStockwerk);
		belegungsLayout.addComponent(lBelegung);
		belegungsLayout.addComponent(uhrzeitStockwerkLayout);
		belegungsLayout.addComponent(bExportBelegung);
		belegungsLayout.addComponent(bExportAllBelegung);
		exportLayout.addComponent(belegungsLayout);

		overallLayout.addComponent(exportLayout, "top:25%;");

		mainLayout.addComponent(overallLayout, "left:25%");
	}

	/**
	 * Sammelt alle Daten für den export der Externen Gruppe
	 */
	private void exportExterneGruppe() {
		List<ExportExterneGruppeBean> beanListe = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

			Benutzungsstatistik benutzungsstatistik = benutzungsstatistikDB
					.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WINTERTHUR_BB);

			for (ExterneGruppe eg : benutzungsstatistik.getExterneGruppeListe()) {
				beanListe.add(new ExportExterneGruppeBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), eg.getName(), eg.getAnzahl_Personen()));
			}
		}

		tabelleExterneGruppen.setItems(beanListe);
	}

	/**
	 * Sammelt alle Daten für den export der Benutzungsstatistik
	 */
	private void exportBenutzungsstatistik() {
		List<ExportBenutzungsstatistikBean> beanListe = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			if (!getWochentagForDate(date).equals("Sonntag")) {

				Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

				Benutzungsstatistik benutzungsstatistik = benutzungsstatistikDB
						.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WINTERTHUR_BB);

				beanListe.add(new ExportBenutzungsstatistikBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "", "Rechercheberatung", benutzungsstatistik.getAnzahl_Rechercheberatung()));
				
				// Geht durch alle Uhrzeiten eines Tages
				for (int i = 8; i <= 19; i++) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("HH");

					// Setze die Uhrzeit für den Export
					String uhrzeit = getUhrzeitStringByInt(i);

					int emailzaehler = 0;
					for (Emailkontakt e : benutzungsstatistik.getEmailkontaktListe()) {
						if (Integer.parseInt(dateFormat.format(e.getTimestamp().getTime())) == i) {
							emailzaehler++;
						}
					}
					beanListe.add(new ExportBenutzungsstatistikBean(getKWForDate(datum), getWochentagForDate(date),
							sdf.format(datum), uhrzeit, "Email", emailzaehler));

					int intensivzaehler = 0;
					for (Intensivfrage in : benutzungsstatistik.getIntensivfrageListe()) {
						if (Integer.parseInt(dateFormat.format(in.getTimestamp().getTime())) == i) {
							intensivzaehler++;
						}
					}
					beanListe.add(new ExportBenutzungsstatistikBean(getKWForDate(datum), getWochentagForDate(date),
							sdf.format(datum), uhrzeit, "Intensive Frage", intensivzaehler));

					int benutzerzaehler = 0;
					for (Benutzerkontakt k : benutzungsstatistik.getBenutzerkontaktListe()) {
						if (Integer.parseInt(dateFormat.format(k.getTimestamp().getTime())) == i) {
							benutzerzaehler++;
						}
					}
					beanListe.add(new ExportBenutzungsstatistikBean(getKWForDate(datum), getWochentagForDate(date),
							sdf.format(datum), uhrzeit, "Benutzerkontakt", benutzerzaehler));

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
	 * Sammelt alle Daten für den export des Wintikuriers pro Tag
	 */
	private void exportWintikurierTag() {
		List<ExportWintikurierTagBean> beanListe = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			if (!getWochentagForDate(date).equals("Sonntag")) {

				Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

				Wintikurier wintikurier = benutzungsstatistikDB
						.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WINTERTHUR_BB)
						.getWintikurier();

				beanListe.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "G", wintikurier.getAnzahl_Gesundheit()));
				beanListe.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "L", wintikurier.getAnzahl_Linguistik()));
				beanListe.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "T", wintikurier.getAnzahl_Technik()));
				beanListe.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "W", wintikurier.getAnzahl_Wirtschaft()));
			}
		}

		tabelleWintikurierTag.setItems(beanListe);
	}

	/**
	 * Sammelt alle Daten für den export des Wintikuriers pro Monat
	 */
	private void exportWintikurierMonat() {
		List<ExportWintikurierTagBean> beanListeTag = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

			Wintikurier wintikurier = benutzungsstatistikDB
					.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WINTERTHUR_BB).getWintikurier();

			if (!getWochentagForDate(date).equals("Sonntag")) {
				beanListeTag.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "G", wintikurier.getAnzahl_Gesundheit()));
				beanListeTag.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "L", wintikurier.getAnzahl_Linguistik()));
				beanListeTag.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "T", wintikurier.getAnzahl_Technik()));
				beanListeTag.add(new ExportWintikurierTagBean(getKWForDate(datum), getWochentagForDate(date),
						sdf.format(datum), "W", wintikurier.getAnzahl_Wirtschaft()));
			}
		}

		// Geht durch alle Einträge durch und erstellt ExportWintikurierMonatBean
		List<ExportWintikurierMonatBean> beanListeMonat = new ArrayList<>();

		for (int jahr = startDate.getYear(); jahr <= endDate.getYear(); jahr++) {
			for (int monat = 0; monat <= 11; monat++) {
				int zaehlerG = 0;
				int zaehlerL = 0;
				int zaehlerT = 0;
				int zaehlerW = 0;

				for (ExportWintikurierTagBean e : beanListeTag) {
					Date datum = null;
					try {
						datum = sdf.parse(e.getDatum());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(datum);
					if (calendar.get(Calendar.YEAR) == jahr && calendar.get(Calendar.MONTH) == monat) {
						if (e.getDepartement().equals("G")) {
							zaehlerG += e.getTotal();
						} else if (e.getDepartement().equals("L")) {
							zaehlerL += e.getTotal();
						} else if (e.getDepartement().equals("T")) {
							zaehlerT += e.getTotal();
						} else if (e.getDepartement().equals("W")) {
							zaehlerW += e.getTotal();
						}
					}
				}

				beanListeMonat.add(new ExportWintikurierMonatBean(getMonatForInt(monat), jahr, "G", zaehlerG));
				beanListeMonat.add(new ExportWintikurierMonatBean(getMonatForInt(monat), jahr, "L", zaehlerL));
				beanListeMonat.add(new ExportWintikurierMonatBean(getMonatForInt(monat), jahr, "T", zaehlerT));
				beanListeMonat.add(new ExportWintikurierMonatBean(getMonatForInt(monat), jahr, "W", zaehlerW));
			}
		}

		tabelleWintikurierMonat.setItems(beanListeMonat);
	}

	/**
	 * Sammelt alle Daten für den export der Belegung - Komplett und detailliert
	 */
	private void exportBelegungKomplett() {
		List<ExportBelegungKomplettBean> beanListe = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			if (!getWochentagForDate(date).equals("Sonntag")) {

				Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

				Belegung belegungBB = belegungDB.selectBelegungForDateAndStandort(datum, StandortEnum.WINTERTHUR_BB);
				Belegung belegungLL = belegungDB.selectBelegungForDateAndStandort(datum, StandortEnum.WINTERTHUR_LL);
				List<Belegung> belegungsListe = new ArrayList<>();
				belegungsListe.add(belegungBB);
				belegungsListe.add(belegungLL);

				// Geht durch Bibliothek und Lernlandschaft
				for (Belegung belegung : belegungsListe) {

					String bereich = null;
					if (belegung.getStandort() == StandortEnum.WINTERTHUR_LL) {
						bereich = "Lernlandschaft";
					} else if (belegung.getStandort() == StandortEnum.WINTERTHUR_BB) {
						bereich = "Bibliothek";
					}

					// Geht durch alle 4 Stockwerke, EG, 1.ZG, 2.ZG, LL
					for (Stockwerk stockwerk : belegung.getStockwerkListe()) {

						String stockwerkName = null;
						if (stockwerk.getName() == StockwerkEnum.EG) {
							stockwerkName = "EG";
						} else if (stockwerk.getName() == StockwerkEnum.ZG1) {
							stockwerkName = "1.ZG";
						} else if (stockwerk.getName() == StockwerkEnum.ZG2) {
							stockwerkName = "2.ZG";
						} else if (stockwerk.getName() == StockwerkEnum.LL) {
							stockwerkName = "Lernlandschaft";
						}

						Kapazität kapazität = stockwerk.getKapzität();

						List<UhrzeitEnum> enumListe = new ArrayList<>();
						enumListe.add(UhrzeitEnum.NEUN);
						enumListe.add(UhrzeitEnum.ELF);
						enumListe.add(UhrzeitEnum.DREIZEHN);
						enumListe.add(UhrzeitEnum.FÜNFZEHN);
						enumListe.add(UhrzeitEnum.SIEBZEHN);
						enumListe.add(UhrzeitEnum.NEUNZEHN);

						for (UhrzeitEnum uhrzeitEnum : enumListe) {

							String uhrzeit = null;

							switch (uhrzeitEnum) {
							case NEUN:
								uhrzeit = getUhrzeitStringByInt(9);
								break;
							case ELF:
								uhrzeit = getUhrzeitStringByInt(11);
								break;
							case DREIZEHN:
								uhrzeit = getUhrzeitStringByInt(13);
								break;
							case FÜNFZEHN:
								uhrzeit = getUhrzeitStringByInt(15);
								break;
							case SIEBZEHN:
								uhrzeit = getUhrzeitStringByInt(17);
								break;
							case NEUNZEHN:
								uhrzeit = getUhrzeitStringByInt(19);
								break;
							}

							for (Arbeitsplätze arbeitsplätze : stockwerk.getArbeitsplatzListe()) {
								if (uhrzeitEnum == arbeitsplätze.getUhrzeit()) {
									int auslastung = arbeitsplätze.getAnzahlPersonen() * 100
											/ kapazität.getMaxArbeitsplätze();
									beanListe.add(new ExportBelegungKomplettBean(getKWForDate(datum),
											getWochentagForDate(date), sdf.format(datum), uhrzeit, bereich,
											stockwerkName, "Arbeitsplätze", arbeitsplätze.getAnzahlPersonen(),
											auslastung + "%"));
								}
							}

							for (SektorA sektorA : stockwerk.getSektorAListe()) {
								if (uhrzeitEnum == sektorA.getUhrzeit()) {
									int auslastung = sektorA.getAnzahlPersonen() * 100 / kapazität.getMaxSektorA();
									beanListe.add(new ExportBelegungKomplettBean(getKWForDate(datum),
											getWochentagForDate(date), sdf.format(datum), uhrzeit, bereich,
											stockwerkName, "Sektor A", sektorA.getAnzahlPersonen(), auslastung + "%"));
								}
							}

							for (SektorB sektorB : stockwerk.getSektorBListe()) {
								if (uhrzeitEnum == sektorB.getUhrzeit()) {
									int auslastung = sektorB.getAnzahlPersonen() * 100 / kapazität.getMaxSektorB();
									beanListe.add(new ExportBelegungKomplettBean(getKWForDate(datum),
											getWochentagForDate(date), sdf.format(datum), uhrzeit, bereich,
											stockwerkName, "Sektor B", sektorB.getAnzahlPersonen(), auslastung + "%"));
								}
							}

							for (Gruppenräume gruppenräume : stockwerk.getGruppenräumeListe()) {
								if (uhrzeitEnum == gruppenräume.getUhrzeit()) {
									int auslastung = gruppenräume.getAnzahlPersonen() * 100
											/ kapazität.getMaxGruppenräume();
									beanListe.add(new ExportBelegungKomplettBean(getKWForDate(datum),
											getWochentagForDate(date), sdf.format(datum), uhrzeit, bereich,
											stockwerkName, "Gruppenräume - Räume", gruppenräume.getAnzahlRäume(),
											auslastung + "%"));
									beanListe.add(new ExportBelegungKomplettBean(getKWForDate(datum),
											getWochentagForDate(date), sdf.format(datum), uhrzeit, bereich,
											stockwerkName, "Gruppenräume - Personen", gruppenräume.getAnzahlPersonen(),
											auslastung + "%"));
								}
							}

							for (Carrels carrels : stockwerk.getCarrelsListe()) {
								if (uhrzeitEnum == carrels.getUhrzeit()) {
									int auslastung = carrels.getAnzahlPersonen() * 100 / kapazität.getMaxCarrels();
									beanListe.add(new ExportBelegungKomplettBean(getKWForDate(datum),
											getWochentagForDate(date), sdf.format(datum), uhrzeit, bereich,
											stockwerkName, "Carrels - Räume", carrels.getAnzahlRäume(),
											auslastung + "%"));
									beanListe.add(new ExportBelegungKomplettBean(getKWForDate(datum),
											getWochentagForDate(date), sdf.format(datum), uhrzeit, bereich,
											stockwerkName, "Carrels - Personen", carrels.getAnzahlPersonen(),
											auslastung + "%"));
								}
							}

						}
					}

				}

			}
		}

		tabelleBelegungKomplett.setItems(beanListe);
	}

	/**
	 * Sammelt alle Daten für den export der Belegung - Normal, zusammengezählt
	 */
	private void exportBelegungNormal() {
		List<ExportBelegungNormalBean> beanListe = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			if (!getWochentagForDate(date).equals("Sonntag")) {

				Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

				Belegung belegungBB = belegungDB.selectBelegungForDateAndStandort(datum, StandortEnum.WINTERTHUR_BB);
				Belegung belegungLL = belegungDB.selectBelegungForDateAndStandort(datum, StandortEnum.WINTERTHUR_LL);
				List<Belegung> belegungsListe = new ArrayList<>();
				Set<String> selectedBelegung = checkStockwerk.getSelectedItems();
				//Nur die ausgewählten Objekte der Checkbox für die Datensammlung benutzen
				for(String s : selectedBelegung) {
					if(s.equals("Bibliothek")) {
						belegungsListe.add(belegungBB);
					}else if(s.equals("Lernlandschaft")) {
						belegungsListe.add(belegungLL);
					}
				}
				
				List<UhrzeitEnum> enumListe = new ArrayList<>();
				Set<String> selectedUhrzeit = checkUhrzeit.getSelectedItems();
				//Nur die ausgewählten Objekte der Checkbox für die Datensammlung benutzen
				for(String s : selectedUhrzeit) {
					if(s.equals("9 Uhr"))
						enumListe.add(UhrzeitEnum.NEUN);
					if(s.equals("11 Uhr"))
						enumListe.add(UhrzeitEnum.ELF);
					if(s.equals("13 Uhr"))
						enumListe.add(UhrzeitEnum.DREIZEHN);
					if(s.equals("15 Uhr"))
						enumListe.add(UhrzeitEnum.FÜNFZEHN);
					if(s.equals("17 Uhr"))
						enumListe.add(UhrzeitEnum.SIEBZEHN);
					if(s.equals("19 Uhr"))
						enumListe.add(UhrzeitEnum.NEUNZEHN);
				}

				// Geht durch alle Uhrzeiten
				for (UhrzeitEnum uhrzeitEnum : enumListe) {

					String uhrzeit = null;

					switch (uhrzeitEnum) {
					case NEUN:
						uhrzeit = getUhrzeitStringByInt(9);
						break;
					case ELF:
						uhrzeit = getUhrzeitStringByInt(11);
						break;
					case DREIZEHN:
						uhrzeit = getUhrzeitStringByInt(13);
						break;
					case FÜNFZEHN:
						uhrzeit = getUhrzeitStringByInt(15);
						break;
					case SIEBZEHN:
						uhrzeit = getUhrzeitStringByInt(17);
						break;
					case NEUNZEHN:
						uhrzeit = getUhrzeitStringByInt(19);
						break;
					}

					// Geht durch Bibliothek und Lernlandschaft
					for (Belegung belegung : belegungsListe) {

						String bereich = null;
						if (belegung.getStandort() == StandortEnum.WINTERTHUR_LL) {
							bereich = "Lernlandschaft";
						} else if (belegung.getStandort() == StandortEnum.WINTERTHUR_BB) {
							bereich = "Bibliothek";
						}

						int maxAlleKapazitäten = 0;
						int personenZaehler = 0;

						// Geht durch alle 4 Stockwerke, EG, 1.ZG, 2.ZG, LL
						for (Stockwerk stockwerk : belegung.getStockwerkListe()) {

							Kapazität kapazität = stockwerk.getKapzität();
							int maxKapazität = kapazität.getMaxArbeitsplätze() + kapazität.getMaxSektorA()
									+ kapazität.getMaxSektorB() + kapazität.getMaxGruppenräume()
									+ kapazität.getMaxCarrels();
							maxAlleKapazitäten += maxKapazität;

							for (Arbeitsplätze arbeitsplätze : stockwerk.getArbeitsplatzListe()) {
								if (uhrzeitEnum == arbeitsplätze.getUhrzeit()) {
									personenZaehler += arbeitsplätze.getAnzahlPersonen();
								}
							}

							for (SektorA sektorA : stockwerk.getSektorAListe()) {
								if (uhrzeitEnum == sektorA.getUhrzeit()) {
									personenZaehler += sektorA.getAnzahlPersonen();
								}
							}

							for (SektorB sektorB : stockwerk.getSektorBListe()) {
								if (uhrzeitEnum == sektorB.getUhrzeit()) {
									personenZaehler += sektorB.getAnzahlPersonen();
								}
							}

							for (Gruppenräume gruppenräume : stockwerk.getGruppenräumeListe()) {
								if (uhrzeitEnum == gruppenräume.getUhrzeit()) {
									personenZaehler += gruppenräume.getAnzahlPersonen();
								}
							}

							for (Carrels carrels : stockwerk.getCarrelsListe()) {
								if (uhrzeitEnum == carrels.getUhrzeit()) {
									personenZaehler += carrels.getAnzahlPersonen();
								}
							}
						}

						int auslastung = personenZaehler * 100 / maxAlleKapazitäten;
						beanListe.add(new ExportBelegungNormalBean(getKWForDate(datum), getWochentagForDate(date),
								sdf.format(datum), uhrzeit, bereich, personenZaehler,
								auslastung + "%"));
					}
				}
			}
		}

		tabelleBelegungNormal.setItems(beanListe);
	}

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

				if (e.getSource() == bExportBenutzung) {
					exportBenutzungsstatistik();
				}

				if (e.getSource() == bExportWintikurierTag) {
					exportWintikurierTag();
				}

				if (e.getSource() == bExportWintikurierMonat) {
					exportWintikurierMonat();
				}

				if (e.getSource() == bExportGruppen) {
					exportExterneGruppe();
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
