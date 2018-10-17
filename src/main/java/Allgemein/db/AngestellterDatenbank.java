package allgemein.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import allgemein.model.Angestellter;

/**
 * Das ist die DAO-Klasse (Data Access Object) vom Angesteller. Man kann damit
 * die Tabelle 'Angestellter' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
public class AngestellterDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der AngestelltenDatenbank
	 */
	public AngestellterDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		}
	}

	/**
	 * Neuen Angestellten in die DB hinzufuegen
	 * 
	 * @param angestellter
	 */
	public void insertAngestellter(Angestellter angestellter) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(angestellter);

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
	 * Angestellter aktualisieren
	 * 
	 * @param angestellter
	 */
	public void updateAngestellter(Angestellter angestellter) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.update(angestellter);

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
	 * @return Liste von allen Angestellten
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Angestellter> selectAllAngestellte() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Angestellter> angestelltenListe = new ArrayList<Angestellter>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			angestelltenListe = tempSession.createQuery("From Angestellter")
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

		return angestelltenListe;
	}

	
}
