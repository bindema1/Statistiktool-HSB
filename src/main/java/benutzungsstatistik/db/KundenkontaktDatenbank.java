package benutzungsstatistik.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import benutzungsstatistik.model.Kundenkontakt;

/**
 * Das ist die DAO-Klasse (Data Access Object) von Kundenkontakt. Man kann damit
 * die Tabelle 'Kundenkontakt' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
public class KundenkontaktDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der KundenkontaktDatenbank
	 */
	public KundenkontaktDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		}
	}

	/**
	 * Neuen Kundenkontakt in die DB hinzufuegen
	 * 
	 * @param kundenkontakt
	 */
	public void insertKundenkontakt(Kundenkontakt kundenkontakt) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(kundenkontakt);

			tempTransaction.commit();
		} catch (final HibernateException ex) {

			ex.printStackTrace();

			if (tempTransaction != null) {
				try {
					tempTransaction.rollback();
				} catch (final HibernateException exRb) {
				}
			}
			throw new RuntimeException(ex.getMessage());
		} finally {
			try {
				if (tempSession != null) {
					tempSession.close();
				}
			} catch (final Exception exC1) {
			}
		}
	}

	/**
	 * Kundenkontakt löschen
	 * 
	 * @param kundenkontakt
	 */
	public void deleteKundenkontakt(Kundenkontakt kundenkontakt) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.delete(kundenkontakt);
			tempTransaction.commit();
		} catch (HibernateException ex) {

			ex.printStackTrace();

			if (tempTransaction != null)
				try {
					tempTransaction.rollback();
				} catch (HibernateException exRb) {
				}
			throw new RuntimeException(ex.getMessage());
		} finally {
			try {
				if (tempSession != null)
					tempSession.close();
			} catch (Exception exC1) {
			}
		}
	}

	/**
	 * @return Liste von allen Kundenkontakten
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Kundenkontakt> selectAllKundenkontakte() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Kundenkontakt> kundenkontaktenListe = new ArrayList<Kundenkontakt>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			kundenkontaktenListe = tempSession.createQuery("From Kundenkontakt")
					.list();
			tempTransaction.commit();

		} catch (final HibernateException ex) {
			if (tempTransaction != null) {
				try {
					tempTransaction.rollback();
				} catch (final HibernateException exRb) {
				}
			}

			throw new RuntimeException(ex.getMessage());
		} finally {
			try {
				if (tempSession != null) {
					tempSession.close();
				}
			} catch (final Exception exC1) {
			}
		}

		return kundenkontaktenListe;
	}
	
	/**
	 * @return Liste von allen Kundenkontakten für eine Benutzungsstatistik
	 */
	public List<Kundenkontakt> selectAllKundenkontakteForBenutzungsstatistik(int benutzungsstatistik_ID) {

		// Kundenkontakt aus der DB auslesen
		final KundenkontaktDatenbank kundenkontaktDB = new KundenkontaktDatenbank();
		List<Kundenkontakt> kundenkontaktenListe = kundenkontaktDB.selectAllKundenkontakte();
		final List<Kundenkontakt> kundenkontaktenListeFuerBenutzungsstatistik = new ArrayList<Kundenkontakt>();
		
		for (final Kundenkontakt kundenkontakt : kundenkontaktenListe) {
			if (kundenkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID() == benutzungsstatistik_ID) {
				kundenkontaktenListeFuerBenutzungsstatistik.add(kundenkontakt);
			}
		}
		
		return kundenkontaktenListeFuerBenutzungsstatistik;
	}
	
}
