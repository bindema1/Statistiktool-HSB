package benutzungsstatistik.view;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Wintikurier;

/**
 * View des Wintikuriers. Zeigt alle Button, Label, Felder etc. in einem Layout
 * an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class WintikurierView extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-Wintikurier";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bGesundheit;
	private Button bLinguistik;
	private Button bTechnik;
	private Button bWirtschaft;
	private Button bGesundheitMinus;
	private Button bLinguistikMinus;
	private Button bTechnikMinus;
	private Button bWirtschaftMinus;
	private Label lGesundheitTotal;
	private Label lLinguistikTotal;
	private Label lTechnikTotal;
	private Label lWirtschaftTotal;
	private Label lText;
	private Label lTotal;
	private Label lPlatzhalter;
	private DateField datefield;
	private boolean korrektur;
//	private WintikurierDatenbank wintikurierDB = new WintikurierDatenbank();
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	private Wintikurier wintikurier;
	private Benutzungsstatistik benutzungsstatistik;

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
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

	public WintikurierView() {
		
	}

	private void initData() {

	}

	// Initialisieren der GUI Komponente
	private void initComponents() {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener());

		lText = new Label();
		if(korrektur == true) {
			lText.setValue("Wintikurier vom ");
		}else {
			lText.setValue("Wintikurier vom " + new SimpleDateFormat("dd.MM.yyyy").format(benutzungsstatistik.getDatum()));
		}
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);
		
		datefield = new DateField();
		datefield.setValue(Instant.ofEpochMilli(benutzungsstatistik.getDatum().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
		datefield.setDateFormat("dd.MM.yyyy");
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			ZonedDateTime zdt = event.getValue().atStartOfDay().atZone(ZoneId.systemDefault());
			Date date = Date.from(zdt.toInstant());

			benutzungsstatistik = new BenutzungsstatistikDatenbank().selectBenutzungsstatistikForDateAndStandort(date,
					StandortEnum.WINTERTHUR_BB);
			wintikurier = benutzungsstatistik.getWintikurier();
			lWirtschaftTotal.setValue("" + wintikurier.getAnzahl_Wirtschaft());
			lTechnikTotal.setValue("" + wintikurier.getAnzahl_Technik());
			lLinguistikTotal.setValue("" + wintikurier.getAnzahl_Linguistik());
			lGesundheitTotal.setValue("" + wintikurier.getAnzahl_Gesundheit());
		});

		lTotal = new Label();
		lTotal.setValue("Total");
		lTotal.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		lPlatzhalter = new Label();
		lPlatzhalter.setValue("");

		bGesundheit = new Button();
		bGesundheit.setCaption("+1 Gesundheit");
		bGesundheit.addStyleName(ValoTheme.BUTTON_LARGE);
		bGesundheit.addClickListener(createClickListener());

		lGesundheitTotal = new Label();
		lGesundheitTotal.setValue("" + wintikurier.getAnzahl_Gesundheit());
		lGesundheitTotal.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bGesundheitMinus = new Button();
		bGesundheitMinus.setCaption("Korrektur -1");
		bGesundheitMinus.addStyleName(ValoTheme.BUTTON_LARGE + " " + ValoTheme.BUTTON_DANGER);
		bGesundheitMinus.addClickListener(createClickListener());

		bLinguistik = new Button();
		bLinguistik.setCaption("+1 Linguistik");
		bLinguistik.addStyleName(ValoTheme.BUTTON_LARGE);
		bLinguistik.addClickListener(createClickListener());

		lLinguistikTotal = new Label();
		lLinguistikTotal.setValue("" + wintikurier.getAnzahl_Linguistik());
		lLinguistikTotal.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bLinguistikMinus = new Button();
		bLinguistikMinus.setCaption("Korrektur -1");
		bLinguistikMinus.addStyleName(ValoTheme.BUTTON_LARGE + " " + ValoTheme.BUTTON_DANGER);
		bLinguistikMinus.addClickListener(createClickListener());

		bTechnik = new Button();
		bTechnik.setCaption("  +1 Technik  ");
		bTechnik.addStyleName(ValoTheme.BUTTON_LARGE);
		bTechnik.addClickListener(createClickListener());

		lTechnikTotal = new Label();
		lTechnikTotal.setValue("" + wintikurier.getAnzahl_Technik());
		lTechnikTotal.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bTechnikMinus = new Button();
		bTechnikMinus.setCaption("Korrektur -1");
		bTechnikMinus.addStyleName(ValoTheme.BUTTON_LARGE + " " + ValoTheme.BUTTON_DANGER);
		bTechnikMinus.addClickListener(createClickListener());

		bWirtschaft = new Button();
		bWirtschaft.setCaption("+1 Wirtschaft");
		bWirtschaft.addStyleName(ValoTheme.BUTTON_LARGE);
		bWirtschaft.addClickListener(createClickListener());

		lWirtschaftTotal = new Label();
		lWirtschaftTotal.setValue("" + wintikurier.getAnzahl_Wirtschaft());
		lWirtschaftTotal.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bWirtschaftMinus = new Button();
		bWirtschaftMinus.setCaption("Korrektur -1");
		bWirtschaftMinus.addStyleName(ValoTheme.BUTTON_LARGE + " " + ValoTheme.BUTTON_DANGER);
		bWirtschaftMinus.addClickListener(createClickListener());

		GridLayout grid = new GridLayout(8, 7);
		grid.addStyleName("gridlayout");
		grid.setWidth("100%");
		grid.setHeight("90%");
		grid.addComponent(bZurueck, 0, 0);
		if(korrektur == true) {
			grid.addComponent(lText, 1, 0, 3, 0);
			grid.addComponent(datefield, 4, 0, 7, 0);
		}else {
			grid.addComponent(lText, 1, 0, 7, 0);
		}
		grid.addComponent(bGesundheit, 0, 1, 1, 4);
		grid.addComponent(bLinguistik, 2, 1, 3, 4);
		grid.addComponent(bTechnik, 4, 1, 5, 4);
		grid.addComponent(bWirtschaft, 6, 1, 7, 4);
		grid.addComponent(lGesundheitTotal, 0, 5, 1, 5);
		grid.addComponent(lLinguistikTotal, 2, 5, 3, 5);
		grid.addComponent(lTechnikTotal, 4, 5, 5, 5);
		grid.addComponent(lWirtschaftTotal, 6, 5, 7, 5);
		grid.addComponent(bGesundheitMinus, 0, 6, 1, 6);
		grid.addComponent(bLinguistikMinus, 2, 6, 3, 6);
		grid.addComponent(bTechnikMinus, 4, 6, 5, 6);
		grid.addComponent(bWirtschaftMinus, 6, 6, 7, 6);
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
					if(korrektur == true) {
						if (col == 1 || col == 2 || col == 3) {
							grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
						}
					}
				} else if (row == 5) {
					// Zeile 5 sind label, dort darf die Grösse nicht verändert werden, da das
					// Alignment sonst nicht mehr funktioniert
				} else if (row >= 1 && row <= 4) {
					c.setHeight("100%");
					c.setWidth("90%");
				} else {
					c.setHeight("80%");
					c.setWidth("80%");
				}

			}
		}

		mainLayout.addComponent(grid);

	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	    String args[] = event.getParameters().split("/");
	    String id = args[0];
	    String korrekturString = args[1];
	    this.benutzungsstatistik = benutzungsstatistikDB.findBenutzungsstatistikById(Integer.parseInt(id));
	    this.wintikurier = benutzungsstatistik.getWintikurier();
	    this.korrektur = Boolean.parseBoolean(korrekturString);
	    
	    setCompositionRoot(init());
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					if(korrektur == true) {
						getUI().getNavigator().navigateTo(KorrekturView.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
					}else {
						Page.getCurrent().setUriFragment("!"+BenutzungsstatistikBBView.NAME);
					}
				}

				if (e.getSource() == bGesundheit) {
					wintikurier.increaseAnzahl_Gesundheit();
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("Anzahl Gesundheit +1", Type.TRAY_NOTIFICATION);
					lGesundheitTotal.setValue("" + wintikurier.getAnzahl_Gesundheit());
				}

				if (e.getSource() == bGesundheitMinus) {
					if (wintikurier.getAnzahl_Gesundheit() != 0) {
						wintikurier.decreaseAnzahl_Gesundheit();
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
						Notification.show("Anzahl Gesundheit -1", Type.TRAY_NOTIFICATION);
						lGesundheitTotal.setValue("" + wintikurier.getAnzahl_Gesundheit());
					} else {
						Notification.show("Tiefer als 0 nicht möglich", Type.WARNING_MESSAGE);
					}
				}

				if (e.getSource() == bLinguistik) {
					wintikurier.increaseAnzahl_Linguistik();
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("Anzahl Linguistik +1", Type.TRAY_NOTIFICATION);
					lLinguistikTotal.setValue("" + wintikurier.getAnzahl_Linguistik());
				}

				if (e.getSource() == bLinguistikMinus) {
					if (wintikurier.getAnzahl_Linguistik() != 0) {
						wintikurier.decreaseAnzahl_Linguistik();
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
						Notification.show("Anzahl Linguistik -1", Type.TRAY_NOTIFICATION);
						lLinguistikTotal.setValue("" + wintikurier.getAnzahl_Linguistik());
					} else {
						Notification.show("Tiefer als 0 nicht möglich", Type.WARNING_MESSAGE);
					}
				}

				if (e.getSource() == bTechnik) {
					wintikurier.increaseAnzahl_Technik();
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("Anzahl Technik +1", Type.TRAY_NOTIFICATION);
					lTechnikTotal.setValue("" + wintikurier.getAnzahl_Technik());
				}

				if (e.getSource() == bTechnikMinus) {
					if (wintikurier.getAnzahl_Technik() != 0) {
						wintikurier.decreaseAnzahl_Technik();
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
						Notification.show("Anzahl Technik -1", Type.TRAY_NOTIFICATION);
						lTechnikTotal.setValue("" + wintikurier.getAnzahl_Technik());
					} else {
						Notification.show("Tiefer als 0 nicht möglich", Type.WARNING_MESSAGE);
					}
				}

				if (e.getSource() == bWirtschaft) {
					wintikurier.increaseAnzahl_Wirtschaft();
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("Anzahl Wirtschaft +1", Type.TRAY_NOTIFICATION);
					lWirtschaftTotal.setValue("" + wintikurier.getAnzahl_Wirtschaft());
				}

				if (e.getSource() == bWirtschaftMinus) {
					if (wintikurier.getAnzahl_Wirtschaft() != 0) {
						wintikurier.decreaseAnzahl_Wirtschaft();
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
						Notification.show("Anzahl Wirtschaft -1", Type.TRAY_NOTIFICATION);
						lWirtschaftTotal.setValue("" + wintikurier.getAnzahl_Wirtschaft());
					} else {
						Notification.show("Tiefer als 0 nicht möglich", Type.WARNING_MESSAGE);
					}
				}

			}
		};

	}

}
