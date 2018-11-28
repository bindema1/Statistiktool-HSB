package allgemein.view;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.themes.ValoTheme;

import administrator.view.ExportView;
import administrator.view.PasswortView;
import belegung.model.StockwerkEnum;
import belegung.view.BelegungErfassenView;
import benutzungsstatistik.view.BenutzungsstatistikBBView;

public class StartseiteView extends Composite implements View{

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Startseite";
	private AbsoluteLayout mainLayout;
	private Button bBenutzungsstatistik;
	private Button bBelegung;
	private Button bExport;
	private Button bPasswort;

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

		bBenutzungsstatistik = new Button();
		bBenutzungsstatistik.setCaption("Benutzungsstatistik");
		bBenutzungsstatistik.addStyleName(ValoTheme.BUTTON_LARGE);
		bBenutzungsstatistik.addClickListener(createClickListener());

		bBelegung = new Button();
		bBelegung.setCaption("Belegung");
		bBelegung.addStyleName(ValoTheme.BUTTON_LARGE);
		bBelegung.addClickListener(createClickListener());
		
		bExport = new Button();
		bExport.setCaption("Export");
		bExport.addStyleName(ValoTheme.BUTTON_LARGE);
		bExport.addClickListener(createClickListener());
		
		bPasswort = new Button();
		bPasswort.setCaption("Passwörter ändern");
		bPasswort.addStyleName(ValoTheme.BUTTON_LARGE);
		bPasswort.addClickListener(createClickListener());

		GridLayout grid = new GridLayout(2, 2);
		grid.setSizeFull();
		grid.addComponent(bBenutzungsstatistik, 0, 0);
		grid.addComponent(bBelegung, 1, 0);
		grid.addComponent(bExport, 0, 1);
		grid.addComponent(bPasswort, 1, 1);

		for (int col = 0; col < grid.getColumns(); col++) {
			for (int row = 0; row < grid.getRows(); row++) {
				Component c = grid.getComponent(col, row);
				grid.setComponentAlignment(c, Alignment.MIDDLE_CENTER);

				c.setHeight("50%");
				c.setWidth("50%");
			}
		}

		mainLayout.addComponent(grid);
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bBenutzungsstatistik) {
					getUI().getNavigator().navigateTo(BenutzungsstatistikBBView.NAME);
				}

				if (e.getSource() == bBelegung) {
					getUI().getNavigator().navigateTo(BelegungErfassenView.NAME + '/' + " " + '/' + StockwerkEnum.EG.toString() + '/' + false + '/' + 0 + '/' +" ");
				}
				
				if (e.getSource() == bExport) {
					getUI().getNavigator().navigateTo(ExportView.NAME);
				}
				
				if (e.getSource() == bPasswort) {
					getUI().getNavigator().navigateTo(PasswortView.NAME);
				}

			}
		};

	}
}
