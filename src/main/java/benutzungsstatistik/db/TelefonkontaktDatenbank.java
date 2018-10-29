package benutzungsstatistik.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import benutzungsstatistik.model.Telefonkontakt;

/**
 * Das ist die DAO-Klasse (Data Access Object) von Telefonkontakt. Man kann damit
 * die Tabelle 'Telefonkontakt' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
public class TelefonkontaktDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der TelefonkontaktDatenbank
	 */
	public TelefonkontaktDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		}
	}

	/**
	 * Neuen Telefonkontakt in die DB hinzufuegen
	 * 
	 * @param telefonkontakt
	 */
	public void insertTelefonkontakt(Telefonkontakt telefonkontakt) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(telefonkontakt);

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
	 * Telefonkontakt löschen
	 * 
	 * @param telefonkontakt
	 */
	public void deleteTelefonkontakt(Telefonkontakt telefonkontakt) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.delete(telefonkontakt);

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
	 * @return Liste von allen Telefonkontakten
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Telefonkontakt> selectAllTelefonkontakte() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Telefonkontakt> telefonkontaktenListe = new ArrayList<Telefonkontakt>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			telefonkontaktenListe = tempSession.createQuery("From Telefonkontakt")
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

		return telefonkontaktenListe;
	}
	
	/**
	 * @return Liste von allen Telefonkontakten für eine Benutzungsstatistik
	 */
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public List<Telefonkontakt> selectAllTelefonkontakteForBenutzungsstatistik(int benutzungsstatistik_ID) {

		// Telefonkontakt aus der DB auslesen
		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Telefonkontakt> telefonkontaktenListe = new ArrayList<Telefonkontakt>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			
			String hql = "FROM Telefonkontakt T WHERE T.benutzungsstatistik.id = :id";
			Query query = tempSession.createQuery(hql);
			query.setParameter("id", benutzungsstatistik_ID);
			telefonkontaktenListe = query.list();

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
		
		return telefonkontaktenListe;
	}
	
}
