package benutzungsstatistik.view;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.vaadin.teemu.switchui.Switch;

import com.vaadin.annotations.Theme;
import com.vaadin.data.HasValue;
import com.vaadin.flow.router.Route;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.view.MainView;
import benutzungsstatistik.db.BenutzerkontaktDatenbank;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.db.EmailkontaktDatenbank;
import benutzungsstatistik.db.IntensivfrageDatenbank;
import benutzungsstatistik.db.TelefonkontaktDatenbank;
import benutzungsstatistik.model.Benutzungsstatistik;

/**
 * View der Korrektur. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
 * 
 * @author Marvin Bindemann
 */
@Route(value = "korrektur")
@Theme("mytheme")
public class KorrekturView {

	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bBenutzerkontakt;
	private Button bIntensivFrage;
	private Button bEmailkontakt;
	private Button bTelefonkontakt;
	private Button bBenutzerkontaktMinus;
	private Button bIntensivFrageMinus;
	private Button bEmailkontaktMinus;
	private Button bTelefonkontaktMinus;
	private Button bRechercheBeratung;
	private Button bRechercheBeratungMinus;
	private Button bKorrekturWintikurier;
	private Button bKorrekturGruppen;
	private EmailkontaktDatenbank emailKontaktDB = new EmailkontaktDatenbank();
	private IntensivfrageDatenbank intensivFrageDB = new IntensivfrageDatenbank();
	private BenutzerkontaktDatenbank benutzerKontaktDB = new BenutzerkontaktDatenbank();
	private TelefonkontaktDatenbank telefonKontaktDB = new TelefonkontaktDatenbank();
	
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
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

	public KorrekturView(Benutzungsstatistik benutzungsstatistik) {
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
		mainLayout.addComponent(bZurueck);

		Label lText = new Label();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		lText.setValue("Benutzungsstatistik Korrektur vom " + sdf.format(benutzungsstatistik.getDatum()));
		lText.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		DateTimeField datefield = new DateTimeField();
		datefield.setValue(LocalDateTime.now());
		datefield.setDateFormat("dd.MM.yyyy");
		datefield.addValueChangeListener(
				event -> Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION));

		List<String> data = IntStream.range(0, 6).mapToObj(i -> "Option " + i).collect(Collectors.toList());
		ListSelect<String> uhrzeitListSelect = new ListSelect<>("Select an option", data);
		uhrzeitListSelect.setRows(6);
		uhrzeitListSelect.select(data.get(2));
		uhrzeitListSelect.setWidth(100.0f, Unit.PERCENTAGE);
		uhrzeitListSelect.addValueChangeListener(event -> Notification.show("Value changed:", String.valueOf(event.getValue()),
                Type.TRAY_NOTIFICATION));
		
		bBenutzerkontakt = new Button();
		bBenutzerkontakt.setCaption("Benutzerkontakt");
		bBenutzerkontakt.setIcon(VaadinIcons.QUESTION_CIRCLE_O);
		bBenutzerkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bBenutzerkontakt.addClickListener(createClickListener(mainView));

		Label lBenutzerkontakt = new Label();
		lBenutzerkontakt.setValue("" +benutzerKontaktDB.selectAllBenutzerkontakteForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID()));
		lBenutzerkontakt.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		bBenutzerkontaktMinus = new Button();
		bBenutzerkontaktMinus.setCaption("B Korrektur -1");
		bBenutzerkontaktMinus.addStyleName(ValoTheme.BUTTON_LARGE +" " +ValoTheme.BUTTON_DANGER);
		bBenutzerkontaktMinus.addClickListener(createClickListener(mainView));
		
		bIntensivFrage = new Button();
		bIntensivFrage.setCaption("Intensiv Frage");
		bIntensivFrage.setIcon(VaadinIcons.HOURGLASS);
		bIntensivFrage.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bIntensivFrage.addClickListener(createClickListener(mainView));

		Label lIntensivFrage = new Label();
		lIntensivFrage.setValue("" +intensivFrageDB.selectAllIntensivfragenForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID()));
		lIntensivFrage.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		bIntensivFrageMinus = new Button();
		bIntensivFrageMinus.setCaption("I Korrektur -1");
		bIntensivFrageMinus.addStyleName(ValoTheme.BUTTON_LARGE +" " +ValoTheme.BUTTON_DANGER);
		bIntensivFrageMinus.addClickListener(createClickListener(mainView));
		
		bEmailkontakt = new Button();
		bEmailkontakt.setCaption("Email");
		bEmailkontakt.setIcon(VaadinIcons.ENVELOPE_OPEN_O);
		bEmailkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bEmailkontakt.addClickListener(createClickListener(mainView));

		Label lEmailkontakt = new Label();
		lEmailkontakt.setValue("" +emailKontaktDB.selectAllEmailkontakteForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID()));
		lEmailkontakt.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		bEmailkontaktMinus = new Button();
		bEmailkontaktMinus.setCaption("E Korrektur -1");
		bEmailkontaktMinus.addStyleName(ValoTheme.BUTTON_LARGE +" " +ValoTheme.BUTTON_DANGER);
		bEmailkontaktMinus.addClickListener(createClickListener(mainView));
		
		bTelefonkontakt = new Button();
		bTelefonkontakt.setCaption("Telefon");
		bTelefonkontakt.setIcon(VaadinIcons.PHONE);
		bTelefonkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE);
		bTelefonkontakt.addClickListener(createClickListener(mainView));

		Label lTelefonkontakt = new Label();
		lTelefonkontakt.setValue("" +telefonKontaktDB.selectAllTelefonkontakteForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID()));
		lTelefonkontakt.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		bTelefonkontaktMinus = new Button();
		bTelefonkontaktMinus.setCaption("T Korrektur -1");
		bTelefonkontaktMinus.addStyleName(ValoTheme.BUTTON_LARGE +" " +ValoTheme.BUTTON_DANGER);
		bTelefonkontaktMinus.addClickListener(createClickListener(mainView));
		
		Label lKassenbeleg = new Label();
		lKassenbeleg.setValue("Kassenbeleg");
		lKassenbeleg.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		Switch kassenbeleg = new Switch();
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
		
		Label lRechercheberatung = new Label();
		lRechercheberatung.setValue("Rechercheberatung: " +benutzungsstatistik.getAnzahl_Rechercheberatung());
		lRechercheberatung.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		bRechercheBeratung = new Button();
		bRechercheBeratung.setCaption("Rechercheb. +1");
		bRechercheBeratung.addStyleName(ValoTheme.BUTTON_LARGE);
		bRechercheBeratung.addClickListener(createClickListener(mainView));

		bRechercheBeratungMinus = new Button();
		bRechercheBeratungMinus.setCaption("Rechercheb. -1");
		bRechercheBeratungMinus.addStyleName(ValoTheme.BUTTON_LARGE +" " +ValoTheme.BUTTON_DANGER);
		bRechercheBeratungMinus.addClickListener(createClickListener(mainView));

		bKorrekturWintikurier = new Button();
		bKorrekturWintikurier.setCaption("Korrektur Wintikurier");
		bKorrekturWintikurier.addStyleName(ValoTheme.BUTTON_LARGE);
		bKorrekturWintikurier.addClickListener(createClickListener(mainView));

		bKorrekturGruppen = new Button();
		bKorrekturGruppen.setCaption("Korrektur Gruppen");
		bKorrekturGruppen.addStyleName(ValoTheme.BUTTON_LARGE);
		bKorrekturGruppen.addClickListener(createClickListener(mainView));


		GridLayout grid = new GridLayout(5, 8);
		grid.addStyleName("gridlayout");
		grid.setSizeFull();
		grid.setSpacing(true);
		grid.addComponent(bZurueck, 0, 0);
		grid.addComponent(lText, 1, 0, 2, 0);
		grid.addComponent(datefield, 3, 0, 4, 0);
		grid.addComponent(uhrzeitListSelect, 0, 1, 0, 5);
		grid.addComponent(bBenutzerkontakt, 1, 1, 1, 3);
		grid.addComponent(lBenutzerkontakt, 1, 4);
		grid.addComponent(bBenutzerkontaktMinus, 1, 5);
		grid.addComponent(bIntensivFrage, 2, 1, 2, 3);
		grid.addComponent(lIntensivFrage, 2, 4);
		grid.addComponent(bIntensivFrageMinus, 2, 5);
		grid.addComponent(bEmailkontakt, 3, 1, 3, 3);
		grid.addComponent(lEmailkontakt, 3, 4);
		grid.addComponent(bEmailkontaktMinus, 3, 5);
		grid.addComponent(bTelefonkontakt, 4, 1, 4, 3);
		grid.addComponent(lTelefonkontakt, 4, 4);
		grid.addComponent(bTelefonkontaktMinus, 4, 5);
		grid.addComponent(lKassenbeleg, 0, 6);
		grid.addComponent(kassenbeleg, 0, 7);
		grid.addComponent(lRechercheberatung, 1, 6, 2, 6);
		grid.addComponent(bRechercheBeratung, 1, 7);
		grid.addComponent(bRechercheBeratungMinus, 2, 7);
		grid.addComponent(bKorrekturWintikurier, 3, 6, 3, 7);
		grid.addComponent(bKorrekturGruppen, 4, 6, 4, 7);
		
		
		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);
				
				// Button grösser machen
//				if (row != 0) {
//					c.setHeight("80%");
//					c.setWidth("80%");
//				}
			}
		}

		mainLayout.addComponent(grid);

	}

	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					mainView.setContent(new BenutzungsstatistikView().init(mainView));
				}


			}
		};

	}

}
