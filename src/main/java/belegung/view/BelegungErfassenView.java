package belegung.view;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.ClassResource;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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
public class BelegungErfassenView implements View{

	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bTagesübersicht;
	private Button bLL;
	private Button b1ZG;
	private Button b2ZG;
	private Button bEG;
	private boolean korrektur;
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
	}

	// Initialisieren der GUI Komponente
	private void initComponents(MainView mainView) {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener(mainView));

		Label lText = new Label();
		if(korrektur == true) {
			lText.setValue("Belegung erfassen vom ");
		}else {
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

		VerticalLayout overallLayout = new VerticalLayout();

		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.setWidth("100%");
		headerLayout.addComponent(bZurueck);
		headerLayout.addComponent(lText);
		if(korrektur == true) {
			headerLayout.addComponent(datefield);
		}
		headerLayout.addComponent(bTagesübersicht);
		overallLayout.addComponent(headerLayout);

		GridLayout grid = new GridLayout(2, 4);
		grid.setSizeFull();
		grid.addComponent(bLL, 0, 0);
		grid.addComponent(b2ZG, 0, 1);
		grid.addComponent(b1ZG, 0, 2);
		grid.addComponent(bEG, 0, 3);
		grid.addComponent(image, 1, 0, 1, 3);
		grid.setColumnExpandRatio(0, 0.1f);
		grid.setColumnExpandRatio(1, 0.3f);
		grid.setColumnExpandRatio(2, 0.3f);
		grid.setColumnExpandRatio(3, 0.3f);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (col == 0) {
					c.setHeight("100%");
					c.setWidth("30%");
				} else {
					c.setHeight("100%");
					c.setWidth("100%");
				}
			}
		}

		overallLayout.addComponent(grid);

		mainLayout.addComponent(overallLayout);
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					if(korrektur == true) {
						leiteZurTagesübersichtFürStockwerk(mainView);
					}else {
						mainView.setContent(new StartseiteView().init(mainView));
					}
				}

				if (e.getSource() == bTagesübersicht) {
					leiteZurTagesübersichtFürStockwerk(mainView);
				}

				if (e.getSource() == bLL) {
					stockwerkzaehler = 3;
				}

				if (e.getSource() == b2ZG) {
					stockwerkzaehler = 2;
				}

				if (e.getSource() == b1ZG) {
					stockwerkzaehler = 1;
				}

				if (e.getSource() == bEG) {
					stockwerkzaehler = 0;
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
