package belegung.view;

import java.time.LocalDateTime;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.view.MainView;
import allgemein.view.StartseiteView;
import belegung.bean.TagesübersichtBelegungBean;
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
	private Button bLL;
	private Button b1ZG;
	private Button b2ZG;
	private Button bEG;
	private Belegung belegung;
	private StockwerkEnum stockwerkEnum;

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

	public TagesübersichtBelegungView(Belegung belegung, StockwerkEnum stockwerkenum) {
		this.belegung = belegung;
		this.stockwerkEnum = stockwerkenum;
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

		DateTimeField datefield = new DateTimeField();
		datefield.setValue(LocalDateTime.now());
		datefield.setDateFormat("dd.MM.yyyy");
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			ZonedDateTime zdt = event.getValue().atZone(ZoneId.systemDefault());
			Date date = Date.from(zdt.toInstant());

		});

		Grid<TagesübersichtBelegungBean> tabelleUhrzeiten = new Grid<TagesübersichtBelegungBean>();
		tabelleUhrzeiten.addColumn(TagesübersichtBelegungBean::getUhrzeit).setCaption("Uhrzeit");
		tabelleUhrzeiten.addColumn(TagesübersichtBelegungBean::getArbeitsplätze).setCaption("Arbeitsplätze");
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
		fülleTabelleUhrzeiten(tabelleUhrzeiten);

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

		Image image = null;
		if(stockwerkEnum == StockwerkEnum.EG) {
			image = new Image(null, new ClassResource("/belegung/EG-lang.png"));
			bEG.setStyleName(ValoTheme.BUTTON_PRIMARY);
		}else if(stockwerkEnum == StockwerkEnum.ZG1) {
			image = new Image(null, new ClassResource("/belegung/1.ZG-lang.png"));
			b1ZG.setStyleName(ValoTheme.BUTTON_PRIMARY);
		}else if(stockwerkEnum == StockwerkEnum.ZG2) {
			image = new Image(null, new ClassResource("/belegung/2.ZG-lang.png"));
			b2ZG.setStyleName(ValoTheme.BUTTON_PRIMARY);
		}else if(stockwerkEnum == StockwerkEnum.LL) {
			image = new Image(null, new ClassResource("/belegung/LL-lang.png"));
			bLL.setStyleName(ValoTheme.BUTTON_PRIMARY);
		}

		VerticalLayout overallLayout = new VerticalLayout();

		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.setWidth("100%");
		headerLayout.addComponent(bZurueck);
		headerLayout.addComponent(lText);
		headerLayout.addComponent(datefield);
		headerLayout.addComponent(bErfassung);
		overallLayout.addComponent(headerLayout);
		overallLayout.addComponent(tabelleUhrzeiten);

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

		tabelleUhrzeiten.setWidth("100%");
		tabelleUhrzeiten.setHeightByRows(beanListe.size());
		tabelleUhrzeiten.setItems(beanListe);
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					Notification.show("Zurück", Type.WARNING_MESSAGE);
					mainView.setContent(new StartseiteView().init(mainView));
				}

				if (e.getSource() == bErfassung) {
//					mainView.setContent(new KorrekturView(benutzungsstatistik).init(mainView));
				}

				if (e.getSource() == bLL) {
					//TODO Belegung von LL aus DB holen
//					mainView.setContent(new TagesübersichtBelegungView(belegung, StockwerkEnum.LL).init(mainView));
				}

				if (e.getSource() == b2ZG) {
					//TODO Belegung von EG aus DB holen
					mainView.setContent(new TagesübersichtBelegungView(belegung, StockwerkEnum.ZG2).init(mainView));
				}

				if (e.getSource() == b1ZG) {
					//TODO Belegung von EG aus DB holen
					mainView.setContent(new TagesübersichtBelegungView(belegung, StockwerkEnum.ZG1).init(mainView));
				}

				if (e.getSource() == bEG) {
					//TODO Belegung von EG aus DB holen
					mainView.setContent(new TagesübersichtBelegungView(belegung, StockwerkEnum.EG).init(mainView));
				}

			}
		};

	}

}
