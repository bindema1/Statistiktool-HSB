package benutzungsstatistik.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import benutzungsstatistik.model.ExterneGruppe;

/**
 * Das ist die DAO-Klasse (Data Access Object) von ExterneGruppe. Man kann damit
 * die Tabelle 'ExterneGruppe' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
@SuppressWarnings("deprecation")
public class ExterneGruppeDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der ExterneGruppeDatenbank
	 */
	public ExterneGruppeDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		}
	}

	/**
	 * Neue ExterneGruppe in die DB hinzufuegen
	 * 
	 * @param externeGruppe
	 */
	public void insertExterneGruppe(ExterneGruppe externeGruppe) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(externeGruppe);

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
	 * ExterneGruppe löschen
	 * 
	 * @param externeGruppe
	 */
	public void deleteExterneGruppe(ExterneGruppe externeGruppe) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.delete(externeGruppe);

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
	 * ExterneGruppe updaten
	 * 
	 * @param externeGruppe
	 */
	public void updateExterneGruppe(ExterneGruppe externeGruppe) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.update(externeGruppe);

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
	 * @return Liste von allen ExterneGruppen
	 */
	@SuppressWarnings({ "unchecked" })
	public List<ExterneGruppe> selectAllExterneGruppen() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<ExterneGruppe> externeGruppenListe = new ArrayList<ExterneGruppe>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			externeGruppenListe = tempSession.createQuery("From ExterneGruppe")
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

		return externeGruppenListe;
	}
	
	/**
	 * @return Liste von allen ExterneGruppen für eine Benutzungsstatistik
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ExterneGruppe> selectAllExterneGruppenForBenutzungsstatistik(int benutzungsstatistik_ID) {

		// ExterneGruppe aus der DB auslesen
		Session tempSession = null;
		Transaction tempTransaction = null;
		List<ExterneGruppe> externeGruppenListe = new ArrayList<ExterneGruppe>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			String hql = "FROM ExterneGruppe T WHERE T.benutzungsstatistik.id = :id";
			Query query = tempSession.createQuery(hql);
			query.setParameter("id", benutzungsstatistik_ID);
			externeGruppenListe = query.list();
			
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

		return externeGruppenListe;
	}
	
}
