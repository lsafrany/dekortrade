package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SQLExceptionSer extends Exception implements IsSerializable {

	String message;

	public SQLExceptionSer() {
	}

	public SQLExceptionSer(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
