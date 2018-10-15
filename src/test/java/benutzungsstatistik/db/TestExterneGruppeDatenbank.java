package benutzungsstatistik.db;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Allgemein.db.StandortDatenbank;
import Allgemein.model.Standort;
import benutzungsstatistik.db.ExterneGruppeDatenbank;
import benutzungsstatistik.db.BenutzungsstatistikDatenbank;
import benutzungsstatistik.model.ExterneGruppe;
import benutzungsstatistik.model.Benutzungsstatistik;

/**
 * Testet alle Methoden der ExterneGruppeDatenbank
 * 
 * @author Marvin Bindemann
 */
public class TestExterneGruppeDatenbank {

	ExterneGruppeDatenbank externeGruppeDB = new ExterneGruppeDatenbank();
	ExterneGruppe externeGruppe;
	StandortDatenbank standortDB = new StandortDatenbank();
	BenutzungsstatistikDatenbank benutzungsstatistikDB = new BenutzungsstatistikDatenbank();

	@Before
	public void initComponents() {
		Standort standort = new Standort("Test Standort");
		standortDB.insertStandort(standort);
		Benutzungsstatistik benutzungsstatistik = new Benutzungsstatistik(new Date(), 8, true, standort);
		benutzungsstatistikDB.insertBenutzungsstatistik(benutzungsstatistik);

		externeGruppe = new ExterneGruppe("Test Gruppe", 10, benutzungsstatistik);
	}

	@Test
	public void testinsertExterneGruppe() {
		externeGruppeDB.insertExterneGruppe(externeGruppe);
	}

	@Test
	public void testselectAllExterneGruppee() {
		externeGruppeDB.insertExterneGruppe(externeGruppe);

		List<ExterneGruppe> externeGruppeListe = new ArrayList<ExterneGruppe>();
		externeGruppeListe = externeGruppeDB.selectAllExterneGruppen();

		assertEquals(externeGruppeListe.get(externeGruppeListe.size() - 1).getExterneGruppe_ID(),
				externeGruppe.getExterneGruppe_ID());
		assertEquals(externeGruppeListe.get(externeGruppeListe.size() - 1).getName(), externeGruppe.getName());
		assertEquals(externeGruppeListe.get(externeGruppeListe.size() - 1).getAnzahl_Personen(),
				externeGruppe.getAnzahl_Personen());
		assertEquals(externeGruppeListe.get(externeGruppeListe.size() - 1).getBenutzungsstatistik().getBenutzungsstatistik_ID(),
				externeGruppe.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}

	@Test
	public void testselectAllExterneGruppeeForBenutzungsstatistik() {
		externeGruppeDB.insertExterneGruppe(externeGruppe);

		List<ExterneGruppe> externeGruppeListe = new ArrayList<ExterneGruppe>();
		externeGruppeListe = externeGruppeDB
				.selectAllExterneGruppenForBenutzungsstatistik(externeGruppe.getBenutzungsstatistik().getBenutzungsstatistik_ID());

		assertEquals(externeGruppeListe.get(0).getExterneGruppe_ID(), externeGruppe.getExterneGruppe_ID());
		assertEquals(externeGruppeListe.get(0).getName(), externeGruppe.getName());
		assertEquals(externeGruppeListe.get(0).getAnzahl_Personen(), externeGruppe.getAnzahl_Personen());
		assertEquals(externeGruppeListe.get(0).getBenutzungsstatistik().getBenutzungsstatistik_ID(),
				externeGruppe.getBenutzungsstatistik().getBenutzungsstatistik_ID());
	}

	@Test
	public void testdeleteExterneGruppe() {
		List<ExterneGruppe> externeGruppeListe = new ArrayList<ExterneGruppe>();
		externeGruppeListe = externeGruppeDB.selectAllExterneGruppen();

		for (ExterneGruppe k : externeGruppeListe) {
			externeGruppeDB.deleteExterneGruppe(k);
		}

		externeGruppeListe = externeGruppeDB.selectAllExterneGruppen();

		assertEquals(externeGruppeListe.size(), 0);
	}

	@Test
	public void testupdateExterneGruppe() {
		externeGruppeDB.insertExterneGruppe(externeGruppe);
		externeGruppe.setAnzahl_Personen(5);
		externeGruppe.setName("Test Gruppe 2");
		externeGruppeDB.updateExterneGruppe(externeGruppe);

		List<ExterneGruppe> externeGruppeListe = new ArrayList<ExterneGruppe>();
		externeGruppeListe = externeGruppeDB.selectAllExterneGruppen();

		assertEquals(externeGruppeListe.get(externeGruppeListe.size() - 1).getName(), externeGruppe.getName());
		assertEquals(externeGruppeListe.get(externeGruppeListe.size() - 1).getAnzahl_Personen(),
				externeGruppe.getAnzahl_Personen());

	}
}
