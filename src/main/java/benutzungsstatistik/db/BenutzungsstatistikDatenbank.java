package benutzungsstatistik.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import allgemein.model.StandortEnum;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Internerkurier;
import benutzungsstatistik.model.Wintikurier;

/**
 * Das ist die DAO-Klasse (Data Access Object) von der Benutzungsstatistik. Man
 * kann damit die Tabelle 'Benutzungsstatistik' verwalten. Sie öffnet
 * Connections, schliesst diese wieder und hat alle Funktionen implementiert.
 * (Delete, Insert, Update, Select)
 * 
 * @author Marvin Bindemann
 */
@SuppressWarnings("deprecation")
public class BenutzungsstatistikDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der BenutzungsstatistikDatenbank
	 */
	public BenutzungsstatistikDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
	}

	/**
	 * Neue Benutzungsstatistik in die DB hinzufuegen
	 * 
	 * @param benutzungsstatistik
	 */
	public void insertBenutzungsstatistik(Benutzungsstatistik benutzungsstatistik) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(benutzungsstatistik);

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
	 * Benutzungsstatistik aktualisieren
	 * 
	 * @param benutzungsstatistik
	 */
	public void updateBenutzungsstatistik(Benutzungsstatistik benutzungsstatistik) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.update(benutzungsstatistik);

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
	 * @return Liste von allen Benutzungsstatistiken
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Benutzungsstatistik> selectAllBenutzungsstatistiken() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Benutzungsstatistik> benutzungsstatistikenListe = new ArrayList<Benutzungsstatistik>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			benutzungsstatistikenListe = tempSession.createQuery("From Benutzungsstatistik").list();
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

		return benutzungsstatistikenListe;
	}

	/**
	 * @return Benutzungsstatistik für ein bestimmtes Datum an einem bestimmten
	 *         Standort
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Benutzungsstatistik selectBenutzungsstatistikForDateAndStandort(Date date, StandortEnum standort) {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Benutzungsstatistik> benutzungsstatistikenListe = new ArrayList<Benutzungsstatistik>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			String hql = "FROM Benutzungsstatistik B WHERE B.datum = :datum AND B.standort = :standort";
			Query query = tempSession.createQuery(hql);
			query.setParameter("datum", date);
			query.setParameter("standort", standort);
			benutzungsstatistikenListe = query.list();

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

		Benutzungsstatistik benutzungsstatistik = null;

		for (Benutzungsstatistik b : benutzungsstatistikenListe) {
			benutzungsstatistik = b;
		}

		// Falls es keine Benutzungsstatistik für das Datum gibt, erstelle eine
		// Benutzungsstatistik
		if (benutzungsstatistik == null) {
			if(standort == StandortEnum.WINTERTHUR_BB) {
				benutzungsstatistik = new Benutzungsstatistik(date, 0, false, standort, new Wintikurier(0, 0, 0, 0));
			}else if(standort == StandortEnum.WINTERTHUR_LL) {
				benutzungsstatistik = new Benutzungsstatistik(date, standort);
			}else if(standort == StandortEnum.WÄDENSWIL) {
				benutzungsstatistik = new Benutzungsstatistik(date, 0, standort, new Internerkurier(0, 0, 0));
			}
			
			insertBenutzungsstatistik(benutzungsstatistik);
			System.out.println("Benutzungsstatistik gespeichert " + benutzungsstatistik.getBenutzungsstatistik_ID());
		}

		return benutzungsstatistik;
	}
	
	/**
	 * @return Benutzungsstatistik für eine ID
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Benutzungsstatistik findBenutzungsstatistikById(int benutzungsstatistik_ID) {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Benutzungsstatistik> benutzungsstatistikenListe = new ArrayList<Benutzungsstatistik>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			String hql = "FROM Benutzungsstatistik B WHERE B.benutzungsstatistik_ID = :benutzungsstatistik_ID";
			Query query = tempSession.createQuery(hql);
			query.setParameter("benutzungsstatistik_ID", benutzungsstatistik_ID);
			benutzungsstatistikenListe = query.list();

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

		if(benutzungsstatistikenListe.size() == 1) {
			return benutzungsstatistikenListe.get(0);	
		}else {
			return null;
		}
	}

}
