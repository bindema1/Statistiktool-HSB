package allgemein.view;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import administrator.view.ExportViewWaedi;
import administrator.view.ExportViewWinti;
import administrator.view.PasswortView;
import allgemein.db.AngestellterDatenbank;
import allgemein.service.ScheduledExecutorEmailService;
import belegung.view.BelegungErfassenViewWaedi;
import belegung.view.BelegungErfassenViewWinti;
import benutzungsstatistik.view.BenutzungsstatistikViewBB;
import benutzungsstatistik.view.BenutzungsstatistikViewLL;
import benutzungsstatistik.view.BenutzungsstatistikViewWaedi;
import testdaten.TestDaten;

/**
 * MainView ist die Anfangsklasse, welche das VaadinServlet aufsetzt.
 * 
 * @author Marvin Bindemann
 */
@SuppressWarnings("deprecation")
@Theme("mytheme")
public class MainView extends UI {

	private static final long serialVersionUID = 1L;

	protected void init(VaadinRequest request) {

		// Zu Testzwecken werden hier Testdaten geladen. Später gibt es dafür ein
		// SQL-Skript
		AngestellterDatenbank angestellterDB = new AngestellterDatenbank();
		if (angestellterDB.selectAllAngestellte().size() == 0) {
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

		// Ein Task, welcher im Hintergrund läuft und leere Belegungen sowie den
		// Kassenbeleg prüft
		ScheduledExecutorEmailService.sendeMailWegenLeererBelegungOderWegenKassenbeleg();
	}

	@WebServlet(urlPatterns = "/*", name = "MyServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MainView.class, productionMode = false)
	public static class MyServlet extends VaadinServlet {
		private static final long serialVersionUID = 5334673737128772893L;

		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();
			getService().addSessionInitListener(new SessionInitListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void sessionInit(SessionInitEvent event) {
					event.getSession().addBootstrapListener(new BootstrapListener() {

						private static final long serialVersionUID = 1L;

						@Override
						public void modifyBootstrapFragment(BootstrapFragmentResponse response) {

						}

						@Override
						public void modifyBootstrapPage(BootstrapPageResponse response) {
							// Das wird benutzt, damit man auf dem iPad bzw. auf mobilen Geräten nicht
							// zoomen kann
							response.getDocument().head().prependElement("meta").attr("name", "viewport").attr(
									"content",
									"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no");
						}
					});
				}
			});
		}
	}

	private void router(String route) {

		// Wenn der User existiert
		if (getUI().getSession().getAttribute("user") != null) {
			// Alle Views hinzufügen
			getNavigator().addView(StartseiteView.NAME, StartseiteView.class);
//			getNavigator().addView(PasswortView.NAME, PasswortView.class);
//			getNavigator().addView(ExportViewWinti.NAME, ExportViewWinti.class);
//			getNavigator().addView(ExportViewWaedi.NAME, ExportViewWaedi.class);
//			getNavigator().addView(BelegungErfassenViewWinti.NAME, BelegungErfassenViewWinti.class);
//			getNavigator().addView(TagesübersichtBelegungViewWinti.NAME, TagesübersichtBelegungViewWinti.class);
//			getNavigator().addView(BelegungErfassenViewWaedi.NAME, BelegungErfassenViewWaedi.class);
//			getNavigator().addView(TagesübersichtBelegungViewWaedi.NAME, TagesübersichtBelegungViewWaedi.class);
//			getNavigator().addView(BenutzungsstatistikViewBB.NAME, BenutzungsstatistikViewBB.class);
//			getNavigator().addView(BenutzungsstatistikViewLL.NAME, BenutzungsstatistikViewLL.class);
//			getNavigator().addView(BenutzungsstatistikViewWaedi.NAME, BenutzungsstatistikViewWaedi.class);
//			getNavigator().addView(ExterneGruppeViewBB.NAME, ExterneGruppeViewBB.class);
//			getNavigator().addView(KorrekturViewBB.NAME, KorrekturViewBB.class);
//			getNavigator().addView(KorrekturViewLL.NAME, KorrekturViewLL.class);
//			getNavigator().addView(KorrekturViewWaedi.NAME, KorrekturViewWaedi.class);
//			getNavigator().addView(TagesübersichtBenutzungViewBB.NAME, TagesübersichtBenutzungViewBB.class);
//			getNavigator().addView(TagesübersichtBenutzungViewLL.NAME, TagesübersichtBenutzungViewLL.class);
//			getNavigator().addView(TagesübersichtBenutzungViewWaedi.NAME, TagesübersichtBenutzungViewWaedi.class);
//			getNavigator().addView(WintikurierViewBB.NAME, WintikurierViewBB.class);
//			getNavigator().addView(InternerkurierViewWaedi.NAME, InternerkurierViewWaedi.class);

			// Wenn der User eine bestimmte URL eingegeben hat, wird es hier auf die
			// richtige URL weitergeleitet
			if (route.contains("!" + PasswortView.NAME)) {
				getNavigator().navigateTo(PasswortView.NAME);
			} else if (route.contains("!" + ExportViewWinti.NAME)) {
				getNavigator().navigateTo(ExportViewWinti.NAME);
			} else if (route.contains("!" + ExportViewWaedi.NAME)) {
				getNavigator().navigateTo(ExportViewWaedi.NAME);
			} else if (route.contains("!" + BenutzungsstatistikViewBB.NAME)) {
				getNavigator().navigateTo(BenutzungsstatistikViewBB.NAME);
			} else if (route.contains("!" + BenutzungsstatistikViewLL.NAME)) {
				getNavigator().navigateTo(BenutzungsstatistikViewLL.NAME);
			} else if (route.contains("!" + BenutzungsstatistikViewWaedi.NAME)) {
				getNavigator().navigateTo(BenutzungsstatistikViewWaedi.NAME);
			} else if (route.contains("!" + BelegungErfassenViewWinti.NAME)) {
				getNavigator().navigateTo(BelegungErfassenViewWinti.NAME);
			} else if (route.contains("!" + BelegungErfassenViewWaedi.NAME)) {
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

}
