package allgemein.view;

import com.vaadin.navigator.View;
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
import benutzungsstatistik.view.BenutzungsstatistikViewBB;

public class StartseiteView extends Composite implements View{

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Startseite";
	private AbsoluteLayout mainLayout;
	private Button bBenutzungsstatistikBB;
	private Button bBelegungWini;
	private Button bExportWinti;
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

		bBenutzungsstatistikBB = new Button();
		bBenutzungsstatistikBB.setCaption("Benutzungsstatistik BB");
		bBenutzungsstatistikBB.addStyleName(ValoTheme.BUTTON_LARGE);
		bBenutzungsstatistikBB.addClickListener(createClickListener());

		bBelegungWini = new Button();
		bBelegungWini.setCaption("Belegung Winti");
		bBelegungWini.addStyleName(ValoTheme.BUTTON_LARGE);
		bBelegungWini.addClickListener(createClickListener());
		
		bExportWinti = new Button();
		bExportWinti.setCaption("Export Winti");
		bExportWinti.addStyleName(ValoTheme.BUTTON_LARGE);
		bExportWinti.addClickListener(createClickListener());
		
		bPasswort = new Button();
		bPasswort.setCaption("Passwörter ändern");
		bPasswort.addStyleName(ValoTheme.BUTTON_LARGE);
		bPasswort.addClickListener(createClickListener());

		
		//Erstellt ein GridLayout, welches je nach User andere Button anzeigt
		GridLayout grid = new GridLayout(2, 2);
		grid.setSizeFull();
		String user = VaadinSession.getCurrent().getAttribute("user").toString();
		if (user.equals("Admin Winterthur")) {
			grid.addComponent(bBenutzungsstatistikBB, 0, 0);
			grid.addComponent(bBelegungWini, 1, 0);
			grid.addComponent(bExportWinti, 0, 1);
			grid.addComponent(bPasswort, 1, 1);
		}else if(user.equals("Mitarbeitende Winterthur")) {
			grid.addComponent(bBenutzungsstatistikBB, 0, 0);
			grid.addComponent(bBelegungWini, 1, 0);
			grid.addComponent(new Label(), 0, 1);
			grid.addComponent(new Label(), 1, 1);
		}else if(user.equals("Studentische Mitarbeitende Winterthur")) {
			grid.addComponent(new Label(), 0, 0);
			grid.addComponent(bBelegungWini, 1, 0);
			grid.addComponent(new Label(), 0, 1);
			grid.addComponent(new Label(), 1, 1);
		}else if(user.equals("Admin Wädenswil")) {
			grid.addComponent(bPasswort, 1, 1);
		}else if(user.equals("Mitarbeitende Wädenswil")) {
			
		}else 
			
		System.out.println(grid.getColumns() +"  " +grid.getRows());

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
				if (e.getSource() == bBenutzungsstatistikBB) {
					getUI().getNavigator().navigateTo(BenutzungsstatistikViewBB.NAME);
				}

				if (e.getSource() == bBelegungWini) {
					getUI().getNavigator().navigateTo(BelegungErfassenViewWinti.NAME + '/' + " " + '/' + StockwerkEnum.EG.toString() + '/' + false + '/' + 0 + '/' +" ");
				}
				
				if (e.getSource() == bExportWinti) {
					getUI().getNavigator().navigateTo(ExportView.NAME);
				}
				
				if (e.getSource() == bPasswort) {
					getUI().getNavigator().navigateTo(PasswortView.NAME);
				}

			}
		};

	}
}
