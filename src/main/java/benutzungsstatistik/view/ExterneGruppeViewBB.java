package benutzungsstatistik.view;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
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
public class ExterneGruppeViewBB extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-ExterneGruppe";
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

	/**
	 * Bildet das AbsoluteLayout, als Wrapper um die ganze View
	 * 
	 * @return AbsoluteLayout
	 */
	private AbsoluteLayout buildMainLayout() {
		mainLayout = new AbsoluteLayout();
		// Setzt die Hintergrundfarbe
		if (korrektur == true) {
			mainLayout.addStyleName("backgroundKorrektur");
		} else {
			mainLayout.addStyleName("backgroundErfassung");
		}
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
		initComponents();

		return absolutLayout;
	}

	public ExterneGruppeViewBB() {

	}

	/**
	 * Holt die akutelle Benutzungsstatistik und setzt die Uhrzeit
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initComponents() {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener());

		Label lText = new Label();
		if (korrektur == true) {
			lText.setValue("Externe Gruppen erfassen vom ");
		} else {
			lText.setValue("Externe Gruppen erfassen vom "
					+ new SimpleDateFormat("dd.MM.yyyy").format(benutzungsstatistik.getDatum()));
		}
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		DateField datefield = new DateField();
		datefield.setValue(Instant.ofEpochMilli(benutzungsstatistik.getDatum().getTime()).atZone(ZoneId.systemDefault())
				.toLocalDate());
		datefield.setDateFormat("dd.MM.yyyy");
		if (!VaadinSession.getCurrent().getAttribute("user").toString().contains("Admin")) {
			// Nicht-Administratoren dürfen nur eine Woche in der Zeit zurück
			datefield.setRangeStart(LocalDate.now().minusDays(7));
			datefield.setRangeEnd(LocalDate.now());
		}
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			ZonedDateTime zdt = event.getValue().atStartOfDay().atZone(ZoneId.systemDefault());
			Date date = Date.from(zdt.toInstant());
			// Holt die Statistk für ein Datum
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
				"<html><body>- Erfassung von nicht-HSB-geführten Gruppen <br>- Name von angemeldeten Gruppen s. ggf. im Outlook Kalender <br>- Gruppe unbekannt -> N.N.</body></html>");

		bSpeichern = new Button();
		bSpeichern.setCaption("Speichern");
		bSpeichern.addClickListener(createClickListener());

		tabelle = new Grid<ExterneGruppeBean>();
		tabelle.addColumn(ExterneGruppeBean::getName).setCaption("Name");
		tabelle.addColumn(ExterneGruppeBean::getAnzahl_personen).setCaption("Anzahl Personen");
		tabelle.addColumn(ExterneGruppeBean::getErfasstUm).setCaption("Erfasst um");
		// Spalte zum Bearbeiten
		tabelle.addColumn(ExterneGruppeBean -> "Bearbeiten", new ButtonRenderer(clickEvent -> {
			ExterneGruppeBean beanZuBearbeiten = (ExterneGruppeBean) clickEvent.getItem();
			externeGruppeBeanListe.remove(beanZuBearbeiten);

			ExterneGruppe externeGruppeZuLöschen = null;
			// Löscht den Eintrag in der Tabelle und fügt es in den Textfeldern ein
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
		// Spalte zum Löschen
		tabelle.addColumn(ExterneGruppeBean -> "Löschen", new ButtonRenderer(clickEvent -> {

			ConfirmDialog.show(getUI(), "Bitte bestätigen:", "Sind Sie sicher sie wollen die Gruppe löschen?",
					"Löschen", "Abbrechen", new ConfirmDialog.Listener() {

						private static final long serialVersionUID = 1L;

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								// User klickt "Löschen"
								externeGruppeBeanListe.remove(clickEvent.getItem());

								ExterneGruppeBean beanZuLöschen = (ExterneGruppeBean) clickEvent.getItem();

								ExterneGruppe externeGruppeZuLöschen = null;
								for (ExterneGruppe e : benutzungsstatistik.getExterneGruppeListe()) {
									if (beanZuLöschen.getName().equals(e.getName())) {
										externeGruppeZuLöschen = e;
									}
								}
								// Lösche den Eintrag
								benutzungsstatistik.removeExterneGruppe(externeGruppeZuLöschen);
								benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

								tabelle.setItems(externeGruppeBeanListe);
							} else {
								// User klickt "Abbrechen"
								// mache nichts
							}
						}
					});

		})).setCaption("Löschfunktion");
		fülleGruppenTabelle();

		GridLayout grid = new GridLayout(5, 7);
		// Setzt die Hintergrundfarbe
		if (korrektur == true) {
			grid.addStyleName("backgroundKorrektur");
		} else {
			grid.addStyleName("backgroundErfassung");
		}
		grid.setSizeFull();
		grid.setSpacing(true);
		grid.addComponent(bZurueck, 0, 0);
		if (korrektur == true) {
			grid.addComponent(lText, 1, 0, 2, 0);
			grid.addComponent(datefield, 3, 0, 4, 0);
		} else {
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
					if (korrektur == true) {
						if (col == 1 || col == 2 || col == 3) {
							grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
						}
					}
				} else if (row >= 3) {
					c.setHeight("88%");
					c.setWidth("88%");
				} else {
					c.setHeight("80%");
					c.setWidth("80%");
				}

			}
		}

		mainLayout.addComponent(grid);

	}

	/**
	 * Füllt die Tabelle
	 */
	private void fülleGruppenTabelle() {

		externeGruppeBeanListe = new ArrayList<>();

		for (ExterneGruppe eg : benutzungsstatistik.getExterneGruppeListe()) {
			ExterneGruppeBean egb = new ExterneGruppeBean();
			egb.setName(eg.getName());
			egb.setAnzahl_personen(eg.getAnzahl_Personen());
			egb.setErfasstUm(eg.getErfasstUm());
			externeGruppeBeanListe.add(egb);
		}

		tabelle.setItems(externeGruppeBeanListe);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String args[] = event.getParameters().split("/");
		// ID der Benutzungsstatistik
		String id = args[0];
		// Korrektur auf false oder true
		String korrekturString = args[1];
		this.benutzungsstatistik = benutzungsstatistikDB.findBenutzungsstatistikById(Integer.parseInt(id));
		this.korrektur = Boolean.parseBoolean(korrekturString);

		setCompositionRoot(init());
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					if (korrektur == true) {
						getUI().getNavigator().navigateTo(
								KorrekturViewBB.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
					} else {
						Page.getCurrent().setUriFragment("!" + BenutzungsstatistikViewBB.NAME);
					}
				}

				if (e.getSource() == bSpeichern) {

					// Speichert den Eintrag aus den Textfeldern
					try {
						String name = tName.getValue();
						int personenzahl = Integer.parseInt(tPersonenzahl.getValue());

						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
						String erfasstUm = sdf.format(cal.getTime());

						benutzungsstatistik.addExterneGruppe(
								new ExterneGruppe(name, personenzahl, erfasstUm, benutzungsstatistik));
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

						ExterneGruppeBean egb = new ExterneGruppeBean();
						egb.setName(name);
						egb.setAnzahl_personen(personenzahl);
						egb.setErfasstUm(erfasstUm);
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
