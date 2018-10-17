package benutzungsstatistik.view;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.vaadin.teemu.switchui.Switch;

import com.vaadin.annotations.Theme;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import Testdaten.TestDaten;
import allgemein.db.StandortDatenbank;
import allgemein.model.Angestellter;
import allgemein.model.Standort;
import allgemein.view.MainView;
import benutzungsstatistik.db.BeantwortungBibliothekspersonalDatenbank;
import benutzungsstatistik.db.EmailkontaktDatenbank;
import benutzungsstatistik.db.ExterneGruppeDatenbank;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.db.IntensivfrageDatenbank;
import benutzungsstatistik.db.BenutzerkontaktDatenbank;
import benutzungsstatistik.db.TelefonkontaktDatenbank;
import benutzungsstatistik.db.WintikurierDatenbank;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Telefonkontakt;

/**
 * View der Benutzungsstatistik. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class BenutzungsstatistikView {

	private AbsoluteLayout mainLayout;
//	private Angestellter angestellter;
	private Button bZurueck;
	private Button bBenutzerkontakt;
	private Button bIntensivFrage;
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
	StandortDatenbank standortDB = new StandortDatenbank();
	EmailkontaktDatenbank emailKontaktDB = new EmailkontaktDatenbank();
	ExterneGruppeDatenbank externeGruppeDB = new ExterneGruppeDatenbank();
	BeantwortungBibliothekspersonalDatenbank beantwortungBibliothekspersonalDB = new BeantwortungBibliothekspersonalDatenbank();
	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	IntensivfrageDatenbank intensivFrageDB = new IntensivfrageDatenbank();
	BenutzerkontaktDatenbank benutzerKontaktDB = new BenutzerkontaktDatenbank();
	TelefonkontaktDatenbank telefonKontaktDB = new TelefonkontaktDatenbank();
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

	public BenutzungsstatistikView() {

	}

	private void initData() {
		TestDaten t = new TestDaten();
//		List<Standort> standortListe = standortDB.selectAllStandorte();
//		Standort standort = null;
//		for(Standort s : standortListe) {
//			if(s.getName().equals("Winterthur BB")) {
//				standort = s;
//			}
//		}
//		
//		benutzungsstatistik = benutzungsstatistikDB.selectBenutzungsstatistikForDateAndStandort(new Date(), standort);
		benutzungsstatistik = benutzungsstatistikDB.selectAllBenutzungsstatistiken().get(0);
	}

	// Initialisieren der GUI Komponente
	private void initComponents(MainView mainView) {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener(mainView));

		lText = new Label();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		lText.setValue("Benutzungsstatistik vom " + sdf.format(benutzungsstatistik.getDatum()));
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		lKassenbeleg = new Label();
		lKassenbeleg.setValue("Kassenbeleg");
		lKassenbeleg.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bBenutzerkontakt = new Button();
		bBenutzerkontakt.setCaption("Benutzerkontakt");
		bBenutzerkontakt.setIcon(VaadinIcons.QUESTION_CIRCLE_O);
		bBenutzerkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bBenutzerkontakt.addClickListener(createClickListener(mainView));

		bIntensivFrage = new Button();
		bIntensivFrage.setCaption("Intensiv Frage");
		bIntensivFrage.setIcon(VaadinIcons.HOURGLASS);
		bIntensivFrage.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bIntensivFrage.addClickListener(createClickListener(mainView));

		bRechercheBeratung = new Button();
		bRechercheBeratung.setCaption("Recherche-Beratung");
		bRechercheBeratung.setIcon(VaadinIcons.GLASSES);
		bRechercheBeratung.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bRechercheBeratung.addClickListener(createClickListener(mainView));

		bEmailkontakt = new Button();
		bEmailkontakt.setCaption("Email");
		bEmailkontakt.setIcon(VaadinIcons.ENVELOPE_OPEN_O);
		bEmailkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bEmailkontakt.addClickListener(createClickListener(mainView));

		bTelefonkontakt = new Button();
		bTelefonkontakt.setCaption("Telefon");
		bTelefonkontakt.setIcon(VaadinIcons.PHONE);
		bTelefonkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bTelefonkontakt.addClickListener(createClickListener(mainView));

		bWintikurier = new Button();
		bWintikurier.setCaption("Wintikurier");
		bWintikurier.setIcon(VaadinIcons.TRUCK);
		bWintikurier.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bWintikurier.addClickListener(createClickListener(mainView));

		bExterneGruppe = new Button();
		bExterneGruppe.setCaption("Externe Gruppe");
		bExterneGruppe.setIcon(VaadinIcons.GROUP);
		bExterneGruppe.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bExterneGruppe.addClickListener(createClickListener(mainView));

		bKorrektur = new Button();
		bKorrektur.setCaption("Korrektur");
		bKorrektur.setIcon(VaadinIcons.EDIT);
		bKorrektur.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bKorrektur.addClickListener(createClickListener(mainView));

		bTagesuebersicht = new Button();
		bTagesuebersicht.setCaption("Tagesübersicht");
		bTagesuebersicht.setIcon(VaadinIcons.CLIPBOARD_TEXT);
		bTagesuebersicht.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bTagesuebersicht.addClickListener(createClickListener(mainView));

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

			List<Benutzungsstatistik> liste = benutzungsstatistikDB.selectAllBenutzungsstatistiken();
			Notification.show("Kassenbeleg verschoben", Type.TRAY_NOTIFICATION);
		});

		GridLayout grid = new GridLayout(6, 7);
		grid.addStyleName("gridlayout");
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0, 3, 0);
		grid.addComponent(lKassenbeleg, 4, 0);
		grid.addComponent(kassenbeleg, 5, 0);
		grid.addComponent(bBenutzerkontakt, 0, 1, 1, 2);
		grid.addComponent(bIntensivFrage, 2, 1, 3, 2);
		grid.addComponent(bRechercheBeratung, 4, 1, 5, 2);
		grid.addComponent(bEmailkontakt, 0, 3, 1, 4);
		grid.addComponent(bTelefonkontakt, 2, 3, 3, 4);
		grid.addComponent(bWintikurier, 4, 3, 5, 4);
		grid.addComponent(bExterneGruppe, 0, 5, 1, 6);
		grid.addComponent(bKorrektur, 2, 5, 3, 6);
		grid.addComponent(bTagesuebersicht, 4, 5, 5, 6);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row != 0) {
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
//					mainView.setContent(new ModulGUI(modul).init(mainView));
					Notification.show("Zurück", Type.WARNING_MESSAGE);
				}

				if (e.getSource() == bBenutzerkontakt) {

					Benutzerkontakt benutzerkontakt = new Benutzerkontakt(new Timestamp(new Date().getTime()),
							benutzungsstatistik);
					benutzerKontaktDB.insertBenutzerkontakt(benutzerkontakt);

					List<Benutzerkontakt> benutzerliste = benutzerKontaktDB.selectAllBenutzerkontakteForBenutzungsstatistik(
							benutzungsstatistik.getBenutzungsstatistik_ID());
					Notification.show("Benutzerkontakt erfasst ", Type.WARNING_MESSAGE);
					Notification.show("+1 Benutzerkontakt", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bIntensivFrage) {

					Intensivfrage intensivfrage = new Intensivfrage(new Timestamp(new Date().getTime()),
							benutzungsstatistik);
					intensivFrageDB.insertIntensivfrage(intensivfrage);

					List<Intensivfrage> intensivliste = intensivFrageDB.selectAllIntensivfragenForBenutzungsstatistik(
							benutzungsstatistik.getBenutzungsstatistik_ID());

					Notification.show("Intensivfrage erfasst ", Type.WARNING_MESSAGE);
					Notification.show("+1 Intensivfrage", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bRechercheBeratung) {
					benutzungsstatistik
							.setAnzahl_Rechercheberatung(benutzungsstatistik.getAnzahl_Rechercheberatung() + 1);
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					Notification.show("Weiterleitung Recherche-Beratung erfasst, insgesamt "
							+ benutzungsstatistik.getAnzahl_Rechercheberatung(), Type.WARNING_MESSAGE);
					Notification.show("+1 Rechercheberatung", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bEmailkontakt) {
					Emailkontakt emailkontakt = new Emailkontakt(new Timestamp(new Date().getTime()),
							benutzungsstatistik);
					emailKontaktDB.insertEmailkontakt(emailkontakt);

					List<Emailkontakt> emailliste = emailKontaktDB.selectAllEmailkontakteForBenutzungsstatistik(
							benutzungsstatistik.getBenutzungsstatistik_ID());

					Notification.show("Emailkontakt erfasst ", Type.WARNING_MESSAGE);
					Notification.show("+1 Emailkontakt", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bTelefonkontakt) {
					Telefonkontakt telefonkontakt = new Telefonkontakt(new Timestamp(new Date().getTime()),
							benutzungsstatistik);
					telefonKontaktDB.insertTelefonkontakt(telefonkontakt);

					List<Telefonkontakt> telefonliste = telefonKontaktDB.selectAllTelefonkontakteForBenutzungsstatistik(
							benutzungsstatistik.getBenutzungsstatistik_ID());

					Notification.show("Telefonkontakt erfasst ", Type.WARNING_MESSAGE);
					Notification.show("+1 Telefonkontakt", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bWintikurier) {
					mainView.setContent(new WintikurierView(benutzungsstatistik).init(mainView));
				}

				if (e.getSource() == bExterneGruppe) {
					mainView.setContent(new ExterneGruppeView(benutzungsstatistik).init(mainView));
				}

				if (e.getSource() == bKorrektur) {
					mainView.setContent(new KorrekturView(benutzungsstatistik).init(mainView));
				}

				if (e.getSource() == bTagesuebersicht) {
					mainView.setContent(new TagesübersichtView(benutzungsstatistik).init(mainView));
				}

			}
		};

	}

}
