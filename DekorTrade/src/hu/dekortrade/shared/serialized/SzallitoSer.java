package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SzallitoSer implements IsSerializable {
	
	private String kod;

	private String nev;

	private String cim;

	private String elerhetoseg;

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

}

