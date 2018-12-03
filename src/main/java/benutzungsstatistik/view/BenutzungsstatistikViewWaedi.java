package benutzungsstatistik.view;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Slider;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import allgemein.view.StartseiteView;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Telefonkontakt;

/**
 * View der Benutzungsstatistik. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class BenutzungsstatistikViewWaedi extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-Waedi";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bBenutzerkontakt;
	private Button bIntensivFrage;
	private Slider sIntensivFrageSlider;
//	private Button bRechercheBeratung;
	private Button bEmailkontakt;
	private Button bTelefonkontakt;
	private Button bInternerkurier;
	private Button bKorrektur;
	private Button bTagesuebersicht;
	private Label lText;
	private int uhrzeit;
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	private Benutzungsstatistik benutzungsstatistik;
	private SimpleDateFormat stundenFormat = new SimpleDateFormat("HH");

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

	public BenutzungsstatistikViewWaedi() {
		setCompositionRoot(init());
	}

	private void initData() {
		benutzungsstatistik = new BenutzungsstatistikDatenbank().selectBenutzungsstatistikForDateAndStandort(new Date(),
				StandortEnum.WÄDENSWIL);
		
		uhrzeit = Integer.parseInt(stundenFormat.format(new Date().getTime()));
	}

	// Initialisieren der GUI Komponente
	private void initComponents() {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener());

		lText = new Label();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		lText.setValue("Benutzungsstatistik vom " + sdf.format(benutzungsstatistik.getDatum()));
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bBenutzerkontakt = new Button();
		setBenutzerCaption();
		bBenutzerkontakt.setIcon(VaadinIcons.QUESTION_CIRCLE_O);
		bBenutzerkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP +" iconBenutzungHuge");
		bBenutzerkontakt.addClickListener(createClickListener());

		bIntensivFrage = new Button();
		setIntensivCaption();
		bIntensivFrage.setIcon(VaadinIcons.HOURGLASS);
		bIntensivFrage.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP +" iconBenutzungHuge");
		bIntensivFrage.addClickListener(createClickListener());
		
		sIntensivFrageSlider = new Slider();
		sIntensivFrageSlider.setCaption("Intensive Frage - Dauer in Minuten");
		sIntensivFrageSlider.setMin(1.0);
		sIntensivFrageSlider.setMax(30.0);
		sIntensivFrageSlider.setValue(5.0);
		
//		bRechercheBeratung = new Button();
//		setRechercheCaption();
//		bRechercheBeratung.setIcon(VaadinIcons.GLASSES);
//		bRechercheBeratung.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP +" iconBenutzungHuge");
//		bRechercheBeratung.addClickListener(createClickListener());

		bEmailkontakt = new Button();
		setEmailCaption();
		bEmailkontakt.setIcon(VaadinIcons.ENVELOPE_OPEN_O);
		bEmailkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP +" iconBenutzungHuge");
		bEmailkontakt.addClickListener(createClickListener());

		bTelefonkontakt = new Button();
		setTelefonCaption();
		bTelefonkontakt.setIcon(VaadinIcons.PHONE);
		bTelefonkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP +" iconBenutzungHuge");
		bTelefonkontakt.addClickListener(createClickListener());

		bInternerkurier = new Button();
		bInternerkurier.setCaption("Interner Kurier");
		bInternerkurier.setIcon(VaadinIcons.TRUCK);
		bInternerkurier.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP +" iconBenutzungHuge");
		bInternerkurier.addClickListener(createClickListener());

		bKorrektur = new Button();
		bKorrektur.setCaption("Korrektur");
		bKorrektur.setIcon(VaadinIcons.EDIT);
		bKorrektur.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP +" iconBenutzungHuge");
		bKorrektur.addClickListener(createClickListener());

		bTagesuebersicht = new Button();
		bTagesuebersicht.setCaption("Tagesübersicht");
		bTagesuebersicht.setIcon(VaadinIcons.CLIPBOARD_TEXT);
		bTagesuebersicht.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP +" iconBenutzungHuge");
		bTagesuebersicht.addClickListener(createClickListener());

		GridLayout grid = new GridLayout(3, 10);
		grid.addStyleName("gridlayout");
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0, 2, 0);
		grid.addComponent(bBenutzerkontakt, 0, 1, 0, 3);
		grid.addComponent(bIntensivFrage, 1, 1, 1, 2);
		grid.addComponent(sIntensivFrageSlider, 1, 3, 1, 3);
		grid.addComponent(bTagesuebersicht, 2, 1, 2, 3);
		grid.addComponent(bEmailkontakt, 0, 4, 0, 6);
		grid.addComponent(bTelefonkontakt, 1, 4, 1, 6);
		grid.addComponent(bKorrektur, 2, 4, 2, 6);
		grid.addComponent(bInternerkurier, 0, 7, 0, 9);
//		grid.addComponent(bRechercheBeratung, 1, 7, 1, 9);
		grid.addComponent(new Label(), 1, 7, 1, 9);
		grid.addComponent(new Label(), 2, 7, 2, 9);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row != 0) {
					if(row == 3 && col == 1) {
						//Slider nicht verändern
						c.setWidth("80%");
					}else {
						c.setHeight("80%");
						c.setWidth("80%");
					}
				}
			}
		}
		
		grid.setColumnExpandRatio(0, 0.33f);
		grid.setColumnExpandRatio(1, 0.33f);
		grid.setColumnExpandRatio(2, 0.33f);

		mainLayout.addComponent(grid);
	}

	private void setTelefonCaption() {
		int telefonzaehler = 0;
		for (Telefonkontakt telefon : benutzungsstatistik.getTelefonkontaktListe()) {
			if (Integer.parseInt(stundenFormat.format(telefon.getTimestamp().getTime())) == uhrzeit) {
				telefonzaehler++;
			}
		}
		bTelefonkontakt.setCaption("Telefon, "+uhrzeit +" Uhr: "+telefonzaehler);
	}
	
	private void setEmailCaption() {
		int emailzaehler = 0;
		for (Emailkontakt email : benutzungsstatistik.getEmailkontaktListe()) {
			if (Integer.parseInt(stundenFormat.format(email.getTimestamp().getTime())) == uhrzeit) {
				emailzaehler++;
			}
		}
		bEmailkontakt.setCaption("Email, "+uhrzeit +" Uhr: "+emailzaehler);
	}
	
	private void setBenutzerCaption() {
		int benutzerzaehler = 0;
		for (Benutzerkontakt benutzer : benutzungsstatistik.getBenutzerkontaktListe()) {
			if (Integer.parseInt(stundenFormat.format(benutzer.getTimestamp().getTime())) == uhrzeit) {
				benutzerzaehler++;
			}
		}
		bBenutzerkontakt.setCaption("Benutzerkontakt, "+uhrzeit +" Uhr: "+benutzerzaehler);
	}
	
	private void setIntensivCaption() {
		int intensivzaehler = 0;
		for (Intensivfrage intensiv : benutzungsstatistik.getIntensivfrageListe()) {
			if (Integer.parseInt(stundenFormat.format(intensiv.getTimestamp().getTime())) == uhrzeit) {
				intensivzaehler++;
			}
		}
		bIntensivFrage.setCaption("Intensivfrage, "+uhrzeit +" Uhr: "+intensivzaehler);
	}	
	
//	private void setRechercheCaption() {
//		benutzungsstatistik.getAnzahl_Rechercheberatung();
//		bRechercheBeratung.setCaption("Rechercheberatung, Total: "+benutzungsstatistik.getAnzahl_Rechercheberatung());
//	}	
	

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					getUI().getNavigator().navigateTo(StartseiteView.NAME);
				}

				if (e.getSource() == bBenutzerkontakt) {
					benutzungsstatistik.addBenutzerkontakt(
							new Benutzerkontakt(new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("+1 Benutzerkontakt", Type.TRAY_NOTIFICATION);
					
					setBenutzerCaption();
				}

				if (e.getSource() == bIntensivFrage) {
					int zaehler = 0;
					int slider = sIntensivFrageSlider.getValue().intValue();
					for(int i = 1; i<=slider; i+=5) {
						benutzungsstatistik.addIntensivfrage(
								new Intensivfrage(new Timestamp(new Date().getTime()), benutzungsstatistik));
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
						zaehler++;
					}
					Notification.show("+ "+zaehler +" Intensivfrage", Type.TRAY_NOTIFICATION);
					
					benutzungsstatistik.addBenutzerkontakt(
							new Benutzerkontakt(new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					
					setIntensivCaption();
					setBenutzerCaption();
				}

//				if (e.getSource() == bRechercheBeratung) {
//					benutzungsstatistik
//							.setAnzahl_Rechercheberatung(benutzungsstatistik.getAnzahl_Rechercheberatung() + 1);
//					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
//					Notification.show("+1 Rechercheberatung", Type.TRAY_NOTIFICATION);
//					
//					setRechercheCaption();
//				}

				if (e.getSource() == bEmailkontakt) {
					benutzungsstatistik.addEmailkontakt(
							new Emailkontakt(new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);					
					Notification.show("+1 Emailkontakt", Type.TRAY_NOTIFICATION);
					
					setEmailCaption();
				}

				if (e.getSource() == bTelefonkontakt) {
					benutzungsstatistik.addTelefonkontakt(
							new Telefonkontakt(new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("+1 Telefonkontakt", Type.TRAY_NOTIFICATION);
					
					setTelefonCaption();
				}

				if (e.getSource() == bInternerkurier) {
					getUI().getNavigator().navigateTo(InternerkurierViewWaedi.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID() + '/' + false);
				}

				if (e.getSource() == bKorrektur) {
					getUI().getNavigator().navigateTo(KorrekturViewWaedi.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
				}

				if (e.getSource() == bTagesuebersicht) {
					getUI().getNavigator().navigateTo(TagesübersichtBenutzungViewWaedi.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
				}

			}
		};

	}

}
