package allgemein.view;

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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import administrator.view.ExportView;
import administrator.view.PasswortView;
import allgemein.db.AngestellterDatenbank;
import allgemein.model.Angestellter;
import allgemein.model.MD5;
import belegung.view.BelegungErfassenView;
import belegung.view.TagesübersichtBelegungView;
import benutzungsstatistik.view.BenutzungsstatistikBBView;
import benutzungsstatistik.view.ExterneGruppeView;
import benutzungsstatistik.view.KorrekturView;
import benutzungsstatistik.view.TagesübersichtBenutzungView;
import benutzungsstatistik.view.WintikurierView;

public class LoginPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "";
	private AngestellterDatenbank angestellterDB = new AngestellterDatenbank();
	private MD5 md5 = new MD5();

	public LoginPage() {
		Panel panel = new Panel("Login");
		panel.setSizeUndefined();
		addComponent(panel);

		FormLayout content = new FormLayout();
		TextField username = new TextField("Username");
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
						getUI().getNavigator().addView(BelegungErfassenView.NAME, BelegungErfassenView.class);
						getUI().getNavigator().addView(TagesübersichtBelegungView.NAME,
								TagesübersichtBelegungView.class);
						getUI().getNavigator().addView(BenutzungsstatistikBBView.NAME, BenutzungsstatistikBBView.class);
						getUI().getNavigator().addView(ExterneGruppeView.NAME, ExterneGruppeView.class);
						getUI().getNavigator().addView(KorrekturView.NAME, KorrekturView.class);
						getUI().getNavigator().addView(TagesübersichtBenutzungView.NAME,
								TagesübersichtBenutzungView.class);
						getUI().getNavigator().addView(WintikurierView.NAME, WintikurierView.class);

						getUI().getNavigator().addViewChangeListener(new ViewChangeListener() {

							@Override
							public boolean beforeViewChange(ViewChangeEvent event) {

								String user = VaadinSession.getCurrent().getAttribute("user").toString();

								if (event.getNewView() instanceof PasswortView && user.contains("Admin")
										|| event.getNewView() instanceof ExportView && user.equals("Admin Winterthur")) {
									return true;
								}
								if (event.getNewView() instanceof BenutzungsstatistikBBView && user.equals("Admin Winterthur")
										|| event.getNewView() instanceof BenutzungsstatistikBBView && user.equals("Mitarbeitende Winterthur")) {
									return true;
								}
								if (event.getNewView() instanceof BelegungErfassenView && !user.equals("Mitarbeitende Wädenswil")
										|| event.getNewView() instanceof TagesübersichtBelegungView && !user.equals("Mitarbeitende Wädenswil")
										|| event.getNewView() instanceof WintikurierView && !user.equals("Mitarbeitende Wädenswil")
										|| event.getNewView() instanceof ExterneGruppeView && !user.equals("Mitarbeitende Wädenswil")
										|| event.getNewView() instanceof BelegungErfassenView && !user.equals("Mitarbeitende Wädenswil")
										|| event.getNewView() instanceof TagesübersichtBenutzungView && !user.equals("Mitarbeitende Wädenswil")
										|| event.getNewView() instanceof KorrekturView && !user.equals("Mitarbeitende Wädenswil")) {
									return true;
								} 
								if (event.getNewView() instanceof StartseiteView) {
									return true;
								}
								
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
