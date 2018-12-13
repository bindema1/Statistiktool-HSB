package allgemein.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;

import administrator.view.ExportViewWaedi;
import administrator.view.ExportViewWinti;
import administrator.view.PasswortView;
import allgemein.db.AngestellterDatenbank;
import allgemein.model.Angestellter;
import allgemein.service.ScheduledExecutorEmailService;
import belegung.view.BelegungErfassenViewWaedi;
import belegung.view.BelegungErfassenViewWinti;
import belegung.view.TagesübersichtBelegungViewWaedi;
import belegung.view.TagesübersichtBelegungViewWinti;
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
 * LoginPage setzt das Layout zum einloggen. Ausserdem erstellt es alle Views
 * und lässt nur die jeweiligen User zu der Seite wo sie Zugriff haben
 * 
 * @author Marvin Bindemann
 */
public class LoginPage extends VerticalLayout implements View {
	
	private static final long serialVersionUID = 1L;
	public static final String NAME = "";
	private AngestellterDatenbank angestellterDB = new AngestellterDatenbank();
//	private MD5 md5 = new MD5();
	private NativeSelect<String> username;

	public LoginPage() {
		// Erstellt ein Panel für das Login
		Panel panel = new Panel("Login");
		panel.setSizeUndefined();
		addComponent(panel);

		FormLayout content = new FormLayout();
		// Dropdown aller Angestellten
		List<String> data = new ArrayList<>();
		data.add("Bitte wählen ↓");
		for (Angestellter a : angestellterDB.selectAllAngestellte()) {
			data.add(a.getName());
		}
		username = new NativeSelect<>("User", data);
		username.setWidth(100.0f, Unit.PERCENTAGE);
		username.setSelectedItem(data.get(0));
		username.setEmptySelectionAllowed(false);
		content.addComponent(username);
		PasswordField password = new PasswordField("Passwort");
		password.setValue("123");
		content.addComponent(password);

		Button send = new Button("Login");
		send.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				Angestellter angestellter = angestellterDB.getAngestellterByName(username.getValue());

				if (angestellter != null) {
					// Wenn der Angestellte in der Datenbank existiert und das Passwort Richtig ist
					if (angestellter.getPasswort().equals(password.getValue())) {

						// Prüft ob das Passwort älter als ein halbes Jahr ist
						pruefePasswortDatum(angestellter);

						// Speichert den User in der Session
						VaadinSession.getCurrent().setAttribute("user", username.getValue());
						// Fügt alle Views hinzu
						getUI().getNavigator().addView(StartseiteView.NAME, StartseiteView.class);
						getUI().getNavigator().addView(PasswortView.NAME, PasswortView.class);
						getUI().getNavigator().addView(ExportViewWinti.NAME, ExportViewWinti.class);
						getUI().getNavigator().addView(ExportViewWaedi.NAME, ExportViewWaedi.class);
						getUI().getNavigator().addView(BelegungErfassenViewWinti.NAME, BelegungErfassenViewWinti.class);
						getUI().getNavigator().addView(TagesübersichtBelegungViewWinti.NAME,
								TagesübersichtBelegungViewWinti.class);
						getUI().getNavigator().addView(BelegungErfassenViewWaedi.NAME, BelegungErfassenViewWaedi.class);
						getUI().getNavigator().addView(TagesübersichtBelegungViewWaedi.NAME,
								TagesübersichtBelegungViewWaedi.class);
						getUI().getNavigator().addView(BenutzungsstatistikViewBB.NAME, BenutzungsstatistikViewBB.class);
						getUI().getNavigator().addView(BenutzungsstatistikViewLL.NAME, BenutzungsstatistikViewLL.class);
						getUI().getNavigator().addView(BenutzungsstatistikViewWaedi.NAME,
								BenutzungsstatistikViewWaedi.class);
						getUI().getNavigator().addView(ExterneGruppeViewBB.NAME, ExterneGruppeViewBB.class);
						getUI().getNavigator().addView(KorrekturViewBB.NAME, KorrekturViewBB.class);
						getUI().getNavigator().addView(KorrekturViewLL.NAME, KorrekturViewLL.class);
						getUI().getNavigator().addView(KorrekturViewWaedi.NAME, KorrekturViewWaedi.class);
						getUI().getNavigator().addView(TagesübersichtBenutzungViewBB.NAME,
								TagesübersichtBenutzungViewBB.class);
						getUI().getNavigator().addView(TagesübersichtBenutzungViewLL.NAME,
								TagesübersichtBenutzungViewLL.class);
						getUI().getNavigator().addView(TagesübersichtBenutzungViewWaedi.NAME,
								TagesübersichtBenutzungViewWaedi.class);
						getUI().getNavigator().addView(WintikurierViewBB.NAME, WintikurierViewBB.class);
						getUI().getNavigator().addView(InternerkurierViewWaedi.NAME, InternerkurierViewWaedi.class);

						getUI().getNavigator().addViewChangeListener(new ViewChangeListener() {

							private static final long serialVersionUID = 1L;

							@Override
							public boolean beforeViewChange(ViewChangeEvent event) {

								String user = null;
								// Prüft ob der User auch den Zugriff zu der gewählten Seite hat
								if (VaadinSession.getCurrent().getAttribute("user") != null) {
									user = VaadinSession.getCurrent().getAttribute("user").toString();

									// Export, Passwort View dürfen nur Admins
									if (event.getNewView() instanceof PasswortView && user.contains("Admin")
											|| event.getNewView() instanceof ExportViewWinti
													&& user.equals("Admin Winterthur")
											|| event.getNewView() instanceof ExportViewWaedi
													&& user.equals("Admin Wädenswil")) {
										return true;
									}
									// Benutzungsstatistik Bibliothek darf nur Admin und Mitarbeitende Winterthur
									if (event.getNewView() instanceof BenutzungsstatistikViewBB
											&& user.equals("Admin Winterthur")
											|| event.getNewView() instanceof BenutzungsstatistikViewBB
													&& user.equals("Mitarbeitende Winterthur")
											|| event.getNewView() instanceof WintikurierViewBB
													&& user.equals("Admin Winterthur")
											|| event.getNewView() instanceof WintikurierViewBB
													&& user.equals("Mitarbeitende Winterthur")
											|| event.getNewView() instanceof ExterneGruppeViewBB
													&& user.equals("Admin Winterthur")
											|| event.getNewView() instanceof ExterneGruppeViewBB
													&& user.equals("Mitarbeitende Winterthur")
											|| event.getNewView() instanceof TagesübersichtBenutzungViewBB
													&& user.equals("Admin Winterthur")
											|| event.getNewView() instanceof TagesübersichtBenutzungViewBB
													&& user.equals("Mitarbeitende Winterthur")
											|| event.getNewView() instanceof KorrekturViewBB
													&& user.equals("Admin Winterthur")
											|| event.getNewView() instanceof KorrekturViewBB
													&& user.equals("Mitarbeitende Winterthur")) {
										return true;
									}
									// Benutzungsstatistik Lernlandschaft darf nur Admin und Studentische
									// Mitarbeiter Winterthur
									if (event.getNewView() instanceof BenutzungsstatistikViewLL
											&& user.equals("Admin Winterthur")
											|| event.getNewView() instanceof BenutzungsstatistikViewLL
													&& user.equals("Studentische Mitarbeitende Winterthur")
											|| event.getNewView() instanceof TagesübersichtBenutzungViewLL
													&& user.equals("Admin Winterthur")
											|| event.getNewView() instanceof TagesübersichtBenutzungViewLL
													&& user.equals("Studentische Mitarbeitende Winterthur")
											|| event.getNewView() instanceof KorrekturViewLL
													&& user.equals("Admin Winterthur")
											|| event.getNewView() instanceof KorrekturViewLL
													&& user.equals("Studentische Mitarbeitende Winterthur")) {
										return true;
									}
									// Benutzungsstatistik Wädenswil dürfen nur User aus Wädenswil
									if (event.getNewView() instanceof BenutzungsstatistikViewWaedi
											&& user.contains("Wädenswil")
											|| event.getNewView() instanceof InternerkurierViewWaedi
													&& user.contains("Wädenswil")
											|| event.getNewView() instanceof TagesübersichtBenutzungViewWaedi
													&& user.contains("Wädenswil")
											|| event.getNewView() instanceof KorrekturViewWaedi
													&& user.contains("Wädenswil")) {
										return true;
									}
									// Belegung Winterthur dürfen nur alle mit Wort "Winterthur" im Namen
									if (event.getNewView() instanceof BelegungErfassenViewWinti
											&& user.contains("Winterthur")
											|| event.getNewView() instanceof TagesübersichtBelegungViewWinti
													&& user.contains("Winterthur")) {
										return true;
									}
									// Belegung Wädenswil dürfen nur alle mit Wort "Wädenswil" im Namen
									if (event.getNewView() instanceof BelegungErfassenViewWaedi
											&& user.contains("Wädenswil")
											|| event.getNewView() instanceof TagesübersichtBelegungViewWaedi
													&& user.contains("Wädenswil")) {
										return true;
									}
									// Jeder darf auf die Startseite
									if (event.getNewView() instanceof StartseiteView) {
										return true;
									}
								} else {
									// Wenn User null ist
									if (event.getNewView() instanceof LoginPage) {
										return true;
									}
								}

								// Ansonsten wird Zugriff verweigert angezeigt
								Notification.show("Zugriff verweigert", Notification.Type.WARNING_MESSAGE);
								return false;
							}

						});

//						Object object = getUI().getSession().getAttribute("route");
//						System.out.println("BOOM " +object.toString());
//						Page.getCurrent().setUriFragment(object.toString());

						Page.getCurrent().setUriFragment("!" + StartseiteView.NAME);
					} else {
						Notification.show("Ungültige Eingaben", Notification.Type.WARNING_MESSAGE);
					}
				} else {
					Notification.show("Ungültige Eingaben", Notification.Type.WARNING_MESSAGE);
				}
			}

		});
		content.addComponent(send);
		content.setSizeUndefined();
		content.setMargin(true);
		panel.setContent(content);
		setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

	}

	/**
	 * Prüft ob das Passwort des eingeloggten Users älter als ein halbes Jahr ist
	 * Sendet eine Email an die Bibliothek Winterthur oder Wädenswil
	 */
	protected void pruefePasswortDatum(Angestellter angestellter) {
		// Holt das Datum des Passworts sowie das heutige Datum
		Date passwortDatum = angestellter.getPasswort_datum();
		Date heutigesDatum = new Date();

		// Wenn das alte Passwort älter ist als ein halbes Jahr
		long differenz = heutigesDatum.getTime() - passwortDatum.getTime();
		// Der Long wert wird gerechnet, sodass er als Ergebnis die Zeit in Tagen
		// anzeigt
		long diffenzInTagen = differenz / (24 * 60 * 60 * 1000);

		// Wenn das Passwort älter als 180 Tage ist
		if (diffenzInTagen >= 180) {
			List<String> to = new ArrayList<>();
			if (angestellter.getName().contains("Wädenswil")) {
				// Email an Wädenswil
				to.add("waedenswil.hsb@zhaw.ch");
			} else {
				// Email an Winterthur
				to.add("ausleihe.winterthur.hsb@zhaw.ch");
			}

			// Nachricht des Textes
			String subject = "Abgelaufenes Passwort";
			String text = "Das Passwort des Users " + angestellter.getName()
					+ " ist abgelaufen. Bitte durch den Administrator erneuern";

			// Mail wird versendet
			ScheduledExecutorEmailService.sendEmail(to, subject, text);
		}

	}

}
