package hu.dekortrade99.server.sync;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VevoSzinkron implements IsSerializable {

	private String rovidnev;

	private String nev;

	private Boolean torolt;

	public VevoSzinkron(String rovidnev, String nev, Boolean torolt) {
		this.rovidnev = rovidnev;
		this.nev = nev;
		this.torolt = torolt;
	}

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

	public Boolean getTorolt() {
		return torolt;
	}

	public void setTorolt(Boolean torolt) {
		this.torolt = torolt;
	}
	
}
