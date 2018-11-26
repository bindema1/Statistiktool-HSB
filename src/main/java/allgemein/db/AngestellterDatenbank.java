package allgemein.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
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
	@SuppressWarnings({ "unchecked"})
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
	
	
	/**
	 * @return Angestellter für einen Namen
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public Angestellter getAngestellterByName(String name) {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Angestellter> angestellterListe = new ArrayList<Angestellter>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			String hql = "FROM Angestellter a WHERE a.name = :name";
			Query query = tempSession.createQuery(hql);
			query.setParameter("name", name);
			angestellterListe = query.list();

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
		
		if(angestellterListe.size() == 1) {
			return angestellterListe.get(0);	
		}else {
			return null;
		}
	}	

	
}
