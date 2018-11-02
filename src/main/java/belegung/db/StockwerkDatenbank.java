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
import belegung.model.Arbeitsplätze;
import belegung.model.Belegung;
import belegung.model.Stockwerk;
import belegung.model.StockwerkEnum;

/**
 * Das ist die DAO-Klasse (Data Access Object) vom Stockwerk. Man kann damit
 * die Tabelle 'Stockwerk' verwalten. Sie öffnet Connections, schliesst diese
 * wieder und hat alle Funktionen implementiert. (Delete, Insert, Update,
 * Select)
 * 
 * @author Marvin Bindemann
 */
public class StockwerkDatenbank {

	private static SessionFactory sessionFactory;

	/**
	 * Konstruktor der BelegungDatenbank
	 */
	public StockwerkDatenbank() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
	}

	/**
	 * Neuen Stockwerk in die DB hinzufuegen
	 * 
	 * @param stockwerk
	 */
	public void insertStockwerk(Stockwerk stockwerk) {

		Session tempSession = null;
		Transaction tempTransaction = null;

		try {
			tempSession = sessionFactory.openSession();
			tempTransaction = tempSession.beginTransaction();

			tempSession.save(stockwerk);

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
	 * Suche ein Stockwerk in der Belegung für ein Stockwerkenum
	 * 
	 * @return Stockwerk
	 */
	public Stockwerk getStockwerkByEnum(StockwerkEnum stockwerkEnum, Belegung belegung) {
		for(Stockwerk s : belegung.getStockwerkListe()) {
			if(s.getName() == stockwerkEnum) {
				return s;
			}
		}
		return null;
	}
	

}
