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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Internerkurier;

/**
 * View des Wintikuriers. Zeigt alle Button, Label, Felder etc. in einem Layout
 * an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class InternerkurierViewWaedi extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-Internerkurier";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bReidbach;
	private Button bRA;
	private Button bGS;
	private Button bReidbachMinus;
	private Button bRAMinus;
	private Button bGSMinus;
	private Label lReidbachTotal;
	private Label lRATotal;
	private Label lGSTotal;
	private Label lText;
	private Label lTotal;
	private Label lPlatzhalter;
	private DateField datefield;
	private boolean korrektur;
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	private Internerkurier internerkurier;
	private Benutzungsstatistik benutzungsstatistik;

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		if (korrektur == true) {
			mainLayout.addStyleName("backgroundKorrektur");
		} else {
			mainLayout.addStyleName("backgroundErfassung");
		}
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

	public InternerkurierViewWaedi() {
		
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
			lText.setValue("Interner Kurier vom ");
		}else {
			lText.setValue("Interner Kurier vom " + new SimpleDateFormat("dd.MM.yyyy").format(benutzungsstatistik.getDatum()));
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
			internerkurier = benutzungsstatistik.getInternerkurier();
			lGSTotal.setValue("" + internerkurier.getAnzahl_GS());
			lRATotal.setValue("" + internerkurier.getAnzahl_RA());
			lReidbachTotal.setValue("" + internerkurier.getAnzahl_Reidbach());
		});

		lTotal = new Label();
		lTotal.setValue("Total");
		lTotal.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		lPlatzhalter = new Label();
		lPlatzhalter.setValue("");

		bReidbach = new Button();
		bReidbach.setCaption("+1 Kampus Reidbach");
		bReidbach.addStyleName(ValoTheme.BUTTON_LARGE);
		bReidbach.addClickListener(createClickListener());

		lReidbachTotal = new Label();
		lReidbachTotal.setValue("" + internerkurier.getAnzahl_Reidbach());
		lReidbachTotal.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bReidbachMinus = new Button();
		bReidbachMinus.setCaption("Korrektur -1");
		bReidbachMinus.addStyleName(ValoTheme.BUTTON_LARGE + " " + ValoTheme.BUTTON_DANGER);
		bReidbachMinus.addClickListener(createClickListener());

		bRA = new Button();
		bRA.setCaption("  +1 Kampus RA  ");
		bRA.addStyleName(ValoTheme.BUTTON_LARGE);
		bRA.addClickListener(createClickListener());

		lRATotal = new Label();
		lRATotal.setValue("" + internerkurier.getAnzahl_RA());
		lRATotal.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bRAMinus = new Button();
		bRAMinus.setCaption("Korrektur -1");
		bRAMinus.addStyleName(ValoTheme.BUTTON_LARGE + " " + ValoTheme.BUTTON_DANGER);
		bRAMinus.addClickListener(createClickListener());

		bGS = new Button();
		bGS.setCaption("  +1 Kampus GS  ");
		bGS.addStyleName(ValoTheme.BUTTON_LARGE);
		bGS.addClickListener(createClickListener());

		lGSTotal = new Label();
		lGSTotal.setValue("" + internerkurier.getAnzahl_GS());
		lGSTotal.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bGSMinus = new Button();
		bGSMinus.setCaption("Korrektur -1");
		bGSMinus.addStyleName(ValoTheme.BUTTON_LARGE + " " + ValoTheme.BUTTON_DANGER);
		bGSMinus.addClickListener(createClickListener());

		GridLayout grid = new GridLayout(6, 7);
		grid.setWidth("100%");
		grid.setHeight("90%");
		grid.addComponent(new HorizontalLayout(bZurueck), 0, 0, 1, 0);
		if(korrektur == true) {
			grid.addComponent(lText, 2, 0, 3, 0);
			grid.addComponent(datefield, 4, 0, 5, 0);
		}else {
			grid.addComponent(lText, 2, 0, 5, 0);
		}
		grid.addComponent(bReidbach, 0, 1, 1, 4);
		grid.addComponent(bRA, 2, 1, 3, 4);
		grid.addComponent(bGS, 4, 1, 5, 4);
		grid.addComponent(lReidbachTotal, 0, 5, 1, 5);
		grid.addComponent(lRATotal, 2, 5, 3, 5);
		grid.addComponent(lGSTotal, 4, 5, 5, 5);
		grid.addComponent(bReidbachMinus, 0, 6, 1, 6);
		grid.addComponent(bRAMinus, 2, 6, 3, 6);
		grid.addComponent(bGSMinus, 4, 6, 5, 6);
		
		grid.setColumnExpandRatio(0, 0.166f);
		grid.setColumnExpandRatio(1, 0.166f);
		grid.setColumnExpandRatio(2, 0.166f);
		grid.setColumnExpandRatio(3, 0.166f);
		grid.setColumnExpandRatio(4, 0.166f);
		grid.setColumnExpandRatio(5, 0.166f);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {

				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row == 0) {
					c.setWidth("80%");
				} else if (row == 5) {
					// Zeile 5 sind label, dort darf die Grösse nicht verändert werden, da das
					// Alignment sonst nicht mehr funktioniert
				} else if (row >= 1 && row <= 4) {
					c.setHeight("90%");
					c.setWidth("80%");
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
	    this.internerkurier = benutzungsstatistik.getInternerkurier();
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
						getUI().getNavigator().navigateTo(KorrekturViewWaedi.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
					}else {
						Page.getCurrent().setUriFragment("!"+BenutzungsstatistikViewWaedi.NAME);
					}
				}

				if (e.getSource() == bReidbach) {
					internerkurier.increaseAnzahl_Reidbach();
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("Anzahl Reidbach +1", Type.TRAY_NOTIFICATION);
					lReidbachTotal.setValue("" + internerkurier.getAnzahl_Reidbach());
				}

				if (e.getSource() == bReidbachMinus) {
					if (internerkurier.getAnzahl_Reidbach() != 0) {
						internerkurier.decreaseAnzahl_Reidbach();
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
						Notification.show("Anzahl Reidbach -1", Type.TRAY_NOTIFICATION);
						lReidbachTotal.setValue("" + internerkurier.getAnzahl_Reidbach());
					} else {
						Notification.show("Tiefer als 0 nicht möglich", Type.WARNING_MESSAGE);
					}
				}

				if (e.getSource() == bRA) {
					internerkurier.increaseAnzahl_RA();
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("Anzahl RA +1", Type.TRAY_NOTIFICATION);
					lRATotal.setValue("" + internerkurier.getAnzahl_RA());
				}

				if (e.getSource() == bRAMinus) {
					if (internerkurier.getAnzahl_RA() != 0) {
						internerkurier.decreaseAnzahl_RA();
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
						Notification.show("Anzahl RA -1", Type.TRAY_NOTIFICATION);
						lRATotal.setValue("" + internerkurier.getAnzahl_RA());
					} else {
						Notification.show("Tiefer als 0 nicht möglich", Type.WARNING_MESSAGE);
					}
				}

				if (e.getSource() == bGS) {
					internerkurier.increaseAnzahl_GS();
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("Anzahl GS +1", Type.TRAY_NOTIFICATION);
					lGSTotal.setValue("" + internerkurier.getAnzahl_GS());
				}

				if (e.getSource() == bGSMinus) {
					if (internerkurier.getAnzahl_GS() != 0) {
						internerkurier.decreaseAnzahl_GS();
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
						Notification.show("Anzahl GS -1", Type.TRAY_NOTIFICATION);
						lGSTotal.setValue("" + internerkurier.getAnzahl_GS());
					} else {
						Notification.show("Tiefer als 0 nicht möglich", Type.WARNING_MESSAGE);
					}
				}

			}
		};

	}

}
