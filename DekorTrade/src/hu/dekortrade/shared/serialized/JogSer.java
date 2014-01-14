package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class JogSer implements IsSerializable {
	
	private String rovidnev;

	private String jog;

	private String nev;
	
	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
	}

	public String getJog() {
		return jog;
	}

	public void setJog(String jog) {
		this.jog = jog;
	}

	public String getNev() {
		return nev;
	}

	public void setNev(String nev) {
		this.nev = nev;
	}
	
}

