package benutzungsstatistik.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import benutzungsstatistik.model.Intensivfrage;

/**
 * Das ist die DAO-Klasse (Data Access Object) von Intensivfrage. Man kann damit
 * die Tabelle 'Intensivfrage' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
public class IntensivfrageDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der IntensivfrageDatenbank
	 */
	public IntensivfrageDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		}
	}

	/**
	 * Neue Intensivfrage in die DB hinzufuegen
	 * 
	 * @param intensivfrage
	 */
	public void insertIntensivfrage(Intensivfrage intensivfrage) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(intensivfrage);

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
	 * Intensivfrage löschen
	 * 
	 * @param intensivfrage
	 */
	public void deleteIntensivfrage(Intensivfrage intensivfrage) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.delete(intensivfrage);

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
	 * @return Liste von allen Intensivfragen
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Intensivfrage> selectAllIntensivfragen() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Intensivfrage> intensivfragenListe = new ArrayList<Intensivfrage>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			intensivfragenListe = tempSession.createQuery("From Intensivfrage")
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

		return intensivfragenListe;
	}
	
	/**
	 * @return Liste von allen Intensivfragen für eine Benutzungsstatistik
	 */
	public List<Intensivfrage> selectAllIntensivfragenForBenutzungsstatistik(int benutzungsstatistik_ID) {

		// Intensivfragen aus der DB auslesen
		final IntensivfrageDatenbank intensivfrageDB = new IntensivfrageDatenbank();
		List<Intensivfrage> intensivfragenListe = intensivfrageDB.selectAllIntensivfragen();
		final List<Intensivfrage> intensivfragenListeFuerBenutzungsstatistik = new ArrayList<Intensivfrage>();
		
		for (final Intensivfrage intensivfrage : intensivfragenListe) {
			if (intensivfrage.getBenutzungsstatistik().getBenutzungsstatistik_ID() == benutzungsstatistik_ID) {
				intensivfragenListeFuerBenutzungsstatistik.add(intensivfrage);
			}
		}
		
		return intensivfragenListeFuerBenutzungsstatistik;
	}
	
}
