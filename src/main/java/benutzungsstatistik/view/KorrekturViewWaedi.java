package benutzungsstatistik.view;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Telefonkontakt;

/**
 * View der Korrektur. Zeigt alle Button, Label, Felder etc. in einem Layout an.
 * 
 * @author Marvin Bindemann
 */
@SuppressWarnings("serial")
@Theme("mytheme")
public class KorrekturViewWaedi extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-Korrektur-Waedi";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bBenutzerkontakt;
	private Button bIntensivFrage;
	private Button bEmailkontakt;
	private Button bTelefonkontakt;
	private Button bBenutzerkontaktMinus;
	private Button bIntensivFrageMinus;
	private Button bEmailkontaktMinus;
	private Button bTelefonkontaktMinus;
//	private Button bRechercheBeratung;
//	private Button bRechercheBeratungMinus;
	private Button bKorrekturInternerkurier;
	private Label lBenutzerkontakt;
//	private Label lRechercheberatung;
	private Label lIntensivFrage;
	private Label lEmailkontakt;
	private Label lTelefonkontakt;
	private int ausgewählteUhrzeit;
	private int benutzerzaehler = 0;
	private int emailzaehler = 0;
	private int intensivzaehler = 0;
	private int telefonzaehler = 0;
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	private Benutzungsstatistik benutzungsstatistik;
	private SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd-yyyy");
	private SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat sdfTimestamp = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH");

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		// Setzt die Farbe des Layouts
		mainLayout.addStyleName("backgroundKorrektur");
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		return mainLayout;
	}

	public AbsoluteLayout init() {

		AbsoluteLayout absolutLayout = buildMainLayout();
		initData();
		initComponents();

		return absolutLayout;
	}

	public KorrekturViewWaedi() {
	}

	private void initData() {

	}

	// Initialisieren der GUI Komponente
	private void initComponents() {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener());
		mainLayout.addComponent(bZurueck);

		Label lText = new Label();
		lText.setValue("Benutzungsstatistik Korrektur vom ");
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bBenutzerkontakt = new Button();
		bBenutzerkontakt.setCaption("Kontakt");
		bBenutzerkontakt.setEnabled(false);
		bBenutzerkontakt.setIcon(VaadinIcons.QUESTION_CIRCLE_O);
		bBenutzerkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBenutzerkontakt.addClickListener(createClickListener());

		lBenutzerkontakt = new Label();
		lBenutzerkontakt.setValue("0");
		lBenutzerkontakt.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bBenutzerkontaktMinus = new Button();
		bBenutzerkontaktMinus.setEnabled(false);
		bBenutzerkontaktMinus.setCaption("Korrektur -1");
		bBenutzerkontaktMinus.addStyleName(ValoTheme.BUTTON_DANGER);
		bBenutzerkontaktMinus.addClickListener(createClickListener());

		bIntensivFrage = new Button();
		bIntensivFrage.setCaption("Intensivfrage");
		bIntensivFrage.setEnabled(false);
		bIntensivFrage.setIcon(VaadinIcons.HOURGLASS);
		bIntensivFrage.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bIntensivFrage.addClickListener(createClickListener());

		lIntensivFrage = new Label();
		lIntensivFrage.setValue("0");
		lIntensivFrage.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bIntensivFrageMinus = new Button();
		bIntensivFrageMinus.setCaption("Korrektur -1");
		bIntensivFrageMinus.setEnabled(false);
		bIntensivFrageMinus.addStyleName(ValoTheme.BUTTON_DANGER);
		bIntensivFrageMinus.addClickListener(createClickListener());

		bEmailkontakt = new Button();
		bEmailkontakt.setCaption("Email");
		bEmailkontakt.setEnabled(false);
		bEmailkontakt.setIcon(VaadinIcons.ENVELOPE_OPEN_O);
		bEmailkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bEmailkontakt.addClickListener(createClickListener());

		lEmailkontakt = new Label();
		lEmailkontakt.setValue("0");
		lEmailkontakt.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bEmailkontaktMinus = new Button();
		bEmailkontaktMinus.setCaption("Korrektur -1");
		bEmailkontaktMinus.setEnabled(false);
		bEmailkontaktMinus.addStyleName(ValoTheme.BUTTON_DANGER);
		bEmailkontaktMinus.addClickListener(createClickListener());

		bTelefonkontakt = new Button();
		bTelefonkontakt.setCaption("Telefon");
		bTelefonkontakt.setEnabled(false);
		bTelefonkontakt.setIcon(VaadinIcons.PHONE);
		bTelefonkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bTelefonkontakt.addClickListener(createClickListener());

		lTelefonkontakt = new Label();
		lTelefonkontakt.setValue("0");
		lTelefonkontakt.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bTelefonkontaktMinus = new Button();
		bTelefonkontaktMinus.setCaption("Korrektur -1");
		bTelefonkontaktMinus.setEnabled(false);
		bTelefonkontaktMinus.addStyleName(ValoTheme.BUTTON_DANGER);
		bTelefonkontaktMinus.addClickListener(createClickListener());

		List<String> data = Arrays.asList(new String[] { "Bitte wählen ↓", "08-09", "09-10", "10-11", "11-12", "12-13",
				"13-14", "14-15", "15-16", "16-17", "17-18" });
		NativeSelect<String> uhrzeitListSelect = new NativeSelect<>("Uhrzeit:", data);
		uhrzeitListSelect.setSelectedItem(data.get(0));
		uhrzeitListSelect.setEmptySelectionAllowed(false);
		uhrzeitListSelect.setWidth(100.0f, Unit.PERCENTAGE);
		uhrzeitListSelect.addValueChangeListener(event -> {
			if (!String.valueOf(event.getValue()).equals("Bitte wählen ↓")) {
				bBenutzerkontakt.setEnabled(true);
				bBenutzerkontaktMinus.setEnabled(true);
				bEmailkontakt.setEnabled(true);
				bEmailkontaktMinus.setEnabled(true);
				bIntensivFrage.setEnabled(true);
				bIntensivFrageMinus.setEnabled(true);
				bTelefonkontakt.setEnabled(true);
				bTelefonkontaktMinus.setEnabled(true);

				ausgewählteUhrzeit = 0;

				switch (String.valueOf(event.getValue())) {
				case "08-09":
					ausgewählteUhrzeit = 8;
					break;
				case "09-10":
					ausgewählteUhrzeit = 9;
					break;
				case "10-11":
					ausgewählteUhrzeit = 10;
					break;
				case "11-12":
					ausgewählteUhrzeit = 11;
					break;
				case "12-13":
					ausgewählteUhrzeit = 12;
					break;
				case "13-14":
					ausgewählteUhrzeit = 13;
					break;
				case "14-15":
					ausgewählteUhrzeit = 14;
					break;
				case "15-16":
					ausgewählteUhrzeit = 15;
					break;
				case "16-17":
					ausgewählteUhrzeit = 16;
					break;
				case "17-18":
					ausgewählteUhrzeit = 17;
					break;
				}

				benutzerzaehler = 0;
				emailzaehler = 0;
				intensivzaehler = 0;
				telefonzaehler = 0;
				for (Benutzerkontakt b : benutzungsstatistik.getBenutzerkontaktListe()) {
					if (Integer.parseInt(dateFormat.format(b.getTimestamp().getTime())) == ausgewählteUhrzeit) {
						benutzerzaehler++;
					}
				}
				lBenutzerkontakt.setValue("" + benutzerzaehler);
				for (Emailkontakt e : benutzungsstatistik.getEmailkontaktListe()) {
					if (Integer.parseInt(dateFormat.format(e.getTimestamp().getTime())) == ausgewählteUhrzeit) {
						emailzaehler++;
					}
				}
				lEmailkontakt.setValue("" + emailzaehler);
				for (Intensivfrage i : benutzungsstatistik.getIntensivfrageListe()) {
					if (Integer.parseInt(dateFormat.format(i.getTimestamp().getTime())) == ausgewählteUhrzeit) {
						intensivzaehler++;
					}
				}
				lIntensivFrage.setValue("" + intensivzaehler);
				for (Telefonkontakt t : benutzungsstatistik.getTelefonkontaktListe()) {
					if (Integer.parseInt(dateFormat.format(t.getTimestamp().getTime())) == ausgewählteUhrzeit) {
						telefonzaehler++;
					}
				}
				lTelefonkontakt.setValue("" + telefonzaehler);

			} else {
				bBenutzerkontakt.setEnabled(false);
				bBenutzerkontaktMinus.setEnabled(false);
				bEmailkontakt.setEnabled(false);
				bEmailkontaktMinus.setEnabled(false);
				bIntensivFrage.setEnabled(false);
				bIntensivFrageMinus.setEnabled(false);
				bTelefonkontakt.setEnabled(false);
				bTelefonkontaktMinus.setEnabled(false);
			}

		});

//		lRechercheberatung = new Label();
//		lRechercheberatung.setValue("Rechercheberatung: " + benutzungsstatistik.getAnzahl_Rechercheberatung());
//		lRechercheberatung.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);
//
//		bRechercheBeratung = new Button();
//		bRechercheBeratung.setCaption("Rechercheb. +1");
//		bRechercheBeratung.addClickListener(createClickListener());
//
//		bRechercheBeratungMinus = new Button();
//		bRechercheBeratungMinus.setCaption("Rechercheb. -1");
//		bRechercheBeratungMinus.addStyleName(ValoTheme.BUTTON_DANGER);
//		bRechercheBeratungMinus.addClickListener(createClickListener());

		bKorrekturInternerkurier = new Button();
		bKorrekturInternerkurier.setCaption("Interner Kurier");
		bKorrekturInternerkurier.addClickListener(createClickListener());

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

			benutzungsstatistik = benutzungsstatistikDB.selectBenutzungsstatistikForDateAndStandort(date,
					StandortEnum.WÄDENSWIL);

			// Alle Werte anpassen
			uhrzeitListSelect.setSelectedItem(data.get(0));
			bBenutzerkontakt.setEnabled(false);
			bBenutzerkontaktMinus.setEnabled(false);
			bEmailkontakt.setEnabled(false);
			bEmailkontaktMinus.setEnabled(false);
			bTelefonkontakt.setEnabled(false);
			bTelefonkontaktMinus.setEnabled(false);
			bIntensivFrage.setEnabled(false);
			bIntensivFrageMinus.setEnabled(false);
			lBenutzerkontakt.setValue("0");
			lEmailkontakt.setValue("0");
			lTelefonkontakt.setValue("0");
			lIntensivFrage.setValue("0");
//			lRechercheberatung.setValue("Rechercheberatung: " + benutzungsstatistik.getAnzahl_Rechercheberatung());
		});

		GridLayout grid = new GridLayout(5, 7);
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0, 2, 0);
		grid.addComponent(datefield, 3, 0, 4, 0);
		grid.addComponent(uhrzeitListSelect, 0, 1, 0, 2);
		grid.addComponent(new Label(), 0, 3);
		grid.addComponent(bKorrekturInternerkurier, 0, 4, 0, 5);
		grid.addComponent(bBenutzerkontakt, 1, 1, 1, 3);
		grid.addComponent(lBenutzerkontakt, 1, 4);
		grid.addComponent(bBenutzerkontaktMinus, 1, 5);
		grid.addComponent(bIntensivFrage, 2, 1, 2, 3);
		grid.addComponent(lIntensivFrage, 2, 4);
		grid.addComponent(bIntensivFrageMinus, 2, 5);
		grid.addComponent(bEmailkontakt, 3, 1, 3, 3);
		grid.addComponent(lEmailkontakt, 3, 4);
		grid.addComponent(bEmailkontaktMinus, 3, 5);
		grid.addComponent(bTelefonkontakt, 4, 1, 4, 3);
		grid.addComponent(lTelefonkontakt, 4, 4);
		grid.addComponent(bTelefonkontaktMinus, 4, 5);
		grid.addComponent(new Label(), 0, 6, 4, 6);
//		grid.addComponent(lRechercheberatung, 1, 6, 2, 6);
//		grid.addComponent(bRechercheBeratung, 1, 7);
//		grid.addComponent(bRechercheBeratungMinus, 2, 7);
		grid.setColumnExpandRatio(0, 0.2f);
		grid.setColumnExpandRatio(1, 0.2f);
		grid.setColumnExpandRatio(2, 0.2f);
		grid.setColumnExpandRatio(3, 0.2f);
		grid.setColumnExpandRatio(4, 0.2f);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row == 0) {
					if (col == 1 || col == 2) {
						grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
					}
				} else if (row == 4 && col != 0) {
					// Zeile 4 sind label, dort darf die Grösse nicht verändert werden, da das
					// Alignment sonst nicht mehr funktioniert
				} else if (row >= 1 && row <= 5) {
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

	@Override
	public void enter(ViewChangeEvent event) {
		String args[] = event.getParameters().split("/");
		String id = args[0];
		this.benutzungsstatistik = benutzungsstatistikDB.findBenutzungsstatistikById(Integer.parseInt(id));

		setCompositionRoot(init());
	}

	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
				}

				if (e.getSource() == bBenutzerkontakt) {

					Date date = null;
					try {
						String datumStatistik = sdfDate.format(benutzungsstatistik.getDatum());
						String gewählteUhrzeit = sdfHour.format(
								sdfTimestamp.parse(sdfDate.format(new Date()) + " " + ausgewählteUhrzeit + ":00:00"));
						String timestamp = datumStatistik + " " + gewählteUhrzeit;
						date = sdfTimestamp.parse(timestamp);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					benutzungsstatistik.addBenutzerkontakt(
							new Benutzerkontakt(new Timestamp(date.getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					benutzerzaehler++;
					lBenutzerkontakt.setValue("" + benutzerzaehler);
					Notification.show("+1 Benutzerkontakt" + ausgewählteUhrzeit + " Uhr", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bBenutzerkontaktMinus) {

					if (benutzerzaehler != 0) {
						Benutzerkontakt benutzerkontakt = null;
						for (Benutzerkontakt b : benutzungsstatistik.getBenutzerkontaktListe()) {
							if (Integer.parseInt(dateFormat.format(b.getTimestamp().getTime())) == ausgewählteUhrzeit) {
								benutzerkontakt = b;
							}
						}
						benutzungsstatistik.removeBenutzerkontakt(benutzerkontakt);
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

						benutzerzaehler--;
						lBenutzerkontakt.setValue("" + benutzerzaehler);
						Notification.show("-1 Benutzerkontakt " + ausgewählteUhrzeit + " Uhr", Type.TRAY_NOTIFICATION);
					} else {
						Notification.show("Der Benutzerkontakt ist bereits 0", Type.WARNING_MESSAGE);
					}
				}

				if (e.getSource() == bIntensivFrage) {

					Date date = null;
					try {
						String datumStatistik = sdfDate.format(benutzungsstatistik.getDatum());
						String gewählteUhrzeit = sdfHour.format(
								sdfTimestamp.parse(sdfDate.format(new Date()) + " " + ausgewählteUhrzeit + ":00:00"));
						String timestamp = datumStatistik + " " + gewählteUhrzeit;
						date = sdfTimestamp.parse(timestamp);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					benutzungsstatistik
							.addIntensivfrage(new Intensivfrage(new Timestamp(date.getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					intensivzaehler++;
					lIntensivFrage.setValue("" + intensivzaehler);
					Notification.show("+1 Intensivfrage" + ausgewählteUhrzeit + " Uhr", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bIntensivFrageMinus) {

					if (intensivzaehler != 0) {
						Intensivfrage intensivfrage = null;
						for (Intensivfrage i : benutzungsstatistik.getIntensivfrageListe()) {
							if (Integer.parseInt(dateFormat.format(i.getTimestamp().getTime())) == ausgewählteUhrzeit) {
								intensivfrage = i;
							}
						}
						benutzungsstatistik.removeIntensivfrage(intensivfrage);
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

						intensivzaehler--;
						lIntensivFrage.setValue("" + intensivzaehler);
						Notification.show("-1 Intensivfrage " + ausgewählteUhrzeit + " Uhr", Type.TRAY_NOTIFICATION);
					} else {
						Notification.show("Die Intensivfrage ist bereits 0", Type.WARNING_MESSAGE);
					}
				}

				if (e.getSource() == bEmailkontakt) {

					Date date = null;
					try {
						String datumStatistik = sdfDate.format(benutzungsstatistik.getDatum());
						String gewählteUhrzeit = sdfHour.format(
								sdfTimestamp.parse(sdfDate.format(new Date()) + " " + ausgewählteUhrzeit + ":00:00"));
						String timestamp = datumStatistik + " " + gewählteUhrzeit;
						date = sdfTimestamp.parse(timestamp);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					benutzungsstatistik
							.addEmailkontakt(new Emailkontakt(new Timestamp(date.getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					emailzaehler++;
					lEmailkontakt.setValue("" + emailzaehler);
					Notification.show("+1 Emailkontakt" + ausgewählteUhrzeit + " Uhr", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bEmailkontaktMinus) {

					if (emailzaehler != 0) {
						Emailkontakt emailkontakt = null;
						for (Emailkontakt e1 : benutzungsstatistik.getEmailkontaktListe()) {
							if (Integer
									.parseInt(dateFormat.format(e1.getTimestamp().getTime())) == ausgewählteUhrzeit) {
								emailkontakt = e1;
							}
						}
						benutzungsstatistik.removeEmailkontakt(emailkontakt);
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

						emailzaehler--;
						lEmailkontakt.setValue("" + emailzaehler);
						Notification.show("-1 Emailkontakt " + ausgewählteUhrzeit + " Uhr", Type.TRAY_NOTIFICATION);
					} else {
						Notification.show("Der Emailkontakt ist bereits 0", Type.WARNING_MESSAGE);
					}
				}

				if (e.getSource() == bTelefonkontakt) {

					Date date = null;
					try {
						String datumStatistik = sdfDate.format(benutzungsstatistik.getDatum());
						String gewählteUhrzeit = sdfHour.format(
								sdfTimestamp.parse(sdfDate.format(new Date()) + " " + ausgewählteUhrzeit + ":00:00"));
						String timestamp = datumStatistik + " " + gewählteUhrzeit;
						date = sdfTimestamp.parse(timestamp);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					benutzungsstatistik
							.addTelefonkontakt(new Telefonkontakt(new Timestamp(date.getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					telefonzaehler++;
					lTelefonkontakt.setValue("" + telefonzaehler);
					Notification.show("+1 Telefonkontakt " + ausgewählteUhrzeit + " Uhr", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bTelefonkontaktMinus) {

					if (telefonzaehler != 0) {
						Telefonkontakt telefonkontakt = null;
						for (Telefonkontakt t : benutzungsstatistik.getTelefonkontaktListe()) {
							if (Integer.parseInt(dateFormat.format(t.getTimestamp().getTime())) == ausgewählteUhrzeit) {
								telefonkontakt = t;
							}
						}
						benutzungsstatistik.removeTelefonkontakt(telefonkontakt);
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

						telefonzaehler--;
						lTelefonkontakt.setValue("" + telefonzaehler);
						Notification.show("-1 Telefonkontakt " + ausgewählteUhrzeit + " Uhr", Type.TRAY_NOTIFICATION);
					} else {
						Notification.show("Der Telefonkontakt ist bereits 0", Type.WARNING_MESSAGE);
					}
				}

//				if (e.getSource() == bRechercheBeratung) {
//					benutzungsstatistik
//							.setAnzahl_Rechercheberatung(benutzungsstatistik.getAnzahl_Rechercheberatung() + 1);
//					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
//
//					lRechercheberatung
//							.setValue("Rechercheberatung: " + benutzungsstatistik.getAnzahl_Rechercheberatung());
//
//					Notification.show(
//							"+1 Rechercheberatung, insgesamt " + benutzungsstatistik.getAnzahl_Rechercheberatung(),
//							Type.TRAY_NOTIFICATION);
//				}
//
//				if (e.getSource() == bRechercheBeratungMinus) {
//					if (benutzungsstatistik.getAnzahl_Rechercheberatung() > 0) {
//						benutzungsstatistik
//								.setAnzahl_Rechercheberatung(benutzungsstatistik.getAnzahl_Rechercheberatung() - 1);
//						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
//
//						lRechercheberatung
//								.setValue("Rechercheberatung: " + benutzungsstatistik.getAnzahl_Rechercheberatung());
//
//						Notification.show(
//								"+1 Rechercheberatung, insgesamt " + benutzungsstatistik.getAnzahl_Rechercheberatung(),
//								Type.TRAY_NOTIFICATION);
//					} else {
//						Notification.show("Die Rechercheberatung ist bereits 0", Type.WARNING_MESSAGE);
//					}
//				}

				if (e.getSource() == bKorrekturInternerkurier) {
					getUI().getNavigator().navigateTo(InternerkurierViewWaedi.NAME + '/'
							+ benutzungsstatistik.getBenutzungsstatistik_ID() + '/' + true);
				}

			}
		};

	}

}
