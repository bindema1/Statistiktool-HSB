package allgemein.db;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import allgemein.model.Email;
/**
 * Testet alle Methoden der EmailDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestEmailDatenbank {

	EmailDatenbank emailDB = new EmailDatenbank();
	Email email;
	
	@Before
	public void initComponents() {
		email = new Email(new Date());
	}

	@Test
	public void testinsertAngestellter() {
		emailDB.insertEmail(email);
	}

	@Test
	public void testselectAllAngestellte() {
		emailDB.insertEmail(email);
		
		Email email2 = emailDB.getEmail();
		System.out.println(email2.getVersendetTimestamp());

		assertEquals(email2.getVersendetTimestamp(), email.getVersendetTimestamp());
	}
	
}
