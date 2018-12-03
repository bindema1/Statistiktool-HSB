package allgemein.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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

import administrator.view.ExportView;
import administrator.view.PasswortView;
import allgemein.db.AngestellterDatenbank;
import allgemein.model.Angestellter;
import allgemein.model.MD5;
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

public class LoginPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "";
	private AngestellterDatenbank angestellterDB = new AngestellterDatenbank();
	private MD5 md5 = new MD5();
	private NativeSelect<String> username;

	public LoginPage() {
		Panel panel = new Panel("Login");
		panel.setSizeUndefined();
		addComponent(panel);

		FormLayout content = new FormLayout();
		List<String> data = new ArrayList<>();
		for (Angestellter a : angestellterDB.selectAllAngestellte()) {
			data.add(a.getName());
		}
		username = new NativeSelect<>("User", data);
		username.setWidth(100.0f, Unit.PERCENTAGE);
		content.addComponent(username);
		PasswordField password = new PasswordField("Passwort");
		content.addComponent(password);

		Button send = new Button("Login");
		send.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				Angestellter angestellter = angestellterDB.getAngestellterByName(username.getValue());

				if (angestellter != null) {
					if (angestellter.getPasswort().equals(md5.convertMD5(password.getValue()))) {
						VaadinSession.getCurrent().setAttribute("user", username.getValue());
						getUI().getNavigator().addView(StartseiteView.NAME, StartseiteView.class);
						getUI().getNavigator().addView(PasswortView.NAME, PasswortView.class);
						getUI().getNavigator().addView(ExportView.NAME, ExportView.class);
						getUI().getNavigator().addView(BelegungErfassenViewWinti.NAME, BelegungErfassenViewWinti.class);
						getUI().getNavigator().addView(TagesübersichtBelegungViewWinti.NAME,
								TagesübersichtBelegungViewWinti.class);
						getUI().getNavigator().addView(BenutzungsstatistikViewBB.NAME, BenutzungsstatistikViewBB.class);
						getUI().getNavigator().addView(BenutzungsstatistikViewLL.NAME, BenutzungsstatistikViewLL.class);
						getUI().getNavigator().addView(ExterneGruppeViewBB.NAME, ExterneGruppeViewBB.class);
						getUI().getNavigator().addView(KorrekturViewBB.NAME, KorrekturViewBB.class);
						getUI().getNavigator().addView(KorrekturViewLL.NAME, KorrekturViewLL.class);
						getUI().getNavigator().addView(TagesübersichtBenutzungViewBB.NAME, TagesübersichtBenutzungViewBB.class);
						getUI().getNavigator().addView(TagesübersichtBenutzungViewLL.NAME, TagesübersichtBenutzungViewLL.class);
						getUI().getNavigator().addView(WintikurierViewBB.NAME, WintikurierViewBB.class);

						getUI().getNavigator().addViewChangeListener(new ViewChangeListener() {

							@Override
							public boolean beforeViewChange(ViewChangeEvent event) {

								String user = null;
								if (VaadinSession.getCurrent().getAttribute("user") != null) {
									user = VaadinSession.getCurrent().getAttribute("user").toString();

									//Export, Passwort View dürfen nur Admins
									if (event.getNewView() instanceof PasswortView && user.contains("Admin")
											|| event.getNewView() instanceof ExportView
													&& user.equals("Admin Winterthur")) {
										return true;
									}
									//Benutzungsstatistik Bibliothek darf nur Admin und Mitarbeitende Winterthur
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
													&& user.equals("Mitarbeitende Winterthur")){
										return true;
									}
									//Benutzungsstatistik Lernlandschaft darf nur Admin und Studentische Mitarbeiter Winterthur
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
									//Belegung Winterthur dürfen nur alle mit Wort "Winterthur" im Namen
									if (event.getNewView() instanceof BelegungErfassenViewWinti
											&& user.contains("Winterthur")
											|| event.getNewView() instanceof TagesübersichtBelegungViewWinti
													&& user.contains("Winterthur")){
										return true;
									}
									//Jeder darf auf die Startseite
									if (event.getNewView() instanceof StartseiteView) {
										return true;
									}
								}else {
									//Wenn User null ist
									if (event.getNewView() instanceof LoginPage) {
										return true;
									}
								}

								//Ansonsten wird Zugriff verweigert angezeigt
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

	@Override
	public void enter(ViewChangeEvent event) {

	}

}
