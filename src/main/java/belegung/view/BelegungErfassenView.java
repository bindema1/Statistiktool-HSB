package belegung.view;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.ClassResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import allgemein.view.MainView;
import allgemein.view.StartseiteView;
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
@SuppressWarnings("serial")
@Theme("mytheme")
public class BelegungErfassenView implements View {

	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bTagesübersicht;
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

	public BelegungErfassenView(Date date, StockwerkEnum stockwerkenum, boolean korrektur, int erfassungsSchritt) {

		this.date = date;

		if (stockwerkenum == StockwerkEnum.LL) {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_LL);
		} else {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_BB);
		}

		this.stockwerkEnum = stockwerkenum;
		this.korrektur = korrektur;
		this.erfassungsSchritt = erfassungsSchritt;

		if (erfassungsSchritt == 0 || erfassungsSchritt == 3 || erfassungsSchritt == 4) {
			räumeVorhanden = false;
		} else {
			// Bei Schritt 1 und 2 sind Gruppenräume und Carrels
			räumeVorhanden = true;
		}
	}

	private void initData() {
		date = belegung.getDatum();
	}

	// Initialisieren der GUI Komponente
	private void initComponents(MainView mainView) {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener(mainView));

		Label lText = new Label();
		if (korrektur == true) {
			lText.setValue("Belegung erfassen vom ");
		} else {
			lText.setValue("Belegung erfassen vom " + new SimpleDateFormat("dd.MM.yyyy").format(belegung.getDatum()));
		}
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bTagesübersicht = new Button();
		bTagesübersicht.setCaption("Tagesübersicht");
		bTagesübersicht.addStyleName(ValoTheme.BUTTON_LARGE);
		bTagesübersicht.addClickListener(createClickListener(mainView));

		bPersonen = new Button();
		bPersonen.setCaption("Arbeitsplätze");
		bPersonen.addStyleName(ValoTheme.BUTTON_LARGE);
		bPersonen.addClickListener(createClickListener(mainView));

		bPersonen10 = new Button();
		bPersonen10.setCaption("+ 10");
		bPersonen10.addStyleName(ValoTheme.BUTTON_LARGE);
		bPersonen10.addClickListener(createClickListener(mainView));

		bPersonen5 = new Button();
		bPersonen5.setCaption("+ 5");
		bPersonen5.addStyleName(ValoTheme.BUTTON_LARGE);
		bPersonen5.addClickListener(createClickListener(mainView));

		bPersonenMinus = new Button();
		bPersonenMinus.setCaption("-1 Korrektur");
		bPersonenMinus.addStyleName(ValoTheme.BUTTON_DANGER);
		bPersonenMinus.addClickListener(createClickListener(mainView));

		bRäume = new Button();
		bRäume.setCaption("Räume");
		bRäume.addStyleName(ValoTheme.BUTTON_LARGE);
		bRäume.addClickListener(createClickListener(mainView));

		bRäumeMinus = new Button();
		bRäumeMinus.setCaption("-1 Korrektur");
		bRäumeMinus.addStyleName(ValoTheme.BUTTON_DANGER);
		bRäumeMinus.addClickListener(createClickListener(mainView));

		bSpeichern = new Button();
		bSpeichern.addStyleName(ValoTheme.BUTTON_LARGE);
		bSpeichern.setCaption("Speichern");
		bSpeichern.addClickListener(createClickListener(mainView));

		tTotalPersonen = new TextField();
		tTotalPersonen.setPlaceholder("Total Personen");
		tTotalPersonen.setValue("0");
		tTotalRäume = new TextField();
		tTotalRäume.setPlaceholder("Total Räume");
		tTotalRäume.setValue("0");

		data = null;
		ListSelect<String> uhrzeitListSelect;
		if (korrektur == true) {
			data = Arrays.asList(new String[] { "Wählen", "9 Uhr", "11 Uhr", "13 Uhr", "15 Uhr", "17 Uhr", "19 Uhr" });
			uhrzeitListSelect = new ListSelect<>("Uhrzeit:", data);
			uhrzeitListSelect.setRows(7);
			uhrzeitListSelect.select(data.get(0));
			setButtonEnabled(false);
		} else {
			data = Arrays.asList(new String[] { "9 Uhr", "11 Uhr", "13 Uhr", "15 Uhr", "17 Uhr", "19 Uhr" });
			uhrzeitListSelect = new ListSelect<>("Uhrzeit:", data);
			uhrzeitListSelect.setRows(6);

			int time = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
			if (time <= 9) {
				uhrzeitListSelect.select(data.get(0));
				ausgewählteUhrzeit = UhrzeitEnum.NEUN;
			} else if (time <= 11) {
				uhrzeitListSelect.select(data.get(1));
				ausgewählteUhrzeit = UhrzeitEnum.ELF;
			} else if (time <= 13) {
				uhrzeitListSelect.select(data.get(2));
				ausgewählteUhrzeit = UhrzeitEnum.DREIZEHN;
			} else if (time <= 15) {
				uhrzeitListSelect.select(data.get(3));
				ausgewählteUhrzeit = UhrzeitEnum.FÜNFZEHN;
			} else if (time <= 17) {
				uhrzeitListSelect.select(data.get(4));
				ausgewählteUhrzeit = UhrzeitEnum.SIEBZEHN;
			} else if (time >= 18) {
				uhrzeitListSelect.select(data.get(5));
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

			switch (String.valueOf(event.getValue())) {
			case "[9 Uhr]":
				ausgewählteUhrzeit = UhrzeitEnum.NEUN;
				break;
			case "[11 Uhr]":
				ausgewählteUhrzeit = UhrzeitEnum.ELF;
				break;
			case "[13 Uhr]":
				ausgewählteUhrzeit = UhrzeitEnum.DREIZEHN;
				break;
			case "[15 Uhr]":
				ausgewählteUhrzeit = UhrzeitEnum.FÜNFZEHN;
				break;
			case "[17 Uhr]":
				ausgewählteUhrzeit = UhrzeitEnum.SIEBZEHN;
				break;
			case "[19 Uhr]":
				ausgewählteUhrzeit = UhrzeitEnum.NEUNZEHN;
				break;
			case "[Wählen]":
				setButtonEnabled(false);
				break;
			}

			// Das GridLayout mit Zahlen füllen
			//alleButtonRotSetzenOhneZahlen();
			layoutMitZahlenFüllen();
		});

		DateTimeField datefield = new DateTimeField();
		datefield.setValue(
				Instant.ofEpochMilli(belegung.getDatum().getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
		datefield.setDateFormat("dd.MM.yyyy");
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			ZonedDateTime zdt = event.getValue().atZone(ZoneId.systemDefault());
			date = Date.from(zdt.toInstant());

			if (stockwerkEnum == StockwerkEnum.LL) {
				belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_LL);
			} else if (stockwerkEnum == StockwerkEnum.WÄDI) {
				belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WÄDENSWIL);
			} else {
				belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_BB);
			}

			// Alle Button disabled setzen, da der User erst eine Uhrzeit auswählen muss
			uhrzeitListSelect.select(data.get(0));
			setButtonEnabled(false);
		});

		// Unteres Grid
		bLL = new Button();
		bLL.setCaption("LL");
		bLL.addStyleName(ValoTheme.BUTTON_LARGE);
		bLL.addClickListener(createClickListener(mainView));
		bEG = new Button();
		bEG.setCaption("EG");
		bEG.addStyleName(ValoTheme.BUTTON_LARGE);
		bEG.addClickListener(createClickListener(mainView));
		b1ZG = new Button();
		b1ZG.setCaption("1.ZG");
		b1ZG.addStyleName(ValoTheme.BUTTON_LARGE);
		b1ZG.addClickListener(createClickListener(mainView));
		b2ZG = new Button();
		b2ZG.setCaption("2.ZG");
		b2ZG.addStyleName(ValoTheme.BUTTON_LARGE);
		b2ZG.addClickListener(createClickListener(mainView));

		GridLayout grid = new GridLayout(5, 11);
		grid.addStyleName("gridlayout");
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0);
		if (korrektur == true) {
			grid.addComponent(lText, 1, 0);
			grid.addComponent(datefield, 2, 0, 3, 0);
		} else {
			grid.addComponent(lText, 1, 0, 3, 0);
		}
		grid.addComponent(bTagesübersicht, 4, 0);
		grid.addComponent(uhrzeitListSelect, 0, 1, 0, 5);

		if (räumeVorhanden == true) {
			grid.addComponent(bPersonen, 1, 1, 2, 4);
			grid.addComponent(tTotalPersonen, 1, 5);
			grid.addComponent(bPersonenMinus, 2, 5);
			grid.addComponent(bRäume, 3, 1, 4, 3);
			grid.addComponent(tTotalRäume, 3, 4);
			grid.addComponent(bRäumeMinus, 4, 4);
			grid.addComponent(bSpeichern, 3, 5, 4, 5);
		} else {
			grid.addComponent(bPersonen, 1, 1, 3, 4);
			grid.addComponent(tTotalPersonen, 1, 5, 3, 5);
			grid.addComponent(bPersonen10, 4, 1);
			grid.addComponent(bPersonen5, 4, 2);
			grid.addComponent(bPersonenMinus, 4, 3);
			grid.addComponent(bSpeichern, 4, 4, 4, 5);
		}
		grid.addComponent(bLL, 0, 6);
		grid.addComponent(b2ZG, 0, 7);
		grid.addComponent(b1ZG, 0, 8);
		grid.addComponent(bEG, 0, 9);
		grid.addComponent(new Label(), 0, 10);
		grid.addComponent(createAbsoluteLayoutForImage(mainView), 1, 6, 4, 10);
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
						if (row == 5 && col == 1 || row == 5 && col == 2 || row == 4 && col == 3
								|| row == 4 && col == 4) {
							c.setHeight("90%");
							c.setWidth("80%");
						} else {
							c.setHeight("90%");
							c.setWidth("90%");
						}
					} else {
						if (row == 5 && col == 1 || row == 5 && col == 2 || row == 5 && col == 3) {
							c.setHeight("90%");
							// c.setWidth("80%");
						} else {
							c.setHeight("90%");
							c.setWidth("90%");
						}
					}

					if (row >= 6) {
						if (col == 0) {
							c.setHeight("100%");
							c.setWidth("50%");
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
	 * Erstellt ein AbsoluteLayout, welches das Bild als hintergrund hat sowie
	 * Button darauf, welche per Pixel angeordnet sind
	 * 
	 * @return Component
	 */
	private Component createAbsoluteLayoutForImage(MainView mainView) {

		AbsoluteLayout absoluteLayout = new AbsoluteLayout();
		bArbeitsplätze = new Button();
		bArbeitsplätze.setStyleName(ValoTheme.BUTTON_LINK);
		bArbeitsplätze.addClickListener(createClickListener(mainView));
		bSektorA = new Button();
		bSektorA.setStyleName(ValoTheme.BUTTON_LINK);
		bSektorA.addClickListener(createClickListener(mainView));
		bSektorB = new Button();
		bSektorB.setStyleName(ValoTheme.BUTTON_LINK);
		bSektorB.addClickListener(createClickListener(mainView));
		bGruppenräume = new Button();
		bGruppenräume.setStyleName(ValoTheme.BUTTON_LINK);
		bGruppenräume.addClickListener(createClickListener(mainView));
		bCarrels = new Button();
		bCarrels.setStyleName(ValoTheme.BUTTON_LINK);
		bCarrels.addClickListener(createClickListener(mainView));

		if(korrektur == false) {
			//alleButtonRotSetzenOhneZahlen();
		}
		
		if (räumeVorhanden == true) {
			bPersonen.setCaption("Personen");
		}

		image = null;
		if (stockwerkEnum == StockwerkEnum.EG) {
			image = new Image(null, new ClassResource("/belegung/EG-lang.png"));
			absoluteLayout.addComponent(image);
			bEG.setStyleName(ValoTheme.BUTTON_PRIMARY);
			absoluteLayout.addComponent(bArbeitsplätze, "top: 50px; left: 50px;");
			bArbeitsplätze.setWidth("900px");
			bArbeitsplätze.setHeight("65px");
			absoluteLayout.addComponent(bGruppenräume, "top: 120px; left: 90px;");
			bGruppenräume.setHeight("110px");
			bGruppenräume.setWidth("130px");

		} else if (stockwerkEnum == StockwerkEnum.ZG1) {
			image = new Image(null, new ClassResource("/belegung/1.ZG-lang.png"));
			absoluteLayout.addComponent(image);
			b1ZG.setStyleName(ValoTheme.BUTTON_PRIMARY);
			absoluteLayout.addComponent(bArbeitsplätze, "top: 20px; left: 50px;");
			bArbeitsplätze.setHeight("230px");
			bArbeitsplätze.setWidth("900px");

		} else if (stockwerkEnum == StockwerkEnum.ZG2) {
			image = new Image(null, new ClassResource("/belegung/2.ZG-lang.png"));
			absoluteLayout.addComponent(image);
			b2ZG.setStyleName(ValoTheme.BUTTON_PRIMARY);
			absoluteLayout.addComponent(bArbeitsplätze, "top: 20px; left: 50px;");
			bArbeitsplätze.setHeight("230px");
			bArbeitsplätze.setWidth("900px");

		} else if (stockwerkEnum == StockwerkEnum.LL) {
			image = new Image(null, new ClassResource("/belegung/LL-lang.png"));
			absoluteLayout.addComponent(image);
			bLL.setStyleName(ValoTheme.BUTTON_PRIMARY);
			absoluteLayout.addComponent(bSektorA, "top: 40px; left: 50px;");
			bSektorA.setHeight("200px");
			bSektorA.setWidth("430px");
			absoluteLayout.addComponent(bSektorB, "top: 40px; left: 520px;");
			bSektorB.setHeight("200px");
			bSektorB.setWidth("430px");
			absoluteLayout.addComponent(bGruppenräume, "top: 155px; left: 320px;");
			bGruppenräume.setHeight("60px");
			bGruppenräume.setWidth("350px");
			absoluteLayout.addComponent(bCarrels, "top: 110px; left: 760px;");
			bCarrels.setHeight("60px");
			bCarrels.setWidth("170px");
		}

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

		if (stockwerkEnum == StockwerkEnum.EG) {
			if (erfassungsSchritt == 0) {
//				bArbeitsplätze.setStyleName(ValoTheme.BUTTON_PRIMARY);

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
//				bGruppenräume.setStyleName(ValoTheme.BUTTON_PRIMARY);

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

//				bGruppenräume.setStyleName(ValoTheme.BUTTON_PRIMARY);
				// Carrels
			} else if (erfassungsSchritt == 2) {

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
//				bCarrels.setStyleName(ValoTheme.BUTTON_PRIMARY);
				// SektorA
			} else if (erfassungsSchritt == 3) {
				bPersonen.setCaption("Sektor A");

				for (Stockwerk s : belegung.getStockwerkListe()) {
					if (s.getName() == stockwerkEnum) {
						for (SektorA a : s.getSektorAListe()) {
							if (a.getUhrzeit() == ausgewählteUhrzeit) {
								tTotalPersonen.setValue("" + a.getAnzahlPersonen());
							}
						}
					}
				}

//				bSektorA.setStyleName(ValoTheme.BUTTON_PRIMARY);
				// SektorB
			} else if (erfassungsSchritt == 4) {
				bPersonen.setCaption("Sektor B");

				for (Stockwerk s : belegung.getStockwerkListe()) {
					if (s.getName() == stockwerkEnum) {
						for (SektorB a : s.getSektorBListe()) {
							if (a.getUhrzeit() == ausgewählteUhrzeit) {
								tTotalPersonen.setValue("" + a.getAnzahlPersonen());
							}
						}
					}
				}

//				bSektorB.setStyleName(ValoTheme.BUTTON_PRIMARY);
			}

		}
	}

	private void alleButtonRotSetzenOhneZahlen() {
		// Alle Button Rot setzen, welche Daten 0 sind
		bArbeitsplätze.setStyleName(ValoTheme.BUTTON_DANGER);
		bGruppenräume.setStyleName(ValoTheme.BUTTON_DANGER);
		bCarrels.setStyleName(ValoTheme.BUTTON_DANGER);
		bSektorA.setStyleName(ValoTheme.BUTTON_DANGER);
		bSektorB.setStyleName(ValoTheme.BUTTON_DANGER);
		
		for (Stockwerk s : belegung.getStockwerkListe()) {
			if (s.getName() == stockwerkEnum) {
				for (Arbeitsplätze a : s.getArbeitsplatzListe()) {
					if (a.getUhrzeit() == ausgewählteUhrzeit) {
						if (a.getAnzahlPersonen() >= 0) {
							bArbeitsplätze.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						}
					}
				}
				for (Gruppenräume a : s.getGruppenräumeListe()) {
					if (a.getUhrzeit() == ausgewählteUhrzeit || a.getAnzahlRäume() == 0) {
						if (a.getAnzahlPersonen() >= 0) {
							bGruppenräume.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						}
					}
				}
				for (Carrels a : s.getCarrelsListe()) {
					if (a.getUhrzeit() == ausgewählteUhrzeit) {
						if (a.getAnzahlPersonen() >= 0 || a.getAnzahlRäume() == 0) {
							bCarrels.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						}
					}
				}
				for (SektorA a : s.getSektorAListe()) {
					if (a.getUhrzeit() == ausgewählteUhrzeit) {
						if (a.getAnzahlPersonen() >= 0) {
							bSektorA.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						}
					}
				}
				for (SektorB a : s.getSektorBListe()) {
					if (a.getUhrzeit() == ausgewählteUhrzeit) {
						if (a.getAnzahlPersonen() >= 0) {
							bSektorB.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						}
					}
				}
			}
		}
	}

	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					if (korrektur == true) {
						mainView.setContent(new TagesübersichtBelegungView(date, stockwerkEnum).init(mainView));
					} else {
						mainView.setContent(new StartseiteView().init(mainView));
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

				if (e.getSource() == bSpeichern) {

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
											Carrels carrels = new Carrels(anzahlPersonen, anzahlRäume, ausgewählteUhrzeit,
													s);
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

						belegungDB.updateBelegung(belegung);

						Notification.show("Zählung gespeichert", Type.TRAY_NOTIFICATION);

					} catch (NumberFormatException e1) {
						// Not an integer
						Notification.show("Die Eingabe muss eine Zahl sein", Type.WARNING_MESSAGE);
					}
				}

				if (e.getSource() == bTagesübersicht) {
					mainView.setContent(new TagesübersichtBelegungView(date, stockwerkEnum).init(mainView));
				}

				if (e.getSource() == bLL) {
					mainView.setContent(new BelegungErfassenView(date, StockwerkEnum.LL, korrektur, 1).init(mainView));
				}

				if (e.getSource() == b2ZG) {
					mainView.setContent(new BelegungErfassenView(date, StockwerkEnum.ZG2, korrektur, 0).init(mainView));
				}

				if (e.getSource() == b1ZG) {
					mainView.setContent(new BelegungErfassenView(date, StockwerkEnum.ZG1, korrektur, 0).init(mainView));
				}

				if (e.getSource() == bEG) {
					mainView.setContent(new BelegungErfassenView(date, StockwerkEnum.EG, korrektur, 0).init(mainView));
				}

				if (e.getSource() == bArbeitsplätze) {
					mainView.setContent(new BelegungErfassenView(date, stockwerkEnum, korrektur, 0).init(mainView));
				}

				if (e.getSource() == bGruppenräume) {
					mainView.setContent(new BelegungErfassenView(date, stockwerkEnum, korrektur, 1).init(mainView));
				}

				if (e.getSource() == bCarrels) {
					mainView.setContent(new BelegungErfassenView(date, stockwerkEnum, korrektur, 2).init(mainView));
				}

				if (e.getSource() == bSektorA) {
					mainView.setContent(new BelegungErfassenView(date, stockwerkEnum, korrektur, 3).init(mainView));
				}

				if (e.getSource() == bSektorB) {
					mainView.setContent(new BelegungErfassenView(date, stockwerkEnum, korrektur, 4).init(mainView));
				}

			}

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
