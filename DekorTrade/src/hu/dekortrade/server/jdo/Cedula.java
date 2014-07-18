package hu.dekortrade.server.jdo;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Cedula {

	@Persistent
	private String cedula;

	@Persistent
	private String rovidnev;

	@Persistent
	private String status;

	@Persistent
	private String elado;

	@Persistent
	private Float befizethuf;

	@Persistent
	private Float befizeteur;

	@Persistent
	private Float befizetusd;

	@Persistent
	private Date datum;

	public Cedula(String cedula, String rovidnev, String status, String elado, Float befizethuf, Float befizeteur, Float befizetusd,
			Date datum) {
		this.cedula = cedula;
		this.rovidnev = rovidnev;
		this.status = status;
		this.elado = elado;
		this.befizethuf = befizethuf;
		this.befizeteur = befizeteur;
		this.befizetusd = befizetusd;
		this.datum = datum;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public String getElado() {
		return elado;
	}

	public void setElado(String elado) {
		this.elado = elado;
	}

	public Float getBefizethuf() {
		return befizethuf;
	}

	public void setBefizethuf(Float befizethuf) {
		this.befizethuf = befizethuf;
	}

	public Float getBefizeteur() {
		return befizeteur;
	}

	public void setBefizeteur(Float befizeteur) {
		this.befizeteur = befizeteur;
	}

	public Float getBefizetusd() {
		return befizetusd;
	}

	public void setBefizetusd(Float befizetusd) {
		this.befizetusd = befizetusd;
	}

}
