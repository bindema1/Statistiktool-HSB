package belegung.view;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
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
import allgemein.view.MainView;
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
public class TagesübersichtBelegungView {

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

	public TagesübersichtBelegungView(Date date, StockwerkEnum stockwerkenum) {

		if (stockwerkenum == StockwerkEnum.LL) {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_LL);
		} else {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(date, StandortEnum.WINTERTHUR_BB);
		}

		this.stockwerkEnum = stockwerkenum;
		this.date = date;
	}

	private void initData() {

	}

	// Initialisieren der GUI Komponente
	private void initComponents(MainView mainView) {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener(mainView));

		Label lText = new Label();
		lText.setValue("Tagesübersicht Belegung vom ");
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bErfassung = new Button();
		bErfassung.setCaption("Erfassung");
		bErfassung.addStyleName(ValoTheme.BUTTON_LARGE);
		bErfassung.addClickListener(createClickListener(mainView));

		bKorrektur = new Button();
		bKorrektur.setCaption("Korrektur");
		bKorrektur.addStyleName(ValoTheme.BUTTON_LARGE);
		bKorrektur.addClickListener(createClickListener(mainView));

		Grid<TagesübersichtBelegungBean> tabelleUhrzeiten = new Grid<TagesübersichtBelegungBean>();
		tabelleUhrzeitenAufsetzen(tabelleUhrzeiten);
		fülleTabelleUhrzeiten(tabelleUhrzeiten);

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

			// Alle Werte anpassen
			fülleTabelleUhrzeiten(tabelleUhrzeiten);
		});

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

		image = null;
		if (stockwerkEnum == StockwerkEnum.EG) {
			image = new Image(null, new ClassResource("/belegung/EG-lang.png"));
			bEG.setStyleName(ValoTheme.BUTTON_PRIMARY);
		} else if (stockwerkEnum == StockwerkEnum.ZG1) {
			image = new Image(null, new ClassResource("/belegung/1.ZG-lang.png"));
			b1ZG.setStyleName(ValoTheme.BUTTON_PRIMARY);
		} else if (stockwerkEnum == StockwerkEnum.ZG2) {
			image = new Image(null, new ClassResource("/belegung/2.ZG-lang.png"));
			b2ZG.setStyleName(ValoTheme.BUTTON_PRIMARY);
		} else if (stockwerkEnum == StockwerkEnum.LL) {
			image = new Image(null, new ClassResource("/belegung/LL-lang.png"));
			bLL.setStyleName(ValoTheme.BUTTON_PRIMARY);
		}

//		VerticalLayout overallLayout = new VerticalLayout();
//		HorizontalLayout headerLayout = new HorizontalLayout();
//		headerLayout.setWidth("100%");
//		headerLayout.setSpacing(true);
//		headerLayout.addComponent(bZurueck);
//		headerLayout.addComponent(lText);
//		headerLayout.addComponent(datefield);
//		headerLayout.addComponent(bErfassung);
//		headerLayout.addComponent(bKorrektur);
//		overallLayout.addComponent(headerLayout);
//		overallLayout.addComponent(tabelleUhrzeiten);

		grid = new GridLayout(5, 9);
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0);
		grid.addComponent(datefield, 2, 0);
		grid.addComponent(bErfassung, 3, 0);
		grid.addComponent(bKorrektur, 4, 0);
		grid.addComponent(tabelleUhrzeiten, 0, 1, 4, 4);
		grid.addComponent(bLL, 0, 5);
		grid.addComponent(b2ZG, 0, 6);
		grid.addComponent(b1ZG, 0, 7);
		grid.addComponent(bEG, 0, 8);
		grid.addComponent(image, 1, 5, 4, 8);
		
		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);
				
				// Button grösser machen
				if (row == 0) {
					if (col == 1 || col == 2) {
						grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
					}
				}else if(row >= 1 && row <= 4){
					//Tabelle
					c.setHeight("100%");
					c.setWidth("93%");
				}else {
					c.setHeight("100%");
					c.setWidth("100%");
					
					if (col == 0 && row >= 5) {
						c.setWidth("50%");
					} 
				}
			}
		}

		mainLayout.addComponent(grid);
	}

	private void fülleTabelleUhrzeiten(Grid<TagesübersichtBelegungBean> tabelleUhrzeiten) {

		List<TagesübersichtBelegungBean> beanListe = new ArrayList<>();
		List<UhrzeitEnum> enumListe = new ArrayList<>();
		enumListe.add(UhrzeitEnum.NEUN);
		enumListe.add(UhrzeitEnum.ELF);
		enumListe.add(UhrzeitEnum.DREIZEHN);
		enumListe.add(UhrzeitEnum.FÜNFZEHN);
		enumListe.add(UhrzeitEnum.SIEBZEHN);
		enumListe.add(UhrzeitEnum.NEUNZEHN);

		Stockwerk stockwerk = null;
		for (Stockwerk s : belegung.getStockwerkListe()) {
			if (s.getName() == stockwerkEnum) {
				stockwerk = s;
			}
		}

		for (UhrzeitEnum uhrzeitEnum : enumListe) {

			TagesübersichtBelegungBean t = new TagesübersichtBelegungBean();
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

			for (Arbeitsplätze arbeitsplätze : stockwerk.getArbeitsplatzListe()) {
				if (uhrzeitEnum == arbeitsplätze.getUhrzeit()) {
					t.setArbeitsplätze(arbeitsplätze.getAnzahlPersonen());
				}
			}

			for (SektorA sektorA : stockwerk.getSektorAListe()) {
				if (uhrzeitEnum == sektorA.getUhrzeit()) {
					t.setSektorA(sektorA.getAnzahlPersonen());
				}
			}

			for (SektorB sektorB : stockwerk.getSektorBListe()) {
				if (uhrzeitEnum == sektorB.getUhrzeit()) {
					t.setSektorB(sektorB.getAnzahlPersonen());
				}
			}

			for (Gruppenräume gruppenräume : stockwerk.getGruppenräumeListe()) {
				if (uhrzeitEnum == gruppenräume.getUhrzeit()) {
					t.setGruppenräume(gruppenräume.getAnzahlRäume());
					t.setGruppenräumePersonen(gruppenräume.getAnzahlPersonen());
				}
			}

			for (Carrels carrels : stockwerk.getCarrelsListe()) {
				if (uhrzeitEnum == carrels.getUhrzeit()) {
					t.setCarrels(carrels.getAnzahlRäume());
					t.setCarrelsPersonen(carrels.getAnzahlPersonen());
				}
			}

			beanListe.add(t);
		}

		tabelleUhrzeiten.setWidth("90%");
		tabelleUhrzeiten.setHeightByRows(beanListe.size());
		tabelleUhrzeiten.setItems(beanListe);
	}

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

	@SuppressWarnings("serial")
	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					mainView.setContent(new BelegungErfassenView(StockwerkEnum.EG, false, 0).init(mainView));
				}

				if (e.getSource() == bErfassung) {
					mainView.setContent(new BelegungErfassenView(StockwerkEnum.EG, false, 0).init(mainView));
				}

				if (e.getSource() == bKorrektur) {
					mainView.setContent(new BelegungErfassenView(StockwerkEnum.EG, true, 0).init(mainView));
				}

				if (e.getSource() == bLL) {
					mainView.setContent(new TagesübersichtBelegungView(date, StockwerkEnum.LL).init(mainView));
				}

				if (e.getSource() == b2ZG) {
					mainView.setContent(new TagesübersichtBelegungView(date, StockwerkEnum.ZG2).init(mainView));
				}

				if (e.getSource() == b1ZG) {
					mainView.setContent(new TagesübersichtBelegungView(date, StockwerkEnum.ZG1).init(mainView));
				}

				if (e.getSource() == bEG) {
					mainView.setContent(new TagesübersichtBelegungView(date, StockwerkEnum.EG).init(mainView));
				}

			}
		};

	}

}
