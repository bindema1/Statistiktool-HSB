package benutzungsstatistik.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import Allgemein.model.Standort;
import benutzungsstatistik.model.Benutzungsstatistik;
import benutzungsstatistik.model.Wintikurier;

/**
 * Das ist die DAO-Klasse (Data Access Object) von der Benutzungsstatistik. Man kann damit
 * die Tabelle 'Benutzungsstatistik' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
public class BenutzungsstatistikDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der BenutzungsstatistikDatenbank
	 */
	public BenutzungsstatistikDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
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
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Benutzungsstatistik> selectAllBenutzungsstatistiken() {

		Session tempSession = null;
		Transaction tempTransaction = null;
		List<Benutzungsstatistik> benutzungsstatistikenListe = new ArrayList<Benutzungsstatistik>();

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();
			benutzungsstatistikenListe = tempSession.createQuery("From Benutzungsstatistik")
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

		return benutzungsstatistikenListe;
	}
	
	/**
	 * @return Benutzungsstatistik für ein bestimmtes Datum an einem bestimmten Standort
	 */
	public Benutzungsstatistik selectBenutzungsstatistikForDateAndStandort(Date date, Standort standort) {

		List<Benutzungsstatistik> benutzungsstatistikenListe = selectAllBenutzungsstatistiken();
		Benutzungsstatistik benutzungsstatistik = null;

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		for(Benutzungsstatistik b : benutzungsstatistikenListe) {
			if(b.getStandort().getStandort_ID() == standort.getStandort_ID())
				if(sdf.format(b.getDatum()).equals(sdf.format(date))){
					benutzungsstatistik = b;
				}
		}
		
		//Falls es keine Benutzungsstatistik für das Datum gibt, erstelle eine Benutzungsstatistik
		if(benutzungsstatistik == null) {
			WintikurierDatenbank wintikurierDB = new WintikurierDatenbank();
			Wintikurier wintikurier = new Wintikurier(0, 0, 0, 0);
			wintikurierDB.insertWintikurier(wintikurier);
			
			benutzungsstatistik = new Benutzungsstatistik(date, 0, false, standort, wintikurier);
			insertBenutzungsstatistik(benutzungsstatistik);
		}

		return benutzungsstatistik;
	}

	
}
