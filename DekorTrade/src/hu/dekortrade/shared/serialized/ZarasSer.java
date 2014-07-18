package hu.dekortrade.shared.serialized;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ZarasSer implements IsSerializable {

	private String zaras;

	private String penztaros;

	private String penztarosnev;

	private Float kivetusd;

	private Float kiveteur;

	private Float kivethuf;

	private Float egyenlegusd;

	private Float egyenlegeur;

	private Float egyenleghuf;

	private Date datum;

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

	public String getPenztarosnev() {
		return penztarosnev;
	}

	public void setPenztarosnev(String penztarosnev) {
		this.penztarosnev = penztarosnev;
	}
		
	public Float getKivetusd() {
		return kivetusd;
	}

	public void setKivetusd(Float kivetusd) {
		this.kivetusd = kivetusd;
	}

	public Float getKiveteur() {
		return kiveteur;
	}

	public void setKiveteur(Float kiveteur) {
		this.kiveteur = kiveteur;
	}

	public Float getKivethuf() {
		return kivethuf;
	}

	public void setKivethuf(Float kivethuf) {
		this.kivethuf = kivethuf;
	}

	public Float getEgyenlegusd() {
		return egyenlegusd;
	}

	public void setEgyenlegusd(Float egyenlegusd) {
		this.egyenlegusd = egyenlegusd;
	}

	public Float getEgyenlegeur() {
		return egyenlegeur;
	}

	public void setEgyenlegeur(Float egyenlegeur) {
		this.egyenlegeur = egyenlegeur;
	}

	public Float getEgyenleghuf() {
		return egyenleghuf;
	}

	public void setEgyenleghuf(Float egyenleghuf) {
		this.egyenleghuf = egyenleghuf;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

}
