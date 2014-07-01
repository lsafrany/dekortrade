package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VevoKosarSer implements IsSerializable {

	private String vevo = null;

	private String vevonev = null;

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
		
}
