package allgemein.view;

import java.util.logging.Level;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.UI;

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
		
//		return new BenutzungsstatistikView().init(this);
//		return new TagesübersichtBelegungView(testdaten.getBelegungLL()).init(this);
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		return new StartseiteView().init(this);
	}
	
	@WebServlet(urlPatterns = "/*", name = "MyServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainView.class, productionMode = false)
    public static class MyServlet extends VaadinServlet {
    }

}
