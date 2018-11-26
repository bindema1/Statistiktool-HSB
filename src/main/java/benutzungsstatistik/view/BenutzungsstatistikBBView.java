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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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
public class BenutzungsstatistikBBView extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Benutzung-BB";
	private AbsoluteLayout mainLayout;
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
//	private EmailkontaktDatenbank emailKontaktDB = new EmailkontaktDatenbank();
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
//	private IntensivfrageDatenbank intensivFrageDB = new IntensivfrageDatenbank();
//	private BenutzerkontaktDatenbank benutzerKontaktDB = new BenutzerkontaktDatenbank();
//	private TelefonkontaktDatenbank telefonKontaktDB = new TelefonkontaktDatenbank();
	private Benutzungsstatistik benutzungsstatistik;

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

	public BenutzungsstatistikBBView() {
		setCompositionRoot(init());
	}

	private void initData() {
		benutzungsstatistik = new BenutzungsstatistikDatenbank().selectBenutzungsstatistikForDateAndStandort(new Date(),
				StandortEnum.WINTERTHUR_BB);
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
		bBenutzerkontakt.setCaption("Benutzerkontakt");
		bBenutzerkontakt.setIcon(VaadinIcons.QUESTION_CIRCLE_O);
		bBenutzerkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bBenutzerkontakt.addClickListener(createClickListener());

		bIntensivFrage = new Button();
		bIntensivFrage.setCaption("Intensiv Frage");
		bIntensivFrage.setIcon(VaadinIcons.HOURGLASS);
		bIntensivFrage.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bIntensivFrage.addClickListener(createClickListener());

		bRechercheBeratung = new Button();
		bRechercheBeratung.setCaption("Recherche-Beratung");
		bRechercheBeratung.setIcon(VaadinIcons.GLASSES);
		bRechercheBeratung.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bRechercheBeratung.addClickListener(createClickListener());

		bEmailkontakt = new Button();
		bEmailkontakt.setCaption("Email");
		bEmailkontakt.setIcon(VaadinIcons.ENVELOPE_OPEN_O);
		bEmailkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bEmailkontakt.addClickListener(createClickListener());

		bTelefonkontakt = new Button();
		bTelefonkontakt.setCaption("Telefon");
		bTelefonkontakt.setIcon(VaadinIcons.PHONE);
		bTelefonkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bTelefonkontakt.addClickListener(createClickListener());

		bWintikurier = new Button();
		bWintikurier.setCaption("Wintikurier");
		bWintikurier.setIcon(VaadinIcons.TRUCK);
		bWintikurier.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bWintikurier.addClickListener(createClickListener());

		bExterneGruppe = new Button();
		bExterneGruppe.setCaption("Externe Gruppe");
		bExterneGruppe.setIcon(VaadinIcons.GROUP);
		bExterneGruppe.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bExterneGruppe.addClickListener(createClickListener());

		bKorrektur = new Button();
		bKorrektur.setCaption("Korrektur");
		bKorrektur.setIcon(VaadinIcons.EDIT);
		bKorrektur.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bKorrektur.addClickListener(createClickListener());

		bTagesuebersicht = new Button();
		bTagesuebersicht.setCaption("Tagesübersicht");
		bTagesuebersicht.setIcon(VaadinIcons.CLIPBOARD_TEXT);
		bTagesuebersicht.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
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
				}

				if (e.getSource() == bIntensivFrage) {

					benutzungsstatistik.addIntensivfrage(
							new Intensivfrage(new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					Notification.show("+1 Intensivfrage", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bRechercheBeratung) {
					benutzungsstatistik
							.setAnzahl_Rechercheberatung(benutzungsstatistik.getAnzahl_Rechercheberatung() + 1);
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					Notification.show("+1 Rechercheberatung", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bEmailkontakt) {
					benutzungsstatistik.addEmailkontakt(
							new Emailkontakt(new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					Notification.show("+1 Emailkontakt", Type.TRAY_NOTIFICATION);
				}

				if (e.getSource() == bTelefonkontakt) {
					benutzungsstatistik.addTelefonkontakt(
							new Telefonkontakt(new Timestamp(new Date().getTime()), benutzungsstatistik));
					benutzungsstatistikDB.updateBenutzungsstatistik(benutzungsstatistik);

					Notification.show("+1 Telefonkontakt", Type.TRAY_NOTIFICATION);
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
