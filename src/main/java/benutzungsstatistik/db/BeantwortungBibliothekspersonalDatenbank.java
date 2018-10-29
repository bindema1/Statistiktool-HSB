package benutzungsstatistik.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import benutzungsstatistik.model.BeantwortungBibliothekspersonal;

/**
 * Das ist die DAO-Klasse (Data Access Object) von BeantwortungBibliothekspersonal. Man kann damit
 * die Tabelle 'BeantwortungBibliothekspersonal' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
public class BeantwortungBibliothekspersonalDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der BeantwortungBibliothekspersonalDatenbank
	 */
	public BeantwortungBibliothekspersonalDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		}
	}

	/**
	 * Neuen BeantwortungBibliothekspersonal in die DB hinzufuegen
	 * 
	 * @param beantwortungBibliothekspersonal
	 */
	public void insertBeantwortungBibliothekspersonal(BeantwortungBibliothekspersonal beantwortungBibliothekspersonal) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(beantwortungBibliothekspersonal);

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
	 * BeantwortungBibliothekspersonal löschen
	 * 
	 * @param beantwortungBibliothekspersonal
	 */
	public void deleteBeantwortungBibliothekspersonal(BeantwortungBibliothekspersonal beantwortungBibliothekspersonal) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.delete(beantwortungBibliothekspersonal);

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
	 * @return Liste von allen BeantwortungBibliothekspersonalen
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<BeantwortungBibliothekspersonal> selectAllBeantwortungBibliothekspersonalen() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<BeantwortungBibliothekspersonal> beantwortungBibliothekspersonalenListe = new ArrayList<BeantwortungBibliothekspersonal>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			beantwortungBibliothekspersonalenListe = tempSession.createQuery("From BeantwortungBibliothekspersonal")
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

		return beantwortungBibliothekspersonalenListe;
	}
	
	/**
	 * @return Liste von allen BeantwortungBibliothekspersonalen für eine Benutzungsstatistik
	 */
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public List<BeantwortungBibliothekspersonal> selectAllBeantwortungBibliothekspersonalenForBenutzungsstatistik(int benutzungsstatistik_ID) {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<BeantwortungBibliothekspersonal> beantwortungBibliothekspersonalenListe = new ArrayList<BeantwortungBibliothekspersonal>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			String hql = "FROM Beantwortungbibliothekspersonal T WHERE T.benutzungsstatistik.id = :id";
			Query query = tempSession.createQuery(hql);
			query.setParameter("id", benutzungsstatistik_ID);
			beantwortungBibliothekspersonalenListe = query.list();
			
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

		return beantwortungBibliothekspersonalenListe;
	}
	
}
