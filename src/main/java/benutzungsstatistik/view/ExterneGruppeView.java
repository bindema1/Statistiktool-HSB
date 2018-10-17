package benutzungsstatistik.view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.view.MainView;
import benutzungsstatistik.bean.ExterneGruppeBean;
import benutzungsstatistik.db.ExterneGruppeDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.ExterneGruppe;

/**
 * View der ExterneGruppe. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
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
	private Grid<ExterneGruppeBean> tabelle;
	private List<ExterneGruppeBean> externeGruppeBeanListe;
	private Benutzungsstatistik benutzungsstatistik;
	private ExterneGruppeDatenbank externeGruppeDB = new ExterneGruppeDatenbank();

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

	public ExterneGruppeView(Benutzungsstatistik benutzungsstatistik) {
		this.benutzungsstatistik = benutzungsstatistik;
	}

	private void initData() {

	}

	// Initialisieren der GUI Komponente
	@SuppressWarnings("unchecked")
	private void initComponents(MainView mainView) {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener(mainView));

		Label lText = new Label();
		lText.setValue("Externe Gruppen erfassen vom ");
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		DateTimeField datefield = new DateTimeField();
		datefield.setValue(LocalDateTime.now());
		datefield.setDateFormat("dd.MM.yyyy");
		datefield.addValueChangeListener(
				event -> Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION));

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

		List<ExterneGruppe> externeGruppeListe = externeGruppeDB
				.selectAllExterneGruppenForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID());
		tabelle = new Grid<ExterneGruppeBean>();
		externeGruppeBeanListe = new ArrayList<>();

		for (ExterneGruppe eg : externeGruppeListe) {
			ExterneGruppeBean egb = new ExterneGruppeBean();
			egb.setName(eg.getName());
			egb.setAnzahl_personen(eg.getAnzahl_Personen());
			externeGruppeBeanListe.add(egb);
		}

		tabelle.setItems(externeGruppeBeanListe);
		tabelle.addColumn(ExterneGruppeBean::getName).setCaption("Name");
		tabelle.addColumn(ExterneGruppeBean::getAnzahl_personen).setCaption("Anzahl Personen");

		tabelle.addColumn(ExterneGruppeBean -> "Bearbeiten", new ButtonRenderer(clickEvent -> {
			externeGruppeBeanListe.remove(clickEvent.getItem());
			ExterneGruppeBean beanZuBearbeiten = (ExterneGruppeBean) clickEvent.getItem();

			List<ExterneGruppe> externeGruppeListe2 = externeGruppeDB
					.selectAllExterneGruppenForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID());
			for (ExterneGruppe e : externeGruppeListe2) {
				if (beanZuBearbeiten.getName().equals(e.getName())) {
					externeGruppeDB.deleteExterneGruppe(e);
				}
			}
			
			tName.setValue(beanZuBearbeiten.getName());
			tPersonenzahl.setValue(""+beanZuBearbeiten.getAnzahl_personen());

			tabelle.setItems(externeGruppeBeanListe);
		})).setCaption("Bearbeitung");
		
		tabelle.addColumn(ExterneGruppeBean -> "Löschen", new ButtonRenderer(clickEvent -> {
			
//			ConfirmDialog.show(this, "Bitte bestätigen:", "Sind Sie sicher sie wollen die Gruppe löschen?",
//			        "Löschen", "Abbrechen", new ConfirmDialog.Listener() {
//
//			            public void onClose(ConfirmDialog dialog) {
//			                if (dialog.isConfirmed()) {
//			                    // Confirmed to continue
			                	externeGruppeBeanListe.remove(clickEvent.getItem());

			        			ExterneGruppeBean beanZuLöschen = (ExterneGruppeBean) clickEvent.getItem();

			        			List<ExterneGruppe> externeGruppeListe2 = externeGruppeDB
			        					.selectAllExterneGruppenForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID());
			        			for (ExterneGruppe e : externeGruppeListe2) {
			        				if (beanZuLöschen.getName().equals(e.getName())) {
			        					externeGruppeDB.deleteExterneGruppe(e);
			        				}
			        			}

			        			tabelle.setItems(externeGruppeBeanListe);
//			                } else {
//			                    // User did not confirm
//			                	// do nothing
//			                }
//			            }
//			        });
			
//			ConfirmDialog dialog = new ConfirmDialog("Löschen bestätigen",
//			        "Wollen Sie die Gruppe wirklich löschen?", "Löschen", this::onPublish,
//			        "Abbrechen", this::onCancel);
			
			
		})).setCaption("Löschfunktion");

		GridLayout grid = new GridLayout(5, 7);
		grid.setSizeFull();
		grid.setSpacing(true);
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0, 2, 0);
		grid.addComponent(datefield, 3, 0, 4, 0);
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
					if (col == 1 || col == 2 || col == 3) {
						grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
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

	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					mainView.setContent(new BenutzungsstatistikView().init(mainView));
				}

				if (e.getSource() == bSpeichern) {

					try {
						String name = tName.getValue();
						int personenzahl = Integer.parseInt(tPersonenzahl.getValue());

						externeGruppeDB.insertExterneGruppe(new ExterneGruppe(name, personenzahl, benutzungsstatistik));

						ExterneGruppeBean egb = new ExterneGruppeBean();
						egb.setName(name);
						egb.setAnzahl_personen(personenzahl);
						externeGruppeBeanListe.add(egb);
						tabelle.setItems(externeGruppeBeanListe);
						
						//Textfelder zurücksetzen
						tName.setValue("");
						tPersonenzahl.setValue("");
					} catch (NumberFormatException e1) {
						// Not an integer
						if(tPersonenzahl.getValue().equals("") || tName.getValue().equals("")) {
							Notification.show("Bitte alle Felder ausfüllen", Type.WARNING_MESSAGE);
						}else {
							Notification.show("Personenzahl muss eine Zahl sein", Type.WARNING_MESSAGE);
						}
					}

				}

			}
		};

	}


}
