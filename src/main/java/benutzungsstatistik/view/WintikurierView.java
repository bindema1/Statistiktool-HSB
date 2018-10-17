package benutzungsstatistik.view;

import java.time.LocalDateTime;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.view.MainView;
import benutzungsstatistik.db.WintikurierDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Wintikurier;

/**
 * View des Wintikuriers. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class WintikurierView {

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
	private DateTimeField datefield;
	private WintikurierDatenbank wintikurierDB = new WintikurierDatenbank();
	private Wintikurier wintikurier;
	private Benutzungsstatistik benutzungsstatistik;

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		return mainLayout;
	}

	public AbsoluteLayout init(MainView mainView) {

		AbsoluteLayout absolutLayout = buildMainLayout();
		initData();
		initComponents(mainView);

		return absolutLayout;
	}

	public WintikurierView(Benutzungsstatistik benutzungsstatistik) {
		this.benutzungsstatistik = benutzungsstatistik;
		wintikurier = benutzungsstatistik.getWintikurier();
	}

	private void initData() {

	}

	// Initialisieren der GUI Komponente
	private void initComponents(MainView mainView) {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener(mainView));

		lText = new Label();
		lText.setValue("Benutzungsstatistik vom ");
		lText.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		datefield = new DateTimeField();
//		datefield.setValue(LocalDateTime.ofInstant(benutzungsstatistik.getDatum().toInstant(), ZoneId.systemDefault()));
		datefield.setValue(LocalDateTime.now());
		datefield.setDateFormat("dd.MM.yyyy");
		datefield.addValueChangeListener(event -> {
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
			
//			BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
//			ZonedDateTime zdt = event.getValue().atZone(ZoneId.systemDefault());
//			Date date = Date.from(zdt.toInstant());
//			Benutzungsstatistik benutzungsstatistikNeu = benutzungsstatistikDB.selectBenutzungsstatistikForDateAndStandort(date, benutzungsstatistik.getStandort());
//			benutzungsstatistik = benutzungsstatistikNeu;
		});
		
		lTotal = new Label();
		lTotal.setValue("Total");
		lTotal.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		lPlatzhalter = new Label();
		lPlatzhalter.setValue("");
		
		bGesundheit = new Button();
		bGesundheit.setCaption("Gesundheit");
		bGesundheit.addStyleName(ValoTheme.BUTTON_LARGE);
		bGesundheit.addClickListener(createClickListener(mainView));

		lGesundheitTotal = new Label();
		lGesundheitTotal.setValue("" +wintikurier.getAnzahl_Gesundheit());
		lGesundheitTotal.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		bGesundheitMinus = new Button();
		bGesundheitMinus.setCaption("Korrektur -1");
		bGesundheitMinus.addStyleName(ValoTheme.BUTTON_LARGE +" " +ValoTheme.BUTTON_DANGER);
		bGesundheitMinus.addClickListener(createClickListener(mainView));
		
		bLinguistik = new Button();
		bLinguistik.setCaption("Linguistik");
		bLinguistik.addStyleName(ValoTheme.BUTTON_LARGE);
		bLinguistik.addClickListener(createClickListener(mainView));
		
		lLinguistikTotal = new Label();
		lLinguistikTotal.setValue("" +wintikurier.getAnzahl_Linguistik());
		lLinguistikTotal.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		bLinguistikMinus = new Button();
		bLinguistikMinus.setCaption("Korrektur -1");
		bLinguistikMinus.addStyleName(ValoTheme.BUTTON_LARGE  +" " +ValoTheme.BUTTON_DANGER);
		bLinguistikMinus.addClickListener(createClickListener(mainView));
		
		bTechnik = new Button();
		bTechnik.setCaption(" Technik  ");
		bTechnik.addStyleName(ValoTheme.BUTTON_LARGE);
		bTechnik.addClickListener(createClickListener(mainView));
		
		lTechnikTotal = new Label();
		lTechnikTotal.setValue("" +wintikurier.getAnzahl_Technik());
		lTechnikTotal.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		bTechnikMinus = new Button();
		bTechnikMinus.setCaption("Korrektur -1");
		bTechnikMinus.addStyleName(ValoTheme.BUTTON_LARGE +" " +ValoTheme.BUTTON_DANGER);
		bTechnikMinus.addClickListener(createClickListener(mainView));
		
		bWirtschaft = new Button();
		bWirtschaft.setCaption("Wirtschaft");
		bWirtschaft.addStyleName(ValoTheme.BUTTON_LARGE);
		bWirtschaft.addClickListener(createClickListener(mainView));
		
		lWirtschaftTotal = new Label();
		lWirtschaftTotal.setValue("" +wintikurier.getAnzahl_Wirtschaft());
		lWirtschaftTotal.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		bWirtschaftMinus = new Button();
		bWirtschaftMinus.setCaption("Korrektur -1");
		bWirtschaftMinus.addStyleName(ValoTheme.BUTTON_LARGE +" " +ValoTheme.BUTTON_DANGER);
		bWirtschaftMinus.addClickListener(createClickListener(mainView));


//		VerticalLayout overallLayout = new VerticalLayout();
//		overallLayout.setSpacing(true);
//		
//		HorizontalLayout headerLayout = new HorizontalLayout();
//		headerLayout.setWidth("100%");
//		headerLayout.addComponent(bZurueck);
//		headerLayout.addComponent(lText);
//		headerLayout.addComponent(datefield);
//		overallLayout.addComponent(headerLayout);
//		
//		HorizontalLayout buttonLayout = new HorizontalLayout();
//		buttonLayout.setHeight("200px");
//		buttonLayout.setWidth("100%");
//		buttonLayout.addComponent(bGesundheit);
//		buttonLayout.addComponent(bLinguistik);
//		buttonLayout.addComponent(bTechnik);
//		buttonLayout.addComponent(bWirtschaft);
//		overallLayout.addComponent(buttonLayout);
//		
//		overallLayout.addComponent(lTotal);
//		
//		HorizontalLayout totalLayout = new HorizontalLayout();
//		totalLayout.addComponent(lGesundheitTotal);
//		totalLayout.addComponent(lLinguistikTotal);
//		totalLayout.addComponent(lTechnikTotal);
//		totalLayout.addComponent(lWirtschaftTotal);
//		overallLayout.addComponent(totalLayout);
//		
//		HorizontalLayout korrekturLayout = new HorizontalLayout();
//		korrekturLayout.addComponent(bGesundheitMinus);
//		korrekturLayout.addComponent(bLinguistikMinus);
//		korrekturLayout.addComponent(bTechnikMinus);
//		korrekturLayout.addComponent(bWirtschaftMinus);
//		overallLayout.addComponent(korrekturLayout);
//		
//		mainLayout.addComponent(overallLayout);
		
		GridLayout grid = new GridLayout(8, 7);
		grid.addStyleName("gridlayout");
		grid.setWidth("100%");
		grid.setHeight("90%");
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0, 3, 0);
		grid.addComponent(datefield, 4, 0, 7, 0);
		grid.addComponent(bGesundheit, 0, 1, 1, 4);
		grid.addComponent(bLinguistik, 2, 1, 3, 4);
		grid.addComponent(bTechnik, 4, 1, 5, 4);
		grid.addComponent(bWirtschaft, 6, 1, 7, 4);
//		grid.addComponent(lTotal, 0, 4, 1, 4);
//		grid.addComponent(lPlatzhalter, 2, 4, 7, 4);
		grid.addComponent(lGesundheitTotal, 0, 5, 1, 5);
		grid.addComponent(lLinguistikTotal, 2, 5, 3, 5);
		grid.addComponent(lTechnikTotal, 4, 5, 5, 5);
		grid.addComponent(lWirtschaftTotal, 6, 5, 7, 5);
		grid.addComponent(bGesundheitMinus, 0, 6, 1, 6);
		grid.addComponent(bLinguistikMinus, 2, 6, 3, 6);
		grid.addComponent(bTechnikMinus, 4, 6, 5, 6);
		grid.addComponent(bWirtschaftMinus, 6, 6, 7, 6);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);
				
				// Button grösser machen
				if (row == 0) {
					if(col == 1 || col == 2 || col == 3) {
						grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
					}
				}else if (row == 5){
					//Zeile 5 sind label, dort darf die Grösse nicht verändert werden, da das Alignment sonst nicht mehr funktioniert
				}else if (row >= 1 && row <= 4){
					c.setHeight("100%");
					c.setWidth("90%");
				}else{
					c.setHeight("80%");
					c.setWidth("80%");
				}
				
			}
		}

		mainLayout.addComponent(grid);

	}

	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					mainView.setContent(new BenutzungsstatistikView().init(mainView));
				}
				
				if (e.getSource() == bGesundheit) {
					wintikurier.increaseAnzahl_Gesundheit();
					wintikurierDB.updateWintikurier(wintikurier);
					Notification.show("Anzahl Gesundheit +1", Type.TRAY_NOTIFICATION);
					lGesundheitTotal.setValue(""+wintikurier.getAnzahl_Gesundheit());
				}
				
				if (e.getSource() == bGesundheitMinus) {
					if(wintikurier.getAnzahl_Gesundheit() != 0) {
						wintikurier.decreaseAnzahl_Gesundheit();
						wintikurierDB.updateWintikurier(wintikurier);
						Notification.show("Anzahl Gesundheit -1", Type.TRAY_NOTIFICATION);
						lGesundheitTotal.setValue(""+wintikurier.getAnzahl_Gesundheit());
					}else {
						Notification.show("Tiefer als 0 nicht möglich", Type.WARNING_MESSAGE);
					}
				}
				
				if (e.getSource() == bLinguistik) {
					wintikurier.increaseAnzahl_Linguistik();
					wintikurierDB.updateWintikurier(wintikurier);
					Notification.show("Anzahl Linguistik +1", Type.TRAY_NOTIFICATION);
					lLinguistikTotal.setValue(""+wintikurier.getAnzahl_Linguistik());
				}
				
				if (e.getSource() == bLinguistikMinus) {
					if(wintikurier.getAnzahl_Linguistik() != 0) {
						wintikurier.decreaseAnzahl_Linguistik();
						wintikurierDB.updateWintikurier(wintikurier);
						Notification.show("Anzahl Linguistik -1", Type.TRAY_NOTIFICATION);
						lLinguistikTotal.setValue(""+wintikurier.getAnzahl_Linguistik());
					}else {
						Notification.show("Tiefer als 0 nicht möglich", Type.WARNING_MESSAGE);
					}
				}
				
				if (e.getSource() == bTechnik) {
					wintikurier.increaseAnzahl_Technik();
					wintikurierDB.updateWintikurier(wintikurier);
					Notification.show("Anzahl Technik +1", Type.TRAY_NOTIFICATION);
					lTechnikTotal.setValue(""+wintikurier.getAnzahl_Technik());
				}
				
				if (e.getSource() == bTechnikMinus) {
					if(wintikurier.getAnzahl_Technik() != 0) {
						wintikurier.decreaseAnzahl_Technik();
						wintikurierDB.updateWintikurier(wintikurier);
						Notification.show("Anzahl Technik -1", Type.TRAY_NOTIFICATION);
						lTechnikTotal.setValue(""+wintikurier.getAnzahl_Technik());
					}else {
						Notification.show("Tiefer als 0 nicht möglich", Type.WARNING_MESSAGE);
					}
				}
				
				if (e.getSource() == bWirtschaft) {
					wintikurier.increaseAnzahl_Wirtschaft();
					wintikurierDB.updateWintikurier(wintikurier);
					Notification.show("Anzahl Wirtschaft +1", Type.TRAY_NOTIFICATION);
					lWirtschaftTotal.setValue(""+wintikurier.getAnzahl_Wirtschaft());
				}
				
				if (e.getSource() == bWirtschaftMinus) {
					if(wintikurier.getAnzahl_Wirtschaft() != 0) {
						wintikurier.decreaseAnzahl_Wirtschaft();
						wintikurierDB.updateWintikurier(wintikurier);
						Notification.show("Anzahl Wirtschaft -1", Type.TRAY_NOTIFICATION);
						lWirtschaftTotal.setValue(""+wintikurier.getAnzahl_Wirtschaft());
					}else {
						Notification.show("Tiefer als 0 nicht möglich", Type.WARNING_MESSAGE);
					}
				}

			}
		};

	}

}
