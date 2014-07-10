package hu.dekortrade.shared.serialized;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FizetesSer implements IsSerializable {

	private String cedula;

	private String vevo;

	private String vevonev;
	
	private String tipus;

	private String penztaros;

	private String penztarosnev;
	
	private Float fizet;
	
	private Float fizeteur;	

	private Float fizetusd;	

	private Date datum;

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getVevo() {
		return vevo;
	}

	public void setVevo(String vevo) {
		this.vevo = vevo;
	}

	public String getVevonev() {
		return vevonev;
	}

	public void setVevonev(String vevonev) {
		this.vevonev = vevonev;
	}

	public String getTipus() {
		return tipus;
	}

	public void setTipus(String tipus) {
		this.tipus = tipus;
	}

	public String getPenztaros() {
		return penztaros;
	}

	public void setPenztaros(String penztaros) {
		this.penztaros = penztaros;
	}

	public String getPenztarosnev() {
		return penztarosnev;
	}

	public void setPenztarosnev(String penztarosnev) {
		this.penztarosnev = penztarosnev;
	}

	public Float getFizet() {
		return fizet;
	}

	public void setFizet(Float fizet) {
		this.fizet = fizet;
	}

	public Float getFizeteur() {
		return fizeteur;
	}

	public void setFizeteur(Float fizeteur) {
		this.fizeteur = fizeteur;
	}

	public Float getFizetusd() {
		return fizetusd;
	}

	public void setFizetusd(Float fizetusd) {
		this.fizetusd = fizetusd;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

}
