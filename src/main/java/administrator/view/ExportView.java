package administrator.view;

import java.util.Arrays;
import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.view.MainView;

@SuppressWarnings("serial")
public class ExportView implements View {

	private AbsoluteLayout mainLayout;
	private Button bExportBenutzung;
	private Button bExportWintikurier;
	private Button bExportGruppen;
	private Button bExportBelegung;

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

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

	private void initData() {

	}

	// Initialisieren der GUI Komponente
	@SuppressWarnings("unchecked")
	private void initComponents(MainView mainView) {

		Label lText = new Label();
		lText.setValue("Daten exportieren - Winterthur");
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		DateField datumVon = new DateField();
		datumVon.setCaption("Datum Von");
		datumVon.setDateFormat("dd.MM.yyyy");
		datumVon.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);
		});

		DateField datumBis = new DateField();
		datumBis.setCaption("Datum Bis");
		datumBis.setDateFormat("dd.MM.yyyy");
		datumBis.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);
		});

		// Benutzung
		Label lBenutzung = new Label();
		lBenutzung.setValue("Benutzungsstatistik");
		lBenutzung.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		List<String> dataZeit = Arrays.asList("nach Stunde (detailliert)", "nach Tag", "nach KW", "nach Monat",
				"nach Jahr");
		RadioButtonGroup radioKontakt = new RadioButtonGroup<>("Kontakt", dataZeit);
		radioKontakt.addValueChangeListener(event -> {
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
		});

		List<String> dataKontakt = Arrays.asList("Benutzerkontakt", "Intensivfrage", "Telefonkontakt", "Emailkontakt");
		CheckBoxGroup checkKontakt = new CheckBoxGroup<>("Kontakt", dataKontakt);
		checkKontakt.addValueChangeListener(event -> {
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
		});

		bExportBenutzung = new Button();
		bExportBenutzung.setCaption("Benutzung exportieren");
		bExportBenutzung.addClickListener(createClickListener(mainView));

		List<String> dataWintikurier = Arrays.asList("detailliert nach Datum", "nach Monat", "nach Quartal",
				"nach Jahr");
		RadioButtonGroup radioWintikurier = new RadioButtonGroup<>("Wintikurier", dataWintikurier);
		radioWintikurier.addValueChangeListener(event -> {
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
		});

		bExportWintikurier = new Button();
		bExportWintikurier.setCaption("Wintikurier exportieren");
		bExportWintikurier.addClickListener(createClickListener(mainView));

		List<String> dataGruppen = Arrays.asList("detailliert nach Datum", "nach KW", "nach Monat", "nach Jahr");
		RadioButtonGroup radioGruppen = new RadioButtonGroup<>("Externe Gruppen", dataGruppen);
		radioGruppen.addValueChangeListener(event -> {
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
		});

		bExportGruppen = new Button();
		bExportGruppen.setCaption("Gruppen exportieren");
		bExportGruppen.addClickListener(createClickListener(mainView));

		// Belegung
		Label lBelegung = new Label();
		lBelegung.setValue("Belegung");
		lBelegung.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

//		List<String> dataZeit = Arrays.asList("nach Stunde (detailliert)", "nach Tag", "nach KW", "nach Monat", "nach Jahr");
		RadioButtonGroup radioBelegung = new RadioButtonGroup<>("Belegung", dataZeit);
		radioBelegung.addValueChangeListener(event -> {
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
		});

		List<String> dataUhrzeit = Arrays.asList("Alle Uhrzeiten", "9 Uhr", "11 Uhr", "13 Uhr", "15 Uhr", "17 Uhr",
				"19 Uhr");
		RadioButtonGroup radioUhrzeit = new RadioButtonGroup<>("Uhrzeit", dataUhrzeit);
		radioUhrzeit.setSelectedItem(dataUhrzeit.get(4));
		radioUhrzeit.addValueChangeListener(event -> {
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
		});

		List<String> dataStockwerk = Arrays.asList("EG", "1.ZG", "2.ZG", "LL", "Bibliothek");
		CheckBoxGroup checkStockwerk = new CheckBoxGroup<>("Stockwerk", dataStockwerk);
		checkStockwerk.addValueChangeListener(event -> {
			
			if(checkStockwerk.isSelected(dataStockwerk.get(4))){
				checkStockwerk.select(dataStockwerk.get(0), dataStockwerk.get(1), dataStockwerk.get(2));
			}

			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
		});

		bExportBelegung = new Button();
		bExportBelegung.setCaption("Belegung exportieren");
		bExportBelegung.addClickListener(createClickListener(mainView));

		// Layout
		VerticalLayout overallLayout = new VerticalLayout();
		overallLayout.addComponent(lText);

		// Datum
		HorizontalLayout datumLayout = new HorizontalLayout();
		datumLayout.addComponent(datumVon);
		datumLayout.addComponent(datumBis);
		overallLayout.addComponent(datumLayout);

		// Benutzung
		overallLayout.addComponent(lBenutzung);
		HorizontalLayout benutzungLayout = new HorizontalLayout();
		benutzungLayout.addComponent(radioKontakt);
		benutzungLayout.addComponent(checkKontakt);
		benutzungLayout.addComponent(radioWintikurier);
		benutzungLayout.addComponent(radioGruppen);
		overallLayout.addComponent(benutzungLayout);
		HorizontalLayout benutzungLayout2 = new HorizontalLayout();
		benutzungLayout2.addComponent(bExportBenutzung);
		benutzungLayout2.addComponent(bExportWintikurier);
		benutzungLayout2.addComponent(bExportGruppen);
		overallLayout.addComponent(benutzungLayout2);

		// Belegung
		overallLayout.addComponent(lBelegung);
		HorizontalLayout belegungLayout = new HorizontalLayout();
		belegungLayout.addComponent(radioBelegung);
		belegungLayout.addComponent(radioUhrzeit);
		belegungLayout.addComponent(checkStockwerk);
		overallLayout.addComponent(belegungLayout);
		overallLayout.addComponent(bExportBelegung);

		mainLayout.addComponent(overallLayout);
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
					// TODO Export
				}

				if (e.getSource() == bExportBelegung) {
					// TODO Export
				}

			}
		};

	}

}
