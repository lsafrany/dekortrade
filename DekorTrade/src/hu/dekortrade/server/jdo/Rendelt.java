package hu.dekortrade.server.jdo;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Rendelt {
	
	@Persistent
	private String rovidnev;

	@Persistent
	private String rendeles;
	
	@Persistent
	private Date datum;
	
	public Rendelt(String rovidnev,String rendeles, Date datum) {
		this.rovidnev = rovidnev;
		this.rendeles = rendeles;
		this.datum = datum;
	}

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
	}

	public String getRendeles() {
		return rendeles;
	}

	public void setRendeles(String rendeles) {
		this.rendeles = rendeles;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}
	
}

