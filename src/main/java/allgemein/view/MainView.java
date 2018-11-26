package allgemein.view;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import administrator.view.ExportView;
import administrator.view.PasswortView;
import belegung.view.BelegungErfassenView;
import belegung.view.TagesübersichtBelegungView;
import benutzungsstatistik.view.BenutzungsstatistikBBView;
import benutzungsstatistik.view.ExterneGruppeView;
import benutzungsstatistik.view.KorrekturView;
import benutzungsstatistik.view.TagesübersichtBenutzungView;
import benutzungsstatistik.view.WintikurierView;
import testdaten.TestDaten;

/**
 * MainView setzt den Content und gibt seine eigene MainView an die
 * entsprechende View weiter
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class MainView extends UI {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	protected void init(VaadinRequest request) {

		//Zu Testzwecken werden hier Testdaten geladen. Später gibt es dafür ein SQL-Skript
		new TestDaten();
		
		new Navigator(this, this);

		getNavigator().addView(LoginPage.NAME, LoginPage.class);
		getNavigator().setErrorView(LoginPage.class);

		Page.getCurrent().addUriFragmentChangedListener(new UriFragmentChangedListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void uriFragmentChanged(UriFragmentChangedEvent event) {
				router(event.getUriFragment());
			}
		});

		router("");
	}

	@WebServlet(urlPatterns = "/*", name = "MyServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MainView.class, productionMode = true)
	public static class MyServlet extends VaadinServlet {
	}

	private void router(String route) {

		System.out.println("ROUTE: " +route);
		
		if (getSession().getAttribute("user") != null) {
			getNavigator().addView(StartseiteView.NAME, StartseiteView.class);
			getNavigator().addView(PasswortView.NAME, PasswortView.class);
			getNavigator().addView(ExportView.NAME, ExportView.class);
			getNavigator().addView(BelegungErfassenView.NAME, BelegungErfassenView.class);
			getNavigator().addView(TagesübersichtBelegungView.NAME, TagesübersichtBelegungView.class);
			getNavigator().addView(BenutzungsstatistikBBView.NAME, BenutzungsstatistikBBView.class);
			getNavigator().addView(ExterneGruppeView.NAME, ExterneGruppeView.class);
			getNavigator().addView(KorrekturView.NAME, KorrekturView.class);
			getNavigator().addView(TagesübersichtBenutzungView.NAME, TagesübersichtBenutzungView.class);
			getNavigator().addView(WintikurierView.NAME, WintikurierView.class);
			
			if (route.equals("!"+PasswortView.NAME)) {
				getNavigator().navigateTo(PasswortView.NAME);
			} else if (route.equals("!"+ExportView.NAME)) {
				getNavigator().navigateTo(ExportView.NAME);
			} else if (route.equals("!"+BenutzungsstatistikBBView.NAME)) {
				getNavigator().navigateTo(BenutzungsstatistikBBView.NAME);
			} else if (route.equals("!"+KorrekturView.NAME)) {
				getNavigator().navigateTo(KorrekturView.NAME);
			} else if (route.equals("!"+TagesübersichtBenutzungView.NAME)) {
				getNavigator().navigateTo(TagesübersichtBenutzungView.NAME);
			} else if (route.equals("!"+WintikurierView.NAME)) {
				getNavigator().navigateTo(WintikurierView.NAME);
			} else if (route.equals("!"+ExterneGruppeView.NAME)) {
				getNavigator().navigateTo(ExterneGruppeView.NAME);
			} else if (route.equals("!"+TagesübersichtBelegungView.NAME)) {
				getNavigator().navigateTo(TagesübersichtBelegungView.NAME);
			} else if (route.equals("!"+BelegungErfassenView.NAME)) {
				getNavigator().navigateTo(BelegungErfassenView.NAME);
			} else {
				getNavigator().navigateTo(StartseiteView.NAME);
			}
		} else {
			getUI().getSession().setAttribute("route", route);
			getNavigator().navigateTo(LoginPage.NAME);
		}
	}

}
