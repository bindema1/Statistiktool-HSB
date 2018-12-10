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
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.model.StandortEnum;
import benutzungsstatistik.bean.TagesübersichtBenutzungBean;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.BeantwortungBibliothekspersonal;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Intensivfrage;

/**
 * View der Tagesübersicht. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class TagesübersichtBenutzungViewLL extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-Übersicht-LL";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bKorrektur;
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

	public TagesübersichtBenutzungViewLL() {

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

		Grid<TagesübersichtBenutzungBean> tabelleUhrzeiten = new Grid<TagesübersichtBenutzungBean>();
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getUhrzeit).setCaption("Uhrzeit");
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getKontakt).setCaption("Benutzer");
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getIntensiv).setCaption("Intensivfrage");
		tabelleUhrzeiten.addColumn(TagesübersichtBenutzungBean::getBeantwortungBibi)
				.setCaption("Beantwortung Bibliothek");
		fülleTabelleUhrzeiten(tabelleUhrzeiten);

		DateField datefield = new DateField();
		datefield.setValue(LocalDate.now());
		datefield.setDateFormat("dd.MM.yyyy");
		datefield.addValueChangeListener(event -> {
			Notification.show("Datum geändert", Type.TRAY_NOTIFICATION);

			ZonedDateTime zdt = event.getValue().atStartOfDay().atZone(ZoneId.systemDefault());
			Date date = Date.from(zdt.toInstant());

			benutzungsstatistik = new BenutzungsstatistikDatenbank().selectBenutzungsstatistikForDateAndStandort(date,
					StandortEnum.WINTERTHUR_LL);

			// Alle Werte anpassen
			fülleTabelleUhrzeiten(tabelleUhrzeiten);
		});

		GridLayout grid = new GridLayout(6, 2);
		grid.setSizeFull();
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0, 2, 0);
		grid.addComponent(datefield, 3, 0, 4, 0);
		grid.addComponent(bKorrektur, 5, 0);
		grid.addComponent(tabelleUhrzeiten, 0, 1, 5, 1);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row == 0) {
					if (col == 1 || col == 2) {
						grid.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
					}
				} else {
//					c.setHeight("87%");
					c.setWidth("90%");
				}
			}
		}

		grid.setRowExpandRatio(0, 0.1f);
		grid.setRowExpandRatio(1, 0.9f);

		mainLayout.addComponent(grid);
	}

	private void fülleTabelleUhrzeiten(Grid<TagesübersichtBenutzungBean> tabelleUhrzeiten) {

		List<TagesübersichtBenutzungBean> tagesübersichtListe = new ArrayList<>();

		for (int i = 8; i <= 19; i++) {
			TagesübersichtBenutzungBean tb = new TagesübersichtBenutzungBean();
			int plus = i + 1;
			tb.setUhrzeit(i + "-" + plus);
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH");

			int beantwortungzaehler = 0;
			for (BeantwortungBibliothekspersonal e : benutzungsstatistik.getBeantwortungBibliothekspersonalListe()) {
				if (Integer.parseInt(dateFormat.format(e.getTimestamp().getTime())) == i) {
					beantwortungzaehler++;
				}
			}
			tb.setBeantwortungBibi(beantwortungzaehler);

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

			tagesübersichtListe.add(tb);
		}

		tabelleUhrzeiten.setItems(tagesübersichtListe);
		tabelleUhrzeiten.setWidth("90%");
		tabelleUhrzeiten.setHeightByRows(tagesübersichtListe.size());
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String args[] = event.getParameters().split("/");
		String id = args[0];
		this.benutzungsstatistik = benutzungsstatistikDB.findBenutzungsstatistikById(Integer.parseInt(id));
		setCompositionRoot(init());
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewLL.NAME);
				}

				if (e.getSource() == bKorrektur) {
					getUI().getNavigator()
							.navigateTo(KorrekturViewLL.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
				}

			}
		};

	}

}
