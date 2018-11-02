package belegung.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import belegung.model.Kapazität;
import belegung.model.StockwerkEnum;

/**
 * Das ist die DAO-Klasse (Data Access Object) von der Kapazität. Man kann damit
 * die Tabelle 'Kapazität' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und kann Kapazitäten aus der Datenbank lesen. (Select)
 * 
 * @author Marvin Bindemann
 */
public class KapazitätDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der BelegungDatenbank
	 */
	public KapazitätDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		}
	}
	
	/**
	 * Neue Kapazität in die DB hinzufuegen
	 * 
	 * @param kapazität
	 */
	public void insertKapazität(Kapazität kapazität) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(kapazität);

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
	 * @return Kapazität
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public Kapazität selectKapazitätForStockwerk(StockwerkEnum stockwerk) {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Kapazität> kapazitätListe = new ArrayList<Kapazität>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			kapazitätListe = tempSession.createQuery("From Kapazität")
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
		
		Kapazität kapazität = null;
		for(Kapazität k : kapazitätListe) {
			if(k.getStockwerk() == stockwerk) {
				kapazität = k;
			}
		}

		return kapazität;
	}
}
