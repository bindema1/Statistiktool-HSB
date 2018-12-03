package allgemein.view;

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
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import administrator.view.ExportView;
import administrator.view.PasswortView;
import belegung.model.StockwerkEnum;
import belegung.view.BelegungErfassenViewWinti;
import belegung.view.TagesübersichtBelegungViewWinti;
import benutzungsstatistik.view.BenutzungsstatistikViewBB;
import benutzungsstatistik.view.BenutzungsstatistikViewLL;
import benutzungsstatistik.view.ExterneGruppeViewBB;
import benutzungsstatistik.view.KorrekturViewBB;
import benutzungsstatistik.view.KorrekturViewLL;
import benutzungsstatistik.view.TagesübersichtBenutzungViewBB;
import benutzungsstatistik.view.TagesübersichtBenutzungViewLL;
import benutzungsstatistik.view.WintikurierViewBB;

public class StartseiteView extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Startseite";
	private AbsoluteLayout mainLayout;
	private Button bBenutzungsstatistikBB;
	private Button bBenutzungsstatistikLL;
	private Button bBelegungWinti;
	private Button bExportWinti;
	private Button bPasswort;
	private Button bLogout;
	private String user;

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		return mainLayout;
	}

	public AbsoluteLayout init() {
		// common part: create layout
		AbsoluteLayout absolutLayout = buildMainLayout();
		initData();
		initComponents();

		return absolutLayout;
	}

	public StartseiteView() {
		setCompositionRoot(init());
	}

	private void initData() {

	}

	// Initialisieren der GUI Komponente
	private void initComponents() {

		Label lText = new Label();
		lText.setValue("Startseite");
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		bBenutzungsstatistikBB = new Button();
		bBenutzungsstatistikBB.setCaption("Benutzungsstatistik BB");
		bBenutzungsstatistikBB.addStyleName(ValoTheme.BUTTON_LARGE);
		bBenutzungsstatistikBB.addClickListener(createClickListener());

		bBenutzungsstatistikLL = new Button();
		bBenutzungsstatistikLL.setCaption("Benutzungsstatistik LL");
		bBenutzungsstatistikLL.addStyleName(ValoTheme.BUTTON_LARGE);
		bBenutzungsstatistikLL.addClickListener(createClickListener());

		bBelegungWinti = new Button();
		bBelegungWinti.setCaption("Belegung Winti");
		bBelegungWinti.addStyleName(ValoTheme.BUTTON_LARGE);
		bBelegungWinti.addClickListener(createClickListener());

		bExportWinti = new Button();
		bExportWinti.setCaption("Export Winti");
		bExportWinti.addStyleName(ValoTheme.BUTTON_LARGE);
		bExportWinti.addClickListener(createClickListener());

		bPasswort = new Button();
		bPasswort.setCaption("Passwörter ändern");
		bPasswort.addStyleName(ValoTheme.BUTTON_LARGE);
		bPasswort.addClickListener(createClickListener());

		bLogout = new Button("Logout");
		bLogout.addClickListener(createClickListener());

		// Erstellt ein GridLayout, welches je nach User andere Button anzeigt
		GridLayout grid = new GridLayout(3, 3);
		grid.setSizeFull();
		user = VaadinSession.getCurrent().getAttribute("user").toString();
		grid.addComponent(bLogout, 0, 0);
		grid.addComponent(lText, 1, 0);
		grid.addComponent(new Label(), 2, 0);
		if (user.equals("Admin Winterthur")) {
			grid.addComponent(bBenutzungsstatistikBB, 0, 1);
			grid.addComponent(bBenutzungsstatistikLL, 1, 1);
			grid.addComponent(bBelegungWinti, 2, 1);
			grid.addComponent(bExportWinti, 0, 2);
			grid.addComponent(bPasswort, 1, 2);
			grid.addComponent(new Label(), 2, 2);
		} else if (user.equals("Mitarbeitende Winterthur")) {
			grid.addComponent(bBenutzungsstatistikBB, 0, 1);
			grid.addComponent(bBelegungWinti, 1, 1);
			grid.addComponent(new Label(), 2, 1);
			grid.addComponent(new Label(), 0, 2);
			grid.addComponent(new Label(), 1, 2);
			grid.addComponent(new Label(), 2, 2);
		} else if (user.equals("Studentische Mitarbeitende Winterthur")) {
			grid.addComponent(bBenutzungsstatistikLL, 0, 1);
			grid.addComponent(bBelegungWinti, 1, 1);
			grid.addComponent(new Label(), 2, 1);
			grid.addComponent(new Label(), 0, 2);
			grid.addComponent(new Label(), 1, 2);
			grid.addComponent(new Label(), 2, 2);
		} else if (user.equals("Admin Wädenswil")) {
			grid.addComponent(bPasswort, 1, 2);
		} else if (user.equals("Mitarbeitende Wädenswil")) {

		}

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				if (row != 0) {
					c.setHeight("70%");
					c.setWidth("70%");
				}
			}
		}
		grid.setRowExpandRatio(0, 0.1f);
		grid.setRowExpandRatio(1, 0.45f);
		grid.setRowExpandRatio(2, 0.45f);

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

				if (e.getSource() == bBelegungWinti) {
					if (user.equals("Studentische Mitarbeitende Winterthur")) {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + " " + '/'
								+ StockwerkEnum.LL.toString() + '/' + false + '/' + 1 + '/' + " ");
					} else {
						getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + " " + '/'
								+ StockwerkEnum.EG.toString() + '/' + false + '/' + 0 + '/' + " ");
					}
				}

				if (e.getSource() == bExportWinti) {
					getUI().getNavigator().navigateTo(ExportView.NAME);
				}

				if (e.getSource() == bPasswort) {
					getUI().getNavigator().navigateTo(PasswortView.NAME);
				}

				if (e.getSource() == bLogout) {
					getUI().getNavigator().removeView(StartseiteView.NAME);
					getUI().getNavigator().removeView(PasswortView.NAME);
					getUI().getNavigator().removeView(ExportView.NAME);
					getUI().getNavigator().removeView(BelegungErfassenViewWinti.NAME);
					getUI().getNavigator().removeView(TagesübersichtBelegungViewWinti.NAME);
					getUI().getNavigator().removeView(BenutzungsstatistikViewBB.NAME);
					getUI().getNavigator().removeView(BenutzungsstatistikViewLL.NAME);
					getUI().getNavigator().removeView(ExterneGruppeViewBB.NAME);
					getUI().getNavigator().removeView(KorrekturViewBB.NAME);
					getUI().getNavigator().removeView(KorrekturViewLL.NAME);
					getUI().getNavigator().removeView(TagesübersichtBenutzungViewBB.NAME);
					getUI().getNavigator().removeView(TagesübersichtBenutzungViewLL.NAME);
					getUI().getNavigator().removeView(WintikurierViewBB.NAME);
					VaadinSession.getCurrent().setAttribute("user", null);
					Page.getCurrent().setUriFragment("");
				}

			}
		};

	}
}
