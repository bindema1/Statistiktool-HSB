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
import benutzungsstatistik.model.BeantwortungBibliothekspersonal;
import benutzungsstatistik.model.Benutzerkontakt;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Intensivfrage;

/**
 * View der Benutzungsstatistik. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class BenutzungsstatistikViewLL extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-LL";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bBenutzerkontakt;
	private Button bIntensivFrage;
	private Slider sIntensivFrageSlider;
	private Button bBeantwortungBibliothekspersonal;
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
		// Setzt die Farbe des Layouts
		mainLayout.addStyleName("backgroundErfassung");
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

	public BenutzungsstatistikViewLL() {
		setCompositionRoot(init());
	}

	private void initData() {
		benutzungsstatistik = new BenutzungsstatistikDatenbank().selectBenutzungsstatistikForDateAndStandort(new Date(),
				StandortEnum.WINTERTHUR_LL);

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

		bBeantwortungBibliothekspersonal = new Button();
		setBeantwortungCaption();
		bBeantwortungBibliothekspersonal.setIcon(VaadinIcons.BOOK);
		bBeantwortungBibliothekspersonal.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBeantwortungBibliothekspersonal.addClickListener(createClickListener());

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

		GridLayout grid = new GridLayout(6, 5);
		grid.setSizeFull();
		grid.setSpacing(true);
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0, 4, 0);
		grid.addComponent(new Label(), 5, 0);
		grid.addComponent(bBenutzerkontakt, 0, 1, 1, 2);
		grid.addComponent(bIntensivFrage, 2, 1, 3, 1);
		grid.addComponent(sIntensivFrageSlider, 2, 2, 3, 2);
		grid.addComponent(bTagesuebersicht, 4, 1, 5, 2);
		grid.addComponent(bBeantwortungBibliothekspersonal, 0, 3, 2, 4);
		grid.addComponent(bKorrektur, 4, 3, 5, 4);
		grid.addComponent(new Label(), 3, 3, 3, 4);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				// Button grösser machen
				if (row != 0) {
					if (row == 2 && col == 2 || row == 2 && col == 3) {
						// Slider nicht verändern
						c.setWidth("80%");
					} else {
						c.setHeight("80%");
						c.setWidth("80%");
					}
				}
			}
		}

		grid.setColumnExpandRatio(0, 0.1666f);
		grid.setColumnExpandRatio(1, 0.1666f);
		grid.setColumnExpandRatio(2, 0.1666f);
		grid.setColumnExpandRatio(3, 0.1666f);
		grid.setColumnExpandRatio(4, 0.1666f);
		grid.setColumnExpandRatio(5, 0.1666f);
		grid.setRowExpandRatio(0, 0.1f);
		grid.setRowExpandRatio(1, 0.225f);
		grid.setRowExpandRatio(2, 0.225f);
		grid.setRowExpandRatio(3, 0.225f);
		grid.setRowExpandRatio(4, 0.225f);

		mainLayout.addComponent(grid);

	}

	private void setBeantwortungCaption() {
		int beantwortungzaehler = 0;
		for (BeantwortungBibliothekspersonal beantwortung : benutzungsstatistik
				.getBeantwortungBibliothekspersonalListe()) {
			if (Integer.parseInt(stundenFormat.format(beantwortung.getTimestamp().getTime())) == uhrzeit) {
				beantwortungzaehler++;
			}
		}
		bBeantwortungBibliothekspersonal
				.setCaption("Beantwortung Bibliothekspersonal, " + uhrzeit + " Uhr: " + beantwortungzaehler);
	}

	private void setBenutzerCaption() {
		int benutzerzaehler = 0;
		for (Benutzerkontakt benutzer : benutzungsstatistik.getBenutzerkontaktListe()) {
			if (Integer.parseInt(stundenFormat.format(benutzer.getTimestamp().getTime())) == uhrzeit) {
				benutzerzaehler++;
			}
		}
		bBenutzerkontakt.setCaption("Benutzerkontakt, " + uhrzeit + " Uhr: " + benutzerzaehler);
	}

	private void setIntensivCaption() {
		int intensivzaehler = 0;
		for (Intensivfrage intensiv : benutzungsstatistik.getIntensivfrageListe()) {
			if (Integer.parseInt(stundenFormat.format(intensiv.getTimestamp().getTime())) == uhrzeit) {
				intensivzaehler++;
			}
		}
		bIntensivFrage.setCaption("Intensivfrage, " + uhrzeit + " Uhr: " + intensivzaehler);
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
					int slider = sIntensivFrageSlider.getValue().intValue();
					for (int i = 1; i <= slider; i += 5) {
						benutzungsstatistik.addIntensivfrage(
								new Intensivfrage(new Timestamp(new Date().getTime()), benutzungsstatistik));
						benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
						zaehler++;
					}
					Notification.show("+ " + zaehler + " Intensivfrage", Type.TRAY_NOTIFICATION);

					benutzungsstatistik.addBenutzerkontakt(
							new Benutzerkontakt(new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					setIntensivCaption();
					sIntensivFrageSlider.setValue(5.0);
					setBenutzerCaption();
				}

				if (e.getSource() == bBeantwortungBibliothekspersonal) {
					benutzungsstatistik.addBeantwortungBibliothekspersonal(new BeantwortungBibliothekspersonal(
							new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);
					Notification.show("+1 Beantwortung Bibliothekspersonal", Type.TRAY_NOTIFICATION);

					setBeantwortungCaption();
				}

				if (e.getSource() == bKorrektur) {
					getUI().getNavigator()
							.navigateTo(KorrekturViewLL.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
				}

				if (e.getSource() == bTagesuebersicht) {
					getUI().getNavigator().navigateTo(
							TagesübersichtBenutzungViewLL.NAME + '/' + benutzungsstatistik.getBenutzungsstatistik_ID());
				}

			}
		};

	}

}
