package belegung.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Das ist die Datenklasse mit allen Attributen, damit man die Kapazität in die
 * Tabelle 'Kapazität' der Datenbank schreiben kann.
 * 
 * @author Marvin Bindemann
 */
@Entity
@Table(name = "kapazität")
public class Kapazität implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int kapzität_ID;

	@Enumerated(EnumType.STRING)
	private StockwerkEnum stockwerk;

	//100% Zahlen der Arbeitsplätze z.B. 80
	private int hunderProzentArbeitsplätze;
	private int hunderProzentSektorA;
	private int hunderProzentSektorB;
	private int hunderProzentCarrels;
	private int hunderProzentCarrelsPersonen;
	private int hunderProzentGruppenräume;
	private int hunderProzentGruppenräumePersonen;
	//Maximalanzahl was möglich ist. z.B. Arbeitsplätze 100
	private int maxArbeitsplätze;
	private int maxSektorA;
	private int maxSektorB;
	private int maxCarrels;
	private int maxCarrelsPersonen;
	private int maxGruppenräume;
	private int maxGruppenräumePersonen;
	

	// Für Hibernate
	public Kapazität() {

	}

	public Kapazität(StockwerkEnum stockwerk, int hunderProzentArbeitsplätze, int hunderProzentSektorA,
			int hunderProzentSektorB, int hunderProzentCarrels, int hunderProzentCarrelsPersonen,
			int hunderProzentGruppenräume, int hunderProzentGruppenräumePersonen, int maxArbeitsplätze, int maxSektorA,
			int maxSektorB, int maxCarrels, int maxCarrelsPersonen, int maxGruppenräume, int maxGruppenräumePersonen) {
		super();
		this.stockwerk = stockwerk;
		this.hunderProzentArbeitsplätze = hunderProzentArbeitsplätze;
		this.hunderProzentSektorA = hunderProzentSektorA;
		this.hunderProzentSektorB = hunderProzentSektorB;
		this.hunderProzentCarrels = hunderProzentCarrels;
		this.hunderProzentCarrelsPersonen = hunderProzentCarrelsPersonen;
		this.hunderProzentGruppenräume = hunderProzentGruppenräume;
		this.hunderProzentGruppenräumePersonen = hunderProzentGruppenräumePersonen;
		this.maxArbeitsplätze = maxArbeitsplätze;
		this.maxSektorA = maxSektorA;
		this.maxSektorB = maxSektorB;
		this.maxCarrels = maxCarrels;
		this.maxCarrelsPersonen = maxCarrelsPersonen;
		this.maxGruppenräume = maxGruppenräume;
		this.maxGruppenräumePersonen = maxGruppenräumePersonen;
	}

	public StockwerkEnum getStockwerk() {
		return stockwerk;
	}

	public int getKapzität_ID() {
		return kapzität_ID;
	}

	public int getHunderProzentArbeitsplätze() {
		return hunderProzentArbeitsplätze;
	}

	public int getHunderProzentSektorA() {
		return hunderProzentSektorA;
	}

	public int getHunderProzentSektorB() {
		return hunderProzentSektorB;
	}

	public int getHunderProzentCarrels() {
		return hunderProzentCarrels;
	}

	public int getHunderProzentGruppenräume() {
		return hunderProzentGruppenräume;
	}
	
	public int getHunderProzentCarrelsPersonen() {
		return hunderProzentCarrelsPersonen;
	}

	public int getHunderProzentGruppenräumePersonen() {
		return hunderProzentGruppenräumePersonen;
	}

	public int getMaxArbeitsplätze() {
		return maxArbeitsplätze;
	}

	public int getMaxSektorA() {
		return maxSektorA;
	}

	public int getMaxSektorB() {
		return maxSektorB;
	}

	public int getMaxCarrels() {
		return maxCarrels;
	}

	public int getMaxCarrelsPersonen() {
		return maxCarrelsPersonen;
	}

	public int getMaxGruppenräume() {
		return maxGruppenräume;
	}

	public int getMaxGruppenräumePersonen() {
		return maxGruppenräumePersonen;
	}

	// Für Hibernate alle Set-Methoden, Hashcode und equals
	public void setStockwerk(StockwerkEnum stockwerk) {
		this.stockwerk = stockwerk;
	}

	public void setKapzität_ID(int kapzität_ID) {
		this.kapzität_ID = kapzität_ID;
	}

	public void setHunderProzentArbeitsplätze(int maxArbeitsplätze) {
		this.hunderProzentArbeitsplätze = maxArbeitsplätze;
	}

	public void setHunderProzentSektorA(int maxSektorA) {
		this.hunderProzentSektorA = maxSektorA;
	}

	public void setHunderProzentSektorB(int maxSektorB) {
		this.hunderProzentSektorB = maxSektorB;
	}

	public void setHunderProzentCarrels(int maxCarrels) {
		this.hunderProzentCarrels = maxCarrels;
	}

	public void setHunderProzentGruppenräume(int maxGruppenräume) {
		this.hunderProzentGruppenräume = maxGruppenräume;
	}

	public void setHunderProzentCarrelsPersonen(int hunderProzentCarrelsPersonen) {
		this.hunderProzentCarrelsPersonen = hunderProzentCarrelsPersonen;
	}

	public void setHunderProzentGruppenräumePersonen(int hunderProzentGruppenräumePersonen) {
		this.hunderProzentGruppenräumePersonen = hunderProzentGruppenräumePersonen;
	}

	public void setMaxArbeitsplätze(int maxArbeitsplätze) {
		this.maxArbeitsplätze = maxArbeitsplätze;
	}

	public void setMaxSektorA(int maxSektorA) {
		this.maxSektorA = maxSektorA;
	}

	public void setMaxSektorB(int maxSektorB) {
		this.maxSektorB = maxSektorB;
	}

	public void setMaxCarrels(int maxCarrels) {
		this.maxCarrels = maxCarrels;
	}

	public void setMaxCarrelsPersonen(int maxCarrelsPersonen) {
		this.maxCarrelsPersonen = maxCarrelsPersonen;
	}

	public void setMaxGruppenräume(int maxGruppenräume) {
		this.maxGruppenräume = maxGruppenräume;
	}

	public void setMaxGruppenräumePersonen(int maxGruppenräumePersonen) {
		this.maxGruppenräumePersonen = maxGruppenräumePersonen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hunderProzentArbeitsplätze;
		result = prime * result + hunderProzentCarrels;
		result = prime * result + hunderProzentCarrelsPersonen;
		result = prime * result + hunderProzentGruppenräume;
		result = prime * result + hunderProzentGruppenräumePersonen;
		result = prime * result + hunderProzentSektorA;
		result = prime * result + hunderProzentSektorB;
		result = prime * result + kapzität_ID;
		result = prime * result + maxArbeitsplätze;
		result = prime * result + maxCarrels;
		result = prime * result + maxCarrelsPersonen;
		result = prime * result + maxGruppenräume;
		result = prime * result + maxGruppenräumePersonen;
		result = prime * result + maxSektorA;
		result = prime * result + maxSektorB;
		result = prime * result + ((stockwerk == null) ? 0 : stockwerk.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kapazität other = (Kapazität) obj;
		if (hunderProzentArbeitsplätze != other.hunderProzentArbeitsplätze)
			return false;
		if (hunderProzentCarrels != other.hunderProzentCarrels)
			return false;
		if (hunderProzentCarrelsPersonen != other.hunderProzentCarrelsPersonen)
			return false;
		if (hunderProzentGruppenräume != other.hunderProzentGruppenräume)
			return false;
		if (hunderProzentGruppenräumePersonen != other.hunderProzentGruppenräumePersonen)
			return false;
		if (hunderProzentSektorA != other.hunderProzentSektorA)
			return false;
		if (hunderProzentSektorB != other.hunderProzentSektorB)
			return false;
		if (kapzität_ID != other.kapzität_ID)
			return false;
		if (maxArbeitsplätze != other.maxArbeitsplätze)
			return false;
		if (maxCarrels != other.maxCarrels)
			return false;
		if (maxCarrelsPersonen != other.maxCarrelsPersonen)
			return false;
		if (maxGruppenräume != other.maxGruppenräume)
			return false;
		if (maxGruppenräumePersonen != other.maxGruppenräumePersonen)
			return false;
		if (maxSektorA != other.maxSektorA)
			return false;
		if (maxSektorB != other.maxSektorB)
			return false;
		if (stockwerk != other.stockwerk)
			return false;
		return true;
	}

}
