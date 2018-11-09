package administrator.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.vaadin.navigator.View;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.db.AngestellterDatenbank;
import allgemein.model.Angestellter;
import allgemein.model.MD5;
import allgemein.view.MainView;

@SuppressWarnings("serial")
public class PasswortView implements View {

	private AbsoluteLayout mainLayout;
	private Button bSpeichern;
	private AngestellterDatenbank angestellterDB = new AngestellterDatenbank();
	private Angestellter angestellter;
	private PasswordField passwort1;
	private PasswordField passwort2;
	private Label passwortDatum;

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

	public PasswortView() {
//		angestellter = angestellterDB.selectAllAngestellte().get(0);
	}

	private void initData() {

	}

	// Initialisieren der GUI Komponente
	private void initComponents(MainView mainView) {

		Collection<Angestellter> angestelltenListe = angestellterDB.selectAllAngestellte();
		ComboBox<Angestellter> combobox = new ComboBox<>("Wählen Sie einen User aus", angestelltenListe);
		combobox.setPlaceholder("Kein User ausgewählt");
		combobox.setWidth("400px");
		combobox.setEmptySelectionAllowed(false);

		combobox.addValueChangeListener(event -> {

			Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);

			this.angestellter = (Angestellter) event.getValue();
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			passwortDatum.setCaption("Passwort zuletzt geändert am " + sdf.format(angestellter.getPasswort_datum()));
		});

		passwortDatum = new Label();
		passwortDatum.setCaption("Passwort zuletzt geändert am ");

		passwort1 = new PasswordField("Passwort");
		passwort2 = new PasswordField("Passwort wiederholen");

		bSpeichern = new Button();
		bSpeichern.setCaption("Speichern");
		bSpeichern.addStyleName(ValoTheme.BUTTON_LARGE);
		bSpeichern.addClickListener(createClickListener(mainView));

		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setWidth("500px");
		layout.addComponent(combobox);
		layout.addComponent(passwortDatum);
		layout.addComponent(passwort1);
		layout.addComponent(passwort2);
		layout.addComponent(bSpeichern);

		mainLayout.addComponent(layout);
	}

	public ClickListener createClickListener(final MainView mainView) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				if (e.getSource() == bSpeichern) {
					if (passwort1.getValue().equals(passwort2.getValue())) {
						String passwort = passwort1.getValue();
						MD5 md5 = new MD5();
						angestellter.setPasswort(md5.convertMD5(passwort));
						angestellter.setPasswort_datum(new Date());
						angestellterDB.updateAngestellter(angestellter);

						// Daten wieder auf 0 setzen
						passwort1.setValue("");
						passwort2.setValue("");

						Notification.show("Passwort gespeichert für User " + angestellter.getName(),
								Type.TRAY_NOTIFICATION);
					} else {
						Notification.show("Passwörter stimmen nicht überein", Type.WARNING_MESSAGE);
					}
				}

			}
		};

	}
}
