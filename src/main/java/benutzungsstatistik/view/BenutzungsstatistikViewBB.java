package benutzungsstatistik.view;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.vaadin.teemu.switchui.Switch;

import com.vaadin.annotations.Theme;
import com.vaadin.data.HasValue;
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
import com.vaadin.ui.NativeButton;
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
public class BenutzungsstatistikViewBB extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-BB";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bBenutzerkontakt;
	private Button bIntensivFrage;
	private int slider;
	private Button bRechercheBeratung;
	private Button bEmailkontakt;
	private Button bTelefonkontakt;
	private Button bWintikurier;
	private Button bExterneGruppe;
	private Button bKorrektur;
	private Button bTagesuebersicht;
	private Switch kassenbeleg;
	private Label lText;
	private Label lKassenbeleg;
	private int uhrzeit;
//	private EmailkontaktDatenbank emailKontaktDB = new EmailkontaktDatenbank();
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
//	private IntensivfrageDatenbank intensivFrageDB = new IntensivfrageDatenbank();
//	private BenutzerkontaktDatenbank benutzerKontaktDB = new BenutzerkontaktDatenbank();
//	private TelefonkontaktDatenbank telefonKontaktDB = new TelefonkontaktDatenbank();
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

	public BenutzungsstatistikViewBB() {
		setCompositionRoot(init());
	}

	private void initData() {
		benutzungsstatistik = new BenutzungsstatistikDatenbank().selectBenutzungsstatistikForDateAndStandort(new Date(),
				StandortEnum.WINTERTHUR_BB);
		
		uhrzeit = Integer.parseInt(stundenFormat.format(new Date().getTime()));
	}

	// Initialisieren der GUI Komponente
	private void initComponents() {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
//		bZurueck.setEnabled(false);
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener());

		lText = new Label();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		lText.setValue("Benutzungsstatistik vom " + sdf.format(benutzungsstatistik.getDatum()));
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		lKassenbeleg = new Label();
		lKassenbeleg.setValue("Kassenbeleg");
		lKassenbeleg.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

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
		
		Slider sIntensivFrageSlider = new Slider();
		sIntensivFrageSlider.setCaption("Intensive Frage - Dauer in Minuten");
		sIntensivFrageSlider.setMin(1.0);
		sIntensivFrageSlider.setMax(30.0);
		sIntensivFrageSlider.setValue(5.0);
		sIntensivFrageSlider.addValueChangeListener(event -> {
			slider = event.getValue().intValue();
		});
              

		bRechercheBeratung = new Button();
		setRechercheCaption();
		bRechercheBeratung.setIcon(VaadinIcons.GLASSES);
		bRechercheBeratung.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP +" iconBenutzungHuge");
		bRechercheBeratung.addClickListener(createClickListener());

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

		bWintikurier = new Button();
		bWintikurier.setCaption("Wintikurier");
		bWintikurier.setIcon(VaadinIcons.TRUCK);
		bWintikurier.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP +" iconBenutzungHuge");
		bWintikurier.addClickListener(createClickListener());

		bExterneGruppe = new Button();
		bExterneGruppe.setCaption("Externe Gruppe");
		bExterneGruppe.setIcon(VaadinIcons.GROUP);
		bExterneGruppe.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP +" iconBenutzungHuge");
		bExterneGruppe.addClickListener(createClickListener());

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

		kassenbeleg = new Switch();
		if (benutzungsstatistik.isKassenbeleg()) {
			kassenbeleg.setValue(true);
		} else {
			kassenbeleg.setValue(false);
		}
		kassenbeleg.addValueChangeListener((HasValue.ValueChangeEvent<Boolean> event) -> {
			Boolean item = event.getValue();

			benutzungsstatistik.setKassenbeleg(item);
			benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

			if(item == true) {
				Notification.show("Kassenbeleg muss erstellt werden", Type.TRAY_NOTIFICATION);
			}else {
				Notification.show("Kassenbeleg wurde erstellt", Type.TRAY_NOTIFICATION);
			}
		});

		GridLayout grid = new GridLayout(9, 10);
		grid.addStyleName("gridlayout");
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0, 1, 0);
		grid.addComponent(lText, 2, 0, 5, 0);
		grid.addComponent(lKassenbeleg, 6, 0);
		grid.addComponent(kassenbeleg, 7, 0, 8, 0);
		grid.addComponent(bBenutzerkontakt, 0, 1, 2, 3);
		grid.addComponent(bIntensivFrage, 3, 1, 5, 2);
		grid.addComponent(sIntensivFrageSlider, 3, 3, 5, 3);
		grid.addComponent(bTagesuebersicht, 6, 1, 8, 3);
		grid.addComponent(bEmailkontakt, 0, 4, 2, 6);
		grid.addComponent(bTelefonkontakt, 3, 4, 5, 6);
		grid.addComponent(bRechercheBeratung, 6, 4, 8, 6);
		grid.addComponent(bExterneGruppe, 0, 7, 2, 9);
		grid.addComponent(bWintikurier, 3, 7, 5, 9);
		grid.addComponent(bKorrektur, 6, 7, 8, 9);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row != 0) {
					if(row == 3 && col == 3 || row == 3 && col == 4 || row == 3 && col == 5) {
						//Slider nicht verändern
						c.setWidth("80%");
					}else {
						c.setHeight("80%");
						c.setWidth("80%");
					}
				}
			}
		}

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
	
	private void setRechercheCaption() {
		benutzungsstatistik.getAnzahl_Rechercheberatung();
		bRechercheBeratung.setCaption("Rechercheberatung, Total: "+benutzungsstatistik.getAnzahl_Rechercheberatung());
	}	
	

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

				if (e.getSource() == bRechercheBeratung) {
					benutzungsstatistik
							.setAnzahl_Rechercheberatung(benutzungsstatistik.getAnzahl_Rechercheberatung() + 1);
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("+1 Rechercheberatung", Type.TRAY_NOTIFICATION);
					
					setRechercheCaption();
				}

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

				if (e.getSource() == bWintikurier) {
					getUI().getNavigator().navigateTo(WintikurierView.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID() + '/' + false);
				}

				if (e.getSource() == bExterneGruppe) {
					getUI().getNavigator().navigateTo(ExterneGruppeView.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID() + '/' + false);
				}

				if (e.getSource() == bKorrektur) {
					getUI().getNavigator().navigateTo(KorrekturView.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
				}

				if (e.getSource() == bTagesuebersicht) {
					getUI().getNavigator().navigateTo(TagesübersichtBenutzungView.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
				}

			}
		};

	}

}
