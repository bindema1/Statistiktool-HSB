package belegung.view;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
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
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import allgemein.view.StartseiteView;
import belegung.bean.TagesübersichtBelegungBean;
import belegung.db.BelegungsDatenbank;
import belegung.model.Arbeitsplätze;
import belegung.model.Belegung;
import belegung.model.Kapazität;
import belegung.model.Stockwerk;
import belegung.model.StockwerkEnum;
import belegung.model.UhrzeitEnum;

/**
 * View der Belegung. Zeigt alle Button, Label, Felder etc. in einem Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class BelegungErfassenViewWaedi extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Belegung-Wädenswil";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bTagesübersicht;
	private Button bKorrektur;
	private Button bValidieren;
	private Button bPersonen;
	private Button bPersonen10;
	private Button bPersonen5;
	private Button bPersonenMinus;
	private TextField tTotalPersonen;
	private UhrzeitEnum ausgewählteUhrzeit;
	private boolean korrektur;
	private Belegung belegung;
	private StockwerkEnum stockwerkEnum;
	private Date date;
	private BelegungsDatenbank belegungDB = new BelegungsDatenbank();
	private List<String> data;
	private Grid<TagesübersichtBelegungBean> tabelleUhrzeiten;

	/**
	 * Bildet das AbsoluteLayout, als Wrapper um die ganze View
	 * 
	 * @return AbsoluteLayout
	 */
	private AbsoluteLayout buildMainLayout() {
		mainLayout = new AbsoluteLayout();
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

	public BelegungErfassenViewWaedi() {

	}

	private void initData() {
		// Setzt das Datum
		date = belegung.getDatum();
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
		if (korrektur == true) {
			lText.setValue("Belegung korrigieren vom ");
		} else {
			lText.setValue("Belegung erfassen vom " + new SimpleDateFormat("dd.MM.yyyy").format(belegung.getDatum()));
		}
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bTagesübersicht = new Button();
		bTagesübersicht.setCaption("Tagesübersicht");
		bTagesübersicht.addStyleName(ValoTheme.BUTTON_LARGE);
		bTagesübersicht.addClickListener(createClickListener());

		bValidieren = new Button();
		bValidieren.setCaption("Zählung validieren");
		bValidieren.addStyleName(ValoTheme.BUTTON_LARGE);
		bValidieren.addClickListener(createClickListener());

		bKorrektur = new Button();
		if (korrektur == false) {
			bKorrektur.setCaption("Korrektur");
		} else {
			bKorrektur.setCaption("Erfassung");
		}
		bKorrektur.addStyleName(ValoTheme.BUTTON_LARGE);
		bKorrektur.addClickListener(createClickListener());

		bPersonen = new Button();
		bPersonen.setCaption("+1 Arbeitsplatz");
		bPersonen.addStyleName(ValoTheme.BUTTON_LARGE);
		bPersonen.addClickListener(createClickListener());

		bPersonen10 = new Button();
		bPersonen10.setCaption("+ 10");
		bPersonen10.addStyleName(ValoTheme.BUTTON_LARGE);
		bPersonen10.addClickListener(createClickListener());

		bPersonen5 = new Button();
		bPersonen5.setCaption("+ 5");
		bPersonen5.addStyleName(ValoTheme.BUTTON_LARGE);
		bPersonen5.addClickListener(createClickListener());

		bPersonenMinus = new Button();
		bPersonenMinus.setCaption("-1 Korrektur");
		bPersonenMinus.addStyleName(ValoTheme.BUTTON_DANGER);
		bPersonenMinus.addClickListener(createClickListener());

		tTotalPersonen = new TextField();
		tTotalPersonen.setPlaceholder("Total Personen");
		tTotalPersonen.setValue("0");
		tTotalPersonen.addValueChangeListener(event -> {
			// Wenn das Textfeld sich verändert, soll die Eingabe gespeichert werden, dies
			// passiert bei Klick auf einen Button oder beim Eingeben einer Zahl
			if(Integer.parseInt(event.getValue()) != 0) {
				speichereEingaben();
			}
		});

		// Eine Liste von Uhrzeiten zum auswählen
		data = null;
		NativeSelect<String> uhrzeitListSelect;
		if (korrektur == true) {
			data = Arrays.asList(new String[] { "Bitte wählen ↓", "11 Uhr", "15 Uhr" });
			uhrzeitListSelect = new NativeSelect<>("Uhrzeit:", data);
			uhrzeitListSelect.setEmptySelectionAllowed(false);
			if (ausgewählteUhrzeit != null) {
				if (ausgewählteUhrzeit == UhrzeitEnum.ELF) {
					uhrzeitListSelect.setSelectedItem(data.get(1));
				} else if (ausgewählteUhrzeit == UhrzeitEnum.FÜNFZEHN) {
					uhrzeitListSelect.setSelectedItem(data.get(2));
				}
			} else {
				uhrzeitListSelect.setSelectedItem(data.get(0));
				setButtonEnabled(false);
			}
		} else {
			data = Arrays.asList(new String[] { "11 Uhr", "15 Uhr" });
			uhrzeitListSelect = new NativeSelect<>("Uhrzeit:", data);
			uhrzeitListSelect.setEmptySelectionAllowed(false);

			int time = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
			if (time <= 13) {
				uhrzeitListSelect.setSelectedItem(data.get(0));
				ausgewählteUhrzeit = UhrzeitEnum.ELF;
			} else if (time >= 13) {
				uhrzeitListSelect.setSelectedItem(data.get(1));
				ausgewählteUhrzeit = UhrzeitEnum.FÜNFZEHN;
			}
		}

		uhrzeitListSelect.setWidth(100.0f, Unit.PERCENTAGE);
		if (korrektur == true) {
			uhrzeitListSelect.setEnabled(true);
		} else {
			uhrzeitListSelect.setEnabled(false);
		}
		uhrzeitListSelect.addValueChangeListener(event -> {

			// Alle Button enablen
			setButtonEnabled(true);

			Notification.show(event.getValue(), Type.TRAY_NOTIFICATION);

			// Setzt die ausgewählte Uhrzeit
			switch (event.getValue()) {
			case "11 Uhr":
				ausgewählteUhrzeit = UhrzeitEnum.ELF;
				break;
			case "15 Uhr":
				ausgewählteUhrzeit = UhrzeitEnum.FÜNFZEHN;
				break;
			}

			if (ausgewählteUhrzeit != null) {
				// Das GridLayout mit Zahlen füllen
				layoutMitZahlenFüllen();

				// Tabelle füllen
				fülleTabelleUhrzeiten(tabelleUhrzeiten);
			}
		});

		DateField datefield = new DateField();
		datefield.setValue(
				Instant.ofEpochMilli(belegung.getDatum().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
		datefield.setDateFormat("dd.MM.yyyy");
		if(!VaadinSession.getCurrent().getAttribute("user").toString().contains("Admin")) {
			//Nicht-Administratoren dürfen nur eine Woche in der Zeit zurück
			datefield.setRangeStart(LocalDate.now().minusDays(7));
			datefield.setRangeEnd(LocalDate.now());
		}
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			ZonedDateTime zdt = event.getValue().atStartOfDay().atZone(ZoneId.systemDefault());
			date = Date.from(zdt.toInstant());

			// Sucht die Belegung für das ausgewählte Datum und für den jeweiligen Standort
			if (stockwerkEnum == StockwerkEnum.WÄDI) {
				belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WÄDENSWIL);
			}

			// Alle Button disabled setzen, da der User erst eine Uhrzeit auswählen muss
			uhrzeitListSelect.setSelectedItem(data.get(0));
			setButtonEnabled(false);
		});

		// Tabelle für die Übersicht ganz oben
		tabelleUhrzeiten = new Grid<TagesübersichtBelegungBean>();
		tabelleUhrzeitenAufsetzen(tabelleUhrzeiten);
		if (korrektur == false) {
			fülleTabelleUhrzeiten(tabelleUhrzeiten);
		}

		GridLayout grid = new GridLayout(8, 10);
		if (korrektur == true) {
			grid.addStyleName("gridlayout" + " backgroundKorrektur");
		} else {
			grid.addStyleName("gridlayout" + " backgroundErfassung");
		}
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0);
		if (korrektur == true) {
			grid.addComponent(lText, 1, 0, 3, 0);
			grid.addComponent(datefield, 4, 0);
		} else {
			grid.addComponent(lText, 1, 0, 4, 0);
		}
		grid.addComponent(bKorrektur, 5, 0);
		grid.addComponent(bValidieren, 6, 0, 7, 0);

		// Tabelle der Uhrzeit
		grid.addComponent(tabelleUhrzeiten, 0, 1, 7, 2);
		grid.addComponent(new Label(), 0, 3, 7, 3);

		// Uhrzeiten Links
		grid.addComponent(bTagesübersicht, 0, 4, 1, 4);
		grid.addComponent(uhrzeitListSelect, 0, 5, 1, 6);
		grid.addComponent(new Label(), 0, 7, 1, 7);

		// Arbeitsplatz felder
		grid.addComponent(bPersonen10, 2, 4, 3, 4);
		grid.addComponent(bPersonen5, 2, 5, 3, 5);
		grid.addComponent(bPersonenMinus, 2, 6, 3, 6);
		grid.addComponent(new Label(), 2, 7, 3, 7);
		grid.addComponent(bPersonen, 4, 4, 7, 6);
		grid.addComponent(tTotalPersonen, 4, 7, 7, 7);
		grid.addComponent(new Label(), 0, 8, 7, 9);

		// Das GridLayout mit Zahlen füllen
		layoutMitZahlenFüllen();

		grid.setColumnExpandRatio(0, 0.125f);
		grid.setColumnExpandRatio(1, 0.125f);
		grid.setColumnExpandRatio(2, 0.125f);
		grid.setColumnExpandRatio(3, 0.125f);
		grid.setColumnExpandRatio(4, 0.125f);
		grid.setColumnExpandRatio(5, 0.125f);
		grid.setColumnExpandRatio(6, 0.125f);
		grid.setColumnExpandRatio(7, 0.125f);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row == 0) {
					if (col == 1 || col == 2) {
						grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
					}
				} else {

					// Tabelle
					if (row >= 1 && row <= 3) {
						c.setHeight("60%");
						c.setWidth("95%");
					}else {
						// Alles andere
						c.setHeight("90%");
						c.setWidth("80%");
					}
				}
			}
		}

		mainLayout.addComponent(grid);
	}

	/**
	 * Enabled oder Disabled alle Button
	 * 
	 * @param wert
	 */
	private void setButtonEnabled(boolean wert) {
		bPersonen.setEnabled(wert);
		bPersonen5.setEnabled(wert);
		bPersonen10.setEnabled(wert);
		bPersonenMinus.setEnabled(wert);
	}

	/**
	 * Füllt die Tabelle mit Inhalt
	 * 
	 * @param tabelleUhrzeiten
	 */
	private void fülleTabelleUhrzeiten(Grid<TagesübersichtBelegungBean> tabelleUhrzeiten) {

		List<TagesübersichtBelegungBean> beanListe = new ArrayList<>();

		// Sucht das richtige Stockwerk
		Stockwerk stockwerk = null;
		for (Stockwerk s : belegung.getStockwerkListe()) {
			if (s.getName() == stockwerkEnum) {
				stockwerk = s;
			}
		}

		UhrzeitEnum uhrzeitEnum = ausgewählteUhrzeit;

		TagesübersichtBelegungBean t = new TagesübersichtBelegungBean();
		// Setzt den UhrzeitString für die ausgewählte Uhrzeit
		String uhrzeitEnumString = "";
		switch (uhrzeitEnum) {
		case ELF:
			uhrzeitEnumString = 11 + "";
			break;
		case FÜNFZEHN:
			uhrzeitEnumString = 15 + "";
			break;
		default:
			break;
		}
		uhrzeitEnumString = uhrzeitEnumString + " Uhr";
		t.setUhrzeit(uhrzeitEnumString);

		// Sucht die Anzahl Personen für die ausgewählte Uhrzeit aus den Arbeitsplätzen
		for (Arbeitsplätze arbeitsplätze : stockwerk.getArbeitsplatzListe()) {
			if (uhrzeitEnum == arbeitsplätze.getUhrzeit()) {
				t.setArbeitsplätze(arbeitsplätze.getAnzahlPersonen());
			}
		}

		beanListe.add(t);

		tabelleUhrzeiten.setHeightByRows(beanListe.size());
		tabelleUhrzeiten.setItems(beanListe);
	}

	/**
	 * Setzt die Tabelle auf für die richtige Uhrzeit mit den richtigen
	 * Namen/Sektoren
	 * 
	 * @param tabelleUhrzeiten
	 */
	private void tabelleUhrzeitenAufsetzen(Grid<TagesübersichtBelegungBean> tabelleUhrzeiten) {
		tabelleUhrzeiten.addColumn(TagesübersichtBelegungBean::getUhrzeit).setCaption("Uhrzeit");
		if (stockwerkEnum == StockwerkEnum.WÄDI) {
			tabelleUhrzeiten.addColumn(TagesübersichtBelegungBean::getArbeitsplätze).setCaption("Arbeitsplätze");
		}
	}

	/**
	 * Das GridLayout wird mit Zahlen von der Datenbank für die ausgewählte Uhrzeit
	 * gefüllt
	 */
	private void layoutMitZahlenFüllen() {

		tTotalPersonen.setValue("0");

		// Prüft alle Stockwerke und setzt das jeweilige Layout dafür
		if (stockwerkEnum == StockwerkEnum.WÄDI) {

			for (Stockwerk s : belegung.getStockwerkListe()) {
				if (s.getName() == stockwerkEnum) {
					for (Arbeitsplätze a : s.getArbeitsplatzListe()) {
						if (a.getUhrzeit() == ausgewählteUhrzeit) {
							tTotalPersonen.setValue("" + a.getAnzahlPersonen());
						}
					}
				}
			}
		}
	}

	/**
	 * Speichert den Wert in der Datenbank, wenn er kleiner als die Maximale
	 * Kapazität ist
	 */
	private void speichereEingaben() {
		try {
			int anzahlPersonen = Integer.parseInt(tTotalPersonen.getValue());

			if (pruefeMaximalKapazitaeten(0, 0) == true) {

				if (anzahlPersonen == 0) {
					Notification.show("Sie haben eine Zählung mit 0 gespeichert", Type.WARNING_MESSAGE);
				}

				boolean eintragVorhanden = false;

				if (stockwerkEnum == StockwerkEnum.WÄDI) {
					for (Stockwerk s : belegung.getStockwerkListe()) {
						if (s.getName() == stockwerkEnum) {
							// Bei einem zweiten Aufruf muss der Arbeitsplatz geupdatet werden
							for (Arbeitsplätze a : s.getArbeitsplatzListe()) {
								if (a.getUhrzeit() == ausgewählteUhrzeit) {
									eintragVorhanden = true;
									a.setAnzahlPersonen(anzahlPersonen);
								}
							}

							// Falls es keinen Arbeitsplatz für die ausgewählte Uhrzeit gibt
							if (eintragVorhanden == false) {
								Arbeitsplätze a = new Arbeitsplätze(anzahlPersonen, ausgewählteUhrzeit, s);
								s.addArbeitsplätze(a);
							}
						}
					}
				}

				// Update der Belegung
				belegungDB.updateBelegung(belegung);
				fülleTabelleUhrzeiten(tabelleUhrzeiten);

//				Notification.show("Zählung gespeichert", Type.TRAY_NOTIFICATION);
			}
		} catch (NumberFormatException e1) {
			// Not an integer
			Notification.show("Die Eingabe muss eine Zahl sein", Type.WARNING_MESSAGE);
		}
	}

	/**
	 * Prüft die Maximal Kapizitäten, sodass nicht zu hohe Werte in der Datenbank
	 * entstehen können
	 * 
	 * @param i
	 * 
	 * @return boolean
	 */
	private boolean pruefeMaximalKapazitaeten(int personen, int räume) {

		try {
			int anzahlPersonen = Integer.parseInt(tTotalPersonen.getValue());
			anzahlPersonen += personen;

			if (stockwerkEnum == StockwerkEnum.WÄDI) {
				for (Stockwerk s : belegung.getStockwerkListe()) {
					if (s.getName() == stockwerkEnum) {
						Kapazität kapazität = s.getKapzität();
						if (anzahlPersonen > kapazität.getMaxArbeitsplätze()) {
							Notification.show(
									"Arbeitsplätze darf nicht mehr sein als " + kapazität.getMaxArbeitsplätze(),
									Type.WARNING_MESSAGE);
							return false;
						}
					}
				}
			}

			return true;
		} catch (NumberFormatException e1) {
			// Not an integer
			Notification.show("Die Eingabe muss eine Zahl sein", Type.WARNING_MESSAGE);
		}

		return false;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Der Eingang der Belegung ist eine lange URL, welche durch / abgetrennt wird
		String args[] = event.getParameters().split("/");
		// Datum für die Belegung
		String datumString = args[0];
		// Stockwerk der Belegung
		String stockwerkString = args[1];
		// Ist es eine Korrektur oder nicht
		String korrekturString = args[2];
		// Welche Uhrzeit wurde im Dropdown ausgewählt
		String ausgewählteUhrzeitString = args[3];

		if (datumString.equals(" ")) {
			this.date = new Date();
		} else {
			this.date = new Date(Long.parseLong(datumString));
		}

		if (stockwerkString.equals("WÄDI")) {
			this.stockwerkEnum = StockwerkEnum.WÄDI;
		}

		if (stockwerkEnum == StockwerkEnum.WÄDI) {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WÄDENSWIL);
		}

		this.korrektur = Boolean.parseBoolean(korrekturString);

		switch (ausgewählteUhrzeitString) {
		case "ELF":
			this.ausgewählteUhrzeit = UhrzeitEnum.ELF;
			break;
		case "FÜNFZEHN":
			this.ausgewählteUhrzeit = UhrzeitEnum.FÜNFZEHN;
			break;
		default:
			break;
		}

		setCompositionRoot(init());
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					if (korrektur == true) {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWaedi.NAME + '/' + " " + '/'
								+ stockwerkEnum.toString() + '/' + false + '/' + " ");
					} else {
						Page.getCurrent().setUriFragment("!" + StartseiteView.NAME);
					}
				}

				if (e.getSource() == bPersonen) {
					erhöheOderVermindereTextfieldNachNummer(tTotalPersonen, 1, 0);
				}

				if (e.getSource() == bPersonen5) {
					erhöheOderVermindereTextfieldNachNummer(tTotalPersonen, 5, 0);
				}

				if (e.getSource() == bPersonen10) {
					erhöheOderVermindereTextfieldNachNummer(tTotalPersonen, 10, 0);
				}

				if (e.getSource() == bPersonenMinus) {
					erhöheOderVermindereTextfieldNachNummer(tTotalPersonen, -1, 0);
				}

				if (e.getSource() == bValidieren) {
					// Prüft alle Eingaben und weisst den User darauf hin, wenn es irgendwo eine 0
					// gibt

					Stockwerk stockwerk = null;
					for (Stockwerk s : belegung.getStockwerkListe()) {
						if (s.getName() == stockwerkEnum) {
							stockwerk = s;
						}
					}

					UhrzeitEnum uhrzeitEnum = ausgewählteUhrzeit;
					boolean arbeitsplatzIsEmpty = true;

					for (Arbeitsplätze arbeitsplätze : stockwerk.getArbeitsplatzListe()) {
						if (uhrzeitEnum == arbeitsplätze.getUhrzeit()) {
							if (arbeitsplätze.getAnzahlPersonen() != 0) {
								arbeitsplatzIsEmpty = false;
							}
						}
					}

					String validierung = "";
					if (stockwerkEnum == StockwerkEnum.WÄDI) {
						if (arbeitsplatzIsEmpty == true) {
							if (arbeitsplatzIsEmpty == true)
								validierung += "Arbeitsplatz, ";
							validierung += "hat einen 0 wert";
						}
					}

					if (validierung.equals("")) {
						validierung = "Validierung erfolgreich, keine Fehler";
					}

					Notification.show(validierung, Type.WARNING_MESSAGE);
				}

				if (e.getSource() == bTagesübersicht) {
					getUI().getNavigator().navigateTo(TagesübersichtBelegungViewWaedi.NAME + '/' + date.getTime() + '/'
							+ stockwerkEnum.toString() + '/' + false);
				}

				if (e.getSource() == bKorrektur) {
					if (korrektur == false) {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWaedi.NAME + '/' + date.getTime() + '/'
								+ stockwerkEnum.toString() + '/' + true + '/' + " ");
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWaedi.NAME + '/' + " " + '/'
								+ stockwerkEnum.toString() + '/' + false + '/' + " ");
					}
				}

			}

			/**
			 * Erhöht oder vermindert das Textfield nach einer bestimmten Nummer
			 * 
			 * @param textfield
			 * @param i
			 */
			private void erhöheOderVermindereTextfieldNachNummer(TextField textfield, int personen, int räume) {
				try {
					int anzahl = Integer.parseInt(textfield.getValue());
					anzahl = anzahl + personen + räume;

					if (pruefeMaximalKapazitaeten(personen, räume) == true) {
						if (anzahl >= 0) {
							// Textfelder erhöhen
							textfield.setValue("" + anzahl);
						} else {
							Notification.show("Die Eingabe darf keine Minuszahl sein", Type.WARNING_MESSAGE);
						}
					}
				} catch (NumberFormatException e1) {
					// Not an integer
					Notification.show("Die Eingabe muss eine Zahl sein", Type.WARNING_MESSAGE);
				}
			}

		};

	}

}