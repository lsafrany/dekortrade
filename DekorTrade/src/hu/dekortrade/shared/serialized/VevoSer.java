package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VevoSer implements IsSerializable {
	
	private String rovidnev;

	private String nev;

	private String cim;

	private String elerhetoseg;

	private Boolean internet;

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
	}

	public String getNev() {
		return nev;
	}

	public void setNev(String nev) {
		this.nev = nev;
	}

	public String getCim() {
		return cim;
	}

	public void setCim(String cim) {
		this.cim = cim;
	}

	public String getElerhetoseg() {
		return elerhetoseg;
	}

	public void setElerhetoseg(String elerhetoseg) {
		this.elerhetoseg = elerhetoseg;
	}

	public Boolean getInternet() {
		return internet;
	}

	public void setInternet(Boolean internet) {
		this.internet = internet;
	}

}

