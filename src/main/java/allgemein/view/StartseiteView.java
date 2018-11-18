package allgemein.view;

import java.util.Date;

import com.vaadin.navigator.View;
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
import testdaten.TestDaten;

@SuppressWarnings("serial")
public class StartseiteView extends Composite implements View{

	private AbsoluteLayout mainLayout;
	private Button bBenutzungsstatistik;
	private Button bBelegung;
	private Button bExport;
	private Button bPasswort;
	private MainView mainView;

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		return mainLayout;
	}

	public AbsoluteLayout init(MainView mainView) {
		// common part: create layout
		this.mainView = mainView;
		AbsoluteLayout absolutLayout = buildMainLayout();
		initData();
		initComponents(mainView);

		return absolutLayout;
	}
	
	public StartseiteView() {
		
		//setCompositionRoot(init(mainView));
		
		//Zu Testzwecken werden hier Testdaten geladen. Später gibt es dafür ein SQL-Skript
		new TestDaten();
	}

	private void initData() {

	}

	// Initialisieren der GUI Komponente
	private void initComponents(MainView mainView) {

		bBenutzungsstatistik = new Button();
		bBenutzungsstatistik.setCaption("Benutzungsstatistik");
		bBenutzungsstatistik.addStyleName(ValoTheme.BUTTON_LARGE);
		bBenutzungsstatistik.addClickListener(createClickListener(mainView));

		bBelegung = new Button();
		bBelegung.setCaption("Belegung");
		bBelegung.addStyleName(ValoTheme.BUTTON_LARGE);
		bBelegung.addClickListener(createClickListener(mainView));
		
		bExport = new Button();
		bExport.setCaption("Export");
		bExport.addStyleName(ValoTheme.BUTTON_LARGE);
		bExport.addClickListener(createClickListener(mainView));
		
		bPasswort = new Button();
		bPasswort.setCaption("Passwörter ändern");
		bPasswort.addStyleName(ValoTheme.BUTTON_LARGE);
		bPasswort.addClickListener(createClickListener(mainView));

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
	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bBenutzungsstatistik) {
					mainView.setContent(new BenutzungsstatistikBBView().init(mainView));
				}

				if (e.getSource() == bBelegung) {
					mainView.setContent(new BelegungErfassenView(new Date(), StockwerkEnum.EG, false, 0).init(mainView));
				}
				
				if (e.getSource() == bExport) {
					mainView.setContent(new ExportView().init(mainView));
				}
				
				if (e.getSource() == bPasswort) {
					mainView.setContent(new PasswortView().init(mainView));
				}

			}
		};

	}
}
