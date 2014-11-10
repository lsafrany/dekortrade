package hu.dekortrade.shared.serialized;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ZarasSer implements IsSerializable {

	private String zaras;

	private String penztaros;

	private String penztarosnev;

	private Double kivetusd;

	private Double kiveteur;

	private Double kivethuf;

	private Double egyenlegusd;

	private Double egyenlegeur;

	private Double egyenleghuf;

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
		
	public Double getKivetusd() {
		return kivetusd;
	}

	public void setKivetusd(Double kivetusd) {
		this.kivetusd = kivetusd;
	}

	public Double getKiveteur() {
		return kiveteur;
	}

	public void setKiveteur(Double kiveteur) {
		this.kiveteur = kiveteur;
	}

	public Double getKivethuf() {
		return kivethuf;
	}

	public void setKivethuf(Double kivethuf) {
		this.kivethuf = kivethuf;
	}

	public Double getEgyenlegusd() {
		return egyenlegusd;
	}

	public void setEgyenlegusd(Double egyenlegusd) {
		this.egyenlegusd = egyenlegusd;
	}

	public Double getEgyenlegeur() {
		return egyenlegeur;
	}

	public void setEgyenlegeur(Double egyenlegeur) {
		this.egyenlegeur = egyenlegeur;
	}

	public Double getEgyenleghuf() {
		return egyenleghuf;
	}

	public void setEgyenleghuf(Double egyenleghuf) {
		this.egyenleghuf = egyenleghuf;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

}
