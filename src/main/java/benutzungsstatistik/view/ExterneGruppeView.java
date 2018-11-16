package benutzungsstatistik.view;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import allgemein.view.MainView;
import benutzungsstatistik.bean.ExterneGruppeBean;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.ExterneGruppe;

/**
 * View der ExterneGruppe. Zeigt alle Button, Label, Felder etc. in einem Layout
 * an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class ExterneGruppeView {

	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bSpeichern;
	private TextField tName;
	private TextField tPersonenzahl;
	private boolean korrektur;
	private Grid<ExterneGruppeBean> tabelle;
	private List<ExterneGruppeBean> externeGruppeBeanListe;
	private Benutzungsstatistik benutzungsstatistik;
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
//	private ExterneGruppeDatenbank externeGruppeDB = new ExterneGruppeDatenbank();

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		return mainLayout;
	}

	public AbsoluteLayout init(MainView mainView) {

		AbsoluteLayout absolutLayout = buildMainLayout();
		initData();
		initComponents(mainView);

		return absolutLayout;
	}

	public ExterneGruppeView(Benutzungsstatistik benutzungsstatistik, boolean korrektur) {
		this.benutzungsstatistik = benutzungsstatistik;
		this.korrektur = korrektur;
	}

	private void initData() {

	}

	// Initialisieren der GUI Komponente
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initComponents(MainView mainView) {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener(mainView));

		Label lText = new Label();
		if(korrektur == true) {
			lText.setValue("Externe Gruppen erfassen vom ");
		}else {
			lText.setValue("Externe Gruppen erfassen vom " + new SimpleDateFormat("dd.MM.yyyy").format(benutzungsstatistik.getDatum()));
		}
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		DateField datefield = new DateField();
		datefield.setValue(Instant.ofEpochMilli(benutzungsstatistik.getDatum().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
		datefield.setDateFormat("dd.MM.yyyy");
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			ZonedDateTime zdt = event.getValue().atStartOfDay().atZone(ZoneId.systemDefault());
			Date date = Date.from(zdt.toInstant());
			benutzungsstatistik = new BenutzungsstatistikDatenbank().selectBenutzungsstatistikForDateAndStandort(date,
					StandortEnum.WINTERTHUR_BB);

			// Alle Werte anpassen
			fülleGruppenTabelle();
		});

		tName = new TextField("Name der Gruppe");
		tName.addStyleName(ValoTheme.TEXTFIELD_HUGE);

		tPersonenzahl = new TextField("Anzahl Personen");
		tPersonenzahl.addStyleName(ValoTheme.TEXTFIELD_HUGE);

		Label lBeschreibung = new Label();
		lBeschreibung.setContentMode(ContentMode.HTML);
		lBeschreibung.setValue(
				"<html><body>- Erfassung von nicht-HSB-geführten Gruppen <br>- Name von angemeldeten Gruppe s. ggf. im Outlook Kalender <br>- Gruppe unbekannt -> N.N.</body></html>");

		bSpeichern = new Button();
		bSpeichern.setCaption("Speichern");
		bSpeichern.addClickListener(createClickListener(mainView));

		tabelle = new Grid<ExterneGruppeBean>();
		tabelle.addColumn(ExterneGruppeBean::getName).setCaption("Name");
		tabelle.addColumn(ExterneGruppeBean::getAnzahl_personen).setCaption("Anzahl Personen");
		tabelle.addColumn(ExterneGruppeBean -> "Bearbeiten", new ButtonRenderer(clickEvent -> {
			ExterneGruppeBean beanZuBearbeiten = (ExterneGruppeBean) clickEvent.getItem();
			externeGruppeBeanListe.remove(beanZuBearbeiten);

			ExterneGruppe externeGruppeZuLöschen = null;
			for (ExterneGruppe e : benutzungsstatistik.getExterneGruppeListe()) {
				if (beanZuBearbeiten.getName().equals(e.getName())) {
					externeGruppeZuLöschen = e;
				}
			}
			benutzungsstatistik.removeExterneGruppe(externeGruppeZuLöschen);
			benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

			tName.setValue(beanZuBearbeiten.getName());
			tPersonenzahl.setValue("" + beanZuBearbeiten.getAnzahl_personen());

			tabelle.setItems(externeGruppeBeanListe);
		})).setCaption("Bearbeitung");

		tabelle.addColumn(ExterneGruppeBean -> "Löschen", new ButtonRenderer(clickEvent -> {

//			ConfirmDialog.show(this, "Bitte bestätigen:", "Sind Sie sicher sie wollen die Gruppe löschen?",
//			        "Löschen", "Abbrechen", new ConfirmDialog.Listener() {
//
//			            public void onClose(ConfirmDialog dialog) {
//			                if (dialog.isConfirmed()) {
//			                    // Confirmed to continue
//            } else {
//            // User did not confirm
//        	// do nothing
//        }
//    }
//});
//			ConfirmDialog dialog = new ConfirmDialog("Meeting starting",
//	        "Your next meeting starts in 5 minutes", "OK", this::onOK);

			externeGruppeBeanListe.remove(clickEvent.getItem());

			ExterneGruppeBean beanZuLöschen = (ExterneGruppeBean) clickEvent.getItem();

			ExterneGruppe externeGruppeZuLöschen = null;
			for (ExterneGruppe e : benutzungsstatistik.getExterneGruppeListe()) {
				if (beanZuLöschen.getName().equals(e.getName())) {
					externeGruppeZuLöschen = e;
				}
			}
			benutzungsstatistik.removeExterneGruppe(externeGruppeZuLöschen);
			benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

			tabelle.setItems(externeGruppeBeanListe);

		})).setCaption("Löschfunktion");
		fülleGruppenTabelle();

		GridLayout grid = new GridLayout(5, 7);
		grid.setSizeFull();
		grid.setSpacing(true);
		grid.addComponent(bZurueck, 0, 0);
		if(korrektur == true) {
			grid.addComponent(lText, 1, 0, 2, 0);
			grid.addComponent(datefield, 3, 0, 4, 0);
		}else {
			grid.addComponent(lText, 1, 0, 4, 0);
		}
		grid.addComponent(tName, 0, 1, 2, 1);
		grid.addComponent(tPersonenzahl, 3, 1, 4, 1);
		grid.addComponent(lBeschreibung, 0, 2, 2, 2);
		grid.addComponent(bSpeichern, 3, 2, 4, 2);
		grid.addComponent(tabelle, 0, 3, 4, 6);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {

				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row == 0) {
					if(korrektur == true) {
						if (col == 1 || col == 2 || col == 3) {
							grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
						}
					}
				} else if (row >= 3) {
					c.setHeight("90%");
					c.setWidth("90%");
				} else {
					c.setHeight("80%");
					c.setWidth("80%");
				}

			}
		}

		mainLayout.addComponent(grid);

	}

	private void fülleGruppenTabelle() {

		externeGruppeBeanListe = new ArrayList<>();

		for (ExterneGruppe eg : benutzungsstatistik.getExterneGruppeListe()) {
			ExterneGruppeBean egb = new ExterneGruppeBean();
			egb.setName(eg.getName());
			egb.setAnzahl_personen(eg.getAnzahl_Personen());
			externeGruppeBeanListe.add(egb);
		}

		tabelle.setItems(externeGruppeBeanListe);
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					if(korrektur == true) {
						mainView.setContent(new KorrekturView(benutzungsstatistik).init(mainView));
					}else {
						mainView.setContent(new BenutzungsstatistikBBView().init(mainView));
					}
				}

				if (e.getSource() == bSpeichern) {

					try {
						String name = tName.getValue();
						int personenzahl = Integer.parseInt(tPersonenzahl.getValue());

						benutzungsstatistik.addExterneGruppe(new ExterneGruppe(name, personenzahl, benutzungsstatistik));
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

						ExterneGruppeBean egb = new ExterneGruppeBean();
						egb.setName(name);
						egb.setAnzahl_personen(personenzahl);
						externeGruppeBeanListe.add(egb);
						tabelle.setItems(externeGruppeBeanListe);

						// Textfelder zurücksetzen
						tName.setValue("");
						tPersonenzahl.setValue("");
					} catch (NumberFormatException e1) {
						// Not an integer
						if (tPersonenzahl.getValue().equals("") || tName.getValue().equals("")) {
							Notification.show("Bitte alle Felder ausfüllen", Type.WARNING_MESSAGE);
						} else {
							Notification.show("Personenzahl muss eine Zahl sein", Type.WARNING_MESSAGE);
						}
					}

				}

			}
		};

	}

}
