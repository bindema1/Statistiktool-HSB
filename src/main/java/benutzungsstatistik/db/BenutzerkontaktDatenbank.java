package benutzungsstatistik.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import benutzungsstatistik.model.Benutzerkontakt;

/**
 * Das ist die DAO-Klasse (Data Access Object) von Benutzerkontakt. Man kann damit
 * die Tabelle 'Benutzerkontakt' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
public class BenutzerkontaktDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der BenutzerkontaktDatenbank
	 */
	public BenutzerkontaktDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		}
	}

	/**
	 * Neuen Benutzerkontakt in die DB hinzufuegen
	 * 
	 * @param benutzerkontakt
	 */
	public void insertBenutzerkontakt(Benutzerkontakt benutzerkontakt) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(benutzerkontakt);

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
	 * Benutzerkontakt löschen
	 * 
	 * @param benutzerkontakt
	 */
	public void deleteBenutzerkontakt(Benutzerkontakt benutzerkontakt) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.delete(benutzerkontakt);
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
	 * @return Liste von allen Benutzerkontakten
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Benutzerkontakt> selectAllBenutzerkontakte() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Benutzerkontakt> benutzerkontaktenListe = new ArrayList<Benutzerkontakt>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			benutzerkontaktenListe = tempSession.createQuery("From Benutzerkontakt")
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

		return benutzerkontaktenListe;
	}
	
	/**
	 * @return Liste von allen Benutzerkontakten für eine Benutzungsstatistik
	 */
	public List<Benutzerkontakt> selectAllBenutzerkontakteForBenutzungsstatistik(int benutzungsstatistik_ID) {

		// Benutzerkontakt aus der DB auslesen
		final BenutzerkontaktDatenbank benutzerkontaktDB = new BenutzerkontaktDatenbank();
		List<Benutzerkontakt> benutzerkontaktenListe = benutzerkontaktDB.selectAllBenutzerkontakte();
		final List<Benutzerkontakt> benutzerkontaktenListeFuerBenutzungsstatistik = new ArrayList<Benutzerkontakt>();
		
		for (final Benutzerkontakt benutzerkontakt : benutzerkontaktenListe) {
			if (benutzerkontakt.getBenutzungsstatistik().getBenutzungsstatistik_ID() == benutzungsstatistik_ID) {
				benutzerkontaktenListeFuerBenutzungsstatistik.add(benutzerkontakt);
			}
		}
		
		return benutzerkontaktenListeFuerBenutzungsstatistik;
	}
	
}
