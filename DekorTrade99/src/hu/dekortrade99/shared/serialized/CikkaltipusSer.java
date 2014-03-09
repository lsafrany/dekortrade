package hu.dekortrade99.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CikkaltipusSer implements IsSerializable {

	private String fokod;
	
	private String kod;
	
	private String nev;
	
	private String kep;
	
	public String getFokod() {
		return fokod;
	}

	public void setFokod(String fokod) {
		this.fokod = fokod;
	}

	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}

	public String getNev() {
		return nev;
	}

	public void setNev(String nev) {
		this.nev = nev;
	}

	public String getKep() {
		return kep;
	}

	public void setKep(String kep) {
		this.kep = kep;
	}

}