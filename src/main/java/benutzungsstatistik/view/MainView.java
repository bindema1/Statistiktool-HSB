package benutzungsstatistik.view;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.UI;

import Allgemein.db.StandortDatenbank;
import Allgemein.model.Standort;
import Testdaten.TestDaten;
import benutzungsstatistik.db.BeantwortungBibliothekspersonalDatenbank;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.db.EmailkontaktDatenbank;
import benutzungsstatistik.db.ExterneGruppeDatenbank;
import benutzungsstatistik.db.IntensivfrageDatenbank;
import benutzungsstatistik.db.KundenkontaktDatenbank;
import benutzungsstatistik.db.TelefonkontaktDatenbank;
import benutzungsstatistik.db.WintikurierDatenbank;
import benutzungsstatistik.model.BeantwortungBibliothekspersonal;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Emailkontakt;
import benutzungsstatistik.model.ExterneGruppe;
import benutzungsstatistik.model.Intensivfrage;
import benutzungsstatistik.model.Kundenkontakt;
import benutzungsstatistik.model.Telefonkontakt;
import benutzungsstatistik.model.Wintikurier;

/**
 * MainView setzt den Content und gibt seine eigene MainView an die entsprechende View weiter
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class MainView extends UI {

	protected void init(VaadinRequest request) {
		setContent(buildMainLayout());
	}

	private AbsoluteLayout buildMainLayout() {
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
//		Navigator navigator = new Navigator(this, this);
//	    navigator.addView("login", new Login());
//	    navigator.addView("dashboard", new Dashboard());
//	    navigator.navigateTo("login");
		
		return new BenutzungsstatistikView().init(this);
	}
	
	@WebServlet(urlPatterns = "/*", name = "MyServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainView.class, productionMode = false)
    public static class MyServlet extends VaadinServlet {
    }

}
