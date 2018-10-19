package allgemein.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import allgemein.model.Standort;
import allgemein.model.StandortEnum;

/**
 * Das ist die DAO-Klasse (Data Access Object) vom Standort. Man kann damit
 * die Tabelle 'Standort' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
public class StandortDatenbank {

	
	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der StandortDatenbank
	 */
	public StandortDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		}
	}

	/**
	 * Neuen Standort in die DB hinzufuegen
	 * 
	 * @param standort
	 */
	public void insertStandort(Standort standort) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(standort);

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
	 * @return Liste von allen Standorten
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Standort> selectAllStandorte() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Standort> standortListe = new ArrayList<Standort>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			standortListe = tempSession.createQuery("From Standort")
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

		return standortListe;
	}
	
	public Standort getStandort(StandortEnum standortEnum) {
		Standort standort = null;
		for(Standort s : selectAllStandorte()) {
			if(s.getName().equals(standortEnum)) {
				standort = s;
			}
		}
		
		return standort;
	}
	
	
	
	
}
