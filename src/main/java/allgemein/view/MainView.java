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

import administrator.view.ExportViewWaedi;
import administrator.view.ExportViewWinti;
import administrator.view.PasswortView;
import belegung.view.BelegungErfassenViewWinti;
import belegung.view.TagesübersichtBelegungViewWinti;
import benutzungsstatistik.view.BenutzungsstatistikViewBB;
import benutzungsstatistik.view.BenutzungsstatistikViewLL;
import benutzungsstatistik.view.BenutzungsstatistikViewWaedi;
import benutzungsstatistik.view.ExterneGruppeViewBB;
import benutzungsstatistik.view.KorrekturViewBB;
import benutzungsstatistik.view.KorrekturViewLL;
import benutzungsstatistik.view.KorrekturViewWaedi;
import benutzungsstatistik.view.TagesübersichtBenutzungViewBB;
import benutzungsstatistik.view.TagesübersichtBenutzungViewLL;
import benutzungsstatistik.view.TagesübersichtBenutzungViewWaedi;
import benutzungsstatistik.view.WintikurierViewBB;
import benutzungsstatistik.view.InternerkurierViewWaedi;
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
	@VaadinServletConfiguration(ui = MainView.class, productionMode = false)
	public static class MyServlet extends VaadinServlet {
	}

	private void router(String route) {

//		System.out.println("ROUTE: " +route);
		
		if (getSession().getAttribute("user") != null) {
			getNavigator().addView(StartseiteView.NAME, StartseiteView.class);
			getNavigator().addView(PasswortView.NAME, PasswortView.class);
			getNavigator().addView(ExportViewWinti.NAME, ExportViewWinti.class);
			getNavigator().addView(ExportViewWaedi.NAME, ExportViewWaedi.class);
			getNavigator().addView(BelegungErfassenViewWinti.NAME, BelegungErfassenViewWinti.class);
			getNavigator().addView(TagesübersichtBelegungViewWinti.NAME, TagesübersichtBelegungViewWinti.class);
			getNavigator().addView(BenutzungsstatistikViewBB.NAME, BenutzungsstatistikViewBB.class);
			getNavigator().addView(BenutzungsstatistikViewLL.NAME, BenutzungsstatistikViewLL.class);
			getNavigator().addView(BenutzungsstatistikViewWaedi.NAME, BenutzungsstatistikViewWaedi.class);
			getNavigator().addView(ExterneGruppeViewBB.NAME, ExterneGruppeViewBB.class);
			getNavigator().addView(KorrekturViewBB.NAME, KorrekturViewBB.class);
			getNavigator().addView(KorrekturViewLL.NAME, KorrekturViewLL.class);
			getNavigator().addView(KorrekturViewWaedi.NAME, KorrekturViewWaedi.class);
			getNavigator().addView(TagesübersichtBenutzungViewBB.NAME, TagesübersichtBenutzungViewBB.class);
			getNavigator().addView(TagesübersichtBenutzungViewLL.NAME, TagesübersichtBenutzungViewLL.class);
			getNavigator().addView(TagesübersichtBenutzungViewWaedi.NAME, TagesübersichtBenutzungViewWaedi.class);
			getNavigator().addView(WintikurierViewBB.NAME, WintikurierViewBB.class);
			getNavigator().addView(InternerkurierViewWaedi.NAME, InternerkurierViewWaedi.class);
			
			if (route.equals("!"+PasswortView.NAME)) {
				getNavigator().navigateTo(PasswortView.NAME);
			} else if (route.equals("!"+ExportViewWinti.NAME)) {
				getNavigator().navigateTo(ExportViewWinti.NAME);
			} else if (route.equals("!"+ExportViewWaedi.NAME)) {
				getNavigator().navigateTo(ExportViewWaedi.NAME);
			} else if (route.equals("!"+BenutzungsstatistikViewBB.NAME)) {
				getNavigator().navigateTo(BenutzungsstatistikViewBB.NAME);
			} else if (route.equals("!"+BenutzungsstatistikViewLL.NAME)) {
				getNavigator().navigateTo(BenutzungsstatistikViewLL.NAME);
			} else if (route.equals("!"+BenutzungsstatistikViewWaedi.NAME)) {
				getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
//			} else if (route.equals("!"+KorrekturViewBB.NAME)) {
//				getNavigator().navigateTo(KorrekturViewBB.NAME);
//			} else if (route.equals("!"+KorrekturViewLL.NAME)) {
//				getNavigator().navigateTo(KorrekturViewLL.NAME);
//			} else if (route.equals("!"+TagesübersichtBenutzungViewBB.NAME)) {
//				getNavigator().navigateTo(TagesübersichtBenutzungViewBB.NAME);
//			} else if (route.equals("!"+TagesübersichtBenutzungViewLL.NAME)) {
//				getNavigator().navigateTo(TagesübersichtBenutzungViewLL.NAME);
//			} else if (route.equals("!"+WintikurierViewBB.NAME)) {
//				getNavigator().navigateTo(WintikurierViewBB.NAME);
//			} else if (route.equals("!"+ExterneGruppeViewBB.NAME)) {
//				getNavigator().navigateTo(ExterneGruppeViewBB.NAME);
//			} else if (route.equals("!"+TagesübersichtBelegungViewWinti.NAME)) {
//				getNavigator().navigateTo(TagesübersichtBelegungViewWinti.NAME);
			} else if (route.equals("!"+BelegungErfassenViewWinti.NAME)) {
				getNavigator().navigateTo(BelegungErfassenViewWinti.NAME);
			} else {
				getNavigator().navigateTo(StartseiteView.NAME);
			}
		} else {
			getUI().getSession().setAttribute("route", route);
			getNavigator().navigateTo(LoginPage.NAME);
		}
	}

}
