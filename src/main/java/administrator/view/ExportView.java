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
	private Button bExportAllBelegung;

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		//mainLayout.setHeight("100%");

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

		bExportBenutzung = new Button();
		bExportBenutzung.setCaption("Benutzung exportieren");
		bExportBenutzung.addClickListener(createClickListener(mainView));

		bExportWintikurier = new Button();
		bExportWintikurier.setCaption("Wintikurier exportieren");
		bExportWintikurier.setEnabled(false);
		bExportWintikurier.addClickListener(createClickListener(mainView));
		
		List<String> dataWintikurier = Arrays.asList("nach Tag", "nach Monat");
		RadioButtonGroup<String> radioWintikurier = new RadioButtonGroup<>("Wintikurier", dataWintikurier);
		radioWintikurier.addValueChangeListener(event -> {
			if(event.getValue().contains(dataWintikurier.get(0)) || event.getValue().contains(dataWintikurier.get(1))) {
				bExportWintikurier.setEnabled(true);
			}else {
				bExportWintikurier.setEnabled(false);
			}
		});

		bExportGruppen = new Button();
		bExportGruppen.setCaption("Gruppen exportieren");
		bExportGruppen.addClickListener(createClickListener(mainView));

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
			
			if(event.getValue().contains(dataStockwerk.get(0)) || event.getValue().contains(dataStockwerk.get(1))) {
				bExportBelegung.setEnabled(true);
			}else {
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
