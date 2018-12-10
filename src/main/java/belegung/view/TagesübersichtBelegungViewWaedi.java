package belegung.view;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import belegung.bean.TagesübersichtBelegungBean;
import belegung.db.BelegungsDatenbank;
import belegung.model.Arbeitsplätze;
import belegung.model.Belegung;
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
public class TagesübersichtBelegungViewWaedi extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Belegung-Wädenswil-Übersicht";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bErfassung;
	private Button bKorrektur;
	private Belegung belegung;
	private StockwerkEnum stockwerkEnum;
	private Date date;
	private BelegungsDatenbank belegungDB = new BelegungsDatenbank();
	private GridLayout grid;

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

	public TagesübersichtBelegungViewWaedi() {

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
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			ZonedDateTime zdt = event.getValue().atStartOfDay().atZone(ZoneId.systemDefault());
			date = Date.from(zdt.toInstant());

			// Sucht die Belegung für ein gewähltes Datum und für ein bestimmten Standort
			if (stockwerkEnum == StockwerkEnum.WÄDI) {
				belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WÄDENSWIL);
			}

			// Alle Werte anpassen
			fülleTabelleUhrzeiten(tabelleUhrzeiten);
		});

		grid = new GridLayout(5, 7);
		grid.addStyleName("backgroundTages");
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0);
		grid.addComponent(datefield, 2, 0);
		grid.addComponent(bErfassung, 3, 0);
		grid.addComponent(bKorrektur, 4, 0);
		grid.addComponent(tabelleUhrzeiten, 0, 1, 4, 2);
		grid.addComponent(new Label(), 0, 3, 4, 6);

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
//					c.setHeight("60%");
					c.setWidth("95%");
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
		enumListe.add(UhrzeitEnum.ELF);
		enumListe.add(UhrzeitEnum.FÜNFZEHN);

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
		if (stockwerkEnum == StockwerkEnum.WÄDI) {
			tabelleUhrzeiten.addColumn(TagesübersichtBelegungBean::getArbeitsplätze).setCaption("Arbeitsplätze");
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

		this.date = new Date(Long.parseLong(datumString));

		if (stockwerk.equals("WÄDI")) {
			this.stockwerkEnum = StockwerkEnum.WÄDI;
		} 

		if (stockwerkEnum == StockwerkEnum.WÄDI) {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WÄDENSWIL);
		} 

		setCompositionRoot(init());
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					getUI().getNavigator().navigateTo(BelegungErfassenViewWaedi.NAME + '/' + " " + '/'
							+ StockwerkEnum.WÄDI.toString() + '/' + false + '/' + " ");
				}

				if (e.getSource() == bErfassung) {
					getUI().getNavigator().navigateTo(BelegungErfassenViewWaedi.NAME + '/' + " " + '/'
							+ StockwerkEnum.WÄDI.toString() + '/' + false + '/' + " ");
				}

				if (e.getSource() == bKorrektur) {
					getUI().getNavigator().navigateTo(BelegungErfassenViewWaedi.NAME + '/' + date.getTime() + '/'
							+ StockwerkEnum.WÄDI.toString() + '/' + true + '/' + " ");
				}

			}
		};

	}

}
