package hu.dekortrade.shared.serialized;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ZarasSer implements IsSerializable {

	private String zaras;

	private String penztaros;

	private String penztarosnev;

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

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

}
