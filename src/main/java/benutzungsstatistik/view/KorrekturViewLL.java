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
import benutzungsstatistik.model.BeantwortungBibliothekspersonal;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Intensivfrage;

/**
 * View der Korrektur. Zeigt alle Button, Label, Felder etc. in einem Layout an.
 * 
 * @author Marvin Bindemann
 */
@SuppressWarnings("serial")
@Theme("mytheme")
public class KorrekturViewLL extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-Korrektur-LL";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bBenutzerkontakt;
	private Button bIntensivFrage;
	private Button bBeantwortung;
	private Button bBenutzerkontaktMinus;
	private Button bIntensivFrageMinus;
	private Button bBeantwortungMinus;
	private Label lBenutzerkontakt;
	private Label lIntensivFrage;
	private Label lBeantwortung;
	private int ausgewählteUhrzeit;
	private int benutzerzaehler = 0;
	private int intensivzaehler = 0;
	private int beantwortungzaehler = 0;
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

	public KorrekturViewLL() {

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

		bBeantwortung = new Button();
		bBeantwortung.setCaption("Beant. Bilbiothekspersonal");
		bBeantwortung.setEnabled(false);
		bBeantwortung.setIcon(VaadinIcons.BOOK);
		bBeantwortung.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBeantwortung.addClickListener(createClickListener());

		lBeantwortung = new Label();
		lBeantwortung.setValue("0");
		lBeantwortung.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bBeantwortungMinus = new Button();
		bBeantwortungMinus.setCaption("Korrektur -1");
		bBeantwortungMinus.setEnabled(false);
		bBeantwortungMinus.addStyleName(ValoTheme.BUTTON_DANGER);
		bBeantwortungMinus.addClickListener(createClickListener());

		List<String> data = Arrays.asList(new String[] { "Bitte wählen ↓", "08-09", "09-10", "10-11", "11-12", "12-13",
				"13-14", "14-15", "15-16", "16-17", "17-18", "18-19", "19-20" });
		NativeSelect<String> uhrzeitListSelect = new NativeSelect<>("Uhrzeit:", data);
		uhrzeitListSelect.setSelectedItem(data.get(0));
		uhrzeitListSelect.setEmptySelectionAllowed(false);
		uhrzeitListSelect.setWidth(100.0f, Unit.PERCENTAGE);
		uhrzeitListSelect.addValueChangeListener(event -> {
			if (!String.valueOf(event.getValue()).equals("Bitte wählen ↓")) {
				bBenutzerkontakt.setEnabled(true);
				bBenutzerkontaktMinus.setEnabled(true);
				bBeantwortung.setEnabled(true);
				bBeantwortungMinus.setEnabled(true);
				bIntensivFrage.setEnabled(true);
				bIntensivFrageMinus.setEnabled(true);

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
				case "18-19":
					ausgewählteUhrzeit = 18;
					break;
				case "19-20":
					ausgewählteUhrzeit = 19;
					break;
				}

				benutzerzaehler = 0;
				beantwortungzaehler = 0;
				intensivzaehler = 0;
				for (Benutzerkontakt b : benutzungsstatistik.getBenutzerkontaktListe()) {
					if (Integer.parseInt(dateFormat.format(b.getTimestamp().getTime())) == ausgewählteUhrzeit) {
						benutzerzaehler++;
					}
				}
				lBenutzerkontakt.setValue("" + benutzerzaehler);
				for (BeantwortungBibliothekspersonal e : benutzungsstatistik
						.getBeantwortungBibliothekspersonalListe()) {
					if (Integer.parseInt(dateFormat.format(e.getTimestamp().getTime())) == ausgewählteUhrzeit) {
						beantwortungzaehler++;
					}
				}
				lBeantwortung.setValue("" + beantwortungzaehler);
				for (Intensivfrage i : benutzungsstatistik.getIntensivfrageListe()) {
					if (Integer.parseInt(dateFormat.format(i.getTimestamp().getTime())) == ausgewählteUhrzeit) {
						intensivzaehler++;
					}
				}
				lIntensivFrage.setValue("" + intensivzaehler);
			} else {
				bBenutzerkontakt.setEnabled(false);
				bBenutzerkontaktMinus.setEnabled(false);
				bBeantwortung.setEnabled(false);
				bBeantwortungMinus.setEnabled(false);
				bIntensivFrage.setEnabled(false);
				bIntensivFrageMinus.setEnabled(false);
			}

		});

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
					StandortEnum.WINTERTHUR_BB);

			// Alle Werte anpassen
			uhrzeitListSelect.setSelectedItem(data.get(0));
			bBenutzerkontakt.setEnabled(false);
			bBenutzerkontaktMinus.setEnabled(false);
			bBeantwortung.setEnabled(false);
			bBeantwortungMinus.setEnabled(false);
			bIntensivFrage.setEnabled(false);
			bIntensivFrageMinus.setEnabled(false);
			lBenutzerkontakt.setValue("0");
			lBeantwortung.setValue("0");
			lIntensivFrage.setValue("0");
		});

		GridLayout grid = new GridLayout(5, 8);
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0, 2, 0);
		grid.addComponent(datefield, 3, 0, 4, 0);
		grid.addComponent(uhrzeitListSelect, 0, 1, 0, 2);
		grid.addComponent(new Label(), 0, 3);
		grid.addComponent(new Label(), 0, 4);
		grid.addComponent(new Label(), 0, 5);
		grid.addComponent(bBenutzerkontakt, 1, 1, 1, 3);
		grid.addComponent(lBenutzerkontakt, 1, 4);
		grid.addComponent(bBenutzerkontaktMinus, 1, 5);
		grid.addComponent(bIntensivFrage, 2, 1, 2, 3);
		grid.addComponent(lIntensivFrage, 2, 4);
		grid.addComponent(bIntensivFrageMinus, 2, 5);
		grid.addComponent(bBeantwortung, 3, 1, 4, 3);
		grid.addComponent(lBeantwortung, 3, 4, 4, 4);
		grid.addComponent(bBeantwortungMinus, 3, 5, 4, 5);
//		grid.addComponent(new Label(), 4, 1, 4, 3);
//		grid.addComponent(new Label(), 4, 4);
//		grid.addComponent(new Label(), 4, 5);
		grid.addComponent(new Label(), 0, 6);
		grid.addComponent(new Label(), 0, 7);
		grid.addComponent(new Label(), 1, 6, 2, 6);
		grid.addComponent(new Label(), 1, 7);
		grid.addComponent(new Label(), 2, 7);
		grid.addComponent(new Label(), 3, 6, 3, 7);
		grid.addComponent(new Label(), 4, 6, 4, 7);
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
				} else if (row == 6) {
					grid.setComponentAlignment(c, Alignment.BOTTOM_CENTER);
				} else {
					if (row == 7 && col == 0) {
						// Kassenbeleg
					} else {
						c.setHeight("80%");
						c.setWidth("80%");
					}
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
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewLL.NAME);
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
					Notification.show("+1 Benutzerkontakt " + ausgewählteUhrzeit + " Uhr", Type.TRAY_NOTIFICATION);
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
					Notification.show("+1 Intensivfrage " + ausgewählteUhrzeit + " Uhr", Type.TRAY_NOTIFICATION);
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

				if (e.getSource() == bBeantwortung) {

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

					benutzungsstatistik.addBeantwortungBibliothekspersonal(
							new BeantwortungBibliothekspersonal(new Timestamp(date.getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					beantwortungzaehler++;
					lBeantwortung.setValue("" + beantwortungzaehler);
					Notification.show("+1 Beantwortung Bibliothekspersonal " + ausgewählteUhrzeit + " Uhr",
							Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bBeantwortungMinus) {

					if (beantwortungzaehler != 0) {
						BeantwortungBibliothekspersonal beantwortungBibliothekspersonal = null;
						for (BeantwortungBibliothekspersonal e1 : benutzungsstatistik
								.getBeantwortungBibliothekspersonalListe()) {
							if (Integer
									.parseInt(dateFormat.format(e1.getTimestamp().getTime())) == ausgewählteUhrzeit) {
								beantwortungBibliothekspersonal = e1;
							}
						}
						benutzungsstatistik.removeBeantwortungBibliothekspersonal(beantwortungBibliothekspersonal);
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

						beantwortungzaehler--;
						lBeantwortung.setValue("" + beantwortungzaehler);
						Notification.show("-1 Beantwortung Bibliothekspersonal " + ausgewählteUhrzeit + " Uhr",
								Type.TRAY_NOTIFICATION);
					} else {
						Notification.show("Die Beantwortung Bibliothekspersonal ist bereits 0", Type.WARNING_MESSAGE);
					}
				}

			}
		};

	}

}
