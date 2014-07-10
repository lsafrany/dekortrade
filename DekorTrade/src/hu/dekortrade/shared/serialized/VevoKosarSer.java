package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VevoKosarSer implements IsSerializable {

	private String cedula = null;
	
	private String vevo = null;

	private String vevonev = null;

	private String vevotipus = null;
	
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

	public String getVevotipus() {
		return vevotipus;
	}

	public void setVevotipus(String vevotipus) {
		this.vevotipus = vevotipus;
	}
		
}
