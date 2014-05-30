package hu.dekortrade.shared.serialized;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CikkSelectsSer implements IsSerializable {

	LinkedHashMap<String, LinkedHashMap<String, String>> tipus = new LinkedHashMap<String, LinkedHashMap<String, String>>();

	LinkedHashMap<String, String> fotipus = new LinkedHashMap<String, String>();

	LinkedHashMap<String, String> altipus = new LinkedHashMap<String, String>();

	LinkedHashMap<String, String> gyarto = new LinkedHashMap<String, String>();

	public LinkedHashMap<String, LinkedHashMap<String, String>> getTipus() {
		return tipus;
	}

	public void setTipus(
			LinkedHashMap<String, LinkedHashMap<String, String>> tipus) {
		this.tipus = tipus;
	}

	public LinkedHashMap<String, String> getFotipus() {
		return fotipus;
	}

	public void setFotipus(LinkedHashMap<String, String> fotipus) {
		this.fotipus = fotipus;
	}

	public LinkedHashMap<String, String> getAltipus() {
		return altipus;
	}

	public void setAltipus(LinkedHashMap<String, String> altipus) {
		this.altipus = altipus;
	}

	public LinkedHashMap<String, String> getGyarto() {
		return gyarto;
	}

	public void setGyarto(LinkedHashMap<String, String> gyarto) {
		this.gyarto = gyarto;
	}

}
