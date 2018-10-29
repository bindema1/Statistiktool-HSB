package benutzungsstatistik.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import benutzungsstatistik.model.Emailkontakt;

/**
 * Das ist die DAO-Klasse (Data Access Object) vom Emailkontakt. Man kann damit
 * die Tabelle 'Emailkontakt' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
public class EmailkontaktDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der EmailkontaktDatenbank
	 */
	public EmailkontaktDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		}
	}

	/**
	 * Neuen Emailkontakt in die DB hinzufuegen
	 * 
	 * @param emailkontakt
	 */
	public void insertEmailkontakt(Emailkontakt emailkontakt) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(emailkontakt);

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
	 * Emailkontakt löschen
	 * 
	 * @param emailkontakt
	 */
	public void deleteEmailkontakt(Emailkontakt emailkontakt) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.delete(emailkontakt);

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
	 * @return Liste von allen Emailkontakten
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Emailkontakt> selectAllEmailkontakte() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Emailkontakt> emailkontaktenListe = new ArrayList<Emailkontakt>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			emailkontaktenListe = tempSession.createQuery("From Emailkontakt")
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

		return emailkontaktenListe;
	}
	
	/**
	 * @return Liste von allen Emailkontakten für eine Benutzungsstatistik
	 */
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public List<Emailkontakt> selectAllEmailkontakteForBenutzungsstatistik(int benutzungsstatistik_ID) {

		// Emailkontakte aus der DB auslesen
		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Emailkontakt> emailkontaktenListe = new ArrayList<Emailkontakt>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			String hql = "FROM Emailkontakt T WHERE T.benutzungsstatistik.id = :id";
			Query query = tempSession.createQuery(hql);
			query.setParameter("id", benutzungsstatistik_ID);
			emailkontaktenListe = query.list();
			
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

		return emailkontaktenListe;
	}
	
}
