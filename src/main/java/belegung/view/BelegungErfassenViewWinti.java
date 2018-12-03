package belegung.view;

import java.text.SimpleDateFormat;
import java.time.Instant;
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
import com.vaadin.server.ClassResource;
import com.vaadin.server.Page;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import allgemein.view.StartseiteView;
import belegung.bean.TagesübersichtBelegungBean;
import belegung.db.BelegungsDatenbank;
import belegung.model.Arbeitsplätze;
import belegung.model.Belegung;
import belegung.model.Carrels;
import belegung.model.Gruppenräume;
import belegung.model.SektorA;
import belegung.model.SektorB;
import belegung.model.Stockwerk;
import belegung.model.StockwerkEnum;
import belegung.model.UhrzeitEnum;

/**
 * View der Belegung. Zeigt alle Button, Label, Felder etc. in einem Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class BelegungErfassenViewWinti extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Belegung-Winterthur";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bTagesübersicht;
	private Button bKorrektur;
	private Button bValidieren;
	private Button bLL;
	private Button b1ZG;
	private Button b2ZG;
	private Button bEG;
	private Button bPersonen;
	private Button bPersonen10;
	private Button bPersonen5;
	private Button bPersonenMinus;
	private Button bRäume;
	private Button bRäumeMinus;
	private Button bSpeichern;
	private Button bArbeitsplätze;
	private Button bSektorA;
	private Button bSektorB;
	private Button bGruppenräume;
	private Button bCarrels;
	private TextField tTotalPersonen;
	private TextField tTotalRäume;
	private UhrzeitEnum ausgewählteUhrzeit;
	private boolean korrektur;
	private boolean räumeVorhanden;
	private Belegung belegung;
	private StockwerkEnum stockwerkEnum;
	private Date date;
	private BelegungsDatenbank belegungDB = new BelegungsDatenbank();
	private Image image;
	private int erfassungsSchritt;
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

	public BelegungErfassenViewWinti() {

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
		bTagesübersicht.setCaption("Übersicht");
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

		bRäume = new Button();
		bRäume.setCaption("+1 Räume");
		bRäume.addStyleName(ValoTheme.BUTTON_LARGE);
		bRäume.addClickListener(createClickListener());

		bRäumeMinus = new Button();
		bRäumeMinus.setCaption("-1 Korrektur");
		bRäumeMinus.addStyleName(ValoTheme.BUTTON_DANGER);
		bRäumeMinus.addClickListener(createClickListener());

		bSpeichern = new Button();
		bSpeichern.addStyleName(ValoTheme.BUTTON_LARGE);
		bSpeichern.setCaption("Speichern");
		bSpeichern.addClickListener(createClickListener());

		tTotalPersonen = new TextField();
		tTotalPersonen.setPlaceholder("Total Personen");
		tTotalPersonen.setValue("0");
		tTotalRäume = new TextField();
		tTotalRäume.setPlaceholder("Total Räume");
		tTotalRäume.setValue("0");

		// Eine Liste von Uhrzeiten zum auswählen
		data = null;
		NativeSelect<String> uhrzeitListSelect;
		if (korrektur == true) {
			data = Arrays.asList(
					new String[] { "Bitte wählen ↓", "9 Uhr", "11 Uhr", "13 Uhr", "15 Uhr", "17 Uhr", "19 Uhr" });
			uhrzeitListSelect = new NativeSelect<>("Uhrzeit:", data);
			uhrzeitListSelect.setEmptySelectionAllowed(false);
			if (ausgewählteUhrzeit != null) {
				if (ausgewählteUhrzeit == UhrzeitEnum.NEUN) {
					uhrzeitListSelect.setSelectedItem(data.get(1));
				} else if (ausgewählteUhrzeit == UhrzeitEnum.ELF) {
					uhrzeitListSelect.setSelectedItem(data.get(2));
				} else if (ausgewählteUhrzeit == UhrzeitEnum.DREIZEHN) {
					uhrzeitListSelect.setSelectedItem(data.get(3));
				} else if (ausgewählteUhrzeit == UhrzeitEnum.FÜNFZEHN) {
					uhrzeitListSelect.setSelectedItem(data.get(4));
				} else if (ausgewählteUhrzeit == UhrzeitEnum.SIEBZEHN) {
					uhrzeitListSelect.setSelectedItem(data.get(5));
				} else if (ausgewählteUhrzeit == UhrzeitEnum.NEUNZEHN) {
					uhrzeitListSelect.setSelectedItem(data.get(6));
				}
			} else {
				uhrzeitListSelect.setSelectedItem(data.get(0));
				setButtonEnabled(false);
			}
		} else {
			data = Arrays.asList(new String[] { "9 Uhr", "11 Uhr", "13 Uhr", "15 Uhr", "17 Uhr", "19 Uhr" });
			uhrzeitListSelect = new NativeSelect<>("Uhrzeit:", data);
			uhrzeitListSelect.setEmptySelectionAllowed(false);

			int time = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
			if (time <= 9) {
				uhrzeitListSelect.setSelectedItem(data.get(0));
				ausgewählteUhrzeit = UhrzeitEnum.NEUN;
			} else if (time <= 11) {
				uhrzeitListSelect.setSelectedItem(data.get(1));
				ausgewählteUhrzeit = UhrzeitEnum.ELF;
			} else if (time <= 13) {
				uhrzeitListSelect.setSelectedItem(data.get(2));
				ausgewählteUhrzeit = UhrzeitEnum.DREIZEHN;
			} else if (time <= 15) {
				uhrzeitListSelect.setSelectedItem(data.get(3));
				ausgewählteUhrzeit = UhrzeitEnum.FÜNFZEHN;
			} else if (time <= 17) {
				uhrzeitListSelect.setSelectedItem(data.get(4));
				ausgewählteUhrzeit = UhrzeitEnum.SIEBZEHN;
			} else if (time >= 18) {
				uhrzeitListSelect.setSelectedItem(data.get(5));
				ausgewählteUhrzeit = UhrzeitEnum.NEUNZEHN;
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
			case "9 Uhr":
				ausgewählteUhrzeit = UhrzeitEnum.NEUN;
				break;
			case "11 Uhr":
				ausgewählteUhrzeit = UhrzeitEnum.ELF;
				break;
			case "13 Uhr":
				ausgewählteUhrzeit = UhrzeitEnum.DREIZEHN;
				break;
			case "15 Uhr":
				ausgewählteUhrzeit = UhrzeitEnum.FÜNFZEHN;
				break;
			case "17 Uhr":
				ausgewählteUhrzeit = UhrzeitEnum.SIEBZEHN;
				break;
			case "19 Uhr":
				ausgewählteUhrzeit = UhrzeitEnum.NEUNZEHN;
				break;
			case "Bitte wählen ↓":
				setButtonEnabled(false);
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
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			ZonedDateTime zdt = event.getValue().atStartOfDay().atZone(ZoneId.systemDefault());
			date = Date.from(zdt.toInstant());

			// Sucht die Belegung für das ausgewählte Datum und für den jeweiligen Standort
			if (stockwerkEnum == StockwerkEnum.LL) {
				belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_LL);
			} else if (stockwerkEnum == StockwerkEnum.WÄDI) {
				belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WÄDENSWIL);
			} else {
				belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_BB);
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

		// Unteres Grid
		bLL = new Button();
		bLL.setCaption("LL");
		bLL.addStyleName(ValoTheme.BUTTON_LARGE);
		bLL.addClickListener(createClickListener());
		bEG = new Button();
		bEG.setCaption("EG");
		bEG.addStyleName(ValoTheme.BUTTON_LARGE);
		bEG.addClickListener(createClickListener());
		b1ZG = new Button();
		b1ZG.setCaption("1.ZG");
		b1ZG.addStyleName(ValoTheme.BUTTON_LARGE);
		b1ZG.addClickListener(createClickListener());
		b2ZG = new Button();
		b2ZG.setCaption("2.ZG");
		b2ZG.addStyleName(ValoTheme.BUTTON_LARGE);
		b2ZG.addClickListener(createClickListener());

		GridLayout grid = new GridLayout(5, 14);
		if (korrektur == true) {
			grid.addStyleName("gridlayout" + " backgroundKorrektur");
		} else {
			grid.addStyleName("gridlayout");
		}
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0);
		if (korrektur == true) {
			grid.addComponent(lText, 1, 0);
			grid.addComponent(datefield, 2, 0);
		} else {
			grid.addComponent(lText, 1, 0, 2, 0);
		}
		grid.addComponent(bKorrektur, 3, 0);
		grid.addComponent(bValidieren, 4, 0);

		grid.addComponent(tabelleUhrzeiten, 0, 1, 4, 3);

		grid.addComponent(bTagesübersicht, 0, 4);
		grid.addComponent(uhrzeitListSelect, 0, 5, 0, 6);
		grid.addComponent(new Label(), 0, 7);
		grid.addComponent(new Label(), 0, 8);

		if (räumeVorhanden == true) {
			grid.addComponent(bPersonen, 1, 4, 2, 7);
			grid.addComponent(tTotalPersonen, 1, 8);
			grid.addComponent(bPersonenMinus, 2, 8);
			grid.addComponent(bRäume, 3, 4, 4, 6);
			grid.addComponent(tTotalRäume, 3, 7);
			grid.addComponent(bRäumeMinus, 4, 7);
			grid.addComponent(bSpeichern, 3, 8, 4, 8);
		} else {
			grid.addComponent(bPersonen, 1, 4, 3, 7);
			grid.addComponent(tTotalPersonen, 1, 8, 3, 8);
			grid.addComponent(bPersonen10, 4, 4);
			grid.addComponent(bPersonen5, 4, 5);
			grid.addComponent(bPersonenMinus, 4, 6);
			grid.addComponent(bSpeichern, 4, 7, 4, 8);
		}
		grid.addComponent(bLL, 0, 9);
		grid.addComponent(b2ZG, 0, 10);
		grid.addComponent(b1ZG, 0, 11);
		grid.addComponent(bEG, 0, 12);
		grid.addComponent(new Label(), 0, 13);
		grid.addComponent(createAbsoluteLayoutForImage(), 1, 9, 4, 13);
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
				} else {

					if (räumeVorhanden == true) {
						if (row == 8 && col == 1 || row == 8 && col == 2 || row == 7 && col == 3
								|| row == 7 && col == 4) {
							c.setHeight("90%");
							c.setWidth("80%");
						} else {
							c.setHeight("90%");
							c.setWidth("90%");
						}
					} else {
						if (row == 8 && col == 1 || row == 8 && col == 2 || row == 8 && col == 3) {
							c.setHeight("90%");
							// c.setWidth("80%");
						} else {
							c.setHeight("90%");
							c.setWidth("90%");
						}
					}

					// Tabelle
					if (row >= 1 && row <= 3) {
						c.setHeight("86%");
						c.setWidth("95%");
					}

					// Bild mit Etagen
					if (row >= 9) {
						if (col == 0) {
							c.setHeight("100%");
							c.setWidth("70%");
						} else {
							c.setHeight("90%");
							c.setWidth("100%");
						}
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
		bRäume.setEnabled(wert);
		bRäumeMinus.setEnabled(wert);
		bSpeichern.setEnabled(wert);
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
		case NEUN:
			uhrzeitEnumString = 9 + "";
			break;
		case ELF:
			uhrzeitEnumString = 11 + "";
			break;
		case DREIZEHN:
			uhrzeitEnumString = 13 + "";
			break;
		case FÜNFZEHN:
			uhrzeitEnumString = 15 + "";
			break;
		case SIEBZEHN:
			uhrzeitEnumString = 17 + "";
			break;
		case NEUNZEHN:
			uhrzeitEnumString = 19 + "";
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

		// Sucht die Anzahl Personen für die ausgewählte Uhrzeit aus den SektorenA
		for (SektorA sektorA : stockwerk.getSektorAListe()) {
			if (uhrzeitEnum == sektorA.getUhrzeit()) {
				t.setSektorA(sektorA.getAnzahlPersonen());
			}
		}

		// Sucht die Anzahl Personen für die ausgewählte Uhrzeit aus den SektorenB
		for (SektorB sektorB : stockwerk.getSektorBListe()) {
			if (uhrzeitEnum == sektorB.getUhrzeit()) {
				t.setSektorB(sektorB.getAnzahlPersonen());
			}
		}

		// Sucht die Anzahl Personen für die ausgewählte Uhrzeit aus den Gruppenräumen
		for (Gruppenräume gruppenräume : stockwerk.getGruppenräumeListe()) {
			if (uhrzeitEnum == gruppenräume.getUhrzeit()) {
				t.setGruppenräume(gruppenräume.getAnzahlRäume());
				t.setGruppenräumePersonen(gruppenräume.getAnzahlPersonen());
			}
		}

		// Sucht die Anzahl Personen für die ausgewählte Uhrzeit aus den Carrels
		for (Carrels carrels : stockwerk.getCarrelsListe()) {
			if (uhrzeitEnum == carrels.getUhrzeit()) {
				t.setCarrels(carrels.getAnzahlRäume());
				t.setCarrelsPersonen(carrels.getAnzahlPersonen());
			}
		}

		beanListe.add(t);

		tabelleUhrzeiten.setWidth("90%");
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
		if (stockwerkEnum == StockwerkEnum.LL) {
			tabelleUhrzeiten.addColumn(TagesübersichtBelegungBean::getSektorA).setCaption("Sektor A");
			tabelleUhrzeiten.addColumn(TagesübersichtBelegungBean::getSektorB).setCaption("Sektor B");
			Column<TagesübersichtBelegungBean, ?> gruppeColumn1 = tabelleUhrzeiten
					.addColumn(TagesübersichtBelegungBean::getGruppenräumePersonen).setCaption("Personen");
			Column<TagesübersichtBelegungBean, ?> gruppeColumn2 = tabelleUhrzeiten
					.addColumn(TagesübersichtBelegungBean::getGruppenräume).setCaption("Räume");
			Column<TagesübersichtBelegungBean, ?> carrelColumn1 = tabelleUhrzeiten
					.addColumn(TagesübersichtBelegungBean::getCarrelsPersonen).setCaption("Personen");
			Column<TagesübersichtBelegungBean, ?> carrelColumn2 = tabelleUhrzeiten
					.addColumn(TagesübersichtBelegungBean::getCarrels).setCaption("Räume");
			HeaderRow second = tabelleUhrzeiten.prependHeaderRow();
			second.join(gruppeColumn1, gruppeColumn2).setText("Gruppenräume");
			second.join(carrelColumn1, carrelColumn2).setText("Carrels");

		} else if (stockwerkEnum == StockwerkEnum.ZG1 || stockwerkEnum == StockwerkEnum.ZG2) {
			tabelleUhrzeiten.addColumn(TagesübersichtBelegungBean::getArbeitsplätze).setCaption("Arbeitsplätze");

		} else if (stockwerkEnum == StockwerkEnum.EG) {
			tabelleUhrzeiten.addColumn(TagesübersichtBelegungBean::getArbeitsplätze).setCaption("Arbeitsplätze");
			Column<TagesübersichtBelegungBean, ?> gruppeColumn1 = tabelleUhrzeiten
					.addColumn(TagesübersichtBelegungBean::getGruppenräumePersonen).setCaption("Personen");
			Column<TagesübersichtBelegungBean, ?> gruppeColumn2 = tabelleUhrzeiten
					.addColumn(TagesübersichtBelegungBean::getGruppenräume).setCaption("Räume");
			HeaderRow second = tabelleUhrzeiten.prependHeaderRow();
			second.join(gruppeColumn1, gruppeColumn2).setText("Gruppenräume");
		}
	}

	/**
	 * Erstellt ein AbsoluteLayout, welches das Bild als hintergrund hat sowie
	 * Button darauf, welche per Pixel angeordnet sind
	 * 
	 * @return Component
	 */
	private Component createAbsoluteLayoutForImage() {

		// Erstellt alle Button, um auf dem Bild zu klicken
		AbsoluteLayout absoluteLayout = new AbsoluteLayout();
		bArbeitsplätze = new Button();
		bArbeitsplätze.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		bArbeitsplätze.addClickListener(createClickListener());
		bSektorA = new Button();
		bSektorA.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		bSektorA.addClickListener(createClickListener());
		bSektorB = new Button();
		bSektorB.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		bSektorB.addClickListener(createClickListener());
		bGruppenräume = new Button();
		bGruppenräume.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		bGruppenräume.addClickListener(createClickListener());
		bCarrels = new Button();
		bCarrels.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		bCarrels.addClickListener(createClickListener());

		if (räumeVorhanden == true) {
			bPersonen.setCaption("+1 Person");
		}

		// Setzt das Bild für das jeweilige Stockwerk
		image = null;
		if (stockwerkEnum == StockwerkEnum.EG) {
			image = new Image(null, new ClassResource("/belegung/EG-lang2.png"));
			absoluteLayout.addComponent(image);
			bEG.setStyleName(ValoTheme.BUTTON_PRIMARY);
			absoluteLayout.addComponent(bArbeitsplätze, "top: 40px; left: 50px;");
			bArbeitsplätze.setWidth("650px");
			bArbeitsplätze.setHeight("45px");
			absoluteLayout.addComponent(bGruppenräume, "top: 90px; left: 75px;");
			bGruppenräume.setHeight("100px");
			bGruppenräume.setWidth("90px");

		} else if (stockwerkEnum == StockwerkEnum.ZG1) {
			image = new Image(null, new ClassResource("/belegung/1.ZG-lang2.png"));
			absoluteLayout.addComponent(image);
			b1ZG.setStyleName(ValoTheme.BUTTON_PRIMARY);
			absoluteLayout.addComponent(bArbeitsplätze, "top: 20px; left: 50px;");
			bArbeitsplätze.setHeight("200px");
			bArbeitsplätze.setWidth("650px");

		} else if (stockwerkEnum == StockwerkEnum.ZG2) {
			image = new Image(null, new ClassResource("/belegung/2.ZG-lang2.png"));
			absoluteLayout.addComponent(image);
			b2ZG.setStyleName(ValoTheme.BUTTON_PRIMARY);
			absoluteLayout.addComponent(bArbeitsplätze, "top: 20px; left: 50px;");
			bArbeitsplätze.setHeight("200px");
			bArbeitsplätze.setWidth("650px");

		} else if (stockwerkEnum == StockwerkEnum.LL) {
			image = new Image(null, new ClassResource("/belegung/LL-lang2.png"));
			absoluteLayout.addComponent(image);
			bLL.setStyleName(ValoTheme.BUTTON_PRIMARY);
			absoluteLayout.addComponent(bSektorA, "top: 30px; left: 50px;");
			bSektorA.setHeight("140px");
			bSektorA.setWidth("310px");
			absoluteLayout.addComponent(bSektorB, "top: 30px; left: 380px;");
			bSektorB.setHeight("140px");
			bSektorB.setWidth("380px");
			absoluteLayout.addComponent(bGruppenräume, "top: 115px; left: 235px;");
			bGruppenräume.setHeight("60px");
			bGruppenräume.setWidth("270px");
			absoluteLayout.addComponent(bCarrels, "top: 80px; left: 565px;");
			bCarrels.setHeight("50px");
			bCarrels.setWidth("125px");
		}

		// Füllt die Tabelle mit Zahlen
		layoutMitZahlenFüllen();

		return absoluteLayout;
	}

	/**
	 * Das GridLayout wird mit Zahlen von der Datenbank für die ausgewählte Uhrzeit
	 * gefüllt
	 */
	private void layoutMitZahlenFüllen() {

		tTotalPersonen.setValue("0");
		tTotalRäume.setValue("0");

		// Prüft alle Stockwerke und setzt das jeweilige Layout dafür
		if (stockwerkEnum == StockwerkEnum.EG) {
			if (erfassungsSchritt == 0) {
				for (Stockwerk s : belegung.getStockwerkListe()) {
					if (s.getName() == stockwerkEnum) {
						for (Arbeitsplätze a : s.getArbeitsplatzListe()) {
							if (a.getUhrzeit() == ausgewählteUhrzeit) {
								tTotalPersonen.setValue("" + a.getAnzahlPersonen());
							}
						}
					}
				}
			} else {
				bRäume.setCaption("+1 Gruppenaum");

				for (Stockwerk s : belegung.getStockwerkListe()) {
					if (s.getName() == stockwerkEnum) {
						for (Gruppenräume g : s.getGruppenräumeListe()) {
							if (g.getUhrzeit() == ausgewählteUhrzeit) {
								tTotalPersonen.setValue("" + g.getAnzahlPersonen());
								tTotalRäume.setValue("" + g.getAnzahlRäume());
							}
						}
					}
				}
			}

		} else if (stockwerkEnum == StockwerkEnum.ZG1) {

			for (Stockwerk s : belegung.getStockwerkListe()) {
				if (s.getName() == stockwerkEnum) {
					for (Arbeitsplätze a : s.getArbeitsplatzListe()) {
						if (a.getUhrzeit() == ausgewählteUhrzeit) {
							tTotalPersonen.setValue("" + a.getAnzahlPersonen());
						}
					}
				}
			}

		} else if (stockwerkEnum == StockwerkEnum.ZG2) {

			for (Stockwerk s : belegung.getStockwerkListe()) {
				if (s.getName() == stockwerkEnum) {
					for (Arbeitsplätze a : s.getArbeitsplatzListe()) {
						if (a.getUhrzeit() == ausgewählteUhrzeit) {
							tTotalPersonen.setValue("" + a.getAnzahlPersonen());
						}
					}
				}
			}

		} else if (stockwerkEnum == StockwerkEnum.LL) {

			// Gruppenräume
			if (erfassungsSchritt == 1) {
				bRäume.setCaption("+1 Gruppenraum");

				for (Stockwerk s : belegung.getStockwerkListe()) {
					if (s.getName() == stockwerkEnum) {
						for (Gruppenräume g : s.getGruppenräumeListe()) {
							if (g.getUhrzeit() == ausgewählteUhrzeit) {
								tTotalPersonen.setValue("" + g.getAnzahlPersonen());
								tTotalRäume.setValue("" + g.getAnzahlRäume());
							}
						}
					}
				}
				// Carrels
			} else if (erfassungsSchritt == 2) {
				bRäume.setCaption("+1 Carrel");

				for (Stockwerk s : belegung.getStockwerkListe()) {
					if (s.getName() == stockwerkEnum) {
						for (Carrels g : s.getCarrelsListe()) {
							if (g.getUhrzeit() == ausgewählteUhrzeit) {
								tTotalPersonen.setValue("" + g.getAnzahlPersonen());
								tTotalRäume.setValue("" + g.getAnzahlRäume());
							}
						}
					}
				}
				// SektorA
			} else if (erfassungsSchritt == 3) {
				bPersonen.setCaption("+1 Sektor A");

				for (Stockwerk s : belegung.getStockwerkListe()) {
					if (s.getName() == stockwerkEnum) {
						for (SektorA a : s.getSektorAListe()) {
							if (a.getUhrzeit() == ausgewählteUhrzeit) {
								tTotalPersonen.setValue("" + a.getAnzahlPersonen());
							}
						}
					}
				}

				// SektorB
			} else if (erfassungsSchritt == 4) {
				bPersonen.setCaption("+1 Sektor B");

				for (Stockwerk s : belegung.getStockwerkListe()) {
					if (s.getName() == stockwerkEnum) {
						for (SektorB a : s.getSektorBListe()) {
							if (a.getUhrzeit() == ausgewählteUhrzeit) {
								tTotalPersonen.setValue("" + a.getAnzahlPersonen());
							}
						}
					}
				}
			}

		}
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
		// Welchen Erfassungsschritt gibt es
		String erfassungsSchrittString = args[3];
		// Welche Uhrzeit wurde im Dropdown ausgewählt
		String ausgewählteUhrzeitString = args[4];

		if (datumString.equals(" ")) {
			this.date = new Date();
		} else {
			this.date = new Date(Long.parseLong(datumString));
		}

		if (stockwerkString.equals("LL")) {
			this.stockwerkEnum = StockwerkEnum.LL;
		} else if (stockwerkString.equals("EG")) {
			this.stockwerkEnum = StockwerkEnum.EG;
		} else if (stockwerkString.equals("ZG1")) {
			this.stockwerkEnum = StockwerkEnum.ZG1;
		} else if (stockwerkString.equals("ZG2")) {
			this.stockwerkEnum = StockwerkEnum.ZG2;
		}

		if (stockwerkEnum == StockwerkEnum.LL) {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_LL);
		} else {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_BB);
		}

		this.korrektur = Boolean.parseBoolean(korrekturString);
		this.erfassungsSchritt = Integer.parseInt(erfassungsSchrittString);

		if (erfassungsSchritt == 0 || erfassungsSchritt == 3 || erfassungsSchritt == 4) {
			räumeVorhanden = false;
		} else {
			// Bei Schritt 1 und 2 sind Gruppenräume und Carrels
			räumeVorhanden = true;
		}

		switch (ausgewählteUhrzeitString) {
		case "NEUN":
			this.ausgewählteUhrzeit = UhrzeitEnum.NEUN;
			break;
		case "ELF":
			this.ausgewählteUhrzeit = UhrzeitEnum.ELF;
			break;
		case "DREIZEHN":
			this.ausgewählteUhrzeit = UhrzeitEnum.DREIZEHN;
			break;
		case "FÜNFZEHN":
			this.ausgewählteUhrzeit = UhrzeitEnum.FÜNFZEHN;
			break;
		case "SIEBZEHN":
			this.ausgewählteUhrzeit = UhrzeitEnum.SIEBZEHN;
			break;
		case "NEUNZEHN":
			this.ausgewählteUhrzeit = UhrzeitEnum.NEUNZEHN;
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
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + " " + '/'
								+ stockwerkEnum.toString() + '/' + false + '/' + 0 + '/' + " ");
					} else {
						Page.getCurrent().setUriFragment("!" + StartseiteView.NAME);
					}
				}

				if (e.getSource() == bPersonen) {
					erhöheOderVermindereTextfieldNachNummer(tTotalPersonen, 1);
				}

				if (e.getSource() == bPersonen5) {
					erhöheOderVermindereTextfieldNachNummer(tTotalPersonen, 5);
				}

				if (e.getSource() == bPersonen10) {
					erhöheOderVermindereTextfieldNachNummer(tTotalPersonen, 10);
				}

				if (e.getSource() == bPersonenMinus) {
					erhöheOderVermindereTextfieldNachNummer(tTotalPersonen, -1);
				}

				if (e.getSource() == bRäume) {
					erhöheOderVermindereTextfieldNachNummer(tTotalRäume, 1);
				}

				if (e.getSource() == bRäumeMinus) {
					erhöheOderVermindereTextfieldNachNummer(tTotalRäume, -1);
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
					boolean sektorAIsEmpty = true;
					boolean sektorBIsEmpty = true;
					boolean gruppenraumIsEmpty = true;
					boolean carellIsEmpty = true;

					for (Arbeitsplätze arbeitsplätze : stockwerk.getArbeitsplatzListe()) {
						if (uhrzeitEnum == arbeitsplätze.getUhrzeit()) {
							if (arbeitsplätze.getAnzahlPersonen() != 0) {
								arbeitsplatzIsEmpty = false;
							}
						}
					}

					for (SektorA sektorA : stockwerk.getSektorAListe()) {
						if (uhrzeitEnum == sektorA.getUhrzeit()) {
							if (sektorA.getAnzahlPersonen() != 0) {
								sektorAIsEmpty = false;
							}
						}
					}

					for (SektorB sektorB : stockwerk.getSektorBListe()) {
						if (uhrzeitEnum == sektorB.getUhrzeit()) {
							if (sektorB.getAnzahlPersonen() != 0) {
								sektorBIsEmpty = false;
							}
						}
					}

					for (Gruppenräume gruppenräume : stockwerk.getGruppenräumeListe()) {
						if (uhrzeitEnum == gruppenräume.getUhrzeit()) {
							if (gruppenräume.getAnzahlRäume() != 0 && gruppenräume.getAnzahlPersonen() != 0) {
								gruppenraumIsEmpty = false;
							}
						}
					}

					for (Carrels carrels : stockwerk.getCarrelsListe()) {
						if (uhrzeitEnum == carrels.getUhrzeit()) {
							if (carrels.getAnzahlRäume() != 0 && carrels.getAnzahlPersonen() != 0) {
								carellIsEmpty = false;
							}
						}
					}

					String validierung = "";
					if (stockwerkEnum == StockwerkEnum.LL) {
						if (sektorAIsEmpty == true || sektorBIsEmpty == true || gruppenraumIsEmpty == true
								|| carellIsEmpty == true) {
							if (sektorAIsEmpty == true)
								validierung += "SektorA, ";
							if (sektorBIsEmpty == true)
								validierung += "SektorB, ";
							if (gruppenraumIsEmpty == true)
								validierung += "Gruppenräume, ";
							if (carellIsEmpty == true)
								validierung += "Carells, ";
							validierung += "hat einen 0 wert";
						}
					} else if (stockwerkEnum == StockwerkEnum.ZG1) {
						if (arbeitsplatzIsEmpty == true) {
							if (arbeitsplatzIsEmpty == true)
								validierung += "Arbeitsplatz, ";
							validierung += "hat einen 0 wert";
						}
					} else if (stockwerkEnum == StockwerkEnum.ZG2) {
						if (arbeitsplatzIsEmpty == true) {
							if (arbeitsplatzIsEmpty == true)
								validierung += "Arbeitsplatz, ";
							validierung += "hat einen 0 wert";
						}
					} else if (stockwerkEnum == StockwerkEnum.EG) {
						if (arbeitsplatzIsEmpty == true || gruppenraumIsEmpty == true) {
							if (arbeitsplatzIsEmpty == true)
								validierung += "Arbeitsplatz, ";
							if (gruppenraumIsEmpty == true)
								validierung += "Gruppenräume, ";
							validierung += "hat einen 0 wert";
						}
					}

					if (validierung.equals("")) {
						validierung = "Validierung erfolgreich, keine Fehler";
					}

					Notification.show(validierung, Type.WARNING_MESSAGE);
				}

				if (e.getSource() == bSpeichern) {
					// Speichert den Wert in der Datenbank

					try {
						int anzahlPersonen = Integer.parseInt(tTotalPersonen.getValue());
						int anzahlRäume = 0;
						if (räumeVorhanden == true) {
							anzahlRäume = Integer.parseInt(tTotalRäume.getValue());
						}

						if (anzahlPersonen == 0) {
							Notification.show("Sie haben eine Zählung mit 0 gespeichert", Type.WARNING_MESSAGE);
						}

						boolean eintragVorhanden = false;

						if (stockwerkEnum == StockwerkEnum.EG) {
							if (erfassungsSchritt == 0) {
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
							} else {
								for (Stockwerk s : belegung.getStockwerkListe()) {
									if (s.getName() == stockwerkEnum) {

										// Bei einem zweiten Aufruf müssen die Gruppenräume geupdatet werden
										for (Gruppenräume g : s.getGruppenräumeListe()) {
											if (g.getUhrzeit() == ausgewählteUhrzeit) {
												eintragVorhanden = true;
												g.setAnzahlPersonen(anzahlPersonen);
												g.setAnzahlRäume(anzahlRäume);
											}
										}

										// Falls es keine Gruppenräume für die ausgewählte Uhrzeit gibt
										if (eintragVorhanden == false) {
											Gruppenräume gruppenräume = new Gruppenräume(anzahlPersonen, anzahlRäume,
													ausgewählteUhrzeit, s);
											s.addGruppenräume(gruppenräume);
										}
									}
								}
							}

						} else if (stockwerkEnum == StockwerkEnum.ZG1) {
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
						} else if (stockwerkEnum == StockwerkEnum.ZG2) {
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
						} else if (stockwerkEnum == StockwerkEnum.LL) {
							if (erfassungsSchritt == 1) {
								for (Stockwerk s : belegung.getStockwerkListe()) {
									if (s.getName() == stockwerkEnum) {
										// Bei einem zweiten Aufruf müssen die Gruppenräume geupdatet werden
										for (Gruppenräume g : s.getGruppenräumeListe()) {
											if (g.getUhrzeit() == ausgewählteUhrzeit) {
												eintragVorhanden = true;
												g.setAnzahlPersonen(anzahlPersonen);
												g.setAnzahlRäume(anzahlRäume);
											}
										}

										// Falls es keine Gruppenräume für die ausgewählte Uhrzeit gibt
										if (eintragVorhanden == false) {
											Gruppenräume gruppenräume = new Gruppenräume(anzahlPersonen, anzahlRäume,
													ausgewählteUhrzeit, s);
											s.addGruppenräume(gruppenräume);
										}
									}
								}
							} else if (erfassungsSchritt == 2) {
								for (Stockwerk s : belegung.getStockwerkListe()) {
									if (s.getName() == stockwerkEnum) {
										// Bei einem zweiten Aufruf müssen die Carrels geupdatet werden
										for (Carrels g : s.getCarrelsListe()) {
											if (g.getUhrzeit() == ausgewählteUhrzeit) {
												eintragVorhanden = true;
												g.setAnzahlPersonen(anzahlPersonen);
												g.setAnzahlRäume(anzahlRäume);
											}
										}

										// Falls es keine Carrels für die ausgewählte Uhrzeit gibt
										if (eintragVorhanden == false) {
											Carrels carrels = new Carrels(anzahlPersonen, anzahlRäume,
													ausgewählteUhrzeit, s);
											s.addCarrels(carrels);
										}
									}
								}
							} else if (erfassungsSchritt == 3) {
								for (Stockwerk s : belegung.getStockwerkListe()) {
									if (s.getName() == stockwerkEnum) {
										// Bei einem zweiten Aufruf muss der SektorA geupdatet werden
										for (SektorA a : s.getSektorAListe()) {
											if (a.getUhrzeit() == ausgewählteUhrzeit) {
												eintragVorhanden = true;
												a.setAnzahlPersonen(anzahlPersonen);
											}
										}

										// Falls es keinen SektorA für die ausgewählte Uhrzeit gibt
										if (eintragVorhanden == false) {
											SektorA sektorA = new SektorA(anzahlPersonen, ausgewählteUhrzeit, s);
											s.addSektorA(sektorA);
										}
									}
								}
							} else if (erfassungsSchritt == 4) {
								for (Stockwerk s : belegung.getStockwerkListe()) {
									if (s.getName() == stockwerkEnum) {
										// Bei einem zweiten Aufruf muss der SektorB geupdatet werden
										for (SektorB a : s.getSektorBListe()) {
											if (a.getUhrzeit() == ausgewählteUhrzeit) {
												eintragVorhanden = true;
												a.setAnzahlPersonen(anzahlPersonen);
											}
										}

										// Falls es keinen SektorB für die ausgewählte Uhrzeit gibt
										if (eintragVorhanden == false) {
											SektorB sektorB = new SektorB(anzahlPersonen, ausgewählteUhrzeit, s);
											s.addSektorB(sektorB);
										}
									}
								}
							}
						}

						// Update der Belegung
						belegungDB.updateBelegung(belegung);
						fülleTabelleUhrzeiten(tabelleUhrzeiten);

						Notification.show("Zählung gespeichert", Type.TRAY_NOTIFICATION);

					} catch (NumberFormatException e1) {
						// Not an integer
						Notification.show("Die Eingabe muss eine Zahl sein", Type.WARNING_MESSAGE);
					}
				}

				if (e.getSource() == bTagesübersicht) {
					getUI().getNavigator().navigateTo(TagesübersichtBelegungViewWinti.NAME + '/' + date.getTime() + '/'
							+ stockwerkEnum.toString());
				}

				if (e.getSource() == bKorrektur) {
					if (korrektur == false) {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
								+ stockwerkEnum.toString() + '/' + true + '/' + 0 + '/' + " ");
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + " " + '/'
								+ stockwerkEnum.toString() + '/' + false + '/' + 0 + '/' + " ");
					}
				}

				if (e.getSource() == bLL) {
					if (korrektur == true && ausgewählteUhrzeit != null) {
						getUI().getNavigator()
								.navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
										+ StockwerkEnum.LL.toString() + '/' + korrektur + '/' + 1 + '/'
										+ ausgewählteUhrzeit.toString());
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
								+ StockwerkEnum.LL.toString() + '/' + korrektur + '/' + 1 + '/' + " ");
					}
				}

				if (e.getSource() == b2ZG) {
					if (korrektur == true && ausgewählteUhrzeit != null) {
						getUI().getNavigator()
								.navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
										+ StockwerkEnum.ZG2.toString() + '/' + korrektur + '/' + 0 + '/'
										+ ausgewählteUhrzeit.toString());
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
								+ StockwerkEnum.ZG2.toString() + '/' + korrektur + '/' + 0 + '/' + " ");
					}
				}

				if (e.getSource() == b1ZG) {
					if (korrektur == true && ausgewählteUhrzeit != null) {
						getUI().getNavigator()
								.navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
										+ StockwerkEnum.ZG1.toString() + '/' + korrektur + '/' + 0 + '/'
										+ ausgewählteUhrzeit.toString());
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
								+ StockwerkEnum.ZG1.toString() + '/' + korrektur + '/' + 0 + '/' + " ");
					}
				}

				if (e.getSource() == bEG) {
					if (korrektur == true && ausgewählteUhrzeit != null) {
						getUI().getNavigator()
								.navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
										+ StockwerkEnum.EG.toString() + '/' + korrektur + '/' + 0 + '/'
										+ ausgewählteUhrzeit.toString());
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
								+ StockwerkEnum.EG.toString() + '/' + korrektur + '/' + 0 + '/' + " ");
					}
				}

				if (e.getSource() == bArbeitsplätze) {
					if (korrektur == true && ausgewählteUhrzeit != null) {
						getUI().getNavigator()
								.navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
										+ stockwerkEnum.toString() + '/' + korrektur + '/' + 0 + '/'
										+ ausgewählteUhrzeit.toString());
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
								+ stockwerkEnum.toString() + '/' + korrektur + '/' + 0 + '/' + " ");
					}
				}

				if (e.getSource() == bGruppenräume) {
					if (korrektur == true && ausgewählteUhrzeit != null) {
						getUI().getNavigator()
								.navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
										+ stockwerkEnum.toString() + '/' + korrektur + '/' + 1 + '/'
										+ ausgewählteUhrzeit.toString());
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
								+ stockwerkEnum.toString() + '/' + korrektur + '/' + 1 + '/' + " ");
					}
				}

				if (e.getSource() == bCarrels) {
					if (korrektur == true && ausgewählteUhrzeit != null) {
						getUI().getNavigator()
								.navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
										+ stockwerkEnum.toString() + '/' + korrektur + '/' + 2 + '/'
										+ ausgewählteUhrzeit.toString());
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
								+ stockwerkEnum.toString() + '/' + korrektur + '/' + 2 + '/' + " ");
					}
				}

				if (e.getSource() == bSektorA) {
					if (korrektur == true && ausgewählteUhrzeit != null) {
						getUI().getNavigator()
								.navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
										+ stockwerkEnum.toString() + '/' + korrektur + '/' + 3 + '/'
										+ ausgewählteUhrzeit.toString());
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
								+ stockwerkEnum.toString() + '/' + korrektur + '/' + 3 + '/' + " ");
					}
				}

				if (e.getSource() == bSektorB) {
					if (korrektur == true && ausgewählteUhrzeit != null) {
						getUI().getNavigator()
								.navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
										+ stockwerkEnum.toString() + '/' + korrektur + '/' + 4 + '/'
										+ ausgewählteUhrzeit.toString());
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
								+ stockwerkEnum.toString() + '/' + korrektur + '/' + 4 + '/' + " ");
					}
				}

			}

			/**
			 * Erhöht oder vermindert das Textfield nach einer bestimmten Nummer
			 * 
			 * @param textfield
			 * @param i
			 */
			private void erhöheOderVermindereTextfieldNachNummer(TextField textfield, int i) {
				try {
					int anzahl = Integer.parseInt(textfield.getValue());
					anzahl = anzahl + i;

					if (anzahl >= 0) {
						// Textfelder erhöhen
						textfield.setValue("" + anzahl);
					} else {
						Notification.show("Die Eingabe darf keine Minuszahl sein", Type.WARNING_MESSAGE);
					}
				} catch (NumberFormatException e1) {
					// Not an integer
					Notification.show("Die Eingabe muss eine Zahl sein", Type.WARNING_MESSAGE);
				}
			}

		};

	}

}