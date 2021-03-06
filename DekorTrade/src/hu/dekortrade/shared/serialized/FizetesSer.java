package hu.dekortrade.shared.serialized;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FizetesSer implements IsSerializable {

	private String cedula;

	private String vevo;

	private String vevonev;

	private String tipus;

	private String megjegyzes;
	
	private String penztaros;

	private String penztarosnev;

	private Double fizet;

	private Double fizeteur;

	private Double fizetusd;

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

	public String getMegjegyzes() {
		return megjegyzes;
	}

	public void setMegjegyzes(String megjegyzes) {
		this.megjegyzes = megjegyzes;
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

	public Double getFizet() {
		return fizet;
	}

	public void setFizet(Double fizet) {
		this.fizet = fizet;
	}

	public Double getFizeteur() {
		return fizeteur;
	}

	public void setFizeteur(Double fizeteur) {
		this.fizeteur = fizeteur;
	}

	public Double getFizetusd() {
		return fizetusd;
	}

	public void setFizetusd(Double fizetusd) {
		this.fizetusd = fizetusd;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

}
