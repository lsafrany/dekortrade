package hu.dekortrade.client;

import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.SzallitoSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoSer;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DekorTradeServiceAsync {
	
	void getUser(String user, String password, AsyncCallback<UserSer> callback)
			throws IllegalArgumentException;

	void setPassword(String user, String password,
			@SuppressWarnings("rawtypes") AsyncCallback callback)
			throws IllegalArgumentException;

	void getJog(String rovidnev,AsyncCallback<List<JogSer>> callback)
			throws IllegalArgumentException;

	void getFelhasznalo(AsyncCallback<List<FelhasznaloSer>> callback)
			throws IllegalArgumentException;

	void getSzallito(AsyncCallback<List<SzallitoSer>> callback)
			throws IllegalArgumentException;

	void getVevo(AsyncCallback<List<VevoSer>> callback)
			throws IllegalArgumentException;

}
