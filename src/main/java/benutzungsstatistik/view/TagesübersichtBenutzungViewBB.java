package benutzungsstatistik.view;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.vaadin.teemu.switchui.Switch;

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
import benutzungsstatistik.bean.ExterneGruppeBean;
import benutzungsstatistik.bean.TagesübersichtBenutzungBean;
import benutzungsstatistik.bean.WintikurierBean;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.ExterneGruppe;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Telefonkontakt;

/**
 * View der Tagesübersicht. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class TagesübersichtBenutzungViewBB extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-Übersicht-BB";
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

	public TagesübersichtBenutzungViewBB() {

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

		Label lKassenbeleg = new Label();
		lKassenbeleg.setValue("Kassenbeleg");
		lKassenbeleg.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		Switch kassenbeleg = new Switch();
		kassenbeleg.setEnabled(false);
		if (benutzungsstatistik.isKassenbeleg()) {
			kassenbeleg.setValue(true);
		} else {
			kassenbeleg.setValue(false);
		}

		Label lRechercheberatung = new Label();
		lRechercheberatung.setValue("Rechercheberatung: " + benutzungsstatistik.getAnzahl_Rechercheberatung());
		lRechercheberatung.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		Label lWintikurier = new Label();
		lWintikurier.setValue("Wintikurier");
		lWintikurier.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		Label lGruppen = new Label();
		lGruppen.setValue("Gruppen");
		lGruppen.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		Grid<TagesübersichtBenutzungBean> tabelleUhrzeiten = new Grid<TagesübersichtBenutzungBean>();
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getUhrzeit).setCaption("Uhrzeit");
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getKontakt).setCaption("Benutzer");
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getIntensiv).setCaption("Intensiv");
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getEmail).setCaption("Email");
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getTelefon).setCaption("Telefon");
		fülleTabelleUhrzeiten(tabelleUhrzeiten);

		Grid<WintikurierBean> tabelleWintikurier = new Grid<WintikurierBean>();
		tabelleWintikurier.addColumn(WintikurierBean::getDepartment).setCaption("Department");
		tabelleWintikurier.addColumn(WintikurierBean::getAnzahl).setCaption("Anzahl");
		fülleTabelleWintikurier(tabelleWintikurier);

		Grid<ExterneGruppeBean> tabelleGruppen = new Grid<ExterneGruppeBean>();
		tabelleGruppen.addColumn(ExterneGruppeBean::getName).setCaption("Name").setId("GruppenName");
		tabelleGruppen.addColumn(ExterneGruppeBean::getAnzahl_personen).setCaption("Personen");
		tabelleGruppen.addColumn(ExterneGruppeBean::getErfasstUm).setCaption("Erfasst um");
		fülleTabelleGruppen(tabelleGruppen);

		DateField datefield = new DateField();
		datefield.setValue(LocalDate.now());
		if (!VaadinSession.getCurrent().getAttribute("user").toString().contains("Admin")) {
			// Nicht-Administratoren dürfen nur eine Woche in der Zeit zurück
			datefield.setRangeStart(LocalDate.now().minusDays(7));
			datefield.setRangeEnd(LocalDate.now());
		}
		datefield.setDateFormat("dd.MM.yyyy");
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			ZonedDateTime zdt = event.getValue().atStartOfDay().atZone(ZoneId.systemDefault());
			Date date = Date.from(zdt.toInstant());

			benutzungsstatistik = new BenutzungsstatistikDatenbank().selectBenutzungsstatistikForDateAndStandort(date,
					StandortEnum.WINTERTHUR_BB);

			// Alle Werte anpassen
			fülleTabelleGruppen(tabelleGruppen);
			fülleTabelleUhrzeiten(tabelleUhrzeiten);
			fülleTabelleWintikurier(tabelleWintikurier);
			if (benutzungsstatistik.isKassenbeleg()) {
				kassenbeleg.setValue(true);
			} else {
				kassenbeleg.setValue(false);
			}
			lRechercheberatung.setValue("Rechercheberatung: " + benutzungsstatistik.getAnzahl_Rechercheberatung());
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
		contentLayout.addComponent(tabelleUhrzeiten);
		contentLayout.addComponent(rightLayout);
		overallLayout.addComponent(contentLayout);

		mainLayout.addComponent(overallLayout);

//		GridLayout grid = new GridLayout(6, 12);
//		grid.addStyleName("gridlayout");
//		grid.setSizeFull();
//		grid.addComponent(bZurueck, 0, 0);
//		grid.addComponent(lText, 1, 0, 2, 0);
//		grid.addComponent(datefield, 3, 0, 4, 0);
//		grid.addComponent(bKorrektur, 5, 0);
//		grid.addComponent(tabelleUhrzeiten, 0, 1, 2, 11);
//		grid.addComponent(lRechercheberatung, 3, 1, 5, 1);
//		grid.addComponent(lKassenbeleg, 3, 2);
//		grid.addComponent(kassenbeleg, 4, 2, 5, 2);
//		grid.addComponent(lWintikurier, 3, 3, 5, 3);
//		grid.addComponent(tabelleWintikurier, 3, 4, 5, 7);
//		grid.addComponent(lGruppen, 3, 8, 5, 8);
//		grid.addComponent(tabelleGruppen, 3, 9, 5, 11);
//
//		for (int col = 0; col < grid.getColumns(); col++) {
//			for (int row = 0; row < grid.getRows(); row++) {
//				Component c = grid.getComponent(col, row);
//				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);
//
//				// Button grösser machen
//				if (row == 0) {
//					if (col == 1 || col == 2) {
//						grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
//					}
//				} else {
//					c.setHeight("90%");
//					c.setWidth("90%");
//				}
//			}
//		}
//
//		mainLayout.addComponent(grid);
	}

	private void fülleTabelleWintikurier(Grid<WintikurierBean> tabelleWintikurier) {

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

		List<ExterneGruppeBean> externeGruppeBeanListe = new ArrayList<>();

		for (ExterneGruppe eg : benutzungsstatistik.getExterneGruppeListe()) {
			ExterneGruppeBean egb = new ExterneGruppeBean();
			egb.setName(eg.getName());
			egb.setAnzahl_personen(eg.getAnzahl_Personen());
			egb.setErfasstUm(eg.getErfasstUm());
			externeGruppeBeanListe.add(egb);
		}

		tabelleGruppen.setItems(externeGruppeBeanListe);
		tabelleGruppen.getColumn("GruppenName").setExpandRatio(2);
		tabelleGruppen.setWidth("400px");
		if (externeGruppeBeanListe.size() >= 4) {
			tabelleGruppen.setHeight("150px");
		} else if (externeGruppeBeanListe.size() > 0) {
			tabelleGruppen.setHeightByRows(externeGruppeBeanListe.size());
		} else {
			tabelleGruppen.setHeight("85px");
		}
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
						getUI().getNavigator().navigateTo(BenutzungsstatistikViewBB.NAME);
					}
				}

				if (e.getSource() == bKorrektur) {
					getUI().getNavigator()
							.navigateTo(KorrekturViewBB.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
				}

			}
		};

	}

}
