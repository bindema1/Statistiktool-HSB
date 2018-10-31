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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import allgemein.view.MainView;
import allgemein.view.StartseiteView;
import belegung.db.BelegungsDatenbank2;
import belegung.model.Belegung;
import belegung.model.StockwerkEnum;

/**
 * View der Belegung. Zeigt alle Button, Label, Felder etc. in einem Layout an.
 * 
 * @author Marvin Bindemann
 */
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
	private Button bWeiter;
	private TextField tTotalPersonen;
	private TextField tTotalRäume;
	private int ausgewählteUhrzeit;
	private boolean korrektur;
	private boolean räumeVorhanden;
	private Belegung belegung;
	private StockwerkEnum stockwerkEnum;
	private Date date;
	private BelegungsDatenbank2 belegungDB = new BelegungsDatenbank2();
	private Image image;
	private int stockwerkzaehler = 0;

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

	public BelegungErfassenView(StockwerkEnum stockwerkenum, boolean korrektur) {

		if (stockwerkenum == StockwerkEnum.LL) {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(new Date(), StandortEnum.WINTERTHUR_LL);
		} else {
			this.belegung = belegungDB.selectBelegungForDateAndStandort(new Date(), StandortEnum.WINTERTHUR_BB);
		}

		this.stockwerkEnum = stockwerkenum;
		this.korrektur = korrektur;
	}

	private void initData() {
		date = belegung.getDatum();

		// Automatisch Start mit EG
		räumeVorhanden = true;
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

		});

		bPersonen = new Button();
		bPersonen.setCaption("Arbeitsplätze/Personen");
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

		bWeiter = new Button();
		bWeiter.setCaption("Weiter/Beenden");
		bWeiter.addStyleName(ValoTheme.BUTTON_LARGE);
		bWeiter.addClickListener(createClickListener(mainView));

		tTotalPersonen = new TextField("Total Personen");
		tTotalRäume = new TextField("Total Räume");

		List<String> data = null;
		ListSelect<String> uhrzeitListSelect;
		if (korrektur == true) {
			data = Arrays.asList(new String[] { "Wählen", "9 Uhr", "11 Uhr", "13 Uhr", "15 Uhr", "17 Uhr", "19 Uhr" });
			uhrzeitListSelect = new ListSelect<>("Uhrzeit:", data);
			uhrzeitListSelect.setRows(7);
			uhrzeitListSelect.select(data.get(0));
		} else {
			data = Arrays.asList(new String[] { "9 Uhr", "11 Uhr", "13 Uhr", "15 Uhr", "17 Uhr", "19 Uhr" });
			uhrzeitListSelect = new ListSelect<>("Uhrzeit:", data);
			uhrzeitListSelect.setRows(6);

			int time = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
			if (time <= 9) {
				uhrzeitListSelect.select(data.get(0));
			} else if (time <= 11) {
				uhrzeitListSelect.select(data.get(1));
			} else if (time <= 13) {
				uhrzeitListSelect.select(data.get(2));
			} else if (time <= 15) {
				uhrzeitListSelect.select(data.get(3));
			}
			if (time <= 17) {
				uhrzeitListSelect.select(data.get(4));
			} else if (time >= 18) {
				uhrzeitListSelect.select(data.get(5));
			}
		}

		uhrzeitListSelect.setWidth(100.0f, Unit.PERCENTAGE);
		uhrzeitListSelect.addValueChangeListener(event -> {

			ausgewählteUhrzeit = 0;

			switch (String.valueOf(event.getValue())) {
			case "[9 Uhr]":
				ausgewählteUhrzeit = 9;
				break;
			case "[11 Uhr]":
				ausgewählteUhrzeit = 11;
				break;
			case "[13 Uhr]":
				ausgewählteUhrzeit = 13;
				break;
			case "[15 Uhr]":
				ausgewählteUhrzeit = 15;
				break;
			case "[17 Uhr]":
				ausgewählteUhrzeit = 17;
				break;
			case "[19 Uhr]":
				ausgewählteUhrzeit = 19;
				break;
			}

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

		
		GridLayout grid = new GridLayout(5, 10);
		grid.addStyleName("gridlayout");
		grid.setSizeFull();
		
		grid.addComponent(bZurueck, 0, 0);
		if (korrektur == true) {
			grid.addComponent(lText, 1, 0, 2, 0);
			grid.addComponent(datefield, 3, 0);
		}else {
			grid.addComponent(lText, 1, 0, 3, 0);
		}	
		grid.addComponent(bTagesübersicht, 4, 0);
		grid.addComponent(uhrzeitListSelect, 0, 1, 0, 5);
		grid.addComponent(bPersonen, 1, 1, 2, 4);
		grid.addComponent(tTotalPersonen, 1, 5);
		grid.addComponent(bPersonenMinus, 2, 5);
		grid.addComponent(bRäume, 3, 1, 4, 3);
		grid.addComponent(tTotalRäume, 3, 4);
		grid.addComponent(bRäumeMinus, 4, 4);
		grid.addComponent(bWeiter, 3, 5, 4, 5);
		grid.addComponent(bLL, 0, 6);
		grid.addComponent(b2ZG, 0, 7);
		grid.addComponent(b1ZG, 0, 8);
		grid.addComponent(bEG, 0, 9);
		grid.addComponent(image, 1, 6, 4, 9);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row == 0) {
					if (col == 1 || col == 2) {
						grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
					}
				}else {
					c.setHeight("80%");
					c.setWidth("80%");
					
					if(row >= 6) {
						if (col == 0) {
							c.setHeight("100%");
							c.setWidth("30%");
						} else {
							c.setHeight("100%");
							c.setWidth("100%");
						}
					}
				}
			}
		}

		mainLayout.addComponent(grid);
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					if (korrektur == true) {
						leiteZurTagesübersichtFürStockwerk(mainView);
					} else {
						mainView.setContent(new StartseiteView().init(mainView));
					}
				}

				if (e.getSource() == bTagesübersicht) {
					leiteZurTagesübersichtFürStockwerk(mainView);
				}

				if (e.getSource() == bLL) {
					stockwerkzaehler = 3;
					räumeVorhanden = true;
				}

				if (e.getSource() == b2ZG) {
					stockwerkzaehler = 2;
					räumeVorhanden = false;
				}

				if (e.getSource() == b1ZG) {
					stockwerkzaehler = 1;
					räumeVorhanden = false;
				}

				if (e.getSource() == bEG) {
					stockwerkzaehler = 0;
					räumeVorhanden = true;
				}

			}

			private void leiteZurTagesübersichtFürStockwerk(final MainView mainView) {
				switch (stockwerkzaehler) {
				case 0:
					mainView.setContent(new TagesübersichtBelegungView(date, StockwerkEnum.EG).init(mainView));
					break;
				case 1:
					mainView.setContent(new TagesübersichtBelegungView(date, StockwerkEnum.ZG1).init(mainView));
					break;
				case 2:
					mainView.setContent(new TagesübersichtBelegungView(date, StockwerkEnum.ZG2).init(mainView));
					break;
				case 3:
					mainView.setContent(new TagesübersichtBelegungView(date, StockwerkEnum.LL).init(mainView));
					break;
				}
			}
		};

	}

}
