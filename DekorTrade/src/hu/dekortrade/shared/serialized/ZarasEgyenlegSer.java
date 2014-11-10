package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ZarasEgyenlegSer implements IsSerializable {

	private Double egyenlegusd;

	private Double egyenlegeur;

	private Double egyenleghuf;

	public Double getEgyenlegusd() {
		return egyenlegusd;
	}

	public void setEgyenlegusd(Double egyenlegusd) {
		this.egyenlegusd = egyenlegusd;
	}

	public Double getEgyenlegeur() {
		return egyenlegeur;
	}

	public void setEgyenlegeur(Double egyenlegeur) {
		this.egyenlegeur = egyenlegeur;
	}

	public Double getEgyenleghuf() {
		return egyenleghuf;
	}

	public void setEgyenleghuf(Double egyenleghuf) {
		this.egyenleghuf = egyenleghuf;
	}

}
