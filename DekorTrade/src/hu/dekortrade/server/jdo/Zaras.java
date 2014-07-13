package hu.dekortrade.server.jdo;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Zaras {

	@Persistent
	private String zaras;

	@Persistent
	private String penztaros;

	@Persistent
	private Date datum;

	public Zaras(String zaras, String penztaros, Date datum) {
		this.zaras = zaras;
		this.penztaros = penztaros;
		this.datum = datum;
	}

	public String getZaras() {
		return zaras;
	}

	public void setZaras(String zaras) {
		this.zaras = zaras;
	}

	public String getPenztaros() {
		return penztaros;
	}

	public void setPenztaros(String penztaros) {
		this.penztaros = penztaros;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

}
