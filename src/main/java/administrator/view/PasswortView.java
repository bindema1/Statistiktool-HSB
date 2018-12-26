package administrator.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import allgemein.db.AngestellterDatenbank;
import allgemein.model.Angestellter;
import allgemein.view.StartseiteView;

/**
 * View um Passwörter zu ändern als Administrator
 * 
 * @author Marvin Bindemann
 */
public class PasswortView extends Composite implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Passwort";
	private AbsoluteLayout mainLayout;
	private Button bZurueck;
	private Button bSpeichern;
	private AngestellterDatenbank angestellterDB = new AngestellterDatenbank();
	private Angestellter angestellter;
	private PasswordField passwort1;
	private PasswordField passwort2;
	private Label passwortDatum;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	/**
	 * Bildet das AbsoluteLayout, als Wrapper um die ganze View
	 * 
	 * @return AbsoluteLayout
	 */
	private AbsoluteLayout buildMainLayout() {
		mainLayout = new AbsoluteLayout();
		//Setzt die Hintergrundfarbe auf Grün
		mainLayout.addStyleName("backgroundErfassung");
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		return mainLayout;
	}

	/**
	 * Setzt den CompositionRoot auf ein AbsoluteLayout. Ruft initComponents auf,
	 * welches alle Komponenten dem Layout hinzufügt
	 * 
	 * @return AbsoluteLayout
	 */
	public AbsoluteLayout init() {
		AbsoluteLayout absolutLayout = buildMainLayout();
		initComponents();

		return absolutLayout;
	}

	public PasswortView() {
		setCompositionRoot(init());
	}

	/**
	 * Initialisieren der GUI Komponente. Fügt alle Komponenten dem Layout hinzu
	 */
	private void initComponents() {

		bZurueck = new Button();
		bZurueck.setCaption("Zurück");
		bZurueck.setIcon(VaadinIcons.ARROW_LEFT);
		bZurueck.addClickListener(createClickListener());
		
		Label lText = new Label();
		lText.setValue("Passwörter ändern");
		lText.addStyleName(ValoTheme.LABEL_LARGE + " " + ValoTheme.LABEL_BOLD);

		//Dropdown aller Angestellten
		Collection<Angestellter> angestelltenListe = angestellterDB.selectAllAngestellte();
		NativeSelect<Angestellter> dropdown = new NativeSelect<>("Wählen Sie einen User aus", angestelltenListe);
		dropdown.setWidth("350px");
		dropdown.setEmptySelectionAllowed(false);
		dropdown.addValueChangeListener(event -> {
			//Setzt den gewählten Angestellten und verändert das Label: passwortDatum
			this.angestellter = (Angestellter) event.getValue();
			passwortDatum.setCaption("Passwort zuletzt geändert am: " + sdf.format(angestellter.getPasswort_datum()));
		});

		passwortDatum = new Label();
		passwortDatum.setCaption("Passwort zuletzt geändert am: ");

		passwort1 = new PasswordField("Passwort");
		passwort2 = new PasswordField("Passwort wiederholen");

		bSpeichern = new Button();
		bSpeichern.setCaption("Speichern");
		bSpeichern.addStyleName(ValoTheme.BUTTON_LARGE);
		bSpeichern.addClickListener(createClickListener());

		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponent(bZurueck);
		layout.addComponent(lText);
		layout.addComponent(dropdown);
		layout.addComponent(passwortDatum);
		layout.addComponent(passwort1);
		layout.addComponent(passwort2);
		layout.addComponent(bSpeichern);

		mainLayout.addComponent(layout, "top:20%;left:30%");
	}

	@SuppressWarnings("serial")
	public ClickListener createClickListener() {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				
				if (e.getSource() == bZurueck) {
					getUI().getNavigator().navigateTo(StartseiteView.NAME);
				}
				
				if (e.getSource() == bSpeichern) {
					//Wenn beide Passwörter den selben Wert haben
					if (passwort1.getValue().equals(passwort2.getValue())) {
						String passwort = passwort1.getValue();
						//Verschlüsselt das Passwort mit MD5 in der Datenbank
						angestellter.setPasswort(passwort);
						angestellter.setPasswort_datum(new Date());
						//Updatet den Angestellten in der Datenbank
						angestellterDB.updateAngestellter(angestellter);

						// Daten wieder auf 0 setzen
						passwort1.setValue("");
						passwort2.setValue("");
						passwortDatum.setCaption(
								"Passwort zuletzt geändert am: " + sdf.format(angestellter.getPasswort_datum()));

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
