package benutzungsstatistik.view;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import allgemein.view.StartseiteView;
import benutzungsstatistik.bean.TagesübersichtBenutzungBean;
import benutzungsstatistik.bean.WintikurierBean;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Telefonkontakt;

/**
 * View der Tagesübersicht. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class TagesübersichtBenutzungViewWaedi extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-Übersicht-Waedi";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bKorrektur;
	private boolean vonStartseite;
	private Benutzungsstatistik benutzungsstatistik;
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		// Setzt die Farbe des Layouts
		mainLayout.addStyleName("backgroundTages");
		mainLayout.setWidth("100%");
		// mainLayout.setHeight("100%");

		return mainLayout;
	}

	public AbsoluteLayout init() {

		AbsoluteLayout absolutLayout = buildMainLayout();
		initData();
		initComponents();

		return absolutLayout;
	}

	public TagesübersichtBenutzungViewWaedi() {

	}

	private void initData() {

	}

	// Initialisieren der GUI Komponente
	private void initComponents() {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener());

		Label lText = new Label();
		lText.setValue("Tagesübersicht Belegungsstatistik vom ");
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bKorrektur = new Button();
		bKorrektur.setCaption("Korrektur");
		bKorrektur.addClickListener(createClickListener());

//		Label lRechercheberatung = new Label();
//		lRechercheberatung.setValue("Rechercheberatung: " + benutzungsstatistik.getAnzahl_Rechercheberatung());
//		lRechercheberatung.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		Label lWintikurier = new Label();
		lWintikurier.setValue("Interner Kurier");
		lWintikurier.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		Grid<TagesübersichtBenutzungBean> tabelleUhrzeiten = new Grid<TagesübersichtBenutzungBean>();
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getUhrzeit).setCaption("Uhrzeit");
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getKontakt).setCaption("Benutzer");
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getIntensiv).setCaption("Intensiv");
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getEmail).setCaption("Email");
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getTelefon).setCaption("Telefon");
		fülleTabelleUhrzeiten(tabelleUhrzeiten);

		Grid<WintikurierBean> tabelleInternerkurier = new Grid<WintikurierBean>();
		tabelleInternerkurier.addColumn(WintikurierBean::getDepartment).setCaption("Department");
		tabelleInternerkurier.addColumn(WintikurierBean::getAnzahl).setCaption("Anzahl");
		fülleTabelleInternerkurier(tabelleInternerkurier);

		DateField datefield = new DateField();
		datefield.setValue(LocalDate.now());
		datefield.setDateFormat("dd.MM.yyyy");
		if (!VaadinSession.getCurrent().getAttribute("user").toString().contains("Admin")) {
			// Nicht-Administratoren dürfen nur eine Woche in der Zeit zurück
			datefield.setRangeStart(LocalDate.now().minusDays(7));
			datefield.setRangeEnd(LocalDate.now());
		}
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			ZonedDateTime zdt = event.getValue().atStartOfDay().atZone(ZoneId.systemDefault());
			Date date = Date.from(zdt.toInstant());

			benutzungsstatistik = new BenutzungsstatistikDatenbank().selectBenutzungsstatistikForDateAndStandort(date,
					StandortEnum.WÄDENSWIL);

			// Alle Werte anpassen
			fülleTabelleUhrzeiten(tabelleUhrzeiten);
			fülleTabelleInternerkurier(tabelleInternerkurier);
//			lRechercheberatung.setValue("Rechercheberatung: " + benutzungsstatistik.getAnzahl_Rechercheberatung());
		});

		VerticalLayout overallLayout = new VerticalLayout();
		overallLayout.setSpacing(true);

		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.addComponent(bZurueck);
		headerLayout.addComponent(lText);
		headerLayout.addComponent(datefield);
		headerLayout.addComponent(bKorrektur);
		overallLayout.addComponent(headerLayout);

		HorizontalLayout contentLayout = new HorizontalLayout();
		VerticalLayout rightLayout = new VerticalLayout();
//		rightLayout.addComponent(lRechercheberatung);
		rightLayout.addComponent(lWintikurier);
		rightLayout.addComponent(tabelleInternerkurier);
		rightLayout.setSpacing(true);
		contentLayout.addComponent(tabelleUhrzeiten);
		contentLayout.addComponent(rightLayout);
		overallLayout.addComponent(contentLayout);

		mainLayout.addComponent(overallLayout);
	}

	private void fülleTabelleInternerkurier(Grid<WintikurierBean> tabelleInternerkurier) {

		List<WintikurierBean> internerkurierBeanListe = new ArrayList<>();

		WintikurierBean wb1 = new WintikurierBean();
		wb1.setDepartment("Campus Reidbach");
		wb1.setAnzahl(benutzungsstatistik.getInternerkurier().getAnzahl_Reidbach());
		internerkurierBeanListe.add(wb1);
		WintikurierBean wb2 = new WintikurierBean();
		wb2.setDepartment("Campus GS");
		wb2.setAnzahl(benutzungsstatistik.getInternerkurier().getAnzahl_GS());
		internerkurierBeanListe.add(wb2);
		WintikurierBean wb3 = new WintikurierBean();
		wb3.setDepartment("Campus RA");
		wb3.setAnzahl(benutzungsstatistik.getInternerkurier().getAnzahl_RA());
		internerkurierBeanListe.add(wb3);

		tabelleInternerkurier.setItems(internerkurierBeanListe);
		tabelleInternerkurier.setWidth("400px");
		tabelleInternerkurier.setHeightByRows(internerkurierBeanListe.size());
	}

	private void fülleTabelleUhrzeiten(Grid<TagesübersichtBenutzungBean> tabelleUhrzeiten) {

		List<TagesübersichtBenutzungBean> tagesübersichtListe = new ArrayList<>();

		for (int i = 8; i <= 19; i++) {
			TagesübersichtBenutzungBean tb = new TagesübersichtBenutzungBean();
			int plus = i + 1;
			tb.setUhrzeit(i + "-" + plus);
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH");

			int emailzaehler = 0;
			for (Emailkontakt e : benutzungsstatistik.getEmailkontaktListe()) {
				if (Integer.parseInt(dateFormat.format(e.getTimestamp().getTime())) == i) {
					emailzaehler++;
				}
			}
			tb.setEmail(emailzaehler);

			int intensivzaehler = 0;
			for (Intensivfrage in : benutzungsstatistik.getIntensivfrageListe()) {
				if (Integer.parseInt(dateFormat.format(in.getTimestamp().getTime())) == i) {
					intensivzaehler++;
				}
			}
			tb.setIntensiv(intensivzaehler);

			int benutzerzaehler = 0;
			for (Benutzerkontakt k : benutzungsstatistik.getBenutzerkontaktListe()) {
				if (Integer.parseInt(dateFormat.format(k.getTimestamp().getTime())) == i) {
					benutzerzaehler++;
				}
			}
			tb.setKontakt(benutzerzaehler);

			int telefonzaehler = 0;
			for (Telefonkontakt t : benutzungsstatistik.getTelefonkontaktListe()) {
				if (Integer.parseInt(dateFormat.format(t.getTimestamp().getTime())) == i) {
					telefonzaehler++;
				}
			}
			tb.setTelefon(telefonzaehler);

			tagesübersichtListe.add(tb);
		}

		tabelleUhrzeiten.setItems(tagesübersichtListe);
		tabelleUhrzeiten.setWidth("450px");
		tabelleUhrzeiten.setHeightByRows(tagesübersichtListe.size());
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String args[] = event.getParameters().split("/");
		String id = args[0];
		// Ob der Zugriff direkt aus der Startseite oder von der Belegung kommt
		String vonStartseiteString = args[1];

		this.benutzungsstatistik = benutzungsstatistikDB.findBenutzungsstatistikById(Integer.parseInt(id));
		this.vonStartseite = Boolean.parseBoolean(vonStartseiteString);

		setCompositionRoot(init());
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					if (vonStartseite == true) {
						Page.getCurrent().setUriFragment("!" + StartseiteView.NAME);
					} else {
						getUI().getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
					}
				}

				if (e.getSource() == bKorrektur) {
					getUI().getNavigator().navigateTo(
							KorrekturViewWaedi.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
				}

			}
		};

	}

}
