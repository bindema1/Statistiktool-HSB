package allgemein.view;

import java.io.InputStream;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import administrator.view.ExportViewWaedi;
import administrator.view.ExportViewWinti;
import administrator.view.PasswortView;
import allgemein.model.SpringEmailService;
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
import testdaten.TestDaten;

/**
 * MainView ist die Anfangsklasse, welche das VaadinServlet aufsetzt.
 * 
 * @author Marvin Bindemann
 */
@SuppressWarnings("deprecation")
@Theme("mytheme")
public class MainView extends UI {

	@Autowired
    private static SpringEmailService springEmailService;
    
	private static final long serialVersionUID = 1L;

	protected void init(VaadinRequest request) {

		// Zu Testzwecken werden hier Testdaten geladen. Später gibt es dafür ein
		// SQL-Skript
		BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();
		if (benutzungsstatistikDB.selectAllBenutzungsstatistiken().size() == 0) {
			new TestDaten();
		}

		// Ein Navigator wird gesetzt, sodass verschiedene Views gesetzt werden können
		// und die Navigation per URL funktioniert
		new Navigator(this, this);
		// Am Anfang gelangt man automatisch zur LoginPage
		getNavigator().addView(LoginPage.NAME, LoginPage.class);
		getNavigator().setErrorView(LoginPage.class);

		// Falls man eine eigene URL eingibt
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
		private static final long serialVersionUID = 5334673737128772893L;
	}

	private void router(String route) {

		System.out.println("ROUTE: " + route);

		// Wenn der User existiert
		if (getSession().getAttribute("user") != null) {
			// Alle Views hinzufügen
			getNavigator().addView(StartseiteView.NAME, StartseiteView.class);
			getNavigator().addView(PasswortView.NAME, PasswortView.class);
			getNavigator().addView(ExportViewWinti.NAME, ExportViewWinti.class);
			getNavigator().addView(ExportViewWaedi.NAME, ExportViewWaedi.class);
			getNavigator().addView(BelegungErfassenViewWinti.NAME, BelegungErfassenViewWinti.class);
			getNavigator().addView(TagesübersichtBelegungViewWinti.NAME, TagesübersichtBelegungViewWinti.class);
			getNavigator().addView(BelegungErfassenViewWaedi.NAME, BelegungErfassenViewWaedi.class);
			getNavigator().addView(TagesübersichtBelegungViewWaedi.NAME, TagesübersichtBelegungViewWaedi.class);
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

			// Wenn der User eine bestimmte URL eingegeben hat, wird es hier auf die
			// richtige URL weitergeleitet
			if (route.equals("!" + PasswortView.NAME)) {
				getNavigator().navigateTo(PasswortView.NAME);
			} else if (route.equals("!" + ExportViewWinti.NAME)) {
				getNavigator().navigateTo(ExportViewWinti.NAME);
			} else if (route.equals("!" + ExportViewWaedi.NAME)) {
				getNavigator().navigateTo(ExportViewWaedi.NAME);
			} else if (route.equals("!" + BenutzungsstatistikViewBB.NAME)) {
				getNavigator().navigateTo(BenutzungsstatistikViewBB.NAME);
			} else if (route.equals("!" + BenutzungsstatistikViewLL.NAME)) {
				getNavigator().navigateTo(BenutzungsstatistikViewLL.NAME);
			} else if (route.equals("!" + BenutzungsstatistikViewWaedi.NAME)) {
				getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
			} else if (route.equals("!" + BelegungErfassenViewWinti.NAME)) {
				getNavigator().navigateTo(BelegungErfassenViewWinti.NAME);
			} else if (route.equals("!" + BelegungErfassenViewWaedi.NAME)) {
				getNavigator().navigateTo(BelegungErfassenViewWaedi.NAME);
			} else {
				getNavigator().navigateTo(StartseiteView.NAME);
			}
		} else {
			getUI().getSession().setAttribute("route", route);
			// Falls es keinen User gibt, wird es automatisch zur LoginPage geleitet
			getNavigator().navigateTo(LoginPage.NAME);
		}
	}
	
	/**
	 * Sendet eine Email an die Bibliothek Winterthur oder Wädenswil
	 * 
	 * @param to ausleihe.winterthur.hsb@zhaw.ch || waedenswil.hsb@zhaw.ch
	 */
	@SuppressWarnings({ "static-access" })
	public static void sendEmail(String to, String subject, String text) {
        try {
            String from = "sender@test.com";

            springEmailService.send(from, to, subject, text, null, null, null);

            Notification.show("Email gesendet");

        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Error sending the email", Notification.Type.ERROR_MESSAGE);
        }
    }

}
