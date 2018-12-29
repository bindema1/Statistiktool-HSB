package allgemein.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "email")
public class Email implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int email_ID;
	
	//Wann zuletzt eine Email versendet wurde
	@Temporal(TemporalType.TIMESTAMP)
	private Date versendetTimestamp;

	// Für Hibernate
	public Email(){
					
	}
	
	public Email(Date versendetTimestamp){
		this.versendetTimestamp = versendetTimestamp;
	}

	public Date getVersendetTimestamp() {
		return versendetTimestamp;
	}

	public void setVersendetTimestamp(Date versendetTimestamp) {
		this.versendetTimestamp = versendetTimestamp;
	}

	public int getEmail_ID() {
		return email_ID;
	}

	public void setEmail_ID(int email_ID) {
		this.email_ID = email_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + email_ID;
		result = prime * result + ((versendetTimestamp == null) ? 0 : versendetTimestamp.hashCode());
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
		Email other = (Email) obj;
		if (email_ID != other.email_ID)
			return false;
		if (versendetTimestamp == null) {
			if (other.versendetTimestamp != null)
				return false;
		} else if (!versendetTimestamp.equals(other.versendetTimestamp))
			return false;
		return true;
	}

}
