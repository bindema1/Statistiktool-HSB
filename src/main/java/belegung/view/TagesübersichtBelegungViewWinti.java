package belegung.view;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ClassResource;
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
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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
 * View der Tagesübersicht. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class TagesübersichtBelegungViewWinti extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Belegung-Winterthur-Übersicht";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bErfassung;
	private Button bKorrektur;
	private Button bLL;
	private Button b1ZG;
	private Button b2ZG;
	private Button bEG;
	private Belegung belegung;
	private StockwerkEnum stockwerkEnum;
	private Date date;
	private BelegungsDatenbank belegungDB = new BelegungsDatenbank();
	private Image image;
	private GridLayout grid;
	private boolean vonStartseite;

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
		initComponents();

		return absolutLayout;
	}

	public TagesübersichtBelegungViewWinti() {

	}

	/**
	 * Initialisieren der GUI Komponente. Fügt alle Komponenten dem Layout hinzu
	 */
	@SuppressWarnings("deprecation")
	private void initComponents() {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener());

		Label lText = new Label();
		lText.setValue("Tagesübersicht Belegung vom ");
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bErfassung = new Button();
		bErfassung.setCaption("Erfassung");
		bErfassung.addStyleName(ValoTheme.BUTTON_LARGE);
		bErfassung.addClickListener(createClickListener());

		bKorrektur = new Button();
		bKorrektur.setCaption("Korrektur");
		bKorrektur.addStyleName(ValoTheme.BUTTON_LARGE);
		bKorrektur.addClickListener(createClickListener());

		// Erstellt eine Tabelle für alle Uhrzeiten
		Grid<TagesübersichtBelegungBean> tabelleUhrzeiten = new Grid<TagesübersichtBelegungBean>();
		tabelleUhrzeitenAufsetzen(tabelleUhrzeiten);
		fülleTabelleUhrzeiten(tabelleUhrzeiten);

		DateField datefield = new DateField();
		datefield.setValue(
				Instant.ofEpochMilli(belegung.getDatum().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
		datefield.setDateFormat("dd.MM.yyyy");
		if (!VaadinSession.getCurrent().getAttribute("user").toString().contains("Admin")) {
			// Nicht-Administratoren dürfen nur eine Woche in der Zeit zurück
			datefield.setRangeStart(LocalDate.now().minusDays(7));
			datefield.setRangeEnd(LocalDate.now());
		}
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			System.out.println(event.getValue());
			ZonedDateTime zdt = event.getValue().atStartOfDay().atZone(ZoneId.systemDefault());
			date = Date.from(zdt.toInstant());
			//Wegen Zeitverschiebung, sodass es nicht zu fehlern kommt, +8St. dem Datum anfügen
			date.setHours(8);

			// Sucht die Belegung für ein gewähltes Datum und für ein bestimmten Standort
			if (stockwerkEnum == StockwerkEnum.LL) {
				belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_LL);
			} else {
				belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_BB);
			}

			// Alle Werte anpassen
			fülleTabelleUhrzeiten(tabelleUhrzeiten);
		});

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

		image = null;
		if (stockwerkEnum == StockwerkEnum.EG) {
			image = new Image(null, new ClassResource("/belegung/EG-alle-cut.png"));
			bEG.setStyleName(ValoTheme.BUTTON_PRIMARY);
		} else if (stockwerkEnum == StockwerkEnum.ZG1) {
			image = new Image(null, new ClassResource("/belegung/1ZG-alle-cut.png"));
			b1ZG.setStyleName(ValoTheme.BUTTON_PRIMARY);
		} else if (stockwerkEnum == StockwerkEnum.ZG2) {
			image = new Image(null, new ClassResource("/belegung/2ZG-alle-cut.png"));
			b2ZG.setStyleName(ValoTheme.BUTTON_PRIMARY);
		} else if (stockwerkEnum == StockwerkEnum.LL) {
			image = new Image(null, new ClassResource("/belegung/LL-alle-cut.png"));
			bLL.setStyleName(ValoTheme.BUTTON_PRIMARY);
		}

		grid = new GridLayout(5, 12);
		grid.addStyleName("backgroundTages");
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0);
		grid.addComponent(datefield, 2, 0);
		grid.addComponent(bErfassung, 3, 0);
		grid.addComponent(bKorrektur, 4, 0);
		grid.addComponent(tabelleUhrzeiten, 0, 1, 4, 6);
		grid.addComponent(bLL, 0, 7);
		grid.addComponent(b2ZG, 0, 8);
		grid.addComponent(b1ZG, 0, 9);
		grid.addComponent(bEG, 0, 10);
		grid.addComponent(new Label(), 0, 11);
		AbsoluteLayout absoluteLayout = new AbsoluteLayout();
		absoluteLayout.addComponent(image);
		grid.addComponent(absoluteLayout, 1, 7, 4, 11);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row == 0) {
					if (col == 1 || col == 2) {
						grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
					}
				} else if (row >= 1 && row <= 6) {
					// Tabelle
					c.setHeight("96%");
					c.setWidth("95%");
				} else {
					c.setHeight("100%");
					c.setWidth("100%");

					if (col == 0 && row >= 7) {
						c.setWidth("70%");
					}
				}
			}
		}

		mainLayout.addComponent(grid);
	}

	/**
	 * Füllt die Tabelle mit Inhalt
	 * 
	 * @param tabelleUhrzeiten
	 */
	private void fülleTabelleUhrzeiten(Grid<TagesübersichtBelegungBean> tabelleUhrzeiten) {

		List<TagesübersichtBelegungBean> beanListe = new ArrayList<>();

		// Fügt alle Uhrzeiten einer Liste hinzu
		List<UhrzeitEnum> enumListe = new ArrayList<>();
		enumListe.add(UhrzeitEnum.NEUN);
		enumListe.add(UhrzeitEnum.ELF);
		enumListe.add(UhrzeitEnum.DREIZEHN);
		enumListe.add(UhrzeitEnum.FÜNFZEHN);
		enumListe.add(UhrzeitEnum.SIEBZEHN);
		enumListe.add(UhrzeitEnum.NEUNZEHN);

		// Sucht das richtige Stockwerk
		Stockwerk stockwerk = null;
		for (Stockwerk s : belegung.getStockwerkListe()) {
			if (s.getName() == stockwerkEnum) {
				stockwerk = s;
			}
		}

		// Geht durch alle Uhrzeiten durch
		for (UhrzeitEnum uhrzeitEnum : enumListe) {

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
		}

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

	@Override
	public void enter(ViewChangeEvent event) {
		// Der Eingang der Tagesübersicht Belegung ist eine lange URL, welche durch /
		// abgetrennt wird
		String args[] = event.getParameters().split("/");
		// Datum für die Belegung
		String datumString = args[0];
		// Stockwerk der Belegung
		String stockwerk = args[1];
		// Ob der Zugriff direkt aus der Startseite oder von der Belegung kommt
		String vonStartseiteString = args[2];

		this.date = new Date(Long.parseLong(datumString));

		if (stockwerk.equals("LL")) {
			this.stockwerkEnum = StockwerkEnum.LL;
		} else if (stockwerk.equals("EG")) {
			this.stockwerkEnum = StockwerkEnum.EG;
		} else if (stockwerk.equals("ZG1")) {
			this.stockwerkEnum = StockwerkEnum.ZG1;
		} else if (stockwerk.equals("ZG2")) {
			this.stockwerkEnum = StockwerkEnum.ZG2;
		}

		if (stockwerkEnum == StockwerkEnum.LL) {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_LL);
		} else {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_BB);
		}

		this.vonStartseite = Boolean.parseBoolean(vonStartseiteString);

		setCompositionRoot(init());
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					if (vonStartseite == true) {
						Page.getCurrent().setUriFragment("!" + StartseiteView.NAME);
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + " " + '/'
								+ stockwerkEnum.toString() + '/' + false + '/' + 0 + '/' + " ");
					}
				}

				if (e.getSource() == bErfassung) {
					getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + " " + '/'
							+ stockwerkEnum.toString() + '/' + false + '/' + 0 + '/' + " ");
				}

				if (e.getSource() == bKorrektur) {
					getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + date.getTime() + '/'
							+ stockwerkEnum.toString() + '/' + true + '/' + 0 + '/' + " ");
				}

				if (e.getSource() == bLL) {
					getUI().getNavigator().navigateTo(TagesübersichtBelegungViewWinti.NAME + '/' + date.getTime() + '/'
							+ StockwerkEnum.LL.toString() + '/' + vonStartseite);
				}

				if (e.getSource() == b2ZG) {
					getUI().getNavigator().navigateTo(TagesübersichtBelegungViewWinti.NAME + '/' + date.getTime() + '/'
							+ StockwerkEnum.ZG2.toString() + '/' + vonStartseite);
				}

				if (e.getSource() == b1ZG) {
					getUI().getNavigator().navigateTo(TagesübersichtBelegungViewWinti.NAME + '/' + date.getTime() + '/'
							+ StockwerkEnum.ZG1.toString() + '/' + vonStartseite);
				}

				if (e.getSource() == bEG) {
					getUI().getNavigator().navigateTo(TagesübersichtBelegungViewWinti.NAME + '/' + date.getTime() + '/'
							+ StockwerkEnum.EG.toString() + '/' + vonStartseite);
				}

			}
		};

	}

}
