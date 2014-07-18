package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ZarasEgyenlegSer implements IsSerializable {

	private Float egyenlegusd;

	private Float egyenlegeur;

	private Float egyenleghuf;

	public Float getEgyenlegusd() {
		return egyenlegusd;
	}

	public void setEgyenlegusd(Float egyenlegusd) {
		this.egyenlegusd = egyenlegusd;
	}

	public Float getEgyenlegeur() {
		return egyenlegeur;
	}

	public void setEgyenlegeur(Float egyenlegeur) {
		this.egyenlegeur = egyenlegeur;
	}

	public Float getEgyenleghuf() {
		return egyenleghuf;
	}

	public void setEgyenleghuf(Float egyenleghuf) {
		this.egyenleghuf = egyenleghuf;
	}

}
