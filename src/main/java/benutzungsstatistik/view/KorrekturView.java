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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import Allgemein.db.StandortDatenbank;
import Allgemein.model.Angestellter;
import Allgemein.model.Standort;
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
 * View der Korrektur. Zeigt alle Button, Label, Felder etc. in einem
 * Layout an.
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class KorrekturView {

	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bBenutzerkontakt;
	private Button bIntensivFrage;
	private Button bRechercheBeratung;
	private Button bEmailkontakt;
	private Label lText;
	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
	WintikurierDatenbank wintikurierDB = new WintikurierDatenbank();
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

		lText = new Label();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		lText.setValue("Benutzungsstatistik vom " + sdf.format(benutzungsstatistik.getDatum()));
		lText.addStyleName(ValoTheme.LABEL_LARGE +" " +ValoTheme.LABEL_BOLD);
		
		bBenutzerkontakt = new Button();
		bBenutzerkontakt.setCaption("Einfache Frage");
		bBenutzerkontakt.setIcon(VaadinIcons.QUESTION_CIRCLE_O);
		bBenutzerkontakt.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " " + ValoTheme.BUTTON_LARGE + "huge-icon");
		bBenutzerkontakt.addClickListener(createClickListener(mainView));


//		GridLayout grid = new GridLayout(6, 7);
//		grid.addStyleName("example-gridlayout");
//		grid.setSizeFull();
//		grid.addComponent(bZurueck, 0, 0);
//		grid.addComponent(lText, 1, 0, 3, 0);
//		grid.addComponent(lKassenbeleg, 4, 0);
//		grid.addComponent(kassenbeleg, 5, 0);
//		grid.addComponent(bBenutzerkontakt, 0, 1, 1, 2);
//		grid.addComponent(bIntensivFrage, 2, 1, 3, 2);
//		grid.addComponent(bRechercheBeratung, 4, 1, 5, 2);
//		grid.addComponent(bEmailkontakt, 0, 3, 1, 4);
//		grid.addComponent(bTelefonkontakt, 2, 3, 3, 4);
//		grid.addComponent(bWintikurier, 4, 3, 5, 4);
//		grid.addComponent(bExterneGruppe, 0, 5, 1, 6);
//		grid.addComponent(bKorrektur, 2, 5, 3, 6);
//		grid.addComponent(bTagesuebersicht, 4, 5, 5, 6);

//		for (int col = 0; col < grid.getColumns(); col++) {
//			for (int row = 0; row < grid.getRows(); row++) {
//				Component c = grid.getComponent(col, row);
//				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);
//				
//				// Button grösser machen
//				if (row != 0) {
//					c.setHeight("80%");
//					c.setWidth("80%");
//				}
//			}
//		}

//		mainLayout.addComponent(grid);

	}

	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bZurueck) {
					mainView.setContent(new BenutzungsstatistikView().init(mainView));
				}

				if (e.getSource() == bBenutzerkontakt) {

//					Benutzerkontakt benutzerkontakt = new Benutzerkontakt(new Timestamp(new Date().getTime()), benutzungsstatistik);
//					benutzerKontaktDB.insertBenutzerkontakt(benutzerkontakt);
//
//					List<Benutzerkontakt> benutzerliste = benutzerKontaktDB
//							.selectAllBenutzerkontakteForBenutzungsstatistik(benutzungsstatistik.getBenutzungsstatistik_ID());
//					Notification.show("Benutzerkontakt erfasst " + benutzerliste.get(benutzerliste.size() - 1).getTimestamp(),
//							Type.WARNING_MESSAGE);
//					Notification.show("+1 Benutzerkontakt", Type.TRAY_NOTIFICATION);
				}

			}
		};

	}

}
