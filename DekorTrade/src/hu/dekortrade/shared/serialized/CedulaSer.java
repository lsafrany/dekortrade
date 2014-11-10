package hu.dekortrade.shared.serialized;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CedulaSer implements IsSerializable {

	private String cedula;

	private String rovidnev;

	private String vevonev;

	private String vevotipus;
	
	private String status;

	private String elado;

	private String eladonev;

	private Double befizethuf;

	private Double befizeteur;

	private Double befizetusd;
	
	private Date datum;

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

	public String getVevonev() {
		return vevonev;
	}

	public void setVevonev(String vevonev) {
		this.vevonev = vevonev;
	}
		
	public String getVevotipus() {
		return vevotipus;
	}

	public void setVevotipus(String vevotipus) {
		this.vevotipus = vevotipus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getElado() {
		return elado;
	}
	
	public void setElado(String elado) {
		this.elado = elado;
	}

	public String getEladonev() {
		return eladonev;
	}

	public void setEladonev(String eladonev) {
		this.eladonev = eladonev;
	}

	public Double getBefizethuf() {
		return befizethuf;
	}

	public void setBefizethuf(Double befizethuf) {
		this.befizethuf = befizethuf;
	}

	public Double getBefizeteur() {
		return befizeteur;
	}

	public void setBefizeteur(Double befizeteur) {
		this.befizeteur = befizeteur;
	}

	public Double getBefizetusd() {
		return befizetusd;
	}

	public void setBefizetusd(Double befizetusd) {
		this.befizetusd = befizetusd;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

}
