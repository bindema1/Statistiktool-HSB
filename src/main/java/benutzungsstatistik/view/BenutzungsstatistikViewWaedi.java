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
import com.vaadin.ui.HorizontalLayout;
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
	private Button bRefresh;
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

	/**
	 * Bildet das AbsoluteLayout, als Wrapper um die ganze View
	 * 
	 * @return AbsoluteLayout
	 */
	private AbsoluteLayout buildMainLayout() {
		mainLayout = new AbsoluteLayout();
		mainLayout.addStyleName("backgroundErfassung");
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
		initData();
		initComponents();

		return absolutLayout;
	}

	public BenutzungsstatistikViewWaedi() {
		setCompositionRoot(init());
	}

	/**
	 * Holt die akutelle Benutzungsstatistik und setzt die Uhrzeit
	 */
	private void initData() {
		benutzungsstatistik = new BenutzungsstatistikDatenbank().selectBenutzungsstatistikForDateAndStandort(new Date(),
				StandortEnum.WÄDENSWIL);

		uhrzeit = Integer.parseInt(stundenFormat.format(new Date().getTime()));
	}

	/**
	 * Initialisieren der GUI Komponente. Fügt alle Komponenten dem Layout hinzu
	 */
	private void initComponents() {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener());
		
		bRefresh = new Button();
		bRefresh.setCaption("Refresh");
		bRefresh.setIcon(VaadinIcons.REFRESH);
		bRefresh.addClickListener(createClickListener());

		lText = new Label();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		lText.setValue("Benutzungsstatistik vom " + sdf.format(benutzungsstatistik.getDatum()));
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bBenutzerkontakt = new Button();
		setBenutzerCaption();
		bBenutzerkontakt.setIcon(VaadinIcons.QUESTION_CIRCLE_O);
		bBenutzerkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBenutzerkontakt.addClickListener(createClickListener());

		bIntensivFrage = new Button();
		setIntensivCaption();
		bIntensivFrage.setIcon(VaadinIcons.HOURGLASS);
		bIntensivFrage.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
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
		bEmailkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bEmailkontakt.addClickListener(createClickListener());

		bTelefonkontakt = new Button();
		setTelefonCaption();
		bTelefonkontakt.setIcon(VaadinIcons.PHONE);
		bTelefonkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bTelefonkontakt.addClickListener(createClickListener());

		bInternerkurier = new Button();
		bInternerkurier.setCaption("Interner Kurier");
		bInternerkurier.setIcon(VaadinIcons.TRUCK);
		bInternerkurier.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bInternerkurier.addClickListener(createClickListener());

		bKorrektur = new Button();
		bKorrektur.setCaption("Korrektur");
		bKorrektur.setIcon(VaadinIcons.EDIT);
		bKorrektur.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bKorrektur.addClickListener(createClickListener());

		bTagesuebersicht = new Button();
		bTagesuebersicht.setCaption("Tagesübersicht");
		bTagesuebersicht.setIcon(VaadinIcons.CLIPBOARD_TEXT);
		bTagesuebersicht.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bTagesuebersicht.addClickListener(createClickListener());

		GridLayout grid = new GridLayout(6, 10);
		grid.setSizeFull();
		grid.addComponent(new HorizontalLayout(bZurueck), 0, 0);
		grid.addComponent(bRefresh, 1, 0);
		grid.addComponent(lText, 2, 0, 5, 0);
		grid.addComponent(bBenutzerkontakt, 0, 1, 1, 3);
		grid.addComponent(createSliderGridLayout(), 2, 1, 3, 3);
		grid.addComponent(bTagesuebersicht, 4, 1, 5, 3);
		grid.addComponent(bEmailkontakt, 0, 4, 1, 6);
		grid.addComponent(bTelefonkontakt, 2, 4, 3, 6);
		grid.addComponent(bKorrektur, 4, 4, 5, 6);
		grid.addComponent(bInternerkurier, 0, 7, 1, 9);
//		grid.addComponent(bRechercheBeratung, 1, 7, 1, 9);
		grid.addComponent(new Label(), 2, 7, 3, 9);
		grid.addComponent(new Label(), 4, 7, 5, 9);

		grid.setColumnExpandRatio(0, 0.1666f);
		grid.setColumnExpandRatio(1, 0.1666f);
		grid.setColumnExpandRatio(2, 0.1666f);
		grid.setColumnExpandRatio(3, 0.1666f);
		grid.setColumnExpandRatio(4, 0.1666f);
		grid.setColumnExpandRatio(5, 0.1666f);
		grid.setRowExpandRatio(0, 0.1f);
		grid.setRowExpandRatio(1, 0.1f);
		grid.setRowExpandRatio(2, 0.1f);
		grid.setRowExpandRatio(3, 0.1f);
		grid.setRowExpandRatio(4, 0.1f);
		grid.setRowExpandRatio(5, 0.1f);
		grid.setRowExpandRatio(6, 0.1f);
		grid.setRowExpandRatio(7, 0.1f);
		grid.setRowExpandRatio(8, 0.1f);
		grid.setRowExpandRatio(9, 0.1f);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row != 0) {
					if (row == 3 && col == 1) {
						// Slider nicht verändern
						c.setWidth("80%");
					} else {
						c.setHeight("80%");
						c.setWidth("80%");
					}
				}
			}
		}

		mainLayout.addComponent(grid);
	}

	/**
	 * Erstellt das GridLayout für den Slider
	 * 
	 * @return GridLayout
	 */
	private GridLayout createSliderGridLayout() {
		GridLayout sliderLayout = new GridLayout(1, 2);
		sliderLayout.setSizeFull();
		sliderLayout.addComponent(bIntensivFrage, 0, 0);
		sliderLayout.addComponent(sIntensivFrageSlider, 0, 1);
		for (int col = 0; col < sliderLayout.getColumns(); col++) {
			for (int row = 0; row < sliderLayout.getRows(); row++) {
				Component c = sliderLayout.getComponent(col, row);

				// Button grösser machen
				c.setWidth("100%");

				if (row == 0) {
					c.setHeight("100%");
					sliderLayout.setComponentAlignment(c, Alignment.TOP_CENTER);
				} else {
					c.setHeight("30%");
					sliderLayout.setComponentAlignment(c, Alignment.BOTTOM_CENTER);
				}
			}
		}
		return sliderLayout;
	}

	/**
	 * Erstellt die Caption für einen Button
	 */
	private void setTelefonCaption() {
		int telefonzaehler = 0;
		for (Telefonkontakt telefon : benutzungsstatistik.getTelefonkontaktListe()) {
			if (Integer.parseInt(stundenFormat.format(telefon.getTimestamp().getTime())) == uhrzeit) {
				telefonzaehler++;
			}
		}
		bTelefonkontakt.setCaptionAsHtml(true);
		bTelefonkontakt.setCaption("Telefon <br> " + uhrzeit + " Uhr: " + telefonzaehler);
	}

	/**
	 * Erstellt die Caption für einen Button
	 */
	private void setEmailCaption() {
		int emailzaehler = 0;
		for (Emailkontakt email : benutzungsstatistik.getEmailkontaktListe()) {
			if (Integer.parseInt(stundenFormat.format(email.getTimestamp().getTime())) == uhrzeit) {
				emailzaehler++;
			}
		}
		bEmailkontakt.setCaptionAsHtml(true);
		bEmailkontakt.setCaption("Email <br> " + uhrzeit + " Uhr: " + emailzaehler);
	}

	/**
	 * Erstellt die Caption für einen Button
	 */
	private void setBenutzerCaption() {
		int benutzerzaehler = 0;
		for (Benutzerkontakt benutzer : benutzungsstatistik.getBenutzerkontaktListe()) {
			if (Integer.parseInt(stundenFormat.format(benutzer.getTimestamp().getTime())) == uhrzeit) {
				benutzerzaehler++;
			}
		}
		bBenutzerkontakt.setCaptionAsHtml(true);
		bBenutzerkontakt.setCaption("Benutzerkontakt <br> " + uhrzeit + " Uhr: " + benutzerzaehler);
	}

	/**
	 * Erstellt die Caption für einen Button
	 */
	private void setIntensivCaption() {
		int intensivzaehler = 0;
		for (Intensivfrage intensiv : benutzungsstatistik.getIntensivfrageListe()) {
			if (Integer.parseInt(stundenFormat.format(intensiv.getTimestamp().getTime())) == uhrzeit) {
				intensivzaehler++;
			}
		}
		bIntensivFrage.setCaption("Intensivfrage, " + uhrzeit + " Uhr: " + intensivzaehler);
	}

//	private void setRechercheCaption() {
//		benutzungsstatistik.getAnzahl_Rechercheberatung();
//		bRechercheBeratung.setCaptionAsHtml(true);
//		bRechercheBeratung.setCaption("Rechercheberatung <br> Total: "+benutzungsstatistik.getAnzahl_Rechercheberatung());
//	}	

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					getUI().getNavigator().navigateTo(StartseiteView.NAME);
				}
				
				if (e.getSource() == bRefresh) {
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
				}

				if (e.getSource() == bBenutzerkontakt) {
					// Erstellt einen Eintrag in der Datenbank
					benutzungsstatistik.addBenutzerkontakt(
							new Benutzerkontakt(new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("+1 Benutzerkontakt", Type.TRAY_NOTIFICATION);

					// Aktualisiert die Webseite (für das synchrone Arbeiten)
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
				}

				if (e.getSource() == bIntensivFrage) {
					// Erstellt einen Eintrag in der Datenbank
					int zaehler = 0;
					int slider = sIntensivFrageSlider.getValue().intValue();
					// Pro 5 Minuten gibt es einen Eintrag als Intensivfrage
					for (int i = 1; i <= slider; i += 5) {
						benutzungsstatistik.addIntensivfrage(
								new Intensivfrage(new Timestamp(new Date().getTime()), benutzungsstatistik));
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
						zaehler++;
					}
					Notification.show("+ " + zaehler + " Intensivfrage", Type.TRAY_NOTIFICATION);

					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					// Aktualisiert die Webseite (für das synchrone Arbeiten)
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
				}

//				if (e.getSource() == bRechercheBeratung) {
					// Erstellt einen Eintrag in der Datenbank
//					benutzungsstatistik
//							.setAnzahl_Rechercheberatung(benutzungsstatistik.getAnzahl_Rechercheberatung() + 1);
//					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
//					Notification.show("+1 Rechercheberatung", Type.TRAY_NOTIFICATION);
//					
//					// Aktualisiert die Webseite (für das synchrone Arbeiten)
//					getUI().getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
//				}

				if (e.getSource() == bEmailkontakt) {
					// Erstellt einen Eintrag in der Datenbank
					benutzungsstatistik.addEmailkontakt(
							new Emailkontakt(new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("+1 Emailkontakt", Type.TRAY_NOTIFICATION);

					// Aktualisiert die Webseite (für das synchrone Arbeiten)
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
				}

				if (e.getSource() == bTelefonkontakt) {
					// Erstellt einen Eintrag in der Datenbank
					benutzungsstatistik.addTelefonkontakt(
							new Telefonkontakt(new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("+1 Telefonkontakt", Type.TRAY_NOTIFICATION);

					// Aktualisiert die Webseite (für das synchrone Arbeiten)
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
				}

				if (e.getSource() == bInternerkurier) {
					getUI().getNavigator().navigateTo(InternerkurierViewWaedi.NAME + '/'
							+ benutzungsstatistik.getBenutzungsstatistik_ID() + '/' + false);
				}

				if (e.getSource() == bKorrektur) {
					getUI().getNavigator().navigateTo(
							KorrekturViewWaedi.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
				}

				if (e.getSource() == bTagesuebersicht) {
					getUI().getNavigator().navigateTo(TagesübersichtBenutzungViewWaedi.NAME + '/'
							+ benutzungsstatistik.getBenutzungsstatistik_ID() + '/' + false);
				}

			}
		};

	}

}
