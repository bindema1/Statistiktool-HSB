package allgemein.view;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.UI;

import belegung.view.BelegungErfassenView;
import benutzungsstatistik.view.BenutzungsstatistikBBView;

/**
 * MainView setzt den Content und gibt seine eigene MainView an die
 * entsprechende View weiter
 * 
 * @author Marvin Bindemann
 */
@Theme("mytheme")
public class MainView extends UI{

	protected void init(VaadinRequest request) {
		setContent(buildMainLayout());
	}

	private AbsoluteLayout buildMainLayout() {
		// top-level component properties
		setWidth("100.0%");
		//setHeight("100.0%");

//		Navigator navigator = new Navigator(this, this);
//		navigator.addView("", StartseiteView.class);
//		navigator.addView("belegung", BelegungErfassenView.class);
//		navigator.addView("benutzung", BenutzungsstatistikBBView.class);
//
//		// push state:
//		Button button = new Button("Go to page 1");
//		button.addClickListener(e -> {
//			// URL will change to .../page1
//			Page.getCurrent().pushState("page1");
//		});
//
//		// pop state:
//		Page.getCurrent().addPopStateListener(event -> {
//			String uri = event.getUri();
//			// ... update the UI accordingly
//		});

		return new StartseiteView().init(this);
	}

	@WebServlet(urlPatterns = "/*", name = "MyServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MainView.class, productionMode = true)
	public static class MyServlet extends VaadinServlet {
	}

}
