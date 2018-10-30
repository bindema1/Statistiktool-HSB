package allgemein.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.themes.ValoTheme;

import Testdaten.TestDaten;
import belegung.model.StockwerkEnum;
import belegung.view.BelegungErfassenView;
import benutzungsstatistik.view.BenutzungsstatistikBBView;

public class StartseiteView implements View{

	private AbsoluteLayout mainLayout;
	private Button bBenutzungsstatistik;
	private Button bBelegung;

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		return mainLayout;
	}

	public AbsoluteLayout init(MainView mainView) {
		// common part: create layout
		AbsoluteLayout absolutLayout = buildMainLayout();
		initData();
		initComponents(mainView);

		return absolutLayout;
	}
	
	public StartseiteView() {
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

		GridLayout grid = new GridLayout(2, 1);
		grid.setSizeFull();
		grid.addComponent(bBenutzungsstatistik, 0, 0);
		grid.addComponent(bBelegung, 1, 0);

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
					mainView.setContent(new BelegungErfassenView(StockwerkEnum.EG, false).init(mainView));
				}

			}
		};

	}
}
