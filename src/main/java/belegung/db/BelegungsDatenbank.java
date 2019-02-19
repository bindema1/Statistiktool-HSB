package belegung.db;

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
import belegung.model.Belegung;
import belegung.model.Stockwerk;
import belegung.model.StockwerkEnum;

/**
 * Das ist die DAO-Klasse (Data Access Object) von der Belegung. Man kann damit
 * die Tabelle 'Belegung' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
@SuppressWarnings("deprecation")
public class BelegungsDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der BelegungDatenbank
	 */
	public BelegungsDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
	}

	/**
	 * Neue Belegung in die DB hinzufuegen
	 * 
	 * @param belegung
	 */
	public void insertBelegung(Belegung belegung) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(belegung);

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
	 * Belegung aktualisieren
	 * 
	 * @param belegung
	 */
	public void updateBelegung(Belegung belegung) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.update(belegung);

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
	 * @return Liste von allen Belegungen
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Belegung> selectAllBelegungen() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Belegung> belegungenListe = new ArrayList<Belegung>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			belegungenListe = tempSession.createQuery("From Belegung").list();
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

		return belegungenListe;
	}

	/**
	 * @return Belegung für ein bestimmtes Datum an einem bestimmten Standort
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Belegung selectBelegungForDateAndStandort(Date date, StandortEnum standort) {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Belegung> belegungenListe = new ArrayList<Belegung>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			String hql = "FROM Belegung B WHERE B.datum = :datum AND B.standort = :standort";
			Query query = tempSession.createQuery(hql);
			query.setParameter("datum", date);
			query.setParameter("standort", standort);
			belegungenListe = query.list();

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

		Belegung belegung = null;

		if (belegungenListe.size() == 1) {
			// Wenn genau eine Belegung gefunden wurde
			belegung = belegungenListe.get(0);
		} else if (belegungenListe.size() > 1) {
			// Falls es mehr als nur eine Belegung für das Datum gibt -> Fehler, darf nicht
			// passieren
			System.out.println("WARNING: Mehrere Belegungen für das selbe Datum");
			for (Belegung b : belegungenListe) {
				deleteBelegung(b);
			}
		}
		
		if (belegung == null) {
			// Falls es keine Belegung für das Datum gibt, erstelle eine Belegung
			belegung = new Belegung(date, standort);

			if (standort == StandortEnum.WINTERTHUR_BB) {
				belegung.addStockwerk(new Stockwerk(StockwerkEnum.EG, false, false, true, false,
						new KapazitätDatenbank().selectKapazitätForStockwerk(StockwerkEnum.EG)));
				belegung.addStockwerk(new Stockwerk(StockwerkEnum.ZG1, false, false, false, false,
						new KapazitätDatenbank().selectKapazitätForStockwerk(StockwerkEnum.ZG1)));
				belegung.addStockwerk(new Stockwerk(StockwerkEnum.ZG2, false, false, false, false,
						new KapazitätDatenbank().selectKapazitätForStockwerk(StockwerkEnum.ZG2)));
			} else if (standort == StandortEnum.WINTERTHUR_LL) {
				belegung.addStockwerk(new Stockwerk(StockwerkEnum.LL, true, true, true, true,
						new KapazitätDatenbank().selectKapazitätForStockwerk(StockwerkEnum.LL)));
			} else if (standort == StandortEnum.WÄDENSWIL) {
				belegung.addStockwerk(new Stockwerk(StockwerkEnum.WÄDI, false, false, false, false,
						new KapazitätDatenbank().selectKapazitätForStockwerk(StockwerkEnum.WÄDI)));
			}

			insertBelegung(belegung);
			System.out.println("Belegung gespeichert " + belegung.getBelegungs_ID());
		}

		return belegung;
	}
	
	/**
	 * Belegung löschen
	 * 
	 * @param belegung
	 */
	public void deleteBelegung(Belegung belegung) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.delete(belegung);

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

}
