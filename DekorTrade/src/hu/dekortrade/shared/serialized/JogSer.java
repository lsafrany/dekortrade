package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class JogSer implements IsSerializable {
	
	private String nev;

	private Boolean jog;

	public String getNev() {
		return nev;
	}

	public void setNev(String nev) {
		this.nev = nev;
	}

	public Boolean getJog() {
		return jog;
	}

	public void setJog(Boolean jog) {
		this.jog = jog;
	}
	
}

