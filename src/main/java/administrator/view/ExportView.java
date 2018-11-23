package administrator.view;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.vaadin.haijian.Exporter;

import com.vaadin.navigator.View;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import administrator.bean.ExportExterneGruppeBean;
import administrator.bean.ExportWintikurierBean;
import allgemein.model.StandortEnum;
import allgemein.view.MainView;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.ExterneGruppe;

@SuppressWarnings("serial")
public class ExportView implements View {

	private AbsoluteLayout mainLayout;
	private Button bExportBenutzung;
	private Button bExportWintikurier;
	private Button bExportGruppen;
	private Button bExportBelegung;
	private Button bExportAllBelegung;
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	private LocalDate startDate;
	private LocalDate endDate;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private Grid<ExportExterneGruppeBean> tabelleExterneGruppen;
	private Grid<ExportExterneGruppeBean> tabelleWintikurier;

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		// mainLayout.setHeight("100%");

		return mainLayout;
	}

	public AbsoluteLayout init(MainView mainView) {
		// common part: create layout
		AbsoluteLayout absolutLayout = buildMainLayout();
		initData();
		initComponents(mainView);

		return absolutLayout;
	}

	public ExportView() {

	}

	@SuppressWarnings("static-access")
	private void initData() {
		startDate = startDate.now();
		endDate = endDate.now();
	}

	// Initialisieren der GUI Komponente
	private void initComponents(MainView mainView) {

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

		bExportBenutzung = new Button();
		bExportBenutzung.setCaption("Benutzung exportieren");
		bExportBenutzung.addClickListener(createClickListener(mainView));

		//Download Excelfile für Wintikurier
		bExportWintikurier = new Button();
		bExportWintikurier.setCaption("Wintikurier exportieren");
		bExportWintikurier.setEnabled(false);
		bExportWintikurier.addClickListener(createClickListener(mainView));
//		tabelleWintikurier = new Grid<>(ExportWintikurierBean.class);
//		StreamResource excelStreamResource = new StreamResource(
//				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(tabelleWintikurier), "Wintikurier.xls");
//		FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
//		excelFileDownloader.extend(bExportWintikurier);
		
		List<String> dataWintikurier = Arrays.asList("nach Tag", "nach Monat");
		RadioButtonGroup<String> radioWintikurier = new RadioButtonGroup<>("Wintikurier", dataWintikurier);
		radioWintikurier.addValueChangeListener(event -> {
			if (event.getValue().contains(dataWintikurier.get(0))
					|| event.getValue().contains(dataWintikurier.get(1))) {
				bExportWintikurier.setEnabled(true);
			} else {
				bExportWintikurier.setEnabled(false);
			}
		});
		

		//Download Excelfile für Externe Gruppen
		bExportGruppen = new Button();
		bExportGruppen.setCaption("Gruppen exportieren");
		bExportGruppen.addClickListener(createClickListener(mainView));
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
		CheckBoxGroup<String> checkUhrzeit = new CheckBoxGroup<>("Uhrzeit", dataUhrzeit);
		checkUhrzeit.select(dataUhrzeit.get(3));
		checkUhrzeit.addValueChangeListener(event -> {
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
		});

		bExportBelegung = new Button();
		bExportBelegung.setCaption("Belegung exportieren");
		bExportBelegung.setEnabled(false);
		bExportBelegung.addClickListener(createClickListener(mainView));

		List<String> dataStockwerk = Arrays.asList("Lernlandschaft", "Bibliothek");
		CheckBoxGroup<String> checkStockwerk = new CheckBoxGroup<>("Stockwerk", dataStockwerk);
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
		bExportAllBelegung.addClickListener(createClickListener(mainView));

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
		benutzungsLayout.addComponent(radioWintikurier);
		benutzungsLayout.addComponent(bExportWintikurier);
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

	private void exportExterneGruppe() {
		List<ExportExterneGruppeBean> beanListe = new ArrayList<>();

		// Geht durch alle Tage vom Startdatum bis Enddatum
		for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {

			Date datum = Date.from((date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

			Benutzungsstatistik benutzungsstatistik = benutzungsstatistikDB
					.selectBenutzungsstatistikForDateAndStandort(datum, StandortEnum.WINTERTHUR_BB);

			for (ExterneGruppe eg : benutzungsstatistik.getExterneGruppeListe()) {
				beanListe.add(new ExportExterneGruppeBean(getKWForDate(datum), getWochentagForDate(date), sdf.format(datum),
						eg.getName(), eg.getAnzahl_Personen()));
			}
		}

		tabelleExterneGruppen.setItems(beanListe);
	}

	private String getWochentagForDate(LocalDate date) {
		
		switch(date.getDayOfWeek().toString()) {
		case "MONDAY": return "Montag";
		case "TUESDAY": return "Dienstag";
		case "WEDNESDAY": return "Mittwoch";
		case "THURSDAY": return "Donnerstag";
		case "FRIDAY": return "Freitag";
		case "SATURDAY": return "Samstag";
		case "SUNDAY": return "Sonntag";
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

	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {

				if (e.getSource() == bExportBenutzung) {
					// TODO Export
				}

				if (e.getSource() == bExportWintikurier) {
					// TODO Export
				}

				if (e.getSource() == bExportGruppen) {
					exportExterneGruppe();
				}

				if (e.getSource() == bExportBelegung) {
					// TODO Export
				}

			}
		};

	}

}
