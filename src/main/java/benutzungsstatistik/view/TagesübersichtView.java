package benutzungsstatistik.view;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.teemu.switchui.Switch;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import benutzungsstatistik.bean.ExterneGruppeBean;
import benutzungsstatistik.bean.TagesübersichtBean;
import benutzungsstatistik.bean.WintikurierBean;
import benutzungsstatistik.db.EmailkontaktDatenbank;
import benutzungsstatistik.db.ExterneGruppeDatenbank;
import benutzungsstatistik.db.IntensivfrageDatenbank;
import benutzungsstatistik.db.BenutzerkontaktDatenbank;
import benutzungsstatistik.db.TelefonkontaktDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.ExterneGruppe;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Telefonkontakt;

/**
 * View der Tagesübersicht. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class TagesübersichtView {

	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bKorrektur;
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

	public TagesübersichtView(Benutzungsstatistik benutzungsstatistik) {
		this.benutzungsstatistik = benutzungsstatistik;
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
		lText.setValue("Tagesübersicht Belegungsstatistik vom ");
		lText.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		DateTimeField datefield = new DateTimeField();
		datefield.setValue(LocalDateTime.now());
		datefield.setDateFormat("dd.MM.yyyy");
		datefield.addValueChangeListener(event -> 
			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION)
		);
		
		bKorrektur = new Button();
		bKorrektur.setCaption("Korrektur");
		bKorrektur.addClickListener(createClickListener(mainView));
		
		Label lKassenbeleg = new Label();
		lKassenbeleg.setValue("Kassenbeleg");
		lKassenbeleg.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		Switch kassenbeleg = new Switch();
		kassenbeleg.setEnabled(false);
		if(benutzungsstatistik.isKassenbeleg()) {
			kassenbeleg.setValue(true);
		}else {
			kassenbeleg.setValue(false);
		}
		
		Label lRechercheberatung = new Label();
		lRechercheberatung.setValue("Rechercheberatung: " +benutzungsstatistik.getAnzahl_Rechercheberatung());
		lRechercheberatung.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		Label lWintikurier = new Label();
		lWintikurier.setValue("Wintikurier");
		lWintikurier.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		Label lGruppen = new Label();
		lGruppen.setValue("Gruppen");
		lGruppen.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		
		VerticalLayout overallLayout = new VerticalLayout();
		overallLayout.setSpacing(true);
		
		HorizontalLayout headerLayout = new HorizontalLayout();
//		headerLayout.setSpacing(true);
//		headerLayout.setWidth("100%");
		headerLayout.addComponent(bZurueck);
		headerLayout.addComponent(lText);
		headerLayout.addComponent(datefield);
		headerLayout.addComponent(bKorrektur);
		overallLayout.addComponent(headerLayout);
		
		HorizontalLayout contentLayout = new HorizontalLayout();
		overallLayout.addComponent(contentLayout);
		
		Grid<TagesübersichtBean> tabelleUhrzeiten = new Grid<TagesübersichtBean>();
		fülleTabelleUhrzeiten(tabelleUhrzeiten);		
		contentLayout.addComponent(tabelleUhrzeiten);
		
		Grid<WintikurierBean> tabelleWintikurier = new Grid<WintikurierBean>();
		fülleTabelleWintikurier(tabelleWintikurier);
		
		Grid<ExterneGruppeBean> tabelleGruppen = new Grid<ExterneGruppeBean>();
		fülleTabelleGruppen(tabelleGruppen);
		
		VerticalLayout rightLayout = new VerticalLayout();
		rightLayout.addComponent(lRechercheberatung);
		HorizontalLayout kassenbelegLayout = new HorizontalLayout();
		kassenbelegLayout.addComponent(lKassenbeleg);
		kassenbelegLayout.addComponent(kassenbeleg);
		rightLayout.addComponent(kassenbelegLayout);
		rightLayout.addComponent(lWintikurier);
		rightLayout.addComponent(tabelleWintikurier);
		rightLayout.addComponent(lGruppen);
		rightLayout.addComponent(tabelleGruppen);
		rightLayout.setSpacing(true);
		contentLayout.addComponent(rightLayout);
		

		mainLayout.addComponent(overallLayout);
	}

	private void fülleTabelleWintikurier(Grid<WintikurierBean> tabelleWintikurier) {
		
		tabelleWintikurier.addColumn(WintikurierBean::getDepartment).setCaption("Department");
		tabelleWintikurier.addColumn(WintikurierBean::getAnzahl).setCaption("Anzahl");
		
		List<WintikurierBean> wintikurierBeanListe = new ArrayList<>();
		
		WintikurierBean wb1 = new WintikurierBean();
		wb1.setDepartment("Gesundheit");
		wb1.setAnzahl(benutzungsstatistik.getWintikurier().getAnzahl_Gesundheit());
		wintikurierBeanListe.add(wb1);
		WintikurierBean wb2 = new WintikurierBean();
		wb2.setDepartment("Linguistik");
		wb2.setAnzahl(benutzungsstatistik.getWintikurier().getAnzahl_Linguistik());
		wintikurierBeanListe.add(wb2);
		WintikurierBean wb3 = new WintikurierBean();
		wb3.setDepartment("Technik");
		wb3.setAnzahl(benutzungsstatistik.getWintikurier().getAnzahl_Technik());
		wintikurierBeanListe.add(wb3);
		WintikurierBean wb4 = new WintikurierBean();
		wb4.setDepartment("Wirtschaft");
		wb4.setAnzahl(benutzungsstatistik.getWintikurier().getAnzahl_Wirtschaft());
		wintikurierBeanListe.add(wb4);
		
		tabelleWintikurier.setItems(wintikurierBeanListe);
		tabelleWintikurier.setWidth("400px");
		tabelleWintikurier.setHeightByRows(wintikurierBeanListe.size());
	}

	private void fülleTabelleGruppen(Grid<ExterneGruppeBean> tabelleGruppen) {
		tabelleGruppen.addColumn(ExterneGruppeBean::getName).setCaption("Name");
		tabelleGruppen.addColumn(ExterneGruppeBean::getAnzahl_personen).setCaption("Anzahl Personen");

		ExterneGruppeDatenbank externeGruppeDB = new ExterneGruppeDatenbank();
		List<ExterneGruppe> externeGruppeListe = externeGruppeDB.selectAllExterneGruppenForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID());
		List<ExterneGruppeBean> externeGruppeBeanListe = new ArrayList<>();
		
		for(ExterneGruppe eg : externeGruppeListe) {
			ExterneGruppeBean egb = new ExterneGruppeBean();
			egb.setName(eg.getName());
			egb.setAnzahl_personen(eg.getAnzahl_Personen());
			externeGruppeBeanListe.add(egb);
		}
		
		tabelleGruppen.setItems(externeGruppeBeanListe);
		tabelleGruppen.setWidth("400px");
		if(externeGruppeBeanListe.size() >= 4) {
			tabelleGruppen.setHeight("150px");
		}else {
			tabelleGruppen.setHeightByRows(externeGruppeBeanListe.size());
		}
	}

	private void fülleTabelleUhrzeiten(Grid<TagesübersichtBean> tabelleUhrzeiten) {

		tabelleUhrzeiten.addColumn(TagesübersichtBean::getUhrzeit).setCaption("Uhrzeit");
		tabelleUhrzeiten.addColumn(TagesübersichtBean::getKontakt).setCaption("Benutzer");
		tabelleUhrzeiten.addColumn(TagesübersichtBean::getIntensiv).setCaption("Intensiv");
		tabelleUhrzeiten.addColumn(TagesübersichtBean::getEmail).setCaption("Email");
		tabelleUhrzeiten.addColumn(TagesübersichtBean::getTelefon).setCaption("Telefon");
		
		IntensivfrageDatenbank intensivFrageDB = new IntensivfrageDatenbank();
		List<Intensivfrage> intensivfragenListe = intensivFrageDB.selectAllIntensivfragenForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID());
		BenutzerkontaktDatenbank benutzerKontaktDB = new BenutzerkontaktDatenbank();
		List<Benutzerkontakt> benutzerkontaktListe = benutzerKontaktDB.selectAllBenutzerkontakteForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID());
		TelefonkontaktDatenbank telefonKontaktDB = new TelefonkontaktDatenbank();
		List<Telefonkontakt> telefonkontaktListe = telefonKontaktDB.selectAllTelefonkontakteForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID());
		EmailkontaktDatenbank emailKontaktDB = new EmailkontaktDatenbank();
		List<Emailkontakt> emailkontaktListe = emailKontaktDB.selectAllEmailkontakteForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID());
		
		List<TagesübersichtBean> tagesübersichtListe = new ArrayList<>();
		
		for(int i = 8; i <= 19; i++) {
			TagesübersichtBean tb = new TagesübersichtBean();
			int plus = i + 1;
			tb.setUhrzeit(i +"-" +plus);
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
			
			int emailzaehler = 0;
			for(Emailkontakt e : emailkontaktListe) {
				if(Integer.parseInt(dateFormat.format(e.getTimestamp().getTime())) == i) {
					emailzaehler++;
				}
			}
			tb.setEmail(emailzaehler);
			
			int intensivzaehler = 0;
			for(Intensivfrage in : intensivfragenListe) {
				if(Integer.parseInt(dateFormat.format(in.getTimestamp().getTime())) == i) {
					intensivzaehler++;
				}
			}
			tb.setIntensiv(intensivzaehler);
			
			int benutzerzaehler = 0;
			for(Benutzerkontakt k : benutzerkontaktListe) {
				if(Integer.parseInt(dateFormat.format(k.getTimestamp().getTime())) == i) {
					benutzerzaehler++;
				}
			}
			tb.setKontakt(benutzerzaehler);
			
			int telefonzaehler = 0;
			for(Telefonkontakt t : telefonkontaktListe) {
				if(Integer.parseInt(dateFormat.format(t.getTimestamp().getTime())) == i) {
					telefonzaehler++;
				}
			}
			tb.setTelefon(telefonzaehler);
			
			tagesübersichtListe.add(tb);
		}
		
		tabelleUhrzeiten.setItems(tagesübersichtListe);
		tabelleUhrzeiten.setHeightByRows(tagesübersichtListe.size());
	}

	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					mainView.setContent(new BenutzungsstatistikView().init(mainView));
				}

				if (e.getSource() == bKorrektur) {
					mainView.setContent(new KorrekturView(benutzungsstatistik).init(mainView));
				}

			}
		};

	}

}
