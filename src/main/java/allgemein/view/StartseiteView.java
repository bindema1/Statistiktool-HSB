package allgemein.view;

import java.util.Date;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import administrator.view.ExportViewWaedi;
import administrator.view.ExportViewWinti;
import administrator.view.PasswortView;
import allgemein.model.StandortEnum;
import belegung.model.StockwerkEnum;
import belegung.view.BelegungErfassenViewWaedi;
import belegung.view.BelegungErfassenViewWinti;
import belegung.view.TagesübersichtBelegungViewWaedi;
import belegung.view.TagesübersichtBelegungViewWinti;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.view.BenutzungsstatistikViewBB;
import benutzungsstatistik.view.BenutzungsstatistikViewLL;
import benutzungsstatistik.view.BenutzungsstatistikViewWaedi;
import benutzungsstatistik.view.ExterneGruppeViewBB;
import benutzungsstatistik.view.InternerkurierViewWaedi;
import benutzungsstatistik.view.KorrekturViewBB;
import benutzungsstatistik.view.KorrekturViewLL;
import benutzungsstatistik.view.KorrekturViewWaedi;
import benutzungsstatistik.view.TagesübersichtBenutzungViewBB;
import benutzungsstatistik.view.TagesübersichtBenutzungViewLL;
import benutzungsstatistik.view.TagesübersichtBenutzungViewWaedi;
import benutzungsstatistik.view.WintikurierViewBB;

/**
 * StartseiteView setzt für den jeweiligen User das Layout zusammen, sodass der
 * User mit den richtigen Button zu seinen eigenen Views kommen kann
 * 
 * @author Marvin Bindemann
 */
public class StartseiteView extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Startseite";
	private AbsoluteLayout mainLayout;
	private Button bBenutzungsstatistikBB;
	private Button bBenutzungsstatistikBBUebersicht;
	private Button bBenutzungsstatistikLL;
	private Button bBenutzungsstatistikLLUebersicht;
	private Button bBenutzungsstatistikWaedi;
	private Button bBenutzungsstatistikWaediUebersicht;
	private Button bBelegungWinti;
	private Button bBelegungWintiUebersicht;
	private Button bBelegungWaedi;
	private Button bBelegungWaediUebersicht;
	private Button bExportWinti;
	private Button bExportWaedi;
	private Button bPasswort;
	private Button bLogout;
	private String user;
	private BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();

	/**
	 * Bildet das AbsoluteLayout, als Wrapper um die ganze View
	 * 
	 * @return AbsoluteLayout
	 */
	private AbsoluteLayout buildMainLayout() {
		mainLayout = new AbsoluteLayout();
		// Setzt die Hintergrundfarbe auf Grün
		mainLayout.addStyleName("backgroundErfassung");
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		return mainLayout;
	}

	/**
	 * Setzt den CompositionRoot auf ein AbsoluteLayout. Ruft initComponents auf,
	 * welches alle Komponenten dem Layout hinzufügt
	 * 
	 * @return AbsoluteLayout
	 */
	public AbsoluteLayout init() {
		AbsoluteLayout absolutLayout = buildMainLayout();
		initComponents();

		return absolutLayout;
	}

	public StartseiteView() {
		setCompositionRoot(init());
	}

	/**
	 * Initialisieren der GUI Komponente. Fügt alle Komponenten dem Layout hinzu
	 */
	private void initComponents() {

		Label lText = new Label();
		lText.setValue("Startseite");
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bBenutzungsstatistikBB = new Button();
		bBenutzungsstatistikBB.setCaption("Benutzungsstatistik BB");
		bBenutzungsstatistikBB.setIcon(VaadinIcons.POINTER);
		bBenutzungsstatistikBB.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBenutzungsstatistikBB.addClickListener(createClickListener());

		bBenutzungsstatistikBBUebersicht = new Button();
		bBenutzungsstatistikBBUebersicht.setCaption("Benutzung-Tagesübersicht");
		bBenutzungsstatistikBBUebersicht.setIcon(VaadinIcons.CLIPBOARD_TEXT);
		bBenutzungsstatistikBBUebersicht.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBenutzungsstatistikBBUebersicht.addClickListener(createClickListener());

		bBenutzungsstatistikLL = new Button();
		bBenutzungsstatistikLL.setCaption("Benutzungsstatistik LL");
		bBenutzungsstatistikLL.setIcon(VaadinIcons.POINTER);
		bBenutzungsstatistikLL.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBenutzungsstatistikLL.addClickListener(createClickListener());

		bBenutzungsstatistikLLUebersicht = new Button();
		bBenutzungsstatistikLLUebersicht.setCaption("Benutzung-Tagesübersicht");
		bBenutzungsstatistikLLUebersicht.setIcon(VaadinIcons.CLIPBOARD_TEXT);
		bBenutzungsstatistikLLUebersicht.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBenutzungsstatistikLLUebersicht.addClickListener(createClickListener());

		bBenutzungsstatistikWaedi = new Button();
		bBenutzungsstatistikWaedi.setCaption("Benutzungsstatistik Wädi");
		bBenutzungsstatistikWaedi.setIcon(VaadinIcons.POINTER);
		bBenutzungsstatistikWaedi.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBenutzungsstatistikWaedi.addClickListener(createClickListener());

		bBenutzungsstatistikWaediUebersicht = new Button();
		bBenutzungsstatistikWaediUebersicht.setCaption("Benutzung-Tagesübersicht");
		bBenutzungsstatistikWaediUebersicht.setIcon(VaadinIcons.CLIPBOARD_TEXT);
		bBenutzungsstatistikWaediUebersicht.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBenutzungsstatistikWaediUebersicht.addClickListener(createClickListener());

		bBelegungWinti = new Button();
		bBelegungWinti.setCaption("Belegung Winti");
		bBelegungWinti.setIcon(VaadinIcons.USER_CLOCK);
		bBelegungWinti.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBelegungWinti.addClickListener(createClickListener());

		bBelegungWintiUebersicht = new Button();
		bBelegungWintiUebersicht.setCaption("Belegung-Tagesübersicht");
		bBelegungWintiUebersicht.setIcon(VaadinIcons.CLIPBOARD_USER);
		bBelegungWintiUebersicht.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBelegungWintiUebersicht.addClickListener(createClickListener());

		bBelegungWaedi = new Button();
		bBelegungWaedi.setCaption("Belegung Wädi");
		bBelegungWaedi.setIcon(VaadinIcons.USER_CLOCK);
		bBelegungWaedi.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBelegungWaedi.addClickListener(createClickListener());

		bBelegungWaediUebersicht = new Button();
		bBelegungWaediUebersicht.setCaption("Belegung-Tagesübersicht");
		bBelegungWaediUebersicht.setIcon(VaadinIcons.CLIPBOARD_USER);
		bBelegungWaediUebersicht.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bBelegungWaediUebersicht.addClickListener(createClickListener());

		bExportWinti = new Button();
		bExportWinti.setCaption("Export Winti");
		bExportWinti.setIcon(VaadinIcons.DOWNLOAD);
		bExportWinti.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bExportWinti.addClickListener(createClickListener());

		bExportWaedi = new Button();
		bExportWaedi.setCaption("Export Wädi");
		bExportWaedi.setIcon(VaadinIcons.DOWNLOAD);
		bExportWaedi.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bExportWaedi.addClickListener(createClickListener());

		bPasswort = new Button();
		bPasswort.setCaption("Passwörter ändern");
		bPasswort.setIcon(VaadinIcons.PASSWORD);
		bPasswort.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " iconBenutzungHuge");
		bPasswort.addClickListener(createClickListener());

		bLogout = new Button("Logout");
		bLogout.addClickListener(createClickListener());

		// Erstellt ein GridLayout, welches je nach User andere Button anzeigt
		GridLayout grid = new GridLayout();
		// holt den jeweiligen User aus der Session
		user = VaadinSession.getCurrent().getAttribute("user").toString();
		if (user.equals("Admin Winterthur")) {
			// Admin Winterthur hat ein grösseres Layout als alle anderen User
			grid = new GridLayout(3, 3);
		} else {
			grid = new GridLayout(2, 3);
		}
		grid.setSizeFull();
		grid.addComponent(new HorizontalLayout(bLogout), 0, 0);
		grid.addComponent(lText, 1, 0);
		if (user.equals("Admin Winterthur")) {
			grid.addComponent(new Label(), 2, 0);
			grid.addComponent(bBenutzungsstatistikBB, 0, 1);
			grid.addComponent(bBenutzungsstatistikLL, 1, 1);
			grid.addComponent(bBelegungWinti, 2, 1);
			grid.addComponent(bExportWinti, 0, 2);
			grid.addComponent(bPasswort, 1, 2);
			grid.addComponent(new Label(), 2, 2);

			grid.setColumnExpandRatio(0, 0.33f);
			grid.setColumnExpandRatio(1, 0.33f);
			grid.setColumnExpandRatio(2, 0.33f);
		} else if (user.equals("Mitarbeitende Winterthur")) {
			grid.addComponent(bBenutzungsstatistikBB, 0, 1);
			grid.addComponent(bBelegungWinti, 1, 1);
			grid.addComponent(bBenutzungsstatistikBBUebersicht, 0, 2);
			grid.addComponent(bBelegungWintiUebersicht, 1, 2);
		} else if (user.equals("Studentische Mitarbeitende Winterthur")) {
			grid.addComponent(bBenutzungsstatistikLL, 0, 1);
			grid.addComponent(bBelegungWinti, 1, 1);
			grid.addComponent(bBenutzungsstatistikLLUebersicht, 0, 2);
			grid.addComponent(bBelegungWintiUebersicht, 1, 2);
		} else if (user.equals("Admin Wädenswil")) {
			grid.addComponent(bBenutzungsstatistikWaedi, 0, 1);
			grid.addComponent(bBelegungWaedi, 1, 1);
			grid.addComponent(bExportWaedi, 0, 2);
			grid.addComponent(bPasswort, 1, 2);
		} else if (user.equals("Mitarbeitende Wädenswil")) {
			grid.addComponent(bBenutzungsstatistikWaedi, 0, 1);
			grid.addComponent(bBelegungWaedi, 1, 1);
			grid.addComponent(bBenutzungsstatistikWaediUebersicht, 0, 2);
			grid.addComponent(bBelegungWaediUebersicht, 1, 2);
		}

		// Geht durch alle Zeilen und Columns und setzt die Button grösser
		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				if (row != 0) {
					c.setHeight("70%");
					c.setWidth("70%");
				} else {
					c.setWidth("70%");
				}
			}
		}
		grid.setRowExpandRatio(0, 0.1f);
		grid.setRowExpandRatio(1, 0.45f);
		grid.setRowExpandRatio(2, 0.45f);

		if (user.equals("Admin Winterthur")) {
			// Admin Winterthur hat ein grösseres Layout als alle anderen User
			grid.setColumnExpandRatio(0, 0.333f);
			grid.setColumnExpandRatio(1, 0.333f);
			grid.setColumnExpandRatio(2, 0.333f);
		} else {
			grid.setColumnExpandRatio(0, 0.5f);
			grid.setColumnExpandRatio(1, 0.5f);
		}

		mainLayout.addComponent(grid);
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bBenutzungsstatistikBB) {
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewBB.NAME);
				}

				if (e.getSource() == bBenutzungsstatistikLL) {
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewLL.NAME);
				}

				if (e.getSource() == bBenutzungsstatistikWaedi) {
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
				}

				if (e.getSource() == bBenutzungsstatistikBBUebersicht) {
					getUI().getNavigator()
							.navigateTo(TagesübersichtBenutzungViewBB.NAME + '/' + benutzungsstatistikDB
									.selectBenutzungsstatistikForDateAndStandort(new Date(), StandortEnum.WINTERTHUR_BB)
									.getBenutzungsstatistik_ID() + '/' + true);
				}

				if (e.getSource() == bBenutzungsstatistikLLUebersicht) {
					getUI().getNavigator()
							.navigateTo(TagesübersichtBenutzungViewLL.NAME + '/' + benutzungsstatistikDB
									.selectBenutzungsstatistikForDateAndStandort(new Date(), StandortEnum.WINTERTHUR_LL)
									.getBenutzungsstatistik_ID() + '/' + true);
				}

				if (e.getSource() == bBenutzungsstatistikWaediUebersicht) {
					getUI().getNavigator()
							.navigateTo(TagesübersichtBenutzungViewWaedi.NAME + '/' + benutzungsstatistikDB
									.selectBenutzungsstatistikForDateAndStandort(new Date(), StandortEnum.WÄDENSWIL)
									.getBenutzungsstatistik_ID() + '/' + true);
				}

				if (e.getSource() == bBelegungWinti) {
					if (user.equals("Studentische Mitarbeitende Winterthur")) {
						// Studentische Mitarbeiter werden automatisch zur Belegung der Lernlandschaft
						// geleitet
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + " " + '/'
								+ StockwerkEnum.LL.toString() + '/' + false + '/' + 1 + '/' + " ");
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + " " + '/'
								+ StockwerkEnum.EG.toString() + '/' + false + '/' + 0 + '/' + " ");
					}
				}

				if (e.getSource() == bBelegungWaedi) {
					getUI().getNavigator().navigateTo(BelegungErfassenViewWaedi.NAME + '/' + " " + '/'
							+ StockwerkEnum.WÄDI.toString() + '/' + false + '/' + " ");
				}

				if (e.getSource() == bBelegungWintiUebersicht) {
					if (user.equals("Studentische Mitarbeitende Winterthur")) {
						// Studentische Mitarbeiter werden automatisch zur Tagesübersichts-Belegung der
						// Lernlandschaft geleitet
						getUI().getNavigator().navigateTo(TagesübersichtBelegungViewWinti.NAME + '/'
								+ new Date().getTime() + '/' + StockwerkEnum.LL.toString() + '/' + true);
					} else {
						getUI().getNavigator().navigateTo(TagesübersichtBelegungViewWinti.NAME + '/'
								+ new Date().getTime() + '/' + StockwerkEnum.EG.toString() + '/' + true);
					}
				}

				if (e.getSource() == bBelegungWaediUebersicht) {
					getUI().getNavigator().navigateTo(TagesübersichtBelegungViewWaedi.NAME + '/' + new Date().getTime()
							+ '/' + StockwerkEnum.WÄDI.toString() + '/' + true);
				}

				if (e.getSource() == bExportWinti) {
					getUI().getNavigator().navigateTo(ExportViewWinti.NAME);
				}

				if (e.getSource() == bExportWaedi) {
					getUI().getNavigator().navigateTo(ExportViewWaedi.NAME);
				}

				if (e.getSource() == bPasswort) {
					getUI().getNavigator().navigateTo(PasswortView.NAME);
				}

				if (e.getSource() == bLogout) {
					// Wenn der User sich ausloggt, werden alle Views gelöscht
					getUI().getNavigator().removeView(StartseiteView.NAME);
					getUI().getNavigator().removeView(PasswortView.NAME);
					getUI().getNavigator().removeView(ExportViewWinti.NAME);
					getUI().getNavigator().removeView(ExportViewWaedi.NAME);
					getUI().getNavigator().removeView(BelegungErfassenViewWinti.NAME);
					getUI().getNavigator().removeView(TagesübersichtBelegungViewWinti.NAME);
					getUI().getNavigator().removeView(BenutzungsstatistikViewBB.NAME);
					getUI().getNavigator().removeView(BenutzungsstatistikViewLL.NAME);
					getUI().getNavigator().removeView(BenutzungsstatistikViewWaedi.NAME);
					getUI().getNavigator().removeView(ExterneGruppeViewBB.NAME);
					getUI().getNavigator().removeView(KorrekturViewBB.NAME);
					getUI().getNavigator().removeView(KorrekturViewLL.NAME);
					getUI().getNavigator().removeView(KorrekturViewWaedi.NAME);
					getUI().getNavigator().removeView(TagesübersichtBenutzungViewBB.NAME);
					getUI().getNavigator().removeView(TagesübersichtBenutzungViewLL.NAME);
					getUI().getNavigator().removeView(TagesübersichtBenutzungViewWaedi.NAME);
					getUI().getNavigator().removeView(WintikurierViewBB.NAME);
					getUI().getNavigator().removeView(InternerkurierViewWaedi.NAME);
					VaadinSession.getCurrent().setAttribute("user", null);
					Page.getCurrent().setUriFragment("");
				}

			}
		};

	}
}
