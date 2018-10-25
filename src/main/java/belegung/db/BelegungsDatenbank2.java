package belegung.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import allgemein.model.Standort;
import allgemein.model.StandortEnum;
import belegung.model.Belegung;
import belegung.model.Stockwerk;
import belegung.model.StockwerkEnum;
import benutzungsstatistik.db.WintikurierDatenbank;
import benutzungsstatistik.model.Wintikurier;

/**
 * Das ist die DAO-Klasse (Data Access Object) von der Belegung. Man kann damit
 * die Tabelle 'Belegung' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
public class BelegungsDatenbank2 {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der BelegungDatenbank
	 */
	public BelegungsDatenbank2() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
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
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Belegung> selectAllBelegungen() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Belegung> belegungenListe = new ArrayList<Belegung>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			belegungenListe = tempSession.createQuery("From Belegung")
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

		return belegungenListe;
	}
	
	/**
	 * @return Belegung für ein bestimmtes Datum an einem bestimmten Standort
	 */
	public Belegung selectBelegungForDateAndStandort(Date date, Standort standort) {

		List<Belegung> belegungenListe = selectAllBelegungen();
		Belegung belegung = null;

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		for(Belegung b : belegungenListe) {
			if(b.getStandort().getName().equals(standort.getName())) {
				if(sdf.format(b.getDatum()).equals(sdf.format(date))){
					belegung = b;
				}
			}
		}
		
		//Falls es keine Belegung für das Datum gibt, erstelle eine Belegung
		if(belegung == null) {
			belegung = new Belegung(date, standort);
			
			if(standort.getName() == StandortEnum.WINTERTHUR_BB) {
				belegung.addStockwerk(new Stockwerk(StockwerkEnum.EG, false, false, true, false, new KapazitätDatenbank2().selectKapazitätForStockwerk(StockwerkEnum.EG)));
				belegung.addStockwerk(new Stockwerk(StockwerkEnum.ZG1, false, false, false, false, new KapazitätDatenbank2().selectKapazitätForStockwerk(StockwerkEnum.ZG1)));
				belegung.addStockwerk(new Stockwerk(StockwerkEnum.ZG2, false, false, false, false, new KapazitätDatenbank2().selectKapazitätForStockwerk(StockwerkEnum.ZG2)));
			}else if (standort.getName() == StandortEnum.WINTERTHUR_LL) {
				belegung.addStockwerk(new Stockwerk(StockwerkEnum.LL, true, true, true, true, new KapazitätDatenbank2().selectKapazitätForStockwerk(StockwerkEnum.LL)));
			}			
			
			insertBelegung(belegung);
			System.out.println("Belegung gespeichert "+belegung.getBelegungs_ID());
		}

		return belegung;
	}

	
}
