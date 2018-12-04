package belegung.db;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import belegung.model.Kapazität;
import belegung.model.StockwerkEnum;

public class TestKapazitätDatenbank {

	KapazitätDatenbank kapazitätsDB = new KapazitätDatenbank();
	Kapazität kapazitätTest;
	Kapazität kapazitätEG;
	Kapazität kapazität1ZG;
	Kapazität kapazität2ZG;
	Kapazität kapazitätLL;
	
	
	@Before
	public void initComponents() {
		kapazitätTest = new Kapazität(StockwerkEnum.TEST, 80, 0, 0, 0, 0, 3, 12, 100, 0, 0, 0, 0, 3, 25);
		kapazitätEG = new Kapazität(StockwerkEnum.EG, 80, 0, 0, 0, 0, 3, 12, 100, 0, 0, 0, 0, 3, 25);
		kapazität1ZG = new Kapazität(StockwerkEnum.ZG1, 80, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0);
		kapazität2ZG = new Kapazität(StockwerkEnum.ZG2, 160, 0, 0, 0, 0, 0, 0, 190, 0, 0, 0, 0, 0, 0);
		kapazitätLL = new Kapazität(StockwerkEnum.LL, 0, 120, 135, 12, 12, 8, 24, 0, 160, 150, 12, 25, 8, 45);
	}
	
	@Test
	public void testinsertKapazität() {
		kapazitätsDB.insertKapazität(kapazitätTest);
		kapazitätsDB.insertKapazität(kapazitätEG);
		kapazitätsDB.insertKapazität(kapazität1ZG);
		kapazitätsDB.insertKapazität(kapazität2ZG);
		kapazitätsDB.insertKapazität(kapazitätLL);
	}
	
	@Test
	public void selectKapazitätForStockwerk() {
		kapazitätsDB.insertKapazität(kapazitätTest);
		kapazitätsDB.insertKapazität(kapazitätEG);
		kapazitätsDB.insertKapazität(kapazität1ZG);
		kapazitätsDB.insertKapazität(kapazität2ZG);
		kapazitätsDB.insertKapazität(kapazitätLL);
		
		Kapazität k = kapazitätsDB.selectKapazitätForStockwerk(StockwerkEnum.TEST);

		assertEquals(k.getHunderProzentArbeitsplätze(), kapazitätTest.getHunderProzentArbeitsplätze());
		assertEquals(k.getMaxCarrels(), kapazitätTest.getMaxCarrels());
		assertEquals(k.getMaxGruppenräume(), kapazitätTest.getMaxGruppenräume());
		assertEquals(k.getMaxSektorA(), kapazitätTest.getMaxSektorA());
		assertEquals(k.getMaxSektorB(), kapazitätTest.getMaxSektorB());
	}
}
