package hu.dekortrade99.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RendeltSer implements IsSerializable {

	private String rovidnev;

	private String rendeles;

	private String datum;

	private String statusz;

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

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getStatusz() {
		return statusz;
	}

	public void setStatusz(String statusz) {
		this.statusz = statusz;
	}

}
